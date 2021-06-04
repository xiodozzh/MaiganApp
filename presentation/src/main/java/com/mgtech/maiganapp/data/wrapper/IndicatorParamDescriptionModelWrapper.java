package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.IndicatorDescriptionResponseEntity;
import com.mgtech.maiganapp.data.model.IndicatorParamDescriptionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class IndicatorParamDescriptionModelWrapper {
    public static List<IndicatorParamDescriptionModel> getModelListFromNet(List<IndicatorDescriptionResponseEntity> entities){
        List<IndicatorParamDescriptionModel> models = new ArrayList<>();
        if (entities != null){
            for (IndicatorDescriptionResponseEntity entity: entities){
                models.add(getModelFromNet(entity));
            }
        }
        return models;
    }

    private static IndicatorParamDescriptionModel getModelFromNet(IndicatorDescriptionResponseEntity entity){
        IndicatorParamDescriptionModel model = new IndicatorParamDescriptionModel();
        model.type = entity.getPwDataItemType();
        model.name = entity.getItemName();
        model.shortName = entity.getAbbreviation();
        model.explain = entity.getExplain();
        model.futures = entity.getFeatures();
        model.sexType = entity.getApplicableSex();
        model.lowerLimit = entity.getLowerLimit();
        model.upperLimit = entity.getUpperLimit();
        return model;
    }
}
