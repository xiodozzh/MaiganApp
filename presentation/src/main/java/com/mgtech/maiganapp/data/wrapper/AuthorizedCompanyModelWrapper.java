package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.GetAuthorizedCompanyResponseEntity;
import com.mgtech.maiganapp.data.model.AuthorizedCompanyModel;

import java.util.ArrayList;
import java.util.List;

public class AuthorizedCompanyModelWrapper {
    public static List<AuthorizedCompanyModel> getListFromNet(List<GetAuthorizedCompanyResponseEntity> entities){
        List<AuthorizedCompanyModel>list = new ArrayList<>();
        if (entities != null){
            for (GetAuthorizedCompanyResponseEntity entity : entities) {
                list.add(getModelFromNet(entity));
            }
        }
        return list;
    }

    public static AuthorizedCompanyModel getModelFromNet(GetAuthorizedCompanyResponseEntity entity){
        AuthorizedCompanyModel model = new AuthorizedCompanyModel();
        model.id = entity.getCompanyGuid();
        model.name = entity.getCompanyName();
        model.imgUrl = entity.getPromotionImageUrl();
        model.title = entity.getPromotionTitle();
        model.subtitle = entity.getPromotionSubtitle();
        model.pageUrl = entity.getPageUrl();
        return model;
    }
}
