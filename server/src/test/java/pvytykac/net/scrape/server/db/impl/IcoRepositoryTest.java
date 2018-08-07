package pvytykac.net.scrape.server.db.impl;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import pvytykac.net.scrape.server.db.DbTest;
import pvytykac.net.scrape.server.db.IcoRepository;
import pvytykac.net.scrape.server.db.model.Ico;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Paly
 * @since 2018-08-11
 */
public class IcoRepositoryTest extends DbTest {

    private IcoRepository icoRepository;

    @Before
    public void setUp() throws Exception {
        icoRepository = requireBean(IcoRepository.class);
    }

    @Test
    public void update() throws Exception {
        icoRepository.save(new Ico("a234567890", null));
    }

    @Test
    public void list() throws Exception {
        List<Ico> list = icoRepository.list(1, 1);

        assertThat(list, notNullValue());
        assertThat(list.size(), is(1));

        assertThat(list.get(0).getIco(), is("a234567890"));
        assertThat(list.get(0).getLastUpdated(), is(DateTime.parse("2015-01-06T00:01:02.999")));
    }
}
