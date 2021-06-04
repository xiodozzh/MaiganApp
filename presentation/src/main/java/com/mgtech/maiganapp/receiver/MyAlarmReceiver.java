package com.mgtech.maiganapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.activity.MedicationReminderActivity;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.Locale;

public class MyAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("receive", "onReceive: MyAlarmReceiver" );
//        String medicinalName = intent.getStringExtra("name");
//        String dosage = intent.getStringExtra("dosage");
//        Log.e("receive", "onReceive: " + medicinalName + dosage );
//
//        Intent reIntent = MedicationReminderActivity.getCallingIntent(context);
//        reIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        ViewUtils.showNotification(context, reIntent,
//                 context.getString(R.string.medicine_time), String.format(Locale.getDefault(),context.getString(R.string.format_take_medicine_time),medicinalName,dosage) ,
//                medicinalName==null?MyConstant.NOTIFICATION_MEDICINE_REMINDER_ID
//                        :medicinalName.hashCode());

    }

}
