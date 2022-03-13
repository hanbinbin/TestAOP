package cn.huolala.apt_api;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/26/22.
 * PS: Not easy to write code, please indicate.
 */
public interface Binder<T> {

    void inject(T t);
}
