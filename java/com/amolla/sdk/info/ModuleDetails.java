package com.amolla.sdk.info;

import com.amolla.sdk.Tube;
import android.os.Bundle;

public class ModuleDetails {

    public ModuleDetails() {}

    private static ModuleDetails mThis = null;
    public static ModuleDetails get() {
        if (mThis == null) {
            mThis = new ModuleDetails();
        }
        return mThis;
    }

    /**
     * The type that specifies to get the name of the scanner class.
     * {@link #getInfoByInt(int)} method can be used to return an enumeration of scanner class names.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_SCANNER_CLASS = 0;

    /**
     * The type that specifies to obtain the touch firmware version.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_TOUCH_FW_VER = 1;

    /**
     * The type that specifies to obtain the first camera firmware version.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_1_CAMERA_FW_VER = 2;

    /**
     * The type that specifies to obtain the second camera firmware version.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_2_CAMERA_FW_VER = 3;

    /**
     * The type that specifies to obtain a Bluetooth MAC (Media Access Control) address.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_BT_MAC_ADDR = 4;

    /**
     * The type that specifies to obtain the Wireless LAN driver version.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_WLAN_DRIVER_VER = 5;

    /**
     * The type that specifies to obtain the Wireless LAN firmware version.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_WLAN_FW_VER = 6;

    /**
     * The type that specifies to obtain the Wireless LAN configuration version.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_WLAN_CONFIG_VER = 7;

    /**
     * The type that specifies to obtain a Wireless LAN MAC (Media Access Control) address.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_WLAN_MAC_ADDR = 8;

    /**
     * The type that specifies to obtain a Wireless LAN IP (Internet Protocol) address.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_WLAN_IP_ADDR = 9;

    /**
     * The type that specifies to get the primary battery status.
     * In other words, it's called main battery.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_MAIN_BATT_STATE = 10;

    /**
     * The type that specifies to get the secondary battery status.
     * In other words, it's called backup battery.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_BACK_BATT_STATE = 11;

    /**
     * The type that specifies to get a flag that the primary battery is charging from the USB cable.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_MAIN_BATT_CHARGING = 12;

    /**
     * The type that specifies to get a flag that the secondary battery is charging from the primary battery.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_BACK_BATT_CHARGING = 13;

    /**
     * The type that specifies to check the battery low charge warning level.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_BATT_LOW_LEVEL = 14;

    /**
     * The type that specifies to check the battery critical charge warning level.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_BATT_CRITICAL_LEVEL = 15;

    /**
     * The type that specifies to check the primary battery current.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_MAIN_BATT_CURRENT = 16;

    /**
     * The type that specifies to check the real SOC of the primary battery. (State Of Charge)
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_MAIN_BATT_REALSOC = 17;

    /**
     * The type that specifies to check the secondary battery percentage.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_BACK_BATT_PERCENT = 18;

    /**
     * The type that specifies to check the secondary battery voltage.
     * @see #getInfoByString(int)
     * @since 1.0
     */
    public static final int INFO_MOD_BACK_BATT_VOLT = 19;

    private static String getStringOfIndex(int what) {
        switch (what) {
            case INFO_MOD_SCANNER_CLASS:        return "INFO_MOD_SCANNER_CLASS";
            case INFO_MOD_TOUCH_FW_VER:         return "INFO_MOD_TOUCH_FW_VER";
            case INFO_MOD_CAMERA_FW_VER:        return "INFO_MOD_1_CAMERA_FW_VER";
            case INFO_MOD_CAMERA_FW_VER:        return "INFO_MOD_2_CAMERA_FW_VER";
            case INFO_MOD_BT_MAC_ADDR:          return "INFO_MOD_BT_MAC_ADDR";
            case INFO_MOD_WLAN_DRIVER_VER:      return "INFO_MOD_WLAN_DRIVER_VER";
            case INFO_MOD_WLAN_FW_VER:          return "INFO_MOD_WLAN_FW_VER";
            case INFO_MOD_WLAN_CONFIG_VER:      return "INFO_MOD_WLAN_CONFIG_VER";
            case INFO_MOD_WLAN_MAC_ADDR:        return "INFO_MOD_WLAN_MAC_ADDR";
            case INFO_MOD_WLAN_IP_ADDR:         return "INFO_MOD_WLAN_IP_ADDR";
            case INFO_MOD_MAIN_BATT_STATE:      return "INFO_MOD_MAIN_BATT_STATE";
            case INFO_MOD_BACK_BATT_STATE:      return "INFO_MOD_BACK_BATT_STATE";
            case INFO_MOD_MAIN_BATT_CHARGING:   return "INFO_MOD_MAIN_BATT_CHARGING:";
            case INFO_MOD_BACK_BATT_CHARGING:   return "INFO_MOD_BACK_BATT_CHARGING:";
            case INFO_MOD_BATT_LOW_LEVEL:       return "INFO_MOD_BATT_LOW_LEVEL";
            case INFO_MOD_BATT_CRITICAL_LEVEL:  return "INFO_MOD_BATT_CRITICAL_LEVEL";
            case INFO_MOD_MAIN_BATT_CURRENT:    return "INFO_MOD_MAIN_BATT_CURRENT";
            case INFO_MOD_MAIN_BATT_REALSOC:    return "INFO_MOD_MAIN_BATT_REALSOC";
            case INFO_MOD_BACK_BATT_PERCENT:    return "INFO_MOD_BACK_BATT_PERCENT";
            case INFO_MOD_BACK_BATT_VOLT:       return "INFO_MOD_BACK_BATT_VOLT";
        }
        return "N/A";
    }

    /**
     * Returns the module details according to the selected type.
     * @param what An enumeration to specify when getting information.
     * @return The module details integer, depending on the input type. (TODO: Specifying an enumeration)
     * @since 1.0
     */
    public int getInfoToInt(int what) {
        return Tube.getInt(getStringOfIndex(what), new Bundle());
    }

    /**
     * Returns the module details according to the selected type.
     * @param what An enumeration to specify when getting information.
     * @return The module details string, depending on the input type.
     * @since 1.0
     */
    public String getInfoToString(int what) {
        return Tube.getString(getStringByIndex(what), null);
    }
}
