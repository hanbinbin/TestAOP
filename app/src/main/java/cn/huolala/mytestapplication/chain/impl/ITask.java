package cn.huolala.mytestapplication.chain.impl;

import cn.huolala.mytestapplication.bean.UserInfo;
import cn.huolala.mytestapplication.chain.UserInfoChain;
import cn.huolala.mytestapplication.chain.interceptor.ModifyAddressInterceptor;
import cn.huolala.mytestapplication.chain.interceptor.ModifyAgeInterceptor;
import cn.huolala.mytestapplication.chain.interceptor.ModifyNameInterceptor;
import cn.huolala.mytestapplication.chain.interceptor.ModifySexInterceptor;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 11/23/21.
 * PS: Not easy to write code, please indicate.
 */
public interface ITask {

    void execute(UserInfo userInfo);

    default void handleChain(UserInfo userInfo) {
        UserInfoChain userInfoChain = new UserInfoChain();
        userInfoChain.addIHandler(new ModifyNameInterceptor());
        userInfoChain.addIHandler(new ModifyAddressInterceptor());
        userInfoChain.addIHandler(new ModifySexInterceptor());
        userInfoChain.addIHandler(new ModifyAgeInterceptor());
        userInfoChain.handleChain(userInfo);
    }
}
