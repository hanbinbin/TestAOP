package cn.huolala.mytestapplication.chain;

import cn.huolala.mytestapplication.bean.UserInfo;
import cn.huolala.mytestapplication.chain.impl.ITask;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 11/23/21.
 * PS: Not easy to write code, please indicate.
 */
public class ModifyTask implements ITask {
    @Override
    public void execute(UserInfo userInfo) {
        handleChain(userInfo);
    }
}
