package com.mgtech.blelib.entity;

/**
 * Created by zhaixiang on 2018/3/8.
 * 系统参数
 */

public class SystemIndexParamData {
    private int index;
    private ISystemParam data;

    public SystemIndexParamData(int index, ISystemParam data) {
        this.index = index;
        this.data = data;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ISystemParam getData() {
        return data;
    }

    public void setData(ISystemParam data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SystemIndexParamData{" +
                "index=" + index +
                ", data=" + data +
                '}';
    }
}
