package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.text.format.DateFormat;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.entity.SyncBloodPressure;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.entity.AppConfigNew;
import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.entity.net.response.GetFirstAidPhoneResponseEntity;
import com.mgtech.domain.entity.net.response.GetHomeDataResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.DataUseCase;
import com.mgtech.domain.interactor.MedicineUseCase;
import com.mgtech.domain.interactor.ServeUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.FirstAidPhoneModel;
import com.mgtech.maiganapp.data.model.HomeDataModel;
import com.mgtech.maiganapp.data.wrapper.FirstAidPhoneWrapper;
import com.mgtech.maiganapp.data.wrapper.HomeDataModelWrapper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;

/**
 * @author zhaixiang
 * 主页 view-model
 */

public class HomeViewModel extends BaseFragmentViewModel {
    public MutableLiveData<Boolean> renderEcgData = new MutableLiveData<>();
    public MutableLiveData<Boolean> renderPwData = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public ObservableBoolean haveEcgData = new ObservableBoolean(false);
    public ObservableBoolean havePwData = new ObservableBoolean(false);
    public ObservableBoolean haveMedicationRemind = new ObservableBoolean(false);
    public ObservableBoolean emergencyButtonHide = new ObservableBoolean(false);
    public ObservableBoolean emergencyServiceBought = new ObservableBoolean(true);
    public ObservableField<String> currentValueString = new ObservableField<>("");
    public ObservableField<String> currentUnitString = new ObservableField<>("");
    public ObservableField<String> currentTitleString = new ObservableField<>("");
    public ObservableField<String> pwTime = new ObservableField<>("");
    public ObservableField<String> ecgTime = new ObservableField<>("");
    public ObservableField<String> avgPwData = new ObservableField<>("");
    public ObservableField<String> medicationTimeText = new ObservableField<>("");
    public ObservableField<String> medicationCountText = new ObservableField<>("");

    private DataUseCase dataUseCase;
    private MedicineUseCase medicineUseCase;
    private ServeUseCase serveUseCase;
    public HomeDataModel model = new HomeDataModel();
    public String[] firstAidPhones;
    private IBraceletInfoManager manager;
    private boolean showHr;

    public HomeViewModel(Application context) {
        super(context);
        this.dataUseCase = ((MyApplication) context).getDataUseCase();
        this.medicineUseCase =((MyApplication) context).getMedicineUseCase();
        this.serveUseCase =((MyApplication) context).getServeUseCase();
        this.manager = new BraceletInfoManagerBuilder(context).create();
        showCircle();
    }

    public boolean isMan() {
        return UserInfo.getLocalUserInfo(getApplication()).getSex() == MyConstant.MAN;
    }

