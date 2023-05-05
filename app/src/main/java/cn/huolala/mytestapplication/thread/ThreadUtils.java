package cn.huolala.mytestapplication.thread;

import android.util.Log;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2023/4/7.
 * PS: Not easy to write code, please indicate.
 */
public class ThreadUtils {
    public static synchronized void printName1() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e("ThreadUtils", "printName1");
    }

    public static synchronized void printName2() {
        Log.e("ThreadUtils", "printName2");
    }
}
