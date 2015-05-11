package models;

import models.classification.Genre;
import models.content.Network;
import models.content.Show;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class ShowTest {

    @Test
    public void testCreate() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Network network = new Network("Test Publisher");
                network.save();

                assertThat(Network.getNetworkByName("Test Publisher")).isNotNull();

                Show show = new Show("Test Show");
                show.network = network;
                show.save();

                Show show1 = new Show("Test Show1");
                show1.network = network;
                show1.save();

                assertThat(Show.getShows().size()).isEqualTo(2);
                assertThat(Show.getShow(show.id)).isEqualTo(show);

                List<Show> shows = Show.getShowsByNetwork(network);

                assertThat(shows.size()).isEqualTo(2);
                assertThat(shows.get(0)).isEqualTo(show);

            }
        });
    }

    @Test
    public void testGetShowsByGenre() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Genre genre = new Genre("Test Genre");
                genre.save();

                Network network = new Network("Test Publisher");
                network.save();

                assertThat(Network.getNetworkByName("Test Publisher")).isNotNull();

                Show show = new Show("Test Show");
                show.network = network;
                show.addGenre(genre);
                show.save();

                Show show1 = new Show("Test Show1");
                show1.network = network;
                show1.addGenre(genre);
                show1.save();

                List<Show> shows = Show.getShowsByGenre(genre);

                assertThat(shows.size()).isEqualTo(2);
                assertThat(shows).containsOnly(show, show1);

            }
        });
    }

    @Test
    public void testGetShowByNameAndRssFeedUrl() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                String url = "http://test.url.com";
                String url1 = "http://test.url.1.com";

                Genre genre = new Genre("Test Genre");
                genre.save();

                Network network = new Network("Test Network");
                network.save();

                assertThat(Network.getNetworkByName("Test Network")).isNotNull();

                Show show = new Show("Test Show", url);
                show.network = network;
                show.addGenre(genre);
                show.save();

                Show show1 = new Show("Test Show1", url1);
                show1.network = network;
                show1.addGenre(genre);
                show1.save();

                List<Show> shows = Show.getShowsByGenre(genre);

                assertThat(shows.size()).isEqualTo(2);
                assertThat(shows).containsOnly(show1, show);

                Show showFromQuery = Show.getShowByNameAndRSSFeedURL("Test Show", url);

                assertThat(showFromQuery).isNotNull();
                assertThat(showFromQuery).isEqualTo(show);

                Show showFromBadQuery = Show.getShowByNameAndRSSFeedURL("No", url);

                assertThat(showFromBadQuery).isNull();

            }
        });
    }
}
