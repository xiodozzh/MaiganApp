package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.ThresholdRequestEntity;
import com.mgtech.domain.interactor.ExceptionUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.ThresholdModel;
import com.mgtech.maiganapp.data.wrapper.ThresholdModelWrapper;
import com.orhanobut.logger.Logger;

import java.util.Locale;

import rx.Subscriber;

/**
 * @author zhaixiang
 * @date 2018/1/3
 * 设置推送阈值
 */

public class SettingPushViewModel extends BaseViewModel {
//    public final ObservableBoolean psOn = new ObservableBoolean(false);
//    public final ObservableBoolean pdOn = new ObservableBoolean(false);
//    public final ObservableBoolean loading = new ObservableBoolean(false);
    public final ObservableBoolean error = new ObservableBoolean(false);

    public MutableLiveData<Boolean>psOn = new MutableLiveData<>();
    public MutableLiveData<Boolean>pdOn = new MutableLiveData<>();
    public MutableLiveData<Boolean>loading = new MutableLiveData<>();
    public final ObservableField<String> psRangeString = new ObservableField<>("");
    public final ObservableField<String> pdRangeString = new ObservableField<>("");
    public final ObservableField<String> notificationAuthString = new ObservableField<>("");
    private ExceptionUseCase thresholdUseCase;
    public ThresholdModel thresholdModel;

    public SettingPushViewModel(Application application) {
        super(application);
        this.thresholdUseCase = ((MyApplication) application).getExceptionUseCase();
    }

    public void getNotificationAuth() {
        if (NotificationManagerCompat.from(getApplication()).areNotificationsEnabled()) {
            notificationAuthString.set(getApplication().getString(R.string.notification_on));
        } else {
            notificationAuthString.set(getApplication().getString(R.string.notification_off));
        }
    }

    /**
     * 获取提醒阈值
     */
    public void getThreshold() {
        this.loading.setValue(true);
        this.thresholdUseCase.getThreshold(SaveUtils.getUserId(getApplication()),
                new Subscriber<NetResponseEntity<ThresholdRequestEntity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        loading.setValue(false);
                        error.set(true);
                        showToast(getApplication().getString(R.string.network_error));
                    }

                    @Override
                    public void onNext(NetResponseEntity<ThresholdRequestEntity> netResponseEntity) {
                        Logger.i("onNext: " + netResponseEntity);
                        loading.setValue(false);
                        if (netResponseEntity.getCode() == 0) {
                            ThresholdRequestEntity thresholdRequestEntity = netResponseEntity.getData();
                            if (thresholdRequestEntity != null) {
                                thresholdModel = ThresholdModelWrapper.getModelFromNet(thresholdRequestEntity);
                            }else {
                                thresholdModel = new ThresholdModel();
                                thresholdModel.open = true;
                                thresholdModel.openPd = false;
                                thresholdModel.openPs = false;
                                thresholdModel.psHigh = 140;
                                thresholdModel.psLow = 90;
                                thresholdModel.pdHigh = 90;
                                thresholdModel.pdLow = 60;
                            }
                            error.set(false);
                        } else {
                            error.set(true);
                            showToast(netResponseEntity.getMessage());
                        }
                        Log.i(TAG, "error: " + error.get());
                        renderThresholdModel();
                    }
                });
    }

    public void setPsOn(boolean on) {
        final ThresholdRequestEntity entity = ThresholdModelWrapper.modelToNet(thresholdModel);
        entity.setAccountGuid(SaveUtils.getUserId(getApplication()));
        entity.setOpenPushPS(on ? 1 : 0);
        psOn.setValue(on);
        setThreshold(entity);
    }

    public void setPdOn(boolean on) {
        final ThresholdRequestEntity entity = ThresholdModelWrapper.modelToNet(thresholdModel);
        entity.setAccountGuid(SaveUtils.getUserId(getApplication()));
        entity.setOpenPushPD(on ? 1 : 0);
        pdOn.setValue(on);
        setThreshold(entity);
    }

//    public void setHrOn(boolean on) {
//        final ThresholdRequestEntity entity = ThresholdModelWrapper.modelToNet(thresholdModel);
//        entity.setAccountGuid(SaveUtils.getUserId(getApplication()));
//        entity.setOpenPushHR(on?1:0);
//        hrOn.set(on);
//        setThreshold(entity);
//    }

    private void setThreshold(ThresholdRequestEntity entity) {
        this.thresholdUseCase.setThreshold(entity, new Subscriber<NetResponseEntity<ThresholdRequestEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                showToast(getApplication().getString(R.string.network_error));
                renderThresholdModel();
            }

            @Override
            public void onNext(NetResponseEntity<ThresholdRequestEntity> netResponseEntity) {
                if (netResponseEntity.getCode() == 0) {
                    ThresholdRequestEntity thresholdRequestEntity = netResponseEntity.getData();
                    if (thresholdRequestEntity != null) {
                        thresholdModel = ThresholdModelWrapper.getModelFromNet(thresholdRequestEntity);
                        showToast(getApplication().getString(R.string.set_success));
                    }
                } else {
                    showToast(netResponseEntity.getMessage());
                }
                renderThresholdModel();

            }
        });
    }

    public void setModel(ThresholdModel model) {
        this.thresholdModel = model;
        if (thresholdModel != null) {
            renderThresholdModel();
        }
    }

    /**
     * 渲染数据
     */
    private void renderThresholdModel() {
//        hrOn.set(thresholdModel.openHr);
        psOn.setValue(thresholdModel.openPs);
        pdOn.setValue(thresholdModel.openPd);
        psRangeString.set(getRangeString(thresholdModel.psLow, thresholdModel.psHigh));
        pdRangeString.set(getRangeString(thresholdModel.pdLow, thresholdModel.pdHigh));
//        hrRangeString.set(getRangeString(thresholdModel.hrLow,thresholdModel.hrHigh));
    }

    /**
     * 渲染标题文字
     *
     * @param low  最小值
     * @param high 最大值
     * @return 文字
     */
    private String getRangeString(int low, int high) {
        if (low == 0 && high != 0) {
            return String.format(Locale.getDefault(), getApplication().getString(R.string.notification_remind_if_larger_than), high);
        } else if (low != 0 && high == 0) {
            return String.format(Locale.getDefault(), getApplication().getString(R.string.notification_remind_if_less_than), low);
        } else if (low != 0) {
            return String.format(Locale.getDefault(), getApplication().getString(R.string.notification_remind_if_larger_than_or_less_than_multiple_lines),
                    high, low);
        }
        return "";
    }

    @Override
    protected void onCleared() {
        thresholdUseCase.unSubscribe();
        super.onCleared();
    }

}
