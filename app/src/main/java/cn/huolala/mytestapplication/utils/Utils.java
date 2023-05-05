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

package cn.huolala.mytestapplication.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String getStack() {
        StackTraceElement[] trace = new Throwable().getStackTrace();
        return getStack(trace);
    }

    public static String getStack(StackTraceElement[] trace) {
        return getStack(trace, "", -1);
    }

    public static String getStack(StackTraceElement[] trace, String preFixStr, int limit) {
        if ((trace == null) || (trace.length < 3)) {
            return "";
        }
        if (limit < 0) {
            limit = Integer.MAX_VALUE;
        }
        StringBuilder t = new StringBuilder(" \n");
        for (int i = 3; i < trace.length - 3 && i < limit; i++) {
            t.append(preFixStr);
            t.append("at ");
            t.append(trace[i].getClassName());
            t.append(":");
            t.append(trace[i].getMethodName());
            t.append("(" + trace[i].getLineNumber() + ")");
            t.append("\n");

        }
        return t.toString();
    }

    public static String getWholeStack(StackTraceElement[] trace, String preFixStr) {
        if ((trace == null) || (trace.length < 3)) {
            return "";
        }

        StringBuilder t = new StringBuilder(" \n");
        for (int i = 0; i < trace.length; i++) {
            t.append(preFixStr);
            t.append("at ");
            t.append(trace[i].getClassName());
            t.append(":");
            t.append(trace[i].getMethodName());
            t.append("(" + trace[i].getLineNumber() + ")");
            t.append("\n");

        }
        return t.toString();
    }

    public static String getWholeStack(StackTraceElement[] trace) {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement stackTraceElement : trace) {
            stackTrace.append(stackTraceElement.toString()).append("\n");
        }
        return stackTrace.toString();
    }

    public static String getMainThreadJavaStackTrace() {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement stackTraceElement : Looper.getMainLooper().getThread().getStackTrace()) {
            stackTrace.append(stackTraceElement.toString()).append("\n");
        }
        return stackTrace.toString();
    }

    public static String calculateCpuUsage(long threadMs, long ms) {
        if (threadMs <= 0) {
            return ms > 1000 ? "0%" : "100%";
        }

        if (threadMs >= ms) {
            return "100%";
        }

        return String.format("%.2f", 1.f * threadMs / ms * 100) + "%";
    }

    public static boolean isEmpty(String str) {
        return null == str || str.equals("");
    }


    public static String formatTime(final long timestamp) {
        return new java.text.SimpleDateFormat("[yy-MM-dd HH:mm:ss]").format(new java.util.Date(timestamp));
    }

    public static String printException(Exception e) {
        final StackTraceElement[] stackTrace = e.getStackTrace();
        if ((stackTrace == null)) {
            return "";
        }

        StringBuilder t = new StringBuilder(e.toString());
        for (int i = 2; i < stackTrace.length; i++) {
            t.append('[');
            t.append(stackTrace[i].getClassName());
            t.append(':');
            t.append(stackTrace[i].getMethodName());
            t.append("(" + stackTrace[i].getLineNumber() + ")]");
            t.append("\n");
        }
        return t.toString();
    }

    private static long result;

    public static int selfHash(String s) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        }
        Log.e("TAG0", " result=" + s.hashCode());
        innerSelfHash(s);
        return (int) result;
    }

    private static void innerSelfHash(String s) {
        result = 0;
        if (TextUtils.isEmpty(s)) {
            return;
        }
        int len = s.length();
        for (int i = 0; i < len; i++) {
            result = result * 3 + s.charAt(i);
        }
        Log.e("TAG1", " result=" + result);
        if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
            innerSelfHash(String.valueOf(result));
        }
    }

    public static void testHashSet() {
        HashSet<String> hashSet = new HashSet<>();
        boolean result1 = hashSet.add("123");
        boolean result2 = hashSet.add("123");
        boolean result3 = hashSet.add("123");
        Log.e("testHashSet", " result=" + result1);
        Log.e("testHashSet", " result=" + result2);
        Log.e("testHashSet", " result=" + result3);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("123", "456");
        hashMap.put("aaa", "456");
        hashMap.put("123", "789");
        for (String s : hashMap.keySet()) {
            Log.e("hashMap", s + "==" + hashMap.get(s));
        }

    }

    private static long trueId = 0;

    public static void storeId(int methodId) {
//        long sCurrentDiffTime = 1000;
//        trueId |= (long) methodId << 43;
//        trueId |= sCurrentDiffTime & 0x7FFFFFFFFFFL;
        long[] data = new long[]{-9223372036852651626L, -3798847858336044650L, -6695102223386383978L,
                -6689059307480127082L, -3991139247894533738L, -6779280833608914538L, -3391368849082259050L,
                -6154705452474013290L, 3068666584380762518L, 5832003187772516758L, 2444091203245861270L,
                5232232788960242070L, 2534312729374648726L, 2528269813468391830L, -6695383698363093136L,
                -6688725055945281680L, -3991288781475909776L, -6779280833608913040L, -3391703100617101456L,
                -6154705452474011792L, 3068666584380764016L, 5831668936237674352L, 2444091203245862768L,
                5232083255378866032L, 2534646980909494128L, 2527988338491682672L, 5424524178518732656L, 2125707L};
        for (long datum : data) {
            getMethodIdAndTime(datum);
        }
    }

    public static void getMethodIdAndTime(long trueId) {
        int methodId = (int) ((trueId >> 43) & 0xFFFFFL);
        long time = trueId & 0x7FFFFFFFFFFL;
//        Log.e("methodId", "" + methodId);
        Log.e("getMethodIdAndTime", methodId + ":" + time);
    }

    public static void testMoveArithmetic() {
        int orig = 132768;
        int time = orig | 0xFFFF;
        Log.e("testMoveArithmetic", "result:" + time);
        int result = time & 0xFFFF;
        Log.e("testMoveArithmetic", "result:" + result);
    }

    public static void decode(String content) {
        try {
            String result = URLDecoder.decode(content, "utf-8");
            Log.e("decode result = ", result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void getProperties() {
        try {
            Properties result = System.getProperties();
            Log.e("System.getProperties()=", result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testCharOccupyByte() {
        String str1 = "中";
        String str2 = "=";
        String str3 = "/";
        String str4 = "a";
        String str5 = "9";
        String str6 = "";
        String str7 = " ";
        String str8 = ",";
        Log.e("str1 Occupy byte size=", "" + str1.getBytes().length);
        Log.e("str2 Occupy byte size=", "" + str2.getBytes().length);
        Log.e("str3 Occupy byte size=", "" + str3.getBytes().length);
        Log.e("str4 Occupy byte size=", "" + str4.getBytes().length);
        Log.e("str5 Occupy byte size=", "" + str5.getBytes().length);
        Log.e("str6 Occupy byte size=", "" + str6.getBytes().length);
        Log.e("str7 Occupy byte size=", "" + str7.getBytes().length);
        Log.e("str8 Occupy byte size=", "" + str8.getBytes().length);
    }

    private static final Map<String, String> ABI_TO_INSTRUCTION_SET_MAP
            = new HashMap<>(16);

    static {
        ABI_TO_INSTRUCTION_SET_MAP.put("armeabi", "arm");
        ABI_TO_INSTRUCTION_SET_MAP.put("armeabi-v7a", "arm");
        ABI_TO_INSTRUCTION_SET_MAP.put("x86", "x86");
        ABI_TO_INSTRUCTION_SET_MAP.put("x86_64", "x86_64");
        ABI_TO_INSTRUCTION_SET_MAP.put("arm64-v8a", "arm64");
        ABI_TO_INSTRUCTION_SET_MAP.put("arm64-v8a-hwasan", "arm64");
        ABI_TO_INSTRUCTION_SET_MAP.put("riscv64", "riscv64");
    }

    public static void charToString(String string) {

        char[] sen = new char[]{97, 108, 105, 121, 117, 110};
        String value = String.valueOf(sen);
        Log.i("charToString", "result:" + value);

        char[] huolala = "huolala".toCharArray();
        for (char c : huolala) {
            Log.e("huolala", "result:" + (int) c);
        }

        char[] xiaolachuxing = "xiaolachuxing".toCharArray();
        for (char c : xiaolachuxing) {
            Log.e("xiaolachuxing", "result:" + (int) c);
        }
        char[] huolala1 = new char[]{104, 117, 111, 108, 97, 108, 97};
        char[] xiaolachuxing1 = new char[]{120, 105, 97, 111, 108, 97, 99, 104, 117, 120, 105, 110, 103};
    }

    public static void getCpuTypes() {
        long start = System.currentTimeMillis();
        String[] abis1 = new String[]{};
        String[] abis2 = new String[]{};
        String[] abis3 = new String[]{};
        String[] abis4 = new String[]{};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abis1 = Build.SUPPORTED_ABIS;
            abis2 = Build.SUPPORTED_32_BIT_ABIS;
            abis3 = Build.SUPPORTED_64_BIT_ABIS;
        } else {
            abis4 = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        }
        abis4 = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        StringBuilder abiStr1 = new StringBuilder();
        StringBuilder abiStr2 = new StringBuilder();
        StringBuilder abiStr3 = new StringBuilder();
        StringBuilder abiStr4 = new StringBuilder();
        for (String abi : abis1) {
            abiStr1.append(abi);
            abiStr1.append(',');
        }
        for (String abi : abis2) {
            abiStr2.append(abi);
            abiStr2.append(',');
        }
        for (String abi : abis3) {
            abiStr3.append(abi);
            abiStr3.append(',');
        }
        for (String abi : abis4) {
            abiStr4.append(abi);
            abiStr4.append(',');
        }
        Log.i("ceshi", "CPU ABIS架构: " + abiStr1 + "  cost = " + (System.currentTimeMillis() - start));
        Log.i("ceshi", "CPU 32_ABIS架构: " + abiStr2);
        Log.i("ceshi", "CPU 64_ABIS架构: " + abiStr3);
        Log.i("ceshi", "CPU架构: " + abiStr4);
        //Nexus 5 API 31:       x86_64,arm64-v8a                  32:                         64:x86_64,arm64-v8a
        //Galaxy Nexus API 21:  x86                               32:x86                      64:
        //Piexl 2  API 23:      x86                               32:x86                      64:
        //HONOR MAGIC 29:       arm64-v8a,armeabi-v7a,armeabi,    32:armeabi-v7a,armeabi      64:arm64-v8a
        //Galaxy S10+   :       arm64-v8a,armeabi-v7a,armeabi,    32:armeabi-v7a,armeabi      64:arm64-v8a
        //平板           :       mips  mips64
    }

    public static void filter() {
        List<String> lists = new ArrayList<>();
        lists.add("11111");
        lists.add("22222");
        lists.add("33333");
        lists.add("44444");
        lists.add("555555");
    }

    public static Debug.MemoryInfo getProcessMemoryData(Context context, int pid) {
        Debug.MemoryInfo memInfo = null;
        try {
            //28 为Android P
            if (Build.VERSION.SDK_INT > 28) {
                // 统计进程的内存信息 totalPss
                memInfo = new Debug.MemoryInfo();
                Debug.getMemoryInfo(memInfo);
            } else {
                //As of Android Q, for regular apps this method will only return information about the memory info for the processes running as the caller's uid;
                // no other process memory info is available and will be zero. Also of Android Q the sample rate allowed by this API is significantly limited,
                // if called faster the limit you will receive the same data as the previous call.
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                Debug.MemoryInfo[] memInfos = activityManager.getProcessMemoryInfo(new int[]{pid});
                if (memInfos != null && memInfos.length > 0) {
                    memInfo = memInfos[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memInfo;
    }

    /**
     * 获取虚拟内存大小
     * 单位 KB
     *
     * @return
     */
    public static long getVmSize(String params) {
        return getProcData("/proc/%s/status", params);
    }

    /**
     * 针对虚拟内存的监控
     *
     * 分析mapping及各个内存大小相关的字段
     * 单位 KB
     *
     * @return
     */
    public static void getSmaps() {
        String status = String.format("/proc/%s/smaps", Process.myPid());
        try {
            String content = FileUtil.getStringFromFile(status).trim();
            Log.w("getSmaps", content + " = " + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取proc 文件中的数据
     *
     * @return
     */
    public static long getProcData(String path, String paramName) {
        String status = String.format(path, Process.myPid());
        try {
            String content = FileUtil.getStringFromFile(status).trim();
            Log.w("getProcData", paramName + " = " + content);
            String[] args = content.split("\n");
            for (String str : args) {
                if (str.startsWith(paramName)) {
                    Pattern p = Pattern.compile("\\d+");
                    Matcher matcher = p.matcher(str);
                    if (matcher.find()) {
                        return Long.parseLong(matcher.group());
                    }
                }
            }
            if (args.length > 12) {
                Pattern p = Pattern.compile("\\d+");
                Matcher matcher = p.matcher(args[12]);
                if (matcher.find()) {
                    return Long.parseLong(matcher.group());
                }
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void getMemory(Context context) {
        Debug.MemoryInfo memoryInfo = getProcessMemoryData(context, Process.myPid());
        //物理内存
        int total_pss_foreground = memoryInfo.getTotalPss(); // 物理内存 单位：KB
        int dalvik_pss_foreground = memoryInfo.dalvikPss;// Java物理内存使用 单位：KB
        int native_pss_foreground = memoryInfo.nativePss;// Native物理内存使用 单位：KB
        Log.e("getMemory", "total_pss_foreground: " + total_pss_foreground / 1024 + "MB");
        Log.e("getMemory", "dalvik_pss_foreground: " + dalvik_pss_foreground / 1024 + "MB");
        Log.e("getMemory", "native_pss_foreground: " + native_pss_foreground / 1024 + "MB");
        //第二种方式获取物理内存
        long rss_size_foreground = getVmSize("VmRSS");//物理内存 单位：KB
        Log.e("getMemory", "rss_size_foreground: " + rss_size_foreground / 1024 + "MB");
        //获取memory所有信息
        Log.e("getMemory", "all message: " + memoryInfo.getMemoryStats());
        //虚拟内存
        long vm_size_foreground = getVmSize("VmSize");//虚拟内存 单位：KB
        //分析mapping及各个内存大小相关的字段
        getSmaps();
        Log.e("getMemory", "vm_size_foreground: " + vm_size_foreground / 1024 + "MB");
        //获取java堆VSS
        Runtime runtime = Runtime.getRuntime();
        long dalvikUsed = runtime.totalMemory() - runtime.freeMemory();
        Log.e("getMemory", "java堆最大内存大小=" + (runtime.maxMemory() / (1024 * 1024) + "MB"));
        Log.e("getMemory", "当前java堆内存大小=" + (runtime.totalMemory() / (1024 * 1024) + "MB"));
        Log.e("getMemory", "java堆实际使用的内存大小=" + (dalvikUsed / (1024 * 1024) + "MB"));
        double java_heap_used_rate = (double) dalvikUsed / Runtime.getRuntime().maxMemory();//Java内存使用率
        Log.e("getMemory", "java_heap_used_rate: " + java_heap_used_rate);
        //获取native堆VSS
        long nativeHeap = Debug.getNativeHeapSize();
        long nativeAllocHeap = Debug.getNativeHeapAllocatedSize();
        long nativeFreeSize = Debug.getNativeHeapFreeSize();
        Log.e("getMemory", "当前native堆内存大小:" + nativeHeap / (1024 * 1024) + "MB");
        Log.e("getMemory", "当前native堆已分配内存大小:" + nativeAllocHeap / (1024 * 1024) + "MB");
        Log.e("getMemory", "当前native堆空闲内存大小:" + nativeFreeSize / (1024 * 1024) + "MB");
        //显存
        String graphics_foreground = memoryInfo.getMemoryStat("summary.graphics");// 显存 单位：KB
        Log.e("getMemory", "graphics_foreground: " + graphics_foreground + "KB");
        double percent = (double) dalvikUsed / getAppMaxHeap(context) * 100;
        Log.e("getMemory", "percent: " + percent);
    }

    /**
     * 获取App 可用的最大内
     *
     * @return
     */
    public static long getAppMaxHeap(Context context) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        if (maxMemory == Long.MAX_VALUE) {
            if (context != null) {
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                maxMemory = am.getMemoryClass(); //if not set maxMemory, then is not large heap
            }
        }
        return maxMemory;
    }
}

//        2022-12-07 16:45:35.118 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=-3702612024980221178
//        2022-12-07 16:45:35.119 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=82322610728
//        2022-12-07 16:45:35.119 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=4790393
//        2022-12-07 16:45:35.119 8461-8461/cn.huolala.mytestapplication E/testHashCode:  hashCode=4790393
//        2022-12-07 16:45:35.119 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=9837362776351793
//        2022-12-07 16:45:35.119 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=1210020216
//        2022-12-07 16:45:35.119 8461-8461/cn.huolala.mytestapplication E/testHashCode:  hashCode=1210020216
//        2022-12-07 16:45:35.119 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=-5194328445062503229
//        2022-12-07 16:45:35.119 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=82729948113
//        2022-12-07 16:45:35.119 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=4822866
//        2022-12-07 16:45:35.119 8461-8461/cn.huolala.mytestapplication E/testHashCode:  hashCode=4822866
//        2022-12-07 16:45:35.119 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=-98331974722805256
//        2022-12-07 16:45:35.120 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=9434686560
//        2022-12-07 16:45:35.120 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=1632351
//        2022-12-07 16:45:35.120 8461-8461/cn.huolala.mytestapplication E/testHashCode:  hashCode=1632351
//        2022-12-07 16:45:35.120 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=-98331974722805256
//        2022-12-07 16:45:35.120 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=9434686560
//        2022-12-07 16:45:35.120 8461-8461/cn.huolala.mytestapplication E/TAG1:  result=1632351
//        2022-12-07 16:45:35.120 8461-8461/cn.huolala.mytestapplication E/testHashCode:  hashCode=1632351

//1631732
//    kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt minOf (F[F)F  //1631732
//    kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt maxOf (B[B)B  //1631732
//    kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt maxOf (S[S)S  //1631732
//    kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt maxOf (J[J)J  //1631732
//1631739
//    kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt minOf (D[D)D  //1631739
//    kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt maxOf (I[I)I  //1631739
//    kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt maxOf (FFF)F  //1631739
//4465963
//    kotlin.collections.unsigned.UArraysKt___UArraysKt getIndices-GBYM_sE ([B)Lkotlin.ranges.IntRange;
//    com.alibaba.sdk.android.oss.OSSImpl listObjects (Lcom.alibaba.sdk.android.oss.model.ListObjectsRequest;)Lcom.alibaba.sdk.android.oss.model.ListObjectsResult;


//4592217,25,kotlin.ranges.URangesKt___URangesKt contains-ULb-yJY (Lkotlin.ranges.ULongRange;B)Z
//4544897,25,kotlin.collections.CollectionsKt___CollectionsKt singleOrNull (Ljava.lang.Iterable;Lkotlin.jvm.functions.Function1;)Ljava.lang.Object;
//4829067,9,com.wp.apmSdk.HadesApm updateCommonConfigCostTime (J)V
//4796002,25,kotlin.collections.ArraysKt___ArraysKt dropLast ([CI)Ljava.util.List;
//4449987,131098,kotlin.collections.ArraysKt___ArraysKt scanReduceIndexed ([ZLkotlin.jvm.functions.Function3;)Ljava.util.List;
//4450751,25,kotlin.collections.ArraysKt___ArraysKt single ([JLkotlin.jvm.functions.Function1;)J
//4762384,1,com.wp.apmSdk.HadesApm startUploadCpuInfo ()V
//4813032,10,com.alibaba.sdk.android.oss.internal.ResponseParsers parseCompleteMultipartUploadResponseXML (Ljava.io.InputStream;Lcom.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;)Lcom.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;