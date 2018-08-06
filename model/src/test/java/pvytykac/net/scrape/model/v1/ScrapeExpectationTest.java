package pvytykac.net.scrape.model.v1;

import org.json.JSONObject;
import org.junit.Test;
import pvytykac.net.scrape.model.JsonTest;
import pvytykac.net.scrape.model.v1.enums.ExpectationType;

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
                .withExpected("200")
                .withType(ExpectationType.STATUS_CODE)
                .build();

        JSONObject json = serialize(expectation);

        assertThat(json.getString("expected"), is(expectation.getExpected()));
        assertThat(json.getString("type"), isEnum(expectation.getType()));
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("expected", "Location")
                .put("type", "HEADER_PRESENT");

        ScrapeExpectation expectation = deserialize(json, ScrapeExpectation.class);

        assertThat(expectation.getExpected(), is(json.getString("expected")));
        assertThat(expectation.getType(), isEnum(json.getString("type"), ExpectationType.class));
    }
}