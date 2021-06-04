package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.PwStatisticDataResponseEntity;
import com.mgtech.domain.interactor.DataUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.AutoPwCalculatedEvent;
import com.mgtech.maiganapp.data.model.IndicatorStatisticDataModel;
import com.mgtech.maiganapp.data.wrapper.IndicatorStatisticDataWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.List;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class HistoryHealthGraphViewModel extends BaseFragmentViewModel {
    public static final int DAY = 0;
    public static final int MONTH = 1;

    public static final int BP = 0;
    public static final int V0 = 1;
    public static final int TPR = 2;
    public static final int HR = 3;
    public static final int CO = 4;
    public static final int PM = 5;

    public ObservableField<String> currentDateText = new ObservableField<>("");
    private Calendar calendar = Calendar.getInstance();
    private static final String DAY_FORMAT = "yyyy/MM/dd";
    private static final String MONTH_FORMAT = "yyyy/MM";
    private int timeType;
    private int indicatorType;
    public ObservableBoolean updateGraphData = new ObservableBoolean(false);
    public ObservableBoolean clearGraphData = new ObservableBoolean(false);
    public ObservableBoolean isFriend = new ObservableBoolean(false);
    public ObservableField<String> avgValue = new ObservableField<>("");
    public ObservableField<String> maxValue = new ObservableField<>("");
    public ObservableField<String> minValue = new ObservableField<>("");
    public ObservableField<String> unit = new ObservableField<>("");
    private DataUseCase useCase;
    private String userId;
    public Calendar rangeCalendarStart;
    public Calendar rangeCalendarEnd;
    public IndicatorStatisticDataModel dataModel;

    public HistoryHealthGraphViewModel(@NonNull Application application) {
        super(application);
        showCalendar();
        useCase = ((MyApplication) application).getDataUseCase();
//        getGraphData();
        initTimeRange();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
        this.isFriend.set(!TextUtils.equals(userId,SaveUtils.getUserId(getApplication())));
    }

    private void initTimeRange(){
        rangeCalendarStart = Calendar.getInstance();
        rangeCalendarStart.add(Calendar.YEAR,-100);
        rangeCalendarStart.set(Calendar.HOUR_OF_DAY,0);
        rangeCalendarStart.set(Calendar.MINUTE,0);
        rangeCalendarStart.set(Calendar.SECOND,0);
        rangeCalendarStart.set(Calendar.MILLISECOND,0);
        rangeCalendarEnd = Calendar.getInstance();
        rangeCalendarEnd.set(Calendar.HOUR_OF_DAY,23);
        rangeCalendarEnd.set(Calendar.MINUTE,59);
        rangeCalendarEnd.set(Calendar.SECOND,59);
        rangeCalendarEnd.set(Calendar.MILLISECOND,999);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void autoPwCalculated(AutoPwCalculatedEvent event){
        getGraphData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 设置时间模式
     *
     * @param type 0 日 1 月
     */
    public void setTimeType(int type) {
        timeType = type;
        showCalendar();
        getGraphData();
    }

    public int getTimeType() {
        return timeType;
    }

    public int getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(int indicatorType) {
        this.indicatorType = indicatorType;
        showDisplayValue(dataModel);
    }

    public int getCurrentMonthDay() {
        return Utils.getMonthDay(calendar);
    }

    /**
     * 下个日期
     */
    public void nextDate() {
        if (timeType == DAY) {
            calendar.add(Calendar.DATE, 1);
            if (calendar.after(rangeCalendarEnd)){
                calendar.add(Calendar.DATE,-1);
                return;
            }
        } else {
            calendar.add(Calendar.MONTH, 1);
            if (calendar.after(rangeCalendarEnd)){
                calendar.add(Calendar.MONTH,-1);
                return;
            }
        }
        showCalendar();
        getGraphData();
    }

    /**
     * 上个日期
     */
    public void lastDate() {
        if (timeType == DAY) {
            calendar.add(Calendar.DATE, -1);
            if (calendar.before(rangeCalendarStart)){
                calendar.add(Calendar.DATE,1);
                return;
            }
        } else {
            calendar.add(Calendar.MONTH, -1);
            if (calendar.before(rangeCalendarStart)){
                calendar.add(Calendar.MONTH,1);
                return;
            }
        }
        showCalendar();
        getGraphData();
    }

    /**
     * 设置日期
     *
     * @param c 日期
     */
    public void setDate(Calendar c) {
        calendar.setTimeInMillis(c.getTimeInMillis());
        showCalendar();
        getGraphData();
    }

    public Calendar getCalendar() {
        return calendar;
    }


    /**
     * 显示当前日期
     */
    private void showCalendar() {
        if (timeType == DAY) {
            currentDateText.set(DateFormat.format(DAY_FORMAT, calendar).toString());
        } else if (timeType == MONTH) {
            currentDateText.set(DateFormat.format(MONTH_FORMAT, calendar).toString());
        }
    }


    /**
     * 载入信息
     */
    public void getGraphData() {
        switch (timeType) {
            case DAY:
                getDayData(calendar.getTimeInMillis());
                break;
            case MONTH:
                getAvgDayByMonth(calendar.getTimeInMillis());
                break;
            default:
        }
    }

    /**
     * 获取当天数据
     *
     * @param time 时间
     */
    private void getDayData(long time) {
        clearGraphData.set(!clearGraphData.get());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        useCase.getStatisticDataByDate(userId,calendar.getTimeInMillis(), false,
                new Subscriber<NetResponseEntity<PwStatisticDataResponseEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dataLoadingFail();
                    }

                    @Override
                    public void onNext(NetResponseEntity<PwStatisticDataResponseEntity> netResponseEntity) {
                        Log.i(TAG, "onNext: "+ netResponseEntity);
                        if (netResponseEntity.getCode() == 0) {
                            dataLoadingSuccess(netResponseEntity.getData());
                        } else {
                            dataLoadingFail();
                        }
                    }
                });
    }

    /**
     * 获取当月日数据平均值
     *
     * @param time 时间
     */
    private void getAvgDayByMonth(long time) {
        clearGraphData.set(!clearGraphData.get());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        long startTime = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH,1);
        long endTime = calendar.getTimeInMillis();
        useCase.getStatisticDataByDateRange(userId,startTime,endTime, false,
                new Subscriber<NetResponseEntity<PwStatisticDataResponseEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dataLoadingFail();
                    }

                    @Override
                    public void onNext(NetResponseEntity<PwStatisticDataResponseEntity> netResponseEntity) {
                        if (netResponseEntity.getCode() == 0) {
                            dataLoadingSuccess(netResponseEntity.getData());
                        } else {
                            dataLoadingFail();
                        }
                    }
                });
    }

    /**
     * 数据载入成功
     *
     * @param entity 载入的数据
     */
    private void dataLoadingSuccess(PwStatisticDataResponseEntity entity) {
        if (entity == null){
            dataModel = new IndicatorStatisticDataModel();
        }else {
            dataModel = IndicatorStatisticDataWrapper.getModelFromPwStatisticDataResponseEntity(entity);
        }
        Log.i(TAG, "dataLoadingSuccess: "+dataModel);
        updateGraphData.set(!updateGraphData.get());
        showDisplayValue(dataModel);
    }

    private void dataLoadingFail() {
        dataModel = null;
        updateGraphData.set(!updateGraphData.get());
        showDisplayValue(null);
    }

    /**
     * 显示最大、最小、平均值
     */
    private void showDisplayValue(IndicatorStatisticDataModel currentData) {
        if (currentData == null || currentData.hrList.isEmpty()) {
            maxValue.set("-");
            minValue.set("-");
            avgValue.set("-");
            unit.set("");
            return;
        }
        List<IndicatorStatisticDataModel.Data> dataList;
        switch (indicatorType) {
            case HistoryHealthGraphViewModel.BP:
                unit.set(getApplication().getString(R.string.PS_unit));
                List<IndicatorStatisticDataModel.Data> psList = currentData.psList;
                List<IndicatorStatisticDataModel.Data> pdList = currentData.pdList;
                maxValue.set(Math.round(getMax(psList)) + "/" +
                        Math.round(getMax(pdList)));
                minValue.set(Math.round(getMin(psList)) + "/" +
                        Math.round(getMin(pdList)));
                avgValue.set(Math.round(getAvg(psList)) + "/" +
                        Math.round(getAvg(pdList)));
                break;
            case HistoryHealthGraphViewModel.HR:
                unit.set(getApplication().getString(R.string.HR_unit));
                dataList = currentData.hrList;
                maxValue.set(String.valueOf(Math.round(getMax(dataList))));
                minValue.set(String.valueOf(Math.round(getMin(dataList))));
                avgValue.set(String.valueOf(Math.round(getAvg(dataList))));
                break;
            case HistoryHealthGraphViewModel.CO:
                unit.set(getApplication().getString(R.string.CO_unit));
                dataList = currentData.coList;
                maxValue.set(String.valueOf(Math.round(getMax(dataList))));
                minValue.set(String.valueOf(Math.round(getMin(dataList))));
                avgValue.set(String.valueOf(Math.round(getAvg(dataList))));
                break;

            case HistoryHealthGraphViewModel.TPR:
                unit.set(getApplication().getString(R.string.TPR_unit));
                dataList = currentData.tprList;
                maxValue.set(String.valueOf(Math.round(getMax(dataList))));
                minValue.set(String.valueOf(Math.round(getMin(dataList))));
                avgValue.set(String.valueOf(Math.round(getAvg(dataList))));
                break;

            case HistoryHealthGraphViewModel.PM:
                unit.set(getApplication().getString(R.string.PM_unit));
                dataList = currentData.pmList;
                maxValue.set(String.valueOf(Math.round(getMax(dataList))));
                minValue.set(String.valueOf(Math.round(getMin(dataList))));
                avgValue.set(String.valueOf(Math.round(getAvg(dataList))));
                break;

            case HistoryHealthGraphViewModel.V0:
                unit.set(getApplication().getString(R.string.V0_unit));
                dataList = currentData.v0List;
                maxValue.set(String.valueOf(Math.round(getMax(dataList))));
                minValue.set(String.valueOf(Math.round(getMin(dataList))));
                avgValue.set(String.valueOf(Math.round(getAvg(dataList))));
                break;
            default:
        }
    }

    private float getMax(List<IndicatorStatisticDataModel.Data> list) {
        float max = Float.MIN_VALUE;
        for (IndicatorStatisticDataModel.Data data : list) {
            max = Math.max(max, data.value);
        }
        return max;
    }

    private float getMin(List<IndicatorStatisticDataModel.Data> list) {
        float min = Float.MAX_VALUE;
        for (IndicatorStatisticDataModel.Data data : list) {
            min = Math.min(min, data.value);
        }
        return min;
    }

    private float getAvg(List<IndicatorStatisticDataModel.Data> list) {
        float sum = 0;
        int total = 0;
        for (IndicatorStatisticDataModel.Data data : list) {
            total++;
            sum += data.value;
        }
        if (total == 0) {
            return 0;
        } else {
            return sum / total;
        }
    }
}
