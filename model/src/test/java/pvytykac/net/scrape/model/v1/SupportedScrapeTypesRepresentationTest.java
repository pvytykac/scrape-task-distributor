package pvytykac.net.scrape.model.v1;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import pvytykac.net.scrape.model.JsonTest;
import pvytykac.net.scrape.model.v1.SupportedScrapeTypesRepresentation.SupportedScrapeTypesRepresentationBuilder;

public class SupportedScrapeTypesRepresentationTest extends JsonTest {

	@Test
	public void serialize() throws Exception {
		SupportedScrapeTypesRepresentation representation = new SupportedScrapeTypesRepresentationBuilder()
				.withSupportedScrapeTypes(Collections.singleton("a"))
				.build();

		JSONObject json = serialize(representation);

		JSONArray array = json.getJSONArray("supportedScrapeTypes");
		assertThat(array, notNullValue());
		assertThat(array.length(), is(1));
		assertThat(array.get(0), is("a"));
	}

	@Test
	public void deserialize() throws Exception {
		JSONObject json = new JSONObject()
				.put("supportedScrapeTypes", Collections.singleton("a"));

		SupportedScrapeTypesRepresentation representation = deserialize(json, SupportedScrapeTypesRepresentation.class);

		assertThat(representation.getSupportedScrapeTypes(), notNullValue());
		assertThat(representation.getSupportedScrapeTypes().size(), is(1));
		assertThat(representation.getSupportedScrapeTypes().iterator().next(), is("a"));
	}
}