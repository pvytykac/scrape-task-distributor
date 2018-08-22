package net.pvytykac.scrape.client;

import okhttp3.OkHttpClient;
import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.TimeoutAction;

import java.util.UUID;

public class Scraper implements Runnable {

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

		while (running) {
			ScrapeTask task = client.getScrapeSession("RES");

			if (task != null) {
				try {
					ScrapeResultRepresentation result = scrapeTaskProcessor
							.processTask(UUID.randomUUID().toString(), task);
					PostScrapeStatusRepresentation status = client
							.postScrapeResult(task.getTaskUuid(), result);
					if (status != null) {
						TimeoutAction action = status.getTimeoutAction();
						context.addTimeout(action.getTaskType(), action.getTimeout());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
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
