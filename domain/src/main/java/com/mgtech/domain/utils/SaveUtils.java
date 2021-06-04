package com.mgtech.domain.utils;

import android.content.Context;
import android.text.TextUtils;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaixiang
 */
public class SaveUtils {
    private static final String MMKV_ID = "SaveUtils";
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
    private static final String PREF_QQ_NAME = "nickNameQQ";

    private static final String PREF_WX_OPEN_ID = "openIdWX";
    private static final String PREF_WX_UNION_ID = "unionIdWX";
    private static final String PREF_WX_TOKEN = "tokenWX";
    private static final String PREF_WX_NAME = "nickNameWX";

    private static final String PREF_IS_DIRTY = "dirtyUse";

    private static final String PREF_PARAM_REMINDERS_UPDATE = "param4Update";
    private static final String PREF_PARAM_DISPLAY_UPDATE = "param5Update";
    private static final String PREF_SHOW_MAIN_GUIDE = "showMainGuide";
    private static final String PREF_SHOW_POWER_GUIDE = "showPowerGuide";

    private static final String PREF_FONT_SIZE = "fontSize";

    public static final String PREF_SEX = "sex";
    public static final String PREF_HEIGHT = "height";
    public static final String PREF_WEIGHT = "weight";
    public static final String PREF_BIRTH = "birth";
    public static final String PREF_NAME = "name";
    public static final String PREF_AVATAR_URL = "avatarUrl";

    public static final String PREF_NEED_SAVE_PUSH_ID = "needSavePushId";

    private static final String ECG_GUIDE = "showEcgGuide";

    private static final String PREF_SERVICE_AUTHOR_CODE = "serviceAuthorCode";
    private static final String PREF_EMERGENCY_HIDE = "emergencyHide";
    private static final String PREF_EMERGENCY_Y_POSITION = "emergencyYPosition";
    private static final String PREF_EMERGENCY_X_POSITION = "emergencyXPosition";
    private static final String PREF_PROTOCOL_SIGNED = "protocolSignedV1.0";
    private static final String PREF_FIRST_AID_PHONES = "firstAidPhones";
    private static final String PREF_FIRST_AID_BOUGHT = "isFirstAidBought";

    private static final String PREF_PERMISSION_ACCESS_STORAGE = "permissionAccessStorage";
    private static final String PREF_PERMISSION_CAMERA = "permissionCamera";
    private static final String PREF_PERMISSION_READ_CONTACTS = "permissionReadContacts";

    private static final String PREF_ALLOW_CUSTOMER_CONTACT_BY_PHONE = "allowCustomerServiceContactByPhone";
    private static final String PREF_ALLOW_CUSTOMER_CONTACT_BY_WECHAT = "allowCustomerServiceContactByWechat";

    private static final String PREF_LAST_PW_TIME = "lastPwTime";
    private static final String PREF_LAST_ECG_TIME = "lastEcgTime";

