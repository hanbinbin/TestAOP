package cn.huolala.mytestapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.huolala.apt_annotion.OnClick;
import cn.huolala.apt_api.ViewBinder;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/26/22.
 * PS: Not easy to write code, please indicate.
 */
@SuppressLint("NonConstantResourceId")
public class TestClassAPTActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_class_apt);
        ViewBinder.bind(this); //触发绑定操作
    }

    @OnClick(R.id.toast)
    public void toast(View view) {
        Toast.makeText(this, "点击我之后弹出吐司", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.jump_to_second)
    public void jumpToSecond(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
