package cn.huolala.mytestapplication;

import android.content.Context;
import android.util.Log;
import android.view.Choreographer;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.huolala.mytestapplication.bean.FPSConfig;
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
        return (frameTimeNanos - startSampleTimeInNs) > fpsConfig.getSampleTimeInNs();
    }

    /**
     * 计算
     */
    private void calculate(List<Long> dataSetCopy) {
        long timeInNS = dataSetCopy.get(dataSetCopy.size() - 1) - dataSetCopy.get(0);
        Log.e(TAG, "time = " + TimeUnit.MILLISECONDS.convert(timeInNS, TimeUnit.NANOSECONDS));
        List<Integer> droppedSet = Calculation.getDroppedSet(fpsConfig, dataSetCopy);
        long fps = Calculation.calculateMetric(fpsConfig, dataSetCopy, droppedSet);
        Log.e(TAG, "fps = " + fps);
    }
}
