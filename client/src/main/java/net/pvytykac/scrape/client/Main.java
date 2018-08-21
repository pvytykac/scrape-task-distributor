package net.pvytykac.scrape.client;

import java.util.Scanner;

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
				.target(ScrapeTaskDistributorClientV1.class, args[0]);

		Thread t = new Thread(new Scraper(client));
		t.start();

		try (Scanner scanner = new Scanner(System.in)) {
			String cmd;
			while ((cmd = scanner.nextLine()) == null || !cmd.equalsIgnoreCase("exit")) {
				// no-op
			}
		} catch (Exception ignored) {
			//no-op
		} finally {
			t.interrupt();
		}
	}
}
