package net.pvytykac.scrape.client;

import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeSessionRepresentation;

public class Scraper implements Runnable {

	private final ScrapeTaskDistributorClientV1 client;
	private final ScrapeTaskProcessor scrapeTaskProcessor;
	private boolean running = true;

	public Scraper(ScrapeTaskDistributorClientV1 client) {
		this.client = client;
		this.scrapeTaskProcessor = new ScrapeTaskProcessor();
	}

	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		ScrapeContext context = new ScrapeContext();

		while (running) {
			ScrapeSessionRepresentation session = client.getScrapeSession(5, context.getIgnoredTypes());

			if (session != null) {
				session.getTasks()
						.stream()
						.forEach(task -> {
							ScrapeResultRepresentation result = scrapeTaskProcessor.processTask(session.getSessionUuid(), task);
							PostScrapeStatusRepresentation status = client.postScrapeResult(task.getTaskUuid(), result);
							// todo: process status
						});
			} else {
				sleep(context);
			}
		}
	}

	private static void sleep(ScrapeContext context) {
		try {
			Thread.sleep(context.getNextTimeoutReset()
					.orElse(5000L));
		} catch (InterruptedException ignored) {}
	}
}
