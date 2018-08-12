package pvytykac.net.scrape.server.service.impl;

import com.google.common.collect.ImmutableList;
import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.enums.TaskType;
import pvytykac.net.scrape.server.service.ScrapeTaskService;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

/**
 * @author Paly
 * @since 2018-08-11
 */
public class TaskQueueImpl implements ScrapeTaskService {

    private static final int QUEUE_SIZE = 200;

    private final Queue<ScrapeTask> queue;

    public TaskQueueImpl() {
        this.queue = new LinkedBlockingQueue<>(QUEUE_SIZE);
    }

    @Override
    public List<ScrapeTask> getScrapeTasks(Set<TaskType> ignoredTypes, int limit) {
        ImmutableList.Builder<ScrapeTask> acceptableTasks = ImmutableList.builder();
        Stream.Builder<ScrapeTask> refusedTasks = Stream.builder();
        int size = 0;

        synchronized (queue) {
            ScrapeTask task;
            while(size < limit && (task = queue.poll()) != null) {
                if (ignoredTypes.contains(task.getTaskType())) {
                    refusedTasks.add(task);
                } else {
                    acceptableTasks.add(task);
                    ++size;
                }
            }

            refusedTasks.build().forEach(this::returnScrapeTask);
            return acceptableTasks.build();
        }
    }

    @Override
    public boolean returnScrapeTask(ScrapeTask task) {
        synchronized (queue) {
            return queue.offer(task);
        }
    }
}
