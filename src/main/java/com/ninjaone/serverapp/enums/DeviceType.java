package com.ninjaone.serverapp.enums;

import java.util.EnumSet;

public enum DeviceType {
    WINDOWS_WORKSTATION,
    WINDOWS_SERVER,
    MAC;

    public static EnumSet<DeviceType> getWindowsDevices() {
        return EnumSet.of(WINDOWS_WORKSTATION, WINDOWS_SERVER);
    }

    public static EnumSet<DeviceType> getMacDevices() {
        return EnumSet.complementOf(getWindowsDevices());
    }

    public static boolean isWindowsDevice(DeviceType deviceType) {
        return DeviceType.getWindowsDevices().contains(deviceType);
    }
}
