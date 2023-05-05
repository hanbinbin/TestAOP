package cn.huolala.mytestapplication.launch;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.huolala.mytestapplication.MainActivity;
import cn.huolala.mytestapplication.provider.LauncherHelpProvider;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2022/10/9.
 * PS: Not easy to write code, please indicate.
 */
public class CollectLaunch {
    private static boolean launcherFlag = true;
    private static long mActivityLauncherTimeStamp = 0;

    public static void initLaunch(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                getLaunchTime(activity);
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                // <!--记录上一个Activity pause节点-->
                mActivityLauncherTimeStamp = SystemClock.uptimeMillis();
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    private static void getLaunchTime(Activity activity) {
        //is launch activity
        if (activity.getClass().getName().equals(MainActivity.class.getName())) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                final long coldLauncherTime = SystemClock.uptimeMillis() - LauncherHelpProvider.sStartUpTimeStamp;
                if (launcherFlag) {
                    launcherFlag = false;
                    Log.e("application 1", "launchTime = " + coldLauncherTime);
                }
            } else {
                //--添加onWindowFocusChanged 监听--
                activity.getWindow().getDecorView().getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
                    //--onWindowFocusChanged回调--
                    @Override
                    public void onWindowFocusChanged(boolean b) {
                        //--获取首帧可见距离启动的时间--
                        final long coldLauncherTime = SystemClock.uptimeMillis() - LauncherHelpProvider.sStartUpTimeStamp;
                        activity.getWindow().getDecorView().getViewTreeObserver().removeOnWindowFocusChangeListener(this);
                        if (b && launcherFlag) {
                            launcherFlag = false;
                            Log.e("application 2", "launchTime = " + coldLauncherTime);
                        }
                    }
                });
            }
        } else {
            final long activityLauncherTime = SystemClock.uptimeMillis() - mActivityLauncherTimeStamp;
            Log.e(activity.getClass().getName(), "launcherTime = " + activityLauncherTime);
        }
    }
}
