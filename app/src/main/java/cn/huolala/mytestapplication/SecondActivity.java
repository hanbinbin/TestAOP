package cn.huolala.mytestapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.huolala.mytestapplication.apm.MatrixHandlerThread;
import cn.huolala.mytestapplication.bean.UserInfo;
import cn.huolala.mytestapplication.chain.ModifyTask;
import cn.huolala.mytestapplication.interceptor.AfterInterceptor;
import cn.huolala.mytestapplication.interceptor.BeforeInterceptor;
import cn.huolala.mytestapplication.interceptor.CatchInterceptor;
import cn.huolala.mytestapplication.reference.TestReference;
import cn.huolala.mytestapplication.thread.ThreadUtils;
import cn.huolala.mytestapplication.threadpool.WPFThreadPool;
import cn.huolala.mytestapplication.utils.TelephoneNumberUtils;
import cn.huolala.mytestapplication.utils.Utils;
<<<<<<< HEAD
import cn.huolala.mytestapplication.utils.WPFTraceIdUtils;
=======
>>>>>>> 5551c44 (项目测试代码)
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.EventListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 9/23/21.
 * PS: Not easy to write code, please indicate.
 */
public class SecondActivity extends AppCompatActivity {
    private OkHttpClient okHttpClient;
    String language = "z" + "h";
    String country = "C" + "N";
    Handler handler = MatrixHandlerThread.getDefaultHandler();
    private final Object mLock = new Object();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.e("traceId", "result = " + WPFTraceIdUtils.next());
        findViewById(R.id.output).setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SecondActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                testFileOutputStream();
            }
