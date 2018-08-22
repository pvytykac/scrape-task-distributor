package pvytykac.net.scrape.server.service;

import pvytykac.net.scrape.server.db.model.ico.Ico;

import java.util.Optional;
import java.util.Set;

/**
 * @author Paly
 * @since 2018-08-07
 */
public interface IcoService {

    Optional<Ico> getIco(String offsetIco, Set<Integer> formIds);

}
