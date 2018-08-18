package pvytykac.net.scrape.model.v1;

import org.json.JSONObject;
import org.junit.Test;
import pvytykac.net.scrape.model.JsonTest;
import pvytykac.net.scrape.model.v1.enums.ExpectationType;
import pvytykac.net.scrape.model.v1.enums.Operator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static pvytykac.net.scrape.model.MatcherUtil.isEnum;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class ScrapeExpectationTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        ScrapeExpectation expectation = new ScrapeExpectation.ScrapeExpectationBuilder()
                .withType(ExpectationType.HEADER)
                .withTarget("Location")
                .withExpectedValue("200")
                .withOperator(Operator.NOT_BLANK)
                .withExpected(true)
                .build();

        JSONObject json = serialize(expectation);

        assertThat(json.getString("type"), isEnum(expectation.getType()));
        assertThat(json.getString("target"), is(expectation.getTarget()));
        assertThat(json.getString("expectedValue"), is(expectation.getExpectedValue()));
        assertThat(json.getString("operator"), isEnum(expectation.getOperator()));
        assertThat(json.getBoolean("expected"), is(expectation.getExpected()));
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("type", ExpectationType.HEADER.toString())
                .put("target", "Content-Type")
                .put("expectedValue", "text/plain")
                .put("operator", Operator.CONTAINS.toString())
                .put("expected", true);

        ScrapeExpectation expectation = deserialize(json, ScrapeExpectation.class);

        assertThat(expectation.getType(), isEnum(json.getString("type"), ExpectationType.class));
        assertThat(expectation.getTarget(), is(json.getString("target")));
        assertThat(expectation.getExpectedValue(), is(json.getString("expectedValue")));
        assertThat(expectation.getOperator(), isEnum(json.getString("operator"), Operator.class));
        assertThat(expectation.getExpected(), is(json.getBoolean("expected")));
    }
}