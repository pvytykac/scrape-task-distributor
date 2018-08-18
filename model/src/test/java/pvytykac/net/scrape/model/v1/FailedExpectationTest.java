package pvytykac.net.scrape.model.v1;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.json.JSONObject;
import org.junit.Test;

import pvytykac.net.scrape.model.JsonTest;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class FailedExpectationTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        FailedExpectation expectation = new FailedExpectation.FailedExpectationBuilder()
                .withExpectation(mockModelInstance(ScrapeExpectation.class))
                .withActual("actual")
                .build();

        JSONObject json = serialize(expectation);

        assertThat(json.getString("actual"), is(expectation.getActual()));
        assertThat(json.getJSONObject("expectation"), notNullValue());
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("expectation", new JSONObject())
                .put("actual", "1");

        FailedExpectation expectation = deserialize(json, FailedExpectation.class);

        assertThat(expectation.getActual(), is(json.getString("actual")));
        assertThat(expectation.getExpectation(), notNullValue());
    }
}
