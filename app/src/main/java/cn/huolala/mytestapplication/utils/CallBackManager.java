package cn.huolala.mytestapplication.utils;

import java.util.ArrayList;

import cn.huolala.mytestapplication.impl.LeakInterface;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2022/11/22.
 * PS: Not easy to write code, please indicate.
 */
public class CallBackManager {
    public static ArrayList<LeakInterface> sCallBacks = new ArrayList<>();

    public static void addCallBack(LeakInterface callBack) {
        sCallBacks.add(callBack);
    }

    public static void removeCallBack(LeakInterface callBack) {
        sCallBacks.remove(callBack);
    }
}
