package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.mgtech.domain.entity.net.request.DeletePwRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.PwDataResponseEntity;
import com.mgtech.domain.interactor.DataUseCase;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.AutoPwCalculatedEvent;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.data.wrapper.IndicatorDataWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class HistoryHealthListViewModel extends BaseFragmentViewModel {
    public final ObservableBoolean listDateLoading = new ObservableBoolean(false);
    public final ObservableBoolean getDataSuccess = new ObservableBoolean(false);
    public final ObservableBoolean clearData = new ObservableBoolean(false);
    public final ObservableBoolean hasNoListData = new ObservableBoolean(true);
    public final ObservableBoolean showBackToday = new ObservableBoolean(false);
    public final ObservableBoolean deleteSuccess = new ObservableBoolean(false);
    public final ObservableBoolean isFriend = new ObservableBoolean(false);

    public final ObservableField<String> calendarString = new ObservableField<>("");
    private DataUseCase useCase;
    public List<IndicatorDataModel> dataList = new ArrayList<>();

    private Calendar calendar = Calendar.getInstance();
    public String userId;
    public boolean isMan;

    public HistoryHealthListViewModel(Application context) {
        super(context);
        this.useCase = ((MyApplication) context).getDataUseCase();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
        this.isFriend.set(!TextUtils.equals(userId,SaveUtils.getUserId(getApplication())));
    }

    public void setMan(boolean isMan){
        this.isMan = isMan;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar.setTimeInMillis(calendar.getTimeInMillis());
        this.showBackToday.set(!Utils.isSameDay(calendar, Calendar.getInstance()));
        calendarString.set(DateFormat.format("yyyy-MM", calendar).toString());
        getData();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void autoPwCalculated(AutoPwCalculatedEvent event){
        getData();
    }

    public void getData() {
        dataList.clear();
        listDateLoading.set(true);
        clearData.set(!clearData.get());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.calendar.getTimeInMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, 1);
        long endTime = calendar.getTimeInMillis();
        useCase.getDataByDateRange(userId, startTime, endTime, NetConstant.NO_CACHE,
                new Subscriber<NetResponseEntity<List<PwDataResponseEntity>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listDateLoading.set(false);
                        showToast(getApplication().getString(R.string.network_error));
                        getDataSuccess.set(!getDataSuccess.get());
                    }

                    @Override
                    public void onNext(NetResponseEntity<List<PwDataResponseEntity>> netResponseEntity) {
                        listDateLoading.set(false);
                        if (netResponseEntity.getCode() == 0) {
                            Log.i(TAG, "getData: " + netResponseEntity);
                            List<PwDataResponseEntity> list = netResponseEntity.getData();
                            dataList.clear();
                            if (list != null) {
                                dataList.addAll(IndicatorDataWrapper.getListFromNet(list));
                            }
                        } else {
                            showToast(netResponseEntity.getMessage());
                        }
                        hasNoListData.set(dataList.isEmpty());
                        getDataSuccess.set(!getDataSuccess.get());
                    }
                });
    }

    public void deleteData(final int index) {
        if (index < 0 || index >= dataList.size()) {
            return;
        }
        IndicatorDataModel model = dataList.get(index);
        useCase.deleteData(new DeletePwRequestEntity(model.id,userId),
                new Subscriber<NetResponseEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(NetResponseEntity netResponseEntity) {
                if (netResponseEntity.getCode() == 0){
                    showToast("删除成功");
                    dataList.remove(index);
                    deleteSuccess.set(!deleteSuccess.get());
                }else{
                    showToast(netResponseEntity.getMessage());
                }
            }
        });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }


}
