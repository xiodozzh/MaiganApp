package com.mgtech.data.entity;

import com.mgtech.domain.utils.BluetoothConfig;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.zip.CRC32;

/**
 * Created by zhaixiang on 2016/9/28.
 * 升级文件处理类
 */

public class UpgradeFileManager {
//    private final String TAG = "tag";
    private static int FILE_FRAGMENT_LENGTH = 256;
    private static int FILE_FRAGMENT_LENGTH_FILL = 256;
//    private static final int FILE_FRAGMENT_LENGTH_FILL = 300;
    //    private final int DATA_LENGTH;
    private static final String BYTE_TYPE = "ISO-8859-1";
    private byte[] fileBytes;
    private int fileIndex;
    private static UpgradeFileManager upgradeFileManager;
    private int totalPkgNumber;

    private UpgradeFileManager() {
    }

    /**
     * 获得 UpgradeFileManager 单例
     *
     * @return UpgradeFileManager 单例
     */
    public static UpgradeFileManager getInstance() {
        if (upgradeFileManager == null) {
            synchronized (UpgradeFileManager.class) {
                if (upgradeFileManager == null) {
                    upgradeFileManager = new UpgradeFileManager();
                }
            }
        }
//        FILE_FRAGMENT_LENGTH = fragmentLength;
//        FILE_FRAGMENT_LENGTH_FILL = fragmentLength;
        return upgradeFileManager;
    }

    /**
     * 生成请求包
     *
     * @return 请求包
     */
    public byte[] getHeader(byte location, String content, int[] version) {
        byte[] code = new byte[15];
        this.fileBytes = getFileBytes(content);
        this.fileIndex = 0;
        if (fileBytes.length % FILE_FRAGMENT_LENGTH == 0) {
            totalPkgNumber = fileBytes.length / FILE_FRAGMENT_LENGTH;
        } else {
            totalPkgNumber = fileBytes.length / FILE_FRAGMENT_LENGTH + 1;
        }
        byte[] totalPkg = intToByteArray(totalPkgNumber);
        long crcInteger = getCRC32(fileBytes);
        byte[] crc = intToByteArray((int) crcInteger);
        code[0] = BluetoothConfig.CODE_FOTA_INFO;
        code[1] = (byte) (version[0] & 0xFF);
        code[2] = (byte) (version[1] & 0xFF);
        code[3] = (byte) (version[2] & 0xFF);
        if (location == 0x02) {
            code[4] = location;
        }else{
            code[4] = (byte) (1 - location);
        }
        code[5] = (byte) (FILE_FRAGMENT_LENGTH & 0xFF);
        code[6] = (byte) ((FILE_FRAGMENT_LENGTH >> 8) & 0xFF);
        code[7] = totalPkg[0];
        code[8] = totalPkg[1];
        code[9] = crc[0];
        code[10] = crc[1];
        code[11] = crc[2];
        code[12] = crc[3];
        code[13] = (byte) (totalPkg[0] ^ crc[0]);
        code[14] = (byte) (totalPkg[1] ^ crc[1]);
        return code;
    }

    /**
     * 生成固件文件数据
     *
     * @param index 包序
     * @return 发送包
     */
    public byte[] getBody(int index) {
        if (index * FILE_FRAGMENT_LENGTH >= fileBytes.length) {
            return null;
        }
        byte[] code = new byte[FILE_FRAGMENT_LENGTH + 3];
        code[0] = BluetoothConfig.CODE_FOTA_DATA;
        byte[] pkg = intToByteArray(index);
        code[1] = pkg[0];
        code[2] = pkg[1];
        System.arraycopy(fileBytes, index * FILE_FRAGMENT_LENGTH, code, 3, FILE_FRAGMENT_LENGTH);
        return code;
    }

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
        byte[] code = new byte[FILE_FRAGMENT_LENGTH + 3];
        code[0] = BluetoothConfig.CODE_FOTA_DATA;
        byte[] pkg = intToByteArray(fileIndex);
        code[1] = pkg[0];
        code[2] = pkg[1];
        System.arraycopy(fileBytes, fileIndex * FILE_FRAGMENT_LENGTH, code, 3, FILE_FRAGMENT_LENGTH);
        fileIndex++;
        return code;
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
    private byte[] getFileBytes(String content) {
        byte[] fileBytes = null;
        int extra = content.length() % FILE_FRAGMENT_LENGTH_FILL;
        if (extra == 0) {
            // 正好可以整包发完
            try {
                fileBytes = content.getBytes(BYTE_TYPE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // 不能整包发完的补充 0xFF
            fileBytes = new byte[content.length() + FILE_FRAGMENT_LENGTH_FILL - extra];
            Arrays.fill(fileBytes, (byte) 0xFF);
            try {
                System.arraycopy(content.getBytes(BYTE_TYPE), 0, fileBytes, 0, content.length());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
    private byte[] intToByteArray(int i) {
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
}
