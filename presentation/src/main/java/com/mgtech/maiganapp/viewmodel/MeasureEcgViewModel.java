package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.BleStateEvent;
import com.mgtech.blelib.entity.ManualEcgData;
import com.mgtech.blelib.entity.ManualEcgError;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.entity.net.request.SaveEcgRequestEntity;
import com.mgtech.domain.entity.net.response.EcgResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.EcgUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.EcgModel;
import com.mgtech.maiganapp.data.wrapper.EcgModelWrapper;
import com.mgtech.maiganapp.service.BluetoothService;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 * ecg测量
 */

public class MeasureEcgViewModel extends BaseBleViewModel {
    private Application context;
    private float sampleRate = MyConstant.DEFAULT_ECG_SAMPLE_RATE;
    public static final int MAX_PROGRESS = MyConstant.ECG_SAVING_TIME;
    private static final int OFFSET = 15;
    public final ObservableField<String> descriptionString = new ObservableField<>("");
    public final ObservableField<String> errorText = new ObservableField<>("");

    public final ObservableField<String> hrString = new ObservableField<>("");
    public final ObservableField<double[]> sampleData = new ObservableField<>(new double[]{});
    public final ObservableBoolean complete = new ObservableBoolean(false);
    public final ObservableBoolean cancelMeasure = new ObservableBoolean(false);
    public final ObservableBoolean warning = new ObservableBoolean(false);
    public final ObservableBoolean clearScreen = new ObservableBoolean(false);
    public final ObservableBoolean showMeasureBtn = new ObservableBoolean(false);
    public final ObservableBoolean isEcgPrepare = new ObservableBoolean(true);
    public final ObservableBoolean bleClosed = new ObservableBoolean(false);
    public final ObservableBoolean timeout = new ObservableBoolean(false);
    public final ObservableBoolean showGuide = new ObservableBoolean(false);
    public final ObservableBoolean networkError = new ObservableBoolean(false);
//    public final ObservableInt progress = new ObservableInt(MAX_PROGRESS);
    public final MediatorLiveData<Integer> progress = new MediatorLiveData<>();
    public final ObservableField<String> progressText = new ObservableField<>(MAX_PROGRESS+"");
    public final ObservableField<String> showErrorDialog = new ObservableField<>("");
    public EcgModel resultModel;

    private EcgUseCase ecgUseCase;
    private CountDownTimer countDownTimer;
    private boolean isMeasureFinish;
    private boolean isLeaving;
    private List<Integer> dataList;
    private IBraceletInfoManager manager;
    private static Handler mHandler = new Handler();
    private static final long TIME_OUT = 5000;
    private SaveEcgRequestEntity saveEcgRequestEntity;

    public MeasureEcgViewModel(Application context) {
        super(context);
        this.context = context;
        this.dataList = new ArrayList<>();
        this.ecgUseCase = ((MyApplication) context).getEcgUseCase();
        this.manager = new BraceletInfoManagerBuilder(context).create();
        progress.setValue(MAX_PROGRESS);
    }

    @Override
    protected void onBluetoothStatus(int statusType) {
        switch (statusType) {
            case BleStatusConstant.STATUS_DISCONNECTED:
                cancelTimeOut();
                if (ViewUtils.isScreenOn(context) && !background) {
                    linkIfAllowed();
                }
                clearScreen.set(!clearScreen.get());
                this.warning.set(false);
                setWorking(false);
                this.isEcgPrepare.set(true);
                this.showMeasureBtn.set(false);
                warning(false);
                cancelCountDown();

                break;
            case BleStatusConstant.STATUS_SLEEP:
                cancelTimeOut();
                warning(false);
                this.warning.set(false);
                this.showMeasureBtn.set(true);
                this.descriptionString.set(context.getString(R.string.measure_ecg_start));
                break;
            case BleStatusConstant.STATUS_MEASURE_ECG_1:
                isEcgPrepare.set(true);
                setWorking(true);
                measuringPeakValley();
                break;
            case BleStatusConstant.STATUS_MEASURE_ECG_2:
                isEcgPrepare.set(false);
                startCountDown();
                setWorking(true);
                measuring();
                break;
            default:
        }
    }

    @Override
    protected void onLinkStatusReceived(boolean isConnected) {
        if (!isConnected) {
            linkIfAllowed();
        } else {
            link();
            this.descriptionString.set(context.getString(R.string.measure_ecg_start));
        }
    }

    @Override
    protected void onStateChange(int bleState) {
        if (bleState == BleStateEvent.STATE_OFF) {
            bleClosed.set(!bleClosed.get());
            stateViewModel.bluetoothClose();
        } else if (bleState == BleStateEvent.STATE_ON) {
            if (manager.isPaired()) {
                checkLinkState();
            }
        }
    }

