package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.user.User;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Result;
import utils.DevDataUtil;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.*;
import static play.test.Helpers.*;

public class UserControllerTest {

    @Test
    public void createUser() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                ObjectNode newUserJson = Json.newObject();
                newUserJson.put("emailAddress", "test@test.com");
                newUserJson.put("password", "testPassword");
                newUserJson.put("zipCode", "20002");

                Result result = callAction(routes.ref.UserController.createUser(), fakeRequest().withJsonBody(newUserJson));

                assertThat(status(result)).isEqualTo(CREATED);

            }
        });
    }

    @Test
    public void updateUser() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadTestData();

                ObjectNode updatedInformationUserJson = Json.newObject();
                updatedInformationUserJson.put("firstName", "First");
                updatedInformationUserJson.put("lastName", "Last");

                String authToken = DevDataUtil.user1.createToken();

                Result result = callAction(routes.ref.UserController.updateUser(DevDataUtil.user1.id),
                        fakeRequest().withHeader(AuthorizationController.AUTH_TOKEN_HEADER, authToken).withJsonBody(updatedInformationUserJson));

                assertThat(status(result)).isEqualTo(OK);

                //Check if the user in the DB was updated properly
                User user1 = User.getUser(DevDataUtil.user1.id);
                assertThat(user1.firstName).isEqualTo("First");
                assertThat(user1.lastName).isEqualTo("Last");
                assertThat(user1.getEmailAddress()).isEqualTo(DevDataUtil.user1.getEmailAddress());

                //Make sure the user returned in the result object is correct
                JsonNode json = Json.parse(contentAsString(result));
                assertThat(json.get("firstName").asText()).isEqualTo("First");
                assertThat(json.get("lastName").asText()).isEqualTo("Last");
                assertThat(json.get("emailAddress").asText()).isEqualTo(user1.getEmailAddress());
            }
        });
    }
}
