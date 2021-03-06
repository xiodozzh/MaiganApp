package com.mgtech.maiganapp.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.mgtech.blelib.biz.AbstractBleResponseCallback;
import com.mgtech.blelib.biz.BleRequest;
import com.mgtech.blelib.biz.BleResponse;
import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.constant.BleConstant;
import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.BleStateEvent;
import com.mgtech.blelib.entity.BluetoothOrder;
import com.mgtech.blelib.entity.CurrentStepData;
import com.mgtech.blelib.entity.FirmStateData;
import com.mgtech.blelib.entity.FirmwareInfoData;
import com.mgtech.blelib.entity.PhoneEvent;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.entity.SampleCompleteData;
import com.mgtech.blelib.entity.StepHistory;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.blelib.utils.StepUtils;
import com.mgtech.blelib.utils.Utils;
import com.mgtech.data.net.socket.SocketConnection;
import com.mgtech.domain.entity.AppConfigNew;
import com.mgtech.domain.entity.SocketListener;
import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.entity.net.request.CheckPhoneInfoRequestEntity;
import com.mgtech.domain.entity.net.request.GetStepRequestEntity;
import com.mgtech.domain.entity.net.request.SaveStepRequestEntity;
import com.mgtech.domain.entity.net.response.CheckPhoneInfoResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.PwDataResponseEntity;
import com.mgtech.domain.entity.net.response.StepDetailItemEntity;
import com.mgtech.domain.entity.socket.SocketAutoPwEntity;
import com.mgtech.domain.entity.socket.SocketEntity;
import com.mgtech.domain.entity.socket.SocketMsgEntity;
import com.mgtech.domain.interactor.DataUseCase;
import com.mgtech.domain.interactor.SingleSignOnUseCase;
import com.mgtech.domain.interactor.StepUseCase;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.BleApplication;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.activity.MainActivity;
import com.mgtech.maiganapp.data.event.AppForegroundEvent;
import com.mgtech.maiganapp.data.event.AutoPwCalculatedEvent;
import com.mgtech.maiganapp.data.event.FirmwareUpgradeEvent;
import com.mgtech.maiganapp.data.event.LogoutEvent;
import com.mgtech.maiganapp.data.event.SocketEvent;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.data.model.StepModel;
import com.mgtech.maiganapp.data.wrapper.IndicatorDataWrapper;
import com.mgtech.maiganapp.data.wrapper.StepModelWrapper;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;


/**
 * @author zhaixiang
 * ???????????????Service???????????????????????????????????????????????????
 * ???????????????????????????????????????????????????????????????????????????????????????????????????
 * ??????onStart????????????
 */

public class BluetoothService extends Service {
    private String notificationAlarmTitle;
    private String notificationAlarmContent;

    private static final String TAG = "bluetoothService";
    private IBraceletInfoManager manager;
    private MyReceiver receiver;

    private DataUseCase dataUseCase;
    private StepUseCase stepUseCase;
    private SocketConnection connection;
    private SingleSignOnUseCase singleSignOnUseCase;
    private volatile boolean shouldSocketConnect = false;
    private static final long RE_CONNECT_TIME = 2000;
    private static final int RE_CONNECT_INDEX_MAX = 12;
    private static final int RE_CONNECT_INDEX_MIN = 1;
    private int reConnectCount = RE_CONNECT_INDEX_MIN;
    private long lastTimeToLoadTodayStep = 0L;

