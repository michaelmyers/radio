package controllers;

import be.objectify.deadbolt.java.actions.Unrestricted;
import models.classification.Genre;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.Json;
import java.util.List;

public class GenreController extends Controller {

    @Unrestricted
    public static Result getGenres() {
        List<Genre> genres = Genre.getGenres();
        return ok(Json.toJson(genres));
    }

    @Unrestricted
    public static Result count() {
        return ok(Json.newObject().put("count", Genre.count()));
    }

    @Unrestricted
    public static Result getGenre(Long id) {
        Genre genre = Genre.getGenre(id);
        return genre == null ? notFound() : ok(Json.toJson(genre));
    }
}
