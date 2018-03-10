package io.github.wujingchao.demo.jmtp;


import be.derycke.pieter.com.COMException;
import jmtp.MTPDataset;
import jmtp.MTPHelper;
import jmtp.PortableDevice;
import jmtp.PortableDeviceManager;
import okio.Buffer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.fail;

public class MTPTestCase {

    private static PortableDevice device;

    @BeforeClass
    public static void init() {
        PortableDeviceManager portableDeviceManager = new PortableDeviceManager();
        PortableDevice[] devices =portableDeviceManager.getDevices();
        log("connect device count : " + devices.length);
        if (devices.length == 0) {
            fail("No Mtp Device Connected");
        }
        device = devices[0];
        device.open();
        MTPHelper.getInstance().registerEvent(device, new MTPHelper.EventListener() {
            @Override
            public void onEvent(MTPDataset event, Exception e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
            }
        });
    }

    @AfterClass
    public static void uninit() {
        if (device != null) {
            MTPHelper.getInstance().unregisterEvent(device);

            device.close();
        }
    }

    private static void log(Object o) {
        System.out.println(o);
    }

    @Test
    public void testGetDeviceInfo() throws COMException {
        MTPDataset request = new MTPDataset(0x1001);
        MTPDataset response = MTPHelper.getInstance().sendCMDWithInData(device, request);
        log(response);
    }

    @Test
    public void testGetStorageIDs() throws COMException {
        MTPDataset request = new MTPDataset(0x1004);
        MTPDataset response = MTPHelper.getInstance().sendCMDWithInData(device, request);
        byte[] storageIdArr = response.data;
        long responseCode = response.code;
    }


    @Test
    public void testGetNumObjects() throws COMException {
        MTPDataset request = new MTPDataset();
        request.code = 0x1006;//Operation Code
        request.params = new int[]{0xFFFFFFFF, 0, 0x00000000}; //Params
        MTPDataset response = MTPHelper.getInstance().sendCMDWithoutDataPhase(device, request);
        long responseCode = response.code;
        int[] responseParams = response.params;
    }

    @Test
    public void testSetDevicePropValue() throws COMException {
        MTPDataset request = new MTPDataset();
        request.code = 0x1016;
        request.params = new int[]{0xD402};
        Buffer buffer = new Buffer();
        buffer.writeByte(0x5);
        buffer.writeShortLe('X');
        buffer.writeShortLe('B');
        buffer.writeShortLe('O');
        buffer.writeShortLe('X');
        buffer.writeShortLe(0);
        request.data = buffer.readByteArray(); //MTP Protocol String
        MTPDataset response = MTPHelper.getInstance().sendCMDWithOutData(device, request);
        long responseCode = response.code;
    }
}
