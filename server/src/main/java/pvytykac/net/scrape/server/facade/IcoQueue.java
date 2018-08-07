package pvytykac.net.scrape.server.facade;

import pvytykac.net.scrape.model.v1.enums.TaskType;
import pvytykac.net.scrape.server.db.model.Ico;

import java.util.List;
import java.util.Set;

/**
 * @author Paly
 * @since 2018-08-07
 */
public interface IcoQueue {

    List<Ico> dequeue(Set<TaskType> ignoredTypes, Integer limit);

    void returnToQueue(String ico);

}
