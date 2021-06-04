package com.mgtech.maiganapp.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;

public class MyDaemonService extends Service {
    /**
     * myservice被销毁com.njrgrj.app.service.myservice.destroy
     */
    public final static String SERVICE_DESTROY = "com.njrgrj.app.service.myservice.destroy";

    @Override
    public void onCreate() {
        super.onCreate();
        //开启服务
//        boolean flag = isServiceWork(this, BluetoothMeasureService.class.getName());
//        Log.i("MyDaemonService", "MyService flag is " + flag);
//        if (!flag) {
//            startService(BluetoothMeasureService.getCallingIntent(this));
//        }
//
//        //监听myservice的destroy状态
//        PackageManager manager = getPackageManager();
//        //要查找的BroadCastReceiver
//        Intent intent = new Intent(SERVICE_DESTROY);
//        List<ResolveInfo> resolveInfos = manager.queryBroadcastReceivers(intent, PackageManager.GET_INTENT_FILTERS);
//        if (resolveInfos.size() == 0) {
//            registerReceiver(new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context arg0, Intent arg1) {
//                    if (SERVICE_DESTROY.equals(arg1.getAction())) {
//                        startService(BluetoothMeasureService.getCallingIntent(getApplicationContext()));
//                    }
//                }
//            }, myIntentFilter());
//        }
    }

    private IntentFilter myIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SERVICE_DESTROY);
        return intentFilter;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

}
