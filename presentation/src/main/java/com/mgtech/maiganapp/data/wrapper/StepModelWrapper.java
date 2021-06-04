package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.StepDetailItemEntity;
import com.mgtech.maiganapp.data.model.StepModel;

import java.util.ArrayList;
import java.util.List;

public class StepModelWrapper {
    public static List<StepModel> getModelListFromNet(List<StepDetailItemEntity> entities){
        List<StepModel>list = new ArrayList<>();
        if (entities != null) {
            for (StepDetailItemEntity entity : entities) {
                list.add(getModelFromNet(entity));
            }
        }
        return list;
    }

    public static StepModel getModelFromNet(StepDetailItemEntity entity){
        StepModel model = new StepModel();
        model.startTime = entity.getStartTime();
        model.endTime = entity.getEndTime();
        model.duration = entity.getDuration();
        model.distance = entity.getDistance();
        model.heat = entity.getHeat();
        model.stepCount = (int)entity.getStepCount();
        return model;
    }
}
