package pvytykac.net.scrape.server.service.impl;

import pvytykac.net.scrape.server.db.model.ico.Ico;
import pvytykac.net.scrape.server.db.repository.IcoRepository;
import pvytykac.net.scrape.server.service.IcoService;

import java.util.Set;

/**
 * @author Paly
 * @since 2018-08-22
 */
public class IcoServiceImpl implements IcoService {

    private final IcoRepository repository;

    public IcoServiceImpl(IcoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ico getIco(String offsetIco, Set<Integer> formIds) {
        return null;
    }

    @Override
    public Ico getIco(String offsetIco) {
        return null;
    }
}
