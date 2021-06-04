package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.response.AllDiseaseEntity;
import com.mgtech.maiganapp.data.model.DiseaseModel;

import java.util.ArrayList;
import java.util.List;

public class DiseaseModelWrapper {
    public static List<DiseaseModel> getListFromDiseaseBeans(List<AllDiseaseEntity.DiseaseBean> beans){
        List<DiseaseModel> list = new ArrayList<>();
        if (beans != null){
            for (AllDiseaseEntity.DiseaseBean bean : beans) {
                list.add(getModelFromDiseaseBean(bean));
            }
        }
        return list;
    }

    public static List<DiseaseModel> getListFromPersonalInfoDiseaseBeans(List<PersonalInfoEntity.DiseaseBean> beans){
        List<DiseaseModel> list = new ArrayList<>();
        if (beans != null){
            for (PersonalInfoEntity.DiseaseBean bean : beans) {
                list.add(getModelFromDiseaseBean(bean));
            }
        }
        return list;
    }


    public static List<PersonalInfoEntity.DiseaseBean> transferListToBeans(List<DiseaseModel> list){
        List<PersonalInfoEntity.DiseaseBean> beans = new ArrayList<>();
        if (list != null){
            for (DiseaseModel model : list) {
                PersonalInfoEntity.DiseaseBean bean = new PersonalInfoEntity.DiseaseBean();
                bean.setGuid(model.id);
                bean.setName(model.name);
                beans.add(bean);
            }
        }
        return beans;
    }


    private static DiseaseModel getModelFromDiseaseBean(AllDiseaseEntity.DiseaseBean bean){
        DiseaseModel model = new DiseaseModel();
        if (bean != null) {
            model.id = bean.getId();
            model.name = bean.getName();
        }
        return model;
    }

    private static DiseaseModel getModelFromDiseaseBean(PersonalInfoEntity.DiseaseBean bean){
        DiseaseModel model = new DiseaseModel();
        if (bean != null) {
            model.id = bean.getGuid();
            model.name = bean.getName();
        }
        return model;
    }
}
