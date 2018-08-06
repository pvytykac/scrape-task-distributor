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
public class ScrapeTaskTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        ScrapeTask task = new ScrapeTask.ScrapeTaskBuilder()
                .withTaskUuid("task1")
                .withParameters(Collections.singletonMap("a", "b"))
                .withSteps(Collections.singletonList(mockModelInstance(ScrapeStep.class)))
                .build();

        JSONObject json = serialize(task);

        assertThat(json.getString("taskUuid"), is(task.getTaskUuid()));
        assertThat(json.getJSONObject("parameters").toMap(), is(task.getParameters()));
        assertThat(json.getJSONArray("steps").length(), is(1));
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("taskUuid", "taskB")
                .put("parameters", new JSONObject())
                .put("steps", new JSONArray());

        ScrapeTask task = deserialize(json, ScrapeTask.class);

        assertThat(task.getTaskUuid(), is(json.getString("taskUuid")));
        assertThat(task.getParameters(), notNullValue());
        assertThat(task.getSteps(), notNullValue());
    }
}