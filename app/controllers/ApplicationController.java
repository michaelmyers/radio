package controllers;

import be.objectify.deadbolt.java.actions.Unrestricted;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import play.mvc.*;
import views.html.*;

public class ApplicationController extends Controller {

    @Unrestricted
    public static Result index(String route) {

        Config conf = ConfigFactory.load();
        String applicationName = conf.getString("application.name");
        String version = conf.getString("application.version");

        return ok(app.render(applicationName, version));
    }
}