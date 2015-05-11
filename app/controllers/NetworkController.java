package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import be.objectify.deadbolt.java.actions.Unrestricted;
import models.content.Episode;
import models.content.Network;
import play.libs.Json;
import play.mvc.Controller;

import play.mvc.Result;
import java.util.List;

public class NetworkController extends Controller {

    @Unrestricted
    public static Result getNetworks() {
        List<Network> networks = Network.getNetworks();
        return ok(Json.toJson(networks));
    }

    @Unrestricted
    public static Result count() {
        return ok(Json.newObject().put("count", Network.getNumberOfDistinctNetworks()));
    }

    @Unrestricted
    public static Result getNetwork(Long id) {
        Network network = Network.getNetwork(id);
        return network == null ? notFound() : ok(Json.toJson(network));
    }

    @SubjectPresent
    public static Result createNetwork() {
        Network newNetwork = Json.fromJson(request().body().asJson(), Network.class);
        newNetwork.save();

        return created(Json.toJson(newNetwork));
    }

    @Unrestricted
    public static Result getShowsForNetwork(Long id) {
        return ok();
    }

    @Unrestricted
    public static Result getEpisodesForNetwork(Long id) {
        return ok(Json.toJson(Episode.getEpisodesByNetwork(id)));
    }
}
