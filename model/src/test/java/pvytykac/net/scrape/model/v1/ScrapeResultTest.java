package pvytykac.net.scrape.model.v1;

import org.json.JSONObject;
import org.junit.Test;
import pvytykac.net.scrape.model.JsonTest;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class ScrapeResultTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        ScrapeResult result = new ScrapeResult.ScrapeResultBuilder()
                .withContentType("application/json")
                .withHeaders(Collections.singletonMap("Location", "URI"))
                .withPayload("html")
                .withStatusCode(202)
                .build();

        JSONObject serialized = serialize(result);

        assertThat(serialized.getString("contentType"), is(result.getContentType()));
        assertThat(serialized.getJSONObject("headers").toMap(), is(result.getHeaders()));
        assertThat(serialized.getString("payload"), is(result.getPayload()));
        assertThat(serialized.getInt("statusCode"), is(result.getStatusCode()));
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("contentType", "text/css")
                .put("headers", new JSONObject().put("Location", "HREF"))
                .put("payload", "<html/>")
                .put("statusCode", 503);

        ScrapeResult result = deserialize(json, ScrapeResult.class);

        assertThat(result.getContentType(), is(json.getString("contentType")));
        assertThat(result.getHeaders(), is(json.getJSONObject("headers").toMap()));
        assertThat(result.getPayload(), is(json.getString("payload")));
        assertThat(result.getStatusCode(), is(json.getInt("statusCode")));
    }
}