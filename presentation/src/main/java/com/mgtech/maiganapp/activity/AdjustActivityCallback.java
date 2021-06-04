package com.mgtech.maiganapp.activity;

/**
 * Created by zhaixiang on 2017/4/25.
 * 校准Activity回调
 */

public interface AdjustActivityCallback {
//    void showDrawingPage(boolean show);

    void showData(short[] data);

    void showMovementError();

    void showProgress(int progress);

    void showState(int state);

//    void clearScreen();
}
