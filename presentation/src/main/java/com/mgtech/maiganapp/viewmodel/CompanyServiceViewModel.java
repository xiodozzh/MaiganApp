package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.mgtech.domain.interactor.UnreadMessageUseCase;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.service.NetJobService;

public class CompanyServiceViewModel extends BaseViewModel {

    public CompanyServiceViewModel(@NonNull Application application) {
        super(application);
    }

}
