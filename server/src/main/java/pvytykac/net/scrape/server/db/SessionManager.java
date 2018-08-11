package pvytykac.net.scrape.server.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;

public class SessionManager {

	private final SessionFactory sessionFactory;

	public SessionManager(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setUpHibernateSession() {
		if (!ManagedSessionContext.hasBind(sessionFactory)) {
			Session session = sessionFactory.openSession();
			ManagedSessionContext.bind(session);
			session.beginTransaction();
		}
	}

	public void destroyHibernateSession() {
		if (ManagedSessionContext.hasBind(sessionFactory)) {
			Session session = sessionFactory.getCurrentSession();
			session.getTransaction().commit();
			session.close();
			ManagedSessionContext.unbind(sessionFactory);
		}
	}
}