    /**
     * 获取id
     *
     * @param context 上下文
     * @return id
     */
    public static String getUserId(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_ID);
    }

    public static String getUserId() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_ID);
    }

    public static void saveTempAccount(Context context, String userId, String token) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_TEMP_ID, userId);
        mmkv.encode(PREF_TOKEN, token);
    }

    public static String getTempUserId(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_TEMP_ID);
    }

    public static boolean isNeedPushId() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(PREF_NEED_SAVE_PUSH_ID);
    }

    public static void setNeedPushId(boolean needSave) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_NEED_SAVE_PUSH_ID, needSave);
    }

    /**
     * 保存id
     *
     * @param context 上下文
     * @param userId  id
     */
    public static void saveAccount(Context context, String userId, String phone, String zone, String password,
                                   String token, String refreshToken, long expiresTime, int loginType) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_LOGIN_TYPE, loginType);
        mmkv.encode(PREF_ID, userId);
        mmkv.encode(PREF_PHONE, phone);
        mmkv.encode(PREF_ZONE, zone);
        if (!TextUtils.isEmpty(password)) {
            // 如果没有密码，不清空旧密码
            mmkv.encode(PREF_PASSWORD, password);
        }
        mmkv.encode(PREF_TOKEN, token);
        mmkv.encode(PREF_REFRESH_TOKEN, refreshToken);
        mmkv.encode(PREF_TOKEN_AVAILABLE_TIME, avaiableTime(expiresTime));
    }

    public static String getRefreshToken() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_REFRESH_TOKEN);
    }

    public static void removeRefreshToken() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.remove(PREF_REFRESH_TOKEN);
    }


    public static void savePersonalInfo(Context context, PersonalInfoEntity entity) {
        if (entity == null) {
            return;
        }
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_ID, entity.getAccountGuid());
        mmkv.encode(PREF_QQ_OPEN_ID, entity.getQqOpenId());
        mmkv.encode(PREF_WX_OPEN_ID, entity.getWxOpenId());
        mmkv.encode(PREF_QQ_UNION_ID, entity.getQqUnion());
        mmkv.encode(PREF_WX_UNION_ID, entity.getWxUnion());
        mmkv.encode(PREF_QQ_TOKEN, entity.getQqToken());
        mmkv.encode(PREF_WX_TOKEN, entity.getWxToken());
        mmkv.encode(PREF_WX_NAME, entity.getWxNickName());
        mmkv.encode(PREF_QQ_NAME, entity.getQqNickName());
    }

    public static int getLoginType(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeInt(PREF_LOGIN_TYPE);
    }

    public static String getOpenIdQQ(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_QQ_OPEN_ID);
    }

    public static String getOpenIdWX(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_WX_OPEN_ID);
    }

    public static String getUnionIdQQ(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_QQ_UNION_ID);
    }

    public static String getUnionIdWX(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_WX_UNION_ID);
    }

    public static String getTokenQQ(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_QQ_TOKEN);
    }

    public static String getTokenWX(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_WX_TOKEN);
    }

    public static String getQQName() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_QQ_NAME);
    }

    public static String getWeChatName() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_WX_NAME);
    }

    /**
     * 删除用户账户信息
     *
     * @param context 上下文
     */
    public static void deleteUserInfo(Context context) {
        IBraceletInfoManager manager = new BraceletInfoManagerBuilder(context).create();
        manager.deletePairInformation();

        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.removeValuesForKeys(new String[]{
                PREF_ID,
                PREF_TOKEN,
                PREF_TOKEN_TYPE,
                PREF_TOKEN_AVAILABLE_TIME,
                PREF_PASSWORD,
                PREF_PHONE,
                PREF_ZONE,
                PREF_PARAM_REMINDERS_UPDATE,
                PREF_PARAM_DISPLAY_UPDATE,
                PREF_TEMP_ID,
                PREF_QQ_TOKEN,
                PREF_WX_TOKEN,
                PREF_QQ_UNION_ID,
                PREF_WX_UNION_ID,
                PREF_QQ_OPEN_ID,
                PREF_WX_OPEN_ID,
                PREF_FIRST_AID_BOUGHT,
                PREF_FIRST_AID_PHONES
        });
    }

    /**
     * 获取phone
     *
     * @param context 上下文
     * @return phone
     */
    public static String getPhone(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_PHONE);
    }

    public static String getZone(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_ZONE);
    }


    /**
     * 保存密码
     *
     * @param context  上下文
     * @param password 密码
     */
    public static void savePassword(Context context, String password) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_PASSWORD, password);
    }

    /**
     * 获取密码
     *
     * @param context 上下文
     * @return 密码
     */
    public static String getPassword(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_PASSWORD);
    }


    /**
     * 获取token
     *
     * @param context 上下文
     * @return token
     */
    public static String getToken(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_TOKEN);
    }

    /**
     * 保存 token
     *
     * @param token 网络令牌
     */
    public static void saveToken(String token, String refreshToken, long expiresTime) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_TOKEN, token);
        mmkv.encode(PREF_TOKEN_AVAILABLE_TIME, avaiableTime(expiresTime));
        mmkv.encode(PREF_REFRESH_TOKEN, refreshToken);
        Logger.i("saveToken: " + token);
    }

    private static long avaiableTime(long expiresInSeconds) {
        return Calendar.getInstance().getTimeInMillis() + expiresInSeconds * TimeUnit.SECONDS.toMillis(1);
    }

    /**
     * 获取 token 到期时间
     *
     * @return 时间 (s)
     */
    public static long getTokenTime() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeLong(PREF_TOKEN_AVAILABLE_TIME);
    }

    /**
     * 是否第一次使用
     *
     * @param context 上下文
     * @return true 不是第一次使用
     */
    public static boolean isDirtyUse(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(PREF_IS_DIRTY);
    }

    /**
     * 设置是否第一次使用 true 不是第一次
     *
     * @param context 上下文
     */
    public static void setDirtyUse(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_IS_DIRTY, true);
    }

    /**
     * 是否需要将缓存数据更新至服务器
     *
     * @param context  上下文
     * @param isUpdate true 需要更新至服务器
     */
    public static void setRemindersNeedUpdate(Context context, boolean isUpdate) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_PARAM_REMINDERS_UPDATE, isUpdate);
    }

    /**
     * 是否需要将缓存数据更新至服务器
     *
     * @param context  上下文
     * @param isUpdate true 需要更新至服务器
     */
    public static void setParamDisplayUpdate(Context context, boolean isUpdate) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_PARAM_DISPLAY_UPDATE, isUpdate);
    }

    /**
     * 是否需要将缓存数据更新至服务器
     *
     * @param context 上线文
     */
    public static boolean isParamRemindersUpdate(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(PREF_PARAM_REMINDERS_UPDATE);
    }

    /**
     * 是否需要将缓存数据更新至服务器
     *
     * @param context 上下文
     */
    public static boolean isParam5Update(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(PREF_PARAM_DISPLAY_UPDATE);
    }

