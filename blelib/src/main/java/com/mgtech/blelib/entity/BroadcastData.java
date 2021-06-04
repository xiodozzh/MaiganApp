package com.mgtech.blelib.entity;

import com.mgtech.blelib.core.ParsedBleBroadcast;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author zhaixiang
 * 广播
 */

public class BroadcastData {
    public static final int SIGNAL_WEAK = 0;
    public static final int SIGNAL_MEDIUM = 1;
    public static final int SIGNAL_STRONG = 2;
    public static final int SIGNAL_STRONGEST = 3;

    private int randomNumber;
    private String deviceName;
    private String macAddress;
    private int[] protocolVersion;
    private int[] softwareVersion;
    private int[] hardwareVersion;
    private int pairCode;
    private byte[] macAddressBytes;
    private int power;
    private long initTime;
    private int rssi;

    public BroadcastData(String mac,int rssi, long initTime, ParsedBleBroadcast parsedBleBroadcast){
        macAddress = mac;
        randomNumber = parsedBleBroadcast.getRandomNumber();
        protocolVersion = parsedBleBroadcast.getProtocolVersion();
        pairCode = parsedBleBroadcast.getPairCode();
        macAddressBytes = parsedBleBroadcast.getMacAddress();
        power = parsedBleBroadcast.getPower();
        deviceName = parsedBleBroadcast.getDeviceName();
        softwareVersion = parsedBleBroadcast.getBandSoftVersion();
        hardwareVersion = parsedBleBroadcast.getBandHardVersion();
        this.rssi = rssi;
        this.initTime = initTime;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int[] getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int[] protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public int[] getFirmwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(int[] softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public int[] getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(int[] hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public int getPairCode() {
        return pairCode;
    }

    public void setPairCode(int pairCode) {
        this.pairCode = pairCode;
    }

    public byte[] getMacAddressBytes() {
        return macAddressBytes;
    }

    public void setMacAddressBytes(byte[] macAddressBytes) {
        this.macAddressBytes = macAddressBytes;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public long getInitTime() {
        return initTime;
    }

    public void setInitTime(long initTime) {
        this.initTime = initTime;
    }

    public int getRssi() {
        return rssi;
    }

    public int getSignalLevel(){
        if (rssi < -80){
            return SIGNAL_WEAK;
        }else if (rssi < -70){
            return SIGNAL_MEDIUM;
        }else if (rssi < -60){
            return SIGNAL_STRONG;
        }else {
            return SIGNAL_STRONGEST;
        }
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }


    @Override
    public String toString() {
        return "BroadcastData{" +
                "randomNumber=" + randomNumber +
                ", deviceName='" + deviceName + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", protocolVersion=" + Arrays.toString(protocolVersion) +
                ", pairCode=" + pairCode +
                ", macAddressBytes=" + Arrays.toString(macAddressBytes) +
                ", power=" + power +
                ", initTime=" + initTime +
                ", rssi=" + rssi +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BroadcastData that = (BroadcastData) o;
        return randomNumber == that.randomNumber &&
                pairCode == that.pairCode &&
                power == that.power &&
                initTime == that.initTime &&
                rssi == that.rssi &&
                Objects.equals(deviceName, that.deviceName) &&
                Objects.equals(macAddress, that.macAddress) &&
                Arrays.equals(protocolVersion, that.protocolVersion) &&
                Arrays.equals(softwareVersion, that.softwareVersion) &&
                Arrays.equals(hardwareVersion, that.hardwareVersion) &&
                Arrays.equals(macAddressBytes, that.macAddressBytes);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(randomNumber, deviceName, macAddress, pairCode, power, initTime, rssi);
        result = 31 * result + Arrays.hashCode(protocolVersion);
        result = 31 * result + Arrays.hashCode(softwareVersion);
        result = 31 * result + Arrays.hashCode(hardwareVersion);
        result = 31 * result + Arrays.hashCode(macAddressBytes);
        return result;
    }
}
