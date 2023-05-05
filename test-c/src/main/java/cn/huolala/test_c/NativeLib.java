package cn.huolala.test_c;

public class NativeLib {

    // Used to load the 'test_c' library on application startup.
    static {
        System.loadLibrary("test_c");
    }

    public static NativeLib getInstance() {
        return Holder.holder;
    }

    public static class Holder {
        private static final NativeLib holder = new NativeLib();
    }

    public void testNativeHeadOOM1() {
        testOOM1();
    }

    public void testNativeHeadOOM2() {
        testOOM2();
    }

    /**
     * A native method that is implemented by the 'test_c' native library,
     * which is packaged with this application.
     */
    private native String stringFromJNI();

    private native void testOOM1();

    private native void testOOM2();
}