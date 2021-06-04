package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.GetFirstAidPhoneResponseEntity;
import com.mgtech.maiganapp.data.model.FirstAidPhoneModel;

/**
 * @author Jesse
 */
public class FirstAidPhoneWrapper {
    public static FirstAidPhoneModel getModelFromNet(GetFirstAidPhoneResponseEntity entity){
        FirstAidPhoneModel model = new FirstAidPhoneModel();
        model.isBought = entity.getIsBought() == 1;
        model.phoneList = entity.getFirstAidPhones();
        return model;
    }
}
