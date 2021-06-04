package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.CustomMedicineEntity;
import com.mgtech.domain.entity.net.response.MedicineResponseEntity;
import com.mgtech.maiganapp.data.model.IMedicineModel;

public class MedicineModelWrapper {
    public static IMedicineModel.Content wrapFromLib(MedicineResponseEntity entity){
        return new IMedicineModel.Content(entity.getDrugGuid(),
                entity.getName(), entity.getMetering(), entity.getSpecs(), entity.getDosage(),false);
    }

    public static IMedicineModel.Content wrapFromCustom(CustomMedicineEntity entity){
        return new IMedicineModel.Content(entity.getGuid(),
                entity.getName(), "", "", "",true);
    }
}
