package com.mgtech.domain.entity.net.response;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.Utils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhaixiang on 2017/1/12.
 * 按时间获取数据
 */

public class GetCalculateDataResponseEntity extends ResponseEntity implements Comparable<GetCalculateDataResponseEntity>{
    @Expose
    @SerializedName(NetConstant.JSON_TARGET_USER_GUID)
    private String targetUser;
    @Expose
    @SerializedName(NetConstant.JSON_REQUEST_DATE)
    private String requestDate;
    @Expose
    @SerializedName(NetConstant.JSON_ANALYZE_INFO)
    private String analyzeInfo;
    @SerializedName(NetConstant.JSON_IS_SPORT)
    @Expose
    private String isSport = "0";
    @SerializedName(NetConstant.JSON_ROW_GUID)
    @Expose
    private String rowGuid;
    @Expose(deserialize = false)
    private ArrayList<IndicatorItemResponseEntity>data;
    @Expose(deserialize = false)
    private Calendar date;
    @Expose(deserialize = false)
    private static final SimpleDateFormat formate = new SimpleDateFormat(MyConstant.DISPLAY_DATE_5, Locale.getDefault());

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getAnalyzeInfo() {
        return analyzeInfo;
    }

    public void setAnalyzeInfo(String analyzeInfo) {
        this.analyzeInfo = analyzeInfo;
    }

    public String getRowGuid() {
        return rowGuid;
    }

    public void setRowGuid(String rowGuid) {
        this.rowGuid = rowGuid;
    }

    public String getIsSport() {
        return isSport;
    }

    public void setIsSport(String isSport) {
        this.isSport = isSport;
    }

    /**
     * 获取数据
     * @return ResultEntity<IndicatorItemResponseEntity>
     */
    public ArrayList<IndicatorItemResponseEntity> getIndicator(){
//        if (data != null){
//            return data;
//        }
        if (analyzeInfo != null){
            Type type = new TypeToken<List<IndicatorItemResponseEntity>>() {
            }.getType();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();
//            return gsonBuilder.create().fromJson(analyzeInfo.replaceAll("\\\\","").replaceAll("\\*",""), type);
            data = gsonBuilder.create().fromJson(analyzeInfo, type);
//            return new Gson().fromJson(analyzeInfo.replaceAll("\\\\","").replaceAll("\\*",""), type);
//            return ResponseEntityMapper.mapperToIndicatorEntities(analyzeInfo.replaceAll("\\\\","").replaceAll("\\*",""));
        }
        return data;
    }

    public IndicatorItemResponseEntity getItemValue(@MyConstant.IndicatorType int type){
        if (data == null){
            getIndicator();
        }
        if (data == null){
            return null;
        }
        IndicatorItemResponseEntity item;
        switch (type){
            case MyConstant.RANK_HR:
                item = scanEntity("RANK_HR");
                break;
            case MyConstant.RANK_AC:
                item = scanEntity("RANK_AC");
                break;
            case MyConstant.RANK_PD:
                item = scanEntity("RANK_PD");
                break;
            case MyConstant.RANK_PS:
                item = scanEntity("RANK_PS");
                break;
            case MyConstant.RANK_PM:
                item = scanEntity("RANK_PM");
                break;
            case MyConstant.RANK_TM:
                item = scanEntity("RANK_TM");
                break;
            case MyConstant.RANK_TPR:
                item = scanEntity("RANK_TPR");
                break;
            case MyConstant.RANK_CO:
                item = scanEntity("RANK_CO");
                break;
            case MyConstant.RANK_V0:
                item = scanEntity("RANK_V0");
                break;
            case MyConstant.RANK_SI:
                item = scanEntity("RANK_SI");
                break;
            case MyConstant.RANK_SV:
                item = scanEntity("RANK_SV");
                break;
            case MyConstant.RANK_ALK:
                item = scanEntity("RANK_ALK");
                break;
            case MyConstant.RANK_ALT:
                item = scanEntity("RANK_ALT");
                break;
            case MyConstant.RANK_K:
                item = scanEntity("RANK_K");
                break;
            case MyConstant.RANK_BV:
                item = scanEntity("RANK_BV");
                break;
            default:
                return null;
        }
        return item;
    }

    public Calendar getDate(){
        if (date != null){
            return date;
        }
//        date = Calendar.getInstance();
////        date = SavePreferenceUtils.UTCCalendarToLocal(requestDate);
//        try {
//            date.setTime(format.parse(requestDate));
//            date.add(Calendar.HOUR,SavePreferenceUtils.getTimeZone());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        date = Utils.utcCalendarToLocal(requestDate);
        return date;
    }

    private IndicatorItemResponseEntity scanEntity(String type){
        if (data == null){
            return null;
        }
        for (IndicatorItemResponseEntity entity : data) {
            if (type.equals(entity.getName())){
                return entity;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "GetCalculateDataResponseEntity{" +
                "targetUser='" + targetUser + '\'' +
                ", requestDate='" + requestDate + '\'' +
                ", analyzeInfo='" + analyzeInfo + '\'' +
                ", isSport='" + isSport + '\'' +
                ", rowGuid='" + rowGuid + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull GetCalculateDataResponseEntity o) {
        return this.requestDate.compareTo(o.requestDate);
    }
}
