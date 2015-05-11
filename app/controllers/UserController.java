package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import be.objectify.deadbolt.java.actions.Unrestricted;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import models.user.User;

import java.util.List;

public class UserController extends Controller {

    @Unrestricted
    public static Result getUsers() {
        List<User> users = User.getUsers();
        return ok(Json.toJson(users));
    }

    @Unrestricted
    public static Result getUser(Long id) {
        User user = User.getUser(id);
        return user == null ? notFound() : ok(Json.toJson(user));
    }

    @Unrestricted
    public static Result createUser()  {
        User newUser = Json.fromJson(request().body().asJson(), User.class);

        //TODO: We need to add some validation here.

        //The password is not set because it is a private property, it needs to be pulled from the json
        String password = request().body().asJson().get(User.PASSWORD_KEY_JSON).asText();
        newUser.setPassword(password);
        newUser.save();

        //Return the user back to the client in the body
        ObjectNode newUserJson = Json.newObject();
        newUserJson.put(User.USER_KEY_JSON, Json.toJson(newUser));

        return created(newUserJson);
    }

    @SubjectPresent
    public static Result updateUser(Long id) {
        User user = Json.fromJson(request().body().asJson(), User.class);
        User updated = User.updateUser(id, user);
        return ok(Json.toJson(updated));
    }

    @SubjectPresent
    public static Result disableUser(Long id)
    {
        User disabledUser = User.disableUser(id);

        //Setup an object to return
        ObjectNode disableUserJson = Json.newObject();

        if (disabledUser == null) {
            //If the user is null, it wasn't found
            disableUserJson.put("error", "User not found");
        } else {
            //otherwise return the user
            disableUserJson.put(User.USER_KEY_JSON, Json.toJson(disabledUser));
        }

        return ok(disableUserJson);
    }

    public static Result deleteUser(Long id)
    {
        User.deleteUser(id);
        return noContent(); // http://stackoverflow.com/a/2342589/1415732
    }

}
