package pvytykac.net.scrape.server.db.impl;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import pvytykac.net.scrape.server.db.DbTest;
import pvytykac.net.scrape.server.db.model.ico.Ico;
import pvytykac.net.scrape.server.db.repository.IcoRepository;
import pvytykac.net.scrape.server.db.repository.impl.RepositoryFacade;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * @author Paly
 * @since 2018-08-11
 */
public class IcoRepositoryTest extends DbTest {

    private static final String ID_A = "00000078";
    private static final String ID_B = "00000175";

    private IcoRepository icoRepository;

    @Before
    public void setUp() throws Exception {
        icoRepository = requireBean(RepositoryFacade.class).getIcoRepository();
    }

    @Test
    public void updateLastUpdated() throws Exception {
        assertThat(icoRepository.updateLastUpdated(ID_A), is(true));

        Ico ico = session.createQuery("FROM Ico i WHERE i.id = :id", Ico.class)
                .setParameter("id", ID_A)
                .uniqueResult();

        assertThat(ico, notNullValue());
        assertThat(ico.getId(), is(ID_A));
        assertThat(ico.getForm(), is(301));
        assertThat(ico.getLastUpdated(), notNullValue());

        DateTime now = DateTime.now(DateTimeZone.UTC);
        assertThat(ico.getLastUpdated(), greaterThanOrEqualTo(now.minusSeconds(5)));
        assertThat(ico.getLastUpdated(), lessThanOrEqualTo(now));
    }
//
//    @Test
//    public void listWithNoOffset() throws Exception {
//        List<Ico> list = icoRepository.findNext(null);
//
//        assertThat(list, notNullValue());
//        assertThat(list.size(), is(1));
//
//        assertThat(list.get(0).getId(), is(ID_A));
//        assertThat(list.get(0).getForm(), is(301));
//        assertThat(list.get(0).getLastUpdated(), is(DateTime.parse("2015-01-06T00:01:02.999")));
//    }
//
//    @Test
//    public void listWithOffset() throws Exception {
//        List<Ico> list = icoRepository.findNext(ID_A);
//
//        assertThat(list, notNullValue());
//        assertThat(list.size(), is(1));
//
//        assertThat(list.get(0).getId(), is(ID_B));
//        assertThat(list.get(0).getForm(), is(331));
//        assertThat(list.get(0).getLastUpdated(), is(DateTime.parse("2016-07-09T01:02:03.111")));
//    }
}
