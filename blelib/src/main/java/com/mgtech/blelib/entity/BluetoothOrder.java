package com.mgtech.blelib.entity;

/**
 * Created by zhaixiang on 2018/3/8.
 * 蓝牙指令
 */

public class BluetoothOrder {
    private int order;
    private Object data;
    private boolean available = true;

    public BluetoothOrder(int order, Object data) {
        this.order = order;
        this.data = data;
    }

    public BluetoothOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }


    @Override
    public String toString() {
        return "BluetoothOrder{" +
                "order=" + order +
                ", data=" + data +
                ", available=" + available +
                '}';
    }
}
