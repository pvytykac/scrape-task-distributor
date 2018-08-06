package pvytykac.net.scrape.model.v1;

import org.json.JSONObject;
import org.junit.Test;
import pvytykac.net.scrape.model.JsonTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Paly
 * @since 2018-08-06
 */
public class ClientExceptionTest extends JsonTest {

    @Test
    public void serialize() throws Exception {
        ClientException exception = new ClientException.ClientExceptionBuilder()
                .withMessage("message")
                .withPayload("payload")
                .withStackTrace("stack trace")
                .withStatusCode(200)
                .build();

        JSONObject json = serialize(exception);

        assertThat(json.getString("message"), is(exception.getMessage()));
        assertThat(json.getString("payload"), is(exception.getPayload()));
        assertThat(json.getString("stackTrace"), is(exception.getStackTrace()));
        assertThat(json.getInt("statusCode"), is(exception.getStatusCode()));
    }

    @Test
    public void deserialize() throws Exception {
        JSONObject json = new JSONObject()
                .put("message", "msg")
                .put("payload", "body")
                .put("stackTrace", "stack")
                .put("statusCode", 204);

        ClientException exception = deserialize(json, ClientException.class);

        assertThat(exception.getMessage(), is(json.getString("message")));
        assertThat(exception.getPayload(), is(json.getString("payload")));
        assertThat(exception.getStackTrace(), is(json.getString("stackTrace")));
        assertThat(exception.getStatusCode(), is(json.getInt("statusCode")));
    }
}
