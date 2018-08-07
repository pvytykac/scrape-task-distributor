package pvytykac.net.scrape.server.facade;

import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeTaskRepresentation;
import pvytykac.net.scrape.model.v1.enums.TaskType;

import java.util.Optional;
import java.util.Set;

/**
 * @author Paly
 * @since 2018-08-07
 */
public interface TaskFacade {

    Optional<ScrapeTaskRepresentation> getScrapeTasks(Set<TaskType> ignoredTypes, Integer limit);

    Optional<PostScrapeStatusRepresentation> processScrapeResult(ScrapeResultRepresentation result);

}
