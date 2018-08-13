package net.pvytykac.scrape.client;

import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pvytykac.net.scrape.model.v1.ClientException;
import pvytykac.net.scrape.model.v1.Scrape;
import pvytykac.net.scrape.model.v1.ScrapeError;
import pvytykac.net.scrape.model.v1.ScrapeExpectation;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeStep;
import pvytykac.net.scrape.model.v1.ScrapeTask;

public class ScrapeTaskProcessor {

	private final OkHttpClient http;

	public ScrapeTaskProcessor() {
		this.http = new OkHttpClient();
	}

	public ScrapeResultRepresentation processTask(String sessionUuid, ScrapeTask task) {
		Map<String, String> parameters = new HashMap<>(task.getParameters());
		int progress = 0;
		Response response = null;
		ScrapeStep currentStep = null;

		try {
			for (ScrapeStep step: task.getSteps()) {
				++progress;
				currentStep = step;

				Request request = new ScrapeRequestBuilder(step, parameters).build();
				response = http.newCall(request).execute();

				if (progress < task.getSteps().size()) {
					processStepResult(response, parameters, step.getExpectations(), step.getScrape());
				}

			}

			return successResult(sessionUuid, task, response);
		} catch (Exception ex) {
			return clientErrorResult(sessionUuid, task, currentStep, response, ex);
		}
	}

	private static void processStepResult(Response response, Map<String, String> parameters, List<ScrapeExpectation> expectations, List<Scrape> scrapes) {
		// todo: parse out stuff, store it in parameters, process expectations, send expectation failed error if expectations not met
	}

	private static ScrapeResultRepresentation successResult(String sessionUuid, ScrapeTask task, Response response) {
		ScrapeResult.ScrapeResultBuilder result = new ScrapeResult.ScrapeResultBuilder()
				.withContentType(response.header("Content-Type"))
				.withHeaders(parseHeaders(response.headers()))
				.withPayload(parsePayload(response.body()))
				.withStatusCode(response.code());

		return new ScrapeResultRepresentation.ScrapeResultRepresentationBuilder()
				.withSessionId(sessionUuid)
				.withTaskId(task.getTaskUuid())
				.withTaskType(task.getTaskType())
				.withResult(result)
				.build();
	}

	private static ScrapeResultRepresentation clientErrorResult(String sessionUuid, ScrapeTask task,
			ScrapeStep step, Response response, Exception ex) {
		String payload = null;
		Integer code = response == null ? null : response.code();

		try {
			payload = response == null ? null : response.body().string();
		} catch (Exception ignored) {}

		ClientException.ClientExceptionBuilder clientException = new ClientException.ClientExceptionBuilder()
				.withMessage(ex.getMessage())
				.withStatusCode(code)
				.withPayload(payload);

		return new ScrapeResultRepresentation.ScrapeResultRepresentationBuilder()
				.withTaskType(task.getTaskType())
				.withSessionId(sessionUuid)
				.withTaskId(task.getTaskUuid())
				.withError(new ScrapeError.ScrapeErrorBuilder()
						.withClientException(clientException)
						.withScrapeStep(step))
				.build();
	}

	private static String parsePayload(ResponseBody responseBody) {
		try {
			return responseBody == null
					? null
					: responseBody.string();
		} catch (IOException ignored) {
			return null;
		}
	}

	private static Map<String, String> parseHeaders(Headers headers) {
		Function<List<String>, String> f = list -> {
			String str = list.stream().reduce("", (acc, cur) -> acc + cur + ",");
			return str.substring(0, str.length() - 2);
		};

		return headers.toMultimap()
				.entrySet()
				.stream()
				.map(entry -> new SimpleEntry<>(entry.getKey(), f.apply(entry.getValue())))
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue));
	}

}
