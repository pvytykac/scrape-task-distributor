package net.pvytykac.scrape.client;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import okio.Buffer;
import okio.BufferedSink;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseWrapper {

	private static final Logger LOG = LoggerFactory.getLogger(ResponseWrapper.class);

	private final Response response;
	private String body;
	private Document document;
	private boolean bodyConsumed;
	private Map<String, String> headers;

	public ResponseWrapper(Response response) {
		BufferedSink sink = new Buffer();
		try {
			response.request().body().writeTo(sink);
		} catch (Exception ignored) {
			// ignored
	    }

		LOG.info("request: {}\nbody: {}", response.request().url().uri(), sink.buffer().readUtf8());
		this.response = response;
	}

	public Request request() {
		return response.request();
	}

	public Protocol protocol() {
		return response.protocol();
	}

	public int code() {
		return response.code();
	}

	public String header(String name) {
		return response.header(name);
	}

	public Long time() {
		return response.sentRequestAtMillis();
	}

	public Map<String, String> headers() {
		if (headers == null) {
			this.headers = response.headers().toMultimap()
				.entrySet().stream()
				.map(entry -> {
					String flattenValues = entry.getValue()
							.stream()
							.reduce("", (acc, val) -> acc + val + ",");
					return new SimpleEntry<>(entry.getKey(), flattenValues.substring(0, flattenValues.length() - 1));
				})
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		}

		return this.headers;
	}

	public String body() {
		if (!this.bodyConsumed) {
			try {
				this.body = response.body().string();
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			} finally {
				this.bodyConsumed = true;
			}
		}

		return body;
	}

	public Document html() {
		if (this.document == null) {
			this.document = Jsoup.parse(body(), request().url().toString());
		}

		return this.document;
	}
}
