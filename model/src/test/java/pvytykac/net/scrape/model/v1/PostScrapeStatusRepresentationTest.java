package pvytykac.net.scrape.model.v1;

import org.json.JSONObject;
import org.junit.Test;
import pvytykac.net.scrape.model.JsonTest;

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
                .withTimeoutAction(mockModelInstance(TimeoutAction.class))
                .build();

        JSONObject json = serialize(status);

        assertThat(json.getJSONObject("timeoutAction"), notNullValue());
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("timeoutAction", new JSONObject());

        PostScrapeStatusRepresentation status = deserialize(json, PostScrapeStatusRepresentation.class);

        assertThat(status.getTimeoutAction(), notNullValue());
    }
}