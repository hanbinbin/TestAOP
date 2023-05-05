package cn.huolala.mytestapplication.bean

import com.model.binbin.mylibrary.TestInternalBean

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2023/2/3.
 * PS: Not easy to write code, please indicate.
 */
class ChildTestInternalBean : TestInternalBean() {
    internal val email = "test@gmail.com"  //同一个模块内，父类属性定义为internal 子类可以继承其属性；
                                           //如果在不同模块内，子类不能继承父类internal属性，且自己可以定义相同名字的属性，或者父类属性剔除internal关键字

    override fun toString(): String {
        return "{ name:$name;" +
                " age:$age; " +
                "address:$address;" +
                " email:$email }"
    }
}