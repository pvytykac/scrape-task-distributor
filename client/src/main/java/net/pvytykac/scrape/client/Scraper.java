package net.pvytykac.scrape.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.Scrape;
import pvytykac.net.scrape.model.v1.ScrapeExpectation;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeResult.ScrapeResultBuilder;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation.ScrapeResultRepresentationBuilder;
import pvytykac.net.scrape.model.v1.ScrapeStep;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.ScrapeTaskRepresentation;

public class Scraper implements Runnable {

	private final ScrapeTaskDistributorClientV1 client;
	private Set<String> supportedTypes;
	private final Map<String, Long> ignoredTypes;
	private final OkHttpClient http;

	public Scraper(ScrapeTaskDistributorClientV1 client) {
		this.client = client;
		this.supportedTypes = client.getSupportedScrapeTypes()
				.getSupportedScrapeTypes();
	    this.ignoredTypes = new HashMap<>();
	    this.http = new OkHttpClient();
	}

	@Override
	public void run() {
		ScrapeTaskRepresentation tasks = client.getScrapeTasks(1, ignoredTypes.keySet());

		for (ScrapeTask task: tasks.getTasks()) {
			try {
				List<ScrapeStep> steps = new ArrayList<>(task.getSteps());
				steps.sort(Comparator.comparingInt(ScrapeStep::getSequenceNumber));

				Map<String, String> parameters = new HashMap<>(task.getParameters());
				int ix = 0, count = steps.size();

				for (ScrapeStep step : steps) {
					++ix;
					RequestBody payload = step.getPayload() == null ?
							null :
							getPayload(step.getPayload(), step.getContentType(), parameters);

					Request request = new Request.Builder()
							.url(step.getUri())
							.method(step.getMethod().toString(), payload)
//							.headers(Headers.of(step.getHeaders()))
							.build();

					Response response = http.newCall(request).execute();

					if (ix == count) {
						submitResult(tasks.getSessionUuid(), task.getTaskUuid(), response);
					} else {
						processScrapeStep(response, step.getExpectations(), step.getScrape(), parameters);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void submitResult(String sessionUuid, String taskUuid, Response response) throws IOException {
		ScrapeResultRepresentation result = new ScrapeResultRepresentationBuilder()
				.withResult(new ScrapeResultBuilder()
						.withContentType(response.header("Content-Type"))
						.withPayload(response.body().string())
						.withStatusCode(response.code())
						.withHeaders(Collections.emptyMap()))
				.withSessionId(sessionUuid)
				.withTaskId(taskUuid)
				.build();

		PostScrapeStatusRepresentation status = client.postScrapeResult(taskUuid, result);
	}

	private static void processScrapeStep(Response response, List<ScrapeExpectation> expectations, List<Scrape> scrapes,
			Map<String, String> parameters) {
		if (expectations != null) {
			expectations.forEach(expectation -> {
				switch(expectation.getType()) {
					case HEADER_PRESENT:
						if (response.header(expectation.getExpected()) == null) {
							throw new IllegalStateException("expected header '' not present");
						}
						break;
					case STATUS_CODE:
						if (response.code() != Integer.valueOf(expectation.getExpected())) {
							throw new IllegalStateException("expected status code '' but was ''");
						}
						break;
					default: // no-op
				}
			});
		}

		scrapes.forEach(scrape -> {
			String value;

			switch(scrape.getType()) {
				case HEADER:
					value = response.header(scrape.getTarget());
					break;
				case XPATH:
				case JSON_PATH:
				default:
					throw new IllegalStateException("unsupported scrape type '" + scrape.getType() + "'");
			}

			parameters.put(scrape.getStoreAs(), value);
		});

		if (expectations != null) {
			expectations.forEach(expectation -> {
				switch(expectation.getType()) {
					case PARAMETER_PRESENT:
						if (parameters.containsKey(expectation.getExpected())) {
							throw new IllegalStateException("expected parameter '" + expectation.getExpected() + "' not present");
						}
						break;
					default: // no-op
				}
			});
		}
	}

	private static RequestBody getPayload(String payload, String contentType, Map<String, String> parameters) {
		String content = payload;
		for (Map.Entry<String, String> param: parameters.entrySet()) {
			content = content.replaceAll("\\$\\{" + param.getKey() + "}", param.getValue());
		}

		return RequestBody.create(MediaType.parse(contentType), content);
	}
}
