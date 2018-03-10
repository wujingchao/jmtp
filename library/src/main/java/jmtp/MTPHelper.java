package jmtp;

import java.math.BigInteger;
import java.util.Arrays;

import be.derycke.pieter.com.COMException;
import be.derycke.pieter.com.Guid;

import static jmtp.Win32WPDDefines.WPD_COMMAND_MTP_EXT_END_DATA_TRANSFER;
import static jmtp.Win32WPDDefines.WPD_COMMAND_MTP_EXT_EXECUTE_COMMAND_WITHOUT_DATA_PHASE;
import static jmtp.Win32WPDDefines.WPD_COMMAND_MTP_EXT_EXECUTE_COMMAND_WITH_DATA_TO_READ;
import static jmtp.Win32WPDDefines.WPD_COMMAND_MTP_EXT_EXECUTE_COMMAND_WITH_DATA_TO_WRITE;
import static jmtp.Win32WPDDefines.WPD_COMMAND_MTP_EXT_READ_DATA;
import static jmtp.Win32WPDDefines.WPD_COMMAND_MTP_EXT_WRITE_DATA;
import static jmtp.Win32WPDDefines.WPD_EVENT_DEVICE_REMOVED;
import static jmtp.Win32WPDDefines.WPD_EVENT_MTP_VENDOR_EXTENDED_EVENTS;
import static jmtp.Win32WPDDefines.WPD_EVENT_OBJECT_ADDED;
import static jmtp.Win32WPDDefines.WPD_EVENT_PARAMETER_EVENT_ID;
import static jmtp.Win32WPDDefines.WPD_OBJECT_ID;
import static jmtp.Win32WPDDefines.WPD_PROPERTY_COMMON_COMMAND_CATEGORY;
import static jmtp.Win32WPDDefines.WPD_PROPERTY_COMMON_COMMAND_ID;
import static jmtp.Win32WPDDefines.WPD_PROPERTY_MTP_EXT_EVENT_PARAMS;
import static jmtp.Win32WPDDefines.WPD_PROPERTY_MTP_EXT_OPERATION_CODE;
import static jmtp.Win32WPDDefines.WPD_PROPERTY_MTP_EXT_OPERATION_PARAMS;
import static jmtp.Win32WPDDefines.WPD_PROPERTY_MTP_EXT_RESPONSE_CODE;
import static jmtp.Win32WPDDefines.WPD_PROPERTY_MTP_EXT_RESPONSE_PARAMS;
import static jmtp.Win32WPDDefines.WPD_PROPERTY_MTP_EXT_TRANSFER_CONTEXT;
import static jmtp.Win32WPDDefines.WPD_PROPERTY_MTP_EXT_TRANSFER_DATA;
import static jmtp.Win32WPDDefines.WPD_PROPERTY_MTP_EXT_TRANSFER_NUM_BYTES_TO_READ;
import static jmtp.Win32WPDDefines.WPD_PROPERTY_MTP_EXT_TRANSFER_NUM_BYTES_TO_WRITE;
import static jmtp.Win32WPDDefines.WPD_PROPERTY_MTP_EXT_TRANSFER_NUM_BYTES_WRITTEN;
import static jmtp.Win32WPDDefines.WPD_PROPERTY_MTP_EXT_TRANSFER_TOTAL_DATA_SIZE;

public class MTPHelper {

    private static MTPHelper INSTANCE = new MTPHelper();

    private EventListener eventListener;

    public static MTPHelper getInstance() {
        return INSTANCE;
    }

    private PortableDeviceEventCallbackImplWin32 deviceEventCallbackImplWin32 = new PortableDeviceEventCallbackImplWin32() {
        @Override
        public void onEvent(PortableDeviceValuesImplWin32 eventParameters) {
            if (eventListener == null) {
                return;
            }
            System.out.println("PortableDeviceEventCallbackImplWin32#onEvent");
            try {
                Guid eventId = eventParameters.getGuidValue(WPD_EVENT_PARAMETER_EVENT_ID);
                if (checkGUIIDEvent(eventId, WPD_EVENT_MTP_VENDOR_EXTENDED_EVENTS)) {//Vendor Extend Event
                    MTPDataset eventResult = new MTPDataset();
                    eventResult.code = eventId.getData1() >> 16;

                    PortableDevicePropVariantCollectionImplWin32 portableDevicePropVariantCollection = eventParameters.getPortableDeviceValuesCollectionValue(WPD_PROPERTY_MTP_EXT_EVENT_PARAMS);
                    eventResult.params = new int[(int)portableDevicePropVariantCollection.count()];
                    for (int i = 0; i < portableDevicePropVariantCollection.count(); i++) {
                        eventResult.params[i] = portableDevicePropVariantCollection.getAt(i).getVt();
                    }
                    eventListener.onEvent(eventResult, null);
                } else if (checkGUIIDEvent(eventId, WPD_EVENT_OBJECT_ADDED)) {
                    MTPDataset eventResult = new MTPDataset();
                    eventResult.code = 0x4002;
                    String objectId = eventParameters.getStringValue(WPD_OBJECT_ID);
                    eventResult.params = new int[1];
                    eventResult.params[0] = Integer.parseInt(objectId.substring(2), 16);
                    eventListener.onEvent(eventResult, null);
                } else if (checkGUIIDEvent(eventId, WPD_EVENT_DEVICE_REMOVED)) {
                    System.out.println("WPD_EVENT_DEVICE_REMOVED");
                    eventListener.onEvent(null, new EventDeviceRemoveException("WPD_EVENT_DEVICE_REMOVED"));
                }

            } catch (COMException e) {
                eventListener.onEvent(null, e);
            }
        }
    };

