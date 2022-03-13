package cn.huolala.mytestapplication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation Processing Tool（APT）
 * <p>
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/26/22.
 * PS: Not easy to write code, please indicate.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ContentView {
    int value(); // int value() default 1; 添加默认值
}
