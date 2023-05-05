package cn.huolala.mytestapplication

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.zip.GZIPOutputStream

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("cn.huolala.mytestapplication", appContext.packageName)
    }

    @Test
    fun testMagic() {
        val GZIP_MAGIC = 0x8b1f
        Log.e("testMagic", "" + GZIP_MAGIC.toByte())
        Log.e("testMagic", "" + (GZIP_MAGIC shr 8).toByte())
    }
}