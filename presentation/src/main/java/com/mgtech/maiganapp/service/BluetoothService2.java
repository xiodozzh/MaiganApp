//package com.mgtech.maiganapp.service;
//
//import android.app.Service;
//import android.bluetooth.BluetoothAdapter;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Paint;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Handler;
//import android.os.IBinder;
//import android.text.TextUtils;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.JobIntentService;
//
//import com.google.gson.Gson;
//import com.mgtech.blelib.biz.AsyncBleCenter;
//import com.mgtech.blelib.biz.IBizController;
//import com.mgtech.blelib.biz.IBraceletInfoManager;
//import com.mgtech.blelib.constant.BleConstant;
//import com.mgtech.blelib.constant.BleStatusConstant;
//import com.mgtech.blelib.entity.BleStateEvent;
//import com.mgtech.blelib.entity.BluetoothOrder;
//import com.mgtech.blelib.entity.CurrentStepData;
//import com.mgtech.blelib.entity.FirmwareInfoData;
//import com.mgtech.blelib.entity.PhoneEvent;
//import com.mgtech.blelib.entity.ReminderData;
//import com.mgtech.blelib.entity.SampleCompleteData;
//import com.mgtech.blelib.entity.StatusDataEvent;
//import com.mgtech.blelib.entity.StepHistory;
//import com.mgtech.blelib.entity.SyncBloodPressure;
//import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
//import com.mgtech.blelib.utils.StepUtils;
//import com.mgtech.blelib.utils.Utils;
//import com.mgtech.data.net.socket.SocketConnection;
//import com.mgtech.domain.entity.AppConfigNew;
//import com.mgtech.domain.entity.SocketListener;
//import com.mgtech.domain.entity.UserInfo;
//import com.mgtech.domain.entity.net.request.CheckPhoneInfoRequestEntity;
//import com.mgtech.domain.entity.net.request.GetStepRequestEntity;
//import com.mgtech.domain.entity.net.request.SaveStepRequestEntity;
//import com.mgtech.domain.entity.net.response.CheckPhoneInfoResponseEntity;
//import com.mgtech.domain.entity.net.response.NetResponseEntity;
//import com.mgtech.domain.entity.net.response.PwDataResponseEntity;
//import com.mgtech.domain.entity.net.response.StepDetailItemEntity;
//import com.mgtech.domain.entity.socket.SocketAutoPwEntity;
//import com.mgtech.domain.entity.socket.SocketEntity;
//import com.mgtech.domain.entity.socket.SocketMsgEntity;
//import com.mgtech.domain.interactor.DataUseCase;
//import com.mgtech.domain.interactor.SingleSignOnUseCase;
//import com.mgtech.domain.interactor.StepUseCase;
//import com.mgtech.domain.utils.HttpApi;
//import com.mgtech.domain.utils.NetConstant;
//import com.mgtech.domain.utils.SaveUtils;
//import com.mgtech.maiganapp.MyApplication;
//import com.mgtech.maiganapp.R;
//import com.mgtech.maiganapp.activity.MainActivity;
//import com.mgtech.maiganapp.activity.MeasureEcgActivity;
//import com.mgtech.maiganapp.activity.MeasurePwActivity;
//import com.mgtech.maiganapp.data.event.AppForegroundEvent;
//import com.mgtech.maiganapp.data.event.AutoPwCalculatedEvent;
//import com.mgtech.maiganapp.data.event.FirmwareUpgradeEvent;
//import com.mgtech.maiganapp.data.event.LogoutEvent;
//import com.mgtech.maiganapp.data.event.NotificationPageEvent;
//import com.mgtech.maiganapp.data.event.SocketEvent;
//import com.mgtech.maiganapp.data.model.IndicatorDataModel;
//import com.mgtech.maiganapp.data.model.StepModel;
//import com.mgtech.maiganapp.data.wrapper.IndicatorDataWrapper;
//import com.mgtech.maiganapp.data.wrapper.StepModelWrapper;
//import com.mgtech.maiganapp.utils.ViewUtils;
//import com.orhanobut.logger.Logger;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//import rx.Subscriber;
//
//
///**
// * @author zhaixiang
// * 一直运行的Service，蓝牙不断扫描设备，直到与设备连接
// * 信号采集时将数据保存到文件，采集结束后上传云端。网络异常时暂不上传
// * 使用onStart方式启动
// */
//
//public class BluetoothService2 extends Service {
//    public static final String EXTRA_SCAN_BLE = "scanBle";
//    public static final String EXTRA_PAIR_BLE = "pairBle";
//    public static final String EXTRA_STOP_BLE = "stopBle";
//    public static final String EXTRA_MEASURING_PW_BLE = "measuringPw";
//    public static final String EXTRA_MEASURING_ECG_BLE = "measuringEcg";
//    public static final String EXTRA_STOP_MEASURING_BLE = "stopMeasuring";
//
//    private String notificationAlarmTitle;
//    private String notificationAlarmContent;
//
//    private static final String TAG = "bluetoothService";
//    private IBraceletInfoManager manager;
//    private IBizController controller;
//    private MyReceiver receiver;
//
//    private DataUseCase dataUseCase;
//    private StepUseCase stepUseCase;
//    private SocketConnection connection;
//    private SingleSignOnUseCase singleSignOnUseCase;
//    private volatile boolean shouldSocketConnect = false;
//    private static final long RE_CONNECT_TIME = 2000;
//    private static final int RE_CONNECT_INDEX_MAX = 12;
//    private static final int RE_CONNECT_INDEX_MIN = 1;
//    private int reConnectCount = RE_CONNECT_INDEX_MIN;
//    private long lastTimeToLoadTodayStep = 0L;
//
//    private static Handler mHandler = new Handler();
//    /**
//     * 两次扫描间隔
//     */
//    private static final long SCAN_TIME_SPAN = 30 * 60 * 1000;
//    /**
//     * 每次扫描时间
//     */
//    private static final long SCAN_TIME = 60 * 1000;
//    private boolean appForeground;
//    private int currentPage;
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Logger.i("BluetoothService onCreate");
//        notificationAlarmTitle = getString(R.string.notification);
//        notificationAlarmContent = getString(R.string.bracelet_need_bond_again);
//        manager = new BraceletInfoManagerBuilder(this).create();
//
//        if (manager.isPaired()) {
//            setBackgroundNotification(new Intent(this, MainActivity.class),
//                    getString(R.string.app_name), getString(R.string.protect_your_health));
//        }
//        initBleController();
//        EventBus.getDefault().register(this);
//        receiver = new MyReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BleConstant.ACTIVITY_ACTION);
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        filter.addAction(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(receiver, filter);
//        singleSignOnUseCase = ((MyApplication) getApplication()).getSingleSignOnUseCase();
//        stepUseCase = ((MyApplication) getApplication()).getStepUseCase();
//    }
//
//    private void initBleController() {
//        controller = new AsyncBleCenter(this);
////        controller = ((MyApplication)getApplication()).initBle();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i(TAG, "service on start command");
//        Log.e("thread", "onStartCommand thread planId: " + Thread.currentThread().getId());
//        try {
//            if (intent != null) {
//                if (intent.getBooleanExtra(EXTRA_SCAN_BLE, false) && manager.isPaired() && isBleOn()) {
//                    if (manager.isPaired()) {
//                        setBackgroundNotification(new Intent(this, MainActivity.class),
//                                getString(R.string.app_name), getString(R.string.protect_your_health));
//                    }
//                    link();
//                } else if (intent.getBooleanExtra(EXTRA_PAIR_BLE, false)&& isBleOn()) {
//                    pair();
//                } else if (intent.getBooleanExtra(EXTRA_STOP_BLE, false)) {
//                    Log.i(TAG, "onStartCommand: disconnect");
//                    disconnect();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        setScanTime();
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    private void setBackgroundNotification(Intent notificationIntent,String text,String subTitle) {
////        if (notificationIntent == null) {
////            notificationIntent = new Intent(this, MainActivity.class);
////        }
////        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
////        startForeground(ViewUtils.NOTIFICATION_FORGROUND_ID, ViewUtils.getBackgroundNotification(this, notificationIntent,
////                text, subTitle, ViewUtils.CHANNEL_NOTIFICATION_ID));
//    }
//
//    /**
//     * 设置扫描时间
//     */
//    private void setScanTime() {
//        long pairTime = manager.getPairedTime();
//        if (pairTime == 0) {
//            return;
//        }
//        Calendar currentCalendar = Calendar.getInstance();
//        long current = currentCalendar.getTimeInMillis();
//        int scanInTime = (int) (SCAN_TIME_SPAN - (current - pairTime) % SCAN_TIME_SPAN);
//        Log.e(TAG, "setScanTime: " + scanInTime);
//        mHandler.removeCallbacks(startAutoScanRunnable);
//        mHandler.postDelayed(startAutoScanRunnable, scanInTime);
//    }
//
//    private Runnable startAutoScanRunnable = new Runnable() {
//        @Override
//        public void run() {
//            controller.linkIfAvailable();
//            mHandler.postDelayed(stopScanRunnable, SCAN_TIME);
//            setScanTime();
//        }
//    };
//
//
//    private Runnable stopScanRunnable = new Runnable() {
//        @Override
//        public void run() {
//            Log.e(TAG, "stopScanRunnable run ");
//            controller.stopScan();
//            setScanTime();
//        }
//    };
//
//
//    @Override
//    public void onDestroy() {
//        Log.e(TAG, "onDestroy: ");
//        controller.disconnect();
//        if (dataUseCase != null) {
//            dataUseCase.unSubscribe();
//        }
//        if (stepUseCase != null) {
//            stepUseCase.unSubscribe();
//        }
//        if (singleSignOnUseCase != null) {
//            singleSignOnUseCase.unSubscribe();
//        }
//        EventBus.getDefault().unregister(this);
//        unregisterReceiver(receiver);
//        mHandler.removeCallbacks(checkPhoneValidRunnable);
//        mHandler.removeCallbacks(socketHeartBeatRunnable);
//        mHandler.removeCallbacks(stopScanRunnable);
//        mHandler.removeCallbacks(startAutoScanRunnable);
//        super.onDestroy();
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    public class MyReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
//                Log.e(TAG, "onReceive: ACTION_SCREEN_OFF");
//                EventBus.getDefault().post(new PhoneEvent(BleStatusConstant.PHONE_SCREEN_OFF));
//            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
//                Log.e(TAG, "onReceive: HOME_CLICK");
//                EventBus.getDefault().post(new PhoneEvent(BleStatusConstant.PHONE_BACK_HOME));
//            } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
//                Log.e(TAG, "onReceive: ACTION_SCREEN_ON");
//            } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
//                Log.i("mark", "网络状态已经改变");
//                if (isThereInternetConnection(context)) {
//                    Log.i("mark", "网络状态连接");
//                    shouldSocketConnect = true;
//                    checkAndConnectSocket();
//                } else {
//                    Log.i("mark", "网络状态断开");
//                    disConnectSocket();
//                }
//            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
//                int test = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
//                switch (test) {
//                    case BluetoothAdapter.STATE_OFF:
//                        Log.e("tag", "STATE_OFF 手机蓝牙关闭");
//                        EventBus.getDefault().post(new BleStateEvent(BleStateEvent.STATE_OFF));
//                        break;
//                    case BluetoothAdapter.STATE_TURNING_OFF:
//                        Log.e("tag", "STATE_TURNING_OFF 手机蓝牙正在关闭");
//                        EventBus.getDefault().post(new BleStateEvent(BleStateEvent.STATE_TURNING_OFF));
//                        break;
//                    case BluetoothAdapter.STATE_ON:
//                        Log.e("tag", "STATE_ON 手机蓝牙开启");
//                        EventBus.getDefault().post(new BleStateEvent(BleStateEvent.STATE_ON));
//                        break;
//                    case BluetoothAdapter.STATE_TURNING_ON:
//                        Log.e("tag", "STATE_TURNING_ON 手机蓝牙正在开启");
//                        EventBus.getDefault().post(new BleStateEvent(BleStateEvent.STATE_TURNING_ON));
//                        break;
//                    default:
//                }
//            }
//        }
//    }
//
//    /**
//     * 蓝牙是否打开
//     *
//     * @return true 蓝牙开，false 蓝牙关
//     */
//    public boolean isBleOn() {
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        return mBluetoothAdapter == null || mBluetoothAdapter.isEnabled();
//    }
//
//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    public void getOrder(BluetoothOrder order) {
//        if (!Utils.isBluetoothOpen()) {
//            // 蓝牙关闭，断开并停止扫描
//            disconnect();
//            return;
//        }
//        controller.addOrder(order);
//    }
//
//    /**
//     * 发送连接指令
//     */
//    private void link() {
//        if (!manager.isPaired() || !isBleOn()) {
//            return;
//        }
//        Log.e(TAG, "link: tttttttt");
//        controller.link();
//    }
//
//    private void pair() {
//        if (manager.isPaired() || !isBleOn()) {
//            return;
//        }
//        controller.pair();
//    }
//
//
//    /**
//     * 发送断开指令
//     */
//    private void disconnect() {
//        controller.disconnect();
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    public void getStatus(StatusDataEvent event) {
//        if (event.getStatus() == BleStatusConstant.STATUS_SLEEP) {
//            Log.i(TAG, "app是否前台: " + appForeground);
//            if (!appForeground) {
//                controller.disconnectIfNotWorking();
//            }
//            long now = Calendar.getInstance().getTimeInMillis();
//            if (!manager.isStepHistorySyncToBracelet() && now - lastTimeToLoadTodayStep > 300_000) {
//                lastTimeToLoadTodayStep = now;
//                getCurrentStepDataFromNet();
//                return;
//            }
//            if (isDeviceNeedUpgrade()){
//                Log.i(TAG, "isDeviceNeedUpgrade: ser");
//                EventBus.getDefault().post(new FirmwareUpgradeEvent());
//                return;
//            }
//            if (Utils.doesReminderHaveZoneAndIntervalSetFunc(manager.getDeviceProtocolVersion())){
//                if (!manager.getReminderData().isTimeZoneSet()) {
//                    Logger.e("新版本");
//                    ReminderData data = manager.getReminderData();
//                    Utils.updateReminder(data);
//                    manager.setReminder(data);
//                    controller.addOrder(new BluetoothOrder(BleConstant.CODE_SET_ALERT_REMINDER, manager.getReminderData()));
//                }
//            }else {
//                if (manager.getReminderData().isTimeZoneSet()) {
//                    Logger.e("旧版本手环 新版本本地");
//                    ReminderData data = manager.getReminderData();
//                    Utils.backReminder(data);
//                    manager.setReminder(data);
//                    controller.addOrder(new BluetoothOrder(BleConstant.CODE_SET_ALERT_REMINDER, manager.getReminderData()));
//                }
//            }
//        } else if (event.getStatus() == BleStatusConstant.STATUS_PAIR_CODE_DIFFERENT) {
//            notificationPairCodeChangedPopup();
//        } else if (event.getStatus() == BleStatusConstant.STATUS_SET_ALERT_REMINDERS_SUCCESS){
//            SaveUtils.setRemindersNeedUpdate(getApplication(), true);
//            NetJobService.enqueueWork(this,NetJobService.getCallingIntent(this,NetJobService.TYPE_SAVE_REMINDERS));
//        }
//    }
//
//    /**
//     * 手环是否需要升级
//     *
//     * @return 升级条件：绑定、有新版本、配置信息为强制升级、电量20以上、已经连接、网络正常
//     */
//    private boolean isDeviceNeedUpgrade() {
//        return AppConfigNew.shouldAutoUpgradeBracelet(
//                        getApplication(),
//                        manager.getDeviceFirmwareVersionString(),
//                        manager.getDeviceName(), manager.getPower(),
//                        manager.getPowerStatus() == IBraceletInfoManager.POWER_CHARGING,false);
//    }
//
//    /**
//     * 上传主动采样数据
//     */
//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    public void getAutoMeasureData(SampleCompleteData data) {
//        if (data.isAuto()) {
//            saveAutoPwDataToNet(data.getTime(), data.getData());
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    public void getFirmwareVersion(FirmwareInfoData data) {
//        NetJobService.enqueueWork(this,NetJobService.getCallingIntent(this,NetJobService.TYPE_SET_BRACELET_INFO));
//    }
//
//    @Subscribe(threadMode = ThreadMode.BACKGROUND)
//    public void getStepHistoryData(StepHistory stepHistory) {
//        dealWithStepHistory(stepHistory);
//    }
//
//    private void saveAutoPwDataToNet(long time, List<Object> data) {
//        if (dataUseCase == null) {
//            dataUseCase = ((MyApplication) getApplication()).getDataUseCase();
//        }
//        UserInfo userInfo = UserInfo.getLocalUserInfo(this);
//        this.dataUseCase.calculateAutoData(this, data, time, userInfo.getAlgrithomSex(),
//                Math.round(userInfo.getHeight()), Math.round(userInfo.getWeight()), userInfo.getAge(this),
//                manager.getAddress(), manager.getDeviceProtocolVersionString(), manager.getDeviceFirmwareVersionString(),
//                new Subscriber<NetResponseEntity<PwDataResponseEntity>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(NetResponseEntity<PwDataResponseEntity> netResponseEntity) {
//                        Log.i(TAG, "onNext: " + netResponseEntity);
//                    }
//                });
//    }
//
//    /**
//     * 获取最近数据
//     */
//    private void getCurrentBloodPressure() {
//        if (dataUseCase == null) {
//            dataUseCase = ((MyApplication) getApplication()).getDataUseCase();
//        }
//        dataUseCase.getLastData(SaveUtils.getUserId(getApplicationContext()), NetConstant.NO_CACHE,
//                new Subscriber<NetResponseEntity<PwDataResponseEntity>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(NetResponseEntity<PwDataResponseEntity> netResponseEntity) {
//                        if (netResponseEntity != null && netResponseEntity.getCode() == 0) {
//                            PwDataResponseEntity entity = netResponseEntity.getData();
//                            if (entity != null) {
//                                IndicatorDataModel model = IndicatorDataWrapper.getModelFromPwDataResponseEntity(entity);
//                                SyncBloodPressure syncBloodPressure = new SyncBloodPressure(
//                                        model.time, Math.round(model.ps), model.psLevel,
//                                        Math.round(model.pd), model.pdLevel, Math.round(model.v0), model.v0Level
//                                );
//                                manager.saveSyncBloodPressure(syncBloodPressure);
//                                if (controller != null) {
//                                    controller.addOrder(new BluetoothOrder(BleConstant.CODE_RESET_AUTO_SAMPLE_TIME_AND_SYCN_BP));
//                                }
//                            }
//                        }
//                    }
//                });
//    }
//
//
//    /**
//     * 从网络获取当天数据
//     */
//    private void getCurrentStepDataFromNet() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        long startTime = calendar.getTimeInMillis();
//        calendar.add(Calendar.DATE, 1);
//        long endTime = calendar.getTimeInMillis();
//        GetStepRequestEntity entity = new GetStepRequestEntity(SaveUtils.getUserId(this),
//                GetStepRequestEntity.DAY, startTime, endTime);
//        stepUseCase.getStepDetail(NetConstant.NO_CACHE, entity, new Subscriber<NetResponseEntity<List<com.mgtech.domain.entity.net.response.StepDetailItemEntity>>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onNext(NetResponseEntity<List<StepDetailItemEntity>> netResponseEntity) {
//                Logger.i("getCurrentStepDataFromNet: " + netResponseEntity);
//                if (netResponseEntity.getCode() == 0) {
//                    List<StepDetailItemEntity> list = netResponseEntity.getData();
//                    if (list != null) {
//                        List<StepModel> stepModels = StepModelWrapper.getModelListFromNet(list);
//                        if (stepModels.size() == 1) {
//                            StepModel model = stepModels.get(0);
//                            int currentHour = (int) (model.startTime / 1000L / 3600);
//                            CurrentStepData data = new CurrentStepData();
//
//                            data.setHour(currentHour);
//                            data.setQuarterOffset(0);
//                            data.setDayStep(model.stepCount);
//                            data.setQuarterStep(0);
//                            data.setDayMinute(model.duration);
//                            data.setQuarterMinute(0);
//                            Logger.i("getCurrentStepDataFromNet: " + data);
//                            getOrder(new BluetoothOrder(BleConstant.CODE_CALIBRATE_STEP_DATA, data));
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//
//    /**
//     * 处理计步历史数据
//     *
//     * @param stepHistory 计步信息
//     */
//    private void dealWithStepHistory(StepHistory stepHistory) {
//        Log.e(TAG, "dealWithStepHistory: " + stepHistory);
//        List<SaveStepRequestEntity.StepBean> list = new ArrayList<>();
//        List<StepHistory.HourData> data = stepHistory.getHourDataList();
//        if (data == null || data.isEmpty()) {
//            return;
//        }
//        long syncTime = 0;
//        int height = Math.round(UserInfo.getLocalUserInfo(this).getHeight());
//        int weight = Math.round(UserInfo.getLocalUserInfo(this).getWeight());
//        for (StepHistory.HourData hourData : data) {
//            long hour = hourData.getStartHour();
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(hour * 3600_000);
//            SaveStepRequestEntity.StepBean entity1 = generateSaveStepItemEntity(calendar, hourData.getStepInQuarter1(), hourData.getSportInQuarter1(), height, weight);
//
//            calendar.add(Calendar.MINUTE, 15);
//            SaveStepRequestEntity.StepBean entity2 = generateSaveStepItemEntity(calendar, hourData.getStepInQuarter2(), hourData.getSportInQuarter2(), height, weight);
//
//            calendar.add(Calendar.MINUTE, 15);
//            SaveStepRequestEntity.StepBean entity3 = generateSaveStepItemEntity(calendar, hourData.getStepInQuarter3(), hourData.getSportInQuarter3(), height, weight);
//
//            calendar.add(Calendar.MINUTE, 15);
//            SaveStepRequestEntity.StepBean entity4 = generateSaveStepItemEntity(calendar, hourData.getStepInQuarter4(), hourData.getSportInQuarter4(), height, weight);
//
//            calendar.add(Calendar.MINUTE, 15);
//            syncTime = calendar.getTimeInMillis();
//
//            // 如果数据为空，则不上传
//            if (entity1.getDuration() != 0 || entity1.getStepCount() != 0) {
//                list.add(entity1);
//            }
//            if (entity2.getDuration() != 0 || entity2.getStepCount() != 0) {
//                list.add(entity2);
//            }
//            if (entity3.getDuration() != 0 || entity3.getStepCount() != 0) {
//                list.add(entity3);
//            }
//            if (entity4.getDuration() != 0 || entity4.getStepCount() != 0) {
//                list.add(entity4);
//            }
//        }
//        if (list.isEmpty()) {
//            // 数据为空，不上传，只记录同步时间
//            manager.saveStepSyncTime(syncTime);
//            return;
//        }
//        saveStepData(list, syncTime);
//    }
//
//    /**
//     * 生成SaveStepEntity
//     *
//     * @param calendar 日期
//     * @param step     步数
//     * @param minute   运动时间
//     * @return SaveStepItemEntity
//     */
//    private SaveStepRequestEntity.StepBean generateSaveStepItemEntity(Calendar calendar, int step, int minute, int height, int weight) {
//        SaveStepRequestEntity.StepBean entity = new SaveStepRequestEntity.StepBean();
//        entity.setDuration(minute);
//        entity.setStepCount(step);
//        entity.setHeat(StepUtils.calculateCalorie(step, height, weight));
//        entity.setDistance(StepUtils.calculateDistance(step, height));
//        entity.setStartTime(calendar.getTimeInMillis());
//        entity.setEndTime(calendar.getTimeInMillis() + 15 * 60_000);
//        return entity;
//    }
//
//    private void saveStepData(List<SaveStepRequestEntity.StepBean> list, final long t) {
//        SaveStepRequestEntity entity = new SaveStepRequestEntity(list, SaveUtils.getUserId(getApplicationContext()));
//        stepUseCase.saveStep(entity, new Subscriber<NetResponseEntity>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(NetResponseEntity netResponseEntity) {
//                if (netResponseEntity != null && netResponseEntity.getCode() == 0) {
//                    manager.saveStepSyncTime(t);
//                }
//                unsubscribe();
//            }
//        });
//    }
//
//    /**
//     * 弹出断开提醒(晚上22：00至早上7：00无声音)
//     */
//    private void notificationPairCodeChangedPopup() {
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        Calendar calendar = Calendar.getInstance();
////        startService(NetService.getCallingIntent(this,NetService.TYPE_UN_BIND));
//        NetJobService.enqueueWork(this,NetJobService.getCallingIntent(this,NetJobService.TYPE_UN_BIND));
//        if (calendar.get(Calendar.HOUR_OF_DAY) < 22 && calendar.get(Calendar.HOUR_OF_DAY) > 7) {
//            ViewUtils.showNotification(this, notificationIntent,  notificationAlarmTitle, notificationAlarmContent,
//                    ViewUtils.NOTIFICATION_DISCONNECT_ID);
//        } else {
//            ViewUtils.showNotification(this, notificationIntent,  notificationAlarmTitle, notificationAlarmContent,
//                    ViewUtils.NOTIFICATION_DISCONNECT_ID);
//        }
//    }
//
//    /**
//     * 开始服务
//     *
//     * @param context 上下文
//     */
//    public static void startBluetoothService(Context context) {
//        Intent intent = new Intent(context, BluetoothService2.class);
//        context.startService(intent);
//    }
//
//    /**
//     * 开始服务,并且扫描
//     *
//     * @param context 上下文
//     */
//    public static void startServiceWithBleScan(Context context) {
//        Intent intent = getCallingIntent(context);
//        intent.putExtra(EXTRA_SCAN_BLE, true);
//        context.startService(intent);
//    }
//
//    /**
//     * 开启服务，并扫描绑定
//     *
//     * @param context 上下文
//     */
//    public static void startServiceWithBlePair(Context context) {
//        Intent intent = new Intent(context, BluetoothService2.class);
//        intent.putExtra(EXTRA_PAIR_BLE, true);
//        context.startService(intent);
//    }
//
//    /**
//     * 结束服务
//     *
//     * @param context 上下文
//     */
//    public static void stopMeasureService(Context context) {
//        Log.e(TAG, "stopMeasureAndLeave: ");
//        context.stopService(getCallingIntent(context));
//    }
//
//    public static boolean isServiceOn(Context context) {
//        return ViewUtils.isServiceRunning(context, BluetoothService2.class.getName());
//    }
//
//    public static void disConnect(Context context) {
//        Intent intent = getCallingIntent(context);
//        intent.putExtra(EXTRA_STOP_BLE, true);
//        context.startService(intent);
//    }
//
//    public static Intent getCallingIntent(Context context) {
//        return new Intent(context, BluetoothService2.class);
//    }
//
//    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
//    public void appGoToForeground(AppForegroundEvent event) {
//        appForeground = event.isForeground();
//        if (appForeground){
//            NetJobService.enqueueWork(this,NetJobService.getCallingIntent(this,NetService.TYPE_GET_CONFIG));
//            NetJobService.enqueueWork(this,NetJobService.getCallingIntent(this,NetService.TYPE_INDICATOR_PARAMETERS));
//        }else if (isBleOn()) {
//            controller.disconnectIfNotWorking();
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
//    public void changeNotificationPage(NotificationPageEvent event) {
//        EventBus.getDefault().removeStickyEvent(event);
//        if (currentPage == event.getPageId()){
//            return;
//        }
//        switch (event.getPageId()) {
//            case NotificationPageEvent.MEASURE_PW_ACTIVITY:
////                setBackgroundNotification(new Intent(this, MeasurePwActivity.class),
////                        getString(R.string.app_name),getString(R.string.measure_pw_title));
//
//                setBackgroundNotification(new Intent(this, MeasurePwActivity.class),
//                        getString(R.string.app_name), getString(R.string.protect_your_health));
//
//                break;
//            case NotificationPageEvent.MEASURE_ECG_ACTIVITY:
//                setBackgroundNotification(new Intent(this, MeasureEcgActivity.class),
//                        getString(R.string.app_name), getString(R.string.protect_your_health));
////                setBackgroundNotification(new Intent(this, MeasureEcgActivity.class),
////                        getString(R.string.app_name),getString(R.string.measure_ecg_title));
//                break;
//            case NotificationPageEvent.MAIN_ACTIVITY:
//                setBackgroundNotification(new Intent(this, MainActivity.class),
//                        getString(R.string.app_name),getString(R.string.protect_your_health));
//                break;
//            default:
//                setBackgroundNotification(new Intent(this, MainActivity.class),
//                        getString(R.string.app_name),getString(R.string.protect_your_health));
//        }
//        currentPage = event.getPageId();
//    }
//
//    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
//    public void receiveSocketEvent(SocketEvent event) {
//        Log.i("socket", "receiveSocketEvent: ");
//        EventBus.getDefault().removeStickyEvent(event);
//        if (event.isLink()) {
//            shouldSocketConnect = true;
//            checkAndConnectSocket();
//
//        } else {
//            disConnectSocket();
//        }
//    }
//
//
//    /**
//     * 连接socket
//     */
//    private void checkAndConnectSocket() {
//        mHandler.removeCallbacks(checkPhoneValidRunnable);
//        mHandler.post(checkPhoneValidRunnable);
//    }
//
//    private Runnable socketHeartBeatRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (connection != null && connection.isSocketConnectOn()) {
//                connection.send("{" +
//                        "\"req\":1," +
//                        "\"time\":" + System.currentTimeMillis() +
//                        "}");
//                mHandler.postDelayed(socketHeartBeatRunnable, 60 * 1000);
//            } else {
////                checkPhone();
//            }
//        }
//    };
//
//    private Runnable checkPhoneValidRunnable = new Runnable() {
//        @Override
//        public void run() {
//            checkPhone();
//        }
//    };
//
//    private synchronized void connectSocket() {
//        final String id = SaveUtils.getUserId(this);
//        if (TextUtils.isEmpty(id)) {
//            return;
//        }
//        if (connection != null) {
//            if (connection.isSocketConnectOn()){
//                Log.i(TAG, "connectSocket: "+ true);
//                return;
//            }
//            Log.i(TAG, "connectSocket: "+ false);
//            connection.close();
//        }
//        Log.i(TAG, "connectSocket: first"+ false);
//        connection = SocketConnection.getInstance(String.format(HttpApi.API_SOCKET_URL +
//                        "/createConnection?accountGuid=%s&deviceId=%s&platform=%s",
//                id, com.mgtech.domain.utils.Utils.getPhoneMac(getApplication()), "android"), socketListener);
//        Log.i("socket", String.format(HttpApi.API_SOCKET_URL +
//                        "/createConnection?accountGuid=%s&deviceId=%s&platform=%s",
//                id, com.mgtech.domain.utils.Utils.getPhoneMac(getApplication()), "android") + connection );
//        connection.connect();
//    }
//
//    private SocketListener socketListener = new SocketListener() {
//        @Override
//        public void onOpen() {
//            Log.e("socket", "open: ");
//            reConnectCount = RE_CONNECT_INDEX_MIN;
//            mHandler.postDelayed(socketHeartBeatRunnable, 5_000);
//        }
//
//        @Override
//        public void onMessage(String msg) {
//            Log.e("socket", "onMessage: " + msg);
//            reConnectCount = RE_CONNECT_INDEX_MIN;
//            Gson gson = new Gson();
//            SocketEntity entity = gson.fromJson(msg, SocketEntity.class);
//            Log.i("socket", "onMessage: " + entity);
//            if ("AccountManager".equals(entity.getModule())) {
//                SocketMsgEntity socketMsgEntity = gson.fromJson(msg, SocketMsgEntity.class);
//                SocketMsgEntity.DataBean dataBean = socketMsgEntity.getData();
//                if (dataBean != null) {
//                    if (dataBean.getIsLoginOnOtherDevice() == 1) {
//                        Log.e("socket", "onMessage: logout");
//                        EventBus.getDefault().postSticky(new LogoutEvent(dataBean.getPhoneName()));
//                        disConnectSocket();
//                        mHandler.removeCallbacks(socketHeartBeatRunnable);
//                    }
//                }
//            } else if ("PW".equals(entity.getModule())) {
//                SocketAutoPwEntity socketAutoPwEntity = gson.fromJson(msg, SocketAutoPwEntity.class);
//                PwDataResponseEntity dataEntity = socketAutoPwEntity.getData();
//                if (dataEntity != null) {
//                    IndicatorDataModel model = IndicatorDataWrapper.getModelFromPwDataResponseEntity(dataEntity);
//                    EventBus.getDefault().post(new AutoPwCalculatedEvent());
//                    NetJobService.enqueueWork(BluetoothService2.this,
//                            NetJobService.getCallingIntent(BluetoothService2.this,NetJobService.TYPE_GET_UNREAD_MESSAGE));
////                    SyncBloodPressure syncBloodPressure = new SyncBloodPressure(
////                            model.time, Math.round(model.ps), model.psLevel,
////                            Math.round(model.pd), model.pdLevel, model.v0, model.v0Level
////                    );
////                    manager.saveSyncBloodPressure(syncBloodPressure);
////                    if (controller != null) {
////                        controller.addOrder(new BluetoothOrder(BleConstant.CODE_RESET_AUTO_SAMPLE_TIME_AND_SYCN_BP));
////                    }
//                }
//            }
//
//        }
//
//        @Override
//        public void onClose() {
//            Log.e("socket", "close: ");
//            mHandler.removeCallbacks(checkPhoneValidRunnable);
//            mHandler.removeCallbacks(socketHeartBeatRunnable);
//            mHandler.postDelayed(checkPhoneValidRunnable, RE_CONNECT_TIME * reConnectCount);
//        }
//
//        @Override
//        public void onFail() {
//            Log.e("socket", "fail: ");
//            mHandler.removeCallbacks(checkPhoneValidRunnable);
//            mHandler.removeCallbacks(socketHeartBeatRunnable);
//            mHandler.postDelayed(checkPhoneValidRunnable, RE_CONNECT_TIME * reConnectCount);
//            reConnectCount++;
//            reConnectCount = Math.min(RE_CONNECT_INDEX_MAX, reConnectCount);
////            socketConnectOn = false;
//        }
//    };
//
//    private void checkPhone() {
//        if (!shouldSocketConnect) {
//            return;
//        }
//        mHandler.removeCallbacks(checkPhoneValidRunnable);
//        CheckPhoneInfoRequestEntity entity = new CheckPhoneInfoRequestEntity(
//                com.mgtech.domain.utils.Utils.getPhoneMac(this), SaveUtils.getUserId(this));
//        singleSignOnUseCase.checkPhoneInfo(entity, new Subscriber<com.mgtech.domain.entity.net.response.NetResponseEntity<CheckPhoneInfoResponseEntity>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                mHandler.postDelayed(checkPhoneValidRunnable, RE_CONNECT_TIME * reConnectCount);
//                reConnectCount++;
//                reConnectCount = Math.min(RE_CONNECT_INDEX_MAX, reConnectCount);
//            }
//
//            @Override
//            public void onNext(com.mgtech.domain.entity.net.response.NetResponseEntity<CheckPhoneInfoResponseEntity> netResponseEntity) {
//                if (netResponseEntity != null) {
//                    CheckPhoneInfoResponseEntity e = netResponseEntity.getData();
//                    if (e == null || e.getCheck() != 0) {
//                        EventBus.getDefault().postSticky(new LogoutEvent());
//                        disConnectSocket();
//                    } else {
//                        connectSocket();
//                    }
//                }
//            }
//        });
//    }
//
//    private void disConnectSocket() {
//        shouldSocketConnect = false;
//        if (connection != null) {
//            connection.close();
//        }
//        mHandler.removeCallbacks(socketHeartBeatRunnable);
//    }
//
//    private static boolean isThereInternetConnection(Context context) {
//        boolean isConnected;
//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivityManager == null) {
//            return false;
//        }
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());
//        return isConnected;
//    }
//}
