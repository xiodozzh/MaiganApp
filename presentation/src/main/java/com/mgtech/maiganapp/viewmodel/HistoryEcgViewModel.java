package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.mgtech.domain.entity.net.request.GetEcgListRequestEntity;
import com.mgtech.domain.entity.net.response.EcgResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.EcgUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.EcgModel;
import com.mgtech.maiganapp.data.wrapper.EcgModelWrapper;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * @author zhaixiang
 * ecg history
 */

public class HistoryEcgViewModel extends BaseViewModel {
    private Application context;
    public final ObservableBoolean showBackToday = new ObservableBoolean(false);
    public final ObservableField<String> displayMonthString = new ObservableField<>("");
    private Calendar currentCalendar = Calendar.getInstance();
    private Calendar workCalendar = Calendar.getInstance();
    private Calendar today = Calendar.getInstance();
    private EcgUseCase ecgUseCase;
    public final ObservableBoolean listDateLoading = new ObservableBoolean(false);
    public final ObservableBoolean hasNoListData = new ObservableBoolean(true);
    public final MutableLiveData<Boolean> updateListData = new MutableLiveData<>();
    public final ObservableBoolean isFriend = new ObservableBoolean(false);

    public List<EcgModel> currentEcgList;
    private String userId;
    private int dataLength;

    public HistoryEcgViewModel(Application context) {
        super(context);
        this.context = context;
        this.ecgUseCase = ((MyApplication) context).getEcgUseCase();
        this.currentEcgList = new ArrayList<>();
        displayMonthString.set(DateFormat.format(context.getString(R.string.ecg_display_month), currentCalendar).toString());
        dataLength = ViewUtils.calculateEcgDataLength(context);
    }


    public void setInitData(String id, long time) {
        this.userId = id;
        this.isFriend.set(!TextUtils.equals(id, SaveUtils.getUserId(getApplication())));
        this.currentCalendar.setTimeInMillis(time);
        refreshData();
    }

    public String getUserId() {
        return userId;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ecgUseCase.unSubscribe();
    }

    /**
     * 按时间段获取数据
     *
     * @param start 开始时间
     * @param end   结束时间
     */
    private void getEcgListData(long start, long end) {
        this.listDateLoading.set(true);
        GetEcgListRequestEntity entity = new GetEcgListRequestEntity(start, end, userId, dataLength);
        this.ecgUseCase.getEcgList(entity, new Subscriber<NetResponseEntity<List<EcgResponseEntity>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(context.getString(R.string.network_error));
                hasNoListData.set(true);
                listDateLoading.set(false);
            }

            @Override
            public void onNext(NetResponseEntity<List<EcgResponseEntity>> listNetResponseEntity) {
                if (listNetResponseEntity.getCode() != 0) {
                    showToast(listNetResponseEntity.getMessage());
                } else {
                    List<EcgResponseEntity> list = listNetResponseEntity.getData();
                    currentEcgList.clear();
                    if (list != null) {
                        currentEcgList.addAll(EcgModelWrapper.getModelFromNet(list));
                    }
                    hasNoListData.set(currentEcgList.isEmpty());
//                    updateListData.set(!updateListData.get());
                    updateListData.setValue(true);
                    listDateLoading.set(false);
                }
            }
        });
    }

    private void refreshData() {
        showBackToday.set(!isToday());
        displayMonthString.set(DateFormat.format(context.getString(R.string.ecg_display_month), currentCalendar).toString());
        workCalendar.setTimeInMillis(currentCalendar.getTimeInMillis());
        workCalendar.set(Calendar.HOUR_OF_DAY, 0);
        workCalendar.set(Calendar.MINUTE, 0);
        workCalendar.set(Calendar.SECOND, 0);
        workCalendar.set(Calendar.MILLISECOND, 0);
        long startTime = workCalendar.getTimeInMillis();
        workCalendar.add(Calendar.DATE, 1);
        long endTime = workCalendar.getTimeInMillis();
        getEcgListData(startTime, endTime);
    }

    private boolean isToday() {
        return currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                currentCalendar.get(Calendar.DATE) == today.get(Calendar.DATE);
    }

    /**
     * 设置日期
     *
     * @param year  年
     * @param month 月
     * @param day   日
     */
    public void setCurrentTime(int year, int month, int day) {
        this.currentCalendar.set(year, month, day);
        refreshData();
    }

    public void setCurrentTime(Calendar calendar) {
        this.currentCalendar.setTimeInMillis(calendar.getTimeInMillis());
        refreshData();
    }

    public void setCurrentTime(Date date) {
        this.currentCalendar.setTime(date);
        refreshData();
    }

    public Calendar getCurrentCalendar() {
        return currentCalendar;
    }


}
