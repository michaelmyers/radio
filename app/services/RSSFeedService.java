package services;

import models.classification.Tag;
import models.content.Episode;
import models.classification.Genre;
import models.content.Network;
import models.content.Show;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import play.Logger;
import play.libs.ws.*;
import play.libs.F.Function;
import play.libs.F.Promise;
import utils.DateUtil;
import utils.StringUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;

/**
 *
 * Apple Podcast Specs: https://www.apple.com/itunes/podcasts/specs.html
 *
 */
public class RSSFeedService {

    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CATEGORY = "category";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";
    static final String ENCLOSURE = "enclosure";
    static final String ITUNES_CATEGORY = "itunes:category";
    static final String ITUNES_IMAGE = "itunes:image";
    static final String ITUNES_EXPLICIT = "itunes:explicit";
    static final String ITUNES_AUTHOR = "itunes:author";
    static final String ITUNES_DESCRIPTION = "itunes:summary";
    static final String ITUNES_DURATION = "itunes:duration";
    static final String ITUNES_NEW_FEED = "itunes:new-feed-url";

    /**
     * Imports data from the provided RSS feed.
     * Either creates a new Show or updates and existing.
     *
     * @param url The RSS feed URL to be parsed
     * @return Promise<Show>  Returns a promise for a show or null if there was an error
     */
    public static Promise<Show> importShowFromRSSFeed(String url) {

        Logger.debug("Importing from " + url);

        if (url == null) {
            Logger.error("URL was null, can't import");
            return null;
        }

        Promise<Show> showPromise = WS.url(url).get().map(
                new Function<WSResponse, Show>() {

                    public Show apply(WSResponse response) {

                        //Instead of using response.asXML(); this method handles encoding differences more elegantly
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        factory.setNamespaceAware(true);

                        Document xmlDocument;

                        try {
                            DocumentBuilder builder = factory.newDocumentBuilder();

                            //Trim bad characters
                            //http://stackoverflow.com/a/3030913/1349766
                            String body = response.getBody();
                            body = body.trim().replaceFirst("^([\\W]+)<","<");

                            xmlDocument = builder.parse(new InputSource(new StringReader(body)));

                        } catch (ParserConfigurationException | SAXException | IOException e) {
                            Logger.error("Error parsing RSS Feed XML", e);
                            return null;
                        }

                        Element documentElement = xmlDocument.getDocumentElement();

                        String rssFeedUrl = response.getUri().toASCIIString();
                        String name = documentElement.getElementsByTagName(TITLE).item(0).getTextContent();

                        Show show = Show.getShowByNameAndRSSFeedURL(name, rssFeedUrl);

                        if (show == null) {
                            show = new Show(name,rssFeedUrl);
                        }

                        //Get the easy stuff
                        if (documentElement.getElementsByTagName(LANGUAGE).getLength() > 0) {
                            show.setLanguageCode(documentElement.getElementsByTagName(LANGUAGE).item(0).getTextContent());
                        }

                        if (documentElement.getElementsByTagName(LINK).getLength() > 0) {
                            show.websiteUrl = documentElement.getElementsByTagName(LINK).item(0).getTextContent();
                        }

                        if (documentElement.getElementsByTagName(COPYRIGHT).getLength() > 0) {
                            show.rightsLabel = documentElement.getElementsByTagName(COPYRIGHT).item(0).getTextContent();
                        }

                        if (documentElement.getElementsByTagName(DESCRIPTION).getLength() > 0) {
                            show.description = documentElement.getElementsByTagName(DESCRIPTION).item(0).getTextContent();
                        }

                        if (documentElement.getElementsByTagName(ITUNES_EXPLICIT).getLength() > 0) {
                            String explicit = documentElement.getElementsByTagName(ITUNES_EXPLICIT).item(0).getTextContent();

                            if (explicit.equalsIgnoreCase("yes")) {
                                show.explicit = true;
                            }
                        }

                        if (documentElement.getElementsByTagName(ITUNES_NEW_FEED).getLength() > 0) {
                            show.rssFeedUrl = documentElement.getElementsByTagName(ITUNES_NEW_FEED).item(0).getTextContent();
                        }

                        if (documentElement.getElementsByTagName(ITUNES_IMAGE).getLength() > 0) {
                            //Get the image, it lives as an attribute
                            show.imageUrl = documentElement
                                    .getElementsByTagName(ITUNES_IMAGE).item(0)
                                    .getAttributes().getNamedItem("href").getNodeValue();
                        }

                        if (documentElement.getElementsByTagName(ITUNES_CATEGORY).getLength() > 0) {

                            //We want the last genre as it is the most specific
                            NodeList categoryNodes = documentElement.getElementsByTagName(ITUNES_CATEGORY);

                            for (int j=0; j < categoryNodes.getLength(); j++) {

                                String genre = null;

                                Node category = categoryNodes.item(j);

                                if (category.getAttributes().getNamedItem("text") != null) {
                                    genre = category.getAttributes().getNamedItem("text").getNodeValue();
                                } else if (!category.getTextContent().isEmpty()) {
                                    genre = category.getTextContent();
                                }

                                //Make sure we go something for the genre
                                if (genre != null) {
                                    //See if there is a Genre already by that name
                                    if (Genre.getGenreByName(genre) != null) {
                                        show.addGenre(Genre.getGenreByName(genre));
                                    } else { //Otherwise, make a new one
                                        Genre newGenre = new Genre(genre);
                                        newGenre.save();
                                        show.addGenre(newGenre);
                                    }
                                }
                            }
                        }

                        //If there is an ITUNES_AUTHOR, the first one will be the Network of the show
                        if (documentElement.getElementsByTagName(ITUNES_AUTHOR).getLength() > 0) {
                            show.setAuthor(documentElement.getElementsByTagName(ITUNES_AUTHOR).item(0).getTextContent());

                            if (Network.getNetworkByName(show.getAuthor()) != null) {

                                show.network = Network.getNetworkByName(show.getAuthor());

                            } else {

                                Logger.debug("Creating new Publisher " + show.getAuthor());

                                Network newNetwork = new Network(show.getAuthor());
                                newNetwork.save();

                                show.network = newNetwork;
                            }
                        }

                        Logger.debug("Saving show " + show.getName());

                        show.save();
                        show.saveManyToManyAssociations("genres");

                        //Show is saved, now parse the episodes and add them to the show
                        NodeList episodeList = xmlDocument.getElementsByTagName(ITEM);
                        Logger.debug("Adding " + Integer.toString(episodeList.getLength()) + " episodes to " + show.getName());

                        for (int i=0; i < episodeList.getLength(); i++) {

                            NodeList episodeNodes = episodeList.item(i).getChildNodes();

                            Episode episode = new Episode();

                            for (int j=0; j < episodeNodes.getLength(); j++) {

                                Node episodeNode = episodeNodes.item(j);

                                if (episodeNode.getNodeType() == Node.ELEMENT_NODE) {

                                    String elementName = episodeNode.getNodeName();

                                    //Parse through
                                    if (elementName.equals(TITLE)) {
                                        episode.setName(episodeNode.getTextContent());
                                    } else if (elementName.equals(AUTHOR)) {
                                        episode.setAuthor(episodeNode.getTextContent());
                                    }else if (elementName.equals(ITUNES_AUTHOR)) {
                                        episode.setAuthor(episodeNode.getTextContent());
                                    } else if (elementName.equals(LINK)) {
                                        episode.link = episodeNode.getTextContent();
                                    } else if (elementName.equals(PUB_DATE)) {
                                        //If it is not an empty string.
                                        if (!episodeNode.getTextContent().isEmpty()) {
                                            try {
                                                episode.publishedDate = DateUtil.parseRSS(episodeNode.getTextContent());
                                            } catch (ParseException e) {
                                                Logger.error("Error parsing published date", e);
                                            }
                                        }
                                    } else if (elementName.equals(ITUNES_DESCRIPTION)) {
                                        episode.description = episodeNode.getTextContent();
                                    } else if (elementName.equals(ITUNES_EXPLICIT)) {
                                        if (episodeNode.getTextContent().equalsIgnoreCase("yes")) {
                                            episode.explicit = true;
                                        }
                                    } else if (elementName.equals(ENCLOSURE)) {
                                        episode.audioUrl = episodeNode.getAttributes().getNamedItem("url").getNodeValue();
                                    } else if (elementName.equals(CATEGORY)) {
                                        if (episodeNode.getFirstChild() instanceof CharacterData) {
                                            CharacterData characterData = (CharacterData) episodeNode.getFirstChild();
                                            String tagName = characterData.getTextContent();

                                            //TODO: move this to episode.addTag method
                                            Tag tag = Tag.getTagByName(tagName);

                                            if (tag == null) {
                                                tag = new Tag(tagName);
                                                tag.save();
                                            }

                                            episode.addTag(tag);
                                        }
                                    } else if (elementName.equals(ITUNES_DURATION)) {

                                        //TODO: Move this to episode, setDurationWithString method
                                        String durationString = episodeNode.getTextContent().replace(",", "");

                                        if (!durationString.isEmpty()) {

                                            try {
                                                int occurrencesOfColon = StringUtil.countOccurrencesOf(durationString, ":");

                                                if (occurrencesOfColon == 0) {
                                                    //Just seconds
                                                    episode.duration = Long.parseLong(durationString.trim());
                                                } else if (occurrencesOfColon == 1) {

                                                    String[]durationParts = durationString.split(":");
                                                    int minutes = Integer.parseInt(durationParts[0].trim());
                                                    int seconds = Integer.parseInt(durationParts[1].trim());

                                                    episode.duration = Long.valueOf(DateUtil.addHoursMinutesSeconds(0, minutes, seconds));

                                                } else if (occurrencesOfColon == 2) {

                                                    String[]durationParts = durationString.split(":");
                                                    int hours = Integer.parseInt(durationParts[0].trim());
                                                    int minutes = Integer.parseInt(durationParts[1].trim());
                                                    int seconds = Integer.parseInt(durationParts[2].trim());

                                                    episode.duration = Long.valueOf(DateUtil.addHoursMinutesSeconds(hours, minutes, seconds));
                                                }

                                            } catch (NumberFormatException e) {
                                                Logger.warn("Error parsing duration ", e);
                                            }
                                        }
                                    }
                                }
                            }

                            if (Episode.getEpisodeByNameAndShow(episode.getName(), show) == null) {
                                //show doesn't exist, save it
                                episode.show = show;
                                episode.save();
                                episode.saveManyToManyAssociations("tags");
                            }
                        }

                        return show;
                    }
                }
        );

        return showPromise;

    };

}
