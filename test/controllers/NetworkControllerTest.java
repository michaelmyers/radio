package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.content.Network;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Result;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;
import static play.test.Helpers.status;

public class NetworkControllerTest {

    @Test
    public void getNetwork() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Network network = new Network("Test Network");
                network.save();

                Result result = callAction(routes.ref.NetworkController.getNetwork(network.id), fakeRequest());

                assertThat(status(result)).isEqualTo(OK);
            }
        });
    }

    @Test
    public void getNetworkCount() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Network network = new Network("Test Network");
                network.save();

                Network network2 = new Network("Test Network 2");
                network2.save();

                Result result = callAction(routes.ref.NetworkController.count(), fakeRequest());

                assertThat(status(result)).isEqualTo(OK);

                JsonNode json = Json.parse(contentAsString(result));
                assertThat(json.get("count").asInt()).isEqualTo(2);
            }
        });
    }

}
