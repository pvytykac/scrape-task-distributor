package pvytykac.net.scrape.server.service.impl;

import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation.PostScrapeStatusRepresentationBuilder;
import pvytykac.net.scrape.model.v1.ScrapeError;
import pvytykac.net.scrape.model.v1.ScrapeResult;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.TimeoutAction;
import pvytykac.net.scrape.server.service.IcoService;
import pvytykac.net.scrape.server.service.TaskDistributionFacade;
import pvytykac.net.scrape.server.service.TaskService;
import pvytykac.net.scrape.server.service.TaskTypeService;
import pvytykac.net.scrape.server.task.TaskType;
import pvytykac.net.scrape.server.task.TaskType.Status;

import java.util.Optional;
import java.util.Set;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class TaskDistributionFacadeImpl implements TaskDistributionFacade {

    private final IcoService icoService;
    private final TaskTypeService taskTypeService;
    private final TaskService taskService;

    public TaskDistributionFacadeImpl(IcoService icoService, TaskTypeService taskTypeService,
                                      TaskService taskService) {
        this.icoService = icoService;
        this.taskTypeService = taskTypeService;
        this.taskService = taskService;
    }

    @Override
    public Optional<ScrapeTask> getScrapeTask(String taskTypeId) {
        return taskTypeService.getTaskType(taskTypeId)
                .map(this::createScrapeTask);
    }

    @Override
    public Optional<PostScrapeStatusRepresentation> processScrapeTaskResult(ScrapeResultRepresentation dto) {
        return taskTypeService.getTaskType(dto.getTaskType())
                .map(taskType -> processScrapeResult(taskType, dto));
    }

    private ScrapeTask createScrapeTask(final TaskType taskType) {
        synchronized (taskType) {
            String offset = taskType.getOffsetIco();
            Set<Integer> applicableForms = taskType.getApplicableFormIds();

            return icoService.getIco(offset, applicableForms)
                .map(taskType::createScrapeTask)
                .orElse(null);
        }
    }

    private PostScrapeStatusRepresentation processScrapeResult(TaskType taskType, ScrapeResultRepresentation dto) {
        ScrapeError error = dto.getError();
        ScrapeResult result = dto.getResult();
        Status status = null;

        if (error != null && error.getClientException() != null) {
            status = taskType.processClientError(error.getClientException(), error.getStep());
        } else if (error != null && error.getFailedExpectations() != null && !error.getFailedExpectations().isEmpty()) {
            status = taskType.processFailedExpectations(error.getFailedExpectations(), error.getStep());
        } else if (result != null) {
            status = taskType.processSuccess(result);
        }

        PostScrapeStatusRepresentationBuilder response = new PostScrapeStatusRepresentationBuilder();

        if (status != null && status.isRetriable()) {
            response.withRetry(status.isRetriable());
        }

        if (status != null && status.getTimeout() != null && status.getTimeout() > 0L) {
            response.withTimeoutAction(new TimeoutAction.TimeoutActionBuilder()
                    .withTimeout(status.getTimeout() + dto.getRequestTimeUtcMs())
                    .withTaskType(dto.getTaskType())
                    .build());
        }

        return response.build();
    }
}
