package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.mgtech.domain.entity.net.request.SetRelationNoteNameRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.RelationshipResponseEntity;
import com.mgtech.domain.interactor.RelationUseCase;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.FriendModel;
import com.mgtech.maiganapp.data.wrapper.FriendModelWrapper;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class FriendInformationViewModel extends BaseViewModel {
    public ObservableField<String> phone = new ObservableField<>("");
    public ObservableField<String> name = new ObservableField<>("");
    public ObservableField<String> noteName = new ObservableField<>("");
    public ObservableField<String> tvReadHisData = new ObservableField<>("");
    public ObservableField<String> tvGetHisPushData = new ObservableField<>("");
    public ObservableField<String> tvAllowHimRead = new ObservableField<>("");
    public ObservableField<String> tvAllowPushToHim = new ObservableField<>("");
    public ObservableBoolean haveNoteName = new ObservableBoolean(false);
    public ObservableBoolean change = new ObservableBoolean(false);
    public ObservableBoolean renderData = new ObservableBoolean(false);
    public String avatarUrl;
    private RelationUseCase useCase;

    public FriendModel model;

    private String readText;
//    private String notReadText;
    private String getPushText;
//    private String notGetPushText;
    private String allowHimReadText;
//    private String disallowHimReadText;
    private String allowPushToHimText;
//    private String disallowPushToHimText;

    public boolean readHisData;
    public boolean getHisPushData;
    public boolean allowHimRead;
    public boolean allowPushToHim;

    public FriendInformationViewModel(@NonNull Application application) {
        super(application);
        useCase = ((MyApplication) application).getRelationUseCase();

        readText = application.getString(R.string.friend_info_read_his_data);
//        notReadText = application.getString(R.string.friend_info_not_read_his_data);
        getPushText = application.getString(R.string.friend_info_receive_his_push);
//        notGetPushText = application.getString(R.string.friend_info_not_receive_his_push);
        allowHimReadText = application.getString(R.string.friend_info_allow_him_read);
//        disallowHimReadText = application.getString(R.string.friend_info_disallow_him_read);
        allowPushToHimText = application.getString(R.string.friend_info_allow_push_to_him);
//        disallowPushToHimText = application.getString(R.string.friend_info_disallow_push_to_him);

        this.tvAllowHimRead.set(allowHimReadText);
        this.tvAllowPushToHim.set(allowPushToHimText);
        this.tvGetHisPushData.set(getPushText);
        this.tvReadHisData.set(readText);
    }

    private void setInfo(FriendModel model) {
        this.name.set(model.getName());
        this.phone.set(model.getPhone());
        this.noteName.set(model.getNoteName());
        this.haveNoteName.set(!TextUtils.isEmpty(model.getNoteName()));
        this.avatarUrl = model.getAvatarUrl();
        this.getHisPushData = model.isGetHisPushData();
        this.readHisData = model.isReadHisData();
        this.allowHimRead = model.isAllowHimReadData();
        this.allowPushToHim = model.isAllowPushData();

        renderData.set(!renderData.get());
    }

    public void getInfo(String targetId) {
        useCase.getFriendInfo(SaveUtils.getUserId(getApplication()), targetId, FriendModel.MONITOR, NetConstant.NO_CACHE,
                new Subscriber<NetResponseEntity<RelationshipResponseEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showToast(getApplication().getString(R.string.network_error));
                    }

                    @Override
                    public void onNext(NetResponseEntity<RelationshipResponseEntity> netResponseEntity) {
                        if (netResponseEntity.getCode() == 0) {
                            RelationshipResponseEntity entity = netResponseEntity.getData();
                            if (entity != null) {
                                model = FriendModelWrapper.getModelFromNetEntity(entity);
                                setInfo(model);
                            } else {
                                showToast(getApplication().getString(R.string.network_error));
                            }
                        } else {
                            showToast(netResponseEntity.getMessage());
                        }
                    }
                });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
    }

    public void update() {
        SetRelationNoteNameRequestEntity entity = new SetRelationNoteNameRequestEntity(SaveUtils.getUserId(getApplication()),
                model.getTargetUserId(), model.getRelationType(), allowPushToHim ? 1 : 0, allowHimRead ? 1 : 0,
                getHisPushData? 1 : 0, readHisData ? 1 : 0, noteName.get());
        useCase.setAuthority(entity, new Subscriber<NetResponseEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                setInfo(model);
                showToast(getApplication().getString(R.string.network_error));
            }

            @Override
            public void onNext(NetResponseEntity netResponseEntity) {
                if (netResponseEntity.getCode() == 0) {
                    model.setNoteName(noteName.get());
                    model.setAllowHimReadData(allowHimRead);
                    model.setAllowPushData(allowPushToHim);
                    model.setGetHisPushData(getHisPushData);
                    model.setReadHisData(readHisData);
                    setInfo(model);
                    showToast(getApplication().getString(R.string.set_success));
                } else {
                    setInfo(model);
                    showToast(netResponseEntity.getMessage());
                }
                change.set(!change.get());
            }
        });
    }
}
