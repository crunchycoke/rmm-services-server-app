package com.ninjaone.serverapp.models;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Represents a Customer Bill including a machine device count and the total cost of supporting the
 * devices and services selected.
 */
public class CustomerBill implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final BigDecimal singleDeviceCost = new BigDecimal(4);

    private int windowsDeviceCount = 0;
    private int macDeviceCount = 0;
    private BigDecimal totalCost = new BigDecimal(0);

    public void incrementWindowsDeviceCount() {
        windowsDeviceCount++;
    }

    public void incrementMacDeviceCount() {
        macDeviceCount++;
    }

    public void addDeviceToTotalCost() {
        totalCost = totalCost.add(singleDeviceCost);
    }

    public int getWindowsDeviceCount() {
        return windowsDeviceCount;
    }

    public void setWindowsDeviceCount(int windowsDeviceCount) {
        this.windowsDeviceCount = windowsDeviceCount;
    }

    public int getMacDeviceCount() {
        return macDeviceCount;
    }

    public void setMacDeviceCount(int macDeviceCount) {
        this.macDeviceCount = macDeviceCount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "CustomerBill{" +
            "windowsDeviceCount=" + windowsDeviceCount +
            ", macDeviceCount=" + macDeviceCount +
            ", totalCost=" + totalCost +
            '}';
    }
}
