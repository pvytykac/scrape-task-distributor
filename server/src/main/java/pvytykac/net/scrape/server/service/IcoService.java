package pvytykac.net.scrape.server.service;

import io.dropwizard.lifecycle.Managed;
import pvytykac.net.scrape.server.db.model.Ico;

import java.util.Optional;

/**
 * @author Paly
 * @since 2018-08-07
 */
public interface IcoService extends Managed {

    Optional<Ico> getNextIco();

}
