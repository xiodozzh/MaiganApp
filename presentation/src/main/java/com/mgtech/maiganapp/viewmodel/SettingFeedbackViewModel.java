package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

/**
 * @author zhaixiang
 */
public class SettingFeedbackViewModel extends BaseViewModel {
    public ObservableField<String> textSize = new ObservableField<>("");

    public SettingFeedbackViewModel(@NonNull Application application) {
        super(application);
    }


    public void uploadFile(){

    }
}
