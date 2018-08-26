package net.pvytykac.scrape.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.TimeoutAction;

public class TaskTypeScraper implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(TaskTypeScraper.class);

	private final ScrapeContext context;
	private final String taskType;
	private final ScrapeTaskProcessor processor;
	private boolean running;
	private Thread thread;

	public TaskTypeScraper(ScrapeContext context, String taskType) {
		this.context = context;
		this.taskType = taskType;
		this.processor = new ScrapeTaskProcessor(context.getHttp());
	}

	public void start() {
		this.thread = new Thread(this, taskType.toLowerCase() + "-scrape-thread");
		this.running = true;
		this.thread.start();
	}

	public void stop() {
		this.running = false;
		this.thread.interrupt();
	}

	@Override
	public void run() {
		LOG.info("started scraping task type '{}'", taskType);
		ScrapeTask task = null;
		PostScrapeStatusRepresentation status = null;

		while (running) {
			try {
				waitForTimeoutOrSleep(0L);
				if (status == null || !status.getRetry()) {
					task = context.getClient().getScrapeSession(taskType);
				}

				if (task != null) {
					status = processTask(task);
				} else {
					waitForTimeoutOrSleep(5000L);
				}
			} catch (Exception ex) {
				LOG.error("Error when fetching task", ex);
			}
		}
	}

	private PostScrapeStatusRepresentation processTask(ScrapeTask task) {
		try {
			ScrapeResultRepresentation result = processor.processTask(task);
			PostScrapeStatusRepresentation status = context.getClient().postScrapeResult(task.getTaskUuid(), result);
			if (status != null) {
				TimeoutAction action = status.getTimeoutAction();
				context.setTimeout(action.getTaskType(), action.getTimeout());
			}

			return status;
		} catch (Exception ex) {
			LOG.error("Error when processing task '{}'", task.getTaskUuid(), ex);
			return null;
		}
	}

	private void waitForTimeoutOrSleep(Long defaultSleep) {
		try {
			Long sleep = context.getNextTimeoutReset(taskType)
					.orElse(defaultSleep);
			Thread.sleep(sleep);
		} catch (InterruptedException ignored) {}
	}

}
