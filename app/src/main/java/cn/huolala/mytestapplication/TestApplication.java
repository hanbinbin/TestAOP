package cn.huolala.mytestapplication;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.delivery.wp.argus.android.Argus;
import com.delivery.wp.argus.android.utilities.InternalLogger;
import com.delivery.wp.foundation.Foundation;
import com.delivery.wp.foundation.basic.data.WPFUserData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import cn.huolala.mytestapplication.launch.CollectLaunch;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/23/22.
 * PS: Not easy to write code, please indicate.
 */
public class TestApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        String name = null;
        Log.e("TestApplication", "attachBaseContext time = " + SystemClock.uptimeMillis());
        Log.e("TestApplication", "1.equals(null) =" + "1".equals(name));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TestApplication", "onCreate time = " + SystemClock.uptimeMillis());
        //init foundation
//        Foundation.init(this, BuildConfig.DEBUG);
//        WPFUserData.IFetchCallback back = new WPFUserData.IFetchCallback() {
//            @Override
//            public String userId() {
//                return "25364574";
//            }
//
//            @Override
//            public String userFid() {
//                return "oz0jWpJx";
//            }
//
//            @Override
//            public String token() {
//                return null;
//            }
//
//            @Override
//            public String bizCityId() {
//                return "106";
//            }
//
//            @Override
//            public String bizCityName() {
//                return "shanghai";
//            }
//
//            @Override
//            public String env() {
//                return null;
//            }
//
//            @Override
//            public WPFUserData.Location location() {
//                return null;
//            }
//        };
//        Foundation.getWPFUserData()
//                .setAppId("com.delivery.wp.argus.android.sample")//app packageName
//                .setArgusAppId("app_user_ep")//app_user_ep   app_user   app_driver  app_cdriver
//                .setDeviceId("98843")
//                .setChannel("GooglePlay")
//                .setIFetchCallback(back);

//        new Thread(() -> {
//            TraceConfig traceConfig = new TraceConfig.Builder()
//                    .enableAnrTrace(true)
//                    .isDebug(true)
//                    .isDevEnv(true)
//                    .build();
//            //初始化
//            TracePlugin tracePlugin = new TracePlugin(traceConfig);
//            tracePlugin.init();
//            //开启监听
//            tracePlugin.start();
//        }).start();

        //统计冷启动耗时
        CollectLaunch.initLaunch(this);
//        initArgus();
        //添加耗时操作，产看启动耗时(亲测有效)
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

//    private void initArgus() {
//        Set<String> trackRules = new HashSet<>();
//        trackRules.add("*");
//        Set<String> bodyRules = new HashSet<>();
//        bodyRules.add("*");
//        Map<String, String> actionNameRules = new HashMap<>();
//        actionNameRules.put("*/login/*", "dl");
//        actionNameRules.put("*/pay/*", "zf");
//
//        Argus.NetMetricsTrackRules logRules = new Argus.NetMetricsTrackRules()
//                .trackingUrlRules(trackRules)
//                .trackHttpBodyRules(bodyRules)
//                .urlToActionNameRules(actionNameRules);
//
//        Set<String> offlineBodyRules = new HashSet<>();
//        bodyRules.add("?nb=y");
//        Argus.NetMetricsTrackRules offlineLogRules = new Argus.NetMetricsTrackRules()
//                .trackingUrlRules(trackRules)
//                .trackHttpBodyRules(offlineBodyRules)
//                .urlToActionNameRules(actionNameRules);
//
//        Argus.NetMetricsTrackRules performanceRules = new Argus.NetMetricsTrackRules()
//                .trackingUrlRules(trackRules)
//                .trackHttpBodyRules(offlineBodyRules)
//                .urlToActionNameRules(actionNameRules);
//
//        Argus.Configuration config = new Argus.Configuration(this)
//                .enableConsoleLog(true)
//                .internalLogger(new AndroidLogger())
//                .logEnv("https://mdap-app-log-pre.huolala.cn")
//                .performanceEnv("https://mdap-app-monitor-pre.huolala.cn")
//                //global
////              .appId("app_sg_driver") // app_sg_driver   app_sg_user
////              .logEnv("https://sg-mdap-app-log-pre.xxx.com")
////              .performanceEnv("https://sg-mdap-app-monitor-pre.xxx.com")
//                //xl
////              .appId("app_user_xl") // app_user_xl  app_driver_xl
////              .logEnv("https://mdap-app-log.xxx.com")
////              .performanceEnv("https://mdap-app-monitor-pre.xxx.com")
//                .appVersion("3.1.4")
//                .uploadType(Argus.UploadType.OSS)
//                // mobile: 15921451295  PRE: oLA2jd7a    PRD: oz0jWpJx
//                // mobile: 13371915502  PRE: 6bb1JJmd    PRD: NMP4rEv2
//                .logNetMetricsTrackRules(logRules)
//                .offlineLogNetMetricsTrackRules(offlineLogRules)
//                .performanceNetMetricsTrackRules(performanceRules);
//        Argus.initialize(config);
//        Log.i("system info", "os_version = " + Build.VERSION.RELEASE + ", kernel_version = " + System.getProperty("os.version", "unknown"));
//    }

    /**
     * argus logger
     */
//    public static class AndroidLogger implements InternalLogger {
//
//        @Override
//        public void debug(@NonNull String s) {
//
//        }
//
//        @Override
//        public void error(@NonNull String s, @Nullable Throwable throwable) {
//
//        }
//
//        @Override
//        public void info(@NonNull String s) {
//
//        }
//
//        @Override
//        public void warn(@NonNull String s) {
//
//        }
//    }
}
