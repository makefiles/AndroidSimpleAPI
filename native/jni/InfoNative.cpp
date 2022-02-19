#define LOG_TAG "com_amolla_jni_InfoNative"

#include <jni.h>
#include <stdbool.h>
#include <kernelsettings.h>

#define BUFF_MAX 512

#ifdef __cplusplus
extern "C" {
#endif

jint JNI_OnLoad (JavaVM* jvm, void* reserved)
{
    JNIEnv *env = NULL;
    if ((*jvm)->GetEnv(jvm, (void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    readKernelSettings();
    return JNI_VERSION_1_6;
}

void JNI_OnUnload (JavaVM* jvm, void *reserved)
{
    JNIEnv *env = NULL;
    if ((*jvm)->GetEnv(jvm, (void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return;
    }
    freeKernelSettings();
    return;
}

JNIEXPORT jstring JNICALL Java_com_amolla_jni_InfoNative_nativeGetModelName (JNIEnv *env, jclass cls)
{
    char buffer[BUFF_MAX];
    GetModelName(buffer);
    return (*env)->NewStringUTF(env, buffer);
}

JNIEXPORT jstring JNICALL Java_com_amolla_jni_InfoNative_nativeGetProcessorName (JNIEnv *env, jclass cls)
{
    char buffer[BUFF_MAX];
    GetProcessorName(buffer);
    return (*env)->NewStringUTF(env, buffer);
}

JNIEXPORT jint JNICALL Java_com_amolla_jni_InfoNative_nativeGetHardwareRevision (JNIEnv *env, jclass cls)
{
    return GetHardwareRevision();
}

JNIEXPORT jstring JNICALL Java_com_amolla_jni_InfoNative_nativeGetHardwareRevisionName (JNIEnv *env, jclass cls)
{
    char buffer[BUFF_MAX];
    GetHardwareRevisionName(buffer);
    return (*env)->NewStringUTF(env, buffer);
}

JNIEXPORT jboolean JNICALL Java_com_amolla_jni_InfoNative_nativeSetDeviceModuleName (JNIEnv *env, jclass cls, jint index, jstring name)
{
    const char * buffer = (*env)->GetStringUTFChars(env, name, 0);
    jboolean result = SetDeviceModuleName(index, (char *) buffer);
    (*env)->ReleaseStringUTFChars(name, buffer);
    return result;
}

JNIEXPORT jstring JNICALL Java_com_amolla_jni_InfoNative_nativeGetDeviceModuleName (JNIEnv *env, jclass cls, jint index)
{
    char buffer[BUFF_MAX];
    GetDeviceModuleName(index, buffer);
    return (*env)->NewStringUTF(env, buffer);
}

#ifdef __cplusplus
}
#endif
