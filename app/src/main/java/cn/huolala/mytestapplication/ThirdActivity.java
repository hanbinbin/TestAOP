package cn.huolala.mytestapplication;

import android.animation.AnimatorInflater;
import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

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
public class ThirdActivity extends AppCompatActivity implements LeakInterface, SensorEventListener {
    private final String TAG = "ThirdActivity";
    ApmSlowMethodStack exampleTest = new ApmSlowMethodStack();
    List<String> list = new LinkedList<>();
    List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();

    List<TraceInfo> testList = new ArrayList<>();

    LinkedList<ApmSlowMethodStack.TreeNode> children = new LinkedList<>();
    public static String nameStatic = "nameStatic";
    String name = "name";

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private Sensor accelerometerSensor;
    private float[] gyroValues = new float[3];
    private float[] accelValues = new float[3];

    private int index = 111111;
    Animation anim_in;
    Animation anim_out;
    boolean clickStatus1 = true;
    boolean clickStatus2 = true;

    private final RequestListener<Drawable> mRequestListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            WebpDrawable webpDrawable = (WebpDrawable) resource;
            //需要设置为循环1次才会有onAnimationEnd回调
            webpDrawable.setLoopCount(1);
            webpDrawable.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationStart(Drawable drawable) {
                    super.onAnimationStart(drawable);
                }

                @Override
                public void onAnimationEnd(Drawable drawable) {
                    super.onAnimationEnd(drawable);
                    webpDrawable.unregisterAnimationCallback(this);
                }
            });
            return false;
        }
    };
    private Transformation<Bitmap> mTransformation;
    private WebpDrawableTransformation mWebpDrawableTransformation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        CallBackManager.addCallBack(this);//测试内存泄漏
        mTransformation = new CenterInside();
        mWebpDrawableTransformation = new WebpDrawableTransformation(mTransformation);
        anim_in = AnimationUtils.loadAnimation(this, R.anim.anim_appearing);
        anim_out = AnimationUtils.loadAnimation(this, R.anim.anim_disappearing);
//        initSensor();
        LinearLayout view_container = findViewById(R.id.view_container);
        setClick(view_container);
        addAnim(view_container);

        LinearLayout view_anim_layout = findViewById(R.id.view_anim_layout);
        ImageView image_view1 = findViewById(R.id.image_view1);
        ImageView image_view2 = findViewById(R.id.image_view2);


        view_container.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.e("view_container", "onLayoutChange");
            }
        });

        view_anim_layout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.e("view_anim_layout", "onLayoutChange");
            }
        });
        image_view1.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.e("image_view1", "onLayoutChange");
            }
        });
        image_view2.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.e("image_view2", "onLayoutChange");
            }
        });

        setImageView(image_view1, clickStatus1 ? R.drawable.voice_open : R.drawable.voice_close);
        setAnimImage(image_view2, clickStatus2 ? R.drawable.preview_close : R.drawable.preview_open);


        image_view1.setOnClickListener(v -> {
            clickStatus1 = !clickStatus1;
            setImageView(image_view1, clickStatus1 ? R.drawable.voice_open : R.drawable.voice_close);
        });

        image_view2.setOnClickListener(v -> {
            clickStatus2 = !clickStatus2;
            setAnimImage(image_view2, clickStatus2 ? R.drawable.preview_close : R.drawable.preview_open);
        });
    }

    /**
     * 展示webp动画效果
     *
     * @param imageView
     * @param drawableRes
     */
    private void setImageView(ImageView imageView, int drawableRes) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), drawableRes, null);
        imageView.setImageDrawable(drawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (drawable instanceof AnimatedImageDrawable) {
                AnimatedImageDrawable animatedImageDrawable = (AnimatedImageDrawable) drawable;
                animatedImageDrawable.setRepeatCount(0);
                animatedImageDrawable.start();
            }
        }
    }

    /**
     * 设置按钮动画
     *
     * @param resource
     * @param imageView
     */
    private void setAnimImage(ImageView imageView, int resource) {
        Glide.with(ThirdActivity.this)
                .load(resource)
                .optionalTransform(mTransformation)
                .optionalTransform(WebpDrawable.class, mWebpDrawableTransformation)
                .addListener(mRequestListener)
                .into(imageView);
    }

    private void addAnim(LinearLayout view_container) {
        LayoutTransition layoutTransition = view_container.getLayoutTransition();
        if (layoutTransition == null) {
            layoutTransition = new LayoutTransition();
        }
        layoutTransition.setStartDelay(LayoutTransition.APPEARING, 10);
        layoutTransition.setDuration(1000);
        layoutTransition.setAnimator(LayoutTransition.APPEARING, AnimatorInflater.loadAnimator(this, R.animator.animator_view_appearing));
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, AnimatorInflater.loadAnimator(this, R.animator.animator_view_disappearing));
        view_container.setLayoutTransition(layoutTransition);
    }

    private void addView(LinearLayout view_container) {
        index++;
        TextView textView = new TextView(ThirdActivity.this);
        textView.setText("" + index);
        int color = ThirdActivity.this.getResources().getColor(R.color.teal_200);
        textView.setBackgroundColor(color);
        textView.setTranslationX(200);
        view_container.addView(textView);
    }

    private void deleteView(LinearLayout view_container) {
        int childCount = view_container.getChildCount();
        if (childCount > 0) {
            View view = view_container.getChildAt(0);
            view_container.removeView(view);
        }
    }

    /**
     * 设置点击事件
     */
    private void setClick(LinearLayout view_container) {
        findViewById(R.id.add_view).setOnClickListener(v -> {
            addView(view_container);
        });

        findViewById(R.id.remove_view).setOnClickListener(v -> {
            deleteView(view_container);
        });

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

    /**
     * 获取传感器 + 获取陀螺仪 + 传感器
     */
    private void initSensor() {
        Log.e(TAG, "initSensor");
        //获取传感器管理
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 获取陀螺仪传器
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        // 获取加速计传感器
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
//        // 注册陀螺仪传器监听器
//        sensorManager.registerListener(this, gyroscopeSensor, 100);
//        // 注册加速计传感器监听器
//        sensorManager.registerListener(this, accelerometerSensor, 100);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
//        // 取消注册传感器监听器
//        sensorManager.unregisterListener(this);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.e(TAG, "onSensorChanged");
        // 根传感器类型获取对的数值
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroValues = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelValues = event.values;
        }
        if (gyroValues != null && accelValues != null) {
            Log.e(TAG, "gyroValues != null && accelValues != null");
            // 计算转向度
            float[] rotationMatrix = new float[9];
            boolean success = SensorManager.getRotationMatrix(rotationMatrix, null, accelValues, gyroValues);

            if (success) {
                Log.e(TAG, "success");
                float[] orientationAngles = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientationAngles);

                // 转向角度（弧度）
                float azimuthRadians = orientationAngles[0];
                float pitchRadians = orientationAngles[1];
                float rollRadians = orientationAngles[2];

                // 将弧度转为角度
                float azimuthDegrees = (float) Math.toDegrees(azimuthRadians);
                float pitchDegrees = (float) Math.toDegrees(pitchRadians);
                float rollDegrees = (float) Math.toDegrees(rollRadians);

                // 在这里可以使用转向角度进行相应操作
                // ...
                Log.d("ThirdActivity", "Azimuth: " + azimuthDegrees + ", Pitch: " + pitchDegrees + ", Roll: " + rollDegrees);
            } else {
                Log.e(TAG, "fail");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
