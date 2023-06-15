package cn.huolala.mytestapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.huolala.mytestapplication.utils.DensityUtils;

public class SurfaceActivity extends AppCompatActivity {
    private final static String TAG = "SurfaceActivity.class";
    private final static Object object = new Object();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    static class MyView extends SurfaceView implements SurfaceHolder.Callback {
        SurfaceHolder surfaceHolder;
        MyThread myThread;

        public MyView(Context context) {
            super(context);
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            Log.e(TAG, "in MyView constructor get surfaceHolder = " + surfaceHolder);
            myThread = new MyThread(surfaceHolder);
            myThread.start();
        }

        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            myThread.status = 1;
            synchronized (object) {
                object.notify();
            }
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
            myThread.status = -1;
            Log.e(TAG, "in SurfaceHolder.Callback's surfaceDestroyed call back get surfaceHolder = " + surfaceHolder);
        }
    }

    static class MyThread extends Thread {
        private final SurfaceHolder holder;
        private int status = 0; // 0 初始态 1 运行态 -1 暂停态

        public MyThread(SurfaceHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            int count = 0;
            Log.e(TAG, "in SurfaceHolder.Callback's surfaceCreated call back and new thread to get surfaceHolder = " + holder);
            while (true) {
                Canvas c;
                try {
                    if (status == 0) {
                        continue;
                    }
                    count++;
                    c = holder.lockCanvas();//锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
                    //设置画布背景颜色
                    c.drawColor(Color.BLUE);
                    //创建画笔
                    Paint p = new Paint();
                    p.setColor(Color.WHITE);
                    Rect r = new Rect(100, 50, 1000, 1250);
                    //绘制一个区域
                    c.drawRect(r, p);
                    //重新设置画笔颜色，绘制字体大小
                    p.setColor(Color.RED);
                    p.setTextSize(DensityUtils.textSize(14));
                    c.drawText("这是第" + count + "秒", 100, 310, p);

                    //重新设置画笔颜色，绘制字体大小
                    p.setColor(Color.GRAY);
                    p.setTextSize(DensityUtils.textSize(16));
                    c.drawText("这是第" + count + "秒", 100, 510, p);

                    //重新设置画笔颜色，绘制字体大小
                    p.setColor(Color.GREEN);
                    p.setTextSize(DensityUtils.textSize(18));
                    c.drawText("这是第" + count + "秒", 100, 710, p);
                    holder.unlockCanvasAndPost(c);//结束锁定画图，并提交改变。
                    Thread.sleep(1000);//睡眠时间为1秒
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (object) {
                    if (status == -1) {
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }
}
