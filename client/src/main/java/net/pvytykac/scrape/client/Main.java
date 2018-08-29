package net.pvytykac.scrape.client;

import java.util.Scanner;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import okhttp3.OkHttpClient;

public class Main {

	public static void main(String[] args) throws Exception {
		ScrapeTaskDistributorClientV1 client = createApiClient(args[0]);
		OkHttpClient http = createHttpClient();
		ScrapeContext context = new ScrapeContext(http, client);
		RootScraper root = new RootScraper(context);

		root.start();

		try (Scanner scanner = new Scanner(System.in)) {
			String cmd;
			while ((cmd = scanner.nextLine()) != null && !cmd.equalsIgnoreCase("exit")) {
				// no-op
			}
		} catch (Exception ignored) {
			//no-op
		}

		root.stop();
	}

	private static ScrapeTaskDistributorClientV1 createApiClient(String target) {
		return new Feign.Builder()
				.logger(new Slf4jLogger())
				.encoder(new JacksonEncoder())
				.decoder(new JacksonDecoder())
				.target(ScrapeTaskDistributorClientV1.class, target);
	}

	private static OkHttpClient createHttpClient() {
		return new OkHttpClient.Builder()
				.followRedirects(false)
				.followSslRedirects(false)
				.addInterceptor(new UserAgentInterceptor())
				.build();
	}
}
