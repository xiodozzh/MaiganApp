package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.GetHomeDataResponseEntity;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.maiganapp.data.model.HomeDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class HomeDataModelWrapper {
    public static HomeDataModel getModelFromNet(GetHomeDataResponseEntity entity) {
        if (entity == null) {
            return null;
        }
        HomeDataModel model = new HomeDataModel();
        GetHomeDataResponseEntity.LastDayPWDataBean lastDayPWDataBean = entity.getLastDayPWData();
        if (lastDayPWDataBean != null) {
            model.pwTime = lastDayPWDataBean.getDate();
            GetHomeDataResponseEntity.ParamInfoBean infoBean = lastDayPWDataBean.getParamInfos();
            if (infoBean != null) {
                model.psList = getDataListFromNet(infoBean.getPs());
                model.pdList = getDataListFromNet(infoBean.getPd());
            }
        }
        GetHomeDataResponseEntity.LastTimePWDataBean lastTimePWDataBean = entity.getLastTimePWData();
        if (lastTimePWDataBean != null) {
            model.currentMeasureTime = lastTimePWDataBean.getMeasureTime();
            List<GetHomeDataResponseEntity.LastDataBean> lastDataBeans = lastTimePWDataBean.getData();
            if (lastDataBeans != null) {
                for (GetHomeDataResponseEntity.LastDataBean lastDataBean : lastDataBeans) {
                    if (lastDataBean.getPwDataItemType() == NetConstant.PS) {
                        model.currentPs = lastDataBean.getValue();
                        model.psLevel = lastDataBean.getResultOfAnalysis();
                    } else if (lastDataBean.getPwDataItemType() == NetConstant.PD) {
                        model.currentPd = lastDataBean.getValue();
                        model.pdLevel = lastDataBean.getResultOfAnalysis();
                    } else if (lastDataBean.getPwDataItemType() == NetConstant.V0) {
                        model.currentV0 = lastDataBean.getValue();
                        model.v0Level = lastDataBean.getResultOfAnalysis();
                    }else if (lastDataBean.getPwDataItemType() == NetConstant.HR){
                        model.currentHr = lastDataBean.getValue();
                        model.hrLevel = lastDataBean.getResultOfAnalysis();
                    }
                }
            }
        }
        GetHomeDataResponseEntity.LastTimeECGDataBean lastTimeECGDataBean = entity.getLastTimeECGData();
        if (lastTimeECGDataBean != null) {
            model.ecgTime = lastTimeECGDataBean.getMeasureTime();
            float[] rawData = lastTimeECGDataBean.getRawData();
            if (rawData != null && rawData.length > 0) {
                model.ecgData = new float[rawData.length - 1];
                System.arraycopy(rawData, 1, model.ecgData, 0, model.ecgData.length);
            }

        }
        GetHomeDataResponseEntity.MedicationReminder remindBeans = entity.getMedicationRemind();
        if (remindBeans != null) {
            model.nextDrugCount = remindBeans.getNextTimeDrugVariety();
            model.nextMedicationTime = remindBeans.getNextTime();
            model.futureDrugCount = remindBeans.getFutureRemindCount();
            model.forgetDrugCount = remindBeans.getForgetRemindCount();
        }

        return model;
    }

    private static List<HomeDataModel.Data> getDataListFromNet(List<GetHomeDataResponseEntity.DataBean> beans) {
        List<HomeDataModel.Data> list = new ArrayList<>();
        if (beans == null) {
            return list;
        }
        for (GetHomeDataResponseEntity.DataBean bean : beans) {
            HomeDataModel.Data data = new HomeDataModel.Data();
            data.time = bean.getDate();
            data.value = bean.getValue();
            list.add(data);
        }
        return list;
    }
}
