package cn.huolala.apt_annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/26/22.
 * PS: Not easy to write code, please indicate.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface OnClick {
    int value();
}
