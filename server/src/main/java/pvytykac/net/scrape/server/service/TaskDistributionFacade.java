package pvytykac.net.scrape.server.service;

import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeSessionRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeTask;

import java.util.Optional;

/**
 * @author Paly
 * @since 2018-08-07
 */
public interface TaskDistributionFacade {

    Optional<ScrapeTask> getScrapeTask(String taskType);

    Optional<PostScrapeStatusRepresentation> processScrapeTaskResult(ScrapeResultRepresentation result);

}
