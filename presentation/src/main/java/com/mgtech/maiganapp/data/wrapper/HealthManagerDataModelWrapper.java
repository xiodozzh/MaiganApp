package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.GetHealthManagementResponseEntity;
import com.mgtech.maiganapp.data.model.HealthManagerDataModel;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class HealthManagerDataModelWrapper {
    public static HealthManagerDataModel getModelFromNet(GetHealthManagementResponseEntity entity){
        HealthManagerDataModel model = new HealthManagerDataModel();
        model.psGoal = entity.getPsGoal();
        model.pdGoal = entity.getPdGoal();
        model.normalDays = entity.getNormalDayCount();
        model.abnormalDays = entity.getAbnormalDayCount();
        model.suggest = entity.getSuggest();
        GetHealthManagementResponseEntity.PwDataBean pwDataBean = entity.getPwData();
        if (pwDataBean != null) {
            List<GetHealthManagementResponseEntity.DataBean> psDataBeans = pwDataBean.getPs();
            List<GetHealthManagementResponseEntity.DataBean> pdDataBeans = pwDataBean.getPd();

            if (psDataBeans != null && pdDataBeans != null && psDataBeans.size() == pdDataBeans.size()){
                model.list = getDataModelListFormNet(psDataBeans,pdDataBeans);
            }
        }
        return model;
    }

    private static List<IndicatorDataModel> getDataModelListFormNet(List<GetHealthManagementResponseEntity.DataBean> ps,
                                                                    List<GetHealthManagementResponseEntity.DataBean> pd){
        List<IndicatorDataModel> list = new ArrayList<>();
        int size = ps.size();
        for (int i = 0; i < size; i++) {
            GetHealthManagementResponseEntity.DataBean psBean = ps.get(i);
            GetHealthManagementResponseEntity.DataBean pdBean = pd.get(i);
            IndicatorDataModel model = new IndicatorDataModel();
            model.time = psBean.getDate();
            model.ps = psBean.getValue();
            model.pd = pdBean.getValue();
            model.achieveGoal = (psBean.getIsAchieveGoal() == 1) && (pdBean.getIsAchieveGoal() == 1);
            list.add(model);
        }
        return list;
    }
}