//    public static boolean isShowMainGuide(Context context) {
//        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
//        return mmkv.decodeBool(PREF_SHOW_MAIN_GUIDE, true);
//    }
//
//    public static void setShowMainGuide(Context context, boolean show) {
//        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
//        mmkv.encode(PREF_SHOW_MAIN_GUIDE, show);
//    }
//
//    public static boolean isShowPowerGuide(Context context) {
//        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
//        return mmkv.decodeBool(PREF_SHOW_POWER_GUIDE, true);
//    }
//
//    public static void setShowPowerGuide(Context context, boolean show) {
//        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
//        mmkv.encode(PREF_SHOW_POWER_GUIDE, show);
//    }

    public static int getFontSizeIndex(Context context) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeInt(PREF_FONT_SIZE, 1);
    }

    public static void saveFontSizeIndex(Context context, int size) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_FONT_SIZE, size);
    }


    public static void guideMeasureEcgWatched() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(ECG_GUIDE, true);
    }

    public static boolean doesGuideMeasureEcgWatched() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(ECG_GUIDE, false);
    }


    private static final String GUIDE_CHECK_HR = "guideCheckHr";
    private static final String GUIDE_CHECK_BP = "guideCheckBp";
    private static final String GUIDE_CHECK_HISTORY_LIST = "guideCheckHistoryList";
    private static final String GUIDE_MEASURE = "guideMeasure";
    private static final String GUIDE_CHECK_BAND = "guideCheckBand";
    private static final String GUIDE_WATCH_PW_VIDEO = "guideWatchVideo";
    private static final String GUIDE_CHECK_FRIEND_DATA = "guideCheckFriendData";
    private static final String GUIDE_SET_NOTIFICATION = "guideSetNotification";
    private static final String GUIDE_CHECK_RADAR = "guideRadar";
    private static final String NEVER_SHOW_MEASURE_PW_TIMEOUT_DIALOG = "neverShowMeasurePwTimeoutDialog";


    public static void guideCheckHr() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(GUIDE_CHECK_HR, true);
    }

    public static boolean doesGuideCheckHrWatched() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(GUIDE_CHECK_HR, false);
    }

    public static void guideCheckBp() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(GUIDE_CHECK_BP, true);
    }

    public static boolean doesGuideCheckBpWatched() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(GUIDE_CHECK_BP, false);
    }

    public static void guideMeasure() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(GUIDE_MEASURE, true);
    }

    public static boolean doesGuideMeasureWatched() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(GUIDE_MEASURE, true);
    }

    public static void guideCheckHistoryList() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(GUIDE_CHECK_HISTORY_LIST, true);
    }

    public static boolean doesGuideCheckHistoryWatched() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(GUIDE_CHECK_HISTORY_LIST, false);
    }


    public static boolean doesGuidPwVideoWatched() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(GUIDE_WATCH_PW_VIDEO, false);
    }

    public static void guideVideoWatched() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(GUIDE_WATCH_PW_VIDEO, true);
    }

