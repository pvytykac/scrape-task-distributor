package net.pvytykac.scrape.util;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class ModelBuilderTest {

    private static final Object OBJECT = new Object();

    private ModelBuilder<Object> builder = () -> OBJECT;

    @Test
    public void build() throws Exception {
        assertThat(builder.build(), is(OBJECT));
    }

    @Test
    public void buildAsOptional() throws Exception {
        Optional<Object> optional = builder.buildAsOptional();

        assertThat(optional, notNullValue());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get(), is(OBJECT));
    }
}
