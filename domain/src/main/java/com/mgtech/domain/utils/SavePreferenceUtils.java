package com.mgtech.domain.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.entity.net.PersonalInfoEntity;

import java.util.Calendar;

import static com.mgtech.domain.utils.PreferenceUtil.name;

/**
 *
 * @author zhaixiang
 * 工具类
 */
public class SavePreferenceUtils {
    private static final String PREF_ID = "id";
    private static final String PREF_TEMP_ID = "tempId";
    private static final String PREF_PHONE = "phone";
    private static final String PREF_ZONE = "zone";
    private static final String PREF_PASSWORD = "password";
    private static final String PREF_TOKEN = "token";
    private static final String PREF_TOKEN_AVAILABLE_TIME = "token_time";
    private static final String PREF_REFRESH_TOKEN = "refresh_token";
    private static final String PREF_TOKEN_TYPE = "token_type";
    private static final String PREF_BRACELET_HAS_NEW_VERSION = "hasNewVersion";

    private static final String PREF_LOGIN_TYPE = "loginType";
    private static final String PREF_QQ_OPEN_ID = "openIdQQ";
    private static final String PREF_QQ_UNION_ID = "unionIdQQ";
    private static final String PREF_QQ_TOKEN = "tokenQQ";

    private static final String PREF_WX_OPEN_ID = "openIdWX";
    private static final String PREF_WX_UNION_ID = "unionIdWX";
    private static final String PREF_WX_TOKEN = "tokenWX";

    public static final String PREF_SEX = "sex";
    public static final String PREF_HEIGHT = "height";
    public static final String PREF_WEIGHT = "weight";
    public static final String PREF_BIRTH = "birth";
    public static final String PREF_NAME = "name";
    public static final String PREF_AVATAR_URL = "avatarUrl";

    private static final String PREF_IS_DIRTY = "dirtyUse";

    private static final String PREF_PARAM_4_UPDATE = "param4Update";
    private static final String PREF_PARAM_5_UPDATE = "param5Update";
    private static final String PREF_SHOW_MAIN_GUIDE = "showMainGuide";
    private static final String PREF_SHOW_POWER_GUIDE = "showPowerGuide";
    private static final String PREF_FONT_SIZE = "fontSize";


    /**
     * 获取id
     *
     * @param context 上下文
     * @return id
     */
    public static String getUserId(Context context) {
        return PreferenceUtil.getStringFromPreferences(context, PREF_ID);
    }

