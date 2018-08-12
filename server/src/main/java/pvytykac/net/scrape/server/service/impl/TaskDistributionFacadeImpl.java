package pvytykac.net.scrape.server.service.impl;

import com.google.common.collect.ImmutableList;
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
import pvytykac.net.scrape.server.service.IcoService;
import pvytykac.net.scrape.server.service.ScrapeResultService;
import pvytykac.net.scrape.server.service.ScrapeTaskService;
import pvytykac.net.scrape.server.service.ScrapeTypeService;
import pvytykac.net.scrape.server.service.TaskDistributionFacade;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.pvytykac.scrape.util.CollectionsUtil.removeAndProcessAllAboveLimit;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class TaskDistributionFacadeImpl implements TaskDistributionFacade {

    private final IcoService icoService;
    private final ScrapeTypeService scrapeTypeService;
    private final ScrapeTaskService scrapeTaskService;
    private final ScrapeResultService scrapeResultService;

    public TaskDistributionFacadeImpl(IcoService icoService, ScrapeTypeService scrapeTypeService,
            ScrapeTaskService scrapeTaskService, ScrapeResultService scrapeResultService) {
        this.icoService = icoService;
        this.scrapeTypeService = scrapeTypeService;
        this.scrapeTaskService = scrapeTaskService;
        this.scrapeResultService = scrapeResultService;
    }

    @Override
    public Optional<ScrapeTaskRepresentation> getScrapeTasks(Set<TaskType> ignoredTypes, int limit) {
        List<ScrapeTask> cachedTasks = scrapeTaskService.getScrapeTasks(ignoredTypes, limit);
        List<ScrapeTask> newTasks = createNewTasks(limit - cachedTasks.size());

        String sessionUuid = UUID.randomUUID().toString();
        List<ScrapeTask> tasks = ImmutableList.<ScrapeTask>builder()
                .addAll(cachedTasks)
                .addAll(newTasks)
                .build();

        Optional<ScrapeTaskRepresentation> representation = (cachedTasks.isEmpty() && newTasks.isEmpty())
                ? Optional.empty()
                : Optional.of(toRepresentation(sessionUuid, tasks));

        scrapeResultService.awaitResults(sessionUuid, tasks);

        return representation;
    }

    @Override
    public Optional<PostScrapeStatusRepresentation> processScrapeResult(ScrapeResultRepresentation dto) {
        if (!scrapeResultService.awaitsResult(dto.getSessionUuid(), dto.getTaskUuid())) {
            return Optional.empty();
        }

        Map<ScrapeType, Long> typesToIgnore;
        if (dto.getError() != null) {
            ScrapeTask task = scrapeResultService.getTask(dto.getSessionUuid(), dto.getTaskUuid());

            typesToIgnore = scrapeResultService.processError(dto.getSessionUuid(), dto.getTaskUuid(), dto.getError());

            if (!typesToIgnore.isEmpty()) {
                scrapeTaskService.returnScrapeTask(task);
            }
        } else {
            typesToIgnore = scrapeResultService.processResult(dto.getSessionUuid(), dto.getTaskUuid(), dto.getResult());
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

    @Override
    public Set<String> getSupportedScrapeTypes() {
        return scrapeTypeService.getScrapeTypes();
    }

    private List<ScrapeTask> createNewTasks(int limit) {
        List<ScrapeTask> tasks = Collections.emptyList();

        if (limit > 0) {
            tasks = icoService.getNextIco()
                    .map(scrapeTypeService::createScrapeTasks)
                    .orElse(Stream.empty())
                    .collect(Collectors.toList());

            removeAndProcessAllAboveLimit(tasks, limit, scrapeTaskService::returnScrapeTask);
        }

        return tasks;
    }

    private ScrapeTaskRepresentation toRepresentation(String sessionUuid, List<ScrapeTask> tasks) {
        return new ScrapeTaskRepresentationBuilder()
                .withSessionUuid(sessionUuid)
                .withTasks(tasks)
                .build();
    }
}