    @Override
    protected DefaultResponseCallback getResponseCallback() {
        return new ECGResponseCallback();
    }

    class ECGResponseCallback extends DefaultResponseCallback{

        @Override
        public void onManualECGDataReceived(ManualEcgData data) {
            cancelTimeOut();
            if (isMeasureFinish) {
                return;
            }
            sampleRate = data.getSampleRate();
            if (data.getIndex() == 1) {
                synchronized (this) {
                    for (double i : data.getData()) {
                        dataList.add((int) Math.round(i));
                    }
                }
                descriptionString.set(context.getString(R.string.measure_ecg_measure_normal));
            }else{
                descriptionString.set(context.getString(R.string.measure_ecg_prepare_normal));
            }
            sampleData.set(data.getData());
        }

        @Override
        public void onManualECGErrorDataReceived(ManualEcgError errorType) {
            Log.i(TAG, "onManualErrorDataReceived: " + errorType);
            cancelTimeOut();
            switch (errorType.getErrorCode()) {
                case ManualEcgError.ERROR_TYPE_PROCESS:
                case ManualEcgError.ERROR_TYPE_PARAM:
                    errorText.set(context.getString(R.string.measure_params_error_try_again));
                    warning.set(true);
                    cancelCountDown();
                    break;
                case ManualEcgError.ERROR_TYPE_LOW_POWER:
                    errorText.set(context.getString(R.string.battery_low_please_try_after_charging));
                    warning.set(true);
                    cancelCountDown();
                    break;
                case ManualEcgError.ERROR_TYPE_CHARGING:
                    errorText.set(context.getString(R.string.is_charging_can_not_measure));
                    warning.set(true);
                    cancelCountDown();
                    break;
                case ManualEcgError.ERROR_TYPE_NO_WEAR:
                    errorText.set(context.getString(R.string.measure_fail_content_not_wear));
                    warning.set(true);
                    cancelCountDown();
                    break;
                case ManualEcgError.ERROR_TYPE_LEAD_OFF:
                    warning.set(true);
                    break;
                default:
                    warning.set(false);
            }
        }

        @Override
        public void onManualStop() {
            Log.i(TAG, "onManualSampleStop " + isLeaving);
            cancelTimeOut();
            clearScreen.set(!clearScreen.get());
            setWorking(false);
            isEcgPrepare.set(true);
            if (isLeaving) {
                cancelMeasure.set(!cancelMeasure.get());
            }
        }
    }

