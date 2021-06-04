package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;

import com.mgtech.domain.entity.net.request.DealRelationRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.RelationRequestResponseEntity;
import com.mgtech.domain.interactor.RelationUseCase;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.FriendRequestModel;
import com.mgtech.maiganapp.data.wrapper.FriendRequestWrapper;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class FriendDealRequestViewModel extends BaseViewModel {
    public List<FriendRequestModel> list = new ArrayList<>();
    public ObservableBoolean getFriendRequestSuccess = new ObservableBoolean(false);
    public ObservableBoolean acceptRequestSuccess = new ObservableBoolean(false);
    public ObservableBoolean denyRequestSuccess = new ObservableBoolean(false);
    public ObservableBoolean getFriendLoading = new ObservableBoolean(false);
    public ObservableBoolean dealing = new ObservableBoolean(false);
    public ObservableBoolean dataEmpty = new ObservableBoolean(true);
    private RelationUseCase useCase;
    public int dealIndex;

    public FriendDealRequestViewModel(@NonNull Application application) {
        super(application);
        this.useCase = ((MyApplication) application).getRelationUseCase();
    }

    public void getFriendRequest() {
        getFriendLoading.set(true);
        this.useCase.getRelationRequest(0, 100, new Subscriber<NetResponseEntity<List<RelationRequestResponseEntity>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(getApplication().getString(R.string.network_error));
                list.clear();
                getFriendLoading.set(false);
                dataEmpty.set(list.isEmpty());
            }

            @Override
            public void onNext(NetResponseEntity<List<RelationRequestResponseEntity>> listNetResponseEntity) {
                list.clear();
                if (listNetResponseEntity.getCode() == 0) {
                    List<RelationRequestResponseEntity> entities = listNetResponseEntity.getData();
                    if (entities != null) {
                        list.addAll(FriendRequestWrapper.getListFromNetEntityToFriendRequestAddModel(entities));
                        getFriendRequestSuccess.set(!getFriendRequestSuccess.get());
                    }
                } else {
                    showToast(listNetResponseEntity.getMessage());
                }
                dataEmpty.set(list.isEmpty());
                getFriendLoading.set(false);
            }
        });
    }

    public void dealRequest(int index, final boolean accept) {
        if (index >= list.size()) {
            return;
        }
        dealing.set(true);
        dealIndex = index;
        final FriendRequestModel model = list.get(index);
        DealRelationRequestEntity entity = new DealRelationRequestEntity(model.id,
                accept ? FriendRequestModel.RESULT_ACCEPT : FriendRequestModel.RESULT_DENY);
        this.useCase.setRelation(entity, new Subscriber<NetResponseEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(getApplication().getString(R.string.network_error));
                dealing.set(false);
            }

            @Override
            public void onNext(NetResponseEntity netResponseEntity) {
                if (netResponseEntity.getCode() == 0) {
                    if (accept) {
                        model.result = FriendRequestModel.RESULT_ACCEPT;
                        acceptRequestSuccess.set(!acceptRequestSuccess.get());
                    } else {
                        model.result = FriendRequestModel.RESULT_DENY;
                        showToast(getApplication().getString(R.string.friend_relation_request_denied));
                        denyRequestSuccess.set(!denyRequestSuccess.get());
                    }
                } else {
                    showToast(netResponseEntity.getMessage());
                }
                dealing.set(false);
            }
        });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
    }
}
