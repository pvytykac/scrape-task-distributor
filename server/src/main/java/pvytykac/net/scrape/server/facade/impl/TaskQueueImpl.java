package pvytykac.net.scrape.server.facade.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import pvytykac.net.scrape.model.v1.ScrapeTask;
import pvytykac.net.scrape.model.v1.enums.TaskType;
import pvytykac.net.scrape.server.ScrapeTaskConfiguration;
import pvytykac.net.scrape.server.db.model.Ico;
import pvytykac.net.scrape.server.facade.IcoQueue;
import pvytykac.net.scrape.server.facade.TaskQueue;

/**
 * @author Paly
 * @since 2018-08-11
 */
public class TaskQueueImpl implements TaskQueue {

    private static final int QUEUE_SIZE = 200;

    private final IcoQueue icoQueue;
    private final Queue<ScrapeTask> taskQueue;

    public TaskQueueImpl(IcoQueue icoQueue, List<ScrapeTaskConfiguration> configuration) {
        this.icoQueue = icoQueue;
        this.taskQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);
    }

    @Override
    public List<ScrapeTask> dequeue(Set<TaskType> ignoredTypes, Integer limit) {
        List<ScrapeTask> list = new ArrayList<>();
        AtomicInteger size = new AtomicInteger();

        synchronized (taskQueue) {
            // take existing tasks

            Stream.Builder<ScrapeTask> toBeReturned = Stream.builder();
            Optional<ScrapeTask> scrapeTask;
            do {
                scrapeTask = Optional.ofNullable(taskQueue.poll());

                if (!scrapeTask.isPresent()) {
                    break;
                }

                scrapeTask.filter(task -> ignoredTypes.contains(task.getTaskType()))
                        .ifPresent(toBeReturned::add);

                scrapeTask.filter(task -> !ignoredTypes.contains(task.getTaskType()))
                        .ifPresent(task -> {
                            list.add(task);
                            size.incrementAndGet();
                        });

            } while (size.get() < limit);

            toBeReturned.build().forEach(this::returnToQueue);
        }
        // fill the rest with new tasks

        while (size.get() < limit) {
            Optional<Ico> ico = icoQueue.dequeue();

            if (!ico.isPresent()) {
                break;
            }

            List<ScrapeTask> tasks = ico.map(this::createScrapeTasksForIco)
                    .orElse(Stream.empty())
                    .collect(Collectors.toList());

            tasks.stream()
                    .filter(task -> ignoredTypes.contains(task.getTaskType()))
                    .forEach(this::returnToQueue);

            tasks.stream()
                    .filter(task -> !ignoredTypes.contains(task.getTaskType()))
                    .forEach(task -> {
                        size.incrementAndGet();
                        list.add(task);
                    });
        };

        for (int i = limit; list.size() > i;) {
            returnToQueue(list.remove(i));
        }

        return ImmutableList.copyOf(list);
    }

    private Stream<ScrapeTask> createScrapeTasksForIco(Ico ico) {
        return Stream.of(
                new ScrapeTask.ScrapeTaskBuilder()
                        .withParameters(ImmutableMap.of("ico", ico.getIco(), "form", String.valueOf(ico.getForm())))
                        .withTaskUuid(UUID.randomUUID().toString())
                        .withTaskType(TaskType.TMP)
                        .withSteps(Collections.emptyList())
                        .build(),
                new ScrapeTask.ScrapeTaskBuilder()
                        .withParameters(ImmutableMap.of("ico", ico.getIco(), "form", String.valueOf(ico.getForm())))
                        .withTaskUuid(UUID.randomUUID().toString())
                        .withTaskType(TaskType.ANOTHER)
                        .withSteps(Collections.emptyList())
                        .build()
        );
    }

    @Override
    public void returnToQueue(ScrapeTask task) {
    }
}
