package cn.huolala.mytestapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    var fpsFrameCallback: FPSFrameCallback? = null
    private val atomicBoolean = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

//        Thread.sleep(20000) //耗时时操作,不会弹ANR弹窗

        fpsFrameCallback = FPSFrameCallback(this)

        val doOption = DoOption()
        val list = ArrayList<Test>()
        val map = HashMap<String, Test>()
        val test1 = Test("111")
        val test2 = Test("222")
        val test3 = Test("333")
        val test4 = Test("444")
        list.add(test1)
        map["1"] = test1
        list.add(test2)
        map["2"] = test2
        list.add(test3)
        map["3"] = test3
        list.add(test4)
        map["4"] = test4


        findViewById<Button>(R.id.button1).setOnClickListener {
            //TODO 测试ANR
//            Thread.sleep(15000) //耗时操作,再点击其他控件，会导致ANR
            doOption.startDoOption("test1", object : OptionClickListener {
                override fun optionClick(tag: String, option: String) {
                    Log.e(tag, option)
                }
            })
            Log.e("list", list.toString())
            Log.e("map", map.toString())
            list.clear()
            Log.e("list", list.toString())
            Log.e("map", map.toString())
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            doOption.startDoOption("test2", object : OptionClickListener {
                override fun optionClick(tag: String, option: String) {
                    Log.e(tag, option)
                }
            })
        }

        findViewById<Button>(R.id.test_fps_start).setOnClickListener {
            if (atomicBoolean.compareAndSet(false, true)) {
                fpsFrameCallback?.resetData()
                Choreographer.getInstance().postFrameCallback(fpsFrameCallback)
            }
        }

        findViewById<Button>(R.id.test_fps_stop).setOnClickListener {
            if (atomicBoolean.compareAndSet(true, false)) {
                Choreographer.getInstance().removeFrameCallback(fpsFrameCallback)
            }
        }

        findViewById<Button>(R.id.test_RunTime_APT).setOnClickListener {
            startActivity(Intent(this, TestRunTimeAPTActivity::class.java))
        }

        findViewById<Button>(R.id.test_Class_APT).setOnClickListener {
            startActivity(Intent(this, TestClassAPTActivity::class.java))
        }

        findViewById<Button>(R.id.jump_to_second).setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }

    }

    /**
     * 测试掉帧情况
     */
    override fun onResume() {
        super.onResume()
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}

interface TestClickListener {
    fun testClick(test: String)
}


interface OptionClickListener {
    fun optionClick(tag: String, option: String)
}


class DoOption {

    fun startDoOption(tag: String, listener: OptionClickListener) {

        Test("123").setOnClickListener(object : TestClickListener {
            override fun testClick(test: String) {
                listener.optionClick(tag, test)
            }
        })
    }
}


class Test(name: String) {

    fun setOnClickListener(listener: TestClickListener) {
        listener.testClick("${Random().nextInt(11)}")
    }
}

