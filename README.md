# jmtp
fork from code.google.com/p/jmtp

### A library that invoke windows MTP COM Interface by JNI...

[MTP Protocol Summary](https://wujingchao.github.io/2018/01/28/mtp/)

[MTP Protocol Spec](https://files.cnblogs.com/files/skywang12345/mtp_specification_v1.0.pdf
)

Add Some Feture:

1.Send Custom MTP Command

![No Data Phase](http://o90rk2b64.bkt.clouddn.com/mtp_transfer_nodata.png?imageView2/2/w/656/h/500/q/100)

example:

```
@Test
public void testGetNumObjects() throws COMException {
    MTPDataset request = new MTPDataset();
    request.code = 0x1006;//Operation Code
    request.params = new int[]{0xFFFFFFFF, 0, 0x00000000}; //Params
    MTPDataset response = MTPHelper.getInstance().sendCMDWithoutDataPhase(device, request);
    long responseCode = response.code;
    int[] responseParams = response.params;
}
```

![I->R](http://o90rk2b64.bkt.clouddn.com/mtp_transfer_i-r.png?imageView2/2/w/656/h/500/q/100)

example:
```
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
        request.data = buffer.readByteArray(); //Friendly Name MTP Protocol String
        MTPDataset response = MTPHelper.getInstance().sendCMDWithOutData(device, request);
        long responseCode = response.code;
    }
```

![R->I](http://o90rk2b64.bkt.clouddn.com/mtp_transfer_r-i.png?imageView2/2/w/656/h/500/q/100)

    @Test
    public void testGetStorageIDs() throws COMException {
        MTPDataset request = new MTPDataset(0x1004);
        MTPDataset response = MTPHelper.getInstance().sendCMDWithInData(device, request);
        byte[] storageIdArr = response.data;
        long responseCode = response.code;
    }


2.Register MTP Event

![event](http://o90rk2b64.bkt.clouddn.com/mtp_transfer_event.png?imageView2/2/w/656/h/400/q/100)

    MTPHelper.getInstance().registerEvent(device, new MTPHelper.EventListener() {
        @Override
        public void onEvent(MTPDataset event, Exception e) {
            if (e != null) {
                e.printStackTrace();
                return;
            }
            //mtp event,example object added...
        }
    });


