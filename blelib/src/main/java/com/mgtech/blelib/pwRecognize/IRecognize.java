package com.mgtech.blelib.pwRecognize;

import java.util.List;

/**
 * Created by zhaixiang on 2017/6/26.
 */

public interface IRecognize {
    String TAG = "RECG";
    int COMPLETE = 0;
    int FAULT = 2;
    int CONTINUE = 1;

    interface Callback {
        void onSectionRemove();
    }

    int addData(short[] data);

    void addError();

    short[] getMaxAndMinPoint();

    List<Short> getRawData();

    List<Object> getResult();

//    List<Integer> getPureResult();

    int getResultSize();

    float getTotalHeartRate();

    void setSampleRate(float sampleRate);

    void setComplete(int completePwMin, int completePwMax);

    void setReservedPwNumber(int reservedNumber);

    void setTimeOut(int timeOut);

    void setCallback(IRecognize.Callback callback);

    void clear();
}
