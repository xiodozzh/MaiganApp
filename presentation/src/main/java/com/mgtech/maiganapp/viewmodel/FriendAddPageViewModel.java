package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.entity.net.request.SendRelationRequestRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.RelationUseCase;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.FriendAddItemModel;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class FriendAddPageViewModel extends BaseViewModel {
    private FriendAddItemModel model;
    public ObservableField<String> name = new ObservableField<>("");
//    public ObservableField<String> message = new ObservableField<>("");
//    public MutableLiveData<String>message = new MutableLiveData<>();
    public ObservableBoolean sendSuccess = new ObservableBoolean(false);
    public ObservableBoolean valid = new ObservableBoolean(false);
    public ObservableBoolean sending = new ObservableBoolean(false);
    private RelationUseCase useCase;
    public String message = "";

    public FriendAddPageViewModel(@NonNull Application application) {
        super(application);
        this.useCase = ((MyApplication)application).getRelationUseCase();
    }

    public void setModel(FriendAddItemModel model){
        this.model = model;
        name.set(model.name);
        message = String.format(getApplication().getString(R.string.activity_friend_add_page_i_am),
                UserInfo.getLocalUserInfo(getApplication()).getName());
    }


    public void sendRequest(){
        sending.set(true);
        useCase.sendRelationRequest(new SendRelationRequestRequestEntity(model.id, message),
                new Subscriber<NetResponseEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(getApplication().getString(R.string.network_error));
                sending.set(false);
            }

            @Override
            public void onNext(NetResponseEntity netResponseEntity) {
                if (netResponseEntity.getCode() == 0){
                    showToast(getApplication().getString(R.string.send_success));
                    sendSuccess.set(!sendSuccess.get());
                }else{
                    showToast(netResponseEntity.getMessage());
                }
                sending.set(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
    }
}
