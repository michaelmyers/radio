package utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import models.classification.Genre;
import models.content.Network;
import models.content.Show;
import models.user.User;
import models.user.UserType;
import play.Logger;
import services.ITunesGenreImportService;
import services.ITunesPodcastImportService;

import java.util.List;

public class DevDataUtil {

    public static User user1;
    public static User user2;

    public static void createSuperUser() {
        //If no super is set, set the super
        if (User.findByEmailAddressAndPassword("admin@super.com", "super") == null) {
            //TODO Figure out how to make a more secure super user
            User superUser = new User("admin@super.com", "super", "Super", "User");
            superUser.userType = UserType.SUPER_USER;

            superUser.save();
        }
    }

    public static void loadTestData() {

        user1 = new User("user1@demo.com", "password", "John", "Doe");
        user1.save();

        user2 = new User("user2@demo.com", "password", "Jane", "Doe");
        user2.save();

    }

    public static void loadITunesGenres() {
        ITunesGenreImportService.importData();
    }

    public static void loadDevData() {



        //If we don't have any genres in the db, import them
        if (Genre.getGenres().size() == 0) {
            DevDataUtil.loadITunesGenres();
        }

        Config conf = ConfigFactory.load();

        if (conf.getBoolean("application.importITunesGenres")) {
            //If we don't have all the genres, import them
            List<Genre> genres = Genre.getGenresWithAppleId();

            for (Genre genre : genres) {

                List<Show> shows = Show.getShowsByGenre(genre);

                if (shows.size() < 1) {
                    Logger.debug("Importing genre " + genre.name);
                    ITunesPodcastImportService.importGenreOnBackgroundThread(genre, 10);
                } else {
                    Logger.debug("Genre " + genre.name + " already imported");
                }
            }
        }
    }
}
