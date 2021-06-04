package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.SearchPermissionsResponseEntity;
import com.mgtech.maiganapp.data.model.SearchPermissionsModel;

public class SearchPermissionsWrapper {
    public static SearchPermissionsModel getModelFromNet(SearchPermissionsResponseEntity entity){
        if (entity == null){
            return null;
        }
        SearchPermissionsModel model = new SearchPermissionsModel();
        model.forbidFindByPhone = entity.getForbidFindByPhone() == 1;
        return model;
    }
}
