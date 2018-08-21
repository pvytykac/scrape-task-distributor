package pvytykac.net.scrape.model.v1;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import pvytykac.net.scrape.model.JsonTest;
import net.pvytykac.scrape.util.ModelBuilder;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class ScrapeErrorTest extends JsonTest {

    @Test
    public void serializeWithModelBuilder() throws Exception {
        ModelBuilder<ScrapeStep> scrapeErrorBuilder = mockModelBuilder(ScrapeStep.class);
        ModelBuilder<ClientException> clientExceptionBuilder = mockModelBuilder(ClientException.class);
        List<FailedExpectation> failedExpectations = Collections.singletonList(mockModelInstance(FailedExpectation.class));

        ScrapeError scrapeError = new ScrapeError.ScrapeErrorBuilder()
                .withScrapeStep(scrapeErrorBuilder)
                .withClientException(clientExceptionBuilder)
                .withFailedExpectations(failedExpectations)
                .build();

        JSONObject json = serialize(scrapeError);

        assertThat(json.getJSONObject("step"), notNullValue());
        assertThat(json.getJSONObject("clientException"), notNullValue());
        assertThat(json.getJSONArray("failedExpectations"), notNullValue());
        assertThat(json.getJSONArray("failedExpectations").length(), is(1));
        assertThat(json.getJSONArray("failedExpectations").get(0), notNullValue());
    }

    @Test
    public void serializeWithObject() throws Exception {
        ScrapeStep scrapeStep = mockModelInstance(ScrapeStep.class);

        ScrapeError scrapeError = new ScrapeError.ScrapeErrorBuilder()
                .withScrapeStep(scrapeStep)
                .build();

        JSONObject json = serialize(scrapeError);

        assertThat(json.getJSONObject("step"), notNullValue());
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("step", new JSONObject())
                .put("clientException", new JSONObject())
                .put("failedExpectations", new JSONArray(Collections.singletonList(new JSONObject())));

        ScrapeError error = deserialize(json, ScrapeError.class);

        assertThat(error.getStep(), notNullValue());
        assertThat(error.getClientException(), notNullValue());
        assertThat(error.getFailedExpectations(), notNullValue());
        assertThat(error.getFailedExpectations().size(), is(1));
        assertThat(error.getFailedExpectations().get(0), notNullValue());
    }
}