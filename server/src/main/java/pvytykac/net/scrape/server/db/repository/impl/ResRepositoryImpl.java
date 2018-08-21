package pvytykac.net.scrape.server.db.repository.impl;

import org.hibernate.SessionFactory;

import pvytykac.net.scrape.server.db.model.res.ResInstitution;
import pvytykac.net.scrape.server.db.repository.ResRepository;

public class ResRepositoryImpl extends AbstractRepository<Integer, ResInstitution> implements ResRepository {

	public ResRepositoryImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<ResInstitution> getEntityClass() {
		return ResInstitution.class;
	}
}
