package com.mgtech.domain.entity.net.response;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

/**
 *
 * @author zhaixiang
 * 计算数据
 */

public class IndicatorItemResponseEntity implements Parcelable{
    @Expose
    @SerializedName(NetConstant.JSON_RESULT_NAME)
    private String name;
    @Expose
    @SerializedName(NetConstant.JSON_RESULT_VALUE)
    private String value;
    @Expose
    @SerializedName(NetConstant.JSON_RESULT_UNIT)
    private String unit;
    @Expose
    @SerializedName(NetConstant.JSON_RESULT_DESCRIBE)
    private int resultDescribe;
    @Expose
    @SerializedName(NetConstant.JSON_REQUEST_DATE)
    private String date;

    protected IndicatorItemResponseEntity(Parcel in) {
        name = in.readString();
        value = in.readString();
        unit = in.readString();
        resultDescribe = in.readInt();
        date = in.readString();
    }

    public static final Creator<IndicatorItemResponseEntity> CREATOR = new Creator<IndicatorItemResponseEntity>() {
        @Override
        public IndicatorItemResponseEntity createFromParcel(Parcel in) {
            return new IndicatorItemResponseEntity(in);
        }

        @Override
        public IndicatorItemResponseEntity[] newArray(int size) {
            return new IndicatorItemResponseEntity[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        try{
            return Float.parseFloat(value);
        }catch (Exception e){
            return 0;
        }
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public int getResultDescribe() {
        return resultDescribe;
    }

    public void setResultDescribe(int resultDescribe) {
        this.resultDescribe = resultDescribe;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "IndicatorItemResponseEntity{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", unit='" + unit + '\'' +
                ", resultDescribe='" + resultDescribe + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(value);
        dest.writeString(unit);
        dest.writeInt(resultDescribe);
        dest.writeString(date);
    }
}
