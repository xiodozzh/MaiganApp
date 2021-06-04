package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;

import com.mgtech.domain.entity.net.response.HealthKnowledgeResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.HealthManagementUseCase;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.HealthKnowledgeModel;
import com.mgtech.maiganapp.data.wrapper.HealthKnowledgeWrapper;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class KnowledgeListViewModel extends BaseViewModel {
    private HealthManagementUseCase useCase;
    public final ObservableBoolean loading = new ObservableBoolean(false);
    public final ObservableBoolean loadSuccess = new ObservableBoolean(false);
    public final ObservableBoolean empty = new ObservableBoolean(true);
    public List<HealthKnowledgeModel> data;

    public KnowledgeListViewModel(@NonNull Application application) {
        super(application);
        useCase = ((MyApplication)application).getHealthManagementUseCase();
        data = new ArrayList<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscriber();
    }

    public void getKnowledgeList(){
        loading.set(true);
        useCase.getKnowledgeList(new Subscriber<NetResponseEntity<List<HealthKnowledgeResponseEntity>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(getApplication().getString(R.string.network_error));
                loading.set(false);
            }

            @Override
            public void onNext(NetResponseEntity<List<HealthKnowledgeResponseEntity>> netResponseEntity) {
                if (netResponseEntity.getCode() == 0){
                    data.clear();
                    data.addAll(HealthKnowledgeWrapper.getModelListFromNet(netResponseEntity.getData()));
                    empty.set(data.isEmpty());
                }else{
                    showToast(netResponseEntity.getMessage());
                }
                loadSuccess.set(!loadSuccess.get());
                loading.set(false);
            }
        });
    }
}
