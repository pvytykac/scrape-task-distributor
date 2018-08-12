package pvytykac.net.scrape.server.service;

import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeTaskRepresentation;

import java.util.Optional;
import java.util.Set;

/**
 * @author Paly
 * @since 2018-08-07
 */
public interface TaskDistributionFacade {

    Optional<ScrapeTaskRepresentation> getScrapeTasks(Set<String> ignoredTypes, int limit);

    Optional<PostScrapeStatusRepresentation> processScrapeResult(ScrapeResultRepresentation result);

    Set<String> getScrapeTypesSupportedByPlatform();

}
