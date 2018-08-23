package pvytykac.net.scrape.server.db.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.util.Optional;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

import pvytykac.net.scrape.server.db.DbTest;
import pvytykac.net.scrape.server.db.model.ico.Ico;
import pvytykac.net.scrape.server.db.repository.IcoRepository;
import pvytykac.net.scrape.server.db.repository.impl.RepositoryFacade;

/**
 * @author Paly
 * @since 2018-08-11
 */
public class IcoRepositoryTest extends DbTest {

    private static final String MIN_ICO = "00000078";
    private static final String MAX_ICO = "00000175";
    private static final Set<Integer> FORMS = ImmutableSet.of(331, 301);

    private IcoRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = requireBean(RepositoryFacade.class).getIcoRepository();
    }

    @Test
    public void findNextSome() throws Exception {
        Optional<Ico> ico = repository.findNext(MIN_ICO, FORMS);

        assertThat(ico, notNullValue());
        assertThat(ico.isPresent(), is(true));
        assertThat(ico.get().getId(), is(MAX_ICO));
        assertThat(ico.get().getLastUpdated(), is(DateTime.parse("2016-07-09T01:02:03.111Z")));
        assertThat(ico.get().getForm(), is(331));
    }

    @Test
    public void findNextSomeNoOffset() throws Exception {
        Optional<Ico> ico = repository.findNext(null, FORMS);

        assertThat(ico, notNullValue());
        assertThat(ico.isPresent(), is(true));
        assertThat(ico.get().getId(), is(MIN_ICO));
        assertThat(ico.get().getLastUpdated(), is(DateTime.parse("2015-01-06T00:01:02.999Z")));
        assertThat(ico.get().getForm(), is(301));
    }

    @Test
    public void findNextEmptyOffset() throws Exception {
        Optional<Ico> ico = repository.findNext(MAX_ICO, FORMS);

        assertThat(ico, notNullValue());
        assertThat(ico.isPresent(), is(false));
    }

    @Test
    public void findNextEmptyForm() throws Exception {
        Optional<Ico> ico = repository.findNext(MIN_ICO, ImmutableSet.of(301));

        assertThat(ico, notNullValue());
        assertThat(ico.isPresent(), is(false));
    }

    @Test
    public void updateFromResTrue() throws Exception {
        boolean updated = repository.updateFromRes(MIN_ICO, 1, 2);

        assertThat(updated, is(true));

        Ico ico = session.createQuery("From Ico i WHERE i.id = :id", Ico.class)
                .setParameter("id", MIN_ICO)
                .getSingleResult();

        assertThat(ico, notNullValue());
        assertThat(ico.getId(), is(MIN_ICO));
        assertThat(ico.getForm(), is(1));
        assertThat(ico.getResId(), is(2));
        assertThat(ico.getLastUpdated(), greaterThan(DateTime.now(DateTimeZone.UTC).minusSeconds(10)));
    }

}
