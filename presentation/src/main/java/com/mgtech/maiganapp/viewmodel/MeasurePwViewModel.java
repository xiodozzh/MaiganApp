package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.BleStateEvent;
import com.mgtech.blelib.entity.ManualPwData;
import com.mgtech.blelib.entity.ManualPwError;
import com.mgtech.blelib.entity.ManualSampleData;
import com.mgtech.blelib.entity.SampleCompleteData;
import com.mgtech.blelib.entity.SyncBloodPressure;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.PwDataResponseEntity;
import com.mgtech.domain.interactor.DataUseCase;
import com.mgtech.domain.interactor.PersonalInfoUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.data.wrapper.IndicatorDataWrapper;
import com.mgtech.maiganapp.service.BluetoothService;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Subscriber;

/**
 * @author zhaixiang
 * 测量 view-model
 */

public class MeasurePwViewModel extends BaseBleViewModel {
    private static final long TIME_DIFFER = 1000;

    private Application context;
    public final ObservableField<String> normalStateText = new ObservableField<>("");
    public final ObservableField<String> movementStateText = new ObservableField<>("");
    public final ObservableField<short[]> sampleData = new ObservableField<>(new short[]{});
    public final ObservableBoolean complete = new ObservableBoolean(false);
    public final ObservableField<String> errorText = new ObservableField<>("");
    public String errorTitle = "";
    public final ObservableBoolean cancelMeasure = new ObservableBoolean(false);
    public final ObservableBoolean showAccWarningText = new ObservableBoolean(false);
    public final ObservableBoolean warningText = new ObservableBoolean(false);
    public final ObservableBoolean stuck10 = new ObservableBoolean(false);
//    public final ObservableBoolean stuck30 = new ObservableBoolean(false);
    public final MutableLiveData<Boolean> stuck30 = new MutableLiveData<>();
    public final ObservableBoolean clearScreen = new ObservableBoolean(false);
    public final ObservableBoolean showUserInfoIncompleteDialog = new ObservableBoolean(false);
    public final ObservableBoolean isPreparing = new ObservableBoolean(true);
    public final ObservableBoolean openBle = new ObservableBoolean(false);
    public final ObservableBoolean showErrorDialog = new ObservableBoolean(false);
    public final ObservableBoolean timeoutError = new ObservableBoolean(false);
    public final ObservableBoolean bleClosed = new ObservableBoolean(false);
    //    public final ObservableBoolean timeoutAndGuide = new ObservableBoolean(false);
    public final ObservableField<String> showCalculateErrorDialog = new ObservableField<>("");
    public final ObservableInt measureProgress = new ObservableInt(0);

    public IndicatorDataModel resultModel;
    public Calendar localSaveTime;

    private List<Object> dataList;
    private UserInfo userInfo;
    private DataUseCase dataUseCase;
    private PersonalInfoUseCase personalInfoUseCase;
    private long lastComplete;
    private boolean isLeaving;
    private IBraceletInfoManager manager;
    private static Handler handler = new Handler();
    public boolean isMan;
    private int lastProgress;
    private boolean lastPackageMeasure = false;
    public float sampleRate = 128;

    public MeasurePwViewModel(Application context) {
        super(context);
        this.context = context;
        this.manager = new BraceletInfoManagerBuilder(context).create();
        this.dataList = new ArrayList<>();
        this.dataUseCase = ((MyApplication) context).getDataUseCase();
        this.personalInfoUseCase = ((MyApplication) context).getPersonInfoUseCase();
    }


    @Override
    protected void onBluetoothStatus(int statusType) {
        Log.e(TAG, "measure pw view model: " + statusType);
        switch (statusType) {
            case BleStatusConstant.STATUS_DISCONNECTED:
                cancelDialog();
                cancelTimeoutNoData();
                cancelProgressStuckTimeOut();
                clearScreen.set(!clearScreen.get());
                this.showAccWarningText.set(false);
                if (ViewUtils.isScreenOn(context) && !background) {
                    linkIfAllowed();
                }
                break;
            case BleStatusConstant.STATUS_SLEEP:
                cancelTimeoutNoData();
                cancelProgressStuckTimeOut();
                showAccWarningText.set(false);
                setBeforeClickText();
                break;
            default:
        }
    }

    @Override
    protected void onLinkStatusReceived(boolean isConnected) {
        Log.i(TAG, "onLinkStatusReceived: " + isConnected);
        if (!isConnected) {
            showAccWarningText.set(false);
            linkIfAllowed();
        } else {
            setBeforeClickText();
        }
    }

    @Override
    protected void onStateChange(int state) {
        if (state == BleStateEvent.STATE_OFF) {
            bleClosed.set(!bleClosed.get());
            stateViewModel.bluetoothClose();
        } else if (state == BleStateEvent.STATE_ON) {
            if (manager.isPaired()) {
                checkCanUserMeasure();
            }
        }
    }

    @Override
    protected DefaultResponseCallback getResponseCallback() {
        return new PWResponseCallback();
    }

    class PWResponseCallback extends DefaultResponseCallback{

        @Override
        public void onManualPWDataReceived(ManualPwData data) {
            sampleRate = data.getSampleRate();
            cancelTimeoutNoData();
            setWorking(true);
            showData(data.getData());
            Log.e(TAG, "onManualSampleDataReceived: " + data.getPercent() + " " + data.getErrorCode());
            if (data.isRealData()) {
                if (!lastPackageMeasure) {
                    launchProgressStuckTimeOut();
                }
                measureProgress.set(data.getPercent());
                if (data.getPercent() == 100) {
                    bleRequest.bluetoothGetStoredManualData();
                    showDialog(context.getString(R.string.calculating), context.getString(R.string.please_wait));
                }
            }
            isWorking.set(true);
            Log.e(TAG, "data.getErrorCode() : "+ data.getErrorCode());
            if (data.getErrorCode() == ManualSampleData.ERROR_TYPE_MOTIVATION) {
                if (data.isRealData()) {
                    setMeasureMovementText();
                } else {
                    setPrepareMovementText();
                }
            } else if (data.getErrorCode() == ManualSampleData.ERROR_SIGNAL_AMP_LOW) {
                setSignalLow();
            } else if (data.getErrorCode() == ManualSampleData.ERROR_SIGNAL_AMP_HIGH) {
                setSignalHigh();
            } else if (data.getErrorCode() == ManualSampleData.ERROR_SIGNAL_WEAR_TIGHT) {
                setWearTight();
            } else if (data.getErrorCode() == ManualSampleData.ERROR_SIGNAL_WEAR_LOOSE) {
                setWearLoose();
            } else if (data.getErrorCode() == ManualSampleData.ERROR_SIGNAL_WRONG) {
                setSignalWrong();
            } else if (data.getErrorCode() == ManualSampleData.ERROR_SIGNAL_HR_HIGH) {
                errorTitle = getApplication().getString(R.string.measure_fail_content_hr_low);
                errorText.set(getApplication().getString(R.string.measure_fail_title_hr_low));
                stopMeasureNotLeave();
            } else if (data.getErrorCode() == ManualSampleData.ERROR_SIGNAL_HR_LOW) {
                errorTitle = getApplication().getString(R.string.measure_fail_content_hr_high);
                errorText.set(getApplication().getString(R.string.measure_fail_title_hr_high));
                stopMeasureNotLeave();
            } else {
                if (data.isRealData()) {
                    setMeasureNormalText();
                } else {
                    setPrepareNormalText();
                }
            }
            setProgressTimer(data.getPercent(), data.getErrorCode() == ManualSampleData.ERROR_TYPE_MOTIVATION ||
                    data.getErrorCode() == ManualSampleData.ERROR_SIGNAL_AMP_HIGH);
            Log.e(TAG, "showAccWarningText: " + showAccWarningText.get());
            isPreparing.set(!data.isRealData());
            errorText.set("");
            lastPackageMeasure = data.isRealData();
        }

        @Override
        public void onManualPWErrorDataReceived(ManualPwError errorType) {
            cancelTimeoutNoData();
            clearScreen.set(!clearScreen.get());
            switch (errorType.getErrorCode()) {
                case ManualPwError.ERROR_TYPE_PROCESS:
                case ManualPwError.ERROR_TYPE_PARAM:
                    errorText.set(context.getString(R.string.measure_params_error_try_again));
                    break;
                case ManualPwError.ERROR_TYPE_LOW_POWER:
                    errorTitle = context.getString(R.string.measure_fail_title_low_power);
                    errorText.set(context.getString(R.string.measure_fail_content_low_power));
                    break;
                case ManualPwError.ERROR_TYPE_CHARGING:
                    errorTitle = context.getString(R.string.measure_fail_title_charging);
                    errorText.set(context.getString(R.string.measure_fail_content_charging));
                    break;
                case ManualPwError.ERROR_TYPE_NO_WEAR:
                    errorTitle = context.getString(R.string.measure_fail_title_not_wear);
                    errorText.set(context.getString(R.string.measure_fail_content_not_wear));
                    break;
                case ManualPwError.ERROR_TYPE_BUSY:
                    errorText.set(context.getString(R.string.is_busy_can_not_measure));
                    break;
//                    case ManualPwError.ERROR_SIGNAL_HR_LOW:
//                        errorText.set("心率过低");
//                        break;
//                    case ManualPwError.ERROR_SIGNAL_HR_HIGH:
//                        errorText.set("心率过高");
//                        break;
                default:
            }
        }

        @Override
        public void onManualPWCompleteDataReceived(SampleCompleteData result) {
            if (result.isAuto()) {
                return;
            }
            measureProgress.set(100);
            dataList = new ArrayList<>();
            dataList.addAll(result.getData());
            localSaveTime = Calendar.getInstance();
            sendDataToNet();
        }

        @Override
        public void onManualStop() {
            cancelTimeoutNoData();
            cancelProgressStuckTimeOut();
            clearScreen.set(!clearScreen.get());
            if (isLeaving) {
                measureCancel();
            }
            setWorking(false);
            showAccWarningText.set(false);
        }
    }

    public void destroy() {
        this.dataUseCase.unSubscribe();
        this.personalInfoUseCase.unSubscribe();
        cancelTimeoutNoData();
        cancelProgressStuckTimeOut();
    }

    /**
     * 是否绑定
     */
    @Override
    public boolean isPaired() {
        boolean isPaired = manager.isPaired();
        this.stateViewModel.showBondLayout.set(!isPaired);
        return isPaired;
    }

    /**
     * 检查用户信息是否完整
     */
    public void checkCanUserMeasure() {
        userInfo = UserInfo.getLocalUserInfo(context);
        if (!userInfo.isComplete()) {
            getUserInfoFromNet();
        } else {
            isMan = userInfo.getSex() == MyConstant.MAN;
            checkLink();
        }
    }

    private void setBeforeClickText() {
        showAccWarningText.set(false);
        stuck10.set(false);
        normalStateText.set(context.getString(R.string.please_click_button_and_start_measure));
    }

    private void setPrepareMovementText() {
        showAccWarningText.set(true);
        stuck10.set(false);
        movementStateText.set(context.getString(R.string.measure_pw_prepare_state_movement));
    }

    private void setMeasureMovementText() {
        showAccWarningText.set(true);
        movementStateText.set(context.getString(R.string.measure_pw_measure_state_movement));
    }

    private void setSignalHigh() {
        showAccWarningText.set(true);
        movementStateText.set(context.getString(R.string.measure_pw_measure_state_signal_high));
    }

    private void setSignalLow() {
        showAccWarningText.set(true);
        movementStateText.set(context.getString(R.string.measure_pw_measure_state_signal_low));
    }

    private void setWearTight() {
        showAccWarningText.set(true);
        movementStateText.set(context.getString(R.string.measure_pw_measure_state_tight));
    }

    private void setWearLoose() {
        showAccWarningText.set(true);
        movementStateText.set(context.getString(R.string.measure_pw_measure_state_loose));
    }

    private void setSignalWrong() {
        showAccWarningText.set(true);
        movementStateText.set("未知原因");
    }

    private void setPrepareNormalText() {
        showAccWarningText.set(false);
        normalStateText.set(context.getString(R.string.measure_pw_prepare_normal));
    }

    private void setMeasureNormalText() {
        showAccWarningText.set(false);
        normalStateText.set(context.getString(R.string.measure_pw_measure_normal));
    }

    private void setProgressStopForTenSecond() {
        showAccWarningText.set(false);
        stuck10.set(true);
    }

    private void setProgressTimer(int currentProgress, boolean reset) {
        if (lastProgress >= 100 || currentProgress >= 100) {
            cancelProgressStuckTimeOut();
            return;
        }
        if (reset || lastProgress != currentProgress) {
            cancelProgressStuckTimeOut();
            launchProgressStuckTimeOut();
        }
        lastProgress = currentProgress;
    }

    private void launchProgressStuckTimeOut() {
        handler.removeCallbacks(progressStuck10Runnable);
        handler.postDelayed(progressStuck10Runnable, 10000);
        setStuck30();
    }

    private void cancelProgressStuckTimeOut() {
        stuck10.set(false);
        handler.removeCallbacks(progressStuck10Runnable);
        cancelStuck30();
    }

    private void cancelStuck30() {
        handler.removeCallbacks(progressStuck30Runnable);
    }

    private void setStuck30() {
        if (SaveUtils.doesNeverShowWearErrorDialog()) {
            return;
        }
        handler.removeCallbacks(progressStuck30Runnable);
        handler.postDelayed(progressStuck30Runnable, 30000);
    }

    private Runnable progressStuck10Runnable = new Runnable() {
        @Override
        public void run() {
            setProgressStopForTenSecond();
        }
    };

    private Runnable progressStuck30Runnable = () -> {
//        stuck30.setValue(!stuck30.get());
        stuck30.setValue(true);
        bleRequest.bluetoothStopMeasure();
    };


    public void checkLink() {
        boolean isPaired = manager.isPaired();
        if (isPaired) {
            if (isBleOn()) {
                bleRequest.bluetoothGetLinkStatus();
            } else {
                stateViewModel.bluetoothClose();
            }
        } else {
            stateViewModel.unbind();
        }
    }

    /**
     * 获取个人信息
     */
    private void getUserInfoFromNet() {
        personalInfoUseCase.getInfo(NetConstant.NO_CACHE, SaveUtils.getUserId(context),
                new Subscriber<NetResponseEntity<PersonalInfoEntity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showUserInfoIncompleteDialog.set(!showUserInfoIncompleteDialog.get());
                    }

                    @Override
                    public void onNext(NetResponseEntity<PersonalInfoEntity> netResponseEntity) {
                        if (netResponseEntity != null) {
                            if (netResponseEntity.getCode() != 0) {
                                showToast(netResponseEntity.getMessage());
                                showUserInfoIncompleteDialog.set(!showUserInfoIncompleteDialog.get());
                            } else {
                                PersonalInfoEntity entity = netResponseEntity.getData();
                                UserInfo.saveLocalUserInfo(context, entity);
                                checkCanUserMeasure();
                            }
                        } else {
                            showUserInfoIncompleteDialog.set(!showUserInfoIncompleteDialog.get());
                        }
                    }
                });
    }


    /**
     * 测量心率
     */
    public void startPrepareMeasure() {
        this.isWorking.set(true);
        this.isPreparing.set(true);
        this.errorText.set("");
        this.bleRequest.bluetoothMeasureAutoPw();
        setTimeoutNoData();
        launchProgressStuckTimeOut();
    }

    private Runnable timeOutNoDataRunnable = () -> {
        bleRequest.bluetoothDisconnect();
        timeoutError.set(!timeoutError.get());
    };


//    private Runnable timeOutRunnable = () -> {
//        bluetoothModel.bluetoothDisconnect();
//        timeoutError.set(!timeoutError.get());
//    };

    private void cancelTimeoutNoData() {
        handler.removeCallbacks(timeOutNoDataRunnable);
    }

    private void setTimeoutNoData() {
        handler.removeCallbacks(timeOutNoDataRunnable);
        handler.postDelayed(timeOutNoDataRunnable, 3000);
    }


