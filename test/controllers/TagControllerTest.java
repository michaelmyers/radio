package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import models.classification.Tag;
import org.junit.Test;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.status;

public class TagControllerTest {


    @Test
    public void getTags() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Tag tag1 = new Tag("Tag 1");
                tag1.save();

                Tag tag2 = new Tag("Tag 2");
                tag2.save();

                Result result = callAction(routes.ref.TagController.getTags(0, 5, "name", "asc", "", ""), fakeRequest());

                assertThat(status(result)).isEqualTo(OK);
            }
        });
    }

    @Test
    public void getTagsTerm() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Tag tag1 = new Tag("Tag 1");
                tag1.save();

                Tag tag2 = new Tag("Tag 2");
                tag2.save();

                Tag tag3 = new Tag("Name Tag 3");
                tag3.save();

                Result result = callAction(routes.ref.TagController.getTags(0, 5, "name", "asc", "", "ta"), fakeRequest());


                assertThat(status(result)).isEqualTo(OK);

                JsonNode json = Json.parse(contentAsString(result));
                Logger.debug(json.getNodeType().toString());
                Logger.debug(contentAsString(result));

                assertThat(json.size()).isEqualTo(2);
            }
        });
    }



    @Test
    public void getTagCount() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Tag tag1 = new Tag("Tag 1");
                tag1.save();

                Tag tag2 = new Tag("Tag 2");
                tag2.save();

                Result result = callAction(routes.ref.TagController.count(), fakeRequest());

                assertThat(status(result)).isEqualTo(OK);

                JsonNode json = Json.parse(contentAsString(result));
                assertThat(json.get("count").asInt()).isEqualTo(2);
            }
        });
    }

}
