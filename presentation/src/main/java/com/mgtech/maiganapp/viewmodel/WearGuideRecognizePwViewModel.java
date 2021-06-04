package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.ManualPwData;
import com.mgtech.blelib.entity.ManualPwError;
import com.mgtech.blelib.entity.SampleCompleteData;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.PwExampleItem;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class WearGuideRecognizePwViewModel extends BaseBleViewModel {
    private List<PwExampleItem> items;
    public final ObservableField<String> descriptionString = new ObservableField<>("");
    public final ObservableField<short[]> sampleData = new ObservableField<>(new short[]{});
    public final ObservableBoolean complete = new ObservableBoolean(false);
    public final ObservableField<String>  errorText = new ObservableField<> ("");
    public final ObservableBoolean showMeasureBtn = new ObservableBoolean(false);
    public final ObservableBoolean isPreparing = new ObservableBoolean(true);
    public final ObservableBoolean openBle = new ObservableBoolean(false);
    public final ObservableBoolean showErrorText = new ObservableBoolean(false);
    private Application context;
    private boolean isLeaving;

    public WearGuideRecognizePwViewModel(@NonNull Application application) {
        super(application);
        initItems();
        this.context = application;
    }

    private void initItems(){
        this.items = new ArrayList<>();

        PwExampleItem contentCorrectItem = new PwExampleItem();
        contentCorrectItem.setText("正确有效脉图");
        contentCorrectItem.setMainPicResource(R.drawable.wear_guide_recognize_pw_correct);
        items.add(contentCorrectItem);

        PwExampleItem contentWrongItem1 = new PwExampleItem();
        contentWrongItem1.setText("常见错误脉图1");
        contentWrongItem1.setMainPicResource(R.drawable.wear_guide_recognize_pw_error1);
        items.add(contentWrongItem1);

        PwExampleItem contentWrongItem2 = new PwExampleItem();
        contentWrongItem2.setText("常见错误脉图2");
        contentWrongItem2.setMainPicResource(R.drawable.wear_guide_recognize_pw_error2);
        items.add(contentWrongItem2);

        PwExampleItem contentWrongItem3 = new PwExampleItem();
        contentWrongItem3.setText("常见错误脉图3");
        contentWrongItem3.setMainPicResource(R.drawable.wear_guide_recognize_pw_error3);
        items.add(contentWrongItem3);
    }

    public List<PwExampleItem> getItems() {
        return items;
    }

    /**
     * 检查连接状态
     */
    @Override
    public void checkLinkState(){
        bleRequest.bluetoothGetLinkStatus();
    }

    @Override
    protected void onBluetoothStatus(int statusType) {
        switch (statusType) {
            case BleStatusConstant.STATUS_DISCONNECTED:
                if (ViewUtils.isScreenOn(context) && !background) {
                    linkIfAllowed();
                }
                break;
            case BleStatusConstant.STATUS_SLEEP:
                if (!isLeaving && !showErrorText.get()){
                    startMeasure();
                }
                break;
        }
    }
    @Override
    protected void onLinkStatusReceived(boolean isConnected) {
        if (isConnected){
            startMeasure();
        }else if (ViewUtils.isScreenOn(context) && !background){
            linkIfAllowed();
        }
    }

    @Override
    protected void onStateChange(int bleState) {

    }

    class PWResponseCallback extends DefaultResponseCallback{
        @Override
        public void onManualPWDataReceived(ManualPwData data) {
            setWorking(true);
            sampleData.set(data.getData());
        }

        @Override
        public void onManualPWErrorDataReceived(ManualPwError errorType) {
            showErrorText.set(true);
            switch (errorType.getErrorCode()){
                case ManualPwError.ERROR_TYPE_PROCESS:
                case ManualPwError.ERROR_TYPE_PARAM:
                    errorText.set(context.getString(R.string.measure_params_error_try_again));
                    break;
                case ManualPwError.ERROR_TYPE_LOW_POWER:
                    errorText.set(context.getString(R.string.battery_low_please_try_after_charging));
                    break;
                case ManualPwError.ERROR_TYPE_CHARGING:
                    errorText.set(context.getString(R.string.is_charging_can_not_get_pw));
                    break;
                case ManualPwError.ERROR_TYPE_NO_WEAR:
                    errorText.set(context.getString(R.string.measure_fail_content_not_wear));
                    break;
            }
        }

        @Override
        public void onManualPWCompleteDataReceived(SampleCompleteData result) {
        }

        @Override
        public void onManualStop() {
            setWorking(false);
            complete.set(!complete.get());
        }
    }

    @Override
    protected DefaultResponseCallback getResponseCallback() {
        return new PWResponseCallback();
    }

    public void leave(){
        isLeaving = true;
        stopMeasure();
    }

    public void startMeasure(){
        bleRequest.bluetoothMeasurePwWithoutRecognize(false);
        showErrorText.set(false);
        errorText.set("");
        setWorking(true);
    }

    public void stopMeasure(){
        bleRequest.bluetoothStopMeasure();
    }

}
