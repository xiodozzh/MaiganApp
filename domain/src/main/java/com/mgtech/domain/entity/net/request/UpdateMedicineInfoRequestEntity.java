//package com.mgtech.domain.entity.net.request;
//
//import com.google.gson.Gson;
//import com.google.gson.annotations.SerializedName;
//import com.mgtech.domain.entity.net.response.MedicineResponseEntity;
//import com.mgtech.domain.utils.HttpApi;
//import com.mgtech.domain.utils.NetConstant;
//import com.mgtech.domain.entity.net.RequestEntity;
//
///**
// * Created by zhaixiang on 2017/3/2.
// * 修改用药信息
// */
//
//public class UpdateMedicineInfoRequestEntity implements RequestEntity{
//    @SerializedName(NetConstant.JSON_ROW_GUID)
//    private String rowId = "";
//    @SerializedName(NetConstant.JSON_USER_ID)
//    private String userId = "";
//    @SerializedName(NetConstant.JSON_MEDICINE_NAME)
//    private String medicineName = "";
//    @SerializedName(NetConstant.JSON_DOSAGE)
//    private String dosage = "";
//    @SerializedName(NetConstant.JSON_DAILY_COUNT)
//    private String dailyCount = "";
//    @SerializedName(NetConstant.JSON_USE_METHOD)
//    private String useMethod = "";
//    @SerializedName(NetConstant.JSON_START_TIME)
//    private String startTime = "";
//    @SerializedName(NetConstant.JSON_END_TIME)
//    private String endTime = "";
//    @SerializedName(NetConstant.JSON_USE_FOR)
//    private String usedFor = "";
//    @SerializedName(NetConstant.JSON_EXPLAIN)
//    private String explain = "";
//    @SerializedName(NetConstant.JSON_IS_USE)
//    private String isUse = "";
//    @SerializedName(NetConstant.JSON_REMINDER_TIME)
//    private String reminderTime = "";
//
//    public UpdateMedicineInfoRequestEntity(String rowId, String userId, String medicineName,
//                                           String dosage, String dailyCount, String useMethod,
//                                           String startTime, String endTime, String usedFor, String explain,
//                                           String isUse, String reminderTime) {
//        this.rowId = rowId;
//        this.userId = userId;
//        this.medicineName = medicineName;
//        this.dosage = dosage;
//        this.dailyCount = dailyCount;
//        this.useMethod = useMethod;
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.usedFor = usedFor;
//        this.explain = explain;
//        this.isUse = isUse;
//        this.reminderTime = reminderTime;
//    }
//
//    public UpdateMedicineInfoRequestEntity(MedicineResponseEntity entity){
//        this.rowId = entity.getRowId();
//        this.userId = entity.getId();
//        this.medicineName = entity.getMedicineName();
//        this.dosage = entity.getDosage();
//        this.dailyCount = entity.getDailyCount();
//        this.useMethod = entity.getUseMethod();
//        this.startTime = entity.getStartTime();
//        this.endTime = entity.getEndTime();
//        this.usedFor = entity.getUsedFor();
//        this.explain = entity.getExplain();
//        this.isUse = entity.getIsUse();
//        this.reminderTime = entity.getReminderTime();
//    }
//
//    public String getRowId() {
//        return rowId;
//    }
//
//    public void setRowId(String rowId) {
//        this.rowId = rowId;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getMedicineName() {
//        return medicineName;
//    }
//
//    public void setMedicineName(String medicineName) {
//        this.medicineName = medicineName;
//    }
//
//    public String getDosage() {
//        return dosage;
//    }
//
//    public void setDosage(String dosage) {
//        this.dosage = dosage;
//    }
//
//    public String getDailyCount() {
//        return dailyCount;
//    }
//
//    public void setDailyCount(String dailyCount) {
//        this.dailyCount = dailyCount;
//    }
//
//    public String getUseMethod() {
//        return useMethod;
//    }
//
//    public void setUseMethod(String useMethod) {
//        this.useMethod = useMethod;
//    }
//
//    public String getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(String startTime) {
//        this.startTime = startTime;
//    }
//
//    public String getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(String endTime) {
//        this.endTime = endTime;
//    }
//
//    public String getUsedFor() {
//        return usedFor;
//    }
//
//    public void setUsedFor(String usedFor) {
//        this.usedFor = usedFor;
//    }
//
//    public String getExplain() {
//        return explain;
//    }
//
//    public void setExplain(String explain) {
//        this.explain = explain;
//    }
//
//    public String getIsUse() {
//        return isUse;
//    }
//
//    public void setIsUse(String isUse) {
//        this.isUse = isUse;
//    }
//
//    public String getReminderTime() {
//        return reminderTime;
//    }
//
//    public void setReminderTime(String reminderTime) {
//        this.reminderTime = reminderTime;
//    }
//
//    @Override
//    public String toString() {
//        return "UpdateMedicineInfoRequestEntity{" +
//                "rowId='" + rowId + '\'' +
//                ", userId='" + userId + '\'' +
//                ", medicineName='" + medicineName + '\'' +
//                ", dosage='" + dosage + '\'' +
//                ", dailyCount='" + dailyCount + '\'' +
//                ", useMethod='" + useMethod + '\'' +
//                ", startTime='" + startTime + '\'' +
//                ", endTime='" + endTime + '\'' +
//                ", usedFor='" + usedFor + '\'' +
//                ", explain='" + explain + '\'' +
//                ", isUse='" + isUse + '\'' +
//                ", reminderTime='" + reminderTime + '\'' +
//                '}';
//    }
//
//    @Override
//    public String getUrl() {
//        return HttpApi.API_UPDATE_MEDICINE_INFO;
//    }
//
//    @Override
//    public String getBody() {
//        return new Gson().toJson(this);
//    }
//}
