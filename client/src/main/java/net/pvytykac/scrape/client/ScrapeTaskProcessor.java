package net.pvytykac.scrape.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import pvytykac.net.scrape.model.ModelBuilder;
import pvytykac.net.scrape.model.v1.ClientException;
import pvytykac.net.scrape.model.v1.FailedExpectation;
import pvytykac.net.scrape.model.v1.ScrapeError;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeStep;
import pvytykac.net.scrape.model.v1.ScrapeTask;

public class ScrapeTaskProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(ScrapeTaskProcessor.class);

	private final OkHttpClient http;
	private final ExpectationHandler expectationHandler;
	private final ScrapeHandler scrapeHandler;

	public ScrapeTaskProcessor() {
		this.http = new OkHttpClient();
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
			for (ScrapeStep step: steps) {
				LOG.info("Executing step '{}' for task '{}' of type '{}'", step.getSequenceNumber(), task.getTaskUuid(),
						task.getTaskType());

				++progress;
				currentStep = step;

				Request request = new ScrapeRequestBuilder(step, parameters).build();
				response = new ResponseWrapper(http.newCall(request).execute());

				if (progress < task.getSteps().size()) {
					List<FailedExpectation> failedExpectations = expectationHandler.processExpectations(response,
							step.getExpectations());

					parameters.putAll(scrapeHandler.processScrapes(response, step.getScrape()));

					if (!failedExpectations.isEmpty()) {
						LOG.error("Failed expectations: '{}'", failedExpectations.size());
						return failedExpectationResult(sessionUuid, task, step, failedExpectations);
					}
				}
			}

			LOG.info("Successfully processed task '{}' of type '{}'", task.getTaskUuid(), task.getTaskType());
			return successResult(sessionUuid, task, response);
		} catch (Exception ex) {
			LOG.error("Client error while processing task '{}' of type '{}'", task.getTaskUuid(), task.getTaskType());
			return clientErrorResult(sessionUuid, task, currentStep, response, ex);
		}
	}

	private static ScrapeResultRepresentation successResult(String sessionUuid, ScrapeTask task, ResponseWrapper response) {
		ScrapeResult.ScrapeResultBuilder result = new ScrapeResult.ScrapeResultBuilder()
				.withContentType(response.header("Content-Type"))
				.withHeaders(response.headers())
				.withPayload(response.body())
				.withStatusCode(response.code());

		return new ScrapeResultRepresentation.ScrapeResultRepresentationBuilder()
				.withSessionId(sessionUuid)
				.withTaskId(task.getTaskUuid())
				.withTaskType(task.getTaskType())
				.withResult(result)
				.build();
	}

	private static ScrapeResultRepresentation failedExpectationResult(String sessionUuid, ScrapeTask task,
			ScrapeStep step, List<FailedExpectation> failedExpectations) {
		ModelBuilder<ScrapeError> error = new ScrapeError.ScrapeErrorBuilder()
				.withFailedExpectations(failedExpectations)
				.withScrapeStep(step);

		return new ScrapeResultRepresentation.ScrapeResultRepresentationBuilder()
				.withSessionId(sessionUuid)
				.withTaskId(task.getTaskUuid())
				.withTaskType(task.getTaskType())
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
}
