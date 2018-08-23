package net.pvytykac.scrape.client;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.TimeoutAction;

public class Scraper implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(Scraper.class);

	private final ScrapeTaskDistributorClientV1 client;
	private final ScrapeTaskProcessor scrapeTaskProcessor;
	private final OkHttpClient http;
	private boolean running = true;

	public Scraper(ScrapeTaskDistributorClientV1 client) {
		this.client = client;
		this.http = new OkHttpClient.Builder()
				.followRedirects(false)
				.followSslRedirects(false)
				.build();

		this.scrapeTaskProcessor = new ScrapeTaskProcessor(http);
	}

	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		ScrapeContext context = new ScrapeContext();

		ScrapeTask task = null;
		PostScrapeStatusRepresentation status = null;
		while (running) {
			try {
				sleep(context, 0L);
				if (status == null || !status.getRetry()) {
					task = client.getScrapeSession("RES");
				}

				if (task != null) {
					try {
						ScrapeResultRepresentation result = scrapeTaskProcessor
								.processTask(UUID.randomUUID().toString(), task);
						status = client.postScrapeResult(task.getTaskUuid(), result);
						if (status != null) {
							TimeoutAction action = status.getTimeoutAction();
							context.addTimeout(action.getTaskType(), action.getTimeout());
						}
					} catch (Exception ex) {
						LOG.error("Error when processing task '{}'", task.getTaskUuid(), ex);
					}
				} else {
					sleep(context, 5000L);
				}
			} catch (Exception ex) {
				LOG.error("Error when fetching task", ex);
			}
		}
	}

	private static void sleep(ScrapeContext context, Long defaultSleep) {
		try {
			Long sleep = context.getNextTimeoutReset()
					.orElse(defaultSleep);
			Thread.sleep(sleep);
		} catch (InterruptedException ignored) {}
	}
}
