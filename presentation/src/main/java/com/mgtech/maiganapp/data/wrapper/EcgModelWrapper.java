package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.EcgResponseEntity;
import com.mgtech.maiganapp.data.model.EcgModel;

import java.util.ArrayList;
import java.util.List;

public class EcgModelWrapper {
    public static EcgModel getModelFromNet(EcgResponseEntity entity) {
        if (entity == null) {
            return null;
        }
        EcgModel model = new EcgModel();
        EcgResponseEntity.ResultBean resultBean = entity.getResult();
        if (resultBean != null) {
            model.hr = resultBean.getHrValue();
            model.ecgResult = resultBean.getResultOfEcg();
        }
        model.measureGuid = entity.getMeasureGuid();
        model.sampleRate = entity.getSampleRate();
        model.measureTime = entity.getMeasureTime();
        float[] data = entity.getRawData();
        if (data != null && data.length >= 2) {
            model.ecgData = new float[data.length - 2];
            System.arraycopy(data,2,model.ecgData,0,data.length-2);
        }
        return model;
    }

    public static List<EcgModel> getModelFromNet(List<EcgResponseEntity> entities) {
        List<EcgModel> list = new ArrayList<>();
        if (entities != null) {
            for (EcgResponseEntity entity : entities) {
                EcgModel model = getModelFromNet(entity);
                list.add(model);
            }
        }
        return list;
    }
}
