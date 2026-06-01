package by.langvest.toolkit.platform;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

import java.util.Map;
import java.util.Queue;

public class WorkScheduler {
    protected static final Map<String, Queue<Runnable>> workQueues = Maps.newConcurrentMap();

    public void enqueueWork(String poolName, Runnable work) {
        workQueues.computeIfAbsent(poolName, k -> Queues.newConcurrentLinkedQueue()).add(work);
    }

    public void executeWork(String poolName) {
        Queue<Runnable> queue = workQueues.get(poolName);

        if (queue != null) {
            Runnable work;
            while ((work = queue.poll()) != null) {
                work.run();
            }
        }
    }
}
