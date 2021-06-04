package com.mgtech.maiganapp.data.model;

import android.content.res.Resources;
import android.util.TypedValue;

import java.util.Arrays;

/**
 * @author zhaixiang
 */
public class EcgModel {
    public String measureGuid;
    public String userId;
    public long measureTime;
    public float hr;
    public int ecgResult;
    public float sampleRate;
    public float[] ecgData;

    /**
     * 获取走速 mm/s = 采样率 * 绘图时两点之间的像素点数 / 1mm的像素点
     * @param pxBetweenPoints 绘图时两点之间的像素点数
     * @return 走速
     */
    public float getWalkSpeed(float pxBetweenPoints){
        return sampleRate * pxBetweenPoints / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1,
                Resources.getSystem().getDisplayMetrics());
    }

    @Override
    public String toString() {
        return "EcgModel{" +
                "measureGuid='" + measureGuid + '\'' +
                ", userId='" + userId + '\'' +
                ", measureTime=" + measureTime +
                ", hr=" + hr +
                ", ecgResult=" + ecgResult +
                ", sampleRate=" + sampleRate +
                '}';
    }
}