    private boolean checkGUIIDEvent(Guid eventId, Guid GUID) {
        return (eventId.getData2() == GUID.getData2()) &&
                (eventId.getData3() == GUID.getData3()) &&
                Arrays.equals(eventId.getData4(), GUID.getData4());
    }


    public MTPDataset sendCMDWithOutData(PortableDevice portableDevice, MTPDataset dataset) throws COMException {
        checkWin32Impl(portableDevice);
        PortableDeviceImplWin32 portableDeviceImplWin32 = (PortableDeviceImplWin32) portableDevice;

        PortableDeviceValuesImplWin32 spParameters = new PortableDeviceValuesImplWin32();
        spParameters.setGuidValue(WPD_PROPERTY_COMMON_COMMAND_CATEGORY, WPD_COMMAND_MTP_EXT_EXECUTE_COMMAND_WITH_DATA_TO_WRITE.getFmtid());
        spParameters.setUnsignedIntegerValue(WPD_PROPERTY_COMMON_COMMAND_ID, WPD_COMMAND_MTP_EXT_EXECUTE_COMMAND_WITH_DATA_TO_WRITE.getPid());
        spParameters.setUnsignedIntegerValue(WPD_PROPERTY_MTP_EXT_OPERATION_CODE, dataset.code);

        PortableDevicePropVariantCollectionImplWin32 sMtpParams = new PortableDevicePropVariantCollectionImplWin32();

        for (int param : dataset.params) {
            PropVariant pvParam = new PropVariant(param);
            sMtpParams.add(pvParam);
        }

        spParameters.setPortableDeviceValuesCollectionValue(WPD_PROPERTY_MTP_EXT_OPERATION_PARAMS, sMtpParams);


        spParameters.setUnsignedLargeIntegerValue(WPD_PROPERTY_MTP_EXT_TRANSFER_TOTAL_DATA_SIZE, new BigInteger(String.valueOf(dataset.data.length), 10));

        PortableDeviceValuesImplWin32 spResult = portableDeviceImplWin32.sendCommand(spParameters);

        String pwszContext = spResult.getStringValue(WPD_PROPERTY_MTP_EXT_TRANSFER_CONTEXT);
        spParameters.clear();

        spParameters.setGuidValue(WPD_PROPERTY_COMMON_COMMAND_CATEGORY, WPD_COMMAND_MTP_EXT_WRITE_DATA.getFmtid());
        spParameters.setUnsignedIntegerValue(WPD_PROPERTY_COMMON_COMMAND_ID, WPD_COMMAND_MTP_EXT_WRITE_DATA.getPid());
        spParameters.setStringValue(WPD_PROPERTY_MTP_EXT_TRANSFER_CONTEXT, pwszContext);

        spParameters.setUnsignedIntegerValue(WPD_PROPERTY_MTP_EXT_TRANSFER_NUM_BYTES_TO_WRITE, dataset.data.length);

        spParameters.setBufferValue(WPD_PROPERTY_MTP_EXT_TRANSFER_DATA, dataset.data);
        spResult = portableDeviceImplWin32.sendCommand(spParameters);

        long actualTranLen = spResult.getUnsignedIntegerValue(WPD_PROPERTY_MTP_EXT_TRANSFER_NUM_BYTES_WRITTEN);

        //To read response
        spParameters.clear();
        spParameters.setGuidValue(WPD_PROPERTY_COMMON_COMMAND_CATEGORY, WPD_COMMAND_MTP_EXT_END_DATA_TRANSFER.getFmtid());
        spParameters.setUnsignedIntegerValue(WPD_PROPERTY_COMMON_COMMAND_ID, WPD_COMMAND_MTP_EXT_END_DATA_TRANSFER.getPid());
        spParameters.setStringValue(WPD_PROPERTY_MTP_EXT_TRANSFER_CONTEXT, pwszContext);
        spResult = portableDeviceImplWin32.sendCommand(spParameters);

        long responseCode = spResult.getUnsignedIntegerValue(WPD_PROPERTY_MTP_EXT_RESPONSE_CODE);
        MTPDataset resultDataset = new MTPDataset();
        resultDataset.code = responseCode;
        PortableDevicePropVariantCollectionImplWin32 values = spResult.getPortableDeviceValuesCollectionValue(WPD_PROPERTY_MTP_EXT_RESPONSE_PARAMS);
        int[] responseParams = new int[(int) values.count()];
        for (int i = 0; i < values.count(); i++) {
            responseParams[i] = (int) values.getAt(i).getValue();
        }
        resultDataset.params =  responseParams;
        return resultDataset;
    }

