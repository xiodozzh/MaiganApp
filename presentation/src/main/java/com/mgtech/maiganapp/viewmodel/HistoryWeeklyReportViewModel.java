package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;

import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.WeeklyReportResponseEntity;
import com.mgtech.domain.interactor.WeeklyReportUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.data.model.WeeklyReportModel;
import com.mgtech.maiganapp.data.wrapper.WeeklyReportModelWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class HistoryWeeklyReportViewModel extends BaseViewModel {
    private WeeklyReportUseCase useCase;
    public List<WeeklyReportModel> models = new ArrayList<>();
    public ObservableBoolean reloadModel = new ObservableBoolean(false);
    public ObservableBoolean loading = new ObservableBoolean(false);
    public ObservableBoolean isEmpty = new ObservableBoolean(true);
    public ObservableBoolean networkError = new ObservableBoolean(false);


    public HistoryWeeklyReportViewModel(@NonNull Application application) {
        super(application);
        useCase = ((MyApplication)application).getWeeklyReportUseCase();
    }


    public void getWeeklyReportList(){
        loading.set(true);
        useCase.getWeeklyReportList(SaveUtils.getUserId(getApplication()),
                new Subscriber<NetResponseEntity<List<WeeklyReportResponseEntity>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                loading.set(false);
                networkError.set(true);
            }

            @Override
            public void onNext(NetResponseEntity<List<WeeklyReportResponseEntity>> netResponseEntity) {
                if (netResponseEntity.getCode() == 0){
                    List<WeeklyReportResponseEntity> list = netResponseEntity.getData();
                    models.clear();
                    models.addAll(WeeklyReportModelWrapper.getModelListFromNet(list));
                }else{
                    showToast(netResponseEntity.getMessage());
                }
                isEmpty.set(models.isEmpty());
                reloadModel.set(!reloadModel.get());
                networkError.set(false);
                loading.set(false);
            }
        });
    }

    public void getWeeklyReportListTest(){
        models.clear();
        WeeklyReportModel model = new WeeklyReportModel();
        Calendar calendar = Calendar.getInstance();
        model.endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE,-7);
        model.startTime = calendar.getTimeInMillis();
        model.shareTitle = "血压控制情况";
        model.weekReportGuid = "1";
        model.indicators = new ArrayList<>();
        IndicatorDataModel indicatorDataModel = new IndicatorDataModel();
        indicatorDataModel.time = calendar.getTimeInMillis();
        indicatorDataModel.ps = 120;
        indicatorDataModel.pd = 100;
        indicatorDataModel.achieveGoal = false;
        model.weekReportDetailUrl = "http://192.168.1.131:8080/week/index.html";
        model.indicators.add(indicatorDataModel);
        models.add(model);
        isEmpty.set(models.isEmpty());
        reloadModel.set(!reloadModel.get());
        networkError.set(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscriber();
    }
}
