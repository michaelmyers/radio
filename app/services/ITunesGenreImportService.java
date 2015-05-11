package services;

import models.classification.Genre;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import play.Logger;
import play.Play;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class ITunesGenreImportService {

    public static void importData() {

        File genres = Play.application().getFile("conf/resources/genre/itunes_genre_codes.csv");

        try {

            CSVParser parser = CSVParser.parse(genres, StandardCharsets.UTF_8, CSVFormat.DEFAULT.withHeader());

            for (CSVRecord csvRecord : parser) {

                Long appleGenreId = Long.parseLong(csvRecord.get("code"));

                //Make sure it isn't already in the database
                if (Genre.getGenreByAppleId(appleGenreId) == null) {
                    Genre importedGenre = new Genre(csvRecord.get("genre"));
                    importedGenre.appleGenreId = appleGenreId;

                    if (!csvRecord.get("parent").isEmpty()) {
                        Genre parentGenre = Genre.getGenreByAppleId(Long.parseLong(csvRecord.get("parent")));
                        importedGenre.parentGenre = parentGenre;
                    }

                    importedGenre.save();
                }
            }

        } catch (Exception e) {

            Logger.error("Error importing iTunes Genre Data", e);
        }
    }

}
