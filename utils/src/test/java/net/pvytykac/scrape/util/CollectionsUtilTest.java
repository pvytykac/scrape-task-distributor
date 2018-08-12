package net.pvytykac.scrape.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Paly
 * @since 2018-08-12
 */
public class CollectionsUtilTest {

    @Test
    public void containsDuplicatesTrue() throws Exception {
        assertThat(CollectionsUtil.containsDuplicates(Arrays.asList(1, 1)), is(true));
    }

    @Test
    public void containsDuplicatesFalse() throws Exception {
        assertThat(CollectionsUtil.containsDuplicates(Arrays.asList(1, 2)), is(false));
    }

    @Test
    public void containsNullsTrue() throws Exception {
        assertThat(CollectionsUtil.containsNulls(Arrays.asList(1, null)), is(true));
    }

    @Test
    public void containsNullsFalse() throws Exception {
        assertThat(CollectionsUtil.containsNulls(Arrays.asList(1, 1)), is(false));
    }

    @Test
    public void isSequentialTrue() throws Exception {
        Collection<Integer> collection = Arrays.asList(1, 3, 2);
        assertThat(CollectionsUtil.isSequential(collection), is(true));
    }

    @Test
    public void isSequentialFalse() throws Exception {
        Collection<Integer> collection = Arrays.asList(1, 3, 4);
        assertThat(CollectionsUtil.isSequential(collection), is(false));
    }

    @Test
    public void isSequentialWithNull() throws Exception {
        Collection<Integer> collection = Arrays.asList(1, null, 2, 3);
        assertThat(CollectionsUtil.isSequential(collection), is(true));
    }

    @Test
    public void isSequentialDuplicatesCase() throws Exception {
        Collection<Integer> collection = Arrays.asList(1, 2, 2, 5);
        assertThat(CollectionsUtil.isSequential(collection), is(false));
    }

}