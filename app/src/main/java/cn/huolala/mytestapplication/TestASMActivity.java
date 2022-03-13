package cn.huolala.mytestapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * ASM 是一个通用的 Java 字节码操作和分析框架，它可用于直接以二进制形式修改现有类或动态生成类。
 *
 * 在Android中，字节码插桩的时机就是生成class文件和转换成dex文件之间，这里用到一个AGP的内置api，Transform
 *
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/27/22.
 * PS: Not easy to write code, please indicate.
 */
public class TestASMActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
