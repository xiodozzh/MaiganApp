package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

import com.mgtech.domain.utils.SaveUtils;

/**
 * @author zhaixiang
 */
public class PhoneUnBindViewModel extends BaseViewModel {
    public ObservableField<String> phoneNumber = new ObservableField<>();

    public PhoneUnBindViewModel(@NonNull Application application) {
        super(application);
    }

    public void getPhoneNumber(){
        phoneNumber.set(SaveUtils.getPhone(getApplication()));
    }
}
