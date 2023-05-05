package cn.huolala.mytestapplication;

import android.os.Looper;

import cn.huolala.mytestapplication.trace.LooperTracer;
import cn.huolala.mytestapplication.trace.TraceConfig;
import cn.huolala.mytestapplication.trace.UIThreadMonitor;
import cn.huolala.mytestapplication.apm.MatrixHandlerThread;
import cn.huolala.mytestapplication.utils.MatrixLog;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2022/3/28.
 * PS: Not easy to write code, please indicate.
 */
public class TracePlugin {
    private static final String TAG = "Matrix.TracePlugin";
    private final TraceConfig traceConfig;
    private LooperTracer looperTracer;

    public TracePlugin(TraceConfig config) {
        this.traceConfig = config;
    }

    public void init() {
        MatrixLog.i(TAG, "trace plugin init, trace config: %s", traceConfig.toString());
        looperTracer = new LooperTracer(traceConfig);
    }

    public void start() {
        MatrixLog.w(TAG, "start!");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (willUiThreadMonitorRunning(traceConfig)) {
                    if (!UIThreadMonitor.getMonitor().isInit()) {
                        try {
                            UIThreadMonitor.getMonitor().init(traceConfig);
                        } catch (java.lang.RuntimeException e) {
                            MatrixLog.e(TAG, "[start] RuntimeException:%s", e);
                            return;
                        }
                    }
                }
                if (traceConfig.isAnrTraceEnable()) {
                    looperTracer.onStartTrace();
                }
            }
        };

        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            runnable.run();
        } else {
            MatrixLog.w(TAG, "start TracePlugin in Thread[%s] but not in mainThread!", Thread.currentThread().getId());
            MatrixHandlerThread.getDefaultMainHandler().post(runnable);
        }
    }

    public void stop() {
        MatrixLog.w(TAG, "stop!");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (looperTracer != null) {
                    looperTracer.onCloseTrace();
                }
            }
        };

        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            runnable.run();
        } else {
            MatrixLog.w(TAG, "stop TracePlugin in Thread[%s] but not in mainThread!", Thread.currentThread().getId());
            MatrixHandlerThread.getDefaultMainHandler().post(runnable);
        }

    }

    private boolean willUiThreadMonitorRunning(TraceConfig traceConfig) {
        return traceConfig.isEvilMethodTraceEnable() || traceConfig.isAnrTraceEnable() || traceConfig.isFPSEnable();
    }
}
