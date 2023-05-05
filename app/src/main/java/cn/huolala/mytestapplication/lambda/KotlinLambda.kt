package cn.huolala.mytestapplication.lambda

import android.content.Context
import android.widget.TextView

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2023/1/15.
 * PS: Not easy to write code, please indicate.
 */
class KotlinLambda(context: Context) {
    val textView = TextView(context)

    private fun lambda() {

        val runnable = Runnable {
            println("Hello World!")
        }

        textView.setOnClickListener {
            println("Hello World!")
        }
    }
}