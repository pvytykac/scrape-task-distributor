package net.pvytykac.scrape.client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RootScraper {

	private static final Logger LOG = LoggerFactory.getLogger(RootScraper.class);

	private final ScrapeContext context;
	private final Map<String, TaskTypeScraper> scrapers;
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	public RootScraper(ScrapeContext context) {
		this.context = context;
		this.scrapers = new HashMap<>(20);
	}

	public void start() {
		LOG.info("starting scrapers");
		executor.scheduleAtFixedRate(this::startNewTaskTypes, 0L, 1L, TimeUnit.MINUTES);
	}

	public void stop() {
		LOG.info("shutting down scrapers");
		executor.shutdownNow();
		scrapers.values().forEach(TaskTypeScraper::stop);
	}

	private void startNewTaskTypes() {
		try {
			LOG.debug("fetching task types");
			context.getClient().getSupportedScrapeTypes().getSupportedScrapeTypes()
				.forEach(taskType -> {
					synchronized (scrapers) {
						if (!scrapers.containsKey(taskType)) {
							LOG.debug("starting scraper for task type '{}'", taskType);

							TaskTypeScraper scraper = new TaskTypeScraper(context, taskType);
							scrapers.put(taskType, scraper);
							scraper.start();
						}
					}
				});
		} catch (Exception ex) {
			LOG.error("Error when fetching task types", ex);
		}
	}
}
