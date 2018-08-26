package net.pvytykac.scrape.client;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import okhttp3.OkHttpClient;

public class ScrapeContext {

	private final OkHttpClient http;
	private final ScrapeTaskDistributorClientV1 client;
	private final Map<String, Long> timeouts;

	public ScrapeContext(OkHttpClient http, ScrapeTaskDistributorClientV1 client) {
		this.http = http;
		this.client = client;
		this.timeouts = new HashMap<>(20);
	}

	public OkHttpClient getHttp() {
		return http;
	}

	public ScrapeTaskDistributorClientV1 getClient() {
		return client;
	}

	public void setTimeout(String taskType, Long timeout) {
		if (timeouts.get(taskType) == null || timeouts.get(taskType) < nowUtcMs()) {
			synchronized (timeouts) {
				if (timeouts.get(taskType) == null || timeouts.get(taskType) < nowUtcMs()) {
					timeouts.put(taskType, timeout);
				}
			}
		}
	}

	public Optional<Long> getNextTimeoutReset(String taskType) {
		return timeouts.entrySet().stream()
				.filter(e -> e.getKey().equals(taskType))
				.map(Map.Entry::getValue)
				.findFirst()
				.map(timeout -> timeout - nowUtcMs())
				.filter(timeout -> timeout > 0L);
	}

	private long nowUtcMs() {
		return Instant.now(Clock.systemUTC())
				.toEpochMilli();
	}
}
