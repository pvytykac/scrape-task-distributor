package net.pvytykac.scrape.client;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pvytykac.net.scrape.model.v1.ScrapeDefinition;

public class ScrapeHandler {

	public Container processScrape(ResponseWrapper response, ScrapeDefinition scrape, Container parameters) {
		Element document = response.html();
		processRecursive(scrape, parameters, new Elements(document));
		return parameters;
	}

	private static void processRecursive(ScrapeDefinition scrape, Container container, Elements elements) {
		switch (scrape.getScrapeType()) {
		case ELEMENT:
			elements.stream().findFirst().ifPresent(child -> scrape.getSubDefinitions().forEach(
					subScrape -> processRecursive(subScrape, container,
							new Elements(child.selectFirst(scrape.getSelector())))));
			break;
		case LIST:
			elements.forEach(el -> {
				Container subContainer = Container.listContainer();
				container.put(scrape.getStoreAs(), subContainer);
				if (scrape.getSubDefinitions() != null) {
					Elements children = scrape.getSelector() == null ? elements : el.select(scrape.getSelector());
					scrape.getSubDefinitions()
							.forEach(subScrape -> processRecursive(subScrape, subContainer, children));
				}
			});
			break;
		case ATTRIBUTE:
			elements.forEach(el -> container.put(scrape.getStoreAs(), el.attr(scrape.getSelector())));
			break;
		case TEXT:
			elements.forEach(el -> container.put(scrape.getStoreAs(), el.text()));
			break;
		case OBJECT:
			elements.forEach(el -> {
				Container subContainer = Container.objectContainer();
				container.put(scrape.getStoreAs(), subContainer);
				if (scrape.getSubDefinitions() != null) {
					scrape.getSubDefinitions()
							.forEach(subScrape -> processRecursive(subScrape, subContainer, new Elements(el)));
				}
			});
			break;
		default:
			throw new IllegalStateException("unsupported scrape type: '" + scrape.getScrapeType() + "'");
		}
	}
}