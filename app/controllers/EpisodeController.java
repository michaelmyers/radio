package controllers;

import be.objectify.deadbolt.java.actions.Unrestricted;
import com.avaje.ebean.Page;
import models.content.Episode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import java.util.List;

public class EpisodeController extends Controller {

    @Unrestricted
    public static Result getEpisodes(Long networkId, Long showId, Long genreId, int page, int pageSize, String sortBy, String order, String search) {
        Page<Episode> episodes = Episode.getEpisodes(networkId, showId, genreId, page, pageSize, sortBy, order, search);

        response().setHeader("X-Total-Count", String.valueOf(episodes.getTotalRowCount()));
        response().setHeader("X-Total-Pages", String.valueOf(episodes.getTotalPageCount()));
        response().setHeader("X-Current-Page", String.valueOf(episodes.getPageIndex()));
        response().setHeader("X-Page-Size", String.valueOf(pageSize));
        response().setHeader("X-Has-Next", String.valueOf(episodes.hasNext()));
        response().setHeader("X-Has-Prev", String.valueOf(episodes.hasPrev()));

        return ok(Json.toJson(episodes.getList()));
    }

    @Unrestricted
    public static Result count() {
        return ok(Json.newObject().put("count", Episode.count()));
    }

    @Unrestricted
    public static Result getEpisode(Long id) {
        Episode episode = Episode.getEpisode(id);
        return episode == null ? notFound() : ok(Json.toJson(episode));
    }
}
