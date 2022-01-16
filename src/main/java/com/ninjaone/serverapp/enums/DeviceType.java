package com.ninjaone.serverapp.enums;

import java.util.EnumSet;

/**
 *
 */
public enum DeviceType {
    WINDOWS_WORKSTATION,
    WINDOWS_SERVER,
    MAC;

    /**
     * @return
     */
    public static EnumSet<DeviceType> getWindowsDevices() {
        return EnumSet.of(WINDOWS_WORKSTATION, WINDOWS_SERVER);
    }

    /**
     * @return
     */
    public static EnumSet<DeviceType> getMacDevices() {
        return EnumSet.complementOf(getWindowsDevices());
    }

    /**
     * @param deviceType
     * @return
     */
    public static boolean isWindowsDevice(DeviceType deviceType) {
        return DeviceType.getWindowsDevices().contains(deviceType);
    }
}
