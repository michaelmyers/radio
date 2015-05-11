package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.user.User;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import utils.DevDataUtil;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.*;
import static play.test.Helpers.*;

public class AuthorizationControllerTest {

    @Test
    public void login() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                DevDataUtil.loadTestData();

                ObjectNode loginJson = Json.newObject();
                loginJson.put("emailAddress", DevDataUtil.user1.getEmailAddress());
                loginJson.put("password", DevDataUtil.user1.getPassword());

                Result result = callAction(routes.ref.AuthorizationController.login(), fakeRequest().withJsonBody(loginJson));

                assertThat(status(result)).isEqualTo(OK);
                assertThat(cookie(AuthorizationController.AUTH_TOKEN_COOKIE, result)).isNotNull();
                assertThat(header(AuthorizationController.AUTH_TOKEN_HEADER, result)).isNotNull();

                JsonNode json = Json.parse(contentAsString(result));
                assertThat(json.get(AuthorizationController.AUTH_TOKEN_COOKIE)).isNotNull();
            }
        });
    }

    @Test
    public void loginWithBadPassword() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                DevDataUtil.loadTestData();

                ObjectNode loginJson = Json.newObject();
                loginJson.put("emailAddress", DevDataUtil.user1.getEmailAddress());
                loginJson.put("password", DevDataUtil.user1.getPassword().substring(1));

                Result result = callAction(routes.ref.AuthorizationController.login(), fakeRequest().withJsonBody(loginJson));

                assertThat(status(result)).isEqualTo(UNAUTHORIZED);
            }
        });
    }

    @Test
    public void loginWithBadEmail() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                DevDataUtil.loadTestData();

                ObjectNode loginJson = Json.newObject();
                loginJson.put("emailAddress", DevDataUtil.user1.getEmailAddress().substring(1));
                loginJson.put("password", DevDataUtil.user1.getPassword());

                Result result = callAction(routes.ref.AuthorizationController.login(), fakeRequest().withJsonBody(loginJson));

                assertThat(status(result)).isEqualTo(UNAUTHORIZED);
            }
        });
    }

    @Test
    public void loginWithDifferentCaseEmail() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                DevDataUtil.loadTestData();

                ObjectNode loginJson = Json.newObject();
                loginJson.put("emailAddress", DevDataUtil.user1.getEmailAddress().toUpperCase());
                loginJson.put("password", DevDataUtil.user1.getPassword());

                Result result = callAction(routes.ref.AuthorizationController.login(), fakeRequest().withJsonBody(loginJson));

                assertThat(status(result)).isEqualTo(OK);
            }
        });
    }

    @Test
    public void loginWithNullPassword() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                DevDataUtil.loadTestData();

                ObjectNode loginJson = Json.newObject();
                loginJson.put("emailAddress", DevDataUtil.user1.getEmailAddress());

                Result result = callAction(routes.ref.AuthorizationController.login(), fakeRequest().withJsonBody(loginJson));

                assertThat(status(result)).isEqualTo(BAD_REQUEST);
            }
        });
    }

    @Test
    public void logoutWithHeaderToken() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadTestData();

                String authToken = DevDataUtil.user1.createToken();

                Result result = callAction(routes.ref.AuthorizationController.logout(), fakeRequest().withHeader(AuthorizationController.AUTH_TOKEN_HEADER, authToken));

                assertThat(status(result)).isEqualTo(OK);
                assertThat(header(AuthorizationController.AUTH_TOKEN_HEADER, result)).isNull();
                assertThat(cookie(AuthorizationController.AUTH_TOKEN_COOKIE, result).value()).isEmpty();

                assertThat(contentAsString(result).isEmpty());

                assertThat(User.findByAuthToken(authToken)).isNull();
            }
        });
    }

    @Test
    public void logoutWithCookieToken() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadTestData();

                String authToken = DevDataUtil.user1.createToken();

                Http.Cookie authCookie = new Http.Cookie(AuthorizationController.AUTH_TOKEN_COOKIE, authToken, -1, null, null, false, false);

                Result result = callAction(routes.ref.AuthorizationController.logout(),
                        fakeRequest().withCookies(authCookie));

                assertThat(status(result)).isEqualTo(OK);
                assertThat(header(AuthorizationController.AUTH_TOKEN_HEADER, result)).isNull();
                assertThat(cookie(AuthorizationController.AUTH_TOKEN_COOKIE, result).value()).isEmpty();
                assertThat(contentAsString(result).isEmpty());

                assertThat(User.findByAuthToken(authToken)).isNull();
            }
        });
    }

}
