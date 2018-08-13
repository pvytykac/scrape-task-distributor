package pvytykac.net.scrape.model.v1;

import org.json.JSONObject;
import org.junit.Test;
import pvytykac.net.scrape.model.JsonTest;
import pvytykac.net.scrape.model.ModelBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class ScrapeResultRepresentationTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        ModelBuilder<ScrapeError> errorBuilder = mockModelBuilder(ScrapeError.class);
        ModelBuilder<ScrapeResult> resultBuilder = mockModelBuilder(ScrapeResult.class);
        ScrapeResultRepresentation dto = new ScrapeResultRepresentation.ScrapeResultRepresentationBuilder()
                .withError(errorBuilder)
                .withResult(resultBuilder)
                .withSessionId("sessionUuid")
                .withTaskId("taskUuid")
                .withTaskType("TASK_TYPE")
                .build();

        JSONObject json = serialize(dto);

        assertThat(json.getJSONObject("error"), notNullValue());
        assertThat(json.getJSONObject("result"), notNullValue());
        assertThat(json.getString("sessionUuid"), is(dto.getSessionUuid()));
        assertThat(json.getString("taskUuid"), is(dto.getTaskUuid()));
        assertThat(json.getString("taskType"), is(dto.getTaskType()));
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("error", new JSONObject())
                .put("result", new JSONObject())
                .put("sessionUuid", "123")
                .put("taskUuid", "345")
                .put("taskType", "TYPE");

        ScrapeResultRepresentation dto = deserialize(json, ScrapeResultRepresentation.class);

        assertThat(dto.getError(), notNullValue());
        assertThat(dto.getResult(), notNullValue());
        assertThat(dto.getSessionUuid(), is(json.getString("sessionUuid")));
        assertThat(dto.getTaskUuid(), is(json.getString("taskUuid")));
        assertThat(dto.getTaskType(), is(json.getString("taskType")));
    }
}