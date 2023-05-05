package cn.huolala.mytestapplication.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.huolala.mytestapplication.R;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2022/7/4.
 * PS: Not easy to write code, please indicate.
 */
public class ThreadTestActivity extends AppCompatActivity {
    List<String> list = new ArrayList<>();
    private int result = 0;
    private final Object lock = new Object();
    private int lock_result = 0;
    HandlerThread handlerThread;
    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        initData();
        testClick();
    }

    private void initData() {
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        String str = "abc";
        String str1 = null;
        Log.e("initData", "str.equals(str1) = " + str.equals(str1));
//        Log.e("initData", "str.contains(str1) = " + str.contains(str1));
    }

    private void testClick() {

        findViewById(R.id.button_test_thread).setOnClickListener(v -> {
            testMultiThread();
        });

        findViewById(R.id.dump_thread).setOnClickListener(v -> {
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            for (Thread thread : threadSet) {
                Log.e("dumpAllThreadsInfo ", "thread.name = " + thread.getName()
                        + ";group=" + thread.getThreadGroup()
                        + ";isDaemon=" + thread.isDaemon()
                        + ";priority=" + thread.getPriority());
            }
        });

        findViewById(R.id.test_sync_queue).setOnClickListener(v -> {
            final BlockingQueue<String> synchronousQueue = new SynchronousQueue<>();

            SynchronousQueueProducer queueProducer = new SynchronousQueueProducer(
                    synchronousQueue);
            new Thread(queueProducer).start();

            SynchronousQueueConsumer queueConsumer1 = new SynchronousQueueConsumer(
                    synchronousQueue);
            new Thread(queueConsumer1).start();
        });

        ExecutorService IO = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new MyThreadFactory("io"));

        findViewById(R.id.test_sync_queue_thread).setOnClickListener(v -> {
            for (int i = 0; i < 10; i++) {
                Log.i("sync_queue_thread", "" + i);
                IO.execute(new MyRunnable("" + i));
            }
        });

        findViewById(R.id.test_multi_thread).setOnClickListener(v -> {
            long start = System.currentTimeMillis();
            Thread thread1 = new Thread() {
                @Override
                public void run() {
                    super.run();
                    for (int i = 0; i < 5000000; i++) {
                        result++;
                    }
                }
            };
            Thread thread2 = new Thread() {
                @Override
                public void run() {
                    super.run();
                    for (int i = 0; i < 5000000; i++) {
                        result--;
                    }
                }
            };
            thread1.start();
            thread2.start();
            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("test_multi_thread", "result:" + result + "   cost time:" + (System.currentTimeMillis() - start) + "ms");
        });

        findViewById(R.id.test_safety_multi_thread).setOnClickListener(v -> {
            long start = System.currentTimeMillis();
            Thread thread1 = new Thread() {
                @Override
                public void run() {
                    super.run();
                    for (int i = 0; i < 5000000; i++) {
                        synchronized (lock) {
                            lock_result++;
                        }
                    }
                }
            };
            Thread thread2 = new Thread() {
                @Override
                public void run() {
                    super.run();
                    for (int i = 0; i < 5000000; i++) {
                        synchronized (lock) {
                            lock_result--;
                        }
                    }
                }
            };
            thread1.start();
            thread2.start();
            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("T_safety_multi_thread", "result:" + lock_result + "   cost time:" + (System.currentTimeMillis() - start) + "ms");
        });

        findViewById(R.id.test_one_object_synchronized).setOnClickListener(v -> {
            ThreadTestClass testClass = new ThreadTestClass();
            for (int i = 0; i < 5; i++) {
                IO.execute(new Runnable() {
                    @Override
                    public void run() {
                        testClass.add("add");
                    }
                });
                IO.execute(new Runnable() {
                    @Override
                    public void run() {
                        testClass.reduce("reduce");
                    }
                });
            }
        });

        findViewById(R.id.test_different_object_synchronized).setOnClickListener(v -> {
            ThreadTestClass testClass1 = new ThreadTestClass();
            ThreadTestClass testClass2 = new ThreadTestClass();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testClass1.add("add");
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    testClass2.add("reduce");
                }
            }).start();
        });

        findViewById(R.id.test_static_synchronized).setOnClickListener(v -> {
            for (int i = 0; i < 5; i++) {
                IO.execute(new Runnable() {
                    @Override
                    public void run() {
                        ThreadTestClass.static_add("static_add");
                    }
                });
                IO.execute(new Runnable() {
                    @Override
                    public void run() {
                        ThreadTestClass.static_reduce("static_reduce");
                    }
                });
            }
        });

        findViewById(R.id.test_class_object_synchronized).setOnClickListener(v -> {
            //类对象加锁 效果等同 对静态方法synchronized
            ThreadTestClass testClass1 = new ThreadTestClass();
            ThreadTestClass testClass2 = new ThreadTestClass();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testClass1.test("testClass1.test");
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testClass2.test("testClass2.test");
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ThreadTestClass.static_add("ThreadTestClass.static_add");
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ThreadTestClass.static_reduce("ThreadTestClass.static_reduce");
                }
            }).start();
        });

        findViewById(R.id.thread_communication).setOnClickListener(v -> {
            //TODO 测试管道有问题
            Sender sender = new Sender();
            Receiver receiver = new Receiver();
            try {
                receiver.getPipedInputStream().connect(sender.getPipedOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(sender).start();
            new Thread(receiver).start();
        });

        findViewById(R.id.lock_object_wait_notify).setOnClickListener(v -> {
            Object lock = new Object();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    synchronized (lock) {
                        Log.e("Thread1", "开始调用wait");
                        try {
                            // 同步代码内部才能调
                            lock.wait();
                            Log.e("Thread1", "wait结束, 重新获取时间片");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    synchronized (lock) {
                        Log.e("Thread2", "开始调用wait");
                        try {
                            // 同步代码内部才能调用
                            lock.wait();
                            Log.e("Thread2", "wait结束, 重新获取时间片");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    synchronized (lock) {
                        Log.e("Thread3", "开始调用");
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    synchronized (lock) {
                        Log.e("Thread4", "开始调用");
                    }
                }
            }.start();

            synchronized (lock) {
                // 同步代码内部才能调用
                Log.e("main", "开始调用 notifyAll");
                lock.notifyAll();
            }
        });
        findViewById(R.id.reentrantlock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestReentrantLock.m();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        TestReentrantLock.m1();
                    }
                }.start();

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        TestReentrantLock.m2();
                    }
                }.start();
            }
        });

        findViewById(R.id.deadlock).setOnClickListener(v -> deadlock());

        //创建子线程
        handlerThread = new HandlerThread("test_handler_thread");
        handlerThread.start();
        //获取子线程消息队列对应的handler
        handler = new Handler(handlerThread.getLooper());

        findViewById(R.id.test_choreographer_in_child_thread).setOnClickListener(v -> {
            handler.post(() -> Choreographer.getInstance());
        });
    }

    private void deadlock() {
        Beer beer = new Beer();
        Story story = new Story();

        new Thread(() -> {
            synchronized (beer) {
                Log.e("deadlock", Thread.currentThread().getName() + ": 我有酒，给我故事");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (story) {
                    Log.e("deadlock", Thread.currentThread().getName() + ": 小王开始喝酒讲故事");
                }
            }
        }, "小王").start();

        new Thread(() -> {
            synchronized (story) {
                Log.e("deadlock", Thread.currentThread().getName() + ": 我有故事，给我酒");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (beer) {
                    Log.e("deadlock", Thread.currentThread().getName() + ": 老王开始喝酒讲故事");
                }
            }
        }, "老王").start();
    }

    private void testMultiThread() {
        new Thread(() -> {
            int i = 6;
            while (i < 100) {
                list.add("" + (i++));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < list.size(); i++) {
                    String str = list.get(i);
                    Log.e("123", "" + str);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //TODO
            }
        };

        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.e("futureTask", "do callable");
                return "call result";
            }
        };

        //开启线程
        FutureTask<String> futureTask = new FutureTask<>(callable);
        Log.e("futureTask", "start");
        new Thread(futureTask).start();
        try {
            Log.e("futureTask", futureTask.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class TestThread extends Thread {
        @Override
        public void run() {
            super.run();
            Log.e("TestThread", "run");
        }
    }

    static class SynchronousQueueProducer implements Runnable {
        protected BlockingQueue<String> blockingQueue;

        public SynchronousQueueProducer(BlockingQueue<String> queue) {
            this.blockingQueue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String data = UUID.randomUUID().toString();
                    Log.e("producer", "Put: " + data);
                    blockingQueue.put(data);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    static class SynchronousQueueConsumer implements Runnable {

        protected BlockingQueue<String> blockingQueue;

        public SynchronousQueueConsumer(BlockingQueue<String> queue) {
            this.blockingQueue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String data = blockingQueue.take();
                    Log.i("consumer", Thread.currentThread().getName()
                            + " take(): " + data);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class MyRunnable implements Runnable {
        private final String id;

        public MyRunnable(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            Log.e("MyRunnable start", id + ":" + Thread.currentThread().getName());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("MyRunnable end", id + ":" + Thread.currentThread().getName());
        }
    }

    static class Sender implements Runnable {
        private final PipedOutputStream pipedOutputStream = new PipedOutputStream();

        @Override
        public void run() {
            String name = "我是发送者，将线程信息发送到其他线程";
            Log.e("我是发送消息线程", "消息：" + name);
            try {
                pipedOutputStream.write(name.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public PipedOutputStream getPipedOutputStream() {
            return pipedOutputStream;
        }
    }

    static class Receiver implements Runnable {
        private final PipedInputStream pipedInputStream = new PipedInputStream();

        @Override
        public void run() {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                byte[] bytes = new byte[1024];
                int length;
                while ((length = pipedInputStream.read(bytes)) != -1) {
                    stringBuilder.append(new String(bytes, 0, length));
                }
                Log.e("我是接收消息线程", "消息：" + stringBuilder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public PipedInputStream getPipedInputStream() {
            return pipedInputStream;
        }
    }

    class Beer {

    }

    class Story {

    }
}
