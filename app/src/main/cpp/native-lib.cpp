#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_procialize_singleevent_LoginActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_procialize_singleevent_Activity_MainActivity_stringFromJNI(JNIEnv *env,
                                                                     jobject instance) {

    // TODO

}