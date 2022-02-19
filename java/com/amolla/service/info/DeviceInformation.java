package com.amolla.service.info;

import com.amolla.sdk.Tube;
import com.amolla.sdk.ErroNo;
import com.amolla.sdk.To;
import com.amolla.sdk.util.SysFs;
import com.amolla.jni.InfoNative;
import com.amolla.service.DynamicController;

import android.os.SystemProperties;
import android.util.Log;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceInformation extends DynamicController {
    private static final boolean DEBUG = DEBUG_ALL || false;

    private String mDeviceName;
    private String mProcessor;
    private String mHardwareRevision;
    private String mSerialNumber;
    private String mPartNumber;
    private String mManufacturer;
    private String mManufactureDate;
    private String mModelNumber;
    private String mBaseModelNumber;
    private String mBuildNumber;
    private String mAndroidVersion;
    private String mKernelVersion;
    private String mSdkVersion;
    private String mImageVersion;
    private String mAttestationKey;

    public DeviceInformation(DynamicController ctrl, String tag) {
        super(ctrl, tag);
    }

    public static DeviceInformation init(DynamicController ctrl) {
        if (mInstance == null) {
            mInstance = new DeviceInformation(ctrl, DeviceInformation.class.getSimpleName());
            get().updateAll();
        }
        return get();
    }

    public static DeviceInformation get() {
        return (DeviceInformation) mInstance;
    }

    public boolean updateAll() {
        getDeviceName(true);
        getProcessor(true);
        getHardwareRevision(true);
        getSerialNumber(true);
        getPartNumber(true);
        getManufacturer(true);
        getManufactureDate(true);
        getModelNumber(true);
        getBaseModelNumber(true);
        getBuildNumber(true);
        getAndroidVersion(true);
        getKernelVersion(true);
        getSdkVersion(true);
        getImageVersion(true);
        getAttestationKey(true);
        return true;
    }

    public String getDeviceName(boolean update) {
        if (mDeviceName == null || update) {
            mDeviceName = InfoNative.getModelName();
        }
        return mDeviceName;
    }

    public String getProcessor(boolean update) {
        if (mProcessor == null || update) {
            mProcessor = InfoNative.getProcessorName();
        }
        return mProcessor;
    }

    public String getHardwareRevision(boolean update) {
        if (mHardwareRevision == null || update) {
            mHardwareRevision = String.valueOf(InfoNative.getHardwareRevision());
        }
        return mHardwareRevision;
    }

    public String getSerialNumber(boolean update) {
        if (mSerialNumber == null || update) {
            mSerialNumber = getDefinition("DEF_SERIAL_NUM");
        }
        return mSerialNumber;
    }

    public String getPartNumber(boolean update) {
        if (mPartNumber == null || update) {
            mPartNumber = getDefinition("DEF_PART_NUM");
        }
        return mPartNumber;
    }

    public String getManufacturer(boolean update) {
        if (mManufacturer == null || update) {
            mManufacturer = SystemProperties.get(getDefinition("PPT_MANUFACTURER"), ErroNo.UNKNOWN.toString());
        }
        return mManufacturer;
    }

    public String getManufactureDate(boolean update) {
        if (mManufactureDate == null || update) {
            mManufactureDate = getDefinition("DEF_MANUFACTURE_DATE");
        }
        return mManufactureDate;
    }

    public String getModelNumber(boolean update) {
        if (mModelNumber == null || update) {
            mModelNumber = getDefinition("DEF_MODEL_NUM");
        }
        return mModelNumber;
    }

    public String getBaseModelNumber(boolean update) {
        if (mBaseModelNumber == null || update) {
            mBaseModelNumber = getDefinition("DEF_BASE_MODEL_NUM");
        }
        return mBaseModelNumber;
    }

    public String getBuildNumber(boolean update) {
        if (mBuildNumber == null || update) {
            mBuildNumber = SystemProperties.get(getDefinition("PPT_BUILD_NUM"), ErroNo.UNKNOWN.toString());
        }
        return mBuildNumber;
    }

    public String getAndroidVersion(boolean update) {
        if (mAndroidVersion == null || update) {
            mAndroidVersion = SystemProperties.get(getDefinition("PPT_ANDROID_VER"), ErroNo.UNKNOWN.toString());
        }
        return mAndroidVersion;
    }

    public String getKernelVersion(boolean update) {
        if (mKernelVersion == null || update) {
            mKernelVersion = ErroNo.UNKNOWN.toString();
            BufferedReader reader = new BufferedReader(new FileReader(getDefinition("LINUX_VERSION"), 256);
            try {
                String version = reader.readLine();

                final String VERSION_REGEX =
                    "\\w+\\s+" + // ignore: Linux
                    "\\w+\\s+" + // ignore: version
                    "([^\\s]+)\\s+" + // group 1: 2.6.22-omap1
                    "\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+" + // group 2: (xxxxxx@xxxxx.constant)
                    "\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+" + // ignore: (gcc ..)
                    "([^\\s]+)\\s+" + // group 3: #26
                    "(?:PREEMPT\\s+)?" + // ignore: PREEMPT (optional)
                    "(.+)"; // group 4: date

                Pattern p = Pattern.compile(VERSION_REGEX);
                Matcher m = p.matcher(version);
                if (m.matches() && m.groupCount() > 3) {
                    mKernelVersion = (new StringBuilder(m.group(1))).toString();
                }

            } catch (IOException e) {
                if (DEBUG) { e.printStackTrace(); }
            } finally {
                if (reader != null) reader.close();
            }
        }
        return mKernelVersion;
    }

    public String getSdkVersion(boolean update) {
        if (mSdkVersion == null || update) {
            mSdkVersion = SystemProperties.get(getDefinition("BUILTIN_SDK_VERSION"), ErroNo.UNKNOWN.toString());
        }
        return mSdkVersion;
    }

    public String getImageVersion(boolean update) {
        if (mImageVersion == null || update) {
            mImageVersion = SystemProperties.get(getDefinition("PPT_IMAGE_VER"), ErroNo.UNKNOWN.toString());
        }
        return mImageVersion;
    }

    public String getAttestationKey(boolean update) {
        if (mAttestationKey == null || update) {
            mAttestationKey = SystemProperties.get(getDefinition("PPT_DEVICE_ID"), ErroNo.NOT_PROVISIONED.toString());
            if (!Tube.check("STATIC_SUPPORTED", "IS_NOT_CHECK_KEYBOX")) {
                File keyBox = new File(getDefinition("FS_KEYBOX"));
                if (keyBox.isDirectory() && keyBox.list().length > 0) {
                    String[] fileList = (new File(getDefinition("FS_PERSIST_DATA"))).list(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return name.equals("att");
                        }
                    });
                    if (fileList == null || fileList.length != 1)  {
                        mAttestationKey = ErroNo.UNKNOWN.toString();
                    }
                }
            }
        }
        return mAttestationKey;
    }
}
