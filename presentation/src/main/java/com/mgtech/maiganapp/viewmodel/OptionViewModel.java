package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import com.mgtech.domain.utils.SaveUtils;

public class OptionViewModel extends BaseViewModel {

    public OptionViewModel(Application application) {
        super(application);
    }

    public void setEmergencyHide(boolean hide){
        SaveUtils.setEmergencyHide(hide);
    }

    public boolean getEmergencyHide(){
        return SaveUtils.getEmergencyHide();
    }

}
