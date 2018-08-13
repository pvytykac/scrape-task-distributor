package pvytykac.net.scrape.model.v1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import pvytykac.net.scrape.model.JsonTest;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class ScrapeSessionRepresentationTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        ScrapeSessionRepresentation task = new ScrapeSessionRepresentation.ScrapeTaskRepresentationBuilder()
                .withSessionUuid("uuid")
                .withTasks(Collections.singletonList(mockModelInstance(ScrapeTask.class)))
                .build();

        JSONObject json = serialize(task);

        assertThat(json.getString("sessionUuid"), is(task.getSessionUuid()));
        assertThat(json.getJSONArray("tasks").length(), is(1));
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("sessionUuid", "123")
                .put("tasks", new JSONArray());

        ScrapeSessionRepresentation task = deserialize(json, ScrapeSessionRepresentation.class);

        assertThat(task.getSessionUuid(), is(json.getString("sessionUuid")));
        assertThat(task.getTasks(), notNullValue());
    }
}