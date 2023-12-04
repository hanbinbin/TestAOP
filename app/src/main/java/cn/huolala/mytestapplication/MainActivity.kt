package cn.huolala.mytestapplication

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import cn.huolala.mytestapplication.bean.ChildTestInternalBean
import cn.huolala.mytestapplication.thread.ThreadTestActivity
import cn.huolala.mytestapplication.utils.TimeCount
import cn.huolala.mytestapplication.utils.Utils
import com.delivery.wp.a_module.AModuleBean
import com.delivery.wp.a_module.AModuleUtils
import com.delivery.wp.b_module.BModuleBean
import com.delivery.wp.c_module.CModuleBean
import com.model.binbin.mylibrary.AppUtils
import okhttp3.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.net.ssl.*

class MainActivity : AppCompatActivity() {
    var fpsFrameCallback: FPSFrameCallback? = null
    private val atomicBoolean = AtomicBoolean(false)
    val timeCount = TimeCount(6000, 1000)
    var client: OkHttpClient? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        Log.e("MainActivity", "attachBaseContext")
        Log.e("MainActivity", "${"4.2.2" >= "4.0.0"}")
        Log.e("MainActivity", ChildTestInternalBean().toString())
        val thread = Thread({
            Thread.sleep(3000)
            Log.e("MainActivity", "sleep=" + 3000)
        }, "MainActivity-attachBaseContext-thread-name")
        Log.e("MainActivity", "thread.name=" + thread.name)
        thread.start()

        Log.e("AppUtils.getLibraryName", AppUtils.getLibraryName())
        Log.e("AModuleUtils.getName()", AModuleUtils.getName())
        try {
            //此处会报异常，因为mylibrary库新0.0.4版本里面已经删除AppUtils.getLibraryVersion()方法，
            // 导致AModuleUtils.getVersion()调用时候找不到其方法(a-module模块依赖的mylibrary库的版本是0.0.3)
            Log.e("AModuleUtils.getVersion", AModuleUtils.getVersion())
        } catch (e: Throwable) {  //会抛出来 java.lang.NoSuchMethodError; 使用Exception无法来捕获，必须使用使用父类Throwable来捕获
            e.message?.let { Log.e("Exception", it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("MainActivity", "onCreate")
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
        Log.e("Build.MODEL", "" + Build.MODEL)
        val button1 = findViewById<Button>(R.id.button1)
        button1.text = this.getString(R.string.test_name)
        button1.setOnClickListener {
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
            doOption.startDoOption("folder2/test4", object : OptionClickListener {
                override fun optionClick(tag: String, option: String) {
                    Log.e(tag, option)
                }
            })
        }

        findViewById<Button>(R.id.test_fps_start).setOnClickListener {
//            if (atomicBoolean.compareAndSet(false, true)) {
//                fpsFrameCallback?.resetData()
//                Choreographer.getInstance().postFrameCallback(fpsFrameCallback)
//            }
            timeCount.start()
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

        findViewById<Button>(R.id.jump_to_edit_text).setOnClickListener {
            startActivity(Intent(this, TestEditTextActivity::class.java))
        }

        findViewById<Button>(R.id.jump_to_thread).setOnClickListener {
            startActivity(Intent(this, ThreadTestActivity::class.java))
//            doRequest()
        }
        findViewById<Button>(R.id.jump_to_surface_view).setOnClickListener {
            startActivity(Intent(this, SurfaceActivity::class.java))
        }
        findViewById<ConstraintLayout>(R.id.test_surface).setOnClickListener {
            //如果test_surface(如果蒙层下面的控件消耗了事件，则当这个地方不会发生回调；如果事件没有被消耗，当前这个地方会回调)
            Log.e("ConstraintLayout", "测试没背景的控件，点击事件会穿透 = $it")
        }
        testHashCode()
//       Utils.storeId(602789)
        Utils.testHashSet()
        Utils.testMoveArithmetic()
        val str1 =
            "https:\\/\\/dapi.huolala.cn\\/?_m=get_todo_list&args=%7B%22scene%22%3A1%2C%22network_type%22%3A%22WIFI%22%2C%22todo_type%22%3A0%7D&shllv=560001&os=android&version=6.3.4&revision=6304&_t=1672264491&token=CF27154B0AE39B3954CAE3416403C0F60F261CE53842CFB2A52D613FB375ED7A2A3CDB470E17FDA7ADD7671FDCC1A0DB&hf_version=&device_type=Redmi%20Note%208%20Pro&device_id=e882a9a1b3942b60&city_id=1017&driver_md5=364c750ddda945183508d9649b153699&_su=22122905545069910000005669921373&_sign=C22AC1A2DAAB1DE373DC1B04BFCEB640"
        Utils.decode(str1)
        val str2 =
            "https:\\/\\/mda.huolala.cn\\/driver\\/online\\/report?shllv=560001&os=android&version=6.3.4&revision=6304&_t=1672240282&token=CF27154B0AE39B3954CAE3416403C0F60F261CE53842CFB2A52D613FB375ED7A2A3CDB470E17FDA7ADD7671FDCC1A0DB&hf_version=&device_type=Redmi%20Note%208%20Pro&device_id=e882a9a1b3942b60&city_id=1017&driver_md5=364c750ddda945183508d9649b153699&_su=22122823112202810000003950422582&_sign=8088E6328D8FA78E44FEB8C784EC6C6A"
        Utils.decode(str2)

        Utils.getProperties()

        Utils.testCharOccupyByte()

        Utils.getCpuTypes()

        Utils.charToString("aliyun")

        val sen = charArrayOf(
            97.toChar(),
            108.toChar(),
            105.toChar(),
            121.toChar(),
            117.toChar(),
            110.toChar()
        )
        val value = String(sen)
        Log.e("test", " result=$value")
        //test other module
        testModule()
    }

    private fun testHashCode() {
        val str1 = "kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt.minOf.(F[F)F"
        val str2 = "kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt.maxOf.(B[B)B"
        val str3 = "kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt.maxOf.(S[S)S"
        val str4 = "kotlin.comparisons.ComparisonsKt___ComparisonsJvmKt.maxOf.(J[J)J"

        Log.e("testHashCode", " hashCode=" + Utils.selfHash(str1))//814817137
        Log.e("testHashCode", " hashCode=" + Utils.selfHash(str2))//1003378811
        Log.e("testHashCode", " hashCode=" + Utils.selfHash(str3))//-654816996
        Log.e("testHashCode", " hashCode=" + Utils.selfHash(str4))//-654816996
    }

    private fun doRequest() {
        if (client == null) {
            createSSlSocketFactory()
        }
        val request: Request = Request.Builder()
            .url("https://huolala.cn")
            .get() //The default is GET request, you can not write
            .build()
        client?.let { inner ->
            val call: Call = inner.newCall(request)
            call.enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("TAG", " onFailure: " + e.message)
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        val string = response.body()!!.string()
                        Log.e("TAG", " code = " + response.code())
                        Log.e("TAG", " onResponse: $string")
                    }
                })
        }
    }

    private fun createSSlSocketFactory() {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        val inputStream =
            BufferedInputStream(assets.open("huolala.cn.crt"))
//        val inputStreamNew =
//            BufferedInputStream(assets.open("huolala.cn.pem"))
        val socketFactory = getSslSocketFactory(arrayOf(inputStream))
        val trustManager = systemDefaultTrustManager()
        builder.sslSocketFactory(socketFactory, trustManager)
        client = builder.build()
    }

    private fun getSslSocketFactory(certificates: Array<InputStream>): SSLSocketFactory? {
        val trustManagers: Array<TrustManager>? = prepareTrustManager(certificates.asList())
        val sslContext: SSLContext
        val trustManager: X509TrustManager?
        try {
            trustManager = chooseTrustManager(trustManagers)
            sslContext = SSLContext.getInstance("TLS")
            sslContext.init(
                null,
                arrayOf<TrustManager?>(trustManager),
                null
            )
            return sslContext.socketFactory
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun prepareTrustManager(list: List<InputStream>): Array<TrustManager>? {
        if (list.isEmpty()) {
            return null
        }
        try {
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val sKeyStore =
                KeyStore.getInstance(KeyStore.getDefaultType())
            sKeyStore.load(null, null)
            var index = 0
            list.forEach { certificate ->
                val certificateAlias = (index++).toString()
                sKeyStore.setCertificateEntry(
                    certificateAlias,
                    certificateFactory.generateCertificate(certificate)
                )
                try {
                    certificate.close()
                } catch (e: IOException) {

                }
            }
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(sKeyStore)
            return trustManagerFactory.trustManagers
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun chooseTrustManager(trustManagers: Array<TrustManager>?): X509TrustManager? {
        if (trustManagers == null) {
            return null
        }
        for (trustManager in trustManagers) {
            if (trustManager is X509TrustManager) {
                return trustManager
            }
        }
        return null
    }

    private fun systemDefaultTrustManager(): X509TrustManager? {
        try {
            val trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm()
            )
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                throw IllegalStateException(
                    "Unexpected default trust managers:"
                            + Arrays.toString(trustManagers)
                )
            }
            return trustManagers[0] as X509TrustManager
        } catch (e: GeneralSecurityException) {
            throw Exception("No System TLS", e) // The system has no TLS. Just give up.
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

    private fun testModule() {
        val aBean = AModuleBean()
        val bBean = BModuleBean()
        val cBean = CModuleBean()
        Log.e("TAG", " testModule: ${aBean.name}")
        Log.e("TAG", " testModule: ${bBean.name}")
        Log.e("TAG", " testModule: ${cBean.name}")
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

