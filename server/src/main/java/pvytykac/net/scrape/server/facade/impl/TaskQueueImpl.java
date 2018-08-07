package pvytykac.net.scrape.server.facade.impl;

import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.enums.TaskType;
import pvytykac.net.scrape.server.ScrapeTaskConfiguration;
import pvytykac.net.scrape.server.facade.IcoQueue;
import pvytykac.net.scrape.server.facade.TaskQueue;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Paly
 * @since 2018-08-11
 */
public class TaskQueueImpl implements TaskQueue {

    private final IcoQueue icoQueue;
    private final Queue<ScrapeTask> taskQueue;

    public TaskQueueImpl(IcoQueue icoQueue, List<ScrapeTaskConfiguration> configuration) {
        this.icoQueue = icoQueue;
        this.taskQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public List<ScrapeTask> dequeue(Set<TaskType> ignoredTypes, Integer limit) {
        return Collections.emptyList();
    }

    @Override
    public void returnToQueue(ScrapeTask task) {
    }
}
