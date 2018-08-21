package net.pvytykac.scrape.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static net.pvytykac.scrape.util.ModelBuilderUtil.asImmutableList;
import static net.pvytykac.scrape.util.ModelBuilderUtil.asImmutableMap;
import static net.pvytykac.scrape.util.ModelBuilderUtil.asImmutableSet;
import static net.pvytykac.scrape.util.ModelBuilderUtil.buildOptional;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
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
        Object result = buildOptional(Optional.of(new TestBuilder(object)));

        assertThat(result, is(object));
    }

    @Test
    public void buildOptionalAbsent() throws Exception {
        Object result = buildOptional(Optional.empty());

        assertThat(result, nullValue());
    }

    @Test
    public void asImmutableMapNull() throws Exception {
        assertThat(asImmutableMap(null), nullValue());
    }

    @Test
    public void asImmutableMapNonNull() throws Exception {
        Map<String, String> original = new HashMap<>(Collections.singletonMap("a", "b"));
        Map<String, String> immutable = asImmutableMap(original);

        assertThat(immutable, is(original));

        original.put("b", "c");
        assertThat(immutable, not(original));

        exception.expect(UnsupportedOperationException.class);
        immutable.put("b", "c");
    }

    @Test
    public void asImmutableListNull() throws Exception {
        assertThat(asImmutableList(null), nullValue());
    }

    @Test
    public void asImmutableListNonNull() throws Exception {
        List<String> original = new ArrayList<>(Collections.singletonList("a"));
        List<String> immutable = asImmutableList(original);

        assertThat(immutable, is(original));

        original.add("b");
        assertThat(immutable, not(original));

        exception.expect(UnsupportedOperationException.class);
        immutable.add("b");
    }

    @Test
    public void asImmutableSetNull() throws Exception {
        assertThat(asImmutableSet(null), nullValue());
    }

    @Test
    public void asImmutableSetNonNull() throws Exception {
        Set<String> original = new HashSet<>(Collections.singletonList("a"));
        Set<String> immutable = asImmutableSet(original);

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