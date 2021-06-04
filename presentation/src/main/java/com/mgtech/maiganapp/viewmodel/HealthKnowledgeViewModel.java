package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.mgtech.domain.interactor.UnreadMessageUseCase;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.service.NetJobService;

public class HealthKnowledgeViewModel extends BaseViewModel {
    private UnreadMessageUseCase useCase;

    public HealthKnowledgeViewModel(@NonNull Application application) {
        super(application);
        useCase = ((MyApplication)application).getUnreadMessageUseCase();
    }

    public void markRead(String knowledgeId){
        NetJobService.markKnowledgeRead(getApplication(),knowledgeId);
    }
}