    public MTPDataset sendCMDWithInData(PortableDevice portableDevice, MTPDataset dataset) throws COMException {
        checkWin32Impl(portableDevice);
        PortableDeviceImplWin32 portableDeviceImplWin32 = (PortableDeviceImplWin32) portableDevice;
        MTPDataset resultDataset = new MTPDataset();
        long RESPONSECODE_OK = 0x2001;
        PortableDeviceValuesImplWin32 spParameters = new PortableDeviceValuesImplWin32();
        spParameters.setGuidValue(WPD_PROPERTY_COMMON_COMMAND_CATEGORY, WPD_COMMAND_MTP_EXT_EXECUTE_COMMAND_WITH_DATA_TO_READ.getFmtid());
        spParameters.setUnsignedIntegerValue(WPD_PROPERTY_COMMON_COMMAND_ID, WPD_COMMAND_MTP_EXT_EXECUTE_COMMAND_WITH_DATA_TO_READ.getPid());
        spParameters.setUnsignedIntegerValue(WPD_PROPERTY_MTP_EXT_OPERATION_CODE, dataset.code);

        PortableDevicePropVariantCollectionImplWin32 sMtpParams = new PortableDevicePropVariantCollectionImplWin32();

        for (int params : dataset.params) {
            PropVariant pvParam = new PropVariant(params);
            sMtpParams.add(pvParam);
        }
        spParameters.setPortableDeviceValuesCollectionValue(WPD_PROPERTY_MTP_EXT_OPERATION_PARAMS, sMtpParams);

        PortableDeviceValuesImplWin32 spResult = portableDeviceImplWin32.sendCommand(spParameters);

        String pwszContext = spResult.getStringValue(WPD_PROPERTY_MTP_EXT_TRANSFER_CONTEXT);

        long cbReportedDataSize = spResult.getUnsignedIntegerValue(WPD_PROPERTY_MTP_EXT_TRANSFER_TOTAL_DATA_SIZE);

        if (cbReportedDataSize > 0) {
            //To read data
            spParameters.clear();
            spParameters.setGuidValue(WPD_PROPERTY_COMMON_COMMAND_CATEGORY, WPD_COMMAND_MTP_EXT_READ_DATA.getFmtid());
            spParameters.setUnsignedIntegerValue(WPD_PROPERTY_COMMON_COMMAND_ID, WPD_COMMAND_MTP_EXT_READ_DATA.getPid());
            spParameters.setStringValue(WPD_PROPERTY_MTP_EXT_TRANSFER_CONTEXT, pwszContext);
            byte[] pbBufferIn = new byte[(int) cbReportedDataSize];
            spParameters.setBufferValue(WPD_PROPERTY_MTP_EXT_TRANSFER_DATA, pbBufferIn);
            spParameters.setUnsignedIntegerValue(WPD_PROPERTY_MTP_EXT_TRANSFER_NUM_BYTES_TO_READ, cbReportedDataSize);

            spResult = portableDeviceImplWin32.sendCommand(spParameters);
            resultDataset.data = spResult.getBufferValue(WPD_PROPERTY_MTP_EXT_TRANSFER_DATA);
            //To read response
            spParameters.clear();
            spParameters.setGuidValue(WPD_PROPERTY_COMMON_COMMAND_CATEGORY, WPD_COMMAND_MTP_EXT_END_DATA_TRANSFER.getFmtid());
            spParameters.setUnsignedIntegerValue(WPD_PROPERTY_COMMON_COMMAND_ID, WPD_COMMAND_MTP_EXT_END_DATA_TRANSFER.getPid());
            spParameters.setStringValue(WPD_PROPERTY_MTP_EXT_TRANSFER_CONTEXT, pwszContext);
            spResult = portableDeviceImplWin32.sendCommand(spParameters);
        }
        resultDataset.code = spResult.getUnsignedIntegerValue(WPD_PROPERTY_MTP_EXT_RESPONSE_CODE);
//        if (responseCode == RESPONSECODE_OK) {
//            Printer.println("RESPONSE CODE OK!!!");
//        } else if (responseCode == 0x201D) {
//            Printer.println("Invalid parameter");
//        } else{
//            Printer.println("RESPONSECODE_Fail, code = " + responseCode);
//        }
        PortableDevicePropVariantCollectionImplWin32 values = spResult.getPortableDeviceValuesCollectionValue(WPD_PROPERTY_MTP_EXT_RESPONSE_PARAMS);
        int[] responseParams = new int[(int) values.count()];
        for (int i = 0; i < values.count(); i++) {
            responseParams[i] = (int) values.getAt(i).getValue();
        }
        resultDataset.params =  responseParams;
        return resultDataset;
    }

