package pvytykac.net.scrape.server.db.repository.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pvytykac.net.scrape.server.db.repository.Dbo;
import pvytykac.net.scrape.server.db.repository.Repository;

public abstract class AbstractRepository<ID, ENTITY extends Dbo<ID>> implements Repository<ID, ENTITY> {

	private final SessionFactory sessionFactory;

	public AbstractRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected abstract Class<ENTITY> getEntityClass();

	@Override
	public ENTITY find(ID id) {
		return findOptional(id).orElse(null);
	}

	@Override
	public Optional<ENTITY> findOptional(ID id) {
		return getSession().createQuery("FROM " + getEntityClass().getSimpleName() + " alias WHERE alias.id = :id", getEntityClass())
				.setParameter("id", id)
				.list()
				.stream()
				.findFirst();
	}

	@Override
	public void save(ENTITY entity) {
		getSession().saveOrUpdate(entity);
	}

	protected Query<ENTITY> query(String query) {
		return getSession().createQuery(query, getEntityClass());
	}

	protected Query untypedQuery(String query) {
		return getSession().createQuery(query);
	}

	protected List<ENTITY> list(Query<ENTITY> query) {
		return query.list();
	}


	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
}
