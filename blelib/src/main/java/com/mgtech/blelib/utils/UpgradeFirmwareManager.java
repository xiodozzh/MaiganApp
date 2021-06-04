package com.mgtech.blelib.utils;


import com.mgtech.blelib.entity.FirmwareUpgradeData;

import java.util.Arrays;
import java.util.zip.CRC32;

/**
 * Created by zhaixiang on 2016/9/28.
 * 升级文件处理类
 */

public class UpgradeFirmwareManager {
    public static final int FILE_FRAGMENT_LENGTH = 256;
    private static final int FILE_FRAGMENT_LENGTH_FILL = 256;
    private static final String BYTE_TYPE = "ISO-8859-1";
    private byte[] fileBytes;
    private int fileIndex;
    private static volatile UpgradeFirmwareManager upgradeFileManager;
    private int totalPkgNumber;
    private byte[] version;
    private int mirror;

    private UpgradeFirmwareManager(FirmwareUpgradeData data) {
        this.fileBytes = getFileBytes(data.getContent());
        this.fileIndex = 0;
        if (fileBytes.length % FILE_FRAGMENT_LENGTH == 0) {
            totalPkgNumber = fileBytes.length / FILE_FRAGMENT_LENGTH;
        } else {
            totalPkgNumber = fileBytes.length / FILE_FRAGMENT_LENGTH + 1;
        }
        this.mirror = data.getMirror();
        this.version = new byte[3];
        this.version[0] = (byte) (data.getVersion()[0] & 0xFF);
        this.version[1] = (byte) (data.getVersion()[1] & 0xFF);
        this.version[2] = (byte) (data.getVersion()[2] & 0xFF);
    }

    /**
     * 获得 UpgradeFileManager 单例
     *
     * @return UpgradeFileManager 单例
     */
    public static UpgradeFirmwareManager getInstance(FirmwareUpgradeData data) {
//        if (upgradeFileManager == null) {
//            synchronized (UpgradeFirmwareManager.class) {
//                if (upgradeFileManager == null) {
//                    upgradeFileManager = new UpgradeFirmwareManager(data);
//                }
//            }
//        }
        upgradeFileManager = new UpgradeFirmwareManager(data);
        return upgradeFileManager;
    }

    public byte[] getVersion() {
        return version;
    }

    /**
     * 生成固件文件数据
     *
     * @param index 包序
     * @return 发送包
     */
//    public byte[] getBody(int index) {
//        if (index * FILE_FRAGMENT_LENGTH >= fileBytes.length) {
//            return null;
//        }
//        byte[] code = new byte[FILE_FRAGMENT_LENGTH + 3];
//        code[0] = BluetoothConfig.CODE_FOTA_DATA;
//        byte[] pkg = intToByteArray(index);
//        code[1] = pkg[0];
//        code[2] = pkg[1];
//        System.arraycopy(fileBytes, index * FILE_FRAGMENT_LENGTH, code, 3, FILE_FRAGMENT_LENGTH);
//        return code;
//    }

    /**
     * 获得下一包文件
     *
     * @return 发送包
     */
    public byte[] getNextBody() {
        // 已经结束则抛出异常
        if (!hasNextBody()) {
            throw new RuntimeException("there is no more package!");
        }
        // 正常则返回发送包
        byte[] code = new byte[FILE_FRAGMENT_LENGTH + 2];
        byte[] pkg = intToByteArray(fileIndex);
        code[0] = pkg[0];
        code[1] = pkg[1];
        System.arraycopy(fileBytes, fileIndex * FILE_FRAGMENT_LENGTH, code, 2, FILE_FRAGMENT_LENGTH);
        fileIndex++;
        return code;
    }

    public byte[] getCrc() {
        return intToByteArray(getCRC32(fileBytes));
    }

    /**
     * 是否有未发的包
     *
     * @return true 还有下一包未发
     */
    public boolean hasNextBody() {
        return fileIndex * FILE_FRAGMENT_LENGTH < fileBytes.length;
    }

    /**
     * 获取当前发送包信息
     *
     * @return byte[0, 1]:总包数 byte[2,3]已发包数
     */
    public int getCurrentIndex() {
        return fileIndex;
    }

    /**
     * 补全file
     *
     * @param content 固件文件
     * @return 文件byte数组
     */
    private byte[] getFileBytes(byte[] content) {
        byte[] fileBytes;
        int extra = content.length % FILE_FRAGMENT_LENGTH_FILL;
        if (extra == 0) {
            // 正好可以整包发完
            fileBytes = content;

        } else {
            // 不能整包发完的补充 0xFF
            fileBytes = new byte[content.length + FILE_FRAGMENT_LENGTH_FILL - extra];
            Arrays.fill(fileBytes, (byte) 0xFF);
            System.arraycopy(content, 0, fileBytes, 0, content.length);
        }
        return fileBytes;
    }

    /**
     * 获得crc校验结果
     *
     * @param data 数据
     * @return 校验值
     */
    private long getCRC32(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return crc32.getValue();
    }

    /**
     * int 转换为byte
     *
     * @param i integer
     * @return 0 低位 4 高位
     */
    private byte[] intToByteArray(long i) {
        byte[] result = new byte[4];
        result[3] = (byte) ((i >> 24) & 0xFF);
        result[2] = (byte) ((i >> 16) & 0xFF);
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[0] = (byte) (i & 0xFF);
        return result;
    }

    public int getTotalPkgNumber() {
        return totalPkgNumber;
    }


    public int getMirror() {
        return mirror;
    }
}
