package cn.huolala.mytestapplication.bean;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 3/1/22.
 * PS: Not easy to write code, please indicate.
 */
public class FPSConfig implements Serializable {
    public float refreshRate = 60; //60fps
    public float deviceRefreshRateInMs = 16.6f; //value from device ex 16.6 ms
    // making final for now.....want to be solid on the math before we allow an
    // arbitrary value
    public final long sampleTimeInMs = 736;//928;//736; // default sample time

    public long getSampleTimeInNs() {
        return TimeUnit.NANOSECONDS.convert(sampleTimeInMs, TimeUnit.MILLISECONDS);
    }

    public FPSConfig(){

    }
}
