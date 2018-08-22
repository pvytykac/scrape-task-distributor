package pvytykac.net.scrape.server.resources;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pvytykac.net.scrape.model.v1.SupportedScrapeTypesRepresentation;
import pvytykac.net.scrape.server.service.TaskTypeService;

@Path("/v1/scrape-types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ScrapeTypesResource {

    private final TaskTypeService service;

    public ScrapeTypesResource(TaskTypeService service) {
        this.service = service;
    }

    @GET
    public Response getSupportedTaskTypes() {
        Set<String> types = service.getTaskTypes();

        Response.ResponseBuilder builder = types.isEmpty()
            ? Response.noContent()
            : Response.ok(new SupportedScrapeTypesRepresentation.SupportedScrapeTypesRepresentationBuilder()
                .withSupportedScrapeTypes(types)
                .build());

        return builder.build();
    }
}
