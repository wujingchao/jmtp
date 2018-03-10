package io.github.wujingchao.demo.jmtp;


import be.derycke.pieter.com.COMException;
import jmtp.MTPDataset;
import jmtp.MTPHelper;
import jmtp.PortableDevice;
import jmtp.PortableDeviceManager;
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
                log(event);
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
        log(response);
    }


    @Test
    public void testGetNumObjects() throws COMException {
        MTPDataset request = new MTPDataset(0x1006, new int[]{
                0xFFFFFFFF,//storageID for all storage
                0, //Object Format for all
                0x00000000//ObjectHandle of Association  for all
        });
        MTPDataset response = MTPHelper.getInstance().sendCMDWithoutDataPhase(device, request);
        log(response);
    }
}