    private Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            bleRequest.bluetoothDisconnect();
            timeout.set(!timeout.get());
        }
    };

    /**
     * 测量心率
     */
    public synchronized void startPrepareMeasure() {
        launchTimeOut();
        this.dataList.clear();
        this.bleRequest.bluetoothMeasureECG();
        this.showErrorDialog.set("");
        this.isMeasureFinish = false;
        this.isEcgPrepare.set(true);
        this.errorText.set("");
    }

    /**
     * 设置超时
     */
    private void launchTimeOut() {
        mHandler.removeCallbacks(timeoutRunnable);
        mHandler.postDelayed(timeoutRunnable, TIME_OUT);
    }

    private void cancelTimeOut() {
        mHandler.removeCallbacks(timeoutRunnable);
    }

    private void test() {
        Single.create(new Single.OnSubscribe<double[]>() {
            @Override
            public void call(SingleSubscriber<? super double[]> singleSubscriber) {
                Source source = Okio.source(getApplication().getResources().openRawResource(R.raw.ecg_data));
                BufferedSource buffer = Okio.buffer(source);
                try {
                    String content = buffer.readString(Charset.defaultCharset());
                    String[] s = content.trim().split(",");
                    double[] data = new double[s.length];
                    for (int i = 0; i < s.length; i++) {
                        data[i] = Double.parseDouble(s[i]);
                    }
                    singleSubscriber.onSuccess(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<double[]>() {
                    @Override
                    public void onSuccess(double[] string) {
                        sendDataToNet(string);
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });
    }

    /**
     * 停止采集
     */
    public void stopMeasureAndLeave() {
        cancelTimeOut();
        isLeaving = true;
        isMeasureFinish = true;
        bleRequest.bluetoothStopMeasure();
        cancelCountDown();
    }

    /**
     * 停止采集
     */
    public void stopMeasureNotLeave() {
        progress.setValue(MAX_PROGRESS);
        cancelCountDown();
        bleRequest.bluetoothStopMeasure();
    }

    /**
     * 上传数据
     */
    private void sendDataToNet(double[] data) {
        Log.e(TAG, "sendDataToNet: " + Arrays.toString(data));
        Calendar c = Calendar.getInstance();
        SaveEcgRequestEntity.DeviceInfoBean bean = new SaveEcgRequestEntity.DeviceInfoBean();
        bean.setMacAddress(manager.getAddress());
        bean.setFirmwareVersion(manager.getDeviceFirmwareVersionString());
        bean.setProtocolVersion(manager.getDeviceProtocolVersionString());
        saveEcgRequestEntity = new SaveEcgRequestEntity(bean, SaveUtils.getUserId(getApplication()),
                c.getTimeInMillis(), data, (float) data[1]);
        calculateData();
    }

    public void calculateData() {
        if (saveEcgRequestEntity == null) {
            return;
        }
        showDialog(context.getString(R.string.uploading), context.getString(R.string.please_wait));
        ecgUseCase.saveEcg(saveEcgRequestEntity, new Subscriber<NetResponseEntity<EcgResponseEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

                showToast(context.getString(R.string.network_error));
                e.printStackTrace();
                cancelDialog();
                networkError.set(!networkError.get());
                clearScreen.set(!clearScreen.get());
            }

            @Override
            public void onNext(NetResponseEntity<EcgResponseEntity> responseEntity) {
                int code = responseEntity.getCode();
                if (code == 0){
                    showToast(context.getString(R.string.ecg_upload_complete));
                    EcgResponseEntity ecgResponseEntity = responseEntity.getData();
                    if (ecgResponseEntity != null) {
                        resultModel = EcgModelWrapper.getModelFromNet(ecgResponseEntity);
                        resultModel.userId = SaveUtils.getUserId(getApplication());
                        measureComplete();
                    } else {
                        showToast(context.getString(R.string.network_error));
                    }
                }else if (code == -2){
                    errorText.set(context.getString(R.string.measure_ecg_fail));
                }else{
                    errorText.set(responseEntity.getMessage());
                }
                clearScreen.set(!clearScreen.get());
                cancelDialog();
            }
        });
    }


    /**
     * 已经连接
     */
    private void link() {
        this.descriptionString.set(context.getString(R.string.measure_ecg_start));
        this.warning.set(false);
        this.showMeasureBtn.set(true);
    }

    /**
     * 测量峰谷值
     */
    private void measuringPeakValley() {
        this.stateViewModel.showBondLayout.set(false);
        this.stateViewModel.showMask.set(false);
        this.stateViewModel.showScanLayout.set(false);
        this.descriptionString.set(context.getString(R.string.measure_ecg_prepare_normal));
        this.warning.set(false);
        this.showMeasureBtn.set(true);
    }

    /**
     * 测量
     */
    private void measuring() {
        this.stateViewModel.showBondLayout.set(false);
        this.stateViewModel.showMask.set(false);
        this.stateViewModel.showScanLayout.set(false);
        this.descriptionString.set(context.getString(R.string.measure_ecg_measure_normal));
        this.warning.set(false);
        this.showMeasureBtn.set(true);

    }

    /**
     * 显示warning信息
     *
     * @param isWarning true不显示，false显示
     */
    private void warning(boolean isWarning) {
        this.warning.set(isWarning);
    }


    /**
     * 测量完成
     */
    private void measureComplete() {
        cancelDialog();
        this.clearScreen.set(!clearScreen.get());
        this.hrString.set("");
        this.complete.set(!complete.get());
    }

    /**
     * 测量取消
     */
    private void measureCancel() {
        this.cancelMeasure.set(!cancelMeasure.get());
    }


    private void startCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(MyConstant.ECG_SAVING_TIME * 1000L + 500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                progress.setValue((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                isMeasureFinish = true;
                cancelTimeOut();
                synchronized (this) {
                    int size = dataList.size();
                    double[] result = new double[size + 2];
                    result[0] = 1;
                    result[1] = sampleRate;
                    for (int i = 0; i < size; i++) {
                        result[i + 2] = dataList.get(i) + OFFSET;
                    }
                    sendDataToNet(result);
                    bleRequest.bluetoothStopMeasure();
                }

            }
        };
        countDownTimer.start();
    }

    private synchronized void cancelCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            progress.setValue(MAX_PROGRESS);
            dataList.clear();
        }
    }

    @Override
    protected void onCleared() {
        this.ecgUseCase.unSubscribe();
        this.cancelCountDown();
        cancelTimeOut();
        super.onCleared();
    }


    public void openBle() {
        bleRequest.bluetoothLink();
    }

    @Override
    public boolean isPaired() {
        return manager.isPaired();
    }

    public synchronized void clear() {
        dataList.clear();
        clearScreen.set(!clearScreen.get());
        setWorking(false);
    }
}

