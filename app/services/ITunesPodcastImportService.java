package services;

import com.fasterxml.jackson.databind.JsonNode;
import models.classification.Genre;
import models.content.Show;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import play.Logger;
import play.libs.ws.*;
import play.libs.F.Function;
import play.libs.F.Promise;
import java.util.ArrayList;
import java.util.List;

/**
 * The iTunes Podcast Import Service queries iTunes Search API for podcasts
 * and automatically imports them to the database.
 *
 * Search API Documentation: https://www.apple.com/itunes/affiliates/resources/documentation/itunes-store-web-service-search-api.html#searching
 * Example Query: https://itunes.apple.com/us/rss/toppodcasts/genre=1311/limit=40/json
 *
 * Then once the ID is sourced, the RSS needs to be parsed from the ID using this method:
 * http://stackoverflow.com/questions/2816881/get-the-latest-podcasts-from-itunes-store-with-link-by-rss-json-or-something
 *
 * @author Michael Myers
 * @since 2015-01-15
 */
public class ITunesPodcastImportService {

    public static Promise<List<String>> getAppleIdsFromGenre(Genre genre, int number) {

        String url = new StringBuilder("https://itunes.apple.com/us/rss/toppodcasts/genre=")
                .append(genre.appleGenreId)
                .append("/limit=")
                .append(number)
                .append("/json")
                .toString();

        Logger.debug("Retrieving Apple IDs from " + url);

        return WS.url(url).get().map(
                new Function<WSResponse, List<String>>() {

                    public List<String> apply(WSResponse response) {

                        ArrayList<String> idsList = new ArrayList<String>();

                        JsonNode json = response.asJson();

                        List<JsonNode> idsJsonList = json.get("feed").get("entry").findValues("id");

                        for (JsonNode jsonNode : idsJsonList) {
                            String id = jsonNode.findValue("im:id").asText();
                            idsList.add(id);
                        }

                        return idsList;
                    }
                }
        );
    }

    public static Promise<String> getShowRssUrlFromAppleId(String appleId) {

        Logger.debug("Retrieve RSS URL from " + appleId);

        String url = new StringBuilder("https://buy.itunes.apple.com/WebObjects/MZFinance.woa/wa/com.apple.jingle.app.finance.DirectAction/subscribePodcast?id=")
                .append(appleId).append("&wasWarnedAboutPodcasts=true").toString();

        Logger.debug(url);

        return WS.url(url).setHeader("user-agent", "iTunes/7.4.1").get().map(
                new Function<WSResponse, String>() {

                    public String apply(WSResponse response) {

                        String rssUrl = null;

                        if (response.getStatus() != 200) {
                            Logger.warn("Response was not 200");
                            return null;
                        }

                        if (response.getBody().isEmpty()) {
                            Logger.warn("Body was empty");
                            return null;
                        }

                        Element documentElement = response.asXml().getDocumentElement();

                        Node dictNode = documentElement.getElementsByTagName("dict").item(1);

                        NodeList nodes = dictNode.getChildNodes();

                        for (int i = 0; i < nodes.getLength(); i++) {

                            Node node = nodes.item(i);

                            if(node.getNodeType() == Node.ELEMENT_NODE) {

                                if (node.getTextContent().equals("feedURL")) {
                                    rssUrl = nodes.item(i + 1).getTextContent();
                                    break;
                                }
                            }
                        }

                        return rssUrl;
                    }
                }
        );
    }

    public static void importGenre(Genre genre, int number) {

        List<String> appleIds = ITunesPodcastImportService.getAppleIdsFromGenre(genre, number).get(20000);

        for (String id : appleIds) {
            Logger.debug("Getting rss for id " + id);
            String rssUrl = ITunesPodcastImportService.getShowRssUrlFromAppleId(id).get(20000);
            Logger.debug("getting show from " + rssUrl);
            Show show = RSSFeedService.importShowFromRSSFeed(rssUrl).get(20000);
        }
    }

    public static void importGenreOnBackgroundThread(final Genre genre,final int number) {
        Thread importThread = new Thread(new Runnable() {
            @Override
            public void run() {

                ITunesPodcastImportService.importGenre(genre, number);

            }
        });

        importThread.start();

    }

    public static void importAllGenre(int number) {

        List<Genre> genres = Genre.getGenresWithAppleId();

        for (Genre genre : genres) {
            ITunesPodcastImportService.importGenre(genre, number);
        }
    }

    public static void importAllGenresOnBackgroundThread(final int number) {

        Thread importThread = new Thread(new Runnable() {
            @Override
            public void run() {

                ITunesPodcastImportService.importAllGenre(number);

            }
        });

        importThread.start();
    }

}
