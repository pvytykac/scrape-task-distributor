package pvytykac.net.scrape.server.db;

public class RepositoryFacade {

	private final IcoRepository icoRepository;

	public RepositoryFacade(IcoRepository icoRepository) {
		this.icoRepository = icoRepository;
	}

	public IcoRepository getIcoRepository() {
		return icoRepository;
	}
}
