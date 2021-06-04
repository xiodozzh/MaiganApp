package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.util.Log;

import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.entity.net.request.GetEcgRequestEntity;
import com.mgtech.domain.entity.net.response.EcgResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.EcgUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.EcgModel;
import com.mgtech.maiganapp.data.wrapper.EcgModelWrapper;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import androidx.databinding.ObservableBoolean;
import rx.Subscriber;

/**
 *
 * @author zhaixiang
 * ECG细节
 */

public class EcgDataDetailViewModel extends BaseViewModel {
    private EcgUseCase useCase;
    public final ObservableBoolean loading = new ObservableBoolean(false);
    public final ObservableBoolean ecgDataLoadSuccess = new ObservableBoolean(false);
    public final ObservableBoolean ecgDataLoadFail = new ObservableBoolean(false);
    public final ObservableBoolean horizontal = new ObservableBoolean(false);
    public final ObservableBoolean share = new ObservableBoolean(false);
    public boolean reverse = false;
    public EcgModel model;
    public List<float[]> partEcgList = new ArrayList<>();
    public String name;
    public String age;
    public String sex;
    public String height;
    public String weight;

    public EcgDataDetailViewModel(Application application) {
        super(application);
        this.useCase = ((MyApplication)application).getEcgUseCase();
        UserInfo userInfo = UserInfo.getLocalUserInfo(application);
        name = userInfo.getName();
        age = String.valueOf(userInfo.getAge(application));
        if(userInfo.getSex()==MyConstant.MAN){
            sex = application.getString(R.string.man);
        }else if(userInfo.getSex() == MyConstant.WOMEN){
            sex = application.getString(R.string.woman);
        }else{
            sex = application.getString(R.string.sex_secret);
        }
        height = String.valueOf(Math.round(userInfo.getHeight()));
        weight = String.valueOf(Math.round(userInfo.getWeight()));
    }

    public void setModel(EcgModel model){
        Log.i(TAG, "setModel: "+ model);
        this.share.set(Objects.equals(model.userId,SaveUtils.getUserId(getApplication())));
        if (model.ecgData == null || model.ecgData.length == 0){
            getEcgData(model.measureGuid,model.userId);
        }else{
            this.model = model;
            clipData(model);
            ecgDataLoadSuccess.set(!ecgDataLoadSuccess.get());
        }
    }

    private void getEcgData(String measureId, final String userId){
        loading.set(true);
        GetEcgRequestEntity entity = new GetEcgRequestEntity(measureId,userId);
        this.useCase.getEcg(entity, NetConstant.NO_CACHE, new Subscriber<NetResponseEntity<EcgResponseEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                ecgDataLoadFail.set(!ecgDataLoadFail.get());
                loading.set(false);
            }

            @Override
            public void onNext(NetResponseEntity<EcgResponseEntity> netResponseEntity) {
                if (netResponseEntity.getCode() == 0){
                    EcgResponseEntity ecgResponseEntity = netResponseEntity.getData();
                    if (ecgResponseEntity != null){
                        model = EcgModelWrapper.getModelFromNet(ecgResponseEntity);
                        model.userId = userId;
                        clipData(model);
                    }else{
                        model = null;
                    }
                    ecgDataLoadSuccess.set(!ecgDataLoadSuccess.get());
                    Log.i(TAG, "ecgDataLoadSuccess: " + ecgDataLoadSuccess.get());
                }else{
                    showToast(netResponseEntity.getMessage());
                    ecgDataLoadFail.set(!ecgDataLoadFail.get());
                }
                loading.set(false);
            }
        });
    }

    private void clipData(EcgModel model){
        partEcgList.clear();
        float[] fullData = model.ecgData;
        int start = 0;
        int partLength;
        if (horizontal.get()){
            partLength =ViewUtils.calculateEcgDataLength(getApplication().getResources().getDimensionPixelSize(R.dimen.ecg_horizontal_width));
        }else {
            partLength = ViewUtils.calculateEcgDataLength(getApplication());
        }
        while (start < fullData.length){
            int size = Math.min(fullData.length - start,partLength);
            if (size <= 0){
                break;
            }
            float[]part = new float[size];
            System.arraycopy(fullData,start,part,0,size);
            if (start == 0) {
                part[0] = fullData[start];
            }else{
                part[0] = fullData[start-1];
            }
            partEcgList.add(part);
            start+=size;
        }
    }

    @Override
    protected void onCleared() {
        Log.i(TAG, "onCleared: ");
        super.onCleared();
        useCase.unSubscribe();
    }
}
