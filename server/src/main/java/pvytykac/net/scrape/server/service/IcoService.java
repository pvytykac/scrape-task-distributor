package pvytykac.net.scrape.server.service;

import pvytykac.net.scrape.server.db.model.ico.Ico;

import java.util.Set;

/**
 * @author Paly
 * @since 2018-08-07
 */
public interface IcoService {

    Ico getIco(String offsetIco, Set<Integer> formIds);

    Ico getIco(String offsetIco);

}
