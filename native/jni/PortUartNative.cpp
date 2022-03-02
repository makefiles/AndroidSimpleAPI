/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

#define LOG_TAG "com_amolla_jni_PortUartNative"
#define PID      com_amolla_jni_PortUartNative

#include <jni.h>
#include <stdbool.h>
#include <stdlib.h>
#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <string.h>
#include <cutils/log.h>
#include <errno.h>

#ifdef __cplusplus
extern "C" {
#endif

#define SUPPORT_BLOCK_IO

// Beautifying the ugly Java-C++ bridge (JNI) with these macros.
#define MAKE_JNI_FUNCTION(r, n, p) extern "C" JNIEXPORT r JNICALL Java_ ## p ## _ ## n
#define JNI(r, n, p) MAKE_JNI_FUNCTION(r, n, p)
#define UNUSED(x) (void)(x)

#ifdef SUPPORT_BLOCK_IO
static int  gBlockIoExit = 0;
static int  gBlockIoFd[] = {-1, -1};
#endif
static long gRequests[] = {-1, -1};
static int  checkRequests()
{
    const int len = sizeof(gRequests)/sizeof(gRequests[0]);
    for (int i = 0; i < len; i++) {
        if (gRequests[i] < 0) return -EBADR;
    }
    return 0;
}

static int getUartBaudrate (jint baudrate)
{
	switch (baudrate) {
		case 0: return B0;
		case 50: return B50;
		case 75: return B75;
		case 110: return B110;
		case 134: return B134;
		case 150: return B150;
		case 200: return B200;
		case 300: return B300;
		case 600: return B600;
		case 1200: return B1200;
		case 1800: return B1800;
		case 2400: return B2400;
		case 4800: return B4800;
		case 9600: return B9600;
		case 19200: return B19200;
		case 38400: return B38400;
		case 57600: return B57600;
		case 115200: return B115200;
		case 230400: return B230400;
		case 460800: return B460800;
		case 500000: return B500000;
		case 576000: return B576000;
		case 921600: return B921600;
		case 1000000: return B1000000;
		case 1152000: return B1152000;
		case 1500000: return B1500000;
		case 2000000: return B2000000;
		case 2500000: return B2500000;
		case 3000000: return B3000000;
		case 3500000: return B3500000;
		case 4000000: return B4000000;
		default: return -1;
	}
}

static int setUartSuspend (jint fd, jboolean sleep)
{
	if (fd < 0) return -ENODEV;
    if (checkRequests() < 0) return -EBADR;
	if (ioctl(fd, (sleep ? gRequests[1] : gRequests[0]), NULL) < 0) {
		ALOGE("IOCTL_UART_CLOCK: %s (%d)", strerror(-errno), -errno);
        return -errno;
    }
    return 0;
}

jint JNI_OnLoad (JavaVM* jvm, void* reserved)
{
    JNIEnv *env = NULL;
    if (jvm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    return JNI_VERSION_1_6;
}

void JNI_OnUnload (JavaVM* jvm, void *reserved)
{
    JNIEnv *env = NULL;
    if (jvm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return;
    }
}

JNI(jboolean, nativeRequests, PID) (JNIEnv *env, jclass cls, jlongArray requests)
{
    const int len = sizeof(gRequests)/sizeof(gRequests[0]);
    if (env->GetArrayLength(requests) < len) return JNI_FALSE;
    jlong *ptrArray = env->GetLongArrayElements(requests, 0);
    for (int i = 0; i < len; i++) {
        if (ptrArray[i] < 0) {
            env->ReleaseLongArrayElements(requests, ptrArray, 0);
            return JNI_FALSE;
        }
        gRequests[i] = ptrArray[i];
    }
    env->ReleaseLongArrayElements(requests, ptrArray, 0);
    return JNI_TRUE;
}

JNI(jint, nativeOpen, PID) (JNIEnv *env, jclass cls, jstring path, jint baudrate, jint flags, jboolean hwflow)
{
    /* Get baudrate */
    int speed = getUartBaudrate(baudrate);
    if (speed < 0) return -EINVAL;
    /* Open device */
    const char * cstr = env->GetStringUTFChars(path, 0);
    int fd = open(cstr, O_RDWR | O_NOCTTY | flags);
    env->ReleaseStringUTFChars(path, cstr);
	if (fd < 0) return -ENODEV;
    /* Configure device */
	struct termios cfg;
    if (tcgetattr(fd, &cfg)) {
		ALOGE("Failed to get the UART attribute: %s (%d)", strerror(-errno), -errno);
        close(fd);
        return -errno;
    }
	cfmakeraw(&cfg);
	cfsetispeed(&cfg, speed);
	cfsetospeed(&cfg, speed);
	if (hwflow) {
		cfg.c_cflag |= CRTSCTS;
	} else {
		cfg.c_cflag &= ~CRTSCTS;
	}
	if (tcsetattr(fd, TCSANOW, &cfg)) {
		ALOGE("Failed to set the UART attribute: %s (%d)", strerror(-errno), -errno);
        close(fd);
        return -errno;
	}
#ifdef SUPPORT_BLOCK_IO
    /* Initialize for Block IO */
    if (pipe(gBlockIoFd) < 0) {
		ALOGE("Failed to create pipes: %s (%d)", strerror(-errno), -errno);
        close(fd);
        return -errno;
    }
    gBlockIoExit = 0;
#endif
    setUartSuspend(fd, JNI_FALSE);
    /* Return file description */
    return fd;
}

JNI(jint, nativeClose, PID) (JNIEnv *env, jclass cls, jint fd)
{
	if (fd < 0) return -ENODEV;
#ifdef SUPPORT_BLOCK_IO
    int cmd = 0;
    int retry = 100;
    write(gBlockIoFd[1], &cmd, 1);
    while (retry-- && !gBlockIoExit) {
        usleep(10 * 1000);
    }
    close(gBlockIoFd[0]);
    close(gBlockIoFd[1]);
    gBlockIoFd[0] = -1;
    gBlockIoFd[1] = -1;
#endif
    setUartSuspend(fd, JNI_TRUE);
    close(fd);
	return 0;
}

JNI(jint, nativeRead, PID) (JNIEnv *env, jclass cls, jint fd, jbyteArray buf, jint len)
{
	if (fd < 0) return -ENODEV;
    if (buf == NULL || len < 1) return -EINVAL;
    char tmp[len];
#ifdef SUPPORT_BLOCK_IO
	int max_fd = 0;
	fd_set fds;
	if (max_fd <= fd) max_fd = fd + 1;
	if (max_fd <= gBlockIoFd[0]) max_fd = gBlockIoFd[0] + 1;
	FD_ZERO(&fds);
	FD_SET(gBlockIoFd[0], &fds);
	FD_SET(fd, &fds);
	int rc = select(max_fd, &fds, NULL, NULL, NULL);
	if (FD_ISSET(fd, &fds)) {
		rc = read(fd, tmp, len);
	} else if (FD_ISSET(gBlockIoFd[0], &fds)) {
		gBlockIoExit = 1;
		return -EAGAIN;
	} else {
		rc = 0;
	}
#else
    int rc = read(fd, tmp, len);
#endif
	if (rc < 0) {
		ALOGE("Failed to read from the UART: %s (%d)", strerror(-errno), -errno);
        return -errno;
	}
    env->SetByteArrayRegion(buf, 0, len, (jbyte *) tmp);
    return rc;
}

JNI(jint, nativeWrite, PID) (JNIEnv *env, jclass cls, jint fd, jbyteArray buf, jint pos, jint len)
{
	if (fd < 0) return -ENODEV;
    if (buf == NULL || len < 1) return -EINVAL;
    char tmp[len];
    env->GetByteArrayRegion(buf, 0, len, (jbyte *) tmp);
    int rc = write(fd, tmp, len - pos);
	if (rc < 0) {
		ALOGE("Failed to write to the UART: %s (%d)", strerror(-errno), -errno);
        return -errno;
	}
    return 0;
}

JNI(jint, nativeSendBreak, PID) (JNIEnv *env, jclass cls, jint fd)
{
	if (fd < 0) return -ENODEV;
    tcsendbreak(fd, 0);
	return 0;
}

JNI(jint, nativeSleep, PID) (JNIEnv *env, jclass cls, jint fd, jboolean enable)
{
    return setUartSuspend(fd, enable);
}

#ifdef __cplusplus
}
#endif
