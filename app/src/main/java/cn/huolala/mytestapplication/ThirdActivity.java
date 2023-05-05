package cn.huolala.mytestapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.huolala.mytestapplication.apm.ApmSlowMethodStack;
import cn.huolala.mytestapplication.bean.TestBean;
import cn.huolala.mytestapplication.bean.TraceInfo;
import cn.huolala.mytestapplication.impl.LeakInterface;
import cn.huolala.mytestapplication.utils.CallBackManager;
import cn.huolala.mytestapplication.utils.GzipSinkTest;
import cn.huolala.test_c.NativeLib;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2022/10/9.
 * PS: Not easy to write code, please indicate.
 */
public class ThirdActivity extends AppCompatActivity implements LeakInterface {
    ApmSlowMethodStack exampleTest = new ApmSlowMethodStack();
    List<String> list = new LinkedList<>();
    List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();

    List<TraceInfo> testList = new ArrayList<>();

    LinkedList<ApmSlowMethodStack.TreeNode> children = new LinkedList<>();
    public static String nameStatic = "nameStatic";
    String name = "name";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        CallBackManager.addCallBack(this);//测试内存泄漏
        findViewById(R.id.test_get_stack_reason).setOnClickListener(v -> {
            exampleTest.matchSampleRate();
        });

        findViewById(R.id.read_content_from_server).setOnClickListener(v -> {
            exampleTest.readContentFromServer();
        });

        findViewById(R.id.test_match_is_system_tag).setOnClickListener(v -> {
            exampleTest.testMatchIsSystemTag();
        });

        findViewById(R.id.test_iterator).setOnClickListener(v -> {
//            test();
            testStaticAndStack();
        });

        findViewById(R.id.test_magic).setOnClickListener(v -> {
            int GZIP_MAGIC = 0x8b1f;
            //byte 强转的时候取后8位二进制数，再根据符号位进行转化得出10进制数
            Log.e("magic", "" + (byte) GZIP_MAGIC);//31 (就是直接截取二进制最右边8位即可)
            Log.e("magic", "" + (byte) (GZIP_MAGIC >> 8));//-117 (就是直接截取二进制最右边8位即可)
            int i = 128;//二进制 1000 0000
            int h = 129;//二进制 1000 0001
            int k = 131;//二进制 1000 0011
            int j = 261;//二进制 0000 0001 0000 0101
            Log.e("magic", "" + (byte) i); //-128
            Log.e("magic", "" + (byte) h); //-127
            Log.e("magic", "" + (byte) k); //-125
            Log.e("magic", "" + (byte) j); //5 (就是直接截取二进制最右边8位即可)

            //测试获取文件流的magic
            GzipSinkTest.INSTANCE.test();
        });

        for (int i = 0; i < 100; i++) {
            list.add("a" + i);
            copyOnWriteArrayList.add("a" + i);
        }

        findViewById(R.id.test_java_heap_oom).setOnClickListener(v -> testJavaHeapOOM());

        findViewById(R.id.test_native_heap_oom1).setOnClickListener(v -> testNativeHeapOOM1());

        findViewById(R.id.test_native_heap_oom2).setOnClickListener(v -> testNativeHeapOOM2());


        for (int i = 1; i < 6; i++) {
            ApmSlowMethodStack.MethodItem methodItem = new ApmSlowMethodStack.MethodItem("" + i, i, 1);
            children.add(new ApmSlowMethodStack.TreeNode(methodItem, null));
        }
        for (int i = 0; i < 5; i++) {
            Log.e("origin", "" + children.get(i).durTime());
        }
        Collections.sort(children);
        for (int i = 0; i < 5; i++) {
            Log.e("sort", "" + children.get(i).durTime());
        }

    }

    private void test() {
        //1. 存在数组越界(list.get(i)) 使用迭代器来规避这个问题，
//        for (int i = 0; i < list.size(); i++) {
//            Log.e("for", "" + i);
//            if (i == 99) { //必须为在操作最后一个元素时候，元素被删除了，导致在get该位置元素时候越界了
//                list.remove("a99");
//            }
//            Log.e("content", "" + list.get(i));
//        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                copyOnWriteArrayList.remove(50);
//                copyOnWriteArrayList.remove(51);
//            }
//        }).start();
//        for (int i = 0; i < copyOnWriteArrayList.size(); i++) {
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Log.e("content", "index = " + i + ", content = " + copyOnWriteArrayList.get(i));
//        }

        //2. 存在数组越界(list.get(i))
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(10130); //时间需要调整，不同平台时间不一样
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Log.e("content", "remove content = " + list.remove(99));
//            }
//        }).start();
//        for (int i = 0; i < list.size(); i++) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Log.e("content", "index = " + i + ", content = " + list.get(i));
//        }

        // 3. java.util.ConcurrentModificationException，
        // 不要这迭代时候使用list.remove()操作，改用iterator.remove()替代
//        Iterator<String> iterator1 = list.iterator();
//        while (iterator1.hasNext()) {
//            String str = iterator1.next();
//            Log.e("content", "" + str);
//            if (str.equals("a4")) {
//                list.remove("a4");
//            }
//        }

        //4. java.util.ConcurrentModificationException
        //多线程是也会存在这个问题，一个线程删除，一个线程去遍历
//      new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(150);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Log.e("childThread", "remove");
//                list.remove(2);
//            }
//        }).start();
//        Iterator<String> iterator = list.iterator();
//        while (iterator.hasNext()) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            //next 必须在 remove 之前调用
//            String str = iterator.next();
//            if (str.equals("a4")) {
//                iterator.remove();
//            }
//            Log.e("content", "" + str);
//        }
    }

    private void testStaticAndStack() {
        TestBean testBeanStatic = new TestBean(nameStatic);
        TestBean testBean = new TestBean(name);
    }

    /**
     * test java head oom
     */
    private void testJavaHeapOOM() {
        for (int i = 0; i < 10240; i++) {
            TraceInfo traceInfo = new TraceInfo(0, 0, 0, 0, 0);
            testList.add(traceInfo);
        }
    }

    /**
     * test native head oom
     */
    private void testNativeHeapOOM1() {
        NativeLib.getInstance().testNativeHeadOOM1();
    }

    /**
     * test native head oom
     */
    private void testNativeHeapOOM2() {
        NativeLib.getInstance().testNativeHeadOOM2();
    }
}
