package com.mgtech.domain.entity.net;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.HttpApi;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by zhaixiang on 2016/7/8.
 * 个人信息
 */
public class PersonalInfoEntity implements RequestEntity{
    private String accountGuid;
    private String displayName;
    @SerializedName("provinceGuid")
    private String provinceId;
    @SerializedName("countryGuid")
    private String countryId;
    private float height;
    private float weight;
    private int sex;
    private String birthday;
    private String avatarUrl;
    private String location;
    @SerializedName("wechatOpenId")
    private String wxOpenId;
    @SerializedName("wechatNickName")
    private String wxNickName;
    @SerializedName("wechatAvatarUrl")
    private String wxAvatarUrl;
    @SerializedName("wechatAccessToken")
    private String wxToken;
    @SerializedName("wechatUnionId")
    private String wxUnion;
    @SerializedName("qqOpenId")
    private String qqOpenId;
    @SerializedName("qqNickName")
    private String qqNickName;
    @SerializedName("qqAvatarUrl")
    private String qqAvatarUrl;
    @SerializedName("qqAccessToken")
    private String qqToken;
    @SerializedName("qqUnionId")
    private String qqUnion;
    @SerializedName("diseaseHistory")
    private List<DiseaseBean> diseaseHistory;
    @SerializedName("organDamage")
    private List<DiseaseBean> organDamage;
    @SerializedName("highBloodPressure")
    private List<DiseaseBean> highBloodPressure;
    @SerializedName("otherRiskFactors")
    private List<DiseaseBean> other;
    @SerializedName("phone")
    private String phone;
    @SerializedName("zone")
    private String zone;

    @Override
    public String getUrl() {
        return HttpApi.API_SET_PERSONAL_INFO_URL;
    }

    @Override
    public String getBody() {
        return new Gson().toJson(this);
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getWxNickName() {
        return wxNickName;
    }

    public void setWxNickName(String wxNickName) {
        this.wxNickName = wxNickName;
    }

    public String getWxAvatarUrl() {
        return wxAvatarUrl;
    }

    public void setWxAvatarUrl(String wxAvatarUrl) {
        this.wxAvatarUrl = wxAvatarUrl;
    }

    public String getWxToken() {
        return wxToken;
    }

    public void setWxToken(String wxToken) {
        this.wxToken = wxToken;
    }

    public String getWxUnion() {
        return wxUnion;
    }

    public void setWxUnion(String wxUnion) {
        this.wxUnion = wxUnion;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public String getQqNickName() {
        return qqNickName;
    }

    public void setQqNickName(String qqNickName) {
        this.qqNickName = qqNickName;
    }

    public String getQqAvatarUrl() {
        return qqAvatarUrl;
    }

    public void setQqAvatarUrl(String qqAvatarUrl) {
        this.qqAvatarUrl = qqAvatarUrl;
    }

    public String getQqToken() {
        return qqToken;
    }

    public void setQqToken(String qqToken) {
        this.qqToken = qqToken;
    }

    public String getQqUnion() {
        return qqUnion;
    }

    public void setQqUnion(String qqUnion) {
        this.qqUnion = qqUnion;
    }

    public String getAccountGuid() {
        return accountGuid;
    }

    public void setAccountGuid(String accountGuid) {
        this.accountGuid = accountGuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<DiseaseBean> getOther() {
        return other;
    }

    public void setOther(List<DiseaseBean> other) {
        this.other = other;
    }

    public List<DiseaseBean> getDiseaseHistory() {
        return diseaseHistory;
    }

    public void setDiseaseHistory(List<DiseaseBean> diseaseHistory) {
        this.diseaseHistory = diseaseHistory;
    }

    public List<DiseaseBean> getOrganDamage() {
        return organDamage;
    }

    public void setOrganDamage(List<DiseaseBean> organDamage) {
        this.organDamage = organDamage;
    }

    public List<DiseaseBean> getHighBloodPressure() {
        return highBloodPressure;
    }

    public void setHighBloodPressure(List<DiseaseBean> highBloodPressure) {
        this.highBloodPressure = highBloodPressure;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "PersonalInfoEntity{" +
                "accountGuid='" + accountGuid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", countryId='" + countryId + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", sex=" + sex +
                ", birthday='" + birthday + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", location='" + location + '\'' +
                ", wxOpenId='" + wxOpenId + '\'' +
                ", wxNickName='" + wxNickName + '\'' +
                ", wxAvatarUrl='" + wxAvatarUrl + '\'' +
                ", wxToken='" + wxToken + '\'' +
                ", wxUnion='" + wxUnion + '\'' +
                ", qqOpenId='" + qqOpenId + '\'' +
                ", qqNickName='" + qqNickName + '\'' +
                ", qqAvatarUrl='" + qqAvatarUrl + '\'' +
                ", qqToken='" + qqToken + '\'' +
                ", qqUnion='" + qqUnion + '\'' +
                ", diseaseHistory=" + diseaseHistory +
                ", organDamage=" + organDamage +
                ", highBloodPressure=" + highBloodPressure +
                ", other=" + other +
                ", phone='" + phone + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }

    public static class DiseaseBean {
        private String name;
        private String guid;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        @Override
        public String toString() {
            return "DiseaseBean{" +
                    "name='" + name + '\'' +
                    ", id='" + guid + '\'' +
                    '}';
        }
    }
}
