package pvytykac.net.scrape.server.db.repository;

import java.util.Optional;
import java.util.Set;

import pvytykac.net.scrape.server.db.model.ico.Ico;

/**
 * @author Paly
 * @since 2018-08-07
 */
public interface IcoRepository extends Repository<String, Ico> {

    Optional<Ico> findNext(String offsetId, Set<Integer> formIds);

    boolean updateFromRes(String id, Integer form, Integer resId);

}
