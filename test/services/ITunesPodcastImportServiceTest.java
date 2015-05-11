package services;

import models.classification.Genre;
import models.content.Network;
import models.content.Show;
import org.junit.Ignore;
import org.junit.Test;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class ITunesPodcastImportServiceTest {

    @Test
    @Ignore
    public void testGetAppleIdsFromGenre() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Genre businessGenre = new Genre("Business");
                businessGenre.appleGenreId = (long) 1321;

                List<String> idList = ITunesPodcastImportService.getAppleIdsFromGenre(businessGenre, 20).get(20000);

                assertThat(idList.size()).isEqualTo(20);
            }
        });
    }

    @Test
    @Ignore
    public void testGetShowRssUrlFromAppleIdOne() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                String rssUrl = ITunesPodcastImportService.getShowRssUrlFromAppleId("217999782").get(2000);

                assertThat(rssUrl).isNotNull();
                assertThat(rssUrl).isEqualTo("http://feeds.feedburner.com/tdicasts");
            }
        });
    }

    @Test
    @Ignore
    public void testGetShowRssUrlFromAppleIdTwo() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                String rssUrl = ITunesPodcastImportService.getShowRssUrlFromAppleId("922398209").get(2000);

                assertThat(rssUrl).isNotNull();
                assertThat(rssUrl).isEqualTo("http://howtostartastartup.co/category/podcast/feed/");
            }
        });
    }

    @Test
    public void testImportGenre() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Genre businessGenre = new Genre("Business");
                businessGenre.appleGenreId = (long) 1321;
                businessGenre.save();

                ITunesPodcastImportService.importGenre(businessGenre, 5);

                assertThat(Network.getNetworks().size()).isNotEqualTo(0);
                assertThat(Show.getShows().size()).isEqualTo(5);
                //assertThat(Show.getShowsByGenre(businessGenre).size()).isEqualTo(5);
            }
        });
    }
}
