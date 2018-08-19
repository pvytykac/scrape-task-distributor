package pvytykac.net.scrape.server.service.impl;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pvytykac.net.scrape.model.v1.ScrapeError;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeStep;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.server.ScrapeTaskConfiguration;
import pvytykac.net.scrape.server.db.repository.RepositoryFacade;
import pvytykac.net.scrape.server.service.ScrapeResultHandler;
import pvytykac.net.scrape.server.service.ScrapeResultHandler.Status;
import pvytykac.net.scrape.server.service.ScrapeResultService;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class ScrapeResultServiceImpl implements ScrapeResultService {

    private static final Logger LOG = LoggerFactory.getLogger(ScrapeResultServiceImpl.class);

    private final Map<String, Map<String, ScrapeTask>> sessionTaskMap;
    private final Map<String, ScrapeResultHandler> resultHandlers;

    public ScrapeResultServiceImpl(List<ScrapeTaskConfiguration> configurations, RepositoryFacade repositoryFacade) {
    	try {
			this.sessionTaskMap = new HashMap<>();
			this.resultHandlers = configurations.stream()
					.map(cfg -> {
						try {
							@SuppressWarnings("unchecked")
							Class<ScrapeResultHandler> clazz = (Class<ScrapeResultHandler>) Class.forName(cfg.getHandlerClass());
							ScrapeResultHandler handler = clazz.getConstructor(RepositoryFacade.class)
									.newInstance(repositoryFacade);

							return new SimpleEntry<>(cfg.getScrapeType(), handler);
						} catch (Exception ex) {
							throw new RuntimeException(ex);
						}
					})
					.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		} catch (Exception ex) {
    		throw new RuntimeException(ex);
		}
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
            Long timeout = null;
            ScrapeStep step = error.getStep();

            if (error.getClientException() != null) {
                timeout = handler.processClientError(error.getClientException(), step)
                        .getTimeout();
            } else if (error.getFailedExpectations() != null && !error.getFailedExpectations().isEmpty()) {
                timeout = handler.processFailedExpectations(error.getFailedExpectations(), step)
                        .getTimeout();
            }

            return Optional.ofNullable(timeout);
        } else {
            Status status = handler.processSuccess(result);
            return Optional.empty();
        }
    }
}
