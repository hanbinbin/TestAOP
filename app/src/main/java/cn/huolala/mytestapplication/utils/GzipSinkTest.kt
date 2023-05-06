package cn.huolala.mytestapplication.utils

import cn.huolala.mytestapplication.utils.GZipUtil.uncompress
import okio.Buffer
import okio.GzipSink
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2022/11/2.
 * PS: Not easy to write code, please indicate.
 */
object GzipSinkTest {

    fun test() {
        val gzipOutput = ByteArrayOutputStream()
        gzipOutput.write("34125y4352362423".toByteArray())
        val encodedBuffer = Buffer()
        encodedBuffer.write(gzipOutput.toByteArray())
        GzipSink(encodedBuffer).use { it.write(encodedBuffer, encodedBuffer.size()) }
        //先算出魔术数
        val byteArrayInputStream = ByteArrayInputStream(gzipOutput.toByteArray())
        GzipMagic.getMagic(byteArrayInputStream)
        //在使用系统进行解压
        uncompress(gzipOutput.toByteArray())
    }
}