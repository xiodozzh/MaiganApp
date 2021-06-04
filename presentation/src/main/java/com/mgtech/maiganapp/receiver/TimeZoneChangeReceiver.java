package com.mgtech.maiganapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;

import java.util.TimeZone;

import static android.content.Intent.ACTION_TIMEZONE_CHANGED;

/**
 * Created by zhaixiang on 2017/12/22.
 * 时区变化
 */

public class TimeZoneChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
            Log.e("BroadcastReceiver", "---TIMEZONE_CHANGED!---");
            IBraceletInfoManager manager = new BraceletInfoManagerBuilder(context).create();
            manager.setIsStepHistorySyncToBracelet(false);
            manager.setTimeZone(TimeZone.getDefault().getRawOffset() / 1000 / 3600);
        }
    }
}
