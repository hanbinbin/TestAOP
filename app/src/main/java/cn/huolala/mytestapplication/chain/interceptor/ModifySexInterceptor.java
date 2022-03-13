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
public class ModifySexInterceptor implements IHandler {
    @Override
    public void handle(UserInfo userInfo, IHandleChain iHandleChain) {
        userInfo.setSex(1);
        Log.e("ModifySex", "开始编辑性别");
        iHandleChain.handleChain(userInfo);
        Log.e("ModifySex", "handle 执行完");
    }
}
