package pvytykac.net.scrape.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


/**
 * @author Paly
 * @since 2018-08-06
 */
public class ModelBuilderUtilTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void init() throws Exception {
        new ModelBuilderUtil();
        // no exceptions
    }

    @Test
    public void buildOptionalPresent() throws Exception {
        Object object = new Object();
        Object result = ModelBuilderUtil.buildOptional(Optional.of(new TestBuilder(object)));

        assertThat(result, is(object));
    }

    @Test
    public void buildOptionalAbsent() throws Exception {
        Object result = ModelBuilderUtil.buildOptional(Optional.empty());

        assertThat(result, nullValue());
    }

    @Test
    public void asImmutableMapNull() throws Exception {
        assertThat(ModelBuilderUtil.asImmutableMap(null), nullValue());
    }

    @Test
    public void asImmutableMapNonNull() throws Exception {
        Map<String, String> original = new HashMap<>(Collections.singletonMap("a", "b"));
        Map<String, String> immutable = ModelBuilderUtil.asImmutableMap(original);

        assertThat(immutable, is(original));

        original.put("b", "c");
        assertThat(immutable, not(original));

        exception.expect(UnsupportedOperationException.class);
        immutable.put("b", "c");
    }

    @Test
    public void asImmutableListNull() throws Exception {
        assertThat(ModelBuilderUtil.asImmutableList(null), nullValue());
    }

    @Test
    public void asImmutableListNonNull() throws Exception {
        List<String> original = new ArrayList<>(Collections.singletonList("a"));
        List<String> immutable = ModelBuilderUtil.asImmutableList(original);

        assertThat(immutable, is(original));

        original.add("b");
        assertThat(immutable, not(original));

        exception.expect(UnsupportedOperationException.class);
        immutable.add("b");
    }

    private static final class TestBuilder implements ModelBuilder<Object> {

        private final Object object;

        public TestBuilder(Object object) {
            this.object = object;
        }

        @Override
        public Object build() {
            return object;
        }
    }

}