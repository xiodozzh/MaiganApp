package com.mgtech.blelib.biz;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

import com.mgtech.blelib.constant.BleConstant;
import com.mgtech.blelib.core.BleClient2;
import com.mgtech.blelib.entity.BluetoothOrder;
import com.mgtech.blelib.entity.BroadcastData;
import com.mgtech.blelib.entity.CheckDeviceData;
import com.mgtech.blelib.entity.CurrentStepData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.FirmwareUpgradeData;
import com.mgtech.blelib.entity.HeightWeightData;
import com.mgtech.blelib.entity.LinkStatusEvent;
import com.mgtech.blelib.entity.LookBandData;
import com.mgtech.blelib.entity.ManualEcgData;
import com.mgtech.blelib.entity.ManualEcgError;
import com.mgtech.blelib.entity.ManualMeasureNewOrder;
import com.mgtech.blelib.entity.ManualPwData;
import com.mgtech.blelib.entity.MeasureEcgData;
import com.mgtech.blelib.entity.MeasurePwData;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.entity.SampleCompleteData;
import com.mgtech.blelib.entity.SyncBloodPressure;
import com.mgtech.blelib.entity.SystemParam0Data;
import com.mgtech.blelib.pwRecognize.IRecognize;
import com.mgtech.blelib.pwRecognize.PwRecognize2;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author zhaixiang
 */
public class BleCenter implements IBizController {
    private static final long HOUR = 3600_000;
    private static final int MAX_STEP_HOUR_BRACELET_CAN_SAVE = 204;

    private BleCore bleCore;
    private BleState bleState;
    private BleOrder bleOrder;
    private IBraceletInfoManager manager;
    private Deque<BluetoothOrder> queue;
    private MeasureEcgData measureEcgData;
    private MeasurePwData measurePwData;
    private IResponseBytes responseBytes;
    private BleResponse bleResponse;
    private BleRequest bleRequest;
    private BleSender bleSender;

    public BleCenter(Context context) {
        BleClient2 bleClient2 = BleClient2.getInstance(context);
        queue = new LinkedBlockingDeque<>();
        manager = new BraceletInfoManagerBuilder(context).create();
        bleState = new BleState();
        bleResponse = new BleResponse();
        bleRequest = new BleRequest(this,manager);
        bleSender = new BleSender(this, bleResponse);
        bleOrder = new BleOrder();
        responseBytes = new BluetoothResponseBytes(bleState, bleOrder, bleSender, manager, bleClient2);
        bleCore = new BleCore(context, bleClient2, manager, bleSender, bleOrder, bleState, responseBytes);
        bleOrder.setBleClient(bleClient2);
        bleOrder.setBleCore(bleCore);
        bleOrder.setBleState(bleState);
        bleOrder.setManager(manager);
        bleOrder.setOrder(new BluetoothOrderBytesVersion421(manager));
    }

    @Override
    public void stopScan() {
        bleCore.stopScan();
    }

    @Override
    public void disconnectIfNotWorking() {
        if (!bleState.isWorking()) {
            bleCore.disconnect();
        }
    }

