package com.mgtech.blelib.entity;

/**
 * Created by zhaixiang on 2018/3/8.
 */

public class FirmStateData {
    public static final int START = 1;
    public static final int CONTINUE = 2;
    public static final int END = 3;

    private int status;
    private int progress;
    private int total;

    public FirmStateData(int status, int progress,int total) {
        this.status = status;
        this.total = total;
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "FirmStateData{" +
                "status=" + status +
                ", progress=" + progress +
                ", total=" + total +
                '}';
    }
}
