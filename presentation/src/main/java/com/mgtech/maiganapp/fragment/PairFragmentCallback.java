package com.mgtech.maiganapp.fragment;

/**
 * Created by zhaixiang on 2017/4/15.
 * 绑定Fragment回调
 */

public interface PairFragmentCallback {
    /**
     * 下一步
     * @param fragmentIndex fragment planId
     */
    void nextStep(int fragmentIndex);

    /**
     * 取消校验
     */
    void cancelAdjust();

    /**
     * 开始用脉图识别检测
     */
    void startAdjust(boolean usePwRecognize);

    /**
     * 开始扫描，用于绑定
     */
    void startPairScan();
}
