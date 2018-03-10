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

#include <objbase.h>
#include <PortableDeviceApi.h>

#include "jmtp.h"
#include "jmtp_PortableDeviceImplWin32.h"
#include "PortableDeviceEventImplWin32.h"

static inline IPortableDevice* GetPortableDevice(JNIEnv* env, jobject obj)
{
	return (IPortableDevice*)GetComReferencePointer(env, obj, "pDevice");
}

static inline IPortableDeviceManager* GetPortableDeviceManager(JNIEnv* env, jobject obj)
{
	return (IPortableDeviceManager*)GetComReferencePointer(env, obj, "pDeviceManager");
}

static inline PWSTR* GetEventCookie(JNIEnv * env, jobject obj)
{
	jclass clazz;
	clazz = env->GetObjectClass(obj);
	jmethodID jmethodId;
	jmethodId = env->GetMethodID(clazz, "getNativeEventCookie", "()J");
	jlong jEventCookie = env->CallLongMethod(obj, jmethodId);
	return (PWSTR*)jEventCookie;
}

JNIEXPORT jstring JNICALL Java_jmtp_PortableDeviceImplWin32_getDeviceFriendlyName
	(JNIEnv* env, jobject obj, jstring deviceID)
{
	IPortableDeviceManager* pDeviceManager;
	LPWSTR wszDeviceID;
	LPWSTR wszDeviceFriendlyName;
	DWORD length;
	jstring friendlyName;

	pDeviceManager = GetPortableDeviceManager(env, obj);
	wszDeviceID = (WCHAR*)env->GetStringChars(deviceID, NULL);

	pDeviceManager->GetDeviceFriendlyName(wszDeviceID, NULL, &length);
	wszDeviceFriendlyName = new WCHAR[length + 1];
	pDeviceManager->GetDeviceFriendlyName(wszDeviceID, wszDeviceFriendlyName, &length);	
	friendlyName = env->NewString((jchar*)wszDeviceFriendlyName, wcslen(wszDeviceFriendlyName));

	env->ReleaseStringChars(deviceID, (jchar*)wszDeviceID);
	delete wszDeviceFriendlyName;

	return friendlyName;
}

JNIEXPORT jstring JNICALL Java_jmtp_PortableDeviceImplWin32_getDeviceManufacturer
	(JNIEnv* env, jobject obj, jstring deviceID)
{
	IPortableDeviceManager* pDeviceManager;
	LPWSTR wszDeviceID;
	LPWSTR wszDeviceManufacturer;
	DWORD length;
	jstring manufacturer;

	pDeviceManager = GetPortableDeviceManager(env, obj);
	wszDeviceID = (WCHAR*)env->GetStringChars(deviceID, NULL);

	pDeviceManager->GetDeviceManufacturer(wszDeviceID, NULL, &length);
	wszDeviceManufacturer = new WCHAR[length + 1];
	pDeviceManager->GetDeviceManufacturer(wszDeviceID, wszDeviceManufacturer, &length);	
	manufacturer = env->NewString((jchar*)wszDeviceManufacturer, wcslen(wszDeviceManufacturer));

	env->ReleaseStringChars(deviceID, (jchar*)wszDeviceID);
	delete wszDeviceManufacturer;

	return manufacturer;
}

JNIEXPORT jstring JNICALL Java_jmtp_PortableDeviceImplWin32_getDeviceDescription
	(JNIEnv* env, jobject obj, jstring deviceID)
{
	IPortableDeviceManager* pDeviceManager;
	LPWSTR wszDeviceID;
	LPWSTR wszDeviceDescription;
	DWORD length;
	jstring description;

	pDeviceManager = GetPortableDeviceManager(env, obj);
	wszDeviceID = (WCHAR*)env->GetStringChars(deviceID, NULL);

	pDeviceManager->GetDeviceDescription(wszDeviceID, NULL, &length);
	wszDeviceDescription = new WCHAR[length + 1];
	pDeviceManager->GetDeviceDescription(wszDeviceID, wszDeviceDescription, &length);	
	description = env->NewString((jchar*)wszDeviceDescription, wcslen(wszDeviceDescription));

	env->ReleaseStringChars(deviceID, (jchar*)wszDeviceID);
	delete wszDeviceDescription;

	return description;
}

