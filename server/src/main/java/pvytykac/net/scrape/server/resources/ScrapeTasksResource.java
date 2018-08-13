package pvytykac.net.scrape.server.resources;

import org.hibernate.validator.constraints.Range;
import pvytykac.net.scrape.model.v1.PostScrapeStatusRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeResultRepresentation;
import pvytykac.net.scrape.model.v1.ScrapeSessionRepresentation;
import pvytykac.net.scrape.server.service.TaskDistributionFacade;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Optional;
import java.util.Set;

import static javax.ws.rs.core.Response.status;

@Path("/v1/scrape-tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ScrapeTasksResource {

	private final TaskDistributionFacade taskDistributionFacade;

	public ScrapeTasksResource(TaskDistributionFacade taskDistributionFacade) {
		this.taskDistributionFacade = taskDistributionFacade;
	}

	@GET
	public Response getTasks(
			@QueryParam("ignoredType") Set<String> ignoredTypes,
			@Range(min = 1L, max = 10L) @QueryParam("limit") @DefaultValue("1") Integer limit) {
		Optional<ScrapeSessionRepresentation> optional = taskDistributionFacade.getScrapeTasks(ignoredTypes, limit);

		return optional.map(task -> status(Status.OK).entity(task))
                .orElse(status(Status.NO_CONTENT))
                .build();
	}

	@POST
	@Path("/{taskUuid}/result")
	public Response postResult(
			@PathParam("taskUuid") String taskUuid,
			@Valid @NotNull ScrapeResultRepresentation result) {
        Optional<PostScrapeStatusRepresentation> optional = taskDistributionFacade.processScrapeResult(result);

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
