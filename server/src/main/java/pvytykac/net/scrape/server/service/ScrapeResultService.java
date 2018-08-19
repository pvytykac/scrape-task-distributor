package pvytykac.net.scrape.server.service;

import pvytykac.net.scrape.model.v1.ScrapeError;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.enums.ScrapeType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Paly
 * @since 2018-08-07
 */
public interface ScrapeResultService {

    void awaitResults(String sessionUuid, List<ScrapeTask> tasks);

    Optional<ScrapeTask> getTask(String sessionUuid, String taskUuid);

    Optional<Long> processResult(String sessionUuid, ScrapeTask task, ScrapeResult result, ScrapeError error);

}
