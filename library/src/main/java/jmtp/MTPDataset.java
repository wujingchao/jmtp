package jmtp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MTPDataset {

    public long code;

    public int[] params = new int[0];

    public byte[] data = new byte[0];

    public MTPDataset(){}

    public MTPDataset(long code, int[] params, byte[] data) {
        this.code = code;
        this.params = params;
        this.data = data;
    }

    public MTPDataset(long code, int[] params) {
        this.code = code;
        this.params = params;
    }

    public MTPDataset(long code) {
        this.code = code;
    }

    @Override
    public String toString() {
        String responseStr = responseCodeMap.get(code);
        return String.format("MTPDataset{code= %s, params= %s , data= %s }", responseStr != null ? responseStr : code, Arrays.toString(params), Arrays.toString(data));
    }

    private static Map<Long, String> responseCodeMap = new HashMap<>();

    public static  long RESPONSE_UNDEFINED = 0x2000;
    public static  long RESPONSE_OK = 0x2001;
    public static  long RESPONSE_GENERAL_ERROR = 0x2002;
    public static  long RESPONSE_SESSION_NOT_OPEN = 0x2003;
    public static  long RESPONSE_INVALID_TRANSACTION_ID = 0x2004;
    public static  long RESPONSE_OPERATION_NOT_SUPPORTED = 0x2005;
    public static  long RESPONSE_PARAMETER_NOT_SUPPORTED = 0x2006;
    public static  long RESPONSE_INCOMPLETE_TRANSFER = 0x2007;
    public static  long RESPONSE_INVALID_STORAGE_ID = 0x2008;
    public static  long RESPONSE_INVALID_OBJECT_HANDLE = 0x2009;
    public static  long RESPONSE_DEVICE_PROP_NOT_SUPPORTED = 0x200A;
    public static  long RESPONSE_INVALID_OBJECT_FORMAT_CODE = 0x200B;
    public static  long RESPONSE_STORAGE_FULL = 0x200C;
    public static  long RESPONSE_OBJECT_WRITE_PROTECTED = 0x200D;
    public static  long RESPONSE_STORE_READ_ONLY = 0x200E;
    public static  long RESPONSE_ACCESS_DENIED = 0x200F;
    public static  long RESPONSE_NO_THUMBNAIL_PRESENT = 0x2010;
    public static  long RESPONSE_SELF_TEST_FAILED = 0x2011;
    public static  long RESPONSE_PARTIAL_DELETION = 0x2012;
    public static  long RESPONSE_STORE_NOT_AVAILABLE = 0x2013;
    public static  long RESPONSE_SPECIFICATION_BY_FORMAT_UNSUPPORTED = 0x2014;
    public static  long RESPONSE_NO_VALID_OBJECT_INFO = 0x2015;
    public static  long RESPONSE_INVALID_CODE_FORMAT = 0x2016;
    public static  long RESPONSE_UNKNOWN_VENDOR_CODE = 0x2017;
    public static  long RESPONSE_CAPTURE_ALREADY_TERMINATED = 0x2018;
    public static  long RESPONSE_DEVICE_BUSY = 0x2019;
    public static  long RESPONSE_INVALID_PARENT_OBJECT = 0x201A;
    public static  long RESPONSE_INVALID_DEVICE_PROP_FORMAT = 0x201B;
    public static  long RESPONSE_INVALID_DEVICE_PROP_VALUE = 0x201C;
    public static  long RESPONSE_INVALID_PARAMETER = 0x201D;
    public static  long RESPONSE_SESSION_ALREADY_OPEN = 0x201E;
    public static  long RESPONSE_TRANSACTION_CANCELLED = 0x201F;
    public static  long RESPONSE_SPECIFICATION_OF_DESTINATION_UNSUPPORTED = 0x2020;
    public static  long RESPONSE_INVALID_OBJECT_PROP_CODE = 0xA801;
    public static  long RESPONSE_INVALID_OBJECT_PROP_FORMAT = 0xA802;
    public static  long RESPONSE_INVALID_OBJECT_PROP_VALUE = 0xA803;
    public static  long RESPONSE_INVALID_OBJECT_REFERENCE = 0xA804;
    public static  long RESPONSE_GROUP_NOT_SUPPORTED = 0xA805;
    public static  long RESPONSE_INVALID_DATASET = 0xA806;
    public static  long RESPONSE_SPECIFICATION_BY_GROUP_UNSUPPORTED = 0xA807;
    public static  long RESPONSE_SPECIFICATION_BY_DEPTH_UNSUPPORTED = 0xA808;
    public static  long RESPONSE_OBJECT_TOO_LARGE = 0xA809;
    public static  long RESPONSE_OBJECT_PROP_NOT_SUPPORTED = 0xA80A;

    static {
        responseCodeMap.put(RESPONSE_UNDEFINED, "RESPONSE_UNDEFINED");
        responseCodeMap.put(RESPONSE_OK, "RESPONSE_OK");
        responseCodeMap.put(RESPONSE_GENERAL_ERROR, "RESPONSE_GENERAL_ERROR");
        responseCodeMap.put(RESPONSE_SESSION_NOT_OPEN, "RESPONSE_SESSION_NOT_OPEN");
        responseCodeMap.put(RESPONSE_INVALID_TRANSACTION_ID, "RESPONSE_INVALID_TRANSACTION_ID");
        responseCodeMap.put(RESPONSE_OPERATION_NOT_SUPPORTED, "RESPONSE_OPERATION_NOT_SUPPORTED");
        responseCodeMap.put(RESPONSE_PARAMETER_NOT_SUPPORTED, "RESPONSE_PARAMETER_NOT_SUPPORTED");
        responseCodeMap.put(RESPONSE_INCOMPLETE_TRANSFER, "RESPONSE_INCOMPLETE_TRANSFER");
        responseCodeMap.put(RESPONSE_INVALID_STORAGE_ID, "RESPONSE_INVALID_STORAGE_ID");
        responseCodeMap.put(RESPONSE_INVALID_OBJECT_HANDLE, "RESPONSE_INVALID_OBJECT_HANDLE");
        responseCodeMap.put(RESPONSE_DEVICE_PROP_NOT_SUPPORTED, "RESPONSE_DEVICE_PROP_NOT_SUPPORTED");
        responseCodeMap.put(RESPONSE_INVALID_OBJECT_FORMAT_CODE, "RESPONSE_INVALID_OBJECT_FORMAT_CODE");
        responseCodeMap.put(RESPONSE_STORAGE_FULL, "RESPONSE_STORAGE_FULL");
        responseCodeMap.put(RESPONSE_OBJECT_WRITE_PROTECTED, "RESPONSE_OBJECT_WRITE_PROTECTED");
        responseCodeMap.put(RESPONSE_STORE_READ_ONLY, "RESPONSE_STORE_READ_ONLY");
        responseCodeMap.put(RESPONSE_ACCESS_DENIED, "RESPONSE_ACCESS_DENIED");
        responseCodeMap.put(RESPONSE_NO_THUMBNAIL_PRESENT, "RESPONSE_NO_THUMBNAIL_PRESENT");
        responseCodeMap.put(RESPONSE_SELF_TEST_FAILED, "RESPONSE_SELF_TEST_FAILED");
        responseCodeMap.put(RESPONSE_PARTIAL_DELETION, "RESPONSE_PARTIAL_DELETION");
        responseCodeMap.put(RESPONSE_STORE_NOT_AVAILABLE, "RESPONSE_STORE_NOT_AVAILABLE");
        responseCodeMap.put(RESPONSE_SPECIFICATION_BY_FORMAT_UNSUPPORTED, "RESPONSE_SPECIFICATION_BY_FORMAT_UNSUPPORTED");
        responseCodeMap.put(RESPONSE_NO_VALID_OBJECT_INFO, "RESPONSE_NO_VALID_OBJECT_INFO");
        responseCodeMap.put(RESPONSE_INVALID_CODE_FORMAT, "RESPONSE_INVALID_CODE_FORMAT");
        responseCodeMap.put(RESPONSE_UNKNOWN_VENDOR_CODE, "RESPONSE_UNKNOWN_VENDOR_CODE");
        responseCodeMap.put(RESPONSE_CAPTURE_ALREADY_TERMINATED, "RESPONSE_CAPTURE_ALREADY_TERMINATED");
        responseCodeMap.put(RESPONSE_DEVICE_BUSY, "RESPONSE_DEVICE_BUSY");
        responseCodeMap.put(RESPONSE_INVALID_PARENT_OBJECT, "RESPONSE_INVALID_PARENT_OBJECT");
        responseCodeMap.put(RESPONSE_INVALID_DEVICE_PROP_FORMAT, "RESPONSE_INVALID_DEVICE_PROP_FORMAT");
        responseCodeMap.put(RESPONSE_INVALID_DEVICE_PROP_VALUE, "RESPONSE_INVALID_DEVICE_PROP_VALUE");
        responseCodeMap.put(RESPONSE_INVALID_PARAMETER, "RESPONSE_INVALID_PARAMETER");
        responseCodeMap.put(RESPONSE_SESSION_ALREADY_OPEN, "RESPONSE_SESSION_ALREADY_OPEN");
        responseCodeMap.put(RESPONSE_TRANSACTION_CANCELLED, "RESPONSE_TRANSACTION_CANCELLED");
        responseCodeMap.put(RESPONSE_SPECIFICATION_OF_DESTINATION_UNSUPPORTED, "RESPONSE_SPECIFICATION_OF_DESTINATION_UNSUPPORTED");
        responseCodeMap.put(RESPONSE_INVALID_OBJECT_PROP_CODE, "RESPONSE_INVALID_OBJECT_PROP_CODE");
        responseCodeMap.put(RESPONSE_INVALID_OBJECT_PROP_FORMAT, "RESPONSE_INVALID_OBJECT_PROP_FORMAT");
        responseCodeMap.put(RESPONSE_INVALID_OBJECT_PROP_VALUE, "RESPONSE_INVALID_OBJECT_PROP_VALUE");
        responseCodeMap.put(RESPONSE_INVALID_OBJECT_REFERENCE, "RESPONSE_INVALID_OBJECT_REFERENCE");
        responseCodeMap.put(RESPONSE_GROUP_NOT_SUPPORTED, "RESPONSE_GROUP_NOT_SUPPORTED");
        responseCodeMap.put(RESPONSE_INVALID_DATASET, "RESPONSE_INVALID_DATASET");
        responseCodeMap.put(RESPONSE_SPECIFICATION_BY_GROUP_UNSUPPORTED, "RESPONSE_SPECIFICATION_BY_GROUP_UNSUPPORTED");
        responseCodeMap.put(RESPONSE_SPECIFICATION_BY_DEPTH_UNSUPPORTED, "RESPONSE_SPECIFICATION_BY_DEPTH_UNSUPPORTED");
        responseCodeMap.put(RESPONSE_OBJECT_TOO_LARGE, "RESPONSE_OBJECT_TOO_LARGE");
        responseCodeMap.put(RESPONSE_OBJECT_PROP_NOT_SUPPORTED, "RESPONSE_OBJECT_PROP_NOT_SUPPORTED");
    }
}
