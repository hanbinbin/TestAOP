package cn.huolala.mytestapplication.utils;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2022/3/29.
 * PS: Not easy to write code, please indicate.
 */
public class TimeCount extends CountDownTimer {
    private volatile boolean timeCountIsFinish = true; //倒计时是否完成

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        Log.e("onTick", "" + millisUntilFinished);
    }

    @Override
    public void onFinish() {
        Log.e("onFinish", "");
        setTimeCountIsFinish(true);
    }

    public void setTimeCountIsFinish(boolean timeCountIsFinish) {
        this.timeCountIsFinish = timeCountIsFinish;
    }

    public boolean isTimeCountIsFinish() {
        return timeCountIsFinish;
    }
}
