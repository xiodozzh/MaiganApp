package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.SearchPermissionsResponseEntity;
import com.mgtech.domain.interactor.RelationUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.SearchPermissionsModel;
import com.mgtech.maiganapp.data.wrapper.SearchPermissionsWrapper;

import rx.Subscriber;

/**
 * @author Jesse
 */
public class PrivacyFriendViewModel extends BaseViewModel {
    private RelationUseCase relationUseCase;
    public MutableLiveData<Boolean> error = new MutableLiveData<>();
    public MutableLiveData<SearchPermissionsModel> model = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public boolean currentForbidFindByPhone;

    public PrivacyFriendViewModel(@NonNull Application application) {
        super(application);
        relationUseCase = ((MyApplication)application).getRelationUseCase();
    }

    public void getPermission(){
        loading.setValue(true);
        relationUseCase.getSearchPermission(SaveUtils.getUserId(), new Subscriber<NetResponseEntity<SearchPermissionsResponseEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                error.setValue(true);
                loading.setValue(false);

            }

            @Override
            public void onNext(NetResponseEntity<SearchPermissionsResponseEntity> netResponseEntity) {
                loading.setValue(false);
                if (netResponseEntity.getCode() == 0){
                    error.setValue(false);
                    SearchPermissionsResponseEntity entity = netResponseEntity.getData();
                    if (entity != null){
                        SearchPermissionsModel searchPermissionsModel = SearchPermissionsWrapper.getModelFromNet(entity);
                        currentForbidFindByPhone = searchPermissionsModel.forbidFindByPhone;
                        model.setValue(searchPermissionsModel);
                    }else{
                        error.setValue(true);
                    }
                }else{
                    showToast(netResponseEntity.getMessage());
                    error.setValue(true);
                }
            }
        });
    }

    public void setPermission(boolean forbidFindByPhone){
        relationUseCase.setSearchPermission(SaveUtils.getUserId(), forbidFindByPhone, new Subscriber<NetResponseEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(getApplication().getString(R.string.network_error));
                model.setValue(new SearchPermissionsModel(currentForbidFindByPhone));
            }

            @Override
            public void onNext(NetResponseEntity netResponseEntity) {
                if (netResponseEntity.getCode() == 0){
                    currentForbidFindByPhone = forbidFindByPhone;
                }else{
                    model.setValue(new SearchPermissionsModel(currentForbidFindByPhone));
                    showToast(netResponseEntity.getMessage());
                }
            }
        });
    }


}
