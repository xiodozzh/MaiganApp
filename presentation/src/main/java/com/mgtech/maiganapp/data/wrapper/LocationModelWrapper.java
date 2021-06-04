package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.CountryCodeEntity;
import com.mgtech.domain.entity.net.response.ProvinceCodeEntity;
import com.mgtech.maiganapp.data.model.LocationModel;

import java.util.ArrayList;
import java.util.List;

public class LocationModelWrapper {
    public static List<LocationModel> getModelListFromCountryEntity(List<CountryCodeEntity>entities){
        List<LocationModel> list = new ArrayList<>();
        if (entities != null){
            for (CountryCodeEntity entity :entities) {
                LocationModel model = new LocationModel();
                model.id = entity.getGuid();
                model.name = entity.getName();
                model.hasCities = entity.getContainsCity()==1;
                list.add(model);
            }
        }
        return list;
    }

    public static List<LocationModel> getModelListFromProvinceEntity(List<ProvinceCodeEntity>entities){
        List<LocationModel> list = new ArrayList<>();
        if (entities != null){
            for (ProvinceCodeEntity entity :entities) {
                LocationModel model = new LocationModel();
                model.id = entity.getGuid();
                model.name = entity.getName();
                list.add(model);
            }
        }
        return list;
    }
}
