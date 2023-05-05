package cn.huolala.mytestapplication.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2022/10/9.
 * PS: Not easy to write code, please indicate.
 */
public class LauncherHelpProvider extends ContentProvider {
    // 用来记录启动时间
    public static long sStartUpTimeStamp = SystemClock.uptimeMillis();

    @Override
    public boolean onCreate() {
        Log.e("LauncherHelpProvider", "sStartUpTimeStamp = " + sStartUpTimeStamp);
        Log.e("LauncherHelpProvider", "onCreate time = " + SystemClock.uptimeMillis());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
