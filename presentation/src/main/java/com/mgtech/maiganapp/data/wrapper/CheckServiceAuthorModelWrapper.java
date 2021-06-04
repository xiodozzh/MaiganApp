package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.CheckServiceAuthorityResponseEntity;
import com.mgtech.maiganapp.data.model.CheckServiceAuthorModel;

public class CheckServiceAuthorModelWrapper {
    public static CheckServiceAuthorModel getModelFromNet(CheckServiceAuthorityResponseEntity entity){
        CheckServiceAuthorModel model = new CheckServiceAuthorModel();
        model.authCode = entity.getAuthCode();
        model.haveAuthor = entity.getAuthStatus() == 1;
        return model;
    }
}
