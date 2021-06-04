package com.mgtech.domain.entity;

import android.content.Context;
import android.text.TextUtils;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.PreferenceUtil;
import com.mgtech.domain.utils.SaveUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 *
 * @author zhaixiang
 * 用于计算的个人信息
 */

public class UserInfo {
    private int sex;
    private float height;
    private float weight;
    private String birth;
    private String name = "";
    private String avatarUrl;

    /**
     * 数据是否完整
     * @return 完整
     */
    public boolean isComplete(){
        return height != 0 && weight!= 0 && birth != null && sex != 0;
    }

    public int getSex() {
        return sex;
    }

    public int getAlgrithomSex(){
        if (sex == MyConstant.MAN){
            return 1;
        }else{
            return 0;
        }
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static UserInfo getLocalUserInfo(Context context){
        UserInfo userInfo = new UserInfo();
        userInfo.setSex(PreferenceUtil.getIntFromPreferences(context, SaveUtils.PREF_SEX));
        userInfo.setWeight(PreferenceUtil.getFloatFromPreferences(context,SaveUtils.PREF_WEIGHT));
        userInfo.setHeight(PreferenceUtil.getFloatFromPreferences(context,SaveUtils.PREF_HEIGHT));
        userInfo.setBirth(PreferenceUtil.getStringFromPreferences(context,SaveUtils.PREF_BIRTH));
        userInfo.setName(PreferenceUtil.getStringFromPreferences(context,SaveUtils.PREF_NAME));
        userInfo.setAvatarUrl(PreferenceUtil.getStringFromPreferences(context,SaveUtils.PREF_AVATAR_URL));
        return userInfo;
    }

    public static void saveLocalUserInfo(Context context, UserInfo userInfo){
        PreferenceUtil.writeIntToPreferences(context,SaveUtils.PREF_SEX,userInfo.getSex());
        PreferenceUtil.writeFloatToPreferences(context,SaveUtils.PREF_WEIGHT,userInfo.getWeight());
        PreferenceUtil.writeFloatToPreferences(context,SaveUtils.PREF_HEIGHT,userInfo.getHeight());
        PreferenceUtil.writeStringToPreferences(context,SaveUtils.PREF_BIRTH,userInfo.getBirth());
        PreferenceUtil.writeStringToPreferences(context,SaveUtils.PREF_NAME,userInfo.getName());
        PreferenceUtil.writeStringToPreferences(context,SaveUtils.PREF_AVATAR_URL,userInfo.getAvatarUrl());

        IBraceletInfoManager manager = new BraceletInfoManagerBuilder(context).create();
        manager.setHeightWeight(Math.round(userInfo.getHeight()),Math.round(userInfo.getWeight()*2));
    }


    public static void clearUserInfo(Context context){
        PreferenceUtil.deletePreferences(context,SaveUtils.PREF_SEX);
        PreferenceUtil.deletePreferences(context,SaveUtils.PREF_WEIGHT);
        PreferenceUtil.deletePreferences(context,SaveUtils.PREF_HEIGHT);
        PreferenceUtil.deletePreferences(context,SaveUtils.PREF_BIRTH);
        PreferenceUtil.deletePreferences(context,SaveUtils.PREF_NAME);
        PreferenceUtil.deletePreferences(context,SaveUtils.PREF_AVATAR_URL);
    }

    public static UserInfo saveLocalUserInfo(Context context, PersonalInfoEntity entity){
        UserInfo userInfo = new UserInfo();
        userInfo.setBirth(entity.getBirthday());
        try {
            userInfo.setHeight(Math.round(entity.getHeight()));
            userInfo.setWeight(Math.round(entity.getWeight()));
        }catch (Exception e){
            e.printStackTrace();
        }
        userInfo.setSex(entity.getSex());
        userInfo.setName(entity.getDisplayName());
        String avatarUrl = entity.getAvatarUrl();
        if (TextUtils.isEmpty(avatarUrl)){
            if (SaveUtils.getLoginType(context)== NetConstant.QQ_LOGIN) {
                avatarUrl = entity.getQqAvatarUrl();
            }else{
                avatarUrl = entity.getWxAvatarUrl();
            }
        }
        userInfo.setAvatarUrl(avatarUrl);
        UserInfo.saveLocalUserInfo(context,userInfo);
        return userInfo;
    }


    public int getAge(Context context){
        SimpleDateFormat dateFormat = new SimpleDateFormat(MyConstant.DATE_FORMAT_BIRTHDAY, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        int age = 0;
        try {
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            calendar.setTime(dateFormat.parse(PreferenceUtil.getStringFromPreferences(context,SaveUtils.PREF_BIRTH)));
            age = Math.round(((currentYear - calendar.get(Calendar.YEAR)) * 12 + currentMonth - calendar.get(Calendar.MONTH)) / (float)12);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return age;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "sex=" + sex +
                ", height=" + height +
                ", weight=" + weight +
                ", birth='" + birth + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
