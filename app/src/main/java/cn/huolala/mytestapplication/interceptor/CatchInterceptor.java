package cn.huolala.mytestapplication.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2023/2/16.
 * PS: Not easy to write code, please indicate.
 */
public class CatchInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        try {
            return chain.proceed(request);
        } catch (Exception exception) {
            Log.e("CatchInterceptor", "" + exception.getMessage());
            throw exception;
        }
    }
}
