package cn.huolala.mytestapplication;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestEditTextActivity extends AppCompatActivity {
    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        view = (View) findViewById(R.id.bottom_view);
        // 获取屏幕宽度和高度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        Log.e("DisplayMetrics", "screenWidth = " + screenWidth
                + "; screenHeight = " + screenHeight);
        testSoftInput();
    }

    private void testSoftInput() {
        View rootView = getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            rootView.getWindowVisibleDisplayFrame(rect);
            int heightDiff = rootView.getHeight() - rect.bottom;
            boolean isSoftKeyboardOpened = heightDiff > 0;
            // 处理软键盘打开或关闭的逻辑
            Log.e("softInput status", "rootView.getHeight() = " + rootView.getHeight()
                    + "; rect.bottom = " + rect.bottom + " "
                    + (isSoftKeyboardOpened ? "软键盘开启" : "软键盘关闭"));

            Log.e("Rect1", "rect.top = " + rect.top
                    + "; rect.left = " + rect.left
                    + "; rect.right = " + rect.right
                    + "; rect.bottom = " + rect.bottom);
        });

        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            view.getWindowVisibleDisplayFrame(rect);
            Log.e("Rect2", "rect.top = " + rect.top
                    + "; rect.left = " + rect.left
                    + "; rect.right = " + rect.right
                    + "; rect.bottom = " + rect.bottom);
        });
    }
}