JNIEXPORT void JNICALL Java_jmtp_PortableDeviceImplWin32_openImpl
	(JNIEnv* env, jobject obj, jobject values)
{
	HRESULT hr;
	IPortableDevice* pDevice;
	jobject reference;
	jmethodID mid;
	IPortableDeviceValues* pClientInfo;
	LPWSTR wszDeviceID;
	jfieldID fid;
	jstring jsDeviceID;

	pDevice = GetPortableDevice(env, obj);

	//clientinfo value object opvragen
	mid = env->GetMethodID(env->GetObjectClass(values), "getReference", 
		"()Lbe/derycke/pieter/com/COMReference;");
	reference = env->CallObjectMethod(values, mid);
	mid = env->GetMethodID(env->FindClass("be/derycke/pieter/com/COMReference"), "getMemoryAddress", "()J");
	pClientInfo = (IPortableDeviceValues*)env->CallLongMethod(reference, mid);

	//deviceID opvragen
	fid = env->GetFieldID(env->GetObjectClass(obj), "deviceID", "Ljava/lang/String;");
	jsDeviceID = (jstring)env->GetObjectField(obj, fid);
	wszDeviceID = (WCHAR*)env->GetStringChars(jsDeviceID, NULL);

	hr = pDevice->Open(wszDeviceID, pClientInfo);
	env->ReleaseStringChars(jsDeviceID, (jchar*)wszDeviceID);

	if(FAILED(hr))
	{
		ThrowCOMException(env, L"Couldn't open the device", hr);
		return;
	}
}

JNIEXPORT void JNICALL Java_jmtp_PortableDeviceImplWin32_closeImpl
	(JNIEnv* env, jobject obj)
{
	jclass clazz;
	jmethodID jmethodId;
	clazz = env->GetObjectClass(obj);
	jmethodId = env->GetMethodID(clazz, "getNativeEventCookie", "()J");
	jlong jEventCookie = env->CallLongMethod(obj, jmethodId);
	PWSTR* tpEventCookie = (PWSTR*)jEventCookie;
	delete tpEventCookie;
}

JNIEXPORT jobject JNICALL Java_jmtp_PortableDeviceImplWin32_getDeviceContent
	(JNIEnv* env, jobject obj)
{
	HRESULT hr;
	IPortableDevice* pDevice;
	IPortableDeviceContent* pContent;
	jclass cls;
	jmethodID mid;
	jobject reference;

	pDevice = GetPortableDevice(env, obj);
	hr = pDevice->Content(&pContent);
	if(FAILED(hr))
	{
		ThrowCOMException(env, L"Couldn't retrieve the device content", hr);
		return NULL;
	}

	//smart reference object aanmaken
	cls = env->FindClass("be/derycke/pieter/com/COMReference");
	mid = env->GetMethodID(cls, "<init>", "(J)V");
	reference = env->NewObject(cls, mid, pContent);
	
	cls = env->FindClass("jmtp/PortableDeviceContentImplWin32");
	mid = env->GetMethodID(cls, "<init>", "(Lbe/derycke/pieter/com/COMReference;)V");
	return env->NewObject(cls, mid, reference);
}

