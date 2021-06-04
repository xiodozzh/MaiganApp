package com.mgtech.blelib.entity;

import android.util.Log;

import com.mgtech.blelib.pwRecognize.IRecognize;

import java.util.List;

/**
 * Created by zhaixiang on 2018/1/16.
 * 测量
 */

public class MeasurePwData {
    private final boolean isMeasuring;
    private IRecognize recognize;
//    private ResponseManualData responseManualData;
    private PwCallback callback;
    private int result = IRecognize.CONTINUE;

    public MeasurePwData(PwCallback callback, boolean isMeasuring, IRecognize recognize) {
        this.isMeasuring = isMeasuring;
        this.recognize = recognize;
//        this.responseManualData = new ResponseManualData();
        this.callback = callback;
    }

    public void reset() {
        this.recognize.clear();
        this.result = IRecognize.CONTINUE;
//        this.responseManualData = new ResponseManualData();
    }

    public void addData(ManualSampleData data) {
        if (result == IRecognize.COMPLETE) {
            return;
        }
        Log.e("tttt", "addData: " + data);
        int error = data.getErrorCode();
        // 是否有异常情况
        if (error != 0) {
            recognize.addError();
        }
        float sampleRate = data.getSampleRate();
        if (!data.isAuto() && data.isHaveData()) {
            recognize.setSampleRate(sampleRate);
            short[] zippedData = data.getUnZippedData();
            int numberOfPw = recognize.getResultSize();
            callback.drawData(new ManualPwData(zippedData, numberOfPw, sampleRate, isMeasuring,error));
            result = recognize.addData(zippedData);
            if (result == IRecognize.COMPLETE) {
                short[] peakValley = recognize.getMaxAndMinPoint();
                List<Object> list = recognize.getResult();
                Object[] arr = new Object[list.size()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = list.get(i);
                }
                callback.pwSuccess(arr, sampleRate, peakValley);
            } else if (result == IRecognize.FAULT) {
                callback.timeout();
            }
        }
    }

    public boolean isFinish() {
        return result == IRecognize.COMPLETE;
    }


    public interface PwCallback {

        /**
         * 绘图
         *
         * @param data 解压数据
         */
        void drawData(ManualPwData data);

        /**
         * 采谱成功
         *
         * @param result 整理后的数据
         */
        void pwSuccess(Object[] result, float sample, short[] peakValley);

        /**
         * 超时
         */
        void timeout();
    }

}
