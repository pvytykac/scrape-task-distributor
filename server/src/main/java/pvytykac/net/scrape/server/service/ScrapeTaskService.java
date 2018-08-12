package pvytykac.net.scrape.server.service;

import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.enums.TaskType;

import java.util.List;
import java.util.Set;

/**
 * @author Paly
 * @since 2018-08-11
 */
public interface ScrapeTaskService {

    List<ScrapeTask> getScrapeTasks(Set<TaskType> ignoredTypes, int limit);

    boolean returnScrapeTask(ScrapeTask task);

}
