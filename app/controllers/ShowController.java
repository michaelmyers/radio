package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import be.objectify.deadbolt.java.actions.Unrestricted;
import com.fasterxml.jackson.databind.JsonNode;
import models.content.Episode;
import models.content.Show;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;


import play.mvc.Result;
import services.RSSFeedService;

import java.util.List;

public class ShowController extends Controller {

    @Unrestricted
    public static Result getShows() {
        List<Show> shows = Show.getShows();
        return ok(Json.toJson(shows));
    }

    @Unrestricted
    public static Result getNumberOfDistinctShows() {
        return ok(Json.newObject().put("count", Show.getNumberOfDistinctShows()));
    }

    @Unrestricted
    public static Result getShow(Long id) {
        Show show = Show.getShow(id);
        return show == null ? notFound() : ok(Json.toJson(show));
    }

    //TODO: I thought the BodyParser was supposed to handle the first step of seeing if json existed, but...
    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    @SubjectPresent//TODO: will need to be a user that wants to be a network admin
    public static Result importShow() {
        JsonNode json = request().body().asJson();

        if (json == null) {
            return badRequest("JSON not passed in the body");
        }

        String rssFeedUrl = json.findPath("rssFeedUrl").textValue();

        if (rssFeedUrl == null) {
            return badRequest("Missing parameters [rssFeedUrl]");
        } else {
            RSSFeedService.importShowFromRSSFeed(rssFeedUrl);
            return ok();
        }
    }

    @Unrestricted
    public static Result getEpisodesForShow(Long id) {
        return ok(Json.toJson(Episode.getEpisodesByShow(id)));
    }
}
