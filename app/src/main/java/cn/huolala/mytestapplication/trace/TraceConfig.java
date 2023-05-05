/*
 * Tencent is pleased to support the open source community by making wechat-matrix available.
 * Copyright (C) 2018 THL A29 Limited, a Tencent company. All rights reserved.
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

import java.util.Set;

/**
 * Created by caichongyang on 17/5/18.
 * <p>
 * The config about TracePlugin setting.
 * </p>
 */

public class TraceConfig implements IDefaultConfig {
    private static final String TAG = "Matrix.TraceConfig";
    public static final int STACK_STYLE_SIMPLE = 0;
    public static final int STACK_STYLE_WHOLE = 1;
    public boolean defaultFpsEnable;
    public boolean defaultMethodTraceEnable;
    public boolean defaultStartupEnable;
    public boolean defaultAppMethodBeatEnable = true;
    public boolean defaultAnrEnable;
    public boolean defaultIdleHandlerTraceEnable;
    public boolean defaultTouchEventTraceEnable;
    public boolean isDebug;
    public boolean isDevEnv;
    public boolean defaultSignalAnrEnable;
    public int stackStyle = STACK_STYLE_SIMPLE;
    public boolean defaultMainThreadPriorityTraceEnable;
    public String splashActivities;
    public Set<String> splashActivitiesSet;
    public String anrTraceFilePath = "";
    public String printTraceFilePath = "";
    public boolean isHasActivity;
    public boolean historyMsgRecorder;
    public boolean denseMsgTracer;

    private TraceConfig() {
        this.isHasActivity = true;
    }

    @Override
    public String toString() {
        StringBuilder ss = new StringBuilder(" \n");
        ss.append("# TraceConfig\n");
        ss.append("* isDebug:\t").append(isDebug).append("\n");
        ss.append("* isDevEnv:\t").append(isDevEnv).append("\n");
        ss.append("* isHasActivity:\t").append(isHasActivity).append("\n");
        ss.append("* defaultFpsEnable:\t").append(defaultFpsEnable).append("\n");
        ss.append("* defaultMethodTraceEnable:\t").append(defaultMethodTraceEnable).append("\n");
        ss.append("* defaultStartupEnable:\t").append(defaultStartupEnable).append("\n");
        ss.append("* defaultAnrEnable:\t").append(defaultAnrEnable).append("\n");
        ss.append("* splashActivities:\t").append(splashActivities).append("\n");
        ss.append("* historyMsgRecorder:\t").append(historyMsgRecorder).append("\n");
        ss.append("* denseMsgTracer:\t").append(denseMsgTracer).append("\n");
        return ss.toString();
    }

    @Override
    public boolean isAppMethodBeatEnable() {
        return defaultMethodTraceEnable || defaultStartupEnable;
    }

    @Override
    public boolean isFPSEnable() {
        return defaultFpsEnable;
    }

    @Override
    public boolean isDebug() {
        return isDebug;
    }

    @Override

    public boolean isDevEnv() {
        return isDevEnv;
    }

    @Override
    public int getLooperPrinterStackStyle() {
        return stackStyle;
    }

    @Override
    public boolean isEvilMethodTraceEnable() {
        return defaultMethodTraceEnable;
    }

    public boolean isStartupEnable() {
        return defaultStartupEnable;
    }

    public boolean isHasActivity() {
        return isHasActivity;
    }

    @Override
    public boolean isAnrTraceEnable() {
        return defaultAnrEnable;
    }

    @Override
    public boolean isIdleHandlerTraceEnable() {
        return defaultIdleHandlerTraceEnable;
    }

    public boolean isTouchEventTraceEnable() {
        return defaultTouchEventTraceEnable;
    }

    @Override
    public boolean isSignalAnrTraceEnable() {
        return defaultSignalAnrEnable;
    }

    @Override
    public boolean isMainThreadPriorityTraceEnable() {
        return defaultMainThreadPriorityTraceEnable;
    }

    @Override
    public String getAnrTraceFilePath() {
        return anrTraceFilePath;
    }

    @Override
    public String getPrintTraceFilePath() {
        return printTraceFilePath;
    }

    @Override
    public boolean isHistoryMsgRecorderEnable() {
        return historyMsgRecorder;
    }

    @Override
    public boolean isDenseMsgTracerEnable() {
        return denseMsgTracer;
    }

    public static class Builder {
        private TraceConfig config = new TraceConfig();

        public Builder enableAppMethodBeat(boolean enable) {
            config.defaultAppMethodBeatEnable = enable;
            return this;
        }

        public Builder enableFPS(boolean enable) {
            config.defaultFpsEnable = enable;
            return this;
        }

        public Builder enableEvilMethodTrace(boolean enable) {
            config.defaultMethodTraceEnable = enable;
            return this;
        }

        public Builder enableAnrTrace(boolean enable) {
            config.defaultAnrEnable = enable;
            return this;
        }

        public Builder looperPrinterStackStyle(int stackStyle) {
            config.stackStyle = stackStyle;
            return this;
        }

        public Builder enableSignalAnrTrace(boolean enable) {
            config.defaultSignalAnrEnable = enable;
            return this;
        }

        public Builder enableStartup(boolean enable) {
            config.defaultStartupEnable = enable;
            return this;
        }

        public Builder isDebug(boolean isDebug) {
            config.isDebug = isDebug;
            return this;
        }

        public Builder isDevEnv(boolean isDevEnv) {
            config.isDevEnv = isDevEnv;
            return this;
        }

        public Builder isHasActivity(boolean isHasActivity) {
            config.isHasActivity = isHasActivity;
            return this;
        }

        public Builder splashActivities(String activities) {
            config.splashActivities = activities;
            return this;
        }

        public Builder anrTracePath(String anrTraceFilePath) {
            config.anrTraceFilePath = anrTraceFilePath;
            return this;
        }

        public Builder printTracePath(String anrTraceFilePath) {
            config.printTraceFilePath = anrTraceFilePath;
            return this;
        }

        public Builder enableIdleHandlerTrace(boolean enable) {
            config.defaultIdleHandlerTraceEnable = enable;
            return this;
        }

        public Builder enableTouchEventTrace(boolean enable) {
            config.defaultTouchEventTraceEnable = enable;
            return this;
        }

        public Builder enableMainThreadPriorityTrace(boolean enable) {
            config.defaultMainThreadPriorityTraceEnable = enable;
            return this;
        }

        public Builder enableHistoryMsgRecorder(boolean enable) {
            config.historyMsgRecorder = enable;
            return this;
        }

        public Builder enableDenseMsgTracer(boolean enable) {
            config.denseMsgTracer = enable;
            return this;
        }
        public TraceConfig build() {
            return config;
        }

    }


}
