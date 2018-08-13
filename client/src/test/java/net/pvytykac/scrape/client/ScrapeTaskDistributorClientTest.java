package net.pvytykac.scrape.client;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import pvytykac.net.scrape.model.v1.ScrapeSessionRepresentation;
import pvytykac.net.scrape.model.v1.SupportedScrapeTypesRepresentation;

public class ScrapeTaskDistributorClientTest {

	private ScrapeTaskDistributorClientV1 client;

	@Before
	public void setUp() throws Exception {
		this.client = new Feign.Builder()
				.logger(new Slf4jLogger())
				.encoder(new JacksonEncoder())
				.decoder(new JacksonDecoder())
				.target(ScrapeTaskDistributorClientV1.class, "http://localhost:9080");
	}

	@Test
	public void getSupportedScrapeTypes() throws Exception {
		SupportedScrapeTypesRepresentation response = client.getSupportedScrapeTypes();

		assertThat(response, notNullValue());
		assertThat(response.getSupportedScrapeTypes(), notNullValue());
		assertThat(response.getSupportedScrapeTypes().size(), is(1));
		assertThat(response.getSupportedScrapeTypes().iterator().next(), is("RES"));
	}

	@Test
	public void getScrapeTasks() throws Exception {
		ScrapeSessionRepresentation response = client.getScrapeSession(2, null);

		assertThat(response, notNullValue());
		assertThat(response.getSessionUuid(), notNullValue());
		assertThat(response.getTasks(), notNullValue());
		assertThat(response.getTasks().size(), is(2));
	}

	@Test
	public void getScrapeTasksWithIgnoredTypes() throws Exception {
		ScrapeSessionRepresentation response = client.getScrapeSession(1, ImmutableSet.of("RES", "NON_EXISTENT"));

		assertThat(response, nullValue());
	}
}
