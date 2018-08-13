package net.pvytykac.scrape.client;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import pvytykac.net.scrape.model.v1.ScrapeStep;

public class ScrapeRequestBuilder {

	private final ScrapeStep step;
	private final Map<String, String> parameters;

	public ScrapeRequestBuilder(ScrapeStep step, Map<String, String> parameters) {
		this.step = step;
		this.parameters = parameters;
	}

	private Headers getHeaders() {
		return step.getHeaders() == null
				? Headers.of()
				: Headers.of(step.getHeaders());
	}

	private String getMethod() {
		return step.getMethod().toString();
	}

	private RequestBody getPayload() {
		if (step.getPayload() != null) {
			MediaType contentType = MediaType.parse(step.getContentType());
			return RequestBody.create(contentType, resolveParameters(step.getPayload()));
		} else {
			return null;
		}
	}

	private String getUrl() {
		return resolveParameters(step.getUri());
	}

	private String resolveParameters(String original) {
		return original == null
				? null
				: parameters.entrySet().stream()
						.reduce(original,
								(acc, entry) -> acc.replaceAll("\\$\\{" + entry.getKey() + "}", entry.getValue()),
								(acc, result) -> result);
	}

	public Request build() {
		return new Request.Builder()
				.headers(getHeaders())
				.method(getMethod(), getPayload())
				.url(getUrl())
				.build();

	}

}
