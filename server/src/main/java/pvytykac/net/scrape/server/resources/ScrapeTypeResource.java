package pvytykac.net.scrape.server.resources;

import com.google.common.collect.ImmutableMap;
import pvytykac.net.scrape.server.service.ScrapeTypeService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/v1/scrape-types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ScrapeTypeResource {

    private final ScrapeTypeService service;

    public ScrapeTypeResource(ScrapeTypeService service) {
        this.service = service;
    }

    @GET
    public Response getSupportedTaskTypes() {
        Set<String> types = service.getScrapeTypes();

        Response.ResponseBuilder builder = types.isEmpty()
            ? Response.noContent()
            : Response.ok(ImmutableMap.of("supportedTypes", types));

        return builder.build();
    }

}
