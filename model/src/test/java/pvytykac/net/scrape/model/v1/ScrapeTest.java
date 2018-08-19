package pvytykac.net.scrape.model.v1;

import org.json.JSONObject;
import org.junit.Test;
import pvytykac.net.scrape.model.JsonTest;
import pvytykac.net.scrape.model.v1.enums.ScrapeType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static pvytykac.net.scrape.model.MatcherUtil.isEnum;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class ScrapeTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        Scrape scrape = new Scrape.ScrapeBuilder()
                .withStoreAs("%param_name%")
                .withTarget("//div[id='banner']@text")
                .withType(ScrapeType.HREF)
                .build();

        JSONObject json = serialize(scrape);

        assertThat(json.getString("storeAs"), is(scrape.getStoreAs()));
        assertThat(json.getString("target"), is(scrape.getTarget()));
        assertThat(json.getString("type"), isEnum(scrape.getType()));
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("type", "HEADER")
                .put("target", "Location")
                .put("storeAs", "$redirectUri");

        Scrape scrape = deserialize(json, Scrape.class);

        assertThat(scrape.getType(), isEnum(json.getString("type"), ScrapeType.class));
        assertThat(scrape.getTarget(), is(json.getString("target")));
        assertThat(scrape.getStoreAs(), is(json.getString("storeAs")));
    }
}