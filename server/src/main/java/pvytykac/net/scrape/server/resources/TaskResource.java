package pvytykac.net.scrape.server.resources;

import org.hibernate.validator.constraints.Range;
import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeTaskRepresentation;
import pvytykac.net.scrape.model.v1.enums.TaskType;
import pvytykac.net.scrape.server.facade.TaskFacade;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Optional;
import java.util.Set;

import static javax.ws.rs.core.Response.status;

@Path("/v1/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

	private final TaskFacade taskFacade;

	public TaskResource(TaskFacade taskFacade) {
		this.taskFacade = taskFacade;
	}

	@GET
	public Response getTasks(
			@QueryParam("ignoredType") Set<TaskType> ignoredTypes,
			@Range(min = 1L, max = 10L) @QueryParam("limit") Integer limit) {
		Optional<ScrapeTaskRepresentation> optional = taskFacade.getScrapeTasks(ignoredTypes, limit);

		return optional.map(task -> status(Status.OK).entity(task))
                .orElse(status(Status.NO_CONTENT))
                .build();
	}

	@POST
	@Path("/{taskUuid}/result")
	public Response postResult(
			@PathParam("taskUuid") String taskUuid,
			@Valid @NotNull ScrapeResultRepresentation result) {
        Optional<PostScrapeStatusRepresentation> optional = taskFacade.processScrapeResult(result);

        return optional
                .map(status -> {
                    if (status.getActions().isEmpty()) {
                        return status(Status.NO_CONTENT);
                    } else {
                        return status(Status.OK).entity(status);
                    }
                })
                .orElse(status(Status.NOT_FOUND))
                .build();
    }

}
