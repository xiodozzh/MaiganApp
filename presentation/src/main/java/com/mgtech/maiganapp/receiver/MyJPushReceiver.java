package com.mgtech.maiganapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.mgtech.domain.entity.net.response.PushEntity;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.activity.ExceptionRecordActivity;
import com.mgtech.maiganapp.activity.FriendDealRequestActivity;
import com.mgtech.maiganapp.activity.HealthKnowledgeActivity;
import com.mgtech.maiganapp.activity.MainActivity;
import com.mgtech.maiganapp.activity.MeasurePwActivity;
import com.mgtech.maiganapp.activity.MedicationReminderActivity;
import com.mgtech.maiganapp.activity.WeeklyReportActivity;
import com.mgtech.maiganapp.service.NetJobService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * @author zhaixiang
 */
public class MyJPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private static final int ABNORMAL_REMIND = 8;
    private static final int FRIEND_REQUEST = 1;
    private static final int FRIEND_REQUEST_AGREE = 2;
    private static final int CONTACT_REGISTER = 3;
    private static final int MEASURE_REMIND = 4;
    private static final int MEDICATION_REMIND = 5;
    private static final int WEEK_REPORT = 6;
    private static final int WEEK_ARTICAL = 7;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.i(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.i(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//        	processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.i(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            NetJobService.enqueueWork(context,NetJobService.getCallingIntent(context,NetJobService.TYPE_GET_UNREAD_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuffer sb = new StringBuffer();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ",planId  value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");


                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON isPairFail!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.e(TAG, "processCustomMessage: " + extra);
        if (TextUtils.isEmpty(extra)) {
            return;
        }
        try {
            Gson gson = new Gson();
            PushEntity entity = gson.fromJson(extra, PushEntity.class);
            if (entity == null) {
                return;
            }
            PushEntity.DataBean dataBean = null;
            if (!TextUtils.isEmpty(entity.getData())){
                dataBean = gson.fromJson(entity.getData(),PushEntity.DataBean.class);
            }
            Intent intent = null;
            switch (entity.getType()) {
                case ABNORMAL_REMIND:
                    String id;
                    if (dataBean != null){
                        id = dataBean.getUserGuid();
                    }else{
                        id = SaveUtils.getUserId(context);
                    }
                    intent = ExceptionRecordActivity.getCallingIntent(context, id);
                    break;
                case FRIEND_REQUEST:
                    intent = FriendDealRequestActivity.getCallingIntent(context);
                    break;
                case FRIEND_REQUEST_AGREE:
                    intent = MainActivity.getCallingIntent(context);
                    intent.putExtra(MainActivity.EXTRA_POSITION, MainActivity.PAGE_FRIEND);
                    break;
                case CONTACT_REGISTER:
                    intent = MainActivity.getCallingIntent(context);
                    break;
                case MEASURE_REMIND:
                    intent = MeasurePwActivity.getCallingIntent(context);
                    break;
                case MEDICATION_REMIND:
                    intent = MedicationReminderActivity.getCallingIntent(context);
                    break;
                case WEEK_REPORT:
                    if (dataBean == null){
                        return;
                    }
                    intent = WeeklyReportActivity.getCallingIntent(context,dataBean.getContentLink()+"&accountGuid="+
                            SaveUtils.getUserId(context),dataBean.getShareTitle(),dataBean.getShareSubTitle(),dataBean.getContentGuid());
                    break;
                case WEEK_ARTICAL:
                    if (dataBean == null){
                        return;
                    }
                    intent = HealthKnowledgeActivity.getCallingIntent(context,dataBean.getContentLink(),dataBean.getContentGuid());
                    break;
                default:
                    intent = MainActivity.getCallingIntent(context);
            }
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
