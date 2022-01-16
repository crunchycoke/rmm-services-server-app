package com.ninjaone.serverapp.enums;

import java.util.EnumSet;

/**
 * Represents whether a device is a Windows Workstation, Windows Server, or a Mac machine.
 */
public enum DeviceType {
    WINDOWS_WORKSTATION,
    WINDOWS_SERVER,
    MAC;

    /**
     * Retrieves all Windows Devices within the enums defined.
     *
     * @return A set of Windows enums.
     */
    public static EnumSet<DeviceType> getWindowsDevices() {
        return EnumSet.of(WINDOWS_WORKSTATION, WINDOWS_SERVER);
    }

    /**
     * Retrieves all Mac Devices within the enums defined.
     * @return A set of Mac enums.
     */
    public static EnumSet<DeviceType> getMacDevices() {
        return EnumSet.complementOf(getWindowsDevices());
    }

    /**
     * Checks whether the device is a Windows device or not.
     *
     * @param deviceType Represents the provided device type.
     * @return A boolean value indicating whether a device is a Windows device.
     */
    public static boolean isWindowsDevice(DeviceType deviceType) {
        return DeviceType.getWindowsDevices().contains(deviceType);
    }
}
