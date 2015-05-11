package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import be.objectify.deadbolt.java.actions.Unrestricted;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.user.User;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class AuthorizationController extends Controller {

    public static final String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    public static final String AUTH_TOKEN_COOKIE = "authToken";
    public static final String AUTH_TOKEN_JSON = "authToken";

    @Unrestricted
    public static Result login() {

        Logger.debug("login()");

        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

        if (loginForm.hasErrors()) {
            Logger.error("Login form has errors " + loginForm.errorsAsJson());
            return badRequest(loginForm.errorsAsJson());
        }

        Login login = loginForm.get();

        Logger.debug("Find user with email " + login.emailAddress + " for login");

        User user = User.findByEmailAddressAndPassword(login.emailAddress, login.password);

        if (user == null) {
            Logger.debug("Could not find user by email and password, returning unauthorized");
            return unauthorized();
        } else {
            String authToken = user.createToken();
            Logger.debug("User logged in with token " + authToken);
            //Add the user and auth token as json
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN_JSON, authToken);
            authTokenJson.put(User.USER_KEY_JSON, Json.toJson(user));

            //Add the auth token as the cookie
            response().setCookie(AUTH_TOKEN_COOKIE, authToken);

            //Add the auth token in the header
            response().setHeader(AUTH_TOKEN_HEADER, authToken);

            return ok(authTokenJson);
        }
    }

    @SubjectPresent
    public static Result logout() {

        Logger.debug("logout()");

        if (request().cookie(AuthorizationController.AUTH_TOKEN_COOKIE) != null) {
            User.findByAuthToken(request().cookie(AuthorizationController.AUTH_TOKEN_COOKIE).value()).deleteAuthToken();
        }

        if (request().getHeader(AUTH_TOKEN_HEADER) != null) {
            User.findByAuthToken(request().getHeader(AUTH_TOKEN_HEADER)).deleteAuthToken();
        }

        //Clear out the cookie, header, and user's token
        response().discardCookie(AUTH_TOKEN_COOKIE);

        return  ok();
    }

    public static class Login {

        @Constraints.Required
        @Constraints.Email
        public String emailAddress;

        @Constraints.Required
        public String password;

    }
}