    public void getHomeData() {
        loading.setValue(true);
        dataUseCase.getHomePageData(SaveUtils.getUserId(getApplication()),
                new Subscriber<NetResponseEntity<GetHomeDataResponseEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showToast(getApplication().getString(R.string.network_error));
                        loading.setValue(false);
                    }

                    @Override
                    public void onNext(NetResponseEntity<GetHomeDataResponseEntity> netResponseEntity) {
                        if (netResponseEntity.getCode() == 0) {
                            GetHomeDataResponseEntity entity = netResponseEntity.getData();
                            model = HomeDataModelWrapper.getModelFromNet(entity);

                            Logger.i( "onNext: "+entity.getMedicationRemind());
                            entity.setLastTimeECGData(null);
                            saveCurrentBloodPressure();
                        } else if (!netResponseEntity.isCache()){
                            showToast(netResponseEntity.getMessage());
                        }
                        renderData();
                        loading.setValue(false);
                    }
                });
    }

    public void getFirstAidPhone(){
        serveUseCase.getFirstAidPhone(SaveUtils.getUserId(), new Subscriber<NetResponseEntity<GetFirstAidPhoneResponseEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(NetResponseEntity<GetFirstAidPhoneResponseEntity> netResponseEntity) {
                if (netResponseEntity.getCode() == 0){
                    GetFirstAidPhoneResponseEntity entity = netResponseEntity.getData();
                    if (entity != null){
                        FirstAidPhoneModel model = FirstAidPhoneWrapper.getModelFromNet(entity);
                        SaveUtils.setFirstAidPhone(model.phoneList);
                        SaveUtils.setFirstAidBought(model.isBought);
                        updateEmergencyState();
                    }
                }
            }
        });
    }

    /**
     * 保存数据，用来同步到手环
     */
    private void saveCurrentBloodPressure(){
        SyncBloodPressure syncBloodPressure = new SyncBloodPressure(model.currentMeasureTime,Math.round(model.currentPs)
                ,model.psLevel, Math.round(model.currentPd),model.pdLevel,model.currentV0,model.v0Level);
        manager.saveSyncBloodPressure(syncBloodPressure);
    }

    private void renderData() {
        if (model != null) {
            haveEcgData.set(model.ecgData != null && model.ecgData.length != 0);
            havePwData.set(model.psList != null && !model.psList.isEmpty() && model.pdList != null && !model.pdList.isEmpty());
            showCircle();
            if (haveEcgData.get()) {
                SaveUtils.saveLastEcgData(model.ecgTime);
                ecgTime.set(DateFormat.format(MyConstant.FULL_DATETIME_FORMAT, model.ecgTime).toString());
                renderEcgData.setValue(true);
            }else{
                ecgTime.set("");
            }
            if (havePwData.get()){
                SaveUtils.saveLastPwData(model.pwTime);
                pwTime.set(DateFormat.format(MyConstant.DATE_FORMAT, model.pwTime).toString());
                renderPwData.setValue(true);
                int avgPs = Math.round(getAvg(model.psList));
                int avgPd = Math.round(getAvg(model.pdList));
                avgPwData.set(String.format(Locale.getDefault(),getApplication().getString(R.string.home_avg_bp_of_all_day),avgPs,avgPd));
            }else{
                pwTime.set("");
            }
            if (model.nextDrugCount <= 0 || TextUtils.isEmpty(model.nextMedicationTime)){
                haveMedicationRemind.set(false);
            }else{
                medicationCountText.set(model.futureDrugCount+"");
                medicationTimeText.set(model.nextMedicationTime);
                haveMedicationRemind.set(true);
            }
        }
    }

    public void switchHeaderDisplay(){
        showHr = !showHr;
        showCircle();
    }

    private void showCircle(){
        if (showHr){
            int hr = Math.round(model.currentHr);
            if (hr == 0){
                currentValueString.set("-");
            }else{
                currentValueString.set(String.valueOf(hr));
            }
            currentUnitString.set(getApplication().getString(R.string.hr_unit_ch));
            currentTitleString.set(getApplication().getString(R.string.heart_rate));
        }else{
            int ps = Math.round(model.currentPs);
            int pd = Math.round(model.currentPd);
            if (ps == 0 || pd == 0) {
                currentValueString.set("-/-");
            } else {
                currentValueString.set(ps + "/" + pd);
            }
            currentUnitString.set(getApplication().getString(R.string.bp_unit_ch));
            currentTitleString.set(getApplication().getString(R.string.blood_pressure));
        }
    }

    private float getAvg(List<HomeDataModel.Data> list){
        int size = list.size();
        int sum = 0;
        for (HomeDataModel.Data data : list) {
            sum += data.value;
        }
        return sum/size;
    }

    /**
     * 更新解救按钮状态
     */
    public void updateEmergencyState() {
        emergencyButtonHide.set(SaveUtils.getEmergencyHide());
        emergencyServiceBought.set(SaveUtils.getFirstAidBought());
        firstAidPhones = SaveUtils.getFirstAidPhone();
    }

    @Override
    protected void onCleared() {
        dataUseCase.unSubscribe();
        medicineUseCase.unSubscribe();
        super.onCleared();
    }

}