    private boolean isBleOn() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter == null || mBluetoothAdapter.isEnabled();
    }

    @Override
    public boolean linkIfAvailable() {
        if (!bleState.isReady() && manager.isPaired() && isBleOn()) {
            link();
            return true;
        }
        return false;
    }

    @Override
    public void pairInit() {
        initPairTask();
    }

    @Override
    public void link() {
        bleCore.startScan(BleConstant.DEVICE_NAMES, manager.getAddress());
    }

    @Override
    public void pair() {
        bleCore.startPairScan(BleConstant.DEVICE_NAMES);
    }

    @Override
    public void disconnect() {
        bleCore.disconnect();
    }

    @Override
    public void addOrder(BluetoothOrder order) {
        switch (order.getOrder()) {
            case BleConstant.CODE_GET_LINK_STATUS:
                getLinkStatus();
                break;
            case BleConstant.CODE_REQUEST_PAIR:
                if (order.getData() instanceof BroadcastData) {
                    bleOrder.requestPair((BroadcastData) order.getData());
                }
                break;
            case BleConstant.CODE_LINK:
                bleCore.startScan(BleConstant.DEVICE_NAMES, manager.getAddress());
                break;
            case BleConstant.CODE_PAIR:
                bleCore.startPairScan(BleConstant.DEVICE_NAMES);
                break;
            case BleConstant.CODE_DISCONNECT:
                bleCore.disconnect();
                break;
            case BleConstant.CODE_STOP_SCAN:
                bleCore.stopScan();
                break;
            case BleConstant.CODE_VERIFY:
                if (order.getData() instanceof CheckDeviceData) {
                    bleOrder.verifySuccessAndPair((CheckDeviceData) order.getData());
                }
                break;
            case BleConstant.CODE_PAIR_MAC:
                Object broadcastOrder = order.getData();
                if (broadcastOrder instanceof BroadcastData) {
                    bleCore.connectDevice((BroadcastData) broadcastOrder);
                }
                break;
            case BleConstant.CODE_UN_PAIR:
                unPairTask();
//                bleOrder.unPair();
                break;
            case BleConstant.CODE_RESET_AUTO_SAMPLE_TIME_AND_SYCN_BP:
                resetAutoSampleTimeAndSyncBp(manager.getSyncBloodPressure());
                if (!bleState.isWorking()) {
                    runNextTask();
                }
                break;
            case BleConstant.CODE_STOP_MEASURE:
                cancelMeasure();
                break;
            case BleConstant.CODE_GET_STORED_MANUAL_DATA:
                bleOrder.getStoredMeasureData();
                break;
            default:
                putOrderInQueue(order);
                Logger.i("addOrder: " + order.getOrder() + bleState.isWorking());
                if (!bleState.isWorking()) {
                    runNextTask();
                }
        }
    }

    @Override
    public BleResponse getResponse() {
        return bleResponse;
    }

    @Override
    public BleRequest getRequest() {
        return bleRequest;
    }

    @Override
    public BleState getState() {
        return bleState;
    }

    /**
     * 绑定后执行
     */
    public synchronized void initPairTask() {
        Log.i("BleCenter", "initPairTask: ");
        queue.clear();
        queue.offer(new BluetoothOrder(BleConstant.CODE_SYNC_TIME));
        queue.offer(new BluetoothOrder(BleConstant.CODE_GET_POWER));
        if (manager.getSyncBloodPressure() != null) {
            queue.offer(new BluetoothOrder(BleConstant.CODE_SET_BLOOD_PRESSURE, manager.getSyncBloodPressure()));
        }
        queue.offer(new BluetoothOrder(BleConstant.CODE_SET_HEIGHT_WEIGHT, manager.getHeightWeight()));
        runNextTask();
    }

    public synchronized void clearTask() {
        queue.clear();
    }

    public synchronized void unPairTask(){
        queue.clear();
        queue.offer(new BluetoothOrder(BleConstant.CODE_UN_PAIR));
        runNextTask();
    }

    /**
     * 初始化连接后的任务
     */
    public synchronized void initLinkTask() {
        if (!manager.isPaired()) {
            return;
        }
        queue.clear();
        queue.offer(new BluetoothOrder(BleConstant.CODE_GET_FIRMWARE_INFO));
        queue.offer(new BluetoothOrder(BleConstant.CODE_SYNC_TIME));
        queue.offer(new BluetoothOrder(BleConstant.CODE_GET_POWER));
        queue.offer(new BluetoothOrder(BleConstant.CODE_SET_HEIGHT_WEIGHT, manager.getHeightWeight()));
        SyncBloodPressure bloodPressure = manager.getSyncBloodPressure();
        if (bloodPressure != null) {
            queue.offer(new BluetoothOrder(BleConstant.CODE_SET_BLOOD_PRESSURE, bloodPressure));
        }
        queue.offer(new BluetoothOrder(BleConstant.CODE_GET_AUTO_MEASURE_INFO));

        int hour = (int) Math.max((manager.getStepSyncTime() / HOUR - 1),
                Calendar.getInstance().getTimeInMillis() / 1000 / 60 / 60 - MAX_STEP_HOUR_BRACELET_CAN_SAVE);
        queue.offer(new BluetoothOrder(BleConstant.CODE_GET_HISTORY_STEP, hour));
        queue.offer(new BluetoothOrder(BleConstant.CODE_GET_ALERT_REMINDERS));
        runNextTask();
    }


    /**
     * 将任务放入队列（暂不执行）
     *
     * @param order 任务
     */
    public synchronized void putOrderInQueue(BluetoothOrder order) {
        queue.offer(order);
    }

    /**
     * 重置自动采样时间,并设置血压
     */
    private synchronized void resetAutoSampleTimeAndSyncBp(SyncBloodPressure syncBloodPressure) {
        queue.offerFirst(new BluetoothOrder(BleConstant.CODE_RESET_AUTO_SAMPLE_TIME));
        queue.offerFirst(new BluetoothOrder(BleConstant.CODE_SET_BLOOD_PRESSURE, syncBloodPressure));
    }

    private void getLinkStatus() {
//        EventBus.getDefault().post(new LinkStatusEvent(bleState.isReady()));
        bleSender.getLinkStatus(new LinkStatusEvent(bleState.isReady()));
    }

    /**
     * 执行下一个任务
     */
    public synchronized void runNextTask() {
//        bleState.setWorking(false);
        bleState.setStatus(BleState.STATUS_READY,"runNextTask");
        if (!queue.isEmpty()) {
            Logger.i("任务队列非空");
            if (bleState.isReady()) {
                BluetoothOrder order = queue.poll();
                Logger.i("run Task: order " + order.getOrder());
                runOrder(order);
            } else {
                Logger.i("wait Task: " + queue.getFirst().getClass().getSimpleName());
            }
        } else {
            Logger.i("任务队列为空");
//            EventBus.getDefault().post(new StatusDataEvent(BleStatusConstant.STATUS_SLEEP));
            bleSender.sleep();
        }
    }

    public void runNextTaskWhenNotWorking() {
        Log.i("bleState", "runNextTaskWhenNotWorking: " + bleState.isWorking());
        if (!bleState.isWorking()) {
            runNextTask();
        }
    }

    /**
     * 执行操作
     *
     * @param order 指令
     */
    private void runOrder(BluetoothOrder order) {
        if (!isBleOn()) {
            return;
        }
        boolean isFinish = false;
        bleState.setStatus(BleState.STATUS_LAUNCH_WORK,"runOrder");
//        bleState.setWorking(true);
        switch (order.getOrder()) {
            case BleConstant.CODE_MEASURE_PW:
                if (order.getData() instanceof ManualMeasureNewOrder) {
                    ManualMeasureNewOrder measurePwOrder = (ManualMeasureNewOrder) order.getData();
                    measurePwOrder.setTag(ManualMeasureNewOrder.TAG_PRE);
                    initMeasurePwFirstData();
                    bleOrder.measurePw(measurePwOrder);
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_MEASURE_PW_WITHOUT_RECOGNIZE:
                if (order.getData() instanceof ManualMeasureNewOrder && order.isAvailable()) {
                    ManualMeasureNewOrder measurePwOrder = (ManualMeasureNewOrder) order.getData();
                    measurePwOrder.setTag(ManualMeasureNewOrder.TAG_PW_RAW_DATA);
                    bleOrder.measurePw(measurePwOrder);
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_MEASURE_PW_AUTO:
                if (order.getData() instanceof ManualMeasureNewOrder && order.isAvailable()) {
                    ManualMeasureNewOrder measurePwOrder = (ManualMeasureNewOrder) order.getData();
                    measurePwOrder.setTag(ManualMeasureNewOrder.TAG_AUTO);
                    bleOrder.measurePw(measurePwOrder);
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_MEASURE_ECG:
                if (order.getData() instanceof ManualMeasureNewOrder) {
                    ManualMeasureNewOrder orderEcg = (ManualMeasureNewOrder) order.getData();
                    orderEcg.setTag(ManualMeasureNewOrder.TAG_ECG_PRE);
                    bleOrder.measureEcg(orderEcg);
                    initEcgFirstData();
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_ENABLE_AUTO_MEASURE:
                if (order.getData() instanceof Boolean && order.isAvailable()) {
                    bleOrder.setParameter0(new SystemParam0Data(false, (Boolean) order.getData()));
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_STOP_MEASURE:
                cancelMeasure();
                break;
            case BleConstant.CODE_GET_ALERT_REMINDERS:
                if (order.isAvailable()) {
                    bleOrder.getAlertReminders();
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_GET_DISPLAY_PAGE:
                if (order.isAvailable()) {
                    bleOrder.getDisplayPage();
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_SET_ALERT_REMINDER:
                if (order.getData() instanceof ReminderData && order.isAvailable()) {
                    ReminderData data = (ReminderData) order.getData();
                    bleOrder.setAlertReminders(data);
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_SET_AUTO_MEASURE_INTERVAL:
                if (order.getData() instanceof ReminderData && order.isAvailable()) {
                    ReminderData data = (ReminderData) order.getData();
                    bleOrder.setAlertRemindersAndInteval(data);
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_SET_DISPLAY_PAGE:
                if (order.getData() instanceof DisplayPage && order.isAvailable()) {
                    DisplayPage dataDisplay = (DisplayPage) order.getData();
                    bleOrder.setDisplayPage(dataDisplay);
                } else {
                    isFinish = true;
                }
                break;
//            case BleConstant.CODE_SET_AUTO_MEASURE_INTERVAL:
//                if (order.getData() instanceof AutoMeasurePeriodData && order.isAvailable()) {
//                    AutoMeasurePeriodData autoMeasurePeriodData = (AutoMeasurePeriodData) order.getData();
//                    bleOrder.setAutoMeasureInterval(autoMeasurePeriodData);
//                } else {
//                    isFinish = true;
//                }
//                break;
            case BleConstant.CODE_SET_HEIGHT_WEIGHT:
                if (order.getData() instanceof HeightWeightData && order.isAvailable()) {
                    HeightWeightData heightWeightData = (HeightWeightData) order.getData();
                    bleOrder.setHeightAndWeight(heightWeightData);
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_CALIBRATE_STEP_DATA:
                if (order.getData() instanceof CurrentStepData && order.isAvailable()) {
                    CurrentStepData stepData = (CurrentStepData) order.getData();
                    bleOrder.calibrateStep(stepData);
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_RESET_AUTO_SAMPLE_TIME:
                if (order.isAvailable()) {
                    bleOrder.resetAutoSampleTime();
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_SET_BLOOD_PRESSURE:
                if (order.getData() instanceof SyncBloodPressure && order.isAvailable()) {
                    bleOrder.setBloodPressure((SyncBloodPressure) order.getData());
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_GET_FIRMWARE_INFO:
                if (order.isAvailable()) {
                    bleOrder.getFirmwareInfo();
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_GET_POWER:
                if (order.isAvailable()) {
                    bleOrder.getPower();
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_GET_PARAM_0:
                if (order.isAvailable()) {

                    bleOrder.getParameter0();
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_GET_AUTO_MEASURE_INFO:
                if (order.isAvailable()) {
                    bleOrder.getAutoSampleInfo();
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_SYNC_TIME:
                if (order.isAvailable()) {
                    bleOrder.updateTime();
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_GET_HISTORY_STEP:
                if (order.getData() instanceof Integer && order.isAvailable()) {
                    bleOrder.getStepHistory((Integer) order.getData());
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_UPGRADE:
                if (order.getData() instanceof FirmwareUpgradeData && order.isAvailable()) {
                    bleOrder.sendUpgradeInfo((FirmwareUpgradeData) order.getData());
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_CANCEL_UPGRADE:
                if (order.isAvailable()) {
                    cancelUpgrade();
                    bleOrder.cancelUpgrade();
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_LOOK_FOR_BAND:
                if (order.isAvailable()) {
                    bleOrder.lookForBand((LookBandData) order.getData());
                } else {
                    isFinish = true;
                }
                break;
            case BleConstant.CODE_UN_PAIR:
                if (order.isAvailable()) {
                    bleOrder.unPair();
                } else {
                    isFinish = true;
                }
                break;
            default:
                isFinish = true;
                break;
        }
        if (isFinish) {
            bleState.setStatus(BleState.STATUS_READY,"taskFinish");
//            bleState.setWorking(false);
            runNextTask();
        }
    }

    /**
     * 停止升级固件，如果升级指令未执行，则设置为无效
     */
    private synchronized void cancelUpgrade() {
        for (BluetoothOrder o : queue) {
            if (o.getOrder() == BleConstant.CODE_UPGRADE) {
                o.setAvailable(false);
            }
        }
    }

    private void cancelMeasure() {
        synchronized (this) {
            for (BluetoothOrder o : queue) {
                if (o.getOrder() == BleConstant.CODE_MEASURE_PW || o.getOrder() == BleConstant.CODE_MEASURE_PW_AUTO ||
                        o.getOrder() == BleConstant.CODE_MEASURE_PW_WITHOUT_RECOGNIZE || o.getOrder() == BleConstant.CODE_MEASURE_ECG) {
                    o.setAvailable(false);
                }
            }
        }
        bleOrder.stopMeasure();
    }


    /**
     * 初始化ECG测量
     */
    private void initEcgFirstData() {
//        EventBus.getDefault().post(new StatusDataEvent(BleStatusConstant.STATUS_MEASURE_ECG_1));
        bleSender.ecgStartFirstStatus();
        measureEcgData = new MeasureEcgData(new MeasureEcgData.EcgCallback() {
            @Override
            public void onError(int errorCode) {
//                EventBus.getDefault().post(new ManualEcgError(errorCode));
                bleResponse.manualEcgError(new ManualEcgError(errorCode));
            }

            @Override
            public void drawData(double[] data, float sampleRate) {
                sendEcgData(data, sampleRate, 0);
            }

            @Override
            public void pwSuccess(float sample, int[] peakValley) {
                ManualMeasureNewOrder manualMeasureOrder = new ManualMeasureNewOrder();
                manualMeasureOrder.setType(ManualMeasureNewOrder.ECG);
                manualMeasureOrder.setRateType(0);
                manualMeasureOrder.setRateValue(Math.round(sample));
                manualMeasureOrder.setEnable(ManualMeasureNewOrder.accEnable);
                manualMeasureOrder.setTag(ManualMeasureNewOrder.TAG_ECG_REAL_DATA);
                manualMeasureOrder.setRegionAvailable(true);
                manualMeasureOrder.setRegionMax(peakValley[0]);
                manualMeasureOrder.setRegionMin(peakValley[1]);
                Logger.e("peakValley: " + Arrays.toString(peakValley));
                manualMeasureOrder.setRealTimeDoublePointNumber(ManualMeasureNewOrder.ECG_POINTS);
                bleOrder.measureEcg(manualMeasureOrder);
                initEcgSecondData();
            }
        }, false);
        responseBytes.setMeasureEcgData(measureEcgData);
    }

    private void initEcgSecondData() {
//        EventBus.getDefault().post(new StatusDataEvent(BleStatusConstant.STATUS_MEASURE_ECG_2));
        bleSender.ecgStartSecondStatus();
        if (measureEcgData == null) {
            bleCore.disconnect();
        }
        measureEcgData.setCallback(new MeasureEcgData.EcgCallback() {
            @Override
            public void onError(int errorCode) {
//                EventBus.getDefault().post(new ManualEcgError(errorCode));
                bleResponse.manualEcgError(new ManualEcgError(errorCode));
            }

            @Override
            public void drawData(double[] data, float sampleRate) {
                sendEcgData(data, sampleRate, 1);
            }

            @Override
            public void pwSuccess(float sample, int[] peakValley) {
            }
        }, true);
        responseBytes.setMeasureEcgData(measureEcgData);
    }

    private void initMeasurePwFirstData() {
        IRecognize recognize = new PwRecognize2();
        recognize.setComplete(BleConstant.MEASURE_PRE_PW_COMPLETE_NUMBER, BleConstant.MEASURE_PRE_PW_COMPLETE_NUMBER);
        // 通知Activity当前状态
//        sendBroadcastToActivity(BleStatusConstant.STATUS, BleStatusConstant.STATUS_MEASURE_PW_1, 0);
        measurePwData = new MeasurePwData(new MeasurePwData.PwCallback() {

            @Override
            public void drawData(ManualPwData data) {
//                EventBus.getDefault().post(data);
                bleResponse.manualPwData(data);

            }

            @Override
            public void pwSuccess(Object[] result, float sample, short[] peakValley) {
                ManualMeasureNewOrder manualMeasureOrder = new ManualMeasureNewOrder();
                manualMeasureOrder.setType(ManualMeasureNewOrder.PW);
                manualMeasureOrder.setRateType(0);
                manualMeasureOrder.setTag(ManualMeasureNewOrder.TAG_REAL_DATA);
                manualMeasureOrder.setRateValue(Math.round(sample));
                manualMeasureOrder.setEnable(ManualMeasureNewOrder.accEnable);
                manualMeasureOrder.setRegionAvailable(true);
                manualMeasureOrder.setRegionMax(peakValley[0]);
                manualMeasureOrder.setRegionMin(peakValley[1]);
                Logger.e("peakValley: " + Arrays.toString(peakValley));
                manualMeasureOrder.setRealTimeDoublePointNumber(ManualMeasureNewOrder.POINTS);
                bleOrder.measurePw(manualMeasureOrder);
                initMeasureSecondData();
            }

            @Override
            public void timeout() {
                measurePwData.reset();
            }
        }, false, recognize);
        responseBytes.setMeasurePwData(measurePwData);

    }

    private void initMeasureSecondData() {
        // 通知Activity当前状态
        IRecognize recognize = new PwRecognize2();
        recognize.setComplete(BleConstant.MEASURE_PW_COMPLETE_NUMBER_MIN, BleConstant.MEASURE_PW_COMPLETE_NUMBER_MAX);
        measurePwData = new MeasurePwData(new MeasurePwData.PwCallback() {

            @Override
            public void drawData(final ManualPwData data) {
//                EventBus.getDefault().post(data);
                bleSender.manualPwData(data);
            }

            @Override
            public void pwSuccess(Object[] result, float sampleRate, short[] peakValley) {
//                EventBus.getDefault().post(new SampleCompleteData(result));
                bleSender.getSampleCompleteManualData(new SampleCompleteData(result));
                cancelMeasure();
            }

            @Override
            public void timeout() {
                measurePwData.reset();
            }
        }, true, recognize);
        responseBytes.setMeasurePwData(measurePwData);
    }

    /**
     * 发送ECG数据
     *
     * @param data       数据
     * @param sampleRate 采样率
     * @param i          0 准备，1 正式
     */
    private void sendEcgData(double[] data, float sampleRate, int i) {
//        EventBus.getDefault().post(new ManualEcgData(data, sampleRate, i));
        bleSender.manualEcgData(new ManualEcgData(data, sampleRate, i));
    }
}
