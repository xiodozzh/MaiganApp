package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.mgtech.domain.entity.net.response.FriendDataResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.RelationUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.FriendDataModel;
import com.mgtech.maiganapp.data.model.FriendModel;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.data.wrapper.FriendDataModelWrapper;

import java.util.Calendar;
import java.util.Locale;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class FriendRemindViewModel extends BaseViewModel {
    public ObservableBoolean loading = new ObservableBoolean(false);
    private RelationUseCase useCase;
    private FriendModel friendModel;
    public final ObservableField<String> lastMeasurePwTime = new ObservableField<>("");
    public final ObservableField<String> daysFromLastMeasureToToday = new ObservableField<>("");
    public final ObservableField<String> lastMeasurePwDate = new ObservableField<>("");
    public final ObservableField<String> lastMeasureEcgDate = new ObservableField<>("");
    public final ObservableField<String> lastExceptionDate = new ObservableField<>("");
    public final ObservableField<String> exceptionHrValue = new ObservableField<>("");
    public final ObservableField<String> exceptionBpValue = new ObservableField<>("");
    public final ObservableBoolean showRemindCard = new ObservableBoolean(false);
    public final ObservableBoolean noAuthor = new ObservableBoolean(false);
    public final ObservableBoolean showExceptionCard = new ObservableBoolean(false);
    public final ObservableBoolean showPwCard = new ObservableBoolean(false);
    public final ObservableBoolean showEcgCard = new ObservableBoolean(false);
    public final ObservableBoolean pwHaveData = new ObservableBoolean(false);
    public final ObservableBoolean ecgHaveData = new ObservableBoolean(false);
    public final ObservableBoolean abnormalHaveData = new ObservableBoolean(false);
    public final ObservableBoolean loadPwDataSuccess = new ObservableBoolean(false);
    public final ObservableBoolean loadEcgDataSuccess = new ObservableBoolean(false);
    public final ObservableBoolean networkError = new ObservableBoolean(false);
    public final ObservableBoolean friendNeverMeasure = new ObservableBoolean(false);
    public final ObservableBoolean havePwUnread = new ObservableBoolean(false);
    public final ObservableBoolean haveExceptionUnread = new ObservableBoolean(false);
    public final ObservableField<String> name = new ObservableField<>("");

    public FriendDataModel friendDataModel;
    public String id;
    public boolean isMan;
    private String lastMeasureTimeText = "";
    private String MeasureTimeText = "";


    public FriendRemindViewModel(@NonNull Application application) {
        super(application);
        useCase = ((MyApplication) application).getRelationUseCase();
//        lastMeasureTimeText = application.getString(R.string.activity_friend_remind_last_measure_time);
//        MeasureTimeText = application.getString(R.string.activity_friend_remind_measure_time);
    }

    public void setFriendModel(FriendModel friendModel) {
        Log.i(TAG, "setFriendModel: " + friendModel);
        this.friendModel = friendModel;
        this.id = friendModel.getTargetUserId();
        String name = friendModel.getNoteName();
        if (TextUtils.isEmpty(name)) {
            name = friendModel.getName();
        }
        this.isMan = friendModel.isMan();
        this.name.set(name);
    }

    public void getData() {
        loading.set(true);
        useCase.getFriendData(friendModel.getTargetUserId(), NetConstant.NO_CACHE,
                new Subscriber<NetResponseEntity<FriendDataResponseEntity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showToast(getApplication().getString(R.string.network_error));
                        networkError.set(true);
                        loading.set(false);
                    }

                    @Override
                    public void onNext(NetResponseEntity<FriendDataResponseEntity> netResponseEntity) {
                        if (netResponseEntity.getCode() == 0) {
                            friendDataModel = FriendDataModelWrapper.getModelFromNet(netResponseEntity.getData());
                            renderData();
                            networkError.set(false);
                        } else {
                            friendDataModel = null;
                            networkError.set(true);
                            showToast(netResponseEntity.getMessage());
                        }
                        loading.set(false);
                    }
                });
    }

    public void remindFriend() {
        useCase.reminderFriendMeasure(SaveUtils.getUserId(getApplication()), id,
                new Subscriber<NetResponseEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showToast(getApplication().getString(R.string.network_error));
                    }

                    @Override
                    public void onNext(NetResponseEntity netResponseEntity) {
                        if (netResponseEntity.getCode() == 0) {
                            showToast(getApplication().getString(R.string.activity_friend_remind_reminder_have_send));
                        } else {
                            showToast(netResponseEntity.getMessage());
                        }
                    }
                });
    }

    private void renderData() {
        if (friendDataModel == null) {
            networkError.set(true);
        } else {
            // 权限
            showEcgCard.set(friendDataModel.haveDataPermission);
            showPwCard.set(friendDataModel.haveDataPermission);
            showExceptionCard.set(friendDataModel.haveNotificationPermission);
            // 测量提醒
            if (friendDataModel.lastMeasureTime == 0) {
                lastMeasurePwTime.set("");
                showRemindCard.set(true);
                friendNeverMeasure.set(true);
                daysFromLastMeasureToToday.set("");
            } else {
                Calendar now = Calendar.getInstance();
                now.set(Calendar.HOUR_OF_DAY, 0);
                now.set(Calendar.MINUTE, 0);
                now.set(Calendar.SECOND, 0);
                now.set(Calendar.MILLISECOND, 0);
                if (now.getTimeInMillis() < friendDataModel.lastMeasureTime) {
                    showRemindCard.set(false);
                } else {
                    showRemindCard.set(true);
                    int betweenDays = (int) ((now.getTimeInMillis() - friendDataModel.lastMeasureTime) / (24 * 3600_000));
                    lastMeasurePwTime.set(lastMeasureTimeText
                            + DateFormat.format(MyConstant.FULL_DATETIME_FORMAT, friendDataModel.lastMeasureTime));
//                    daysFromLastMeasureToToday.set(String.format(Locale.getDefault(),
//                            getApplication().getString(R.string.activity_friend_remind_not_measure_for_n_days),betweenDays+1));
                    daysFromLastMeasureToToday.set(getApplication().getString(R.string.activity_friend_remind_your_friend_not_measure_yet));
                    friendNeverMeasure.set(false);
                }
            }
            // 异常记录
            if (friendDataModel.lastException != null) {
                IndicatorDataModel exceptionIndicator = friendDataModel.lastException.indicator;
                if (exceptionIndicator != null) {
                    exceptionHrValue.set(String.valueOf(Math.round(exceptionIndicator.hr)));
                    exceptionBpValue.set(Math.round(exceptionIndicator.ps) + "/" + Math.round(exceptionIndicator.pd));
                    lastExceptionDate.set(MeasureTimeText + DateFormat.format(MyConstant.FULL_DATETIME_FORMAT, exceptionIndicator.time));
                    abnormalHaveData.set(true);
                } else {
                    abnormalHaveData.set(false);
                    exceptionHrValue.set("-");
                    exceptionBpValue.set("-");
                    lastExceptionDate.set("");
                }
            } else {
                abnormalHaveData.set(false);
            }
            // pw
            if (friendDataModel.pdList != null && friendDataModel.psList != null && !friendDataModel.psList.isEmpty() && !friendDataModel.pdList.isEmpty()) {
                lastMeasurePwDate.set(MeasureTimeText + DateFormat.format(MyConstant.DATE_FORMAT, friendDataModel.pwTime));
                loadPwDataSuccess.set(!loadPwDataSuccess.get());
                pwHaveData.set(true);
            } else {
                pwHaveData.set(false);
            }
            // ecg
            if (friendDataModel.ecgData != null && friendDataModel.ecgData.length != 0) {
                lastMeasureEcgDate.set(MeasureTimeText + DateFormat.format(MyConstant.FULL_DATETIME_FORMAT, friendDataModel.ecgTime));
                loadEcgDataSuccess.set(!loadEcgDataSuccess.get());
                ecgHaveData.set(true);
            } else {
                ecgHaveData.set(false);
            }

            this.haveExceptionUnread.set(friendDataModel.abnormalUnreadNumber > 0);
            this.havePwUnread.set(friendDataModel.pwUnreadNumber > 0);
            this.noAuthor.set(!showRemindCard.get() && !friendDataModel.haveDataPermission && !friendDataModel.haveNotificationPermission);
        }
    }


    @Override
    protected void onCleared() {
        useCase.unSubscribe();
        super.onCleared();
    }
}
