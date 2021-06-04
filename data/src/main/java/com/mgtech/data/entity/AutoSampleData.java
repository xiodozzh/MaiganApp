package com.mgtech.data.entity;

import androidx.annotation.NonNull;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.zip.CRC32;

/**
 * Created by zhaixiang on 2016/12/22.
 * 自动采谱数据
 */

public class AutoSampleData {
    private final int bytePerPkg;
    private Calendar calendar;
    private int totalDoublePoints;
    private int crcInt;
    private byte[] crc;
    private int currentOrder;
    private byte[] currentData;
    private boolean pkgError;
    private byte[] pureData;

    public AutoSampleData(int dataPerPackage) {
        this.bytePerPkg = dataPerPackage;
    }

    public void addData(@NonNull byte[] data) {
        if (data.length <= 1) {
            return;
        }
        byte type = (byte) ((data[1] >> 4) & 0xF);
        if (type == 0x01) {
            if (data.length < 11) {
                throw new RuntimeException("not a auto sample data info pkg");
            }
            this.calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, data[2] & 0xFf);
            calendar.set(Calendar.MINUTE, data[3] & 0xFf);
            calendar.set(Calendar.SECOND, data[4] & 0xFf);
            if (calendar.after(Calendar.getInstance())){
                calendar.add(Calendar.DATE,-1);
            }
            this.totalDoublePoints = (data[5] & 0xFF) + ((data[6] & 0xFF) << 8);
            this.crcInt = (data[7] & 0xFF) + ((data[8] & 0xFF) << 8) + ((data[9] & 0xFF) << 16) + ((data[10] & 0xFF) << 24);
            this.crc = new byte[]{data[7], data[8], data[9], data[10]};
            this.currentOrder = 0x0;
            this.pkgError = false;
            this.currentData = null;
            this.pureData = new byte[this.totalDoublePoints * 3];
        } else if (type == 0x02) {
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
        }
    }

    public boolean isFinish(int pointNumber) {
        return this.currentOrder * pointNumber >= this.totalDoublePoints;
    }

    public byte[] getPureData() {
        return this.pureData;
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

    @Override
    public String toString() {
        return "[" +
                "采样时间：" + DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar) +
                ", 总双采样点数：" + totalDoublePoints +
                ", 周期：" + crcInt +
                ']';
    }
}
