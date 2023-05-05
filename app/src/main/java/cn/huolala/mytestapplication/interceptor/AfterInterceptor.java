package cn.huolala.mytestapplication.interceptor;

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
public class AfterInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response;
//        throw new IOException("AfterInterceptor throw exception");
    }
}
