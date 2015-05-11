package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.content.Show;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Result;
import utils.DevDataUtil;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import static play.test.Helpers.contentAsString;

public class ShowControllerTest {

    @Test
    public void getShow() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Show show = new Show("Test Show");
                show.save();

                Result result = callAction(routes.ref.ShowController.getShow(show.id), fakeRequest());

                assertThat(status(result)).isEqualTo(OK);

                //JsonNode json = Json.parse(contentAsString(result));
                //assertThat(json.get)

            }
        });
    }

    @Test
    public void getShowCount() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Show show = new Show("Test Show");
                show.save();

                Show show2 = new Show("Test Show2");
                show2.save();

                Result result = callAction(routes.ref.ShowController.getNumberOfDistinctShows(), fakeRequest());

                assertThat(status(result)).isEqualTo(OK);

                JsonNode json = Json.parse(contentAsString(result));
                assertThat(json.get("count").asInt()).isEqualTo(2);
            }
        });
    }

    @Test
    public void importShow() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadTestData();

                String authToken = DevDataUtil.user1.createToken();

                ObjectNode requestJson = Json.newObject();
                requestJson.put("rssFeedUrl", "http://www.npr.org/templates/rss/podcast.php?id=500005");

                Result result = callAction(routes.ref.ShowController.importShow(), fakeRequest().withJsonBody(requestJson).withHeader(AuthorizationController.AUTH_TOKEN_HEADER, authToken));

                assertThat(status(result)).isEqualTo(OK);

            }
        });
    }

    @Test
    public void importShowUnauthorized() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                ObjectNode requestJson = Json.newObject();
                requestJson.put("rssFeedUrl", "http://www.npr.org/templates/rss/podcast.php?id=500005");

                Result result = callAction(routes.ref.ShowController.importShow(), fakeRequest().withJsonBody(requestJson));

                assertThat(status(result)).isEqualTo(TEMPORARY_REDIRECT);

            }
        });
    }

    @Test
    public void importShowBadRequest() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadTestData();

                String authToken = DevDataUtil.user1.createToken();

                Result result = callAction(routes.ref.ShowController.importShow(), fakeRequest().withTextBody("bad").withHeader(AuthorizationController.AUTH_TOKEN_HEADER, authToken));

                assertThat(status(result)).isEqualTo(BAD_REQUEST);

            }
        });
    }

    @Test
    public void importShowMissingParameter() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadTestData();

                String authToken = DevDataUtil.user1.createToken();

                Result result = callAction(routes.ref.ShowController.importShow(), fakeRequest().withJsonBody(Json.newObject()).withHeader(AuthorizationController.AUTH_TOKEN_HEADER, authToken));

                assertThat(status(result)).isEqualTo(BAD_REQUEST);

            }
        });
    }

}
