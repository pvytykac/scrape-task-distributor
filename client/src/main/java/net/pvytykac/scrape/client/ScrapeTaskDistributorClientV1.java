package net.pvytykac.scrape.client;

import java.util.Set;

import feign.Param;
import feign.RequestLine;
import pvytykac.net.scrape.model.v1.ScrapeTaskRepresentation;
import pvytykac.net.scrape.model.v1.SupportedScrapeTypesRepresentation;

public interface ScrapeTaskDistributorClientV1 {

	@RequestLine("GET /v1/scrape-types")
	SupportedScrapeTypesRepresentation getSupportedScrapeTypes();

	@RequestLine("GET /v1/scrape-tasks?ignoredType={ignoredTypes}&limit={limit}")
	ScrapeTaskRepresentation getScrapeTasks(
			@Param("limit") Integer limit,
			@Param("ignoredTypes") Set<String> ignoredTypes);

}
