package net.pvytykac.scrape.client;

import java.util.Set;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeTaskRepresentation;
import pvytykac.net.scrape.model.v1.SupportedScrapeTypesRepresentation;

public interface ScrapeTaskDistributorClientV1 {

	@RequestLine("GET /v1/scrape-types")
	@Headers("Content-Type: application/json")
	SupportedScrapeTypesRepresentation getSupportedScrapeTypes();

	@RequestLine("GET /v1/scrape-tasks?ignoredType={ignoredTypes}&limit={limit}")
	@Headers("Content-Type: application/json")
	ScrapeTaskRepresentation getScrapeTasks(
			@Param("limit") Integer limit,
			@Param("ignoredTypes") Set<String> ignoredTypes);

	@RequestLine("POST /v1/scrape-tasks/{taskUuid}/result")
	@Headers("Content-Type: application/json")
	PostScrapeStatusRepresentation postScrapeResult(
			@Param("taskUuid") String taskUuid,
			ScrapeResultRepresentation representation);

}
