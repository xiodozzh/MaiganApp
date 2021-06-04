package com.mgtech.maiganapp.data.wrapper;

import android.text.TextUtils;

import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.data.model.DiseaseModel;
import com.mgtech.maiganapp.data.model.PersonalInfoModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PersonalInfoModelWrapper {

    public static PersonalInfoModel getModelFromNet(PersonalInfoEntity entity){
        if (entity == null){
            return null;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        PersonalInfoModel model = new PersonalInfoModel();
        model.gender = entity.getSex();
        model.weight = entity.getWeight();
        model.height = entity.getHeight();
        model.avatarUrl = entity.getAvatarUrl();
        String birthday = entity.getBirthday();
        model.birthday = Calendar.getInstance();
        if (!TextUtils.isEmpty(birthday)){
            try{
                Date date = format.parse(birthday);
                model.birthday.setTime(date);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        model.provinceId = entity.getProvinceId();
        model.countryId = entity.getCountryId();
        model.diseaseHistory = DiseaseModelWrapper.getListFromPersonalInfoDiseaseBeans(entity.getDiseaseHistory());
        model.organDamage = DiseaseModelWrapper.getListFromPersonalInfoDiseaseBeans(entity.getOrganDamage());
        model.highBloodPressure = DiseaseModelWrapper.getListFromPersonalInfoDiseaseBeans(entity.getHighBloodPressure());
        model.otherRiskFactors = DiseaseModelWrapper.getListFromPersonalInfoDiseaseBeans(entity.getOther());
        model.userId = entity.getAccountGuid();
        model.userName = entity.getDisplayName();
        model.wxAvatar = entity.getWxAvatarUrl();
        model.wxName = entity.getWxNickName();
        model.qqAvatar = entity.getQqAvatarUrl();
        model.qqName = entity.getQqNickName();
        model.location = entity.getLocation();
        return model;
    }

    public static PersonalInfoEntity modelToNet(PersonalInfoModel model){
        if (model == null){
            return null;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        PersonalInfoEntity entity = new PersonalInfoEntity();
        entity.setDisplayName(model.userName);
        entity.setSex(model.gender);
        entity.setHeight(model.height);
        entity.setWeight(model.weight);
        entity.setBirthday(format.format(model.birthday.getTime()));
        entity.setProvinceId(model.provinceId);
        entity.setCountryId(model.countryId);
        entity.setAvatarUrl(model.avatarUrl);
        entity.setHighBloodPressure(DiseaseModelWrapper.transferListToBeans(model.highBloodPressure));
        entity.setOther(DiseaseModelWrapper.transferListToBeans(model.otherRiskFactors));
        entity.setDiseaseHistory(DiseaseModelWrapper.transferListToBeans(model.diseaseHistory));
        entity.setOrganDamage(DiseaseModelWrapper.transferListToBeans(model.organDamage));
        entity.setAccountGuid(model.userId);
        return entity;
    }
}
