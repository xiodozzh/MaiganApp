package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.ThresholdRequestEntity;
import com.mgtech.domain.interactor.ExceptionUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.activity.SetAbnormalRangeActivity;
import com.mgtech.maiganapp.data.model.ThresholdModel;
import com.mgtech.maiganapp.data.wrapper.ThresholdModelWrapper;

import java.util.Locale;

import rx.Subscriber;

/**
 * Created by zhaixiang on 2017/8/29.
 * 设置推送
 */

public class SetAbnormalRangeViewModel extends BaseViewModel {

    public final ObservableField<String> title = new ObservableField<>("");
    public final ObservableField<String> upperLimit = new ObservableField<>("");
    public final ObservableField<String> lowerLimit = new ObservableField<>("");
    public final ObservableField<String> desc = new ObservableField<>("");
    public final ObservableBoolean saveSuccess = new ObservableBoolean(false);
    private int[] currentRange;
    private ExceptionUseCase useCase;
    private int type;
    public ThresholdModel thresholdModel;

    public SetAbnormalRangeViewModel(Application application) {
        super(application);
        this.useCase = ((MyApplication) application).getExceptionUseCase();
    }

    public void setRange(int[] currentRange) {
        if (currentRange.length != 2) {
            upperLimit.set("");
            lowerLimit.set("");
            desc.set("");
            return;
        }
        this.currentRange = currentRange;
        if (currentRange[1] == 0) {
            upperLimit.set(getApplication().getString(R.string.notification_no_reminder));
        } else {
            upperLimit.set(String.format(Locale.getDefault(), getApplication().getString(R.string.notification_remind_if_larger_than), currentRange[1]));
        }
        if (currentRange[0] == 0) {
            lowerLimit.set(getApplication().getString(R.string.notification_no_reminder));
        } else {
            lowerLimit.set(String.format(Locale.getDefault(), getApplication().getString(R.string.notification_remind_if_less_than), currentRange[0]));
        }
        if (currentRange[0] == 0 && currentRange[1] == 0) {
            desc.set(getApplication().getString(R.string.notification_no_reminder));
        } else if (currentRange[0] == 0) {
            desc.set(String.format(Locale.getDefault(), getApplication().getString(R.string.notification_remind_if_larger_than), currentRange[1]));
        } else if (currentRange[1] == 0) {
            desc.set(String.format(Locale.getDefault(), getApplication().getString(R.string.notification_remind_if_less_than), currentRange[0]));
        } else {
            desc.set(String.format(Locale.getDefault(), getApplication().getString(R.string.notification_remind_if_larger_than_or_less_than), currentRange[1], currentRange[0]));
        }
    }

    public int[] getCurrentRange() {
        return currentRange;
    }


    public void save() {
        ThresholdRequestEntity entity = ThresholdModelWrapper.modelToNet(thresholdModel);
        entity.setAccountGuid(SaveUtils.getUserId(getApplication()));
        String range = currentRange[0] + "," + currentRange[1];
        switch (type) {
//            case SetAbnormalRangeActivity.HR:
//                entity.setHrThreshold(range);
//                break;
            case SetAbnormalRangeActivity.PS:
                entity.setPsThreshold(range);
                break;
            case SetAbnormalRangeActivity.PD:
                entity.setPdThreshold(range);
                break;
            case SetAbnormalRangeActivity.V0:
                entity.setV0Threshold(range);
                break;
            default:
        }
        this.useCase.setThreshold(entity, new Subscriber<NetResponseEntity<ThresholdRequestEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(getApplication().getString(R.string.network_error));
            }

            @Override
            public void onNext(NetResponseEntity<ThresholdRequestEntity> netResponseEntity) {
                cancelDialog();
                if (netResponseEntity.getCode() == 0) {
                    ThresholdRequestEntity thresholdRequestEntity = netResponseEntity.getData();
                    if (thresholdRequestEntity != null) {
                        thresholdModel = ThresholdModelWrapper.getModelFromNet(thresholdRequestEntity);
                    }
                    saveSuccess.set(!saveSuccess.get());
                } else {
                    showToast(netResponseEntity.getMessage());
                }
            }
        });
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void onCleared() {
        useCase.unSubscribe();
        super.onCleared();
    }
}
