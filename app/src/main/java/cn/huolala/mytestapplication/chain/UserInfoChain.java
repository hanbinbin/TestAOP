package cn.huolala.mytestapplication.chain;

import java.util.ArrayList;
import java.util.List;

import cn.huolala.mytestapplication.bean.UserInfo;
import cn.huolala.mytestapplication.chain.impl.IHandleChain;
import cn.huolala.mytestapplication.chain.impl.IHandler;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 11/23/21.
 * PS: Not easy to write code, please indicate.
 */
public class UserInfoChain implements IHandleChain {
    private int index;
    private final List<IHandler> handlers = new ArrayList<>();

    @Override
    public void handleChain(UserInfo userInfo) {
        if (!handlers.isEmpty()) {
            IHandler iHandler = handlers.get(index++);
            iHandler.handle(userInfo, this);
        }
    }

    public void addIHandler(IHandler iHandler) {
        handlers.add(iHandler);
    }
}
