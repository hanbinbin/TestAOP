/*
 * Tencent is pleased to support the open source community by making wechat-matrix available.
 * Copyright (C) 2021 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.huolala.mytestapplication.trace;

import android.os.Handler;
import android.os.Looper;

import cn.huolala.mytestapplication.constant.Constants;
import cn.huolala.mytestapplication.apm.MatrixHandlerThread;
import cn.huolala.mytestapplication.utils.MatrixLog;
import cn.huolala.mytestapplication.utils.Utils;

public class LooperTracer extends Tracer {

    private static final String TAG = "Matrix.Tracer";
    private Handler lagHandler;
    private final TraceConfig traceConfig;
    private final LagHandleTask lagTask = new LagHandleTask();
    private boolean isTraceEnable;

    public LooperTracer(TraceConfig traceConfig) {
        this.traceConfig = traceConfig;
        this.isTraceEnable = traceConfig.isAnrTraceEnable();
    }

    @Override
    public void onAlive() {
        super.onAlive();
        if (isTraceEnable) {
            UIThreadMonitor.getMonitor().addObserver(this);
            this.lagHandler = new Handler(MatrixHandlerThread.getDefaultHandler().getLooper());
        }
    }

    @Override
    public void onDead() {
        super.onDead();
        if (isTraceEnable) {
            UIThreadMonitor.getMonitor().removeObserver(this);
            lagHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void dispatchBegin(long beginNs, long cpuBeginMs, long token) {
        super.dispatchBegin(beginNs, cpuBeginMs, token);
        if (traceConfig.isDevEnv()) {
            MatrixLog.v(TAG, "* [dispatchBegin] token:%s", token);
        }
        long cost = (System.nanoTime() - token) / Constants.TIME_MILLIS_TO_NANO;
        lagHandler.postDelayed(lagTask, Constants.DEFAULT_NORMAL_LAG - cost);
    }


    @Override
    public void dispatchEnd(long beginNs, long cpuBeginMs, long endNs, long cpuEndMs, long token, boolean isBelongFrame) {
        super.dispatchEnd(beginNs, cpuBeginMs, endNs, cpuEndMs, token, isBelongFrame);
        if (traceConfig.isDevEnv()) {
            long cost = (endNs - beginNs) / Constants.TIME_MILLIS_TO_NANO;
            MatrixLog.v(TAG, "[dispatchEnd] token:%s cost:%sms cpu:%sms usage:%s",
                    token, cost,
                    cpuEndMs - cpuBeginMs, Utils.calculateCpuUsage(cpuEndMs - cpuBeginMs, cost));
        }
        lagHandler.removeCallbacks(lagTask);
    }

    class LagHandleTask implements Runnable {

        @Override
        public void run() {
            try {
                MatrixLog.e(TAG, "happens lag : %s, start ");
                StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
                String dumpStack = Utils.getWholeStack(stackTrace);
                MatrixLog.e(TAG, "happens lag : %s, scene : %s ", dumpStack, "scene");
            } catch (Exception e) {
                MatrixLog.e(TAG, "[JSONException error: %s", e);
            }
        }
    }
}
