package cn.huolala.mytestapplication.lambda;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2023/1/15.
 * PS: Not easy to write code, please indicate.
 */
public class JavaLambda {
    private Context context;
    private TextView textView;

    public JavaLambda(Context context) {
        this.context = context;
        textView = new TextView(context);
    }

    private void lambda() {
        Runnable runnable = () -> System.out.println("runnable!");
        textView.setOnClickListener(v -> System.out.println("on click"));
    }

}
