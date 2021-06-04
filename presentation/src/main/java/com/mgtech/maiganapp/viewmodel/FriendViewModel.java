package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;

import com.mgtech.domain.entity.net.request.GetRelationshipEntity;
import com.mgtech.domain.entity.net.request.RemoveRelationRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.RelationshipResponseEntity;
import com.mgtech.domain.interactor.RelationUseCase;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.FriendModel;
import com.mgtech.maiganapp.data.wrapper.FriendModelWrapper;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class FriendViewModel extends BaseFragmentViewModel {
    public MutableLiveData<List<FriendModel>> friendModelsLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();
    public ObservableBoolean netError = new ObservableBoolean(false);
    public ObservableBoolean empty = new ObservableBoolean(false);

    private RelationUseCase relationUseCase;

    public FriendViewModel(@NonNull Application application) {
        super(application);
        this.relationUseCase = ((MyApplication) application).getRelationUseCase();
    }

    public void getRelations() {
        GetRelationshipEntity entity = new GetRelationshipEntity(SaveUtils.getUserId(getApplication()),
                0, 500, GetRelationshipEntity.TYPE_MONITOR);
        loading.setValue(true);
        relationUseCase.getRelationship(entity, NetConstant.NO_CACHE, new Subscriber<NetResponseEntity<List<RelationshipResponseEntity>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                netError.set(true);
                loading.setValue(false);
                friendModelsLiveData.setValue(new ArrayList<>());
                showToast(getApplication().getString(R.string.network_error));
            }

            @Override
            public void onNext(NetResponseEntity<List<RelationshipResponseEntity>> listNetResponseEntity) {
                Log.i(TAG, "onNext: " + listNetResponseEntity);
                loading.setValue(false);
                if (listNetResponseEntity.getCode() == 0) {
                    List<RelationshipResponseEntity> list = listNetResponseEntity.getData();
                    if (list != null) {
                        List<FriendModel> models = FriendModelWrapper.getListFromNetEntityToFriendModel(list);
                        friendModelsLiveData.setValue(models);
                        empty.set(list.isEmpty());
                    }else{
                        friendModelsLiveData.setValue(new ArrayList<>());
                        empty.set(true);
                    }
                    netError.set(false);
                } else {
                    friendModelsLiveData.setValue(new ArrayList<>());
                    netError.set(true);
                    showToast(listNetResponseEntity.getMessage());
                }
        }
    });
}

    public void deleteRelation(final int index) {
        final List<FriendModel> list = friendModelsLiveData.getValue();
        if (list == null || list.size() <= index) {
            return;
        }
        FriendModel model = list.get(index);
        relationUseCase.removeRelation(new RemoveRelationRequestEntity(model.getRelationId(), SaveUtils.getUserId(getApplication())),
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
                            list.remove(index);
                            empty.set(list.isEmpty());
                            friendModelsLiveData.setValue(list);
                        } else {
                            showToast(netResponseEntity.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        relationUseCase.unSubscribe();
    }

    //    public void getAllData(){
//        loadingIFollow.set(true);
//        loadingFollowMe.set(true);
//        GetRelationshipEntity entity = new GetRelationshipEntity(SaveUtils.getUserId(getApplication()),
//                0, 500, GetRelationshipEntity.TYPE_BOTH);
//        relationUseCase.getRelationship(entity, NetConstant.NO_CACHE, new Subscriber<NetResponseEntity<List<RelationshipResponseEntity>>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                loadingIFollow.set(false);
//                loadingFollowMe.set(false);
//                errorIFollow.set(true);
//                errorFollowMe.set(true);
//                showToast(getApplication().getString(R.string.network_error));
//            }
//
//            @Override
//            public void onNext(NetResponseEntity<List<RelationshipResponseEntity>> listNetResponseEntity) {
//                Log.i(TAG, "onNext: " + listNetResponseEntity);
//                loadingIFollow.set(false);
//                loadingFollowMe.set(false);
//
//                if (listNetResponseEntity.getCode() == 0) {
//                    List<RelationshipResponseEntity> list = listNetResponseEntity.getData();
//                    iFollowList.clear();
//                    followMeList.clear();
//                    if (list != null) {
//                        List<FriendModel> models = FriendModelWrapper.getListFromNetEntityToFriendModel(list);
//                        for (FriendModel model :models) {
//                            if (model.getRelationType() == FriendModel.MONITOR){
//                                iFollowList.add(model);
//                            }else{
//                                followMeList.add(model);
//                            }
//                        }
//                    }
//                    emptyIFollow.set(iFollowList.isEmpty());
//                    emptyFollowMe.set(followMeList.isEmpty());
//                    reloadIFollowData.set(!reloadIFollowData.get());
//                    reloadFollowMeData.set(!reloadFollowMeData.get());
//                    errorFollowMe.set(false);
//                    errorIFollow.set(false);
//                } else {
//                    errorIFollow.set(true);
//                    errorFollowMe.set(true);
//                    showToast(listNetResponseEntity.getMessage());
//                }
//            }
//        });
//    }
//
//    public void getIFollowData() {
//        loadingIFollow.set(true);
//        GetRelationshipEntity entity = new GetRelationshipEntity(SaveUtils.getUserId(getApplication()),
//                0, 500, GetRelationshipEntity.TYPE_MONITOR);
//        relationUseCase.getRelationship(entity, NetConstant.NO_CACHE, new Subscriber<NetResponseEntity<List<RelationshipResponseEntity>>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                errorIFollow.set(true);
//                loadingIFollow.set(false);
//                showToast(getApplication().getString(R.string.network_error));
//            }
//
//            @Override
//            public void onNext(NetResponseEntity<List<RelationshipResponseEntity>> listNetResponseEntity) {
//                Log.i(TAG, "onNext: " + listNetResponseEntity);
//                if (listNetResponseEntity.getCode() == 0) {
//                    List<RelationshipResponseEntity> list = listNetResponseEntity.getData();
//                    iFollowList.clear();
//                    if (list != null) {
//                        iFollowList.addAll(FriendModelWrapper.getListFromNetEntityToFriendModel(list));
//                    }
//                    emptyIFollow.set(iFollowList.isEmpty());
//                    reloadIFollowData.set(!reloadIFollowData.get());
//                    errorIFollow.set(false);
//                } else {
//                    errorIFollow.set(true);
//                    showToast(listNetResponseEntity.getMessage());
//                }
//                loadingIFollow.set(false);
//            }
//        });
//    }
//
//    public void getFollowMeData() {
//        loadingFollowMe.set(true);
//        GetRelationshipEntity entity = new GetRelationshipEntity(SaveUtils.getUserId(getApplication()),
//                0, 500, GetRelationshipEntity.TYPE_MONITORED);
//        relationUseCase.getRelationship(entity, NetConstant.CACHE_THEN_NET, new Subscriber<NetResponseEntity<List<RelationshipResponseEntity>>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                loadingFollowMe.set(false);
//                errorFollowMe.set(true);
//                showToast(getApplication().getString(R.string.network_error));
//            }
//
//            @Override
//            public void onNext(NetResponseEntity<List<RelationshipResponseEntity>> listNetResponseEntity) {
//                Log.i(TAG, "onNext: " + listNetResponseEntity);
//                if (listNetResponseEntity.getCode() == 0) {
//                    List<RelationshipResponseEntity> list = listNetResponseEntity.getData();
//                    followMeList.clear();
//                    if (list != null) {
//                        followMeList.addAll(FriendModelWrapper.getListFromNetEntityToFriendModel(list));
//                    }
//                    emptyFollowMe.set(followMeList.isEmpty());
//                    reloadFollowMeData.set(!reloadFollowMeData.get());
//                    errorFollowMe.set(false);
//                }else{
//                    errorFollowMe.set(true);
//                    showToast(listNetResponseEntity.getMessage());
//                }
//                loadingFollowMe.set(false);
//            }
//        });
//    }

//    public void deleteIFollow(final int index) {
//        FriendModel model = iFollowList.get(index);
//        relationUseCase.removeRelation(new RemoveRelationRequestEntity(model.getRelationId(), SaveUtils.getUserId(getApplication())),
//                new Subscriber<NetResponseEntity>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        showToast(getApplication().getString(R.string.network_error));
//                    }
//
//                    @Override
//                    public void onNext(NetResponseEntity netResponseEntity) {
//                        if (netResponseEntity.getCode() == 0) {
//                            iFollowList.remove(index);
//                            reloadIFollowData.set(!reloadIFollowData.get());
//                        }
//                    }
//                });
//    }
//
//    public void deleteFollowMe(final int index) {
//        FriendModel model = followMeList.get(index);
//        relationUseCase.removeRelation(new RemoveRelationRequestEntity(model.getRelationId(), SaveUtils.getUserId(getApplication())),
//                new Subscriber<NetResponseEntity>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        showToast(getApplication().getString(R.string.network_error));
//                    }
//
//                    @Override
//                    public void onNext(NetResponseEntity netResponseEntity) {
//                        if (netResponseEntity.getCode() == 0) {
//                            followMeList.remove(index);
//                            reloadFollowMeData.set(!reloadFollowMeData.get());
//                        }
//                    }
//                });
//    }
}
