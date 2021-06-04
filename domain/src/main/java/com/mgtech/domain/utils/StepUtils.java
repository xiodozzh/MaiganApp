package com.mgtech.domain.utils;

import android.content.Context;

import com.mgtech.domain.entity.UserInfo;

/**
 * Created by zhaixiang on 2017/11/16.
 * 计步运动相关计算工具
 */

public class StepUtils {
    public static float calculateDistanceFromStep(int step, Context context) {
        UserInfo userInfo = UserInfo.getLocalUserInfo(context);
        if (userInfo == null) {
            return 0;
        }
        float height = userInfo.getHeight();
        return calculateDistance(step,height);
    }

    public static float calculateCalorieFromStep(int step, Context context) {
        UserInfo userInfo = UserInfo.getLocalUserInfo(context);
        if (userInfo == null) {
            return 0;
        }
        float height = userInfo.getHeight();
        float weight = userInfo.getWeight();
        return calculateCalorie(step,height,weight);
    }

    private static float calculateDistance(int step, float height){
        return step * height / 100 / 3;
    }

    private static float calculateCalorie(int step, float height, float weight){
        return calculateDistance(step,height)* weight * 1.036f ;
    }
}
