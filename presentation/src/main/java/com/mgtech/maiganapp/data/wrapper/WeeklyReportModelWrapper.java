package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.WeeklyReportResponseEntity;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.data.model.WeeklyReportModel;

import java.util.ArrayList;
import java.util.List;

import static com.mgtech.domain.entity.net.response.WeeklyReportResponseEntity.UNREAD;

public class WeeklyReportModelWrapper {
    public static List<WeeklyReportModel> getModelListFromNet(List<WeeklyReportResponseEntity> entities){
        List<WeeklyReportModel> list = new ArrayList<>();
        if (entities != null){
            for (WeeklyReportResponseEntity entity :entities) {
                list.add(getModelFromNet(entity));
            }
        }
        return list;
    }

    private static WeeklyReportModel getModelFromNet(WeeklyReportResponseEntity entity){
        WeeklyReportModel model = new WeeklyReportModel();
        model.startTime = entity.getStartTime();
//        model.endTime = entity.getEndTime();
        model.read = entity.getIsRead() != UNREAD;
        model.endTime = model.startTime + 24*3600_000L * 7;
        model.shareContent = entity.getShareContent();
        model.weekReportDetailUrl = entity.getWeekReportDetailUrl();
        model.weekReportGuid = entity.getWeekReportGuid();
        model.shareTitle = entity.getShareTitle();
        model.indicators = new ArrayList<>();
        WeeklyReportResponseEntity.PwDataBean pwDataBean = entity.getPwData();
        if (pwDataBean != null){
            List<WeeklyReportResponseEntity.DataBean> psBeans = pwDataBean.getPs();
            List<WeeklyReportResponseEntity.DataBean> pdBeans = pwDataBean.getPd();
            if (psBeans != null && pdBeans != null && psBeans.size() == pdBeans.size()){
                int size = psBeans.size();
                for (int i = 0; i < size; i++) {
                    WeeklyReportResponseEntity.DataBean psBean = psBeans.get(i);
                    WeeklyReportResponseEntity.DataBean pdBean = pdBeans.get(i);

                    IndicatorDataModel indicatorDataModel = new IndicatorDataModel();
                    indicatorDataModel.achieveGoal  = (psBean.getIsAchieveGoal() == 1) && (pdBean.getIsAchieveGoal() == 1);
                    indicatorDataModel.time = psBean.getDate();
                    indicatorDataModel.ps = psBean.getValue();
                    indicatorDataModel.pd = pdBean.getValue();
                    model.indicators.add(indicatorDataModel);
                }
            }
        }
        return model;
    }
}
