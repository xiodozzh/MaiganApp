package com.mgtech.maiganapp.data.wrapper;

import android.util.Log;

import com.mgtech.domain.entity.net.response.RelationshipResponseEntity;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.maiganapp.data.model.FriendModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class FriendModelWrapper {
    public static List<FriendModel> getListFromNetEntityToFriendModel(List<RelationshipResponseEntity> list){
        List<FriendModel> modelList = new ArrayList<>();
        for (RelationshipResponseEntity entity : list) {
            FriendModel model = getModelFromNetEntity(entity);
            modelList.add(model);
        }
        return modelList;
    }

    public static FriendModel getModelFromNetEntity(RelationshipResponseEntity entity){
        FriendModel model = new FriendModel();
        if (entity == null){
            return model;
        }
        model.setName(entity.getTargetName());
        model.setNoteName(entity.getTargetNoteName());
        model.setRelationId(entity.getRelationGuid());
        model.setRelationType(entity.getType());
        model.setReadHisData(entity.getReadTarget()==1);
        model.setGetHisPushData(entity.getGetTargetPush()==1);
        model.setAllowHimReadData(entity.getAllowTargetRead()==1);
        model.setAllowPushData(entity.getAllowPushToTarget()==1);
        model.setAvatarUrl(entity.getTargetAvatarUrl());
        model.setPwUnreadNumber(entity.getPwDataUnread());
        model.setAbnormalUnreadNumber(entity.getPwAbnormalDataUnread());
        model.setPhone(entity.getPhone());
        model.setTargetUserId(entity.getTargetAccountGuid());
        model.setSex(entity.getSex());
        RelationshipResponseEntity.LastResultBean lastResultBean = entity.getLastResult();
        if (lastResultBean != null) {
            List<RelationshipResponseEntity.DataBean>dataBeans = lastResultBean.getData();
            if (dataBeans != null) {
                model.setHaveData(!dataBeans.isEmpty());
                model.setLastMeasureTime(lastResultBean.getMeasureTime());
                for (RelationshipResponseEntity.DataBean bean : dataBeans) {
                    switch (bean.getPwDataItemType()){
                        case NetConstant.HR:
                            model.setHr(bean.getValue());
                            break;
                        case NetConstant.PS:
                            model.setPs(bean.getValue());
                            break;
                        case NetConstant.PD:
                            model.setPd(bean.getValue());
                            break;
                        default:
                    }
                }
            }
        }
        return model;
    }
}