JNIEXPORT jobject JNICALL Java_jmtp_PortableDeviceImplWin32_sendCommand
	(JNIEnv* env, jobject obj, jobject values)
{
	HRESULT hr;
	IPortableDevice* pDevice;
	IPortableDeviceValues* pValuesIn;
	IPortableDeviceValues* pValuesOut;
	jclass cls;
	jmethodID mid;
	jobject reference;

	pDevice = GetPortableDevice(env, obj);

	//clientinfo value object opvragen
	mid = env->GetMethodID(env->GetObjectClass(values), "getReference", 
		"()Lbe/derycke/pieter/com/COMReference;");
	reference = env->CallObjectMethod(values, mid);
	mid = env->GetMethodID(env->FindClass("be/derycke/pieter/com/COMReference"), "getMemoryAddress", "()J");
	pValuesIn = (IPortableDeviceValues*)env->CallLongMethod(reference, mid);

	hr = pDevice->SendCommand(0, pValuesIn, &pValuesOut);

	if(FAILED(hr))
	{
		ThrowCOMException(env, L"The custom command failed.", hr);
		return NULL;
	}

	//smart reference object aanmaken
	cls = env->FindClass("be/derycke/pieter/com/COMReference");
	mid = env->GetMethodID(cls, "<init>", "(J)V");
	reference = env->NewObject(cls, mid, pValuesOut);
	
	cls = env->FindClass("jmtp/PortableDeviceValuesImplWin32");
	mid = env->GetMethodID(cls, "<init>", "(Lbe/derycke/pieter/com/COMReference;)V");
	return env->NewObject(cls, mid, reference);
}


static jobject mtpEventCallback;

static JavaVM* vm;

void onMTPEvent(IPortableDeviceValues* eventParameters) {
	JNIEnv *env;
	if (vm != nullptr) {
		jint result = vm->AttachCurrentThread((void**)&env, 0);
		if (result != JNI_OK) {
			return;
		}
		jclass clazz;
		jmethodID jmethod;
		jobject obj;

		//TODO cache it!!!
		//COMReference...
		clazz = env->FindClass("be/derycke/pieter/com/COMReference");
		jmethod = env->GetMethodID(clazz, "<init>", "(J)V");
		obj = env->NewObject(clazz, jmethod, eventParameters);

		//PortableDeviceValuesImplWin32...
		clazz = env->FindClass("jmtp/PortableDeviceValuesImplWin32");
		jmethod = env->GetMethodID(clazz, "<init>", "(Lbe/derycke/pieter/com/COMReference;)V");
		obj = env->NewObject(clazz, jmethod, obj);
		
		//PortableDeviceEventCallbackImplWin32...
		clazz = env->FindClass("jmtp/PortableDeviceEventCallbackImplWin32");
		jmethod = env->GetMethodID(clazz, "onEvent", "(Ljmtp/PortableDeviceValuesImplWin32;)V");
		
		env->CallVoidMethod(mtpEventCallback, jmethod, obj);
		vm->DetachCurrentThread();
	}
}


JNIEXPORT void JNICALL Java_jmtp_PortableDeviceImplWin32_registerForEventNotification
  (JNIEnv * env, jobject obj, jobject callback)
{
	env->GetJavaVM(&vm);
	IPortableDevice* pDevice;
	mtpEventCallback = env->NewGlobalRef(callback);	
	pDevice = GetPortableDevice(env, obj);
	RegisterForEventNotifications(pDevice, GetEventCookie(env, obj), onMTPEvent);
}

JNIEXPORT void JNICALL Java_jmtp_PortableDeviceImplWin32_unregisterForEventNotification
  (JNIEnv * env, jobject obj)
{
	IPortableDevice* pDevice;

	pDevice = GetPortableDevice(env, obj);
	PWSTR* pEventCookie = GetEventCookie(env, obj);
    UnregisterForEventNotifications(pDevice, *pEventCookie);
    CoTaskMemFree(pEventCookie);
	env->DeleteGlobalRef(mtpEventCallback);
}

JNIEXPORT jlong JNICALL Java_jmtp_PortableDeviceImplWin32_nativeInitEventCookie
	(JNIEnv *, jobject) {
	PWSTR* p = new PWSTR;
	*p = nullptr;
	return (jlong)p;
}