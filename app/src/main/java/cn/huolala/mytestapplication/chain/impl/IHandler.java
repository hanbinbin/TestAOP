package cn.huolala.mytestapplication.chain.impl;

import cn.huolala.mytestapplication.bean.UserInfo;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 11/23/21.
 * PS: Not easy to write code, please indicate.
 */
public interface IHandler {

    void handle(UserInfo userInfo, IHandleChain iHandleChain);
}
