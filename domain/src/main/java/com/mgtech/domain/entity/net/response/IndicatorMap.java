package com.mgtech.domain.entity.net.response;

import android.util.Log;

import com.mgtech.domain.utils.MyConstant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by zhaixiang on 2017/1/10.
 * 计算数据
 */

public class IndicatorMap {
    private Map<String, IndicatorItemResponseEntity> map;
    private Calendar date;
    private DateFormat format;
    private String isSport;
    private String rowId;

    public IndicatorMap(List<IndicatorItemResponseEntity> entities) {
        this.map = new HashMap<>();
        this.format = new SimpleDateFormat(MyConstant.FULL_DATETIME_FORMAT, Locale.CHINA);
        this.date = Calendar.getInstance();
        if (entities != null) {
            add(entities);
        }
    }

    public IndicatorMap() {
        this.map = new HashMap<>();
        this.format = new SimpleDateFormat(MyConstant.FULL_DATETIME_FORMAT, Locale.CHINA);
        this.date = Calendar.getInstance();
    }

    public void add(IndicatorItemResponseEntity entity) {
        map.put(entity.getName(), entity);
    }

    public void add(List<IndicatorItemResponseEntity> entities) {
        for (IndicatorItemResponseEntity entity : entities) {
            map.put(entity.getName(), entity);
            try {
                if (entity.getDate() != null && !entity.getDate().isEmpty()) {
                    Log.e("test2", "add: " +  entity.getDate());
                    date.setTime(format.parse(entity.getDate().replaceAll("T"," ")));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public int size() {
        return map.size();
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(long timeInMillis) {
        this.date.setTimeInMillis(timeInMillis);
    }

    public void setDate(String timeString) {
        try {
            date.setTime(format.parse(timeString.replaceAll("T"," ")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getIsSport() {
        return isSport;
    }

    public void setIsSport(String isSport) {
        this.isSport = isSport;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public Map<String, IndicatorItemResponseEntity> getMap() {
        return map;
    }

    /**
     * 根据名字获取项目
     *
     * @param key 标题
     * @return IndicatorItemResponseEntity
     */
    public IndicatorItemResponseEntity get(String key) {
        return map.get(key);
    }

    public IndicatorItemResponseEntity getPs() {
        return map.get("RANK_PS");
    }

    public IndicatorItemResponseEntity getPd() {
        return map.get("RANK_PD");
    }

    public IndicatorItemResponseEntity getHeartRate() {
        return map.get("RANK_HR");
    }


    @Override
    public String toString() {
        return "IndicatorMap{" +
                "map=" + map +
                ", date=" + date +
                ", format=" + format +
                ", isSport='" + isSport + '\'' +
                ", rowId='" + rowId + '\'' +
                '}';
    }
}
