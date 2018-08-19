package pvytykac.net.scrape.model.v1;

import org.json.JSONObject;
import org.junit.Test;
import pvytykac.net.scrape.model.JsonTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class TimeoutActionTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        TimeoutAction action = new TimeoutAction.TimeoutActionBuilder()
                .withTaskType("RES")
                .withTimeout(250L)
                .build();

        JSONObject json = serialize(action);

        assertThat(json.getString("taskType"), is(action.getTaskType()));
        assertThat(json.getLong("timeout"), is(action.getTimeout()));
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("taskType", "TASK_TYPE")
                .put("timeout", 500L);

        TimeoutAction action = deserialize(json, TimeoutAction.class);

        assertThat(action.getTaskType(), is(json.getString("taskType")));
        assertThat(action.getTimeout(), is(json.getLong("timeout")));
    }
}