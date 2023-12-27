package cn.huolala.mytestapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import cn.huolala.mytestapplication.SecondActivity;

public class NoticeService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("NoticeService", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("NoticeService", "onStartCommand");
        if (intent != null) {
            String data = intent.getStringExtra(SecondActivity.EXTRA_DATA);
            Log.e("TestASMActivity", "" + data);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
