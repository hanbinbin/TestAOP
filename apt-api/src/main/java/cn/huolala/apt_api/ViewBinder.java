package cn.huolala.apt_api;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/26/22.
 * PS: Not easy to write code, please indicate.
 */
public class ViewBinder {
    public static void bind(Object target) {
        String className = target.getClass().getName();
        try {
            Class<?> clazz = Class.forName(className + "_Binder"); //要使用反射，先获取待操作类所对应的Class对象
            Binder binder = (Binder) clazz.newInstance(); //要调用Binder下面的方法，需要先创建Binder的对象
            binder.inject(target);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
