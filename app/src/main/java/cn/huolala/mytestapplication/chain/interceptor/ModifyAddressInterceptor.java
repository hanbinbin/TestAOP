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
public class ModifyAddressInterceptor implements IHandler {
    @Override
    public void handle(UserInfo userInfo, IHandleChain iHandleChain) {
        userInfo.setAddress("上海市闵行区江航南路");
        Log.e("ModifyAddress", "开始编辑地址");
        iHandleChain.handleChain(userInfo);
        Log.e("ModifyAddress", "handle 执行完");
    }
}
