#define LOG_TAG "com_amolla_jni_UtilNative"

#include <jni.h>
#include <stdbool.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <sys/stat.h>
#include <kernelsettings.h>

#ifdef __cplusplus
extern "C" {
#endif

jint JNI_OnLoad(JavaVM* jvm, void* reserved)
{
    JNIEnv *env = NULL;
    if ((*jvm)->GetEnv(jvm, (void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    return JNI_VERSION_1_6;
}

void JNI_OnUnload(JavaVM* jvm, void *reserved)
{
    JNIEnv *env = NULL;
    if ((*jvm)->GetEnv(jvm, (void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return;
    }
}

JNIEXPORT jint JNICALL Java_com_amolla_jni_UtilNative_nativeWriteBytes(JNIEnv *env, jclass cls, jbyteArray buf, jint offset, jint len) {
	if (NULL == buf || len <= 0) {
        return -1;
    }
    char tmp[len];
    (*env)->GetByteArrayRegion(env, buf, 0, len, (jbyte *) tmp);
    return WritePartition(tmp, offset, len);
}

JNIEXPORT jint JNICALL Java_com_amolla_jni_UtilNative_nativeReadBytes(JNIEnv *env, jclass cls, jbyteArray buf, jint offset, jint len) {
	if (NULL == buf || len <= 0) {
        return -1;
    }
	char tmp[len];memset(tmp, 0, sizeof(char) * len);
	int result = ReadPartition(tmp, offset, len);
    (*env)->SetByteArrayRegion(env, buf, 0, len, (jbyte *) tmp);
	return result;
}

#ifdef __cplusplus
}
#endif
