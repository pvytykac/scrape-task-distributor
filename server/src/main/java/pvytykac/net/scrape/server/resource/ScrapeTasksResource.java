package pvytykac.net.scrape.server.resource;

import io.dropwizard.hibernate.UnitOfWork;
import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.server.service.TaskDistributionFacade;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Optional;

import static javax.ws.rs.core.Response.status;

@Path("/v1/scrape-tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ScrapeTasksResource {

	private final TaskDistributionFacade taskDistributionFacade;

	public ScrapeTasksResource(TaskDistributionFacade taskDistributionFacade) {
		this.taskDistributionFacade = taskDistributionFacade;
	}

	@UnitOfWork
	@POST
	public Response getTask(@QueryParam("taskType") String taskType) {
		Optional<ScrapeTask> optional = taskDistributionFacade.getScrapeTask(taskType);

		return optional.map(task -> status(Status.OK).entity(task))
                .orElse(status(Status.NO_CONTENT))
                .build();
	}

	@UnitOfWork
	@POST
	@Path("/{taskUuid}/result")
	public Response processResult(
			@PathParam("taskUuid") String taskUuid,
			@Valid @NotNull ScrapeResultRepresentation result) {
        Optional<PostScrapeStatusRepresentation> optional = taskDistributionFacade.processScrapeTaskResult(result);

        return optional
                .map(status -> {
                    if (status.getTimeoutAction() == null) {
                        return status(Status.NO_CONTENT);
                    } else {
                        return status(Status.OK).entity(status);
                    }
                })
                .orElse(status(Status.NOT_FOUND))
                .build();
    }

}
