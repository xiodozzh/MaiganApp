package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import android.text.format.DateFormat;

import com.mgtech.domain.entity.net.request.GetStepRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.StepDetailItemEntity;
import com.mgtech.domain.interactor.StepUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.StepModel;
import com.mgtech.maiganapp.data.wrapper.StepModelWrapper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;

/**
 * @author zhaixiang
 * 运动页面View-Model
 */

public class SportViewModel extends BaseViewModel {
    private Application context;
    public static final int TYPE_DAY = 0;
    public static final int TYPE_WEEK = 1;
    public static final int TYPE_MONTH = 2;
    public int type = TYPE_DAY;
    public List<StepModel> stepDetailList;
    public List<StepModel> stepList;
    /**
     * 缓存占位数据
     */
    private List<StepModel> cacheList;
    public final ObservableBoolean dataDataUpdate = new ObservableBoolean(false);
    public final ObservableBoolean detailDataUpdate = new ObservableBoolean(false);
    public final ObservableBoolean loading = new ObservableBoolean(false);
    public final ObservableField<String> descTitle = new ObservableField<>("");
    private StepUseCase useCase;
    public StepModel selectModel;


    public SportViewModel(Application context) {
        super(context);
        this.context = context;
        stepList = new ArrayList<>();
        stepDetailList = new ArrayList<>();
        cacheList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            cacheList.add(new StepModel());
        }
        this.useCase = ((MyApplication) context).getStepUseCase();
    }

    private int getListNumber() {
        switch (type) {
            case TYPE_DAY:
                return 60;
            case TYPE_WEEK:
                return 52;
            case TYPE_MONTH:
                return 24;
            default:
        }
        return 100;
    }


    private int getNetSpanType() {
        switch (type) {
            case TYPE_DAY:
                return GetStepRequestEntity.DAY;
            case TYPE_WEEK:
                return GetStepRequestEntity.WEEK;
            case TYPE_MONTH:
                return GetStepRequestEntity.MONTH;
            default:
                return GetStepRequestEntity.DAY;
        }
    }

    public void refreshData() {
        getStatisticData();
    }

    public void setType(int type) {
        this.type = type;
    }

    private void getStatisticData() {
        final long time[] = getStartEndTime();
        loading.set(true);
        GetStepRequestEntity entity = new GetStepRequestEntity(SaveUtils.getUserId(context),
                getNetSpanType(), time[0], time[1]);
        useCase.getStepDetail(NetConstant.CACHE_THEN_NET, entity, new Subscriber<NetResponseEntity<List<StepDetailItemEntity>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                initStepList(time[0]);
                loading.set(false);
                dataDataUpdate.set(!dataDataUpdate.get());
            }

            @Override
            public void onNext(NetResponseEntity<List<StepDetailItemEntity>> netResponseEntity) {
                Logger.i( "getStepStatisticsByDate: " + netResponseEntity);
                initStepList(time[0]);
                if (netResponseEntity.getCode() == 0) {
                    List<StepDetailItemEntity> list = netResponseEntity.getData();
                    if (list != null) {
                        List<StepModel> items = StepModelWrapper.getModelListFromNet(list);
                        for (StepModel model : items) {
                            for (StepModel displayModel : stepList) {
                                if (displayModel.startTime == model.startTime) {
                                    displayModel.stepCount = model.stepCount;
                                    displayModel.duration = model.duration;
                                    displayModel.distance = model.distance;
                                    displayModel.heat = model.heat;
                                    displayModel.distance = model.distance;
                                    break;
                                } else if (displayModel.startTime > model.startTime) {
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    showToast(netResponseEntity.getMessage());
                }
                loading.set(false);
                dataDataUpdate.set(!dataDataUpdate.get());
                getDataDetail(stepList.size()-1);
            }
        });
    }


    private long[] getStartEndTime() {
        Calendar workCalendar = Calendar.getInstance();
        workCalendar.set(Calendar.HOUR_OF_DAY, 0);
        workCalendar.set(Calendar.MINUTE, 0);
        workCalendar.set(Calendar.SECOND, 0);
        workCalendar.set(Calendar.MILLISECOND, 0);
        long endTime = 0;
        long startTime = 0;
        int listNumber = getListNumber();
        switch (type) {
            case TYPE_DAY:
                workCalendar.add(Calendar.DATE, 1);
                endTime = workCalendar.getTimeInMillis();
                workCalendar.add(Calendar.DATE, -listNumber);
                startTime = workCalendar.getTimeInMillis();
                break;
            case TYPE_WEEK:
                int dayInWeek = workCalendar.get(Calendar.DAY_OF_WEEK);
                workCalendar.add(Calendar.DATE, workCalendar.getFirstDayOfWeek() - dayInWeek + 7);
                endTime = workCalendar.getTimeInMillis();
                workCalendar.add(Calendar.DATE, -7 * listNumber);
                startTime = workCalendar.getTimeInMillis();
                break;
            case TYPE_MONTH:
                workCalendar.set(Calendar.DAY_OF_MONTH, 1);
                workCalendar.add(Calendar.MONTH, 1);
                endTime = workCalendar.getTimeInMillis();
                workCalendar.add(Calendar.MONTH, -getListNumber());
                startTime = workCalendar.getTimeInMillis();
                break;
            default:
        }
        return new long[]{startTime, endTime};
    }


    private void initStepList(long start) {
        int totalNumber = getListNumber();
        int currentSize = stepList.size();
        int diff = currentSize - totalNumber;
        if (diff > 0) {
            for (int i = 0; i < diff; i++) {
                stepList.remove(stepList.size() - 1);
            }
        } else {
            for (int i = currentSize; i < totalNumber; i++) {
                stepList.add(cacheList.get(i));
            }
        }
        Calendar work = Calendar.getInstance();
        for (int i = 0; i < totalNumber; i++) {

            StepModel model = stepList.get(i);
            model.stepCount = 0;
            model.heat = 0;
            model.distance = 0;
            model.duration = 0;
            model.startTime = start;
            work.setTimeInMillis(start);
            switch (type) {
                case TYPE_DAY:
                    if (i == totalNumber - 1) {
                        model.dateString = context.getString(R.string.sport_this_day);
                    } else {
                        model.dateString = DateFormat.format(MyConstant.DISPLAY_DATE_2, model.startTime).toString();
                    }
                    work.add(Calendar.DATE, 1);
                    start = work.getTimeInMillis();
                    break;
                case TYPE_WEEK:
                    work.add(Calendar.DATE, 7);
                    start = work.getTimeInMillis();
                    if (i == totalNumber - 1) {
                        model.dateString = context.getString(R.string.sport_this_week);
                    } else {
                        model.dateString = DateFormat.format(MyConstant.DISPLAY_DATE_2, model.startTime)
                                + "-" + DateFormat.format(MyConstant.DISPLAY_DATE_2, start - 1);
                    }
                    break;
                case TYPE_MONTH:
                    int month = work.get(Calendar.MONTH) + 1;
                    if (month == 1) {
                        model.dateString = DateFormat.format(context.getString(R.string.sport_date_format_step_scroll_year), work).toString();
                    } else {
                        model.dateString = DateFormat.format(context.getString(R.string.sport_date_format_step_scroll_month), work).toString();
                    }
                    if (i == totalNumber - 1) {
                        model.dateString = context.getString(R.string.sport_this_month);
                    }
                    work.add(Calendar.MONTH, 1);
                    start = work.getTimeInMillis();
                    break;
                default:
            }
            model.endTime = start;
        }
    }


    /**
     * 获取计步详细数据
     */
    public void getDataDetail(int index) {
        if (index >= stepList.size()) {
            return;
        }
        StepModel model = stepList.get(index);
        String text = "";
        switch (type) {
            case TYPE_DAY:
                text = context.getString(R.string.sport_this_day);
                if (index != stepList.size()-1) {
                    text = DateFormat.format(context.getString(R.string.sport_date_format_month_day), model.startTime).toString();
                }
                break;
            case TYPE_WEEK:
                text = stepList.get(index).dateString;
                break;
            case TYPE_MONTH:
                text = context.getString(R.string.sport_this_month);
                if (index != stepList.size()-1) {
                    text = DateFormat.format(context.getString(R.string.sport_date_format_month), model.startTime).toString();
                }
                break;
            default:
        }
        descTitle.set(String.format(Locale.getDefault(), context.getString(R.string.sport_format_sport_time), text));
        getDetail(model);
    }

    /**
     * 获取详细数据网络请求
     */
    private void getDetail(final StepModel model) {
        if (selectModel == model) {
            return;
        }
        selectModel = model;
        int detailType = 0;
        switch (type) {
            case TYPE_DAY:
                detailType = GetStepRequestEntity.HOUR;
                break;
            case TYPE_WEEK:
                detailType = GetStepRequestEntity.DAY;
                break;
            case TYPE_MONTH:
                detailType = GetStepRequestEntity.DAY;
                break;
            default:
        }
        GetStepRequestEntity entity = new GetStepRequestEntity(SaveUtils.getUserId(getApplication()),
                detailType, model.startTime, model.endTime);
        useCase.getStepDetail(NetConstant.CACHE_IF_NET_ERROR, entity, new Subscriber<NetResponseEntity<List<StepDetailItemEntity>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                stepDetailList.clear();
                detailDataUpdate.set(!detailDataUpdate.get());
            }

            @Override
            public void onNext(NetResponseEntity<List<StepDetailItemEntity>> listNetResponseEntity) {
                Logger.i( "onNext: " + listNetResponseEntity);
                stepDetailList.clear();
                if (listNetResponseEntity.getCode() == 0) {
                    List<StepDetailItemEntity> list = listNetResponseEntity.getData();
                    if (list != null) {
                        List<StepModel> models = StepModelWrapper.getModelListFromNet(list);
                        for (StepModel model : models) {
                            if (model.stepCount > 0.5f) {
                                stepDetailList.add(model);
                            }
                        }
                    }
                } else {
                    showToast(listNetResponseEntity.getMessage());
                }
                detailDataUpdate.set(!detailDataUpdate.get());
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.useCase.unSubscribe();
    }
}
