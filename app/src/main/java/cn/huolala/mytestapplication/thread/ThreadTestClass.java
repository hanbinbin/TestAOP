package cn.huolala.mytestapplication.thread;

import android.util.Log;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2022/7/20.
 * PS: Not easy to write code, please indicate.
 */
public class ThreadTestClass {
    private int total = 0;
    private static int static_total = 0;

    public synchronized void add(String tag) {
        Log.e(tag == null ? "reduce" : tag, "start:" + System.currentTimeMillis());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        total++;
        Log.e(tag == null ? "reduce" : tag, "end:" + System.currentTimeMillis());
    }

    public static synchronized void static_add(String tag) {
        Log.e(tag == null ? "reduce" : tag, "start:" + System.currentTimeMillis());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        static_total++;
        Log.e(tag == null ? "reduce" : tag, "end:" + System.currentTimeMillis());
    }

    public synchronized void reduce(String tag) {
        Log.e(tag == null ? "reduce" : tag, "start:" + System.currentTimeMillis());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        total--;
        Log.e(tag == null ? "reduce" : tag, "end:" + System.currentTimeMillis());
    }

    public static synchronized void static_reduce(String tag) {
        Log.e(tag == null ? "reduce" : tag, "start:" + System.currentTimeMillis());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        static_total--;
        Log.e(tag == null ? "reduce" : tag, "end:" + System.currentTimeMillis());
    }

    public synchronized void test(String tag) {
        //对类对象加锁
        synchronized (ThreadTestClass.class) {
            Log.e(tag == null ? "test" : tag, "start:" + System.currentTimeMillis());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            total--;
            Log.e(tag == null ? "test" : tag, "end:" + System.currentTimeMillis());
        }
    }

}
