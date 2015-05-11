package models;

import models.classification.Genre;
import models.classification.Tag;
import models.content.Episode;
import models.content.Network;
import models.content.Show;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class EpisodeTest {

    @Test
    public void testCreate() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Show show = new Show("Test Show");
                show.save();

                Episode episode = new Episode("Test Episode");
                episode.show = show;
                episode.save();

                Episode episode1 = new Episode("Test Episode 1");
                episode1.show = show;
                episode1.save();

                assertThat(Show.getShows().size()).isEqualTo(1);
                assertThat(Episode.getEpisodes().size()).isEqualTo(2);

                List<Episode> episodes = Episode.getEpisodesByShow(show);

                assertThat(episodes.size()).isEqualTo(2);
                assertThat(episodes.get(0)).isEqualTo(episode);

                assertThat(Episode.getEpisodeByNameAndShow(episode.getName(), show)).isEqualTo(episode);
                assertThat(Episode.getEpisodeByNameAndShow("episode", show)).isNull();
            }
        });
    }

    @Test
    public void testCreateWithLongStringFields() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Show show = new Show("Test Show");
                show.save();

                String longText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris eu varius nibh. In convallis odio non metus maximus convallis. Proin vel ante efficitur, suscipit ligula nec, elementum elit. Suspendisse potenti. Integer sagittis metus nec nibh viverra tempor. Mauris efficitur, nisl in vestibulum venenatis, dui magna consequat nunc, quis tincidunt elit nunc nec nulla. Aenean blandit auctor ex, eu rutrum orci tempor et. Duis sed dui mauris. Duis auctor, nulla ullamcorper lacinia tincidunt, magna elit aliquam dolor, a fringilla purus metus faucibus est. Suspendisse potenti. Morbi pellentesque congue facilisis. Vestibulum a congue justo, nec congue turpis. Praesent dictum justo at neque pretium, nec cursus mauris sollicitudin. Fusce nisi tortor, vulputate vel commodo non, congue ac nunc. Phasellus erat ex, consequat eget metus sit amet, blandit placerat dolor. Suspendisse eget arcu aliquam, vulputate urna ac, tempor nisl.\n" +
                        "Sed id consectetur augue, pulvinar mollis mi. Nulla facilisi. Donec nec semper felis. Nulla pellentesque vitae felis id suscipit. Phasellus porta ipsum sed purus viverra rhoncus. Nullam a ligula ornare, condimentum elit a, congue purus. Sed lorem augue, sollicitudin non auctor id, dictum sed turpis.\n" +
                        "Pellentesque id facilisis ante. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Sed id leo eu odio lobortis porttitor. Nunc at elementum eros. Suspendisse potenti. Integer sed lacus et enim finibus hendrerit vitae eget lorem. Donec non rhoncus nibh. Aenean facilisis, est sed pulvinar egestas, tellus augue scelerisque ante, in tempor velit felis a urna.\n" +
                        "Ut sapien risus, pharetra a lorem eget, mattis consectetur purus. Phasellus a velit orci. Aenean varius malesuada sem eu vulputate. Proin mattis ante ac imperdiet suscipit. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nam dictum tellus scelerisque sem mollis, sed commodo sem sagittis. Sed viverra non turpis ut malesuada. Donec facilisis augue at augue eleifend.";

                Episode episode = new Episode(longText);
                episode.show = show;
                episode.setAuthor(longText);
                episode.save();

                assertThat(Episode.getEpisodes().size()).isEqualTo(1);

                List<Episode> episodes = Episode.getEpisodesByShow(show);

                assertThat(episodes.size()).isEqualTo(1);
                assertThat(episodes.get(0)).isEqualTo(episode);

                assertThat(Episode.getEpisodeByNameAndShow(episode.getName(), show)).isEqualTo(episode);
            }
        });
    }

    @Test
    public void testEpisodePagination() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Network network = new Network("Test Network");
                network.save();

                Network network1 = new Network("Test Network 1");
                network1.save();

                Genre genre = new Genre("Test Genre");
                genre.save();

                Show show = new Show("Test Show");
                show.network = network;
                show.addGenre(genre);
                show.save();

                Show show1 = new Show("Test Show 1");
                show1.network = network1;
                show1.save();

                Episode episode = new Episode("Test Episode");
                episode.show = show;
                episode.save();

                Episode episode1 = new Episode("Test Episode 1");
                episode1.show = show;
                episode1.save();

                Episode episode2 = new Episode("Test Episode 2");
                episode2.show = show;
                episode2.save();

                Episode episode3 = new Episode("Test Episode 3");
                episode3.show = show;
                episode3.save();

                Episode episode4 = new Episode("Test Episode 4");
                episode4.show = show1;
                episode4.save();

                assertThat(Episode.getEpisodes((long) 0, (long) 0, (long) 0, 0, 1, "name", "asc", "").getList().size()).isEqualTo(1);
                assertThat(Episode.getEpisodes((long) 0, (long) 0, (long) 0, 1, 1, "name", "asc", "").getList().size()).isEqualTo(1);
                assertThat(Episode.getEpisodes((long) 0, (long) 0, (long) 0, 0, 4, "name", "asc", "").getList().size()).isEqualTo(4);
                assertThat(Episode.getEpisodes((long) 0, (long) 0, (long) 0, 0, 10, "name", "asc", "Test").getList().size()).isEqualTo(5);
                assertThat(Episode.getEpisodes((long) 0, (long) 0, (long) 0, 0, 10, "name", "asc", "Episode").getList().size()).isEqualTo(5);

                assertThat(Episode.getEpisodes(network.id, (long) 0, (long) 0, 0, 5, "name", "asc", "").getList().size()).isEqualTo(4);
                assertThat(Episode.getEpisodes((long) 0, show1.id, (long) 0, 0, 5, "name", "asc", "").getList().size()).isEqualTo(1);
                assertThat(Episode.getEpisodes((long) 0, (long) 0, genre.id, 0, 5, "name", "asc", "").getList().size()).isEqualTo(4);

            }
        });
    }

    @Test
    public void testGetEpisodesByNetwork() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Network network = new Network("Test Network");
                network.save();

                Network network1 = new Network("Test Network1");
                network.save();

                Show show = new Show("Test Show");
                show.network = network;
                show.save();

                Show show1 = new Show("Test Show 1");
                show1.network = network1;
                show1.save();

                Episode episode = new Episode("Test Episode");
                episode.show = show;
                episode.save();

                Episode episode1 = new Episode("Test Episode 1");
                episode1.show = show;
                episode1.save();

                Episode episode2 = new Episode("Test Episode 2");
                episode2.show = show1;
                episode2.save();

                List<Episode> episodes = Episode.getEpisodesByNetwork(network);

                assertThat(episodes).isNotNull();
                assertThat(episodes.size()).isEqualTo(2);

                List<Episode> episodes1 = Episode.getEpisodesByNetwork(network1);

                assertThat(episodes1).isNotNull();
                assertThat(episodes1.size()).isEqualTo(1);

            }
        });
    }

    @Test
    public void testGetEpisodesByShow() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Network network = new Network("Test Network");
                network.save();

                Network network1 = new Network("Test Network1");
                network.save();

                Show show = new Show("Test Show");
                show.network = network;
                show.save();

                Show show1 = new Show("Test Show 1");
                show1.network = network1;
                show1.save();

                Episode episode = new Episode("Test Episode");
                episode.show = show;
                episode.save();

                Episode episode1 = new Episode("Test Episode 1");
                episode1.show = show;
                episode1.save();

                Episode episode2 = new Episode("Test Episode 2");
                episode2.show = show1;
                episode2.save();

                List<Episode> episodes = Episode.getEpisodesByShow(show);

                assertThat(episodes).isNotNull();
                assertThat(episodes.size()).isEqualTo(2);

                List<Episode> episodes1 = Episode.getEpisodesByShow(show1);

                assertThat(episodes1).isNotNull();
                assertThat(episodes1.size()).isEqualTo(1);

            }
        });
    }

    @Test
    public void testGetEpisodesByGenre() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Genre genre = new Genre("Test Genre");
                genre.save();

                Genre genre1 = new Genre("Test Genre 1");
                genre1.save();

                Show show = new Show("Test Show");
                show.addGenre(genre);
                show.save();

                Show show1 = new Show("Test Show 1");
                show1.addGenre(genre);
                show1.addGenre(genre1);
                show1.save();

                Show show2 = new Show("Test Show 2");
                show2.save();

                Episode episode = new Episode("Test Episode");
                episode.show = show;
                episode.save();

                Episode episode1 = new Episode("Test Episode 1");
                episode1.show = show;
                episode1.save();

                Episode episode2 = new Episode("Test Episode 2");
                episode2.show = show1;
                episode2.save();

                Episode episode3 = new Episode("Test Episode 3");
                episode3.show = show2;
                episode3.save();


                List<Episode> episodes = Episode.getEpisodesByGenre(genre);

                assertThat(episodes).isNotNull();
                assertThat(episodes.size()).isEqualTo(3);

                List<Episode> episodes1 = Episode.getEpisodesByGenre(genre1);

                assertThat(episodes1).isNotNull();
                assertThat(episodes1.size()).isEqualTo(1);

            }
        });
    }

    @Test
    public void testGetEpisodesByTag() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Tag tag = new Tag("Tag");
                tag.save();

                Tag tag1 = new Tag("Tag 1");
                tag1.save();

                Show show = new Show("Test Show");
                show.save();

                Show show1 = new Show("Test Show 1");
                show1.save();

                Episode episode = new Episode("Test Episode");
                episode.show = show;
                episode.addTag(tag);
                episode.save();

                Episode episode1 = new Episode("Test Episode 1");
                episode1.show = show;
                episode1.save();

                Episode episode2 = new Episode("Test Episode 2");
                episode2.show = show1;
                episode2.addTag(tag);
                episode2.save();

                assertThat(tag.getName()).isEqualTo("tag");
                assertThat(Tag.getTagByName("tag")).isEqualTo(tag);
                assertThat(Tag.getTags().size()).isEqualTo(2);

                List<Episode> episodes = Episode.getEpisodesByTag(tag);

                assertThat(episodes).isNotNull();
                assertThat(episodes.size()).isEqualTo(2);

            }
        });
    }

}
