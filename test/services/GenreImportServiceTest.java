package services;

import models.classification.Genre;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class GenreImportServiceTest {

    @Test
    public void testITunesGenreImportService() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run () {

                ITunesGenreImportService.importData();

                assertThat(Genre.getGenres().size()).isNotEqualTo(0);

                //Make sure the Genres are built correctly
                Genre designGenre = Genre.getGenreByName("Design");
                Genre artGenre = Genre.getGenreByName("Arts");

                assertThat(designGenre).isNotNull();
                assertThat(artGenre).isNotNull();

                assertThat(designGenre.name).isEqualTo("Design");
                assertThat(designGenre.appleGenreId).isEqualTo(1402);
                assertThat(designGenre.parentGenre).isEqualTo(artGenre);

            }
        });
    }

    @Test
    public void testITunesGenreImportServiceDoubleImport() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run () {

                ITunesGenreImportService.importData();

                assertThat(Genre.getGenres().size()).isNotEqualTo(0);

                int numberOfGenres = Genre.getGenres().size();

                //Make sure importing it twice doesn't create more genres (since they already exist)
                ITunesGenreImportService.importData();

                assertThat(Genre.getGenres().size()).isEqualTo(numberOfGenres);

            }
        });
    }
}
