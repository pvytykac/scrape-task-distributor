package pvytykac.net.scrape.server.facade.impl;

import pvytykac.net.scrape.model.v1.ScrapeError;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.enums.ScrapeType;
import pvytykac.net.scrape.server.facade.TaskResultProcessor;

import java.util.List;
import java.util.Map;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class TaskResultProcessorImpl implements TaskResultProcessor {

    @Override
    public void awaitResults(String sessionUuid, List<ScrapeTask> tasks) {

    }

    @Override
    public boolean awaitsResult(String sessionUuid, String taskUuid) {
        return false;
    }

    @Override
    public ScrapeTask getTask(String sessionUuid, String taskUuid) {
        return null;
    }

    @Override
    public Map<ScrapeType, Long> processError(String sessionUuid, String taskUuid, ScrapeError error) {
        return null;
    }

    @Override
    public Map<ScrapeType, Long> processResult(String sessionUuid, String taskUuid, ScrapeResult result) {
        return null;
    }
}
