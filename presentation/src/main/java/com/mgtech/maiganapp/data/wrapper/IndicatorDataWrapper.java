package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.PwDataResponseEntity;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class IndicatorDataWrapper {

    public static List<IndicatorDataModel> getListFromNet(List<PwDataResponseEntity> entities){
        List<IndicatorDataModel> list = new ArrayList<>();
        if (entities != null){
            for (PwDataResponseEntity entity : entities) {
                list.add(getModelFromPwDataResponseEntity(entity));
            }
        }
        return list;
    }

    /**
     * 将网络数据转化为model
     * @param entity 网络类型
     * @return 显示类型
     */
    public static IndicatorDataModel getModelFromPwDataResponseEntity(PwDataResponseEntity entity) {
        IndicatorDataModel model = new IndicatorDataModel();
        model.time = entity.getMeasureTime();
        model.id = entity.getMeasureGuid();
        model.remark = entity.getRemark();
        model.rawDataFileId = entity.getRawDataFileId();
        model.isAuto = entity.getIsAutoSampling() == 1;
        List<PwDataResponseEntity.PwDataBean> list = entity.getData();
        if (list != null) {
            for (PwDataResponseEntity.PwDataBean bean : list) {
                switch (bean.getPwDataItemType()) {
                    case NetConstant.HR:
                        model.hr = bean.getValue();
                        model.hrUnit = bean.getUnit();
                        model.hrLevel = bean.getResultOfAnalysis();
                        model.hrScore = bean.getScore();
                        break;
                    case NetConstant.PS:
                        model.ps = bean.getValue();
                        model.psUnit = bean.getUnit();
                        model.psLevel = bean.getResultOfAnalysis();
                        model.psScore = bean.getScore();
                        break;
                    case NetConstant.PD:
                        model.pd = bean.getValue();
                        model.pdUnit = bean.getUnit();
                        model.pdLevel = bean.getResultOfAnalysis();
                        model.pdScore = bean.getScore();
                        break;
                    case NetConstant.CO:
                        model.co = bean.getValue();
                        model.coUnit = bean.getUnit();
                        model.coLevel = bean.getResultOfAnalysis();
                        model.coScore = bean.getScore();
                        break;
                    case NetConstant.V0:
                        model.v0 = bean.getValue();
                        model.v0Unit = bean.getUnit();
                        model.v0Level = bean.getResultOfAnalysis();
                        model.v0Score = bean.getScore();
                        break;
                    case NetConstant.TPR:
                        model.tpr = bean.getValue();
                        model.tprUnit = bean.getUnit();
                        model.tprLevel = bean.getResultOfAnalysis();
                        model.tprScore = bean.getScore();
                        break;
                    case NetConstant.PM:
                        model.pm = bean.getValue();
                        model.pmUnit = bean.getUnit();
                        model.pmLevel = bean.getResultOfAnalysis();
                        model.pmScore = bean.getScore();
                        break;
                    default:
                }
            }
        }
        return model;
    }



}
