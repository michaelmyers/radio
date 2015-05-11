package controllers;

import models.user.User;
import models.user.UserType;
import org.junit.Ignore;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import utils.DevDataUtil;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;
import static play.test.Helpers.status;

public class AdministrationControllerTest {

    @Test
    public void indexAccessWithoutToken() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadTestData();

                Result result = callAction(routes.ref.AdministrationController.index(""), fakeRequest());

                assertThat(status(result)).isEqualTo(TEMPORARY_REDIRECT);
            }
        });
    }

    @Test
    public void indexAccessWithAdminCookieToken() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadTestData();

                User admin = new User("admin@admin.com", "password");
                admin.userType = UserType.ADMIN;

                String authToken = admin.createToken();

                Http.Cookie authCookie = new Http.Cookie(AuthorizationController.AUTH_TOKEN_COOKIE, authToken, -1, null, null, false, false);

                Result result = callAction(routes.ref.AdministrationController.index(""),
                        fakeRequest().withCookies(authCookie));

                assertThat(status(result)).isEqualTo(OK);

            }
        });
    }

    @Test
    @Ignore
    public void indexAccessWithUserCookieToken() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadTestData();

                User user = new User("user@user.com", "password");
                user.userType = UserType.LISTENER;

                String authToken = user.createToken();

                Http.Cookie authCookie = new Http.Cookie(AuthorizationController.AUTH_TOKEN_COOKIE, authToken, -1, null, null, false, false);

                Result result = callAction(routes.ref.AdministrationController.index(""),
                        fakeRequest().withCookies(authCookie));

                assertThat(status(result)).isEqualTo(UNAUTHORIZED);

            }
        });
    }

}
