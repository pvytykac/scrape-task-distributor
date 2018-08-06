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
public class PostScrapeStatusRepresentationTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        PostScrapeStatusRepresentation status = new PostScrapeStatusRepresentation.PostScrapeStatusRepresentationBuilder()
                .withActions(Collections.singletonList(mockModelInstance(PostScrapeAction.class)))
                .build();

        JSONObject json = serialize(status);

        assertThat(json.getJSONArray("actions").length(), is(1));
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("actions", new JSONArray());

        PostScrapeStatusRepresentation status = deserialize(json, PostScrapeStatusRepresentation.class);

        assertThat(status.getActions(), notNullValue());
    }
}