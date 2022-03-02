/*
 * Copyright (C) 2019 by J.J. (make.exe@gmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.amolla.server;

import com.android.internal.R;
import java.util.HashMap;

public class StaticConst {
    public static final String BUILTIN_SDK_VERSION = "1.0.0"; /** Auto fill out. Do NOT change. **/

    public static final String BASIC = "99";

    public static final HashMap<String, String> KERNEL_INTERFACE_MAP;
    static {
        KERNEL_INTERFACE_MAP = new HashMap<String, String>();
        try {

            KERNEL_INTERFACE_MAP.put("BUILTIN_SDK_VERSION", BUILTIN_SDK_VERSION);

            KERNEL_INTERFACE_MAP.put("LINUX_VERSION", "/proc/version");

            /* System property */
            KERNEL_INTERFACE_MAP.put("PPT_MANUFACTURER", "ro.product.manufacturer");
            KERNEL_INTERFACE_MAP.put("PPT_BUILD_NUM", "ro.build.version.incremental");
            KERNEL_INTERFACE_MAP.put("PPT_ANDROID_VER", "ro.build.version.release");
            KERNEL_INTERFACE_MAP.put("PPT_SDK_VER", "ro.vendor.build.version.sdk");
            KERNEL_INTERFACE_MAP.put("PPT_IMAGE_VER", "ro.build.display.id");
            KERNEL_INTERFACE_MAP.put("PPT_DEVICE_ID", "tinfo.kb.device_id");
            KERNEL_INTERFACE_MAP.put("PPT_CONSOLE", "ro.boot.console");

            /* File system */
            KERNEL_INTERFACE_MAP.put("FS_KEYBOX", "/persist/data/keymaster64/");
            KERNEL_INTERFACE_MAP.put("FS_PERSIST_DATA", "/persist/data/");
            KERNEL_INTERFACE_MAP.put("FS_SYSTEM_VENDOR_ETC", "/system/vendor/etc/");
            KERNEL_INTERFACE_MAP.put("FS_WLAN_DRIVER_VER", "/sys/info/wlan_version_driver");
            KERNEL_INTERFACE_MAP.put("FS_WLAN_FW_VER", "/sys/info/wlan_version_firmware");
            KERNEL_INTERFACE_MAP.put("FS_WLAN_CONFIG_VER", "/vendor/etc/wifi/wver");
            KERNEL_INTERFACE_MAP.put("FS_MAIN_BATT_STATE", "/sys/class/power_supply/battery/status");
            KERNEL_INTERFACE_MAP.put("FS_MAIN_BATT_CHARGING", "/sys/class/power_supply/battery/system_temp_level");
            KERNEL_INTERFACE_MAP.put("FS_BACK_BATT_STATE", "/sys/class/power_supply/battery/status");
            KERNEL_INTERFACE_MAP.put("FS_BACK_BATT_CHARGING", "/sys/class/power_supply/battery/charging_enabled");
            KERNEL_INTERFACE_MAP.put("FS_MAIN_BATT_CURRENT", "/sys/class/power_supply/battery/current_now");
            KERNEL_INTERFACE_MAP.put("FS_MAIN_BATT_REAL_SOC", "/sys/class/power_supply/battery/real_soc");
            KERNEL_INTERFACE_MAP.put("FS_BACK_BATT_PERCENTAGE", "/sys/class/power_supply/battery/capacity");
            KERNEL_INTERFACE_MAP.put("FS_BACK_BATT_VOLTAGE", "/sys/class/power_supply/battery/voltage_now");
            KERNEL_INTERFACE_MAP.put("FS_TOUCH_FW_VER", "/sys/class/input/input0/firmware_version");
            KERNEL_INTERFACE_MAP.put("FS_TOUCH_SENSITIVITY", "/sys/class/input/input0/sensitivity");
            KERNEL_INTERFACE_MAP.put("FS_UART_LIST", "/proc/tty/drivers");

            /* Serial ports */
            KERNEL_INTERFACE_MAP.put("SERIAL_UART_PORT", join(",","/dev/ttyHSL0","/dev/ttyHSL1"));

            /* Default value */
            KERNEL_INTERFACE_MAP.put("DEF_MODEL_NUM", BASIC);
            KERNEL_INTERFACE_MAP.put("DEF_BASE_MODEL_NUM", BASIC);
            KERNEL_INTERFACE_MAP.put("DEF_SERIAL_NUM", "");
            KERNEL_INTERFACE_MAP.put("DEF_PART_NUM", "");
            KERNEL_INTERFACE_MAP.put("DEF_MANUFACTURE_DATE", "");
            KERNEL_INTERFACE_MAP.put("DEF_BT_MAC_ADDR", "");
            KERNEL_INTERFACE_MAP.put("DEF_SHOW_NAVIGATION_BAR", "1");
            KERNEL_INTERFACE_MAP.put("DEF_HAS_DOCK_SETTINGS", "0");
            KERNEL_INTERFACE_MAP.put("DEF_DEFAULT_LED_KEYPAD_LEVEL", "1");
            KERNEL_INTERFACE_MAP.put("DEF_VIBRATOR_LEVEL", "0,12,15,18,21,24,27,31");
            KERNEL_INTERFACE_MAP.put("DEF_KEY_CONTROL_MODE", "0");
            KERNEL_INTERFACE_MAP.put("DEF_CONSOLE_PORT", "ttyHSL1");

            /* IOCTL request */
            KERNEL_INTERFACE_MAP.put("REQ_UART_CLOCK_ENABLE", "0xF000");    // kernel/drivers/tty/serial/msm_serial_hs.c
            KERNEL_INTERFACE_MAP.put("REQ_UART_CLOCK_DISABLE", "0xF001");   // kernel/drivers/tty/serial/msm_serial_hs.c
            KERNEL_INTERFACE_MAP.put("REQ_I2C_SLAVE_ADDRESS", "0x0703");    // kernel/include/uapi/linux/i2c-dev.h

            /* Check supported */
            KERNEL_INTERFACE_MAP.put("IS_DEFAULT_MODEL", join(",",BASIC));
            KERNEL_INTERFACE_MAP.put("IS_NOT_CHECK_KEYBOX", join(",",BASIC));
            KERNEL_INTERFACE_MAP.put("IS_BATT_CHARGING_STATE_IN_SETTINGS", join(",",BASIC));
            KERNEL_INTERFACE_MAP.put("IS_SERIAL_FULL_LENGTH", join(",",BASIC));
            KERNEL_INTERFACE_MAP.put("IS_SUSPEND_NOTI_ENABLE", join(",",BASIC));
            KERNEL_INTERFACE_MAP.put("IS_NOT_WAKE_UART", join(",",BASIC));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String join(String delimiter, String... tokens) {
        StringBuilder sb = new StringBuilder();
        for (String token : tokens) {
            if (sb.length() > 0) {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

	public static String except(String array, String delimiter, String... tokens) {
        if (array == null || array.length() == 0) return "";
        for (String token : tokens) {
            for (String piece : token.split(delimiter)) {
                array = array.replace(piece, "");
            }
        }
        array = array.replace(delimiter + delimiter, delimiter);
        if (array.startsWith(delimiter)) {
            array = array.substring(delimiter.length());
        }
        if (array.endsWith(delimiter)) {
            array = array.substring(0, array.length() - delimiter.length());
        }
        return array;
    }
}
