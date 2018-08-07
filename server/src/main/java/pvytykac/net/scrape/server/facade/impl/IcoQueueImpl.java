package pvytykac.net.scrape.server.facade.impl;

import pvytykac.net.scrape.model.v1.enums.TaskType;
import pvytykac.net.scrape.server.db.IcoRepository;
import pvytykac.net.scrape.server.db.model.Ico;
import pvytykac.net.scrape.server.facade.IcoQueue;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Paly
 * @since 2018-08-07
 */
public class IcoQueueImpl implements IcoQueue {

    private final IcoRepository icoRepository;
    private final Queue<String> icoQueue;

    public IcoQueueImpl(IcoRepository icoRepository) {
        this.icoRepository = icoRepository;
        this.icoQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public List<Ico> dequeue(Set<TaskType> ignoredTypes, Integer limit) {
        return Collections.emptyList();
    }

    @Override
    public void returnToQueue(String ico) {

    }

}
