package cn.huolala.mytestapplication.reference;

import android.util.Log;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2023/3/20.
 * PS: Not easy to write code, please indicate.
 */
public class TestReference {
    /**
     * 一个软引用中的对象，不会很快被JVM回收，JVM会根据当前堆的使用情况来判断何时回收，当堆的使用率超过阈值时，才回去回收软引用中的对象。
     */
    public static void testSoftReference() {
        innerSoftReference();
        innerSoftReference2();
    }

    private static void innerSoftReference() {
        //先通过一个例子来了解一下软引用：
        Object obj = new Object();
        SoftReference<Object> softRef = new SoftReference<>(obj);
        //删除强引用
        obj = null;
        //调用gc
//        System.gc();
        Runtime.getRuntime().gc();
        Log.e("innerSoftReference gc之后的值：", "" + softRef.get()); // 对象依然存在
    }

    /**
     * Queue的意义在于我们在外部可以对queue中的引用进行监控，当引用中的对象被回收后，我们可以对引用对象本身继续做一些清理操作
     */
    private static void innerSoftReference2() {
        //软引用也可以和一个引用队列联合使用，如果软引用中的对象（obj）被回收，那么软引用会被 JVM 加入关联的引用队列中。
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        Object obj = new Object();
        SoftReference<Object> softRef = new SoftReference<>(obj, queue);
        //删除强引用
        obj = null;
        Log.e("innerSoftReference2 内存空余 gc之前的值: ", "" + softRef.get()); // 对象依然存在
        //调用gc
//        System.gc();
        Runtime.getRuntime().gc();
        Log.e("innerSoftReference2 内存空余 gc之后的值: ", "" + softRef.get()); // 对象依然存在
        //申请较大内存使内存空间使用率达到阈值，强迫gc
        int size = 360 * 1024 * 1024;
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = 120;
        }
        Runtime.getRuntime().gc();
        //如果obj被回收，则软引用会进入引用队列
        Reference<?> reference = null;
        try {
            reference = queue.remove();//会阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (reference != null) {
            Log.e("innerSoftReference2 内存紧张，gc之后的值: ", "" + reference.get());  // 对象为null
        }
    }

    /**
     *
     */
    public static void testWeakReference() {
        innerWeakReference();
        innerWeakReference2();
    }

    private static void innerWeakReference() {
        //弱引用的简单使用：
        Object obj = new Object();
        WeakReference<Object> weakRef = new WeakReference<>(obj);
        //删除强引用
        obj = null;
        Log.e("innerWeakReference gc之前的值：", "" + weakRef.get()); // 对象依然存在
        //调用gc
//        System.gc();
        Runtime.getRuntime().gc();
        Log.e("innerWeakReference gc之后的值：", "" + weakRef.get()); // 对象为null
    }

    private static void innerWeakReference2() {
        //弱引用也可以和一个引用队列联合使用，如果弱引用中的对象（obj）被回收，那么弱引用会被 JVM 加入关联的引用队列中。
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        Object obj = new Object();
        WeakReference<Object> weakRef = new WeakReference<>(obj, queue);
        //删除强引用
        obj = null;
        Log.e("innerWeakReference2 gc之前的值: ", "" + weakRef.get()); // 对象依然存在
        //调用gc
//        System.gc();
        Runtime.getRuntime().gc();
        //如果obj被回收，则弱引用会进入引用队列
        Reference<?> reference = null;
        try {
            reference = queue.remove(); //会阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (reference != null) {
            Log.e("innerWeakReference2 对象已被回收: ", "" + reference.get());  // 对象为null
        }
    }
}
