package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import android.text.format.DateFormat;
import android.util.Log;

import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.entity.net.response.GetHealthManagementResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.DataUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.HealthManagerDataModel;
import com.mgtech.maiganapp.data.wrapper.HealthManagerDataModelWrapper;
import com.orhanobut.logger.Logger;

import java.util.Calendar;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class HealthManagerViewModel extends BaseFragmentViewModel {
    private static final String DATE_FORMAT = "yyyy.MM.dd";
    private long startTime;
    private long endTime;
    public ObservableField<String> calendarText = new ObservableField<>("");
    public ObservableField<String> avgValue = new ObservableField<>("-");
    public ObservableField<String> minValue = new ObservableField<>("-");
    public ObservableField<String> maxValue = new ObservableField<>("-");
    public ObservableField<String> suggestString = new ObservableField<>("");
    public ObservableField<String> goalString = new ObservableField<>("-/-");
    public ObservableField<String> achieveDays = new ObservableField<>("0");
    public ObservableField<String> notAchieveDays = new ObservableField<>("0");
    private DataUseCase dataUseCase;
    public MutableLiveData<Boolean> showMonthData = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public HealthManagerDataModel model;

    public HealthManagerViewModel(@NonNull Application application) {
        super(application);
        initCalendarText();
        suggestString.set(application.getString(R.string.health_manage_suggest));
        dataUseCase = ((MyApplication) application).getDataUseCase();
    }

    private void initCalendarText() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE,1);
        endTime = calendar.getTimeInMillis();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        startTime = calendar.getTimeInMillis();
        String displayText = DateFormat.format(DATE_FORMAT, startTime) + "-" + DateFormat.format(DATE_FORMAT, endTime-1);
        calendarText.set(displayText);
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getTotalDays(){
        return Utils.calculateNumberOfDaysBetweenDays(startTime,endTime);
    }

    public void getData() {
        loading.setValue(true);
        dataUseCase.getHealthManagement(SaveUtils.getUserId(getApplication())
                , startTime, endTime,
                new Subscriber<NetResponseEntity<GetHealthManagementResponseEntity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("error: HealthManagerViewModel getData "+e.getMessage());
                        showToast(getApplication().getString(R.string.network_error));
                        loading.setValue(false);
                    }

                    @Override
                    public void onNext(NetResponseEntity<GetHealthManagementResponseEntity> netResponseEntity) {
                        Log.i(TAG, "onNext:"+ netResponseEntity);
                        if (netResponseEntity.getCode() == 0){
                            GetHealthManagementResponseEntity entity = netResponseEntity.getData();
                            if (entity != null){
                                model = HealthManagerDataModelWrapper.getModelFromNet(entity);
                            }else{
                                model = null;
                            }
                            Log.i(TAG, "onNext: "+model);
                            renderModel();
                        }else {
                            showToast(netResponseEntity.getMessage());
                        }
                        loading.setValue(false);
                    }
                });
    }


    private void renderModel(){
        showMaxMinAvgData(model);
        if (model != null){
            suggestString.set(model.suggest);
            goalString.set(Math.round(model.psGoal) + "/" + Math.round(model.pdGoal));
            achieveDays.set(String.valueOf(model.normalDays));
            notAchieveDays.set(String.valueOf(model.abnormalDays));
            showMonthData.setValue(true);
        }
    }


    /**
     * 显示最大、最小、平均值
     */
    private void showMaxMinAvgData(HealthManagerDataModel model) {
        if (model == null || model.list == null || model.list.isEmpty()) {
            maxValue.set("-");
            minValue.set("-");
            avgValue.set("-");
            return;
        }
        maxValue.set(Math.round(model.getMaxPs()) + "/" +
                Math.round(model.getMaxPd()));
        minValue.set(Math.round(model.getMinPs()) + "/" +
                Math.round(model.getMinPd()));
        avgValue.set(Math.round(model.getAvgPs()) + "/" +
                Math.round(model.getAvgPd()));
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        dataUseCase.unSubscribe();
    }
}
