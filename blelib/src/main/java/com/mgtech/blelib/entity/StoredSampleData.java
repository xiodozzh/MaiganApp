package com.mgtech.blelib.entity;

import com.mgtech.blelib.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.CRC32;

/**
 * Created by zhaixiang on 2016/12/22.
 * 自动采谱数据
 */

public class StoredSampleData {
    private int bytePerPkg;
    private Calendar calendar;
    private int totalDoublePoints;
    private int crcInt;
    private byte[] crc;
    private int currentOrder;
    private byte[] currentData;
    private boolean pkgError;
    private byte[] pureData;
    private boolean isError;
    protected boolean isAuto;

    private StoredSampleData(Builder builder){
        this.calendar = builder.calendar;
        this.totalDoublePoints = builder.totalDoublePoints;
        this.crcInt = builder.crcInt;
        this.crc = builder.crc;
        this.currentOrder = builder.currentOrder;
        this.currentData = builder.currentData;
        this.pkgError = builder.pkgError;
        this.pureData = builder.pureData;
        this.isError = builder.isError;
        this.isAuto = builder.isAuto;
    }

    public boolean isError() {
        return isError;
    }

    public boolean isFinish(int pointNumber) {
        return this.currentOrder * pointNumber >= this.totalDoublePoints;
    }

    public byte[] getPureData() {
        return this.pureData;
    }

    public List<Object> getUnzippedData(){
        return unzipPwAutoData(pureData);
    }

    /**
     * 是否crc验证成功
     *
     * @return 成功true
     */
    public boolean isCrcCorrect() {
        CRC32 crc = new CRC32();
        crc.update(this.pureData);
        return (int) crc.getValue() == crcInt;
    }

