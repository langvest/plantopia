package by.langvest.toolkit.platform;

import com.google.common.collect.Queues;

import java.util.Queue;

public class WorkScheduler {
    protected static final Queue<Runnable> clientQueue = Queues.newConcurrentLinkedQueue();

    public void enqueueClientWork(Runnable work) {
        clientQueue.add(work);
    }

    public void executeClientWork() {
        Runnable work;

        while ((work = clientQueue.poll()) != null) {
            work.run();
        }
    }
}
