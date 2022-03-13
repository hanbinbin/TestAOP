package cn.huolala.mytestapplication.chain.interceptor;

import android.util.Log;

import cn.huolala.mytestapplication.bean.UserInfo;
import cn.huolala.mytestapplication.chain.impl.IHandleChain;
import cn.huolala.mytestapplication.chain.impl.IHandler;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 11/23/21.
 * PS: Not easy to write code, please indicate.
 */
public class ModifyAgeInterceptor implements IHandler {
    @Override
    public void handle(UserInfo userInfo, IHandleChain iHandleChain) {
        userInfo.setAge(30);
        Log.e("ModifyAge", "开始编辑年龄");
        //注意这是责任链的最后一环，不需要调IHandleChain，否则会导致数组越界
        Log.e("ModifyAge", "handle 执行完");
    }
}
