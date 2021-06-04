package com.mgtech.blelib.core;


import com.mgtech.blelib.biz.BluetoothConfig;

import java.util.Arrays;

/**
 * @author zhaixiang
 * @date 2016/8/17
 * 解析广播包
 */
public class ParsedBleBroadcast {
    private int[] protocolVersion;
    private int[] bandSoftVersion;
    private int[] bandHardVersion;
    private int pairCode;
    private byte[] macAddress;
    private int chargeState;
    private int power;
    private int broadcastReason;
    private int randomNumber;
    private byte[] signature;
    private String deviceName;

    private ParsedBleBroadcast() {
    }


    public int[] getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int[] protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public int[] getBandSoftVersion() {
        return bandSoftVersion;
    }

    public void setBandSoftVersion(int[] bandSoftVersion) {
        this.bandSoftVersion = bandSoftVersion;
    }

    public int[] getBandHardVersion() {
        return bandHardVersion;
    }

    public void setBandHardVersion(int[] bandHardVersion) {
        this.bandHardVersion = bandHardVersion;
    }

    public int getPairCode() {
        return pairCode;
    }

    public void setPairCode(int pairCode) {
        this.pairCode = pairCode;
    }

    public byte[] getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(byte[] macAddress) {
        this.macAddress = macAddress;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public int getChargeState() {
        return chargeState;
    }

    public void setChargeState(int chargeState) {
        this.chargeState = chargeState;
    }

    public int getBroadcastReason() {
        return broadcastReason;
    }

    public void setBroadcastReason(int broadcastReason) {
        this.broadcastReason = broadcastReason;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber) {
        this.randomNumber = randomNumber;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public String toString() {
        return "广播包信息：\n" +
                "设备名：" + deviceName +
                "\n通讯协议：" + Arrays.toString(protocolVersion) +
                "\n软件版本：" + Arrays.toString(bandSoftVersion) +
                "\n硬件版本：" + Arrays.toString(bandHardVersion) +
                "\n配对码：" + pairCode +
                "\n地址：" + Arrays.toString(macAddress) +
                "\n充电状态：" + chargeState +
                "\n剩余电量：" + power +
                "\n随机码：" + randomNumber +
                "\n广播原因：" + (broadcastReason == 0x00 ? "被动广播" : "主动广播") +
                "\n签名：" + Arrays.toString(signature);
    }


    /**
     * 协议3.2.0 兼容 3.1.0
     */
    public static ParsedBleBroadcast parsedBleBroadcast(byte[] data) {
        if (data.length < 22) {
            return new ParsedBleBroadcast();
        }
        ParsedBleBroadcast parsedBleBroadcast = new ParsedBleBroadcast();
        parsedBleBroadcast.setProtocolVersion(new int[]{data[0] & 0xFF, data[1] & 0xFF, data[2] & 0xFF});
        parsedBleBroadcast.setBandSoftVersion(new int[]{data[3] & 0xFF, data[4] & 0xFF, data[5] & 0xFF});
        parsedBleBroadcast.setBandHardVersion(new int[]{data[6] & 0xFF, data[7] & 0xFF, data[8] & 0xFF});
        byte[] mac = new byte[]{data[9], data[10], data[11], data[12], data[13], data[14]};
        parsedBleBroadcast.setMacAddress(mac);
        parsedBleBroadcast.setPairCode((data[15] & 0xFF) + ((data[16] & 0xFF) << 8) + ((data[17] & 0xFF) << 16) + ((data[18] & 0xFF) << 24));

        if (isLargerThan(3, 1, 0, parsedBleBroadcast)) {
            parsedBleBroadcast.setRandomNumber((data[19] & 0xFF) + (data[20] << 8));
            if (parsedBleBroadcast.randomNumber != 0) {
                parsedBleBroadcast.setBroadcastReason(BluetoothConfig.BROADCAST_ALLOW_BOND);
            }
            parsedBleBroadcast.setSignature(new byte[]{data[21]});
        } else {
            parsedBleBroadcast.setBroadcastReason(data[19] & 0xFF);
            parsedBleBroadcast.setSignature(new byte[]{data[20], data[21]});
        }
        return parsedBleBroadcast;
    }

    private static boolean isLargerThan(int v0, int v1, int v2, ParsedBleBroadcast parsedBleBroadcast) {
        int[] version = parsedBleBroadcast.protocolVersion;
        if (version[0] > v0) {
            return true;
        } else if (version[0] < v0) {
            return false;
        } else {
            if (version[1] > v1) {
                return true;
            } else if (version[1] < v1) {
                return false;
            } else {
                return version[2] > v2;
            }
        }
    }

}
