package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.DataUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;

import rx.Subscriber;

/**
 * @author Jesse
 */
public class MeasurePwResultRemarkViewModel extends BaseViewModel {
    public static final int MAX_NUMBER_CH = 60;
//    private static final int MAX_NUMBER_EN = 60;
    public MutableLiveData<String> remark = new MutableLiveData<>();
    private DataUseCase dataUseCase;

    public MeasurePwResultRemarkViewModel(@NonNull Application application) {
        super(application);
        dataUseCase = ((MyApplication)application).getDataUseCase();
    }

    public int getMaxNumber(){
//        return Utils.isLanguageChinese()? MAX_NUMBER_CH : MAX_NUMBER_CH;
        return MAX_NUMBER_CH;
    }

    public void setRemark(String id, String remarkString) {
        dataUseCase.setPwRemark(SaveUtils.getUserId(), id, remarkString, new Subscriber<NetResponseEntity>() {
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
                    remark.setValue(remarkString);
                    showToast(getApplication().getString(R.string.set_success));
                } else {
                    showToast(netResponseEntity.getMessage());
                }
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        dataUseCase.unSubscribe();
    }
}
