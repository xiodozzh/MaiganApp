package com.mgtech.blelib.entity;

import android.util.Log;

import com.mgtech.blelib.ecg.EcgUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaixiang on 2018/1/16.
 */

public class MeasureEcgData {
    private boolean isMeasuring;
    private EcgCallback ecgCallback;
    private float sampleRate;
    private EcgUtil ecgUtils;
    private EcgUtil detectUtils;
    private List<Double> resultData;
    private int order = -1;

    public MeasureEcgData(EcgCallback ecgCallback, boolean isMeasuring) {
        this.resultData = new ArrayList<>();
        this.isMeasuring = isMeasuring;
        this.ecgCallback = ecgCallback;
        initEcgUtil();
        initDetectUtil();
    }

    public void setCallback(EcgCallback ecgCallback, boolean isMeasuring) {
        this.ecgCallback = ecgCallback;
        this.isMeasuring = isMeasuring;
    }


    public void addData(ManualSampleData data) {
        int error = data.getErrorCode();
        Log.e("addData", "addData : " + data.getOrder());
        if (data.getOrder() != ((order + 1) & 0xF) && order != -1) {
            Log.e("error", "addData error");
        }
        order = data.getOrder();
        ecgCallback.onError(error);
        sampleRate = data.getSampleRate();
        if (data.isHaveData()) {
            short[] zippedData = data.getUnZippedData();
            ecgUtils.addData(zippedData, sampleRate);

            if (!isMeasuring) {
                if (data.getErrorCode() == 0) {
                    detectUtils.detect(zippedData, sampleRate);
                }else{
                    initDetectUtil();
                }
            }
        }
    }

    private void initEcgUtil() {
        order = -1;
        this.ecgUtils = new EcgUtil(new EcgUtil.Callback() {
            @Override
            public void onFilter(double[] data) {
                for (double d : data) {
                    resultData.add(d);
                }
                ecgCallback.drawData(data, sampleRate);
            }

            @Override
            public void isDetect(final double[] result) {
                if (!isMeasuring) {
                    int[] peakValley = new int[]{(int) Math.round(result[3]), (int) Math.round(result[4])};
                    ecgCallback.pwSuccess(sampleRate, peakValley);
                }
            }
        });
    }

    private void initDetectUtil(){
        this.detectUtils = new EcgUtil(new EcgUtil.Callback() {
            @Override
            public void onFilter(double[] data) {
                for (double d : data) {
                    resultData.add(d);
                }
                ecgCallback.drawData(data, sampleRate);
            }

            @Override
            public void isDetect(final double[] result) {
                if (!isMeasuring) {
                    int[] peakValley = new int[]{(int) Math.round(result[3]), (int) Math.round(result[4])};
                    ecgCallback.pwSuccess(sampleRate, peakValley);
                }
            }
        });
    }

    public double[] getData() {
        int size = resultData.size();
        double[] data = new double[size];
        for (int i = 0; i < size; i++) {
            data[i] = resultData.get(i);
        }
        return data;
    }

    public interface EcgCallback {
        /**
         * 手环报错
         *
         * @param errorCode 错误码
         */
        void onError(int errorCode);

        /**
         * 绘图
         *
         * @param data 解压数据
         */
        void drawData(double[] data, float sampleRate);

        /**
         * 采谱成功
         */
        void pwSuccess(float sample, int[] peakValley);

    }

}
