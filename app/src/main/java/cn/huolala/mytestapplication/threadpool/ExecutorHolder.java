package cn.huolala.mytestapplication.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author by binbinhan,
 * Date on 2022/7/7.
 * PS: Not easy to write code, please indicate.
 */
class ExecutorHolder {
    private static ExecutorService IO;
    private static ExecutorService COMPUTE;

    /**
     * init compute threadPool
     */
    public static ExecutorService initCompute() {
        if (COMPUTE == null) {
            int core = Runtime.getRuntime().availableProcessors();
            COMPUTE = new ThreadPoolExecutor(core, core,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    new FoundationThreadFactory("compute")
            );
        }
        return COMPUTE;
    }

    /**
     * init io threadPool
     *
     * The purpose of using SynchronousQueue is to ensure that "for the submitted task,
     * if there is an idle thread, use the idle thread to process it; otherwise,
     * create a new thread to process the task".
     */
    public static ExecutorService initIO() {
        if (IO == null) {
            IO = new ThreadPoolExecutor(0, 50,
                    60L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    new FoundationThreadFactory("io")
            );
        }
        return IO;
    }
}
