package cn.huolala.mytestapplication.utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * 获取压缩文件的magic
 * <p>
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2022/11/2.
 * PS: Not easy to write code, please indicate.
 */
public class GzipMagic {
    public static void getMagic(ByteArrayInputStream byteArrayInputStream) {
        CRC32 crc = new CRC32();
        CheckedInputStream in = new CheckedInputStream(byteArrayInputStream, crc);
        crc.reset();
        try {
            int magic = readUShort(in);
            Log.e("GzipMagic", "" + magic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getMagic1(ByteArrayInputStream byteArrayInputStream) {
        try {
            int magic = readUShort(byteArrayInputStream);
            Log.e("GzipMagic", "" + magic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Reads unsigned short in Intel byte order.
     */
    private static int readUShort(InputStream in) throws IOException {
        int b = readUByte(in);
        return (readUByte(in) << 8) | b;
    }

    /*
     * Reads unsigned byte.
     */
    private static int readUByte(InputStream in) throws IOException {
        int b = in.read();
        if (b == -1) {
            throw new EOFException();
        }
        if (b < -1 || b > 255) {
            // Report on this.in, not argument in; see read{Header, Trailer}.
            throw new IOException("test magic"
                    + ".read() returned value out of range -1..255: " + b);
        }
        return b;
    }
}
