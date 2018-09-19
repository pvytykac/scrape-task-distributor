package net.pvytykac.scrape.client;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

public class ResponseWrapper {

	private final Response response;
	private String body;
	private Document document;
	private boolean bodyConsumed;

	public ResponseWrapper(Response response) {
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

	public Map<String, List<String>> headers() {
		return response.headers().toMultimap();
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
			this.document = Jsoup.parse(body(), response == null ? "" : request().url().toString());
		}

		return this.document;
	}
}
