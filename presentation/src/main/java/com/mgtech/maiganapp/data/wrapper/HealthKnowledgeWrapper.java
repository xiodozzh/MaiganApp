package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.HealthKnowledgeResponseEntity;
import com.mgtech.maiganapp.data.model.HealthKnowledgeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class HealthKnowledgeWrapper {
    private static HealthKnowledgeModel getModelFromNet(HealthKnowledgeResponseEntity entity){
        HealthKnowledgeModel model = new HealthKnowledgeModel();
        if (entity == null){
            return model;
        }
        model.imgUrl = entity.getImageUrl();
        model.url = entity.getDetailUrl();
        model.knowledgeId = entity.getHealthKnowledgeGuid();
        model.title = entity.getTitle();
        model.subTitle = entity.getSubTitle();
        model.time = entity.getDate();
        model.isRead = entity.getIsRead() != 0;
        return model;
    }

    public static List<HealthKnowledgeModel>getModelListFromNet(List<HealthKnowledgeResponseEntity>entities){
        List<HealthKnowledgeModel> list = new ArrayList<>();
        if (entities != null){
            for (HealthKnowledgeResponseEntity entity:entities) {
                list.add(getModelFromNet(entity));
            }
        }
        return list;
    }
}
