package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;
import android.text.format.DateFormat;

import com.mgtech.domain.entity.net.request.SetMedicationRemindRequestEntity;
import com.mgtech.domain.entity.net.response.MedicationRemindResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.MedicineUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.MedicationReminderModel;
import com.mgtech.maiganapp.data.wrapper.MedicationReminderModelWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class MedicationReminderViewModel extends BaseViewModel {
    public List<MedicationReminderModel> data = new ArrayList<>();
    public ObservableBoolean getDataSuccess = new ObservableBoolean(false);
    public ObservableBoolean setSuccess = new ObservableBoolean(false);
    public ObservableBoolean isEmpty = new ObservableBoolean(true);
    public ObservableBoolean isNetError = new ObservableBoolean(false);
    public ObservableBoolean loading = new ObservableBoolean(false);
    public ObservableField<String> dateString = new ObservableField<>("");
    public ObservableField<String> timeString = new ObservableField<>("");
    public ObservableField<String> weekString = new ObservableField<>("");
    private MedicineUseCase useCase;
    public int changedIndex;


    public MedicationReminderViewModel(@NonNull Application application) {
        super(application);
        this.useCase = ((MyApplication)application).getMedicineUseCase();
        Calendar calendar = Calendar.getInstance();
        dateString.set(DateFormat.format(application.getString(R.string.sport_date_format_month_day),calendar).toString());
        timeString.set(DateFormat.format(MyConstant.DISPLAY_TIME,calendar).toString());
        weekString.set(DateFormat.format("EEE",calendar).toString());
    }

    public void getReminderList(){
        loading.set(true);
        this.useCase.getMedicationRemindList(SaveUtils.getUserId(getApplication()),NetConstant.CACHE_IF_NET_ERROR,
                new Subscriber<NetResponseEntity<MedicationRemindResponseEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                loading.set(false);
                isEmpty.set(true);
                isNetError.set(true);
                showToast(getApplication().getString(R.string.network_error));
            }

            @Override
            public void onNext(NetResponseEntity<MedicationRemindResponseEntity> netResponseEntity) {
                if (netResponseEntity.getCode() == 0){
                    MedicationRemindResponseEntity entity = netResponseEntity.getData();
                    data.clear();
                    if (entity != null){
                        data.addAll(MedicationReminderModelWrapper.getModelFromNet(entity));
                    }
                }else{
                    showToast(netResponseEntity.getMessage());
                }
                isNetError.set(false);
                isEmpty.set(data.isEmpty());
                getDataSuccess.set(!getDataSuccess.get());
                loading.set(false);
            }
        });
    }


    public void setReminder(final int index, final int taken) {
        MedicationReminderModel model = data.get(index);
        SetMedicationRemindRequestEntity entity = new SetMedicationRemindRequestEntity(SaveUtils.getUserId(getApplication()),
                model.planId,model.remindId,taken);
        useCase.setMedicationRemind(entity, new Subscriber<NetResponseEntity>() {
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
                if (netResponseEntity.getCode()==0){
                    data.get(index).state = taken;
                    changedIndex = index;
                    setSuccess.set(!setSuccess.get());
                }else{
                    showToast(netResponseEntity.getMessage());
                }
            }
        });
    }

    @Override
    protected void onCleared() {
        useCase.unSubscribe();
        super.onCleared();
    }
}
