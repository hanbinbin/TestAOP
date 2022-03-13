package cn.huolala.mytestapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 以下测试的是运行时候的注解，例如反射解析并使用
 *
 * 反射：它允许运行中的 Java 程序获取自身的信息，并且可以操作类或对象的内部属性。
 * 反射机制：可以在运行时访问Java对象的属性，方法，构造方法等
 *
 * <p>
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/26/22.
 * PS: Not easy to write code, please indicate.
 */
@ContentView(R.layout.activity_test_run_time_apt)
public class TestRunTimeAPTActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject(this); //可以在父类的 onCreate() 方法里面去注册这个方法
    }

    /**
     * 放到父类里面去实现
     *
     * @param target
     */
    private void inject(Object target) {
        Class<?> clazz = target.getClass(); //要使用反射，先获取待操作类所对应的Class对象
        //此时需要的是运行时注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        //这时候
        if (contentView != null) {
            int layout = contentView.value();
            try {
                Method method = clazz.getMethod("setContentView", int.class);
                method.invoke(target, layout);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