//            String str = "message";
//            String extra1 = new String(str);
//            String extra2 = str;
//            Log.e("SecondActivity", "str:" + str);
//            Log.e("SecondActivity", "extra1:" + extra1);
//            Log.e("SecondActivity", "extra2:" + extra2);
//            str = null;
//            Log.e("SecondActivity", "str:" + str);
//            Log.e("SecondActivity", "extra1:" + extra1);
//            Log.e("SecondActivity", "extra2:" + extra2);
        });

        findViewById(R.id.input).setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(SecondActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SecondActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                testFileInputStream();
            }
        });

        TextView textView = findViewById(R.id.telephone_recognize);
        //开始识别电话号码
        findViewById(R.id.telephone_recognize).setOnClickListener(v -> {
            String beforeStr = "这是测试数据";
            List<String> afterStr = TelephoneNumberUtils.readTelNumByPattern(beforeStr);
            dealWith(beforeStr, afterStr, textView);
        });

        //测试责任链模式
        findViewById(R.id.chain).setOnClickListener(v -> {
            //开启操作用户
            new ModifyTask().execute(new UserInfo());
        });

        //测试 argus
        findViewById(R.id.test_argus).setOnClickListener(v -> {
            Request request = new Request.Builder()
                    .url("https://oimg.huolala.cn/stone/prd/20220616175250-3eb9-51040607.png")
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("onFailure", "result = " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.e("onResponse", "code = " + response.code() + " response result = " + response.message());
                    response.close();
                }
            });
        });

        findViewById(R.id.utc_to_beijing).setOnClickListener(v -> {
//            Log.e("current time", "" + System.currentTimeMillis());
//            Log.e("current time to bj", "" + utcToBJ(null));
//
//            long logTime = 1660492800000L; //2011-08-15 00:00:00;
//            String pattern = "yyyy-MM-dd HH:mm:SS";
//            Log.e("log time str", "" + new SimpleDateFormat(pattern).format(new Date(logTime)));
//            Log.e("log time to bj str", "" + utcToBJStr(logTime, pattern));
//
//            long logTime1 = 1660579200000L;//2022-08-16 00:00:00;
//            TimeZone.setDefault(TimeZone.getTimeZone("GMT-5"));
//            Calendar c = Calendar.getInstance();
//            c.setTime(new Date(logTime1));
//            String clientDate1 = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
//            Log.e("logTime1", clientDate1);
            getCurrentTimeZone();


            String logTime2 = "2022-08-17";
            TimeZone.getDefault();
            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long time = 0;
            try {
                time = sdf.parse(logTime2).getTime();
                Log.e("logTime", "" + time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            long time1 = 0;
            try {
                time1 = sdf1.parse(logTime2).getTime();
                Log.e("logTime1", "" + time1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int current = -7;//配置任务人所属时区
            int client = -9;//客户端用户所属时区
            long dayTime = 24 * 60 * 60 * 1000; //一天的时间
            if (current >= 0) {
                if (client > 0) {
                    time = time + client * 60 * 60 * 1000;
                } else {
                    time = time - dayTime + Math.abs(client * 60 * 60 * 1000);
                }
            } else {
                if (client > 0) {
                    time = time + dayTime + client * 60 * 60 * 1000;
                } else {
                    time = time + Math.abs(client * 60 * 60 * 1000);
                }
            }
            Log.e("time", "" + time);
            Log.e("date", "" + sdf.format(time));
        });

        findViewById(R.id.jump_to_third).setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
            startActivity(intent);
        });
        LinkedList<String> list = new LinkedList<>();
        //测试linkList
        findViewById(R.id.test_link_list).setOnClickListener(v -> {
            Log.e("测试linkList", "通过list.push添加数据");
            list.push("aaaa"); //等同  list.addFirst("aaaa");
            list.push("bbbb");
            list.push("cccc");
            list.push("dddd");
            list.push("eeee");
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                Log.e("测试linkList", "iterator " + iterator.next());
            }
            ListIterator<String> iterator0 = list.listIterator(list.size());
            while (iterator0.hasNext()) {
                Log.e("测试linkList", "iterator0 next = " + iterator0.next());
            }
            while (iterator0.hasPrevious()) {
                Log.e("测试linkList", "iterator0 previous = " + iterator0.previous());
            }

            Log.e("测试linkList", "getFirst " + list.getFirst());
            Log.e("测试linkList", "getLast " + list.getLast());

            Log.e("测试linkList", "peek " + list.peek());
            Log.e("测试linkList", "peek " + list.peek());

            Log.e("测试linkList", "pop " + list.pop());
            Log.e("测试linkList", "pop " + list.pop());
            Log.e("测试linkList", "pop " + list.pop());
            Log.e("测试linkList", "pop " + list.pop());
            Log.e("测试linkList", "pop " + list.pop());
            Log.e("测试linkList", "list size " + list.size());

            Log.e("测试linkList", "-------------------------------");
            Log.e("测试linkList", "通过list.add添加数据");
            list.add("aaaa"); //等同  list.addLast("aaaa");
            list.add("bbbb");
            list.add("cccc");
            list.add("dddd");
            list.add("eeee");
            ListIterator<String> iterator1 = list.listIterator(2);
//            while (iterator1.hasNext()) {
//                Log.e("测试linkList", "iterator1 next = " + iterator1.next());
//            }
            while (iterator1.hasPrevious()) {
                Log.e("测试linkList", "iterator1 previous = " + iterator1.previous());
            }

            Log.e("测试linkList", "getFirst " + list.getFirst());
            Log.e("测试linkList", "getLast " + list.getLast());

            Log.e("测试linkList", "peek " + list.peek());
            Log.e("测试linkList", "peek " + list.peek());

            Log.e("测试linkList", "pop " + list.pop());
            Log.e("测试linkList", "pop " + list.pop());
            Log.e("测试linkList", "pop " + list.pop());
            Log.e("测试linkList", "pop " + list.pop());
            Log.e("测试linkList", "pop " + list.pop());
            Log.e("测试linkList", "list size " + list.size());

            List arrayList = new ArrayList();
            arrayList.add("1");
            arrayList.add("2");
            arrayList.add("3");
            arrayList.add("4");
            arrayList.add("5");
            arrayList.add("6");
            Log.e("ArrayList", "content" + arrayList.subList(arrayList.size() - 4, arrayList.size()));

            ConcurrentLinkedDeque<String> dumpStacks = new ConcurrentLinkedDeque<>();
            dumpStacks.add("1");
            dumpStacks.add("2");
            dumpStacks.add("3");
            dumpStacks.add("4");
            dumpStacks.add("5");
            Log.e("ConcurrentLinkedDeque", "content " + Arrays.asList(dumpStacks.toArray()).subList(dumpStacks.size() - 4, dumpStacks.size()));


            List<String> testList = new ArrayList<>();
            testList.add("1");
            testList.add("2");
            testList.add("3");
            testList.add("4");
            testList.add("5");
            testList.add("6");
            List<String> sub = testList.subList(2, testList.size());
            Log.e("test sub list", "origin = " + testList);
            Log.e("test sub list", "sub = " + sub);
            testList.remove(0);
            testList.remove(0);
            testList.remove(0);
            Log.e("test remove list", "remove = " + testList);

            Exception exception1 = new Exception("设计费东方财富以你");
            Exception exception2 = new Exception("安家费IE附件");
            Exception exception3 = new Exception("年度非GV");
            Exception exception4 = new Exception("MVIGUR他");
            Log.e("Exception", "exception1 = " + exception1);
            Log.e("Exception", "exception2 = " + exception2);
            Log.e("Exception", "exception3 = " + exception3);
            Log.e("Exception", "exception4 = " + exception4);
        });

        //防止对测试代码影响，暂时注销
//        //测试lock wait notify
//        AtomicInteger c = new AtomicInteger();
//        //发送消息到子线程消息队列
//        handler.post(() -> {
//            while (true) {
//                Log.e("wait", "result = " + c.getAndIncrement());
//                synchronized (mLock) {
//                    try {
//                        mLock.wait(); //交出当前线程执行的时间片
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        findViewById(R.id.test_object_lock).setOnClickListener(v -> {
//            synchronized (mLock) {
//                mLock.notify(); //唤醒线程
//            }
//        });


        findViewById(R.id.get_current_memory).setOnClickListener(v -> {
            Utils.getMemory(SecondActivity.this);
        });

        List<Object> objects = new ArrayList<>();
        findViewById(R.id.test_m1).setOnClickListener(v -> {
            for (int i = 0; i < 100000; i++) {
                objects.add(new Object());
            }

            WPFThreadPool.getInstance().compute().execute(() -> {
                List<Object> objs = new ArrayList<>();
                for (int i = 0; i < 100000; i++) {
                    objs.add(new Object());
                }
            });
        });

        findViewById(R.id.test_m2).setOnClickListener(v -> {
            total = 0;
//            innerInvoke();
            List<Thread> threadList = new ArrayList<>();
            for (int i = 0; i < 1000000; i++) {
                Thread mMyThread = new Thread();
                threadList.add(mMyThread);
                mMyThread.start();
            }
        });


        TextView textView1 = findViewById(R.id.test_textView_requestLayout);
        textView1.setOnClickListener(v -> {
            //在子线程来更新UI
            handler.post(() -> {
                String string = textView1.getText().toString();
                textView1.setText(string + "aaaa");
                //textView1默认width=match_parent,height=wrap_content;
                // 1.高度不发生变化时候最终会触发ViewRootImpl的onDescendantInvalidated() -> invalidate() -> scheduleTraversals()
                // 2.会触发如果高度触发生改变会触发ViewRootImpl的requestLayout(),此时会去检测线程是否一致
            });
        });

        //测试软/弱应用
        findViewById(R.id.test_reference).setOnClickListener(v -> {
            TestReference.testSoftReference();//软引用
            TestReference.testWeakReference();//弱引用
        });

        //test_synchronized
        findViewById(R.id.test_synchronized).setOnClickListener(v -> {
            new Thread("Thread1") {
                @Override
                public void run() {
                    Log.e("Thread", Thread.currentThread().getName());
                    ThreadUtils.printName1();
                }
            }.start();

            new Thread("Thread2") {
                @Override
                public void run() {
                    Log.e("Thread", Thread.currentThread().getName());
                    ThreadUtils.printName2();
                }
            }.start();
        });

        findViewById(R.id.test_write_to_sdCard).setOnClickListener(v -> {
            new Thread("Thread1") {
                @Override
                public void run() {
                    write("AopDemo.zip");
                    write("ASM_Transform.zip");
                    write("TestC++App.zip");
                    Log.e("test_write_to_sdCard", "finish ");
                }
            }.start();
        });

        Pattern cellPhonePattern = Pattern.compile("eapi.huolala.cn/");
        Matcher cellPhoneMatcher = cellPhonePattern.matcher("https://eapi.huolala.cn/?_m=ep&_a=sign_config");
        Log.e("test1", "result = " + cellPhoneMatcher.matches());

        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new BeforeInterceptor())
                .addInterceptor(new CatchInterceptor())
                .addInterceptor(new AfterInterceptor())
                .eventListener(new EventListener() {
                    @Override
                    public void callEnd(Call call) {
                        super.callEnd(call);
                    }

                    @Override
                    public void callFailed(Call call, IOException ioe) {
                        super.callFailed(call, ioe);
                        Log.e("callFailed", "" + ioe.getMessage());
                    }
                })
                .build();

        //创建fragment
        new MyFragment();
    }

    private void write(String srcPath) {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1002);
            }

            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/aaaa");
            if(!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, srcPath);
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = this.getAssets().open(srcPath);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    int total = 0;
    private void innerInvoke() {
        if (total > 1000) {
            return;
        }
        total++;
        Calendar cal_bj = Calendar.getInstance(new Locale(language, country));
        int zoneOffset = cal_bj.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal_bj.get(Calendar.DST_OFFSET);
        cal_bj.setTime(new Date(1678950000));
        cal_bj.add(Calendar.MILLISECOND, 8 * 60 * 60 * 1000 - (zoneOffset + dstOffset));
        cal_bj.getTime().getTime();//to beijing 1660514355240  2022-08-15 10:59:15
        innerInvoke();
    }

    /**
     * utc time to BJ time
     *
     * @param time utc time
     * @return utc to bj result
     */
    public Long utcToBJ(Long time) {
        Calendar cal_bj = Calendar.getInstance(new Locale(language, country));
        int zoneOffset = cal_bj.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal_bj.get(Calendar.DST_OFFSET);
        if (time != null) {
            cal_bj.setTime(new Date(time));
        }
        cal_bj.add(Calendar.MILLISECOND, 8 * 60 * 60 * 1000 - (zoneOffset + dstOffset));
        return cal_bj.getTime().getTime();//to beijing 1660514355240  2022-08-15 10:59:15
    }

    /**
     * utc long time to BJ str time
     *
     * @param time    utc time
     * @param pattern date format pattern
     * @return format bj time result
     */
    @SuppressLint("SimpleDateFormat")
    public String utcToBJStr(Long time, String pattern) {
        Calendar cal_str = Calendar.getInstance(new Locale(language, country));
        int zoneOffset = cal_str.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal_str.get(Calendar.DST_OFFSET);
        if (time != null) {
            cal_str.setTime(new Date(time));
        }
        cal_str.add(Calendar.MILLISECOND, 8 * 60 * 60 * 1000 - (zoneOffset + dstOffset));
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);//yyyy-MM-dd
        return sdf.format(cal_str.getTime());
    }

    public void getCurrentTimeZone() {
        Calendar cal_str = Calendar.getInstance();
        int zoneOffset = cal_str.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal_str.get(Calendar.DST_OFFSET);
        int currentTimeZone = (zoneOffset + dstOffset) / (3600 * 1000);
        Log.e("getCurrentTimeZone", "zoneOffset = " + zoneOffset + ";  dstOffset = " + dstOffset);
        Log.e("getCurrentTimeZone", "currentTimeZone = " + currentTimeZone);
    }


    private void dealWith(String str, List<String> patternList, TextView textView) {
        if (TextUtils.isEmpty(str) || patternList == null || textView == null) {
            return;
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        for (String pattern : patternList) {
            if (!str.contains(pattern)) {
                continue;
            }
            int start = str.indexOf(pattern);
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //点击事件
                    Log.e("SecondActivity", TelephoneNumberUtils.doWithTelNum(pattern));
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    //设置文件颜色
                    ds.setColor(textView.getResources().getColor(R.color.teal_200));
                    // 去掉下划线
                    ds.setUnderlineText(false);
                }

            }, start, start + pattern.length(), 0);
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    private void testFileOutputStream() {
        FileOutputStream fos = null;
        try {
            String path = getExternalFilesDir(Environment.DIRECTORY_DCIM).toString();
            File file = new File(path + "/fos.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file.getPath());
            // 调用write()方法
            //fos.write(97); //97 -- 底层二进制数据	-- 通过记事本打开 -- 找97对应的字符值 -- a （是通过ASCII码表得出的对应字符）
            // fos.write(57);
            // fos.write(55);

            //public void write(byte[] b):写一个字节数组
            byte[] bys = {97, 98, 99, 100, 101}; //abcde
            fos.write(bys);

            byte[] bys1 = {-26, -75, -117, -24, -81, -107}; //测试
            fos.write(bys1);

            String str = "测试字符";
            byte[] testStr = str.getBytes();
            for (byte byt : testStr) {
                Log.e("测试字符", "" + byt);
            }
            fos.write(testStr);

            //中文字符转byte数组的过程：
//            例如;中 这个字是如何转换成一个byte数组的？
//            String str = "中";
//            byte [] bs = str.getBytes("UTF-8");
//            首先，任意一个字符都定义在统一的unicode编码表中，unicode有65535个字符，几乎包含了所有的语言。所以‘中’在unicode中的编码为\u4E2D(十六机制)对应10进制为20013.
//                    然后看下utf-8计算byte数组的算法：
//            var4[0] = (byte)(224 | var8 >> 12);
//            var4[1] = (byte)(128 | var8 >> 6 & 63);
//            var4[2] = (byte)(128 | var8 & 63);
//
//            第0个数组 20013>> 12 结果再和224进行或运算
//            20013二进制： 0 1 0 0 1 1 1 0 0 0 1 0 1 1 0 1
//            向右移动12位： 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0
//            224的二进制： 0 0 0 0 0 0 0 0 1 1 1 0 0 0 0 0
//                    | 运算结果为： 0 0 0 0 0 0 0 0 1 1 1 0 0 1 0 0
//            11100100的十进制为： -28
//
//            第1个数组 20013>> 6 结果再和63与运算 再和128或运算
//            20013二进制:  0 1 0 0 1 1 1 0 0 0 1 0 1 1 0 1
//            向右移动6位:  0 0 0 0 0 0 0 1 0 0 1 1 1 0 0 0
//            63的二进制:   0 0 0 0 0 0 0 0 0 0 1 1 1 1 1 1
//                    & 运算结果为： 0 0 0 0 0 0 0 0 0 0 1 1 1 0 0 0
//            128的二进制为： 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
//                    | 运算结果为： 10111000
//            十进制为： -72
//
//            第2个数组 20013& 63 再和128或运算
//            20013二进制： 0 1 0 0 1 1 1 0 0 0 1 0 1 1 0 1
//            63的二进制： 0 0 0 0 0 0 0 0 0 0 1 1 1 1 1 1
//                    & 运算结果为： 0 0 0 0 0 0 0 0 0 0 1 0 1 1 0 1
//            128的二进制为： 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
//                    | 运算结果为： 10101101
//            十进制为： -83
//
//            这样3个byte数组的数字分别是 -28 -72 -83 ，大家可以通过java运行来检验下结果是否正确，
//            其实第0位数组算的是16位字节中的前4位，第1位数组放的5-10位字节，第2位数组放的是11-16位的字节，既 4+6+6 = 16 byte数组按照这种方式切分。

            //public void write(byte[] b,int off,int len):写一个字节数组的一部分
            fos.write(bys, 1, 3);
            String str1 = "\n应用名称            应用标识                        应用版本     发起下载数    下载成功数       成功率(%)\n";
            String str2 = "货运司机    com.lalamove.huolala.driver            6.1.57        59382         53796        90.6\n";
            String str3 = "货运司机    com.lalamove.huolala.driver            6.1.21        51537         39422        76\n";
            String str4 = "小拉司机     com.xiaolachuxing.driver              1.0.28         4921         2194         45\n";
            String str5 = "小拉司机     com.xiaolachuxing.driver              1.0.30         6782         5667         84.4\n";
            String str6 = "小拉用户     com.xiaolachuxing.user                1.2.3          15042         7858        52.2\n";
            String str7 = "小拉用户     com.xiaolachuxing.user                1.2.4          7427          3677        49.5\n";
            fos.write(str1.getBytes());
            fos.write(str2.getBytes());
            fos.write(str3.getBytes());
            fos.write(str4.getBytes());
            fos.write(str5.getBytes());
            fos.write(str6.getBytes());
            fos.write(str7.getBytes());
            byte[] bytes = {123, 34, 101, 110, 116, 105, 116, 121, 34, 58, 123, 34, 97, 99, 116, 105, 111, 110, 34, 58, 49, 44, 34, 99, 104, 97, 116, 84, 121, 112, 101, 34, 58, 50, 44, 34, 99, 111, 110, 116, 101, 110, 116, 34, 58, 34, 34, 44, 34, 102, 97, 99, 101, 85, 114, 108, 34, 58, 34, 34, 44, 34, 110, 105, 99, 107, 110, 97, 109, 101, 34, 58, 34, -24, -82, -94, -27, -115, -107, -27, -80, -66, -27, -113, -73, 56, 56, 57, 48, 49, 56, 48, 34, 44, 34, 115, 101, 110, 100, 84, 105, 109, 101, 34, 58, 48, 44, 34, 115, 101, 110, 100, 101, 114, 34, 58, 34, 49, 52, 53, 55, 54, 50, 51, 50, 51, 52, 51, 49, 55, 57, 53, 57, 49, 54, 56, 34, 44, 34, 117, 117, 105, 100, 34, 58, 110, 117, 108, 108, 44, 34, 118, 101, 114, 115, 105, 111, 110, 34, 58, 49, 125, 125};
            fos.write(bytes);
            //释放资源
            fos.close();
            Log.e("123254533", file.getName());

            //截取字符串
            String testPath = "argus-offline-20211223.glog";
            String stringDate =
                    testPath.substring(testPath.lastIndexOf("-") + 1, testPath.indexOf(".glog"));
            Log.e("123254533", stringDate);

            //测试位移运算
            int a = 1;
            int b = 1 << 1;
            int c = 1 << 2;
            int d = 1 << 3;
            int e = 1 << 4;
            Log.e("123254533", "a=" + a + " : b=" + b + " : c=" + c + " : d=" + d + " : e=" + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testFileInputStream() {
        try {
            String path = getExternalFilesDir(Environment.DIRECTORY_DCIM).toString();
            File file = new File(path + "/fos.txt");
            if (!file.exists()) {
                Toast.makeText(this, "文件不存在，无法读取文件", Toast.LENGTH_SHORT).show();
                return;
            }
            FileInputStream fis = new FileInputStream(file.getPath());

            byte[] bys = new byte[1024];
            int len = 0;
            while ((len = fis.read(bys)) != -1) {
                Log.e("fis: ", new String(bys, 0, len));
            }
            // 释放资源
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
