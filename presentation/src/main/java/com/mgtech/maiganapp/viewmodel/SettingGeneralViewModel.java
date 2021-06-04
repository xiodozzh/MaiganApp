package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.mgtech.domain.utils.SaveUtils;

/**
 * @author Jesse
 */
public class SettingGeneralViewModel extends BaseViewModel {
    public SettingGeneralViewModel(@NonNull Application application) {
        super(application);
    }

    public void setEmergencyHide(boolean hide){
        SaveUtils.setEmergencyHide(hide);
    }

    public boolean getEmergencyHide(){
        return SaveUtils.getEmergencyHide();
    }
}
