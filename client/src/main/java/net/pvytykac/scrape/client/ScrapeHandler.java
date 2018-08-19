package net.pvytykac.scrape.client;

import com.google.common.collect.ImmutableMap;
import org.jsoup.select.Elements;
import pvytykac.net.scrape.model.v1.Scrape;
import pvytykac.net.scrape.model.v1.enums.ScrapeType;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScrapeHandler {

	private final Map<ScrapeType, ScrapeStrategy> scrapers;

	public ScrapeHandler() {
		this.scrapers = ImmutableMap.<ScrapeType, ScrapeStrategy>builder()
				.put(ScrapeType.HEADER, (response, target) -> Optional.ofNullable(response.header(target)))
				.put(ScrapeType.HREF, HREF_SCRAPER)
				.build();
	}

	public Map<String, String> processScrapes(ResponseWrapper response, List<Scrape> scrapes) {
		return Optional.ofNullable(scrapes)
				.orElse(Collections.emptyList())
				.stream()
				.map(scrape -> processScrape(response, scrape))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}

	private Optional<Entry<String, String>> processScrape(ResponseWrapper response, Scrape scrape) {
		return scrapers.get(scrape.getType())
				.scrape(response, scrape.getTarget())
				.map(value -> new SimpleEntry<>(scrape.getStoreAs(), value));
	}

	interface ScrapeStrategy {
		Optional<String> scrape(ResponseWrapper response, String target);
	}

	private static final CssScrapeStrategy HREF_SCRAPER = new CssAttributeScrapeStrategy("href");

	private abstract static class CssScrapeStrategy implements ScrapeStrategy {

		abstract Optional<String> toString(Optional<Elements> elements);

		@Override
		public Optional<String> scrape(ResponseWrapper response, String target) {
			return toString(Optional.ofNullable(response.html().select(target)));
		}
	}

	private static final class CssAttributeScrapeStrategy extends CssScrapeStrategy {

		private final String attribute;

		public CssAttributeScrapeStrategy(String attribute) {
			this.attribute = attribute;
		}

		@Override
		Optional<String> toString(Optional<Elements> elements) {
			String attributes = elements.map(Elements::stream)
					.orElse(Stream.empty())
					.map(el -> el.attr(attribute))
					.filter(Objects::nonNull)
					.reduce("", (acc, attr) -> acc + attr + ",");

			attributes = attributes.substring(0, attributes.length() - 1);

			return attributes.trim().isEmpty()
					? Optional.empty()
					: Optional.of(attributes);
		}
	}

}