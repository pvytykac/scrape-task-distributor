package pvytykac.net.scrape.model.v1;

import org.json.JSONObject;
import org.junit.Test;
import pvytykac.net.scrape.model.JsonTest;
import pvytykac.net.scrape.model.v1.enums.ActionType;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static pvytykac.net.scrape.model.MatcherUtil.isEnum;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class PostScrapeActionTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        PostScrapeAction action = new PostScrapeAction.PostScrapeActionBuilder()
                .withActionType(ActionType.TASK_TYPE_TIMEOUT)
                .withParameters(Collections.singletonMap("a", "b"))
                .build();

        JSONObject json = serialize(action);

        assertThat(json.getString("actionType"), isEnum(action.getActionType()));
        assertThat(json.getJSONObject("parameters").toMap(), is(action.getParameters()));
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("actionType", "TASK_TYPE_TIMEOUT")
                .put("parameters", new JSONObject());

        PostScrapeAction action = deserialize(json, PostScrapeAction.class);

        assertThat(action.getActionType(), isEnum(json.getString("actionType"), ActionType.class));
        assertThat(action.getParameters(), notNullValue());
    }
}