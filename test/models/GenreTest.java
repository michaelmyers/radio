package models;

import models.classification.Genre;
import org.junit.Test;
import utils.DevDataUtil;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class GenreTest {

    @Test
    public void testCreate() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadTestData();

                Genre genre = new Genre("Test Genre");
                genre.save();

                assertThat(genre).isNotNull();
                assertThat(genre.id).isNotNull();
                assertThat(genre.creationDate).isNotNull();
                assertThat(genre.createdBy).isNull();
                assertThat(genre.description).isNull();

                Genre genreWithUser = new Genre("Test Genre with User", DevDataUtil.user1);
                genreWithUser.save();

                assertThat(genreWithUser).isNotNull();
                assertThat(genreWithUser.id).isNotNull();
                assertThat(genreWithUser.createdBy).isEqualTo(DevDataUtil.user1);


                assertThat(Genre.getGenre(genre.id)).isNotNull();
                assertThat(Genre.getGenre(genreWithUser.id)).isNotNull();

                assertThat(Genre.getGenres().size()).isEqualTo(2);

            }
        });
    }

    @Test
    public void testSubgenre() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Genre parentGenre = new Genre("Parent Genre");
                parentGenre.save();

                Genre childGenre = new Genre("Child Genre");
                childGenre.parentGenre = parentGenre;
                childGenre.save();

                assertThat(childGenre.isSubgenre()).isTrue();
                assertThat(childGenre.parentGenre).isEqualTo(parentGenre);
            }
        });
    }

    @Test
    public void testDeleteGenre() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Genre genre = new Genre("Test Genre");
                genre.save();

                Long genreId = genre.id;

                assertThat(genre).isNotNull();

                Genre.deleteGenre(genreId);

                assertThat(Genre.getGenre(genreId)).isNull();
                assertThat(Genre.getGenres().size()).isEqualTo(0);

            }
        });
    }

    @Test
    public void testGetSubgenres() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Genre parentGenre = new Genre("Parent Genre");
                parentGenre.save();

                Genre childGenre1 = new Genre("Child Genre 1");
                childGenre1.parentGenre = parentGenre;
                childGenre1.save();

                Genre childGenre2 = new Genre("Child Genre 2");
                childGenre2.parentGenre = parentGenre;
                childGenre2.save();

                assertThat(Genre.getSubgenres(parentGenre).size()).isEqualTo(2);
            }
        });
    }

    @Test
    public void testGetGenreByAppleGenreId() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Long appleGenreId = (long) 1234;

                Genre genre = new Genre("Test Genre Id");
                genre.appleGenreId = appleGenreId;
                genre.save();

                assertThat(Genre.getGenreByAppleId(appleGenreId)).isEqualTo(genre);
            }
        });
    }

    @Test
    public void testGetGenresWithAppleGenreId() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Genre genre1 = new Genre("Test One");
                genre1.appleGenreId = (long) 1234;
                genre1.save();

                Genre genre2 = new Genre("Test Two");
                genre2.appleGenreId = (long) 1235;
                genre2.save();

                assertThat(Genre.getGenresWithAppleId().size()).isEqualTo(2);
            }
        });
    }

    @Test
    public void testGetGenreByName() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Genre genre1 = new Genre("Test One");
                genre1.save();

                Genre genre2 = new Genre("Test Two");
                genre2.save();

                assertThat(Genre.getGenreByName(genre1.name)).isEqualTo(genre1);

            }
        });
    }

    @Test
    public void testGetITunesGenreByName() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadITunesGenres();

                String newsAndPolitics = "News & Politics";

                assertThat(Genre.getGenreByName(newsAndPolitics).name).isEqualTo(newsAndPolitics);

            }
        });
    }
}