//    public static void guideRadar(){
//        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
//        mmkv.encode(GUIDE_CHECK_RADAR, true);
//    }
//
//    public static boolean doesGuideRadarWatched(){
//        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
//        return mmkv.decodeBool(GUIDE_CHECK_RADAR, false);
//    }


    public static void guideCheckBand() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(GUIDE_CHECK_BAND, true);
    }

    public static boolean doesGuideCheckBandWatched() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(GUIDE_CHECK_BAND, false);
    }

    public static void guideCheckFriendData() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(GUIDE_CHECK_FRIEND_DATA, true);
    }

    public static boolean doesGuideCheckFriendDataWatched() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(GUIDE_CHECK_FRIEND_DATA, false);
    }

    public static void guideSetNotification() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(GUIDE_SET_NOTIFICATION, true);
    }

    public static boolean doesGuideSetNotificationWatched() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(GUIDE_SET_NOTIFICATION, false);
    }

    public static void setNeverShowWearErrorDialog() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(NEVER_SHOW_MEASURE_PW_TIMEOUT_DIALOG, true);
    }

    public static boolean doesNeverShowWearErrorDialog() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(NEVER_SHOW_MEASURE_PW_TIMEOUT_DIALOG, false);
    }

    public static void saveServiceAuthCode(String code) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_SERVICE_AUTHOR_CODE, code);
    }

    public static String getServiceAuthCode() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeString(PREF_SERVICE_AUTHOR_CODE, "");
    }

    public static void setEmergencyHide(boolean hide) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_EMERGENCY_HIDE, hide);
    }

    public static boolean getEmergencyHide() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(PREF_EMERGENCY_HIDE, false);
    }

    public static int getEmergencyYPosition() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeInt(PREF_EMERGENCY_Y_POSITION, -1);
    }

    public static int getEmergencyXPosition() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeInt(PREF_EMERGENCY_X_POSITION, -1);
    }

    public static void setEmergencyPosition(int x, int y) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_EMERGENCY_X_POSITION, x);
        mmkv.encode(PREF_EMERGENCY_Y_POSITION, y);
    }

    public static void setProtocolSigned(boolean signed) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_PROTOCOL_SIGNED, signed);
    }

    public static boolean isProtocolSigned() {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(PREF_PROTOCOL_SIGNED);
    }

    public static void setFirstAidPhone(String[] phones) {
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        if (phones == null || phones.length == 0){
            mmkv.removeValueForKey(PREF_FIRST_AID_PHONES);
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (String phone : phones) {
            builder.append(phone).append(",");
        }
        if (builder.length() != 0){
            builder.deleteCharAt(builder.length()-1);
        }
        mmkv.encode(PREF_FIRST_AID_PHONES, builder.toString());
    }

    public static String[] getFirstAidPhone(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        String phoneString = mmkv.decodeString(PREF_FIRST_AID_PHONES,"");
        if (TextUtils.isEmpty(phoneString)){
            return new String[]{};
        }else{
            return phoneString.split(",");
        }
    }

    public static void setFirstAidBought(boolean isBought){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_FIRST_AID_BOUGHT, isBought);
    }

    public static boolean getFirstAidBought(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(PREF_FIRST_AID_BOUGHT);
    }

    public static void setAllowCustomerContact(boolean allow){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_ALLOW_CUSTOMER_CONTACT_BY_PHONE, allow);
        mmkv.encode(PREF_ALLOW_CUSTOMER_CONTACT_BY_WECHAT, allow);
    }

    public static boolean doesAllowCustomerServiceContactByPhone(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(PREF_ALLOW_CUSTOMER_CONTACT_BY_PHONE);
    }

    public static boolean doesAllowCustomerServiceContactByWechat(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(PREF_ALLOW_CUSTOMER_CONTACT_BY_WECHAT);
    }

    public static boolean getPermissionAccessStorage(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(PREF_PERMISSION_ACCESS_STORAGE);
    }

    public static boolean getPermissionReadContact(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(PREF_PERMISSION_READ_CONTACTS);
    }

    public static boolean getPermissionUseCamera(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeBool(PREF_PERMISSION_CAMERA);
    }

    public static void setPermissionAccessStorage(boolean allow){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_PERMISSION_ACCESS_STORAGE,allow);
    }

    public static void setPermissionUseCamera(boolean allow){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_PERMISSION_CAMERA,allow);
    }

    public static void setPermissionReadContact(boolean allow){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_PERMISSION_READ_CONTACTS,allow);
    }

    public static void saveLastPwData(long time){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_LAST_PW_TIME,time);
    }

    public static void saveLastEcgData(long time){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        mmkv.encode(PREF_LAST_ECG_TIME,time);
    }

    public static long getLastPwTime(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeLong(PREF_LAST_PW_TIME);
    }

    public static long getLastEcgTime(){
        MMKV mmkv = MMKV.mmkvWithID(MMKV_ID);
        return mmkv.decodeLong(PREF_LAST_ECG_TIME);
    }
}
