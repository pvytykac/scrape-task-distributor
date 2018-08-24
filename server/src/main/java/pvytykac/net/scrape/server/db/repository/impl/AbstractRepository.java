package pvytykac.net.scrape.server.db.repository.impl;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pvytykac.net.scrape.server.db.repository.Dbo;
import pvytykac.net.scrape.server.db.repository.Repository;

abstract class AbstractRepository<ID, ENTITY extends Dbo<ID>> implements Repository<ID, ENTITY> {

	private final SessionFactory sessionFactory;

	protected AbstractRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected abstract Class<ENTITY> getEntityClass();

	@Override
	public ENTITY find(ID id) {
		return findOptional(id).orElse(null);
	}

	@Override
	public Optional<ENTITY> findOptional(ID id) {
		return query("FROM " + getEntityName() + " alias WHERE alias.id = :id")
				.setParameter("id", id)
				.list()
				.stream()
				.findFirst();
	}

	@Override
	public void save(ENTITY entity) {
		getSession().saveOrUpdate(entity);
	}

	@Override
	public void delete(ENTITY entity) {
		getSession().delete(entity);
	}

	@Override
	public boolean delete(ID id) {
		return untypedQuery("DELETE alias FROM " + getEntityName() + " alias WHERE alias.id = :id")
				.setParameter("id", id)
				.executeUpdate() == 1;
	}

	protected Query<ENTITY> query(String query) {
		return getSession().createQuery(query, getEntityClass());
	}

	protected Query untypedQuery(String query) {
		return getSession().createQuery(query);
	}

	private String getEntityName() {
		return getEntityClass().getSimpleName();
	}

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
}
