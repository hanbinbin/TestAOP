#include <jni.h>
#include <string>
#include "test.h"

extern "C" JNIEXPORT jstring JNICALL
Java_cn_huolala_test_1c_NativeLib_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_cn_huolala_test_1c_NativeLib_testOOM1(
        JNIEnv *env,
        jobject /* this */) {
    testOOM1();
}

extern "C" JNIEXPORT void JNICALL
Java_cn_huolala_test_1c_NativeLib_testOOM2(
        JNIEnv *env,
        jobject /* this */) {
    testOOM2();
}