package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mgtech.domain.entity.net.response.GetAuthorizedCompanyResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.ServeUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.AuthorizedCompanyModel;
import com.mgtech.maiganapp.data.wrapper.AuthorizedCompanyModelWrapper;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author Jesse
 */
public class SettingAuthManagementViewModel extends BaseViewModel{
    public MutableLiveData<List<AuthorizedCompanyModel>> serviceModels = new MutableLiveData<>();
    public MutableLiveData<Boolean> networkError = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private ServeUseCase useCase;

    public SettingAuthManagementViewModel(@NonNull Application application) {
        super(application);
        useCase = ((MyApplication)application).getServeUseCase();
    }

    public void getAuthorizedService(){
        loading.setValue(true);
        useCase.getAuthorizedCompanies(SaveUtils.getUserId(), new Subscriber<NetResponseEntity<List<GetAuthorizedCompanyResponseEntity>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                networkError.setValue(true);
                loading.setValue(false);
            }

            @Override
            public void onNext(NetResponseEntity<List<GetAuthorizedCompanyResponseEntity>> listNetResponseEntity) {
                loading.setValue(false);
                if (listNetResponseEntity.getCode()==0){
                    List<GetAuthorizedCompanyResponseEntity> list = listNetResponseEntity.getData();
                    if (list != null) {
                        List<AuthorizedCompanyModel> models = AuthorizedCompanyModelWrapper.getListFromNet(list);
                        serviceModels.setValue(models);
                    }else{
                        networkError.setValue(true);
                    }
                }else{
                    showToast(listNetResponseEntity.getMessage());
                    networkError.setValue(true);
                }
            }
        });

    }

    public void deleteAuth(int index){
        final List<AuthorizedCompanyModel>list = serviceModels.getValue();
        if (list!=null && list.size() > index){
            AuthorizedCompanyModel model = list.get(index);
            useCase.cancelServiceAuthority(SaveUtils.getUserId(), model.id, new Subscriber<NetResponseEntity>() {
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
                    if (netResponseEntity.getCode() == 0){
                        list.remove(index);
                        serviceModels.setValue(list);
                    }else{
                        showToast(netResponseEntity.getMessage());
                    }
                }
            });
        }
    }
}
