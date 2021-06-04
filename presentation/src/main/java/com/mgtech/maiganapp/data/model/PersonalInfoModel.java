package com.mgtech.maiganapp.data.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author zhaixiang
 */
public class PersonalInfoModel {
    public String userId;
    public String userName;
    public String provinceId;
    public String countryId;
    public String location = "";
    public float height;
    public float weight;
    public int gender;
    public Calendar birthday;
    public String avatarUrl;
    public List<DiseaseModel> diseaseHistory;
    public List<DiseaseModel> organDamage;
    public List<DiseaseModel> highBloodPressure;
    public List<DiseaseModel> otherRiskFactors;

    public String wxName;
    public String wxAvatar;
    public String qqName;
    public String qqAvatar;
}
