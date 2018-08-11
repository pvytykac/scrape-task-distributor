package pvytykac.net.scrape.server.facade.impl;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import io.dropwizard.lifecycle.Managed;
import pvytykac.net.scrape.model.v1.enums.TaskType;
import pvytykac.net.scrape.server.db.IcoRepository;
import pvytykac.net.scrape.server.db.SessionManager;
import pvytykac.net.scrape.server.db.model.Ico;
import pvytykac.net.scrape.server.facade.IcoQueue;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class IcoQueueImpl implements IcoQueue {

    private static final Logger LOG = LoggerFactory.getLogger(IcoQueueImpl.class);
    private static final int QUEUE_SIZE = 100;

    private final Queue<String> queue;
    private final IcoFetcher fetcher;

    public IcoQueueImpl(IcoRepository icoRepository, SessionManager sessionManager) {
        this.queue = new LinkedBlockingQueue<>(QUEUE_SIZE);
        this.fetcher = new IcoFetcher(icoRepository, sessionManager, queue);
    }

    @Override
    public Stream<String> dequeue(Integer limit) {
        synchronized (queue) {
            Stream.Builder builder = Stream.builder();

            for (int i = 0; i < limit; i++) {
                String dequeued = queue.poll();

                if (dequeued == null) {
                    break;
                }

                builder.add(dequeued);
            }

            return builder.build();
        }
    }

    @Override
    public boolean returnToQueue(String ico) {
        synchronized (this) {
            return queue.contains(ico) || queue.offer(ico);
        }
    }

    @Override
    public void start() throws Exception {
        fetcher.start();
    }

    @Override
    public void stop() throws Exception {
        fetcher.stop();
    }

    private static final class IcoFetcher implements Runnable {

        private static final int FETCH_THRESHOLD = 10;

        private final IcoRepository icoRepository;
        private final SessionManager sessionManager;
        private final Queue<String> queue;
        private final ScheduledExecutorService executor;
        private String ico = null;

        public IcoFetcher(IcoRepository icoRepository, SessionManager sessionManager, Queue<String> queue) {
            this.icoRepository = icoRepository;
            this.sessionManager = sessionManager;
            this.queue = queue;
            this.executor = Executors.newScheduledThreadPool(1);
        }

        @Override
        public void run() {
            try {
                sessionManager.setUpHibernateSession();
                synchronized (queue) {
                    if (queue.size() <= FETCH_THRESHOLD) {
                        List<String> icoList = icoRepository.list(50, ico)
                                .stream()
                                .map(Ico::getIco)
                                .collect(toList());

                        if (icoList.isEmpty()) {
                            ico = null;
                        } else {
                            queue.addAll(icoList);
                        }
                    }
                }
            } catch (Exception ex) {
                LOG.error("Error while fetching icos", ex);
            } finally {
                sessionManager.destroyHibernateSession();
            }
        }

        private void start() {
            executor.scheduleAtFixedRate(this, 0L, 2L, TimeUnit.SECONDS);
        }

        private void stop() {
            executor.shutdown();
        }
    }

}
