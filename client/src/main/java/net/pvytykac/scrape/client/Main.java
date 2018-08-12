package net.pvytykac.scrape.client;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

public class Main {

	public static void main(String[] args) throws Exception {
		ScrapeTaskDistributorClientV1 client = new Feign.Builder()
				.logger(new Slf4jLogger())
				.encoder(new JacksonEncoder())
				.decoder(new JacksonDecoder())
				.target(ScrapeTaskDistributorClientV1.class, "http://localhost:9080");

		Thread t = new Thread(new Scraper(client));
		t.start();
		t.join();
	}
}
