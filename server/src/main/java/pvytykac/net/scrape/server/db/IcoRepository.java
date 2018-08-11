package pvytykac.net.scrape.server.db;

import pvytykac.net.scrape.server.db.model.Ico;

import java.util.List;

/**
 * @author Paly
 * @since 2018-08-07
 */
public interface IcoRepository {

    List<Ico> list(int limit, String offsetIco);

    boolean updateLastUpdated(String ico);

}
