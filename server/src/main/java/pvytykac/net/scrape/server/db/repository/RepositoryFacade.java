package pvytykac.net.scrape.server.db.repository;

import org.hibernate.SessionFactory;

import pvytykac.net.scrape.server.db.repository.impl.IcoRepositoryImpl;
import pvytykac.net.scrape.server.db.repository.impl.ResRepositoryImpl;

public class RepositoryFacade {

	private final IcoRepository icoRepository;
	private final ResRepository resRepository;

	public RepositoryFacade(SessionFactory sessionFactory) {
		this.icoRepository = new IcoRepositoryImpl(sessionFactory);
		this.resRepository= new ResRepositoryImpl(sessionFactory);
	}

	public IcoRepository getIcoRepository() {
		return icoRepository;
	}

	public ResRepository getResRepository() {
		return resRepository;
	}
}
