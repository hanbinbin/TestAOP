package cn.huolala.mytestapplication.thread;

import android.util.Log;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2022/8/14.
 * PS: Not easy to write code, please indicate.
 */
public class TestReentrantLock {
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static void m() {
        LOCK.lock();
        try {
            Log.e("TestReentrantLock", Thread.currentThread().getName() + "m");
            // 调用m1()
            m1();
        } finally {
            // 注意锁的释放
            LOCK.unlock();
        }
    }

    public static void m1() {
        LOCK.lock();
        try {
            Log.e("TestReentrantLock", Thread.currentThread().getName() + "m1");
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 注意锁的释放
            LOCK.unlock();
        }
    }

    public static void m2() {
        LOCK.lock();
        try {
            Log.e("TestReentrantLock", Thread.currentThread().getName() + "m2");
        } finally {
            // 注意锁的释放
            LOCK.unlock();
        }
    }
}
