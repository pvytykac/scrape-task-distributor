package pvytykac.net.scrape.server.facade.impl;

import com.google.common.collect.ImmutableMap;
import pvytykac.net.scrape.model.v1.PostScrapeAction;
import pvytykac.net.scrape.model.v1.PostScrapeAction.PostScrapeActionBuilder;
import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation.PostScrapeStatusRepresentationBuilder;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.ScrapeTaskRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeTaskRepresentation.ScrapeTaskRepresentationBuilder;
import pvytykac.net.scrape.model.v1.enums.ActionType;
import pvytykac.net.scrape.model.v1.enums.ScrapeType;
import pvytykac.net.scrape.model.v1.enums.TaskType;
import pvytykac.net.scrape.server.facade.TaskFacade;
import pvytykac.net.scrape.server.facade.TaskQueue;
import pvytykac.net.scrape.server.facade.TaskResultProcessor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class TaskFacadeImpl implements TaskFacade {

    private final TaskQueue taskQueue;
    private final TaskResultProcessor taskResultProcessor;

    public TaskFacadeImpl(TaskQueue taskQueue, TaskResultProcessor taskResultProcessor) {
        this.taskQueue = taskQueue;
        this.taskResultProcessor = taskResultProcessor;
    }

    @Override
    public Optional<ScrapeTaskRepresentation> getScrapeTasks(Set<TaskType> ignoredTypes, Integer limit) {
        List<ScrapeTask> dequeued = taskQueue.dequeue(ignoredTypes, limit);

        Optional<ScrapeTaskRepresentation> tasks = (dequeued == null || dequeued.isEmpty())
                ? Optional.empty()
                : Optional.of(
                    new ScrapeTaskRepresentationBuilder()
                        .withSessionUuid(UUID.randomUUID().toString())
                        .withTasks(dequeued)
                        .build()
                  );

        tasks.ifPresent(t -> taskResultProcessor.awaitResults(t.getSessionUuid(), t.getTasks()));

        return tasks;
    }

    @Override
    public Optional<PostScrapeStatusRepresentation> processScrapeResult(ScrapeResultRepresentation dto) {
        if (!taskResultProcessor.awaitsResult(dto.getSessionUuid(), dto.getTaskUuid())) {
            return Optional.empty();
        }

        Map<ScrapeType, Long> typesToIgnore;
        if (dto.getError() != null) {
            ScrapeTask task = taskResultProcessor.getTask(dto.getSessionUuid(), dto.getTaskUuid());

            typesToIgnore = taskResultProcessor.processError(dto.getSessionUuid(), dto.getTaskUuid(), dto.getError());

            if (!typesToIgnore.isEmpty()) {
                taskQueue.returnToQueue(task);
            }
        } else {
            typesToIgnore = taskResultProcessor.processResult(dto.getSessionUuid(), dto.getTaskUuid(), dto.getResult());
        }

        List<PostScrapeAction> actions = typesToIgnore.entrySet().stream()
                .map(type ->
                    new PostScrapeActionBuilder()
                            .withActionType(ActionType.IGNORE_TASK_TYPE)
                            .withParameters(ImmutableMap.of(
                                    "type", type.getKey().toString(),
                                    "timeout", type.getValue().toString())
                            ).build()
                ).collect(Collectors.toList());

        return new PostScrapeStatusRepresentationBuilder()
                .withActions(actions)
                .buildAsOptional();
    }

}
