package pvytykac.net.scrape.server.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import pvytykac.net.scrape.model.v1.ScrapeError;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.enums.ScrapeType;
import pvytykac.net.scrape.server.service.ScrapeResultService;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class ScrapeResultServiceImpl implements ScrapeResultService {

    private final Map<String, List<ScrapeTask>> sessionTaskMap = new HashMap<>();

    @Override
    public void awaitResults(String sessionUuid, List<ScrapeTask> tasks) {
        sessionTaskMap.computeIfAbsent(sessionUuid, (any) -> new ArrayList<>());
        sessionTaskMap.get(sessionUuid).addAll(tasks);
    }

    @Override
    public boolean awaitsResult(String sessionUuid, String taskUuid) {
        return sessionTaskMap.containsKey(sessionUuid)
                && sessionTaskMap.get(sessionUuid)
                        .stream()
                        .map(ScrapeTask::getTaskUuid)
                        .anyMatch(taskUuid::equals);
    }

    @Override
    public ScrapeTask getTask(String sessionUuid, String taskUuid) {
        return Optional.ofNullable(sessionTaskMap.get(sessionUuid))
                .map(list -> list.stream()
                    .filter(task -> task.getTaskUuid().equals(taskUuid))
                    .findFirst()
                    .orElse(null))
                .orElse(null);
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
