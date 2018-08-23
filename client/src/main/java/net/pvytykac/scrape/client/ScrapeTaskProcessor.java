package net.pvytykac.scrape.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.pvytykac.scrape.util.ModelBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import pvytykac.net.scrape.model.v1.ClientException.ClientExceptionBuilder;
import pvytykac.net.scrape.model.v1.FailedExpectation;
import pvytykac.net.scrape.model.v1.ScrapeError;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation.ScrapeResultRepresentationBuilder;
import pvytykac.net.scrape.model.v1.ScrapeStep;
import pvytykac.net.scrape.model.v1.ScrapeTask;

public class ScrapeTaskProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(ScrapeTaskProcessor.class);

	private final OkHttpClient http;
	private final ExpectationHandler expectationHandler;
	private final ScrapeHandler scrapeHandler;

	public ScrapeTaskProcessor(OkHttpClient client) {
		this.http = client;
		this.expectationHandler = new ExpectationHandler();
		this.scrapeHandler = new ScrapeHandler();
	}

	public ScrapeResultRepresentation processTask(String sessionUuid, ScrapeTask task) {
		Map<String, String> parameters = new HashMap<>(task.getParameters());
		int progress = 0;
		ResponseWrapper response = null;
		ScrapeStep currentStep = null;
		List<ScrapeStep> steps = new ArrayList<>(task.getSteps());
		steps.sort(Comparator.comparingInt(ScrapeStep::getSequenceNumber));

		try {
			LOG.debug("Executing task '{}' type '{}'", task.getTaskUuid(), task.getTaskType());

			for (ScrapeStep step: steps) {
				LOG.debug("Executing step '{}' for task '{}' of type '{}'", step.getSequenceNumber(), task.getTaskUuid(),
						task.getTaskType());

				++progress;
				currentStep = step;

				Request request = new ScrapeRequestBuilder(step, parameters).build();
				response = new ResponseWrapper(http.newCall(request).execute());
				List<FailedExpectation> failedExpectations = expectationHandler.processExpectations(response,
						step.getExpectations());

				if (!failedExpectations.isEmpty()) {
					LOG.debug("There are '{}' failed expectations for task '{}' and step '{}': {}", failedExpectations.size(),
							task.getTaskUuid(), step.getSequenceNumber(), failedExpectations.stream()
									.map(fe -> String.format("[id %d was %s]", fe.getExpectation().getId(), fe.getActual()))
									.toArray());

					return failedExpectationResult(sessionUuid, task, step, failedExpectations, response.time());
				}

				if (progress < steps.size()) {
					parameters.putAll(scrapeHandler.processScrapes(response, step.getScrape()));
				}

				LOG.debug("Step '{}' for task '{}' of type '{}' was processed successfully", step.getSequenceNumber(),
						task.getTaskUuid(), task.getTaskType());
			}

			LOG.info("Task '{}' of type '{}' was processed successfully", task.getTaskUuid(), task.getTaskType());
			return successResult(sessionUuid, task, response);
		} catch (Exception ex) {
			LOG.error("Client error while processing task '{}' of type '{}'", task.getTaskUuid(), task.getTaskType(), ex);
			return clientErrorResult(sessionUuid, task, currentStep, response, ex);
		}
	}

	private static ScrapeResultRepresentation successResult(String sessionUuid, ScrapeTask task, ResponseWrapper response) {
		ScrapeResult.ScrapeResultBuilder result = new ScrapeResult.ScrapeResultBuilder()
				.withContentType(response.header("Content-Type"))
				.withHeaders(response.headers())
				.withPayload(response.body())
				.withStatusCode(response.code());

		return getDefaultResult(sessionUuid, task, response.time())
				.withResult(result)
				.build();
	}

	private static ScrapeResultRepresentation failedExpectationResult(String sessionUuid, ScrapeTask task,
			ScrapeStep step, List<FailedExpectation> failedExpectations, Long time) {
		ModelBuilder<ScrapeError> error = new ScrapeError.ScrapeErrorBuilder()
				.withFailedExpectations(failedExpectations)
				.withScrapeStep(step);

		return getDefaultResult(sessionUuid, task, time)
				.withError(error)
				.build();
	}

	private static ScrapeResultRepresentation clientErrorResult(String sessionUuid, ScrapeTask task,
			ScrapeStep step, ResponseWrapper response, Exception ex) {
		String payload = null;
		Integer code = response == null ? null : response.code();

		try {
			payload = response == null ? null : response.body();
		} catch (Exception ignored) {}

		ClientExceptionBuilder clientException = new ClientExceptionBuilder()
				.withMessage(ex.getMessage())
				.withStatusCode(code)
				.withPayload(payload);

		return getDefaultResult(sessionUuid, task, response == null ? System.currentTimeMillis() : response.time())
				.withError(new ScrapeError.ScrapeErrorBuilder()
						.withClientException(clientException)
						.withScrapeStep(step))
				.build();
	}

	private static ScrapeResultRepresentationBuilder getDefaultResult(String sessionUuid, ScrapeTask task, Long requestTime) {
		return new ScrapeResultRepresentationBuilder()
				.withTaskType(task.getTaskType())
				.withSessionId(sessionUuid)
				.withTaskId(task.getTaskUuid())
				.withPart(1)
				.withTotalParts(1)
				.withRequestTimeUtcMs(requestTime);
	}
}