    private static Handler mHandler = new Handler();
    /**
     * ??????????????????
     */
    private static final long SCAN_TIME_SPAN = 30 * 60 * 1000;
    /**
     * ??????????????????
     */
    private static final long SCAN_TIME = 60 * 1000;
    private boolean appForeground;
    private BleRequest bleRequest;
    private BleResponse bleResponse;
    private Subscription subscription;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i("BluetoothService onCreate");
        notificationAlarmTitle = getString(R.string.notification);
        notificationAlarmContent = getString(R.string.bracelet_need_bond_again);
        manager = new BraceletInfoManagerBuilder(this).create();
        bleRequest = ((BleApplication) getApplication()).getRequest();
        bleResponse = ((BleApplication) getApplication()).getBleResponse();
        subscription = bleResponse.setCallback(new BleResponseCallback());
        EventBus.getDefault().register(this);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BleConstant.ACTIVITY_ACTION);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
        singleSignOnUseCase = ((MyApplication) getApplication()).getSingleSignOnUseCase();
        stepUseCase = ((MyApplication) getApplication()).getStepUseCase();
        setScanTime();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "service on start command");
//        setScanTime();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * ??????????????????
     */
    private void setScanTime() {
        long pairTime = manager.getPairedTime();
        if (pairTime == 0) {
            return;
        }
        Calendar currentCalendar = Calendar.getInstance();
        long current = currentCalendar.getTimeInMillis();
        int scanInTime = (int) (SCAN_TIME_SPAN - (current - pairTime) % SCAN_TIME_SPAN);
        Log.e(TAG, "setScanTime: " + scanInTime);
        mHandler.removeCallbacks(startAutoScanRunnable);
        mHandler.postDelayed(startAutoScanRunnable, scanInTime);
    }

    private Runnable startAutoScanRunnable = new Runnable() {
        @Override
        public void run() {
            bleRequest.linkIfAvailable();
            mHandler.postDelayed(stopScanRunnable, SCAN_TIME);
            setScanTime();
        }
    };


    private Runnable stopScanRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "stopScanRunnable run ");
            bleRequest.stopScan();
            setScanTime();
        }
    };


    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        bleRequest.disconnect();
        if (dataUseCase != null) {
            dataUseCase.unSubscribe();
        }
        if (stepUseCase != null) {
            stepUseCase.unSubscribe();
        }
        if (singleSignOnUseCase != null) {
            singleSignOnUseCase.unSubscribe();
        }
        bleResponse.removeCallback(subscription);
        EventBus.getDefault().unregister(this);
        unregisterReceiver(receiver);
        mHandler.removeCallbacks(checkPhoneValidRunnable);
        mHandler.removeCallbacks(socketHeartBeatRunnable);
        mHandler.removeCallbacks(stopScanRunnable);
        mHandler.removeCallbacks(startAutoScanRunnable);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                Log.e(TAG, "onReceive: ACTION_SCREEN_OFF");
                EventBus.getDefault().post(new PhoneEvent(BleStatusConstant.PHONE_SCREEN_OFF));
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                Log.e(TAG, "onReceive: HOME_CLICK");
                EventBus.getDefault().post(new PhoneEvent(BleStatusConstant.PHONE_BACK_HOME));
            } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
                Log.e(TAG, "onReceive: ACTION_SCREEN_ON");
            } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                Log.i("mark", "????????????????????????");
                if (isThereInternetConnection(context)) {
                    Log.i("mark", "??????????????????");
                    shouldSocketConnect = true;
                    checkAndConnectSocket();
                } else {
                    Log.i("mark", "??????????????????");
                    disConnectSocket();
                }
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int test = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (test) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.e("tag", "STATE_OFF ??????????????????");
                        EventBus.getDefault().post(new BleStateEvent(BleStateEvent.STATE_OFF));
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.e("tag", "STATE_TURNING_OFF ????????????????????????");
                        EventBus.getDefault().post(new BleStateEvent(BleStateEvent.STATE_TURNING_OFF));
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.e("tag", "STATE_ON ??????????????????");
                        EventBus.getDefault().post(new BleStateEvent(BleStateEvent.STATE_ON));
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.e("tag", "STATE_TURNING_ON ????????????????????????");
                        EventBus.getDefault().post(new BleStateEvent(BleStateEvent.STATE_TURNING_ON));
                        break;
                    default:
                }
            }
        }
    }

    class BleResponseCallback extends AbstractBleResponseCallback {

        @Override
        protected void onLinkStatusReceived(boolean isConnected) {
        }

        @Override
        protected void onBluetoothStatusReceived(int statusType) {
            super.onBluetoothStatusReceived(statusType);
            if (statusType == BleStatusConstant.STATUS_SLEEP) {
                Log.i(TAG, "app????????????: " + appForeground);
                if (!appForeground) {
                    bleRequest.disconnectIfNotWorking();
                }
                long now = Calendar.getInstance().getTimeInMillis();
                if (!manager.isStepHistorySyncToBracelet() && now - lastTimeToLoadTodayStep > 300_000) {
                    lastTimeToLoadTodayStep = now;
                    getCurrentStepDataFromNet();
                    return;
                }
                if (isDeviceNeedUpgrade()) {
                    Log.i(TAG, "isDeviceNeedUpgrade: ser");
                    EventBus.getDefault().post(new FirmwareUpgradeEvent());
                    return;
                }
                if (Utils.doesReminderHaveZoneAndIntervalSetFunc(manager.getDeviceProtocolVersion())) {
                    if (!manager.getReminderData().isTimeZoneSet()) {
                        Logger.e("?????????");
                        ReminderData data = manager.getReminderData();
                        Utils.updateReminder(data);
                        manager.setReminder(data);
                        bleRequest.getOrder(new BluetoothOrder(BleConstant.CODE_SET_ALERT_REMINDER, manager.getReminderData()));
                    }
                } else {
                    if (manager.getReminderData().isTimeZoneSet()) {
                        Logger.e("??????????????? ???????????????");
                        ReminderData data = manager.getReminderData();
                        Utils.backReminder(data);
                        manager.setReminder(data);
                        bleRequest.getOrder(new BluetoothOrder(BleConstant.CODE_SET_ALERT_REMINDER, manager.getReminderData()));
                    }
                }
            } else if (statusType == BleStatusConstant.STATUS_PAIR_CODE_DIFFERENT) {
                notificationPairCodeChangedPopup();
            } else if (statusType == BleStatusConstant.STATUS_SET_ALERT_REMINDERS_SUCCESS) {
                SaveUtils.setRemindersNeedUpdate(getApplication(), true);
                NetJobService.enqueueWork(BluetoothService.this, NetJobService.getCallingIntent(BluetoothService.this, NetJobService.TYPE_SAVE_REMINDERS));
            }
        }

        @Override
        protected void onAutoPWCompleteDataReceived(SampleCompleteData data) {
            super.onAutoPWCompleteDataReceived(data);
            saveAutoPwDataToNet(data.getTime(), data.getData());
        }


        @Override
        protected void onFirmwareInfo(FirmwareInfoData data) {
            super.onFirmwareInfo(data);
            NetJobService.enqueueWork(BluetoothService.this, NetJobService.getCallingIntent(BluetoothService.this, NetJobService.TYPE_SET_BRACELET_INFO));
        }

        @Override
        protected void onStep(StepHistory stepHistory) {
            super.onStep(stepHistory);
            dealWithStepHistory(stepHistory);
        }

        @Override
        protected void onFirmwareUpgrade(FirmStateData data) {
            super.onFirmwareUpgrade(data);
            switch (data.getStatus()) {
                case FirmStateData.START:
                    launchTimeOutRunnable();
                    break;
                case FirmStateData.CONTINUE:
//                     if (data.getProgress() == data.getTotal()) {
//                         upgradeSuccess = true;
//                     }
                    mHandler.removeCallbacks(timeOutRunnable);
                    launchTimeOutRunnable();
                    break;
                case FirmStateData.END:
                    mHandler.removeCallbacks(timeOutRunnable);
//                     this.upgradeSuccess = true;
                    bleRequest.bluetoothDisconnect();
                    break;
                default:
                    break;
            }
        }
    }

    private Runnable timeOutRunnable = new Runnable() {
        @Override
        public void run() {
            Logger.i("????????????");
            bleRequest.bluetoothDisconnect();
        }
    };

    private void launchTimeOutRunnable() {
        mHandler.postDelayed(timeOutRunnable, 3000);
    }

    /**
     * ??????????????????
     *
     * @return true ????????????false ?????????
     */
    public boolean isBleOn() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter == null || mBluetoothAdapter.isEnabled();
    }


    /**
     * ????????????????????????
     *
     * @return ???????????????????????????????????????????????????????????????????????????20????????????????????????????????????
     */
    private boolean isDeviceNeedUpgrade() {
        return AppConfigNew.shouldAutoUpgradeBracelet(
                getApplication(),
                manager.getDeviceFirmwareVersionString(),
                manager.getDeviceName(), manager.getPower(),
                manager.getPowerStatus() == IBraceletInfoManager.POWER_CHARGING, false);
    }

    private void saveAutoPwDataToNet(long time, List<Object> data) {
        if (dataUseCase == null) {
            dataUseCase = ((MyApplication) getApplication()).getDataUseCase();
        }
        UserInfo userInfo = UserInfo.getLocalUserInfo(this);
        this.dataUseCase.calculateAutoData(this, data, time, userInfo.getAlgrithomSex(),
                Math.round(userInfo.getHeight()), Math.round(userInfo.getWeight()), userInfo.getAge(this),
                manager.getAddress(), manager.getDeviceProtocolVersionString(), manager.getDeviceFirmwareVersionString(),
                new Subscriber<NetResponseEntity<PwDataResponseEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(NetResponseEntity<PwDataResponseEntity> netResponseEntity) {
                        Log.i(TAG, "onNext: " + netResponseEntity);
                    }
                });
    }


    /**
     * ???????????????????????????
     */
    private void getCurrentStepDataFromNet() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, 1);
        long endTime = calendar.getTimeInMillis();
        GetStepRequestEntity entity = new GetStepRequestEntity(SaveUtils.getUserId(this),
                GetStepRequestEntity.DAY, startTime, endTime);
        stepUseCase.getStepDetail(NetConstant.NO_CACHE, entity, new Subscriber<NetResponseEntity<List<StepDetailItemEntity>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(NetResponseEntity<List<StepDetailItemEntity>> netResponseEntity) {
                Logger.i("getCurrentStepDataFromNet: " + netResponseEntity);
                if (netResponseEntity.getCode() == 0) {
                    List<StepDetailItemEntity> list = netResponseEntity.getData();
                    if (list != null) {
                        List<StepModel> stepModels = StepModelWrapper.getModelListFromNet(list);
                        if (stepModels.size() == 1) {
                            StepModel model = stepModels.get(0);
                            int currentHour = (int) (model.startTime / 1000L / 3600);
                            CurrentStepData data = new CurrentStepData();

                            data.setHour(currentHour);
                            data.setQuarterOffset(0);
                            data.setDayStep(model.stepCount);
                            data.setQuarterStep(0);
                            data.setDayMinute(model.duration);
                            data.setQuarterMinute(0);
                            Logger.i("getCurrentStepDataFromNet: " + data);
                            bleRequest.getOrder(new BluetoothOrder(BleConstant.CODE_CALIBRATE_STEP_DATA, data));
                        }
                    }
                }
            }
        });
    }


    /**
     * ????????????????????????
     *
     * @param stepHistory ????????????
     */
    private void dealWithStepHistory(StepHistory stepHistory) {
        Log.e(TAG, "dealWithStepHistory: " + stepHistory);
        List<SaveStepRequestEntity.StepBean> list = new ArrayList<>();
        List<StepHistory.HourData> data = stepHistory.getHourDataList();
        if (data == null || data.isEmpty()) {
            return;
        }
        long syncTime = 0;
        int height = Math.round(UserInfo.getLocalUserInfo(this).getHeight());
        int weight = Math.round(UserInfo.getLocalUserInfo(this).getWeight());
        for (StepHistory.HourData hourData : data) {
            long hour = hourData.getStartHour();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(hour * 3600_000);
            SaveStepRequestEntity.StepBean entity1 = generateSaveStepItemEntity(calendar, hourData.getStepInQuarter1(), hourData.getSportInQuarter1(), height, weight);

            calendar.add(Calendar.MINUTE, 15);
            SaveStepRequestEntity.StepBean entity2 = generateSaveStepItemEntity(calendar, hourData.getStepInQuarter2(), hourData.getSportInQuarter2(), height, weight);

            calendar.add(Calendar.MINUTE, 15);
            SaveStepRequestEntity.StepBean entity3 = generateSaveStepItemEntity(calendar, hourData.getStepInQuarter3(), hourData.getSportInQuarter3(), height, weight);

            calendar.add(Calendar.MINUTE, 15);
            SaveStepRequestEntity.StepBean entity4 = generateSaveStepItemEntity(calendar, hourData.getStepInQuarter4(), hourData.getSportInQuarter4(), height, weight);

            calendar.add(Calendar.MINUTE, 15);
            syncTime = calendar.getTimeInMillis();

            // ?????????????????????????????????
            if (entity1.getDuration() != 0 || entity1.getStepCount() != 0) {
                list.add(entity1);
            }
            if (entity2.getDuration() != 0 || entity2.getStepCount() != 0) {
                list.add(entity2);
            }
            if (entity3.getDuration() != 0 || entity3.getStepCount() != 0) {
                list.add(entity3);
            }
            if (entity4.getDuration() != 0 || entity4.getStepCount() != 0) {
                list.add(entity4);
            }
        }
        if (list.isEmpty()) {
            // ????????????????????????????????????????????????
            manager.saveStepSyncTime(syncTime);
            return;
        }
        saveStepData(list, syncTime);
    }

    /**
     * ??????SaveStepEntity
     *
     * @param calendar ??????
     * @param step     ??????
     * @param minute   ????????????
     * @return SaveStepItemEntity
     */
    private SaveStepRequestEntity.StepBean generateSaveStepItemEntity(Calendar calendar, int step, int minute, int height, int weight) {
        SaveStepRequestEntity.StepBean entity = new SaveStepRequestEntity.StepBean();
        entity.setDuration(minute);
        entity.setStepCount(step);
        entity.setHeat(StepUtils.calculateCalorie(step, height, weight));
        entity.setDistance(StepUtils.calculateDistance(step, height));
        entity.setStartTime(calendar.getTimeInMillis());
        entity.setEndTime(calendar.getTimeInMillis() + 15 * 60_000);
        return entity;
    }

    private void saveStepData(List<SaveStepRequestEntity.StepBean> list, final long t) {
        SaveStepRequestEntity entity = new SaveStepRequestEntity(list, SaveUtils.getUserId(getApplicationContext()));
        stepUseCase.saveStep(entity, new Subscriber<NetResponseEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(NetResponseEntity netResponseEntity) {
                if (netResponseEntity != null && netResponseEntity.getCode() == 0) {
                    manager.saveStepSyncTime(t);
                }
                unsubscribe();
            }
        });
    }

    /**
     * ??????????????????(??????22???00?????????7???00?????????)
     */
    private void notificationPairCodeChangedPopup() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Calendar calendar = Calendar.getInstance();
