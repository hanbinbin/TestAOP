package cn.huolala.mytestapplication.utils;


//import com.delivery.wp.foundation.Foundation;
//import com.delivery.wp.foundation.basic.util.WPFString;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class WPFTraceIdUtils {
    private static String UUID_TERMINAL_ID;
    private static AtomicInteger sequence;

    /**
     * get traceId
     *
     * @return str
     */
    public static String next() {
//        if (Foundation.isEmpty(UUID_TERMINAL_ID)) {
//            String uuid = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
//            if (uuid.length() > 16) {
//                uuid = uuid.substring(uuid.length() - 16);
//            }
//            UUID_TERMINAL_ID = uuid.concat(".201."); //.201. is protocol rules
//        }
        long currentTimeMillis = System.currentTimeMillis() * 10000;
        return UUID_TERMINAL_ID + currentTimeMillis + nextSeq();
    }

    /**
     * inner method -- get next sequence
     *
     * @return int
     */
    private static int nextSeq() {
        if (sequence == null) {
            sequence = new AtomicInteger(1);
        }
        if (sequence.get() > 10000) {
            sequence.set(1);
        }
        return sequence.getAndIncrement();
    }
}
