package cn.huolala.mytestapplication.threadpool;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

/**
 * Author by binbinhan,
 * Date on 2022/7/7.
 * PS: Not easy to write code, please indicate.
 */
public class WPFThreadPool {
    private ExecutorService compute;
    private ExecutorService io;

    private static final class WPFThreadPoolHolder {
        static final WPFThreadPool wpfThreadPool = new WPFThreadPool();
    }

    public static WPFThreadPool getInstance() {
        return WPFThreadPoolHolder.wpfThreadPool;
    }

    private WPFThreadPool() {

    }

    /**
     * get compute threadPool(The thread will be destroyed immediately after execution；because we not set keepTime，
     * If too many tasks are added, we will add to the cache queue)
     * {@link ExecutorHolder}
     *
     * @return ExecutorService
     */
    public ExecutorService compute() {
        if (compute == null) {
            compute = ExecutorHolder.initCompute();
        }
        return compute;
    }

    /**
     * get io threadPool(Added tasks are processed immediately and we set keepTime)
     * like Executors.newCachedThreadPool(); but we set maximumPoolSize by self
     * {@link ExecutorHolder}
     *
     * @return ExecutorService
     */
    public ExecutorService io() {
        if (io == null) {
            io = ExecutorHolder.initIO();
        }
        return io;
    }

    /**
     * update compute threadPool
     *
     * @param executorService threadPool
     */
    public void updateCompute(@NonNull ExecutorService executorService) {
        compute = executorService;
    }

    /**
     * update io threadPool
     *
     * @param executorService threadPool
     */
    public void updateIO(@NonNull ExecutorService executorService) {
        io = executorService;
    }
}
