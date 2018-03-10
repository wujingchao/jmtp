/*
 * Copyright 2010 Pieter De Rycke
 * 
 * This file is part of JMTP.
 * 
 * JTMP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of 
 * the License, or any later version.
 * 
 * JMTP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU LesserGeneral Public 
 * License along with JMTP. If not, see <http://www.gnu.org/licenses/>.
 */

/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class jmtp_PortableDeviceImplWin32 */

#ifndef _Included_jmtp_PortableDeviceImplWin32
#define _Included_jmtp_PortableDeviceImplWin32
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     jmtp_PortableDeviceImplWin32
 * Method:    getDeviceFriendlyName
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_jmtp_PortableDeviceImplWin32_getDeviceFriendlyName
  (JNIEnv *, jobject, jstring);

/*
 * Class:     jmtp_PortableDeviceImplWin32
 * Method:    getDeviceManufacturer
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_jmtp_PortableDeviceImplWin32_getDeviceManufacturer
  (JNIEnv *, jobject, jstring);

/*
 * Class:     jmtp_PortableDeviceImplWin32
 * Method:    getDeviceDescription
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_jmtp_PortableDeviceImplWin32_getDeviceDescription
  (JNIEnv *, jobject, jstring);

/*
 * Class:     jmtp_PortableDeviceImplWin32
 * Method:    openImpl
 * Signature: (Ljmtp/PortableDeviceValuesImplWin32;)V
 */
JNIEXPORT void JNICALL Java_jmtp_PortableDeviceImplWin32_openImpl
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jmtp_PortableDeviceImplWin32
 * Method:    closeImpl
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jmtp_PortableDeviceImplWin32_closeImpl
  (JNIEnv *, jobject);

/*
 * Class:     jmtp_PortableDeviceImplWin32
 * Method:    getDeviceContent
 * Signature: ()Ljmtp/PortableDeviceContentImplWin32;
 */
JNIEXPORT jobject JNICALL Java_jmtp_PortableDeviceImplWin32_getDeviceContent
  (JNIEnv *, jobject);

/*
 * Class:     jmtp_PortableDeviceImplWin32
 * Method:    sendCommand
 * Signature: (Ljmtp/PortableDeviceValuesImplWin32;)Ljmtp/PortableDeviceValuesImplWin32;
 */
JNIEXPORT jobject JNICALL Java_jmtp_PortableDeviceImplWin32_sendCommand
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jmtp_PortableDeviceImplWin32
 * Method:    registerForEventNotification
 * Signature: (Ljmtp/PortableDeviceEventCallbackImplWin32;)V
 */
JNIEXPORT void JNICALL Java_jmtp_PortableDeviceImplWin32_registerForEventNotification
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jmtp_PortableDeviceImplWin32
 * Method:    unregisterForEventNotification
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jmtp_PortableDeviceImplWin32_unregisterForEventNotification
  (JNIEnv *, jobject);

/*
 * Class:     jmtp_PortableDeviceImplWin32
 * Method:    nativeInitEventCookie
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_jmtp_PortableDeviceImplWin32_nativeInitEventCookie
  (JNIEnv *, jobject);


#ifdef __cplusplus
}
#endif
#endif
