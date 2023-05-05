package cn.huolala.mytestapplication;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.Choreographer;
import android.view.Display;
import android.view.WindowManager;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.huolala.mytestapplication.bean.FPSConfig;
import cn.huolala.mytestapplication.bean.TraceInfo;
import cn.huolala.mytestapplication.utils.Calculation;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/23/22.
 * PS: Not easy to write code, please indicate.
 */
public class FPSFrameCallback implements Choreographer.FrameCallback {
    private static final String TAG = "FPS_TEST";
    private long startSampleTimeInNs = 0;
    private List<Long> dataSet; //holds the frame times of the sample set
    private FPSConfig fpsConfig;

    public FPSFrameCallback(Context context) {
        initData(context);
    }

    /**
     * 重置数据
     */
    public void resetData() {
        startSampleTimeInNs = 0;
        if (dataSet != null) {
            dataSet.clear();
        }
    }

    /**
     * 初始化一些数据
     *
     * @param context
     */
    private void initData(Context context) {
        fpsConfig = new FPSConfig();
        dataSet = new ArrayList<>();
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        fpsConfig.deviceRefreshRateInMs = 1000f / display.getRefreshRate();
        fpsConfig.refreshRate = display.getRefreshRate();
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        //initial case
        if (startSampleTimeInNs == 0) {
            startSampleTimeInNs = frameTimeNanos;
        }

        //add current frame time to our list
        dataSet.add(frameTimeNanos);

        //we have exceeded the sample length ~700ms worth of data...we should push results and save current
        //frame time in new list
        if (isFinishedWithSample(frameTimeNanos)) {
            collectSampleAndSend(frameTimeNanos);
        }

        //we need to register for the next frame callback
        Choreographer.getInstance().postFrameCallback(this);
    }

    private void collectSampleAndSend(long frameTimeNanos) {
        //this occurs only when we have gathered over the sample time ~700ms
        List<Long> dataSetCopy = new ArrayList<>();
        dataSetCopy.addAll(dataSet);
        //push data to the presenter
        calculate(dataSetCopy);
        // clear data
        dataSet.clear();
        //reset sample timer to last frame
        startSampleTimeInNs = frameTimeNanos;
    }

    /**
     * returns true when sample length is exceed
     *
     * @param frameTimeNanos current frame time in NS
     * @return
     */
    private boolean isFinishedWithSample(long frameTimeNanos) {
        getThreadStackTrace();
        return (frameTimeNanos - startSampleTimeInNs) > fpsConfig.getSampleTimeInNs();
    }

    /**
     * 获取堆栈信息
     */
    private void getThreadStackTrace() {
        if (dataSet.size() <= 1) {
            return;
        }
        long timeInNS = dataSet.get(dataSet.size() - 1) - dataSet.get(dataSet.size() - 2);
        long timeInMI = TimeUnit.MILLISECONDS.convert(timeInNS, TimeUnit.NANOSECONDS);
        if ((timeInMI - fpsConfig.deviceRefreshRateInMs) < fpsConfig.freezeTime) {
            return;
        }
        //开始获取冻帧的堆栈信息
        Log.e("forzen", "start:" + dataSet.get(dataSet.size() - 2)
                + "     end:" + dataSet.get(dataSet.size() - 1)
                + "     timeInNS:" + timeInNS);
    }

    /**
     * 计算
     */
    private void calculate(List<Long> dataSetCopy) {
        long timeInNS = dataSetCopy.get(dataSetCopy.size() - 1) - dataSetCopy.get(0);
        long timeInMI = TimeUnit.MILLISECONDS.convert(timeInNS, TimeUnit.NANOSECONDS);
        List<Integer> droppedSet = Calculation.getDroppedSet(fpsConfig, dataSetCopy);
        //获取fps
        long fps = Calculation.calculateMetric(fpsConfig, dataSetCopy, droppedSet);
        //获取drop3  drop7  freeze
        int drop3 = 0;
        int drop7 = 0;
        int freeze = 0;
        for (Integer k : droppedSet) {
            if ((k - 1) >= 3 && (k - 1) < 7) {
                drop3++;
                continue;
            }
            if ((k - 1) >= 7) {
                drop7++;
                if ((k - 1) >= (fpsConfig.freezeTime / fpsConfig.deviceRefreshRateInMs)) {
                    freeze++;
                }
            }
        }
        dataSetCopy.clear();
        droppedSet.clear();
        TraceInfo traceInfo = new TraceInfo((int) timeInMI, (int) fps, drop3, drop7, freeze);
        Log.e(TAG, "traceInfo = " + traceInfo);
    }
}
