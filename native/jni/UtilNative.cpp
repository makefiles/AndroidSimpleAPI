#define LOG_TAG "com_amolla_jni_UtilNative"
#define PID      com_amolla_jni_UtilNative

#include <jni.h>
#include <stdbool.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <sys/stat.h>
#include <kernel_settings.h>

#ifdef __cplusplus
extern "C" {
#endif

// Beautifying the ugly Java-C++ bridge (JNI) with these macros.
#define MAKE_JNI_FUNCTION(r, n, p) extern "C" JNIEXPORT r JNICALL Java_ ## p ## _ ## n
#define JNI(r, n, p) MAKE_JNI_FUNCTION(r, n, p)
#define UNUSED(x) (void)(x)

jint JNI_OnLoad(JavaVM* jvm, void* reserved)
{
    JNIEnv *env = NULL;
    if (jvm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    return JNI_VERSION_1_6;
}

void JNI_OnUnload(JavaVM* jvm, void *reserved)
{
    JNIEnv *env = NULL;
    if (jvm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return;
    }
}

JNI(jint, nativeWriteBlock, PID) (JNIEnv *env, jclass cls, jbyteArray buf, jint offset, jint len) {
	if (NULL == buf || len <= 0) {
        return -1;
    }
    char tmp[len];
    env->GetByteArrayRegion(buf, 0, len, (jbyte *) tmp);
    return WritePartition(tmp, offset, len);
}

JNI(jint, nativeReadBlock, PID) (JNIEnv *env, jclass cls, jbyteArray buf, jint offset, jint len) {
	if (NULL == buf || len <= 0) {
        return -1;
    }
	char tmp[len];memset(tmp, 0, sizeof(char) * len);
	int result = ReadPartition(tmp, offset, len);
    env->SetByteArrayRegion(buf, 0, len, (jbyte *) tmp);
	return result;
}

#ifdef __cplusplus
}
#endif
