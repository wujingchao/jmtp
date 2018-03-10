// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY OF
// ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO
// THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
// PARTICULAR PURPOSE.
//
// Copyright (c) Microsoft Corporation. All rights reserved.
#include <PortableDeviceApi.h>

#include "jmtp.h"
#include "PortableDeviceEventImplWin32.h"


void RegisterForEventNotifications(
    _In_    IPortableDevice*    device,
    _Inout_ PWSTR*              eventCookie,
	FP_OnEvent fp_OnEvent)
{
    HRESULT hr = S_OK;
    PWSTR tempEventCookie = nullptr;
    CComPtr<CPortableDeviceEventsCallback> callback;

    // Check to see if we already have an event registration cookie.  If so,
    // then avoid registering again.
    // NOTE: An application can register for events as many times as they want.
    //       This sample only keeps a single registration cookie around for
    //       simplicity.
    if (*eventCookie != nullptr)
    {
		std::cerr << "there === > *eventCookie != nullptr" << std::endl;
        wprintf(L"This application has already registered to receive device events.\n");
        return;
    }

    *eventCookie = nullptr;

    // Create an instance of the callback object.  This will be called when events
    // are received.
    callback = new (std::nothrow) CPortableDeviceEventsCallback(fp_OnEvent);
    if (callback == nullptr)
    {
        hr = E_OUTOFMEMORY;
        wprintf(L"Failed to allocate memory for IPortableDeviceEventsCallback object, hr = 0x%lx\n", hr);
    }

    // Call Advise to register the callback and receive events.
    if (hr == S_OK)
    {
        hr = device->Advise(0, callback, nullptr, &tempEventCookie);
        if (FAILED(hr))
        {
            wprintf(L"! Failed to register for device events, hr = 0x%lx\n", hr);
        }
    }

    // Save the event registration cookie if event registration was successful.
    if (hr == S_OK)
    {
        *eventCookie = tempEventCookie;
        tempEventCookie = nullptr; // relinquish memory to the caller
        //wprintf(L"This application has registered for device event notifications and was returned the registration cookie '%ws'", *eventCookie);
    }
    else
    {
        // Free the event registration cookie because some error occurred
        CoTaskMemFree(tempEventCookie);
        tempEventCookie = nullptr;
    }
}

void UnregisterForEventNotifications(
    _In_opt_ IPortableDevice*   device,
    _In_opt_ PWSTR             eventCookie)
{
    if (device == nullptr || eventCookie == nullptr)
    {
        return;
    }

    HRESULT hr = device->Unadvise(eventCookie);
    if (FAILED(hr))
    {
        wprintf(L"! Failed to unregister for device events using registration cookie '%ws', hr = 0x%lx\n", eventCookie, hr);
    }
    else
    {
        wprintf(L"This application used the registration cookie '%ws' to unregister from receiving device event notifications", eventCookie);
    }
}

