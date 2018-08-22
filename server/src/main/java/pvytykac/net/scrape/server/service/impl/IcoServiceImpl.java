package pvytykac.net.scrape.server.service.impl;

import pvytykac.net.scrape.server.db.model.ico.Ico;
import pvytykac.net.scrape.server.db.repository.IcoRepository;
import pvytykac.net.scrape.server.service.IcoService;

import java.util.Optional;
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
    public Optional<Ico> getIco(String offsetIco, Set<Integer> formIds) {
        return (formIds == null || formIds.isEmpty())
                ? generateAndFetchNextIco(offsetIco)
                : repository.findNext(offsetIco, formIds);
    }

    private Optional<Ico> generateAndFetchNextIco(String offsetIco) {
        String nextIco = offsetIco == null
                ? "00000078"
                : null;

        if (nextIco == null) {
            return Optional.empty();
        } else {
            Ico ico = new Ico.Builder()
                    .withId(nextIco)
                    .build();

            repository.save(ico);

            return Optional.of(ico);
        }
    }
}
