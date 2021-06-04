package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.ServiceCompanyResponseEntity;
import com.mgtech.maiganapp.data.model.CompanyModel;

import java.util.ArrayList;
import java.util.List;

public class CompanyWrapper {
    public static List<CompanyModel> getListFromNet(List<ServiceCompanyResponseEntity> entities){
        List<CompanyModel> list = new ArrayList<>();
        if (entities != null) {
            for (ServiceCompanyResponseEntity entity : entities) {
                list.add(getModelFromNet(entity));
            }
        }
        return list;
    }

    private static CompanyModel getModelFromNet(ServiceCompanyResponseEntity entity){
        CompanyModel model = new CompanyModel();
        if (entity == null){
            return model;
        }
        model.id = entity.getCompanyGuid();
        model.imageUrl = entity.getPromotionImageUrl();
        model.pageUrl = entity.getPageUrl();
        model.title = entity.getPromotionTitle();
        model.subTitle = entity.getPromotionSubtitle();
        model.name = entity.getCompanyName();
        return model;
    }
}
