package pvytykac.net.scrape.server.facade;

import io.dropwizard.lifecycle.Managed;
import pvytykac.net.scrape.model.v1.enums.TaskType;
import pvytykac.net.scrape.server.db.model.Ico;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Paly
 * @since 2018-08-07
 */
public interface IcoQueue extends Managed {

    Stream<String> dequeue(Integer limit);

    boolean returnToQueue(String ico);

}
