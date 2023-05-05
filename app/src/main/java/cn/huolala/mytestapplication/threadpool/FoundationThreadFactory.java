package cn.huolala.mytestapplication.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author by binbinhan,
 * Date on 2022/7/7.
 * PS: Not easy to write code, please indicate.
 */
class FoundationThreadFactory implements ThreadFactory {
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    FoundationThreadFactory(String prefix) {
        group = Thread.currentThread().getThreadGroup();
        namePrefix = prefix + "-foundation-pool-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0
        );
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
