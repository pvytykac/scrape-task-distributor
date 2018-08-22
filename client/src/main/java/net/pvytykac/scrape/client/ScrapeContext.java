package net.pvytykac.scrape.client;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ScrapeContext {

	private final Map<String, Long> scrapeTypeTimeouts;

	public ScrapeContext() {
		this.scrapeTypeTimeouts = new HashMap<>();
	}

	public Set<String> getIgnoredTypes() {
		return scrapeTypeTimeouts.entrySet().stream()
				.filter(entry -> entry.getValue() >= nowUtcMs())
				.map(Map.Entry::getKey)
				.collect(Collectors.toSet());
	}

	public void addTimeout(String taskType, Long timeout) {
		scrapeTypeTimeouts.put(taskType, timeout);
	}

	private Long nowUtcMs() {
		return Instant.now(Clock.systemUTC())
				.toEpochMilli();
	}

	public Optional<Long> getNextTimeoutReset() {
		List<Long> timeouts = new ArrayList<>(scrapeTypeTimeouts.values());
		Collections.sort(timeouts);

		return timeouts.stream()
				.findFirst()
				.map(timeout -> timeout - System.currentTimeMillis())
				.filter(timeout -> timeout > 0);
	}
}
