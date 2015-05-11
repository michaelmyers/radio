package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectNotPresent;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.admin;

@SubjectPresent
public class AdministrationController extends Controller {

    public static Result index(String route) {

        Config conf = ConfigFactory.load();
        String applicationName = conf.getString("application.name");

        return ok(admin.render(applicationName));
    }
}
