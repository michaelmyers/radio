package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import be.objectify.deadbolt.java.actions.Unrestricted;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.classification.Channel;
import play.mvc.Controller;
import play.libs.Json;
import play.mvc.Result;
import java.util.List;

public class ChannelController extends Controller {

    @Unrestricted
    public static Result getChannels() {
        List<Channel> channels = Channel.getChannels();
        return ok(Json.toJson(channels));
    }

    @Unrestricted
    public static Result getNumberOfDistinctChannels() {
        return ok(Json.newObject().put("count", Channel.getNumberOfDistinctChannels()));
    }

    @Unrestricted
    public static Result getChannel(Long id) {
        Channel channel = Channel.getChannel(id);
        return channel == null ? notFound() : ok(Json.toJson(channel));
    }

    @Unrestricted
    public static Result getEpisodesForChannel(Long id) {
        Channel channel = Channel.getChannel(id);
        return channel == null ? notFound() : ok(Json.toJson(channel.getPlayList()));
    }

    @SubjectPresent
    public static Result createChannel() {
        Channel newChannel = Json.fromJson(request().body().asJson(), Channel.class);
        newChannel.save();

        return created(Json.toJson(newChannel));
    }
}