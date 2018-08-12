package pvytykac.net.scrape.server.service;

import pvytykac.net.scrape.model.v1.ScrapeError;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.enums.ScrapeType;

import java.util.List;
import java.util.Map;

/**
 * @author Paly
 * @since 2018-08-07
 */
public interface ScrapeResultService {

    void awaitResults(String sessionUuid, List<ScrapeTask> tasks);

    boolean awaitsResult(String sessionUuid, String taskUuid);

    ScrapeTask getTask(String sessionUuid, String taskUuid);

    Map<ScrapeType, Long> processError(String sessionUuid, String taskUuid, ScrapeError error);

    Map<ScrapeType, Long> processResult(String sessionUuid, String taskUuid, ScrapeResult result);

}
