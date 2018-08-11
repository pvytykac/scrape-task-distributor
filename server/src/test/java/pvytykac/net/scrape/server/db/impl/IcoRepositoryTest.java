package pvytykac.net.scrape.server.db.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import pvytykac.net.scrape.server.db.DbTest;
import pvytykac.net.scrape.server.db.IcoRepository;
import pvytykac.net.scrape.server.db.model.Ico;

/**
 * @author Paly
 * @since 2018-08-11
 */
public class IcoRepositoryTest extends DbTest {

    private static final String ICO_A = "a234567890";
    private static final String ICO_B = "b234567890";

    private IcoRepository icoRepository;

    @Before
    public void setUp() throws Exception {
        icoRepository = requireBean(IcoRepository.class);
    }

    @Test
    public void updateLastUpdated() throws Exception {
        assertThat(icoRepository.updateLastUpdated(ICO_A), is(true));

        Ico ico = session.createQuery("FROM Ico i WHERE i.ico = :ico", Ico.class)
                .setParameter("ico", ICO_A)
                .uniqueResult();

        assertThat(ico, notNullValue());
        assertThat(ico.getIco(), is(ICO_A));
        assertThat(ico.getForm(), is(105));
        assertThat(ico.getLastUpdated(), notNullValue());

        DateTime now = DateTime.now(DateTimeZone.UTC);
        assertThat(ico.getLastUpdated(), greaterThanOrEqualTo(now.minusSeconds(5)));
        assertThat(ico.getLastUpdated(), lessThanOrEqualTo(now));
    }

    @Test
    public void listWithNoOffset() throws Exception {
        List<Ico> list = icoRepository.list(1, null);

        assertThat(list, notNullValue());
        assertThat(list.size(), is(1));

        assertThat(list.get(0).getIco(), is(ICO_A));
        assertThat(list.get(0).getForm(), is(105));
        assertThat(list.get(0).getLastUpdated(), is(DateTime.parse("2015-01-06T00:01:02.999")));
    }

    @Test
    public void listWithOffset() throws Exception {
        List<Ico> list = icoRepository.list(1, ICO_A);

        assertThat(list, notNullValue());
        assertThat(list.size(), is(1));

        assertThat(list.get(0).getIco(), is(ICO_B));
        assertThat(list.get(0).getForm(), is(101));
        assertThat(list.get(0).getLastUpdated(), is(DateTime.parse("2016-07-09T01:02:03.111")));
    }
}
