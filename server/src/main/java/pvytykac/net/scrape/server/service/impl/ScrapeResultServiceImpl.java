package pvytykac.net.scrape.server.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pvytykac.net.scrape.model.v1.ScrapeError;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.server.service.ScrapeResultHandler;
import pvytykac.net.scrape.server.service.ScrapeResultHandler.ErrorStatus;
import pvytykac.net.scrape.server.service.ScrapeResultService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class ScrapeResultServiceImpl implements ScrapeResultService {

    private static final Logger LOG = LoggerFactory.getLogger(ScrapeResultServiceImpl.class);

    private final Map<String, Map<String, ScrapeTask>> sessionTaskMap;
    private final Map<String, ScrapeResultHandler> resultHandlers;

    public ScrapeResultServiceImpl() {
        this.sessionTaskMap = new HashMap<>();
        this.resultHandlers = new HashMap<>();
    }

    @Override
    public void awaitResults(String sessionUuid, List<ScrapeTask> tasks) {
        sessionTaskMap.computeIfAbsent(sessionUuid, (any) -> new HashMap<>());
        tasks.forEach(task -> sessionTaskMap.get(sessionUuid).put(task.getTaskUuid(), task));
    }

    @Override
    public Optional<ScrapeTask> getTask(String sessionUuid, String taskUuid) {
        return Optional.ofNullable(sessionTaskMap.get(sessionUuid))
                .map(taskMap -> taskMap.remove(taskUuid));
    }

    @Override
    public Optional<Long> processResult(String sessionUuid, ScrapeTask task, ScrapeResult result, ScrapeError error) {
        LOG.info("processing '{}' result for scrape task '{}' of type '{}' from scrape session '{}'",
                error == null ? "success" : "error", task.getTaskUuid(), task.getTaskType(), sessionUuid);

        ScrapeResultHandler handler = resultHandlers.get(task.getTaskType());
        if (error != null) {
            ErrorStatus status = handler.processError(error);

            // todo: put back to task queue, if retriable

            return Optional.ofNullable(status.getTimeout());
        } else {
            ScrapeResultHandler.SuccessStatus status = handler.processSuccess(result);

            return Optional.empty();
        }
    }
}