//    @Override
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void getAppForegroundEvent(AppForegroundEvent event) {
//        background = !event.isForeground();
//        Log.i(TAG, "getAppForegroundEvent: measure"+background);
//    }

    /**
     * 停止采集
     */
    public void stopMeasureAndLeave() {
        isLeaving = true;
        bleRequest.bluetoothStopMeasure();
    }

    /**
     * 停止采集
     */
    public void stopMeasureNotLeave() {
        isLeaving = false;
        cancelTimeoutNoData();
        cancelProgressStuckTimeOut();
        bleRequest.bluetoothStopMeasure();
    }

    /**
     * 上传数据
     */
    private void sendDataToNet() {
        showDialog(context.getString(R.string.calculating), context.getString(R.string.please_wait));
        this.showCalculateErrorDialog.set("");
        this.dataUseCase.calculateData(dataList, localSaveTime.getTimeInMillis(),
                userInfo.getAlgrithomSex(), Math.round(userInfo.getHeight()),
                Math.round(userInfo.getWeight()), userInfo.getAge(context), manager.getAddress(),
                manager.getDeviceProtocolVersionString(),
                manager.getDeviceFirmwareVersionString(),
                new Subscriber<NetResponseEntity<PwDataResponseEntity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        cancelDialog();
                        showErrorDialog.set(!showErrorDialog.get());
                    }

                    @Override
                    public void onNext(NetResponseEntity<PwDataResponseEntity> netResponseEntity) {
                        cancelDialog();
                        if (netResponseEntity != null) {
                            if (netResponseEntity.getCode() == 0) {
                                PwDataResponseEntity entity = netResponseEntity.getData();
                                if (entity != null) {
                                    resultModel = IndicatorDataWrapper.getModelFromPwDataResponseEntity(entity);
                                    saveBloodPressureData(resultModel);
                                    bleRequest.bluetoothResetAutoSampleTimeAndSyncBloodPressure();
                                    dataList.clear();
                                    dataUseCase.unSubscribe();
                                    measureComplete();
                                } else {
                                    calculateFail(context.getString(R.string.activity_measure_pw_calculate_fail));
                                }
                            } else {
                                String msg = netResponseEntity.getMessage();
                                if (TextUtils.isEmpty(msg)) {
                                    calculateFail(context.getString(R.string.please_change_whether_information_is_right));
                                } else {
                                    calculateFail(msg);
                                }
                            }
                        } else {
                            calculateFail(context.getString(R.string.upload_raw_data_fail));
                        }
                    }
                }
        );
    }

    public void uploadAgain() {
        sendDataToNet();
    }

    /**
     * 缓存血压值
     */
    private void saveBloodPressureData(IndicatorDataModel model) {
        SyncBloodPressure syncBloodPressure = new SyncBloodPressure(localSaveTime.getTimeInMillis(), Math.round(model.ps),
                model.psLevel, Math.round(model.pd), model.pdLevel, model.v0, model.v0Level);
        IBraceletInfoManager manager = new BraceletInfoManagerBuilder(context).create();
        manager.saveSyncBloodPressure(syncBloodPressure);
    }

    /**
     * 显示数据
     *
     * @param data 数据
     */
    private void showData(short[] data) {
        this.sampleData.set(data);
    }

    /**
     * 测量完成
     */
    private void measureComplete() {
        long curr = Calendar.getInstance().getTimeInMillis();
        if (curr - lastComplete > TIME_DIFFER) {
            this.clearScreen.set(!clearScreen.get());
            this.complete.set(!complete.get());
        }
        this.lastComplete = curr;
    }

    /**
     * 测量取消
     */
    private void measureCancel() {
        this.cancelMeasure.set(!cancelMeasure.get());
    }

    private void calculateFail(String text) {
        dataList.clear();
        this.showAccWarningText.set(false);
        this.clearScreen.set(!clearScreen.get());
        this.showCalculateErrorDialog.set(text);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "onCleared: ");
        dataUseCase.unSubscribe();
        cancelTimeoutNoData();
        cancelProgressStuckTimeOut();
        checkCanUserMeasure();
    }

    public void clear() {
        clearScreen.set(!clearScreen.get());
        showAccWarningText.set(false);
        isWorking.set(false);
        cancelTimeoutNoData();
        cancelProgressStuckTimeOut();
        checkCanUserMeasure();
    }

}
