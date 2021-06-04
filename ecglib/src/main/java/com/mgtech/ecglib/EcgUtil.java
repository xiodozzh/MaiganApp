package com.mgtech.ecglib;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * Created by zhaixiang on 2017/8/21.
 * ECG 滤波和识别
 */

public class EcgUtil {
    private static final String TAG = "ecgUtil";
    private List<Double> rawData;
    private Callback callback;
    private double sampleRate;
    private static final int POINT_NUMBER = 128;
    private static final int HR_MAX = 110;
    private static final int HR_MIN = 35;
    private static final int RR_NUMBER = 3;
    private LinkedList<Double> detectQueue;
    private boolean isDetecting;

    public EcgUtil(Callback callback) {
        this.callback = callback;
        this.rawData = new ArrayList<>();
        this.detectQueue = new LinkedList<>();
    }

    public interface Callback {
        void onFilter(double[] data);
        void isDetect(double[] result);
    }

    public void setSampleRate(double sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void addData(short[] ecgData, float sampleRate) {
        for (int i = 0; i < ecgData.length; i++) {
            rawData.add((double) ecgData[i]);
            int size = rawData.size();
            int index = size / POINT_NUMBER;
            if (index >= 2 && size % POINT_NUMBER == 0) {
                double[] data = new double[POINT_NUMBER * 2];
                for (int j = 0; j < POINT_NUMBER * 2; j++) {
                    data[j] = rawData.get(size - 2 * POINT_NUMBER + j);
                }
                for (int k = 0; k < POINT_NUMBER; k++) {
                    rawData.remove(0);
                }

                double[] r = filter(data, data.length, sampleRate);
                if (callback != null) {
                    double[] re = new double[r.length/2];
                    System.arraycopy(r,r.length/2,re,0,re.length);
                    callback.onFilter(re);
                }
            }
        }
    }

    private String listToString(List list){
        StringBuilder s = new StringBuilder();
        for (Object o :list) {
            s.append(o);
            s.append(",");
        }
        return s.toString();
    }

    public void detect(short[] ecgData, final double sampleRate) {
        for (short i : ecgData) {
            detectQueue.add((double) i);
        }
        int needSize = (int) Math.round((RR_NUMBER + 1) * (sampleRate * 60) / HR_MIN);
        if (detectQueue.size() < needSize) {
            return;
        }
        while (detectQueue.size() > needSize) {
            detectQueue.removeFirst();
        }
        final double[] data = new double[needSize];
        for (int i = 0; i < data.length; i++) {
            data[i] = detectQueue.get(i);
        }
        if (isDetecting){
            return;
        }
        isDetecting = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                double[] result = detectEcg(data, data.length, sampleRate, HR_MIN, HR_MAX, RR_NUMBER);
                boolean isDetect = result[0] == 0;
                if (isDetect && callback != null) {
                    callback.isDetect(result);
                }
                isDetecting = false;
            }
        }).start();
    }

    private static final String SELF_LIB_NAME = "ecg-lib";

    static {
        System.loadLibrary(SELF_LIB_NAME);
    }

    /**
     * 滤波
     *
     * @param ecgData    原始数据
     * @param ecgSize    数据大小
     * @param sampleRate 采样率
     * @return 滤波后的数据
     */
    private static native double[] filter(double[] ecgData, int ecgSize, double sampleRate);

    /**
     * 识别
     *
     * @param ecgData    原始数据
     * @param ecgSize    数据大小
     * @param sampleRate 采样率
     * @param hrMin      心率下限
     * @param hrMax      心率上限
     * @param rrNum      识别R-R波数量
     * @return 是否识别成功
     */
    private static native boolean detect(double[] ecgData, int ecgSize, double sampleRate, double hrMin, double hrMax, double rrNum);

    /**
     *
     * @param ecgData    原始数据
     * @param ecgSize    数据大小
     * @param sampleRate 采样率
     * @param hrMin      心率下限
     * @param hrMax      心率上限
     * @param rrNum      识别R-R波数量
     * @return double[5]: double[0]==0时识别成功，double[1]为rrNum,double[2]为心率平均值，double[3]为最大值，double[4]为最小值
     */
    private static native double[] detectEcg(double[] ecgData, int ecgSize, double sampleRate, double hrMin, double hrMax, double rrNum);

    public static native String stringFromJNI();
}
