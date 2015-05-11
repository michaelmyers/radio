package services;

import models.classification.Tag;
import models.content.Episode;
import models.classification.Genre;
import models.content.Network;
import models.content.Show;
import org.junit.Ignore;
import org.junit.Test;
import play.Logger;
import utils.DevDataUtil;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;


public class RSSFeedServiceTest {

    @Test
    @Ignore
    public void testImportShowFromRSSFeedSerial() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://feeds.serialpodcast.org/serialpodcast";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show.getName()).isEqualTo("Serial");
                assertThat(show.getLanguageCode()).isEqualTo("en");
                assertThat(show.rssFeedUrl).isEqualTo(feedUrl);
                assertThat(show.rightsLabel).isEqualTo("Copyright 2015 Chicago Public Media and Ira Glass");
                assertThat(show.websiteUrl).isEqualTo("http://serialpodcast.org");
                assertThat(show.description).isNotNull();
                assertThat(show.imageUrl).isEqualTo("http://serialpodcast.org/sites/all/modules/custom/serial/img/serial-itunes-logo.png");
                assertThat(show.getGenres()).contains(Genre.getGenreByName("News & Politics"));
                assertThat(show.network).isEqualTo(Network.getNetworkByName("This American Life"));

                List<Episode> episodes = Episode.getEpisodesByShow(show);
                assertThat(episodes.size()).isEqualTo(12);
            }
        });
    }

    @Test
    @Ignore
    public void testImportShowFromRSSFeed99PI() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                //99% Invisible has the following unique characteristics

                DevDataUtil.loadITunesGenres();

                Tag tag = new Tag("design");
                tag.save();

                String feedUrl = "http://feeds.99percentinvisible.org/99percentinvisible";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show.getName()).isEqualTo("99% Invisible");
                assertThat(show.getLanguageCode()).isEqualTo("en-us");
                assertThat(show.rssFeedUrl).isEqualTo(feedUrl);
                assertThat(show.rightsLabel).isEqualTo("Copyright © 2014 Roman Mars. All rights reserved.");
                assertThat(show.websiteUrl).isEqualTo("http://99percentinvisible.prx.org");
                assertThat(show.description).isNotNull();
                assertThat(show.imageUrl).isEqualTo("http://cdn.99percentinvisible.org/wp-content/uploads/powerpress/99invisible-logo-1400.jpg");
                assertThat(show.getGenres()).containsOnly(Genre.getGenreByName("Design"));
                assertThat(show.network).isEqualTo(Network.getNetworkByName("Roman Mars"));

                List<Episode> episodes = Episode.getEpisodesByShow(show);
                assertThat(episodes.size()).isEqualTo(100);

                List<Episode> tagEpisodes = Episode.getEpisodesByTag(tag);
                assertThat(tagEpisodes).isNotEmpty();
            }
        });
    }

    //also has categories on each episode that look like tags.

    @Test
    public void testImportShowFromRSSFeedHowToStartAStartup() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://www.howtostartastartup.co/category/podcast/feed/";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show.getName()).isEqualTo("How To Start A Startup");
                assertThat(show.getLanguageCode()).isEqualTo("en-us");
                assertThat(show.rssFeedUrl).isEqualTo(feedUrl);
                assertThat(show.rightsLabel).isNull();
                assertThat(show.websiteUrl).isEqualTo("http://howtostartastartup.co");
                assertThat(show.description).isNotNull();
                assertThat(show.imageUrl).isEqualTo("http://howtostartastartup.co/wp-content/uploads/powerpress/HTSASiTunes1.jpg");
                assertThat(show.getGenres()).containsOnly(Genre.getGenreByName("Education"), Genre.getGenreByName("Higher Education"), Genre.getGenreByName("Business"), Genre.getGenreByName("Business News"));
                assertThat(show.network).isEqualTo(Network.getNetworkByName("How To Start A Startup"));

                List<Episode> episodes = Episode.getEpisodesByShow(show);
                assertThat(episodes.size()).isEqualTo(21);
            }
        });
    }

    @Test
    public void testImportShowFromRSSFeedTheDaveRamseyShow() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                Tag tag = new Tag("Dave Ramsey");
                tag.save();

                String feedUrl = "http://www.daveramsey.com/media/audio/podcast/podcast_itunes.xml";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show.getName()).isEqualTo("The Dave Ramsey Show");
                assertThat(show.getLanguageCode()).isEqualTo("en-us");
                assertThat(show.rssFeedUrl).isEqualTo(feedUrl);
                assertThat(show.rightsLabel).isEqualTo("Copyright 2015");
                assertThat(show.websiteUrl).isEqualTo("http://www.daveramsey.com?ectid=itunes");
                assertThat(show.description).isNotNull();
                assertThat(show.imageUrl).isEqualTo("http://a248.e.akamai.net/f/1611/23575/9h/dramsey.download.akamai.com/23572/daveramsey.com/media/image/general/itunes_img_640px.jpg");
                assertThat(show.getGenres()).containsOnly(Genre.getGenreByName("Health"), Genre.getGenreByName("Self-Help"), Genre.getGenreByName("Business"), Genre.getGenreByName("Investing"), Genre.getGenreByName("Kids & Family"));
                assertThat(show.network).isEqualTo(Network.getNetworkByName("Dave Ramsey"));

                List<Episode> episodes = Episode.getEpisodesByShow(show);
                assertThat(episodes.size()).isEqualTo(3);

                List<Episode> tagEpisodes = Episode.getEpisodesByTag(tag);
                assertThat(tagEpisodes).isNotEmpty();
            }
        });
    }

    @Test
    public void testImportShowFromRSSFeedTheAltonBrowncast() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();


                String feedUrl = "http://altonbrown.com/feed/podcast/";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show.getName()).isEqualTo("The Alton Browncast");
                assertThat(show.getLanguageCode()).isEqualTo("en-us");
                assertThat(show.rssFeedUrl).isEqualTo(feedUrl);
                assertThat(show.rightsLabel).isEqualTo("© Alton Brown");
                assertThat(show.websiteUrl).isEqualTo("http://altonbrown.com");
                assertThat(show.description).isNotNull();
                assertThat(show.imageUrl).isEqualTo("http://altonbrown.com/wp-content/uploads/2014/10/new-alton-browncast-1400x1400.jpg");
                assertThat(show.getGenres()).contains(Genre.getGenreByName("Food"));
                assertThat(show.network).isEqualTo(Network.getNetworkByName("The Alton Browncast"));

                List<Episode> episodes = Episode.getEpisodesByShow(show);
                assertThat(episodes.size()).isEqualTo(10);
            }
        });
    }

    //this one has different type of encoding in the header
    //see http://stackoverflow.com/questions/15545720/how-to-fix-invalid-byte-1-of-1-byte-utf-8-sequence
    //http://stackoverflow.com/questions/3482494/howto-let-the-sax-parser-determine-the-encoding-from-the-xml-declaration

    @Test
    public void testImportShowFromRSSFeedHelpingWritersBecomeAuthors() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://www.kmweiland.com/wp-content/podcast/podcast-rss.xml";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show.getName()).isEqualTo("Helping Writers Become Authors");
                assertThat(show.getLanguageCode()).isEqualTo("en-us");
                assertThat(show.rssFeedUrl).isEqualTo(feedUrl);
                assertThat(show.rightsLabel).isEqualTo("℗ & © 2009 K.M. Weiland");
                assertThat(show.websiteUrl).isEqualTo("http://www.helpingwritersbecomeauthors.com");
                assertThat(show.description).isNotNull();
                assertThat(show.imageUrl).isEqualTo("http://www.helpingwritersbecomeauthors.com/wp-content/uploads/2013/11/podcast-logo.jpg");
                assertThat(show.getGenres()).contains(Genre.getGenreByName("Literature"));
                assertThat(show.network).isEqualTo(Network.getNetworkByName("K.M. Weiland"));

                List<Episode> episodes = Episode.getEpisodesByShow(show);
                assertThat(episodes.size()).isGreaterThan(0);

            }
        });
    }

    /**
     * Tests where there is no Itunes Image.
     */
    @Test
    public void testImportShowFromRSSFeedLibriVox() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "https://librivox.org/rss/5267";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show).isNotNull();
            }
        });
    }

    /**
     * This doesn't have an overall ITUNES:AUTHOR so the network doesn't get made
     * It has authors on each episode....
     */
    @Test
    public void testImportShowFromRSSFeedPRIArtsAndEntertainment() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://feeds.feedburner.com/pri/arts";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show).isNotNull();
            }
        });
    }

    @Test
    public void testImportShowFromRSSFeedTheSeanachaiEpisodes() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://feeds.feedburner.com/TheSeanachaiEpisodes";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show).isNotNull();
            }
        });
    }

    @Test
    public void testImportShowFromRSSFeedComicGeekSquad() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://www.comicgeekspeak.com/cgs-rss.xml";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show).isNotNull();
            }
        });
    }

    @Test
    public void testImportShowFromRSSFeedVictoriaSecret() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://feeds.feedburner.com/victoriassecretpodcast";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show).isNotNull();
            }
        });
    }

    @Test
    public void testImportShowFromRSSFeedGanesha() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://www.vanamaliashram.org/Ganesha/Ganesha/rss.xml";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show).isNotNull();
            }
        });
    }

    @Test
    public void testImportShowFromRSSFeedSkeptoid() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://skeptoid.com/podcast.xml";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show).isNotNull();
            }
        });
    }

    //http://www.blogtalkradio.com/thethinkingatheist/podcast
    //error: Content is not allowed in prolog.
    @Test
    public void testImportShowFromRSSFeedTheThinkingAtheist() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://www.blogtalkradio.com/thethinkingatheist/podcast";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show).isNotNull();
            }
        });
    }

    @Test
    public void testImportShowFromRSSFeedRivals() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "https://www2.rivals.com/podcastshow.asp";//?";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show).isNotNull();
            }
        });
    }

    @Test
    public void testImportShowFromRSSFeedAVWeb() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://www.avweb.com/podcast/?zkDo=showRSS";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show).isNotNull();
            }
        });
    }

    @Test
    @Ignore
    public void testImportShowFromRSSThersa() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://www.thersa.org/rss/rsa-audio";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show).isNotNull();
            }
        });
    }

    //Sesame has the genre as textContext on the element and not as an attribute
    @Test
    public void testImportShowFromRSSSesame() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://downloads.cdn.sesame.org/podcast/rss/rss.xml";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show).isNotNull();
                assertThat(show.getGenres()).isNotEmpty();
                assertThat(show.rssFeedUrl).isEqualTo("http://downloads.cdn.sesame.org/podcast/rss/rss.xml");
            }
        });
    }

    @Test
    public void testImportShowFromRSSFeedMeditationOasisDoubleImport() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String feedUrl = "http://www.heartofhealing.net/podcast/MO_RSS_feed.xml";

                Show show = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                assertThat(show).isNotNull();

                List<Episode> episodes = Episode.getEpisodesByShow(show);

                Show show2 = RSSFeedService.importShowFromRSSFeed(feedUrl).get(20000);

                List<Episode> episodes2 = Episode.getEpisodesByShow(show2);

                //Make sure it is the same as before!
                assertThat(episodes2.size()).isEqualTo(episodes.size());
                assertThat(Show.getNumberOfDistinctShows()).isEqualTo(1);
            }
        });
    }
}
