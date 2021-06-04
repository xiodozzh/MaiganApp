package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.ExceptionResponseEntity;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.maiganapp.data.model.ExceptionRecordModel;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;

import java.util.ArrayList;
import java.util.List;

public class ExceptionRecordModelWrapper {
    public static List<ExceptionRecordModel> getModelListFromNet(List<ExceptionResponseEntity> entities){
        List<ExceptionRecordModel> list = new ArrayList<>();
        for (ExceptionResponseEntity entity:entities){
            list.add(getModelFromNet(entity));
        }
        return list;
    }

    public static ExceptionRecordModel getModelFromNet(ExceptionResponseEntity entity){
        ExceptionRecordModel model = new ExceptionRecordModel();
        model.noticeId = entity.getNoticeGuid();
        model.description = entity.getDesc();
        model.isRead = entity.getIsRead() == 1;
        model.noticeTime = entity.getNoticeTime();
        model.indicator = getModelFromExceptionResponseEntity(entity);
        return model;
    }


    /**
     * 将网络数据转化为model
     * @param entity 网络类型
     * @return 显示类型
     */
    private static IndicatorDataModel getModelFromExceptionResponseEntity(ExceptionResponseEntity entity) {
        IndicatorDataModel model = new IndicatorDataModel();
        model.time = entity.getMeasureTime();
        model.id = entity.getMeasureGuid();
        model.remark = entity.getRemark();
        List<ExceptionResponseEntity.DataBean> list = entity.getData();
        if (list != null) {
            for (ExceptionResponseEntity.DataBean bean : list) {
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
