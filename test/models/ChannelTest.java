package models;

import models.classification.Channel;
import models.classification.Genre;
import models.content.Episode;
import models.content.Show;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class ChannelTest {

    @Test
    public void testCreate() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Episode episode1 = new Episode("Episode 1");
                episode1.save();

                Genre testGenre = new Genre("Test Genre");
                testGenre.save();

                Show testShow1 = new Show("Test Show 1");
                testShow1.save();
                Show testShow2 = new Show("Test Show 2");
                testShow2.save();
                Show testShow3 = new Show("Test Show 3");
                testShow3.save();

                List<Show> shows = new ArrayList<Show>();
                shows.add(testShow1);
                shows.add(testShow2);

                Channel testChannel = new Channel("Test Channel", testGenre, shows);

                assertThat(testChannel.getShows()).containsExactly(testShow1, testShow2);
                assertThat(testChannel.name).isEqualTo("Test Channel");
                assertThat(testChannel.genre).isEqualTo(testGenre);
                assertThat(testChannel.creationDate).isNotNull();

                testChannel.addShow(testShow3);

                assertThat(testChannel.getShows()).contains(testShow3);
            }
        });
    }

    @Test
    public void testGetPlayList() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {


                Genre testGenre = new Genre("Test Genre");
                testGenre.save();

                Show testShow1 = new Show("Test Show 1");
                testShow1.save();
                Show testShow2 = new Show("Test Show 2");
                testShow2.save();

                Show testShow3 = new Show("Test Show 3");
                testShow3.addGenre(testGenre);
                testShow3.save(); //has episode 4

                //middle
                Episode episode1 = new Episode("Episode 1");
                episode1.show = testShow1;
                episode1.publishedDate = new DateTime(2015, 1, 2, 0, 0).toDate();
                episode1.save();

                //oldest
                Episode episode2 = new Episode("Episode 2");
                episode2.show = testShow2;
                episode2.publishedDate = new DateTime(2015, 1, 1, 0, 0).toDate();
                episode2.save();

                Episode episode3 = new Episode("Episode 3");
                episode3.save();

                //newest
                Episode episode4 = new Episode("Episode 4");
                episode4.show = testShow3;
                episode4.publishedDate = new DateTime(2015, 1, 3, 0, 0).toDate();
                episode4.save();

                List<Show> shows = new ArrayList<Show>();
                shows.add(testShow1);
                shows.add(testShow2);

                Channel testChannel = new Channel("Test Channel", testGenre, shows);
                testChannel.save();

                assertThat(testChannel.getPlayList()).containsExactly(episode1, episode2, episode4);

                assertThat(testChannel.getPlayList()).containsSequence(episode4, episode1, episode2);
            }
        });
    }
}
