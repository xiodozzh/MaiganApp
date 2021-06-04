package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.ExceptionResponseEntity;
import com.mgtech.domain.entity.net.response.FriendDataResponseEntity;
import com.mgtech.domain.entity.net.response.GetHomeDataResponseEntity;
import com.mgtech.maiganapp.data.model.FriendDataModel;

import java.util.ArrayList;
import java.util.List;

public class FriendDataModelWrapper {
    public static FriendDataModel getModelFromNet(FriendDataResponseEntity entity){
        FriendDataModel model = new FriendDataModel();
        model.lastMeasureTime = entity.getLastMeasureTime();
        model.lastRemindTime = entity.getLastRemindTime();
        model.haveDataPermission = entity.getHasDataPermission() == 1;
        model.haveNotificationPermission = entity.getHasNotificationPermission() == 1;
        ExceptionResponseEntity exceptionResponseEntity = entity.getLastTimeAbnormalRecord();
        if (exceptionResponseEntity != null){
            model.lastException = ExceptionRecordModelWrapper.getModelFromNet(exceptionResponseEntity);
        }
        GetHomeDataResponseEntity.LastDayPWDataBean lastTimePWDataBean = entity.getLastDayPWData();
        if (lastTimePWDataBean != null){
            GetHomeDataResponseEntity.ParamInfoBean infoBean = lastTimePWDataBean.getParamInfos();
            model.pwTime = lastTimePWDataBean.getDate();
            if (infoBean != null){
                model.psList = getDataListFromNet(infoBean.getPs());
                model.pdList = getDataListFromNet(infoBean.getPd());
            }
        }
        GetHomeDataResponseEntity.LastTimeECGDataBean lastTimeECGDataBean = entity.getLastTimeECGData();
        if (lastTimeECGDataBean != null){
            model.ecgTime = lastTimeECGDataBean.getMeasureTime();
            float[] rawData = lastTimeECGDataBean.getRawData();
            if (rawData != null && rawData.length > 0) {
                model.ecgData = new float[rawData.length - 1];
                System.arraycopy(rawData,1,model.ecgData,0,model.ecgData.length);
            }

        }
        model.abnormalUnreadNumber = entity.getPwAbnormalDataUnread();
        model.pwUnreadNumber = entity.getPwDataUnread();
        return model;
    }

    private static List<FriendDataModel.Data> getDataListFromNet(List<GetHomeDataResponseEntity.DataBean> beans){
        List<FriendDataModel.Data> list = new ArrayList<>();
        if (beans == null){
            return list;
        }
        for (GetHomeDataResponseEntity.DataBean bean :beans) {
            FriendDataModel.Data data = new FriendDataModel.Data();
            data.time = bean.getDate();
            data.value = bean.getValue();
            list.add(data);
        }
        return list;
    }
}
