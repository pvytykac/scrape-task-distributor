package pvytykac.net.scrape.server.service.impl;

import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.server.service.IcoService;
import pvytykac.net.scrape.server.service.TaskDistributionFacade;
import pvytykac.net.scrape.server.service.TaskService;
import pvytykac.net.scrape.server.service.TaskTypeService;

import java.util.Optional;

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
    public Optional<ScrapeTask> getScrapeTask(String taskType) {
        return Optional.empty();
    }

    @Override
    public Optional<PostScrapeStatusRepresentation> processScrapeTaskResult(ScrapeResultRepresentation dto) {
        return Optional.empty();
    }
}
