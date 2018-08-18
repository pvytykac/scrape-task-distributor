package pvytykac.net.scrape.server.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.After;
import org.junit.Before;
import pvytykac.net.scrape.server.AppTest;

import java.util.TimeZone;

/**
 * @author Paly
 * @since 2018-08-11
 */
public abstract class DbTest extends AppTest {

    private SessionFactory sessionFactory = requireBean(SessionFactory.class);
    protected Session session;

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Before
    public void setUpHibernateSession() throws Exception {
        session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);
        session.beginTransaction();
    }

    @After
    public void destroyHibernateSession() throws Exception {
        session.getTransaction().rollback();
        session.close();
        ManagedSessionContext.unbind(sessionFactory);
        session = null;
    }
}
