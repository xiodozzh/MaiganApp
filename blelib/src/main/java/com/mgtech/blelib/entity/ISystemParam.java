package com.mgtech.blelib.entity;

/**
 * Created by zhaixiang on 2018/3/15.
 * 系统参数
 */

public interface ISystemParam {
    /**
     * 将byte[] 转换为object
     *
     * @param data byte[]
     */
    void setData(byte[] data);

    /**
     * 将object转换为byte[]
     *
     * @param mac      mac地址
     * @param pairCode 配对码
     * @return byte[]
     */
    byte[] getSetParamCode(byte[] mac, int pairCode);

    /**
     * 用于保存的String
     * @return String
     */
    String getSaveString();

//    void getFromLocal();
}
