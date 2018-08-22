package pvytykac.net.scrape.server.db.repository;

import pvytykac.net.scrape.server.db.model.ico.Ico;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Paly
 * @since 2018-08-07
 */
public interface IcoRepository extends Repository<String, Ico> {

    Optional<Ico> findNext(String offsetId, Set<Integer> formIds);

    boolean updateLastUpdated(String ico);

}
