package controllers;

import be.objectify.deadbolt.java.actions.Unrestricted;
import models.classification.Tag;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class TagController extends Controller {

    @Unrestricted
    public static Result getTags(int page, int pageSize, String sortBy, String order, String search, String term) {

        List<Tag> tags = null;

        //Term takes precedent, otherwise do standard search
        if (!term.isEmpty()) {
            tags = Tag.getTags(0, 5, "name", "asc", new StringBuilder().append(term).append("%").toString()).getList();
        } else {
            tags = Tag.getTags(page, pageSize, sortBy, order, new StringBuilder().append("%").append(search).append("%").toString()).getList();
        }

        return ok(Json.toJson(tags));
    }

    @Unrestricted
    public static Result count() {
        return ok(Json.newObject().put("count", Tag.count()));
    }

    @Unrestricted
    public static Result getTag(Long id) {
        Tag tag = Tag.getTag(id);
        return tag == null ? notFound() : ok(Json.toJson(tag));
    }

}
