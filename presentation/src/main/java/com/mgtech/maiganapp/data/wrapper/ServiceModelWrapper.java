package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.BoughtServiceResponseEntity;
import com.mgtech.maiganapp.data.model.CompanyModel;
import com.mgtech.maiganapp.data.model.ServiceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesse
 */
public class ServiceModelWrapper {
    public static List<ServiceModel> getListFromNet(List<BoughtServiceResponseEntity> entities){
        List<ServiceModel> list = new ArrayList<>();
        if (entities != null) {
            for (BoughtServiceResponseEntity entity : entities) {
                ServiceModel serviceModel = new ServiceModel();
                serviceModel.name = entity.getServiceName();
                serviceModel.id = entity.getServiceGuid();
                serviceModel.serviceCode = entity.getServiceCode();
                serviceModel.iconUrl = entity.getIconUrl();
                serviceModel.pageUrl = entity.getServicePageUrl();
                serviceModel.title = entity.getPromotionTitle();
                serviceModel.imageUrl = entity.getPromotionImageUrl();
                serviceModel.subTitle = entity.getPromotionSubtitle();
                serviceModel.companies = getCompanyModelFromNet(serviceModel, entity.getServiceCompanies());
                list.add(serviceModel);
            }
        }
        return list;
    }

    private static List<CompanyModel> getCompanyModelFromNet(ServiceModel serviceModel, List<BoughtServiceResponseEntity.ServiceCompaniesBean> entities){
        List<CompanyModel> list = new ArrayList<>();
        if (entities == null){
            return list;
        }
        for (BoughtServiceResponseEntity.ServiceCompaniesBean entity : entities) {
            CompanyModel model = new CompanyModel();
            model.id = entity.getCompanyGuid();
            model.name = entity.getCompanyName();
            model.title = entity.getPromotionTitle();
            model.subTitle = entity.getPromotionSubtitle();
            model.pageUrl = entity.getPageUrl();
            model.imageUrl = entity.getPromotionImageUrl();
            model.serviceModels = getServiceModelFromNet(entity.getCompanyService());
            model.associatedServiceModel = serviceModel;
            list.add(model);
        }
        return list;
    }

    private static List<ServiceModel> getServiceModelFromNet(List<BoughtServiceResponseEntity.CompanyServiceBean> entities){
        List<ServiceModel> list = new ArrayList<>();
        if (entities == null){
            return list;
        }
        for (BoughtServiceResponseEntity.CompanyServiceBean entity : entities) {
            ServiceModel serviceModel = new ServiceModel();
            serviceModel.name = entity.getServiceName();
            serviceModel.id = entity.getServiceGuid();
            serviceModel.serviceCode = entity.getServiceCode();
            serviceModel.iconUrl = entity.getIconUrl();
            serviceModel.pageUrl = entity.getServicePageUrl();
            serviceModel.title = entity.getPromotionTitle();
            serviceModel.imageUrl = entity.getPromotionImageUrl();
            serviceModel.subTitle = entity.getPromotionSubtitle();
            list.add(serviceModel);
        }
        return list;
    }
}
