package com.mgtech.blelib.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhaixiang on 2018/3/8.
 * 采谱完成数据
 */

public class SampleCompleteData {
    private List<Object> data;
    private long time;
    private boolean auto;

    public SampleCompleteData(Object[] data) {
        this.data = new ArrayList<>();
        Collections.addAll(this.data,data);
    }

    public SampleCompleteData( List<Object> data, long time,boolean auto) {
        this.data = new ArrayList<>();
        this.data.addAll(data);
        this.time = time;
        this.auto = auto;
    }

    public  List<Object> getData() {
        return data;
    }

    public void setData( List<Object> data) {
        this.data = data;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    @Override
    public String toString() {
        return "SampleCompleteData{" +
                "data=" + data +
                ", time=" + time +
                '}';
    }
}