    public MTPDataset sendCMDWithoutDataPhase(PortableDevice portableDevice, MTPDataset dataset) throws COMException {
        checkWin32Impl(portableDevice);
        PortableDeviceImplWin32 portableDeviceImplWin32 = (PortableDeviceImplWin32) portableDevice;
        PortableDeviceValuesImplWin32 spParameters = new PortableDeviceValuesImplWin32();
        spParameters.setGuidValue(WPD_PROPERTY_COMMON_COMMAND_CATEGORY, WPD_COMMAND_MTP_EXT_EXECUTE_COMMAND_WITHOUT_DATA_PHASE.getFmtid());
        spParameters.setUnsignedIntegerValue(WPD_PROPERTY_COMMON_COMMAND_ID, WPD_COMMAND_MTP_EXT_EXECUTE_COMMAND_WITHOUT_DATA_PHASE.getPid());
        spParameters.setUnsignedIntegerValue(WPD_PROPERTY_MTP_EXT_OPERATION_CODE, dataset.code);

        PortableDevicePropVariantCollectionImplWin32 sMtpParams = new PortableDevicePropVariantCollectionImplWin32();

        for (int params : dataset.params) {
            PropVariant pvParam = new PropVariant(params);
            sMtpParams.add(pvParam);
        }
        spParameters.setPortableDeviceValuesCollectionValue(WPD_PROPERTY_MTP_EXT_OPERATION_PARAMS, sMtpParams);

        PortableDeviceValuesImplWin32 spResult = portableDeviceImplWin32.sendCommand(spParameters);

        long responseCode = spResult.getUnsignedIntegerValue(WPD_PROPERTY_MTP_EXT_RESPONSE_CODE);
//        if (responseCode == RESPONSECODE_OK) {
//            Printer.println("RESPONSE CODE OK!!!");
//        } else if (responseCode == 0x201D) {
//            Printer.println("Invalid parameter");
//        } else {
//            Printer.println("RESPONSECODE_Fail, code = " + responseCode);
//        }
        PortableDevicePropVariantCollectionImplWin32 values = spResult.getPortableDeviceValuesCollectionValue(WPD_PROPERTY_MTP_EXT_RESPONSE_PARAMS);
        int[] responseParams = new int[(int) values.count()];
        for (int i = 0; i < values.count(); i++) {
            responseParams[i] = (int) values.getAt(i).getValue();
        }
        return new MTPDataset(responseCode, responseParams);
    }

    public void registerEvent(PortableDevice portableDevice, EventListener listener) {
        checkWin32Impl(portableDevice);
        PortableDeviceImplWin32 portableDeviceImplWin32 = (PortableDeviceImplWin32) portableDevice;
        if (this.eventListener == null) {
            portableDeviceImplWin32.registerForEventNotification(deviceEventCallbackImplWin32);
        }
        this.eventListener = listener;
    }

    public void unregisterEvent(PortableDevice portableDevice) {
        checkWin32Impl(portableDevice);
        PortableDeviceImplWin32 portableDeviceImplWin32 = (PortableDeviceImplWin32) portableDevice;
        portableDeviceImplWin32.unregisterForEventNotification();
        this.eventListener = null;
    }

    private void checkWin32Impl(PortableDevice portableDevice) {
        if (!PortableDeviceImplWin32.class.isInstance(portableDevice)) {
            throw new IllegalStateException("Just windows now!!!");
        }
    }

    public interface EventListener {
        void onEvent(MTPDataset event, Exception e);
    }

    public static class EventDeviceRemoveException extends Exception{

        EventDeviceRemoveException(String s) {
            super(s);
        }
    }
}
