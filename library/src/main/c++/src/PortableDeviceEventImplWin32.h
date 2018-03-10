#pragma once

#include <PortableDeviceApi.h>

#include "jmtp.h"

// IPortableDeviceEventCallback implementation for use with
// device events.
class CPortableDeviceEventsCallback : public IPortableDeviceEventCallback
{
public:
    CPortableDeviceEventsCallback(FP_OnEvent fp_OnEvent) : m_ref(0),onEvent(fp_OnEvent)
    {
    }

    ~CPortableDeviceEventsCallback()
    {
		onEvent = nullptr;
    }

	IFACEMETHODIMP QueryInterface(
        _In_         REFIID  riid,
        /**_COM_Outptr_**/ void**  ppv)
    {
        static const QITAB qitab[] =
        {
            QITABENT(CPortableDeviceEventsCallback, IPortableDeviceEventCallback),
            { },
        };

        return QISearch(this, qitab, riid, ppv);
    }

    IFACEMETHODIMP_(ULONG) AddRef()
    {
        return InterlockedIncrement(&m_ref);
    }

    IFACEMETHODIMP_(ULONG) Release()
    {
        long ref = InterlockedDecrement(&m_ref);
        if (ref == 0)
        {
            delete this;
        }
        return ref;
    }

    IFACEMETHODIMP OnEvent(
        _In_ IPortableDeviceValues* eventParameters)
    {
        if (eventParameters != nullptr)
        {
			if (onEvent != nullptr) {
				onEvent(eventParameters);
			}
		} else {
			wprintf(L"eventParameters is null");
		}
        return S_OK;
    }

private:
    long m_ref;
	FP_OnEvent onEvent;
};

void RegisterForEventNotifications(
    _In_    IPortableDevice*    device,
    _Inout_ PWSTR*              eventCookie,
	FP_OnEvent fp_OnEvent);


void UnregisterForEventNotifications(
    _In_opt_ IPortableDevice*   device,
    _In_opt_ PWSTR             eventCookie);