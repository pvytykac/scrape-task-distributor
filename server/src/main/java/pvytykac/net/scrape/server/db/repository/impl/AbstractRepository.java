package pvytykac.net.scrape.server.db.repository.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pvytykac.net.scrape.server.db.repository.Repository;

public abstract class AbstractRepository<ID, ENTITY> implements Repository<ID, ENTITY> {

	private final SessionFactory sessionFactory;

	public AbstractRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public ENTITY find(ID id) {
		return getSession().createQuery("FROM " + getEntityClass().getSimpleName() + " alias WHERE alias.id = :id", getEntityClass())
				.setParameter("id", id)
				.getSingleResult();
	}

	@Override
	public Optional<ENTITY> findOptional(ID id) {
		return Optional.ofNullable(find(id));
	}

	@Override
	public void save(ENTITY entity) {
		getSession().persist(entity);
	}

	protected Query<ENTITY> query(String query) {
		return getSession().createQuery(query, getEntityClass());
	}

	@SuppressWarnings("unchecked")
	protected List<ENTITY> list(Query<ENTITY> query) {
		return query.list();
	}

	protected abstract Class<ENTITY> getEntityClass();

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
}
