package models;

import models.classification.Tag;
import models.content.Episode;
import models.content.Show;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class TagTest {

    @Test
    public void testCreate() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                Tag tag = new Tag("Tag");
                tag.save();

                assertThat(tag.getName()).isEqualTo("tag");
                assertThat(Tag.getTagByName("tag")).isEqualTo(tag);
                assertThat(Tag.getTags().size()).isEqualTo(1);

            }
        });
    }
}
