package pvytykac.net.scrape.model.v1;

import org.json.JSONObject;
import org.junit.Test;
import pvytykac.net.scrape.model.JsonTest;
import pvytykac.net.scrape.model.v1.enums.ExpectationType;
import pvytykac.net.scrape.model.v1.enums.Phase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static pvytykac.net.scrape.model.MatcherUtil.isEnum;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class FailedExpectationTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        FailedExpectation expectation = new FailedExpectation.FailedExpectationBuilder()
                .withExpectationType(ExpectationType.HEADER_PRESENT)
                .withActual("value")
                .withExpected("other value")
                .withPhase(Phase.AFTER)
                .build();

        JSONObject json = serialize(expectation);

        assertThat(json.getString("actual"), is(expectation.getActual()));
        assertThat(json.getString("expected"), is(expectation.getExpected()));
        assertThat(json.getString("expectationType"), isEnum(expectation.getExpectationType()));
        assertThat(json.getString("phase"), isEnum(expectation.getPhase()));
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("expectationType", "PARAMETER_PRESENT")
                .put("actual", "1")
                .put("expected", "2")
                .put("phase", "BEFORE");

        FailedExpectation expectation = deserialize(json, FailedExpectation.class);

        assertThat(expectation.getActual(), is(json.getString("actual")));
        assertThat(expectation.getExpected(), is(json.getString("expected")));
        assertThat(expectation.getExpectationType(), isEnum(json.getString("expectationType"), ExpectationType.class));
        assertThat(expectation.getPhase(), isEnum(json.getString("phase"), Phase.class));
    }
}
