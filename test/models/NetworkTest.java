package models;

import models.content.Network;
import org.junit.Test;
import utils.DevDataUtil;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class NetworkTest {

    @Test
    public void testCreate() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                DevDataUtil.loadTestData();

                Network network = new Network("Test Publisher");
                network.save();

                assertThat(network).isNotNull();
                assertThat(network.id).isNotNull();
                assertThat(network.creationDate).isNotNull();
                assertThat(network.createdBy).isNull();

                assertThat(Network.getNetwork(network.id)).isEqualTo(network);
                assertThat(Network.getNetworkByName("Test Publisher")).isEqualTo(network);
                assertThat(Network.getNetworks().size()).isEqualTo(1);
            }
        });
    }

}
