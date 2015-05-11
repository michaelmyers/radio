package controllers;

import org.junit.Test;
import play.mvc.Result;
import utils.DevDataUtil;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;
import static play.test.Helpers.status;

public class DashboardControllerTest {

    @Test
    public void indexAccessWithoutToken() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadTestData();

                Result result = callAction(routes.ref.DashboardController.index("none"), fakeRequest());

                assertThat(status(result)).isEqualTo(TEMPORARY_REDIRECT);
            }
        });
    }

}
