package pvytykac.net.scrape.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.pvytykac.scrape.util.ModelBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Paly
 * @since 2018-08-06
 */
public abstract class JsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonTest() {
//        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public JSONObject serialize(Object pojo) {
        try {
            String json = objectMapper.writeValueAsString(pojo);
            JSONObject serialized = new JSONObject(json);

            assertThat(serialized, notNullValue());

            return serialized;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> T deserialize(JSONObject json, Class<T> clazz) {
        try {
            T deserialized = objectMapper.readerFor(clazz).readValue(json.toString());

            assertThat(deserialized, notNullValue());

            return deserialized;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> ModelBuilder<T> mockModelBuilder(Class<T> clazz) {
        ModelBuilder<T> builder = mock(ModelBuilder.class);
        when(builder.build()).thenReturn(mockModelInstance(clazz));

        return builder;

    }

    public <T> T mockModelInstance(Class<T> clazz) {
        try {
            Constructor<T> ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true);

            return ctor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalArgumentException("The target class '" + clazz.getName() + "' either does not have a default constructor or has no fields");
        }
    }

}