    public int getCurrentOrder() {
        return currentOrder;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public int getTotalDoublePoints() {
        return totalDoublePoints;
    }

    public int getCrcInt() {
        return crcInt;
    }

    public byte[] getCrc() {
        return crc;
    }

    public boolean isPkgError() {
        return pkgError;
    }

    public byte[] getCurrentData() {
        return currentData;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    @Override
    public String toString() {
        return "StoredSampleData{" +
                "bytePerPkg=" + bytePerPkg +
                ", calendar=" + calendar +
                ", totalDoublePoints=" + totalDoublePoints +
                ", crcInt=" + crcInt +
                ", crc=" + Arrays.toString(crc) +
                ", currentOrder=" + currentOrder +
                ", currentData=" + Arrays.toString(currentData) +
                ", pkgError=" + pkgError +
                ", pureData=" + Arrays.toString(pureData) +
                ", isError=" + isError +
                ", isAuto=" + isAuto +
                '}';
    }

    private List<Object> unzipPwAutoData(byte[] data) {
        short[] unzippedData = Utils.unzipData(data, 0);
        int size = unzippedData.length;
        if (size < 4) {
            return null;
        }
        List<Object> autoDataList = new ArrayList<>();
        // 前2个数字
        switch (unzippedData[0]) {
            case 0:
                autoDataList.add(unzippedData[1]);
                break;
            case 1:
                autoDataList.add(32768 / (float) unzippedData[1]);
                break;
            case 2:
                autoDataList.add(1000 / 1.024f / unzippedData[1]);
                break;
        }
        // introductor2~3个数字
        autoDataList.add(unzippedData[3] & 0xFFFF);
        // 4~5个数字
        autoDataList.add(0xFFFF);
        // 4个和之后的数字
        boolean hrStart = false;
        for (int i = 6; i < size; i++) {
            // 连续两个4095变为65535，65535之间两个数字合为一个数字
            if (i < size - 1 && unzippedData[i] == 0xFFF && unzippedData[i + 1] == 0xFFF) {
                i++;
                autoDataList.add(0xFFFF);
                hrStart = !hrStart;
                if (hrStart) {
                    if (i + 2 < size) {
                        int hr = unzippedData[i + 2] & 0xFFFF;
                        autoDataList.add(hr);
                        i += 2;
                    }
                }
            } else {
                autoDataList.add(unzippedData[i]);
            }
        }
        return autoDataList;
    }

    public static class Builder{
        private final int bytePerPkg;
        private static final int autoMeasure = 1 << 7;
        private Calendar calendar;
        private int totalDoublePoints;
        private int crcInt;
        private byte[] crc;
        private int currentOrder;
        private byte[] currentData;
        private boolean pkgError;
        private byte[] pureData;
        private boolean isError;
        private boolean isAuto;

        public Builder(int bytePerPkg) {
            this.bytePerPkg = bytePerPkg;
        }

        public void setInProfile400(byte[] data){
            if (data.length <= 2) {
                return;
            }
            byte type = (byte) ((data[2] >>> 4) & 0x7);
            if (type == 0x01) {
                // 信息包
                if (data.length < 11) {
                    throw new RuntimeException("not a auto sample data info pkg");
                }
                this.isAuto = (data[2] & autoMeasure) == autoMeasure;

                this.calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                long time = ((data[3] & 0xFF) + ((long) (data[4] & 0xFF) << 8) + ((long) (data[5] & 0xFF) << 16) +
                        ((long) (data[6] & 0xFF) << 24) + ((long) (data[7] & 0xFF) << 32) + ((long) (data[8] & 0xFF) << 40)
                        + ((long) (data[9] & 0xFF) << 48) + ((long) (data[10] & 0xFF) << 56));
                calendar.setTimeInMillis(time);

                this.totalDoublePoints = (data[11] & 0xFF) + ((data[12] & 0xFF) << 8);
                this.crcInt = (data[13] & 0xFF) + ((data[14] & 0xFF) << 8) + ((data[15] & 0xFF) << 16) + ((data[16] & 0xFF) << 24);
                this.crc = new byte[]{data[13], data[14], data[15], data[16]};
                this.currentOrder = 0x0;
                this.pkgError = false;
                this.currentData = null;
                this.pureData = new byte[this.totalDoublePoints * 3];
            } else if (type == 0x02) {
                // 数据包
                if (data.length <= 3) {
                    throw new RuntimeException("not a auto sample data pkg");
                }
                byte order = (byte) (data[2] & 0xF);
                if ((order & 0xF) != (this.currentOrder & 0xF)) {
                    this.pkgError = true;
                }
                System.arraycopy(data, 3, pureData, currentOrder * bytePerPkg * 3, data.length - 3);
                this.currentData = data;
                this.currentOrder++;
            } else if (type == 0x00) {
                this.isError = true;
            } else {
                throw new RuntimeException("does not have this type");
            }
        }

        public void setInProfile330(byte[] data){
            byte type = (byte) ((data[1] >>> 4) & 0x7);
            if (type == 0x01) {
                // 信息包
                if (data.length < 11) {
                    throw new RuntimeException("not a auto sample data info pkg");
                }
                this.isAuto = (data[1] & autoMeasure) == autoMeasure;

                this.calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                long time = ((data[2] & 0xFF) + ((long) (data[3] & 0xFF) << 8) + ((long) (data[4] & 0xFF) << 16) +
                        ((long) (data[5] & 0xFF) << 24) + ((long) (data[6] & 0xFF) << 32) + ((long) (data[7] & 0xFF) << 40)
                        + ((long) (data[8] & 0xFF) << 48) + ((long) (data[9] & 0xFF) << 56));
                calendar.setTimeInMillis(time);

                this.totalDoublePoints = (data[10] & 0xFF) + ((data[11] & 0xFF) << 8);
                this.crcInt = (data[12] & 0xFF) + ((data[13] & 0xFF) << 8) + ((data[14] & 0xFF) << 16) + ((data[15] & 0xFF) << 24);
                this.crc = new byte[]{data[12], data[13], data[14], data[15]};
                this.currentOrder = 0x0;
                this.pkgError = false;
                this.currentData = null;
                this.pureData = new byte[this.totalDoublePoints * 3];
            } else if (type == 0x02) {
                // 数据包
                if (data.length < 3) {
                    throw new RuntimeException("not a auto sample data pkg");
                }
                byte order = (byte) (data[1] & 0xF);
                if ((order & 0xF) != (this.currentOrder & 0xF)) {
                    this.pkgError = true;
                }
                System.arraycopy(data, 2, pureData, currentOrder * bytePerPkg * 3, data.length - 2);
                this.currentData = data;
                this.currentOrder++;
            } else if (type == 0x00) {
                this.isError = true;
            } else {
                throw new RuntimeException("does not have this type");
            }
        }

        public StoredSampleData create(){
            return new StoredSampleData(this);
        }

        public boolean isError() {
            return isError;
        }

        public boolean isFinish(int pointNumber) {
            return this.currentOrder * pointNumber >= this.totalDoublePoints;
        }


        /**
         * 是否crc验证成功
         *
         * @return 成功true
         */
        public boolean isCrcCorrect() {
            CRC32 crc = new CRC32();
            crc.update(this.pureData);
            return (int) crc.getValue() == crcInt;
        }


        public Calendar getCalendar() {
            return calendar;
        }


        public byte[] getCrc() {
            return crc;
        }


        public boolean isAuto() {
            return isAuto;
        }

        public void setAuto(boolean auto) {
            isAuto = auto;
        }
    }
}
