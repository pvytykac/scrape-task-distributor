package pvytykac.net.scrape.server.facade;

import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.enums.TaskType;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Paly
 * @since 2018-08-11
 */
public interface TaskQueue {

    List<ScrapeTask> dequeue(Set<TaskType> ignoredTypes, Integer limit);

    void returnToQueue(ScrapeTask task);

}
