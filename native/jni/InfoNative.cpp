/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

#define LOG_TAG "com_amolla_jni_InfoNative"
#define PID      com_amolla_jni_InfoNative

#include <jni.h>
#include <stdbool.h>
#include <kernel_settings.h>

#define BUFF_MAX 512

#ifdef __cplusplus
extern "C" {
#endif

// Beautifying the ugly Java-C++ bridge (JNI) with these macros.
#define MAKE_JNI_FUNCTION(r, n, p) extern "C" JNIEXPORT r JNICALL Java_ ## p ## _ ## n
#define JNI(r, n, p) MAKE_JNI_FUNCTION(r, n, p)
#define UNUSED(x) (void)(x)

jint JNI_OnLoad (JavaVM* jvm, void* reserved)
{
    JNIEnv *env = NULL;
    if (jvm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    InitKernelSettings();
    return JNI_VERSION_1_6;
}

void JNI_OnUnload (JavaVM* jvm, void *reserved)
{
    JNIEnv *env = NULL;
    if (jvm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return;
    }
    FreeKernelSettings();
    return;
}

JNI(jstring, nativeGetModelName, PID) (JNIEnv *env, jclass cls)
{
    char buffer[BUFF_MAX];
    GetModelName(buffer);
    return env->NewStringUTF(buffer);
}

JNI(jstring, nativeGetProcessorName, PID) (JNIEnv *env, jclass cls)
{
    char buffer[BUFF_MAX];
    GetProcessorName(buffer);
    return env->NewStringUTF(buffer);
}

JNI(jint, nativeGetHardwareRevision, PID) (JNIEnv *env, jclass cls)
{
    return GetHardwareRevision();
}

JNI(jstring, nativeGetHardwareRevisionName, PID) (JNIEnv *env, jclass cls)
{
    char buffer[BUFF_MAX];
    GetHardwareRevisionName(buffer);
    return env->NewStringUTF(buffer);
}

JNI(jboolean, nativeSetDeviceModuleName, PID) (JNIEnv *env, jclass cls, jint index, jstring name)
{
    const char * buffer = env->GetStringUTFChars(name, 0);
    jint result = SetDeviceModuleName(index, (char *) buffer);
    env->ReleaseStringUTFChars(name, buffer);
    return result > -1;
}

JNI(jstring, nativeGetDeviceModuleName, PID) (JNIEnv *env, jclass cls, jint index)
{
    char buffer[BUFF_MAX];
    GetDeviceModuleName(index, buffer);
    return env->NewStringUTF(buffer);
}

#ifdef __cplusplus
}
#endif