//        startService(NetService.getCallingIntent(this,NetService.TYPE_UN_BIND));
        NetJobService.enqueueWork(this, NetJobService.getCallingIntent(this, NetJobService.TYPE_UN_BIND));
        if (calendar.get(Calendar.HOUR_OF_DAY) < 22 && calendar.get(Calendar.HOUR_OF_DAY) > 7) {
            ViewUtils.showNotification(this, notificationIntent, notificationAlarmTitle, notificationAlarmContent,
                    ViewUtils.NOTIFICATION_DISCONNECT_ID);
        } else {
            ViewUtils.showNotification(this, notificationIntent, notificationAlarmTitle, notificationAlarmContent,
                    ViewUtils.NOTIFICATION_DISCONNECT_ID);
        }
    }

    /**
     * ????????????
     *
     * @param context ?????????
     */
    public static void startBluetoothService(Context context) {
        Intent intent = new Intent(context, BluetoothService.class);
        context.startService(intent);
    }

    /**
     * ????????????
     *
     * @param context ?????????
     */
    public static void stopMeasureService(Context context) {
        Log.e(TAG, "stopMeasureAndLeave: ");
        context.stopService(getCallingIntent(context));
    }

    public static boolean isServiceOn(Context context) {
        return ViewUtils.isServiceRunning(context, BluetoothService.class.getName());
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BluetoothService.class);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    public void appGoToForeground(AppForegroundEvent event) {
        appForeground = event.isForeground();
        if (appForeground) {
            NetJobService.enqueueWork(this, NetJobService.getCallingIntent(this, NetJobService.TYPE_GET_CONFIG));
            NetJobService.enqueueWork(this, NetJobService.getCallingIntent(this, NetJobService.TYPE_INDICATOR_PARAMETERS));
        } else if (isBleOn()) {
            bleRequest.disconnectIfNotWorking();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    public void receiveSocketEvent(SocketEvent event) {
        Log.i("socket", "receiveSocketEvent: ");
        EventBus.getDefault().removeStickyEvent(event);
        if (event.isLink()) {
            shouldSocketConnect = true;
            checkAndConnectSocket();

        } else {
            disConnectSocket();
        }
    }


    /**
     * ??????socket
     */
    private void checkAndConnectSocket() {
        mHandler.removeCallbacks(checkPhoneValidRunnable);
        mHandler.post(checkPhoneValidRunnable);
    }

    private Runnable socketHeartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (connection != null && connection.isSocketConnectOn()) {
                connection.send("{" +
                        "\"req\":1," +
                        "\"time\":" + System.currentTimeMillis() +
                        "}");
                mHandler.postDelayed(socketHeartBeatRunnable, 60 * 1000);
            } else {
//                checkPhone();
            }
        }
    };

    private Runnable checkPhoneValidRunnable = new Runnable() {
        @Override
        public void run() {
            checkPhone();
        }
    };

    private synchronized void connectSocket() {
        final String id = SaveUtils.getUserId(this);
        if (TextUtils.isEmpty(id)) {
            return;
        }
        if (connection != null) {
            if (connection.isSocketConnectOn()) {
                Log.i(TAG, "connectSocket: " + true);
                return;
            }
            Log.i(TAG, "connectSocket: " + false);
            connection.close();
        }
        Log.i(TAG, "connectSocket: first" + false);
        connection = SocketConnection.getInstance(String.format(HttpApi.API_SOCKET_URL +
                        "/createConnection?accountGuid=%s&deviceId=%s&platform=%s",
                id, com.mgtech.domain.utils.Utils.getPhoneMac(getApplication()), "android"), socketListener);
        Log.i("socket", String.format(HttpApi.API_SOCKET_URL +
                        "/createConnection?accountGuid=%s&deviceId=%s&platform=%s",
                id, com.mgtech.domain.utils.Utils.getPhoneMac(getApplication()), "android") + connection);
        connection.connect();
    }

    private SocketListener socketListener = new SocketListener() {
        @Override
        public void onOpen() {
            Log.e("socket", "open: ");
            reConnectCount = RE_CONNECT_INDEX_MIN;
            mHandler.postDelayed(socketHeartBeatRunnable, 5_000);
        }

        @Override
        public void onMessage(String msg) {
            Log.e("socket", "onMessage: " + msg);
            reConnectCount = RE_CONNECT_INDEX_MIN;
            Gson gson = new Gson();
            SocketEntity entity = gson.fromJson(msg, SocketEntity.class);
            Log.i("socket", "onMessage: " + entity);
            if ("AccountManager".equals(entity.getModule())) {
                SocketMsgEntity socketMsgEntity = gson.fromJson(msg, SocketMsgEntity.class);
                SocketMsgEntity.DataBean dataBean = socketMsgEntity.getData();
                if (dataBean != null) {
                    if (dataBean.getIsLoginOnOtherDevice() == 1) {
                        Log.e("socket", "onMessage: logout");
                        EventBus.getDefault().postSticky(new LogoutEvent(dataBean.getPhoneName()));
                        disConnectSocket();
                        mHandler.removeCallbacks(socketHeartBeatRunnable);
                    }
                }
            } else if ("PW".equals(entity.getModule())) {
                SocketAutoPwEntity socketAutoPwEntity = gson.fromJson(msg, SocketAutoPwEntity.class);
                PwDataResponseEntity dataEntity = socketAutoPwEntity.getData();
                if (dataEntity != null) {
                    IndicatorDataModel model = IndicatorDataWrapper.getModelFromPwDataResponseEntity(dataEntity);
                    EventBus.getDefault().post(new AutoPwCalculatedEvent());
                    NetJobService.enqueueWork(BluetoothService.this,
                            NetJobService.getCallingIntent(BluetoothService.this, NetJobService.TYPE_GET_UNREAD_MESSAGE));
                }
            }

        }

        @Override
        public void onClose() {
            Log.e("socket", "close: ");
            mHandler.removeCallbacks(checkPhoneValidRunnable);
            mHandler.removeCallbacks(socketHeartBeatRunnable);
            mHandler.postDelayed(checkPhoneValidRunnable, RE_CONNECT_TIME * reConnectCount);
        }

        @Override
        public void onFail() {
            Log.e("socket", "fail: ");
            mHandler.removeCallbacks(checkPhoneValidRunnable);
            mHandler.removeCallbacks(socketHeartBeatRunnable);
            mHandler.postDelayed(checkPhoneValidRunnable, RE_CONNECT_TIME * reConnectCount);
            reConnectCount++;
            reConnectCount = Math.min(RE_CONNECT_INDEX_MAX, reConnectCount);
//            socketConnectOn = false;
        }
    };

    private void checkPhone() {
        if (!shouldSocketConnect) {
            return;
        }
        mHandler.removeCallbacks(checkPhoneValidRunnable);
        CheckPhoneInfoRequestEntity entity = new CheckPhoneInfoRequestEntity(
                com.mgtech.domain.utils.Utils.getPhoneMac(this), SaveUtils.getUserId(this));
        singleSignOnUseCase.checkPhoneInfo(entity, new Subscriber<NetResponseEntity<CheckPhoneInfoResponseEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mHandler.postDelayed(checkPhoneValidRunnable, RE_CONNECT_TIME * reConnectCount);
                reConnectCount++;
                reConnectCount = Math.min(RE_CONNECT_INDEX_MAX, reConnectCount);
            }

            @Override
            public void onNext(NetResponseEntity<CheckPhoneInfoResponseEntity> netResponseEntity) {
                if (netResponseEntity != null) {
                    CheckPhoneInfoResponseEntity e = netResponseEntity.getData();
                    if (e == null || e.getCheck() != 0) {
                        EventBus.getDefault().postSticky(new LogoutEvent());
                        disConnectSocket();
                    } else {
                        connectSocket();
                    }
                }
            }
        });
    }

    private void disConnectSocket() {
        shouldSocketConnect = false;
        if (connection != null) {
            connection.close();
        }
        mHandler.removeCallbacks(socketHeartBeatRunnable);
    }

    private static boolean isThereInternetConnection(Context context) {
        boolean isConnected;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());
        return isConnected;
    }
}
