package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.dashboard;

public class DashboardController extends Controller {

    @SubjectPresent
    public static Result index(String route) {
        return ok(dashboard.render("Publisher"));
    }

}
