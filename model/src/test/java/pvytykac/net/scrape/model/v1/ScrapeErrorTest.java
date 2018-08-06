package pvytykac.net.scrape.model.v1;

import org.json.JSONObject;
import org.junit.Test;
import pvytykac.net.scrape.model.JsonTest;
import pvytykac.net.scrape.model.ModelBuilder;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class ScrapeErrorTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        ModelBuilder<ScrapeStep> scrapeErrorBuilder = mockModelBuilder(ScrapeStep.class);
        ModelBuilder<ClientException> clientExceptionBuilder = mockModelBuilder(ClientException.class);
        ModelBuilder<FailedExpectation> failedExpectationModelBuilder = mockModelBuilder(FailedExpectation.class);

        ScrapeError scrapeError = new ScrapeError.ScrapeErrorBuilder()
                .withScrapeStep(scrapeErrorBuilder)
                .withClientException(clientExceptionBuilder)
                .withFailedExpectation(failedExpectationModelBuilder)
                .build();

        JSONObject json = serialize(scrapeError);

        assertThat(json.getJSONObject("step"), notNullValue());
        assertThat(json.getJSONObject("clientException"), notNullValue());
        assertThat(json.getJSONObject("failedExpectation"), notNullValue());
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("step", new JSONObject())
                .put("clientException", new JSONObject())
                .put("failedExpectation", new JSONObject());

        ScrapeError error = deserialize(json, ScrapeError.class);

        assertThat(error.getStep(), notNullValue());
        assertThat(error.getClientException(), notNullValue());
        assertThat(error.getFailedExpectation(), notNullValue());
    }
}