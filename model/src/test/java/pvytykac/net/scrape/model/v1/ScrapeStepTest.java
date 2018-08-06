package pvytykac.net.scrape.model.v1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import pvytykac.net.scrape.model.JsonTest;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class ScrapeStepTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        ScrapeStep step = new ScrapeStep.ScrapeStepBuilder()
                .withSequenceNumber(1)
                .withUri("uri")
                .withMethod("POST")
                .withContentType("application/json")
                .withQueryParameters(singletonMap("a", "b"))
                .withHeaders(singletonMap("b", "c"))
                .withPayload("{}")
                .withExpectations(singletonList(mockModelInstance(ScrapeExpectation.class)))
                .withScrape(singletonList(mockModelInstance(Scrape.class)))
                .build();

        JSONObject json = serialize(step);

        assertThat(json.getInt("sequenceNumber"), is(step.getSequenceNumber()));
        assertThat(json.getString("uri"), is(step.getUri()));
        assertThat(json.getString("method"), is(step.getMethod()));
        assertThat(json.getString("contentType"), is(step.getContentType()));
        assertThat(json.getJSONObject("queryParameters").toMap(), is(step.getQueryParameters()));
        assertThat(json.getJSONObject("headers").toMap(), is(step.getHeaders()));
        assertThat(json.getString("payload"), is(step.getPayload()));
        assertThat(json.getJSONArray("expectations").length(), is(1));
        assertThat(json.getJSONArray("scrape").length(), is(1));
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("sequenceNumber", 2)
                .put("uri", "http://uri.com")
                .put("method", "GET")
                .put("contentType", "text/html")
                .put("queryParameters", new JSONObject())
                .put("headers", new JSONObject())
                .put("payload", "<html/>")
                .put("expectations", new JSONArray())
                .put("scrape", new JSONArray());

        ScrapeStep step = deserialize(json, ScrapeStep.class);

        assertThat(step.getSequenceNumber(), is(json.getInt("sequenceNumber")));
        assertThat(step.getUri(), is(json.getString("uri")));
        assertThat(step.getMethod(), is(json.getString("method")));
        assertThat(step.getContentType(), is(json.getString("contentType")));
        assertThat(step.getPayload(), is(json.getString("payload")));
        assertThat(step.getQueryParameters(), is(json.getJSONObject("queryParameters").toMap()));
        assertThat(step.getHeaders(), is(json.getJSONObject("headers").toMap()));
        assertThat(step.getExpectations(), notNullValue());
        assertThat(step.getScrape(), notNullValue());
    }
}