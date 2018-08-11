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
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;

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
    public Stream<ScrapeTask> dequeue(Set<TaskType> ignoredTypes, Integer limit) {
        return icoQueue.dequeue(1)
                .map(ico -> new ScrapeTask.ScrapeTaskBuilder()
                        .withTaskUuid(UUID.randomUUID().toString())
                        .withParameters(ImmutableMap.of("ico", ico))
                        .withSteps(Collections.emptyList())
                        .build());
    }

    @Override
    public void returnToQueue(ScrapeTask task) {
    }
}