    public static void saveTempAccount(Context context, String userId, String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_TEMP_ID, AESUtils.encryptString(userId));
        editor.putString(PREF_TOKEN, AESUtils.encryptString(token));
        editor.apply();
    }

    public static String getTempUserId(Context context){
        return PreferenceUtil.getStringFromPreferences(context, PREF_TEMP_ID);
    }

    /**
     * 保存id
     *
     * @param context 上下文
     * @param userId  id
     */
    public static void saveAccount(Context context, String userId, String phone, String zone, String password,
                                              String token, String refreshToken,int loginType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_LOGIN_TYPE,loginType);
        editor.putString(PREF_ID, AESUtils.encryptString(userId));
        editor.putString(PREF_PHONE, AESUtils.encryptString(phone));
        editor.putString(PREF_ZONE, AESUtils.encryptString(zone));
        if (!TextUtils.isEmpty(password)) {
            // 如果没有密码，不清空旧密码
            editor.putString(PREF_PASSWORD, AESUtils.encryptString(password));
        }
        editor.putString(PREF_TOKEN, AESUtils.encryptString(token));
        editor.putString(PREF_REFRESH_TOKEN, AESUtils.encryptString(refreshToken));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        editor.putLong(PREF_TOKEN_AVAILABLE_TIME, calendar.getTimeInMillis());
        editor.apply();
    }

    public static void savePersonalInfo(Context context,PersonalInfoEntity entity){
        if (entity == null){
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_ID, AESUtils.encryptString(entity.getAccountGuid()));
        editor.putString(PREF_QQ_OPEN_ID, AESUtils.encryptString(entity.getQqOpenId()));
        editor.putString(PREF_WX_OPEN_ID, AESUtils.encryptString(entity.getWxOpenId()));
        editor.putString(PREF_QQ_UNION_ID, AESUtils.encryptString(entity.getQqUnion()));
        editor.putString(PREF_WX_UNION_ID, AESUtils.encryptString(entity.getWxUnion()));
        editor.putString(PREF_QQ_TOKEN, AESUtils.encryptString(entity.getQqToken()));
        editor.putString(PREF_WX_TOKEN, AESUtils.encryptString(entity.getWxToken()));
        editor.apply();
    }

    public static int getLoginType(Context context){
        return PreferenceUtil.getIntFromPreferences(context, PREF_LOGIN_TYPE);
    }

    public static String getOpenIdQQ(Context context){
        return PreferenceUtil.getStringFromPreferences(context,PREF_QQ_OPEN_ID);
    }

    public static String getOpenIdWX(Context context){
        return PreferenceUtil.getStringFromPreferences(context,PREF_WX_OPEN_ID);
    }

    public static String getUnionIdQQ(Context context){
        return PreferenceUtil.getStringFromPreferences(context,PREF_QQ_UNION_ID);
    }

    public static String getUnionIdWX(Context context){
        return PreferenceUtil.getStringFromPreferences(context,PREF_WX_UNION_ID);
    }

    public static String getTokenQQ(Context context){
        return PreferenceUtil.getStringFromPreferences(context,PREF_QQ_TOKEN);
    }

    public static String getTokenWX(Context context){
        return PreferenceUtil.getStringFromPreferences(context,PREF_WX_TOKEN);
    }

    /**
     * 删除用户账户信息
     *
     * @param context 上下文
     */
    public static void deleteUserInfo(Context context) {
        PreferenceUtil.deletePreferences(context, PREF_ID);
        PreferenceUtil.deletePreferences(context, PREF_TOKEN);
        PreferenceUtil.deletePreferences(context, PREF_TOKEN_TYPE);
        PreferenceUtil.deletePreferences(context, PREF_TOKEN_AVAILABLE_TIME);
        PreferenceUtil.deletePreferences(context, PREF_PASSWORD);
        PreferenceUtil.deletePreferences(context, PREF_PHONE);
        PreferenceUtil.deletePreferences(context, PREF_ZONE);
        PreferenceUtil.deletePreferences(context, PREF_PARAM_4_UPDATE);
        PreferenceUtil.deletePreferences(context, PREF_PARAM_5_UPDATE);
        PreferenceUtil.deletePreferences(context, PREF_TEMP_ID);
        PreferenceUtil.deletePreferences(context, PREF_QQ_TOKEN);
        PreferenceUtil.deletePreferences(context, PREF_WX_TOKEN);
        PreferenceUtil.deletePreferences(context, PREF_QQ_UNION_ID);
        PreferenceUtil.deletePreferences(context, PREF_WX_UNION_ID);
        PreferenceUtil.deletePreferences(context, PREF_QQ_OPEN_ID);
        PreferenceUtil.deletePreferences(context, PREF_WX_OPEN_ID);
        IBraceletInfoManager manager = new BraceletInfoManagerBuilder(context).create();
        manager.deletePairInformation();
    }

    /**
     * 获取phone
     *
     * @param context 上下文
     * @return phone
     */
    public static String getPhone(Context context) {
        return PreferenceUtil.getStringFromPreferences(context, PREF_PHONE);
    }

    public static String getZone(Context context) {
        return PreferenceUtil.getStringFromPreferences(context, PREF_ZONE);
    }


    /**
     * 保存密码
     *
     * @param context  上下文
     * @param password 密码
     */
    public static void savePassword(Context context, String password) {
        PreferenceUtil.writeStringToPreferences(context, PREF_PASSWORD, password);
    }

    /**
     * 获取密码
     *
     * @param context 上下文
     * @return 密码
     */
    public static String getPassword(Context context) {
        return PreferenceUtil.getStringFromPreferences(context, PREF_PASSWORD);
    }


    /**
     * 获取token
     *
     * @param context 上下文
     * @return token
     */
    public static String getToken(Context context) {
        return PreferenceUtil.getStringFromPreferences(context, PREF_TOKEN);
    }

    /**
     * 保存 token
     *
     * @param context 上下文
     * @param token   网络令牌
     */
    public static void saveToken(Context context, String token) {
        PreferenceUtil.safeWriteStringToPreferences(context, PREF_TOKEN, token);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        PreferenceUtil.writeLongToPreferences(context, PREF_TOKEN_AVAILABLE_TIME, calendar.getTimeInMillis());
    }

    /**
     * 获取 token 到期时间
     *
     * @param context 上下文
     * @return 时间 long 值
     */
    public static long getTokenTime(Context context) {
        return PreferenceUtil.getLongToPreferences(context, PREF_TOKEN_AVAILABLE_TIME);

    }

    /**
     * 是否第一次使用
     *
     * @param context 上下文
     * @return true 不是第一次使用
     */
    public static boolean isDirtyUse(Context context) {
        return PreferenceUtil.getBooleanFromPreferences(context, PREF_IS_DIRTY);
    }

    /**
     * 设置是否第一次使用 true 不是第一次
     *
     * @param context 上下文
     */
    public static void setDirtyUse(Context context) {
        PreferenceUtil.writeBooleanToPreferences(context, PREF_IS_DIRTY, true);
    }

    /**
     * 是否需要将缓存数据更新至服务器
     *
     * @param context  上线文
     * @param isUpdate true 需要更新至服务器
     */
    public static void setParam4Update(Context context, boolean isUpdate) {
        PreferenceUtil.writeBooleanToPreferences(context, PREF_PARAM_4_UPDATE, isUpdate);
    }

    /**
     * 是否需要将缓存数据更新至服务器
     *
     * @param context  上线文
     * @param isUpdate true 需要更新至服务器
     */
    public static void setParam5Update(Context context, boolean isUpdate) {
        PreferenceUtil.writeBooleanToPreferences(context, PREF_PARAM_5_UPDATE, isUpdate);
    }

    /**
     * 是否需要将缓存数据更新至服务器
     *
     * @param context 上线文
     */
    public static boolean isParam4Update(Context context) {
        return PreferenceUtil.getBooleanFromPreferences(context, PREF_PARAM_4_UPDATE);
    }

    /**
     * 是否需要将缓存数据更新至服务器
     *
     * @param context 上线文
     */
    public static boolean isParam5Update(Context context) {
        return PreferenceUtil.getBooleanFromPreferences(context, PREF_PARAM_5_UPDATE);
    }

    public static boolean isShowMainGuide(Context context) {
        return PreferenceUtil.getBooleanFromPreferences(context, PREF_SHOW_MAIN_GUIDE, true);
    }

    public static void setShowMainGuide(Context context, boolean show) {
        PreferenceUtil.writeBooleanToPreferences(context, PREF_SHOW_MAIN_GUIDE, show);
    }

    public static boolean isShowPowerGuide(Context context) {
        return PreferenceUtil.getBooleanFromPreferences(context, PREF_SHOW_POWER_GUIDE, true);
    }

    public static void setShowPowerGuide(Context context, boolean show) {
        PreferenceUtil.writeBooleanToPreferences(context, PREF_SHOW_POWER_GUIDE, show);
    }


    public static int getFontSizeIndex(Context context) {
        return PreferenceUtil.getIntFromPreferences(context.getApplicationContext(), PREF_FONT_SIZE, 1);
    }

    public static void saveFontSizeIndex(Context context, int size) {
        PreferenceUtil.writeIntToPreferences(context, PREF_FONT_SIZE, size);
    }
}
