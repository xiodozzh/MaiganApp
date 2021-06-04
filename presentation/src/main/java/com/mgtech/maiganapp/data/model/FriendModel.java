package com.mgtech.maiganapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.mgtech.domain.utils.MyConstant;

import java.util.Objects;

/**
 * @author zhaixiang
 */
public class FriendModel implements Parcelable {
    public static final int MONITOR = 0;
    public static final int MONITORED = 1;

    private String relationId;
    private String avatarUrl;
    private String name;
    private String noteName;
    private String targetUserId;
    private float hr;
    private float ps;
    private float pd;
    private int measureNumber;
    private long lastMeasureTime;
    private int relationType;
    private boolean readHisData;
    private boolean getHisPushData;
    private boolean allowHimReadData;
    private boolean allowPushData;
    private boolean haveData;
    private String phone;
    private int sex;
    private int pwUnreadNumber;
    private int abnormalUnreadNumber;

    public FriendModel() {
    }


    protected FriendModel(Parcel in) {
        relationId = in.readString();
        avatarUrl = in.readString();
        name = in.readString();
        noteName = in.readString();
        targetUserId = in.readString();
        hr = in.readFloat();
        ps = in.readFloat();
        pd = in.readFloat();
        measureNumber = in.readInt();
        lastMeasureTime = in.readLong();
        relationType = in.readInt();
        readHisData = in.readByte() != 0;
        getHisPushData = in.readByte() != 0;
        allowHimReadData = in.readByte() != 0;
        allowPushData = in.readByte() != 0;
        haveData = in.readByte() != 0;
        phone = in.readString();
        sex = in.readInt();
        pwUnreadNumber = in.readInt();
        abnormalUnreadNumber = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(relationId);
        dest.writeString(avatarUrl);
        dest.writeString(name);
        dest.writeString(noteName);
        dest.writeString(targetUserId);
        dest.writeFloat(hr);
        dest.writeFloat(ps);
        dest.writeFloat(pd);
        dest.writeInt(measureNumber);
        dest.writeLong(lastMeasureTime);
        dest.writeInt(relationType);
        dest.writeByte((byte) (readHisData ? 1 : 0));
        dest.writeByte((byte) (getHisPushData ? 1 : 0));
        dest.writeByte((byte) (allowHimReadData ? 1 : 0));
        dest.writeByte((byte) (allowPushData ? 1 : 0));
        dest.writeByte((byte) (haveData ? 1 : 0));
        dest.writeString(phone);
        dest.writeInt(sex);
        dest.writeInt(pwUnreadNumber);
        dest.writeInt(abnormalUnreadNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FriendModel> CREATOR = new Creator<FriendModel>() {
        @Override
        public FriendModel createFromParcel(Parcel in) {
            return new FriendModel(in);
        }

        @Override
        public FriendModel[] newArray(int size) {
            return new FriendModel[size];
        }
    };

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public float getHr() {
        return hr;
    }

    public void setHr(float hr) {
        this.hr = hr;
    }

    public float getPs() {
        return ps;
    }

    public void setPs(float ps) {
        this.ps = ps;
    }

    public float getPd() {
        return pd;
    }

    public void setPd(float pd) {
        this.pd = pd;
    }

    public int getMeasureNumber() {
        return measureNumber;
    }

    public void setMeasureNumber(int measureNumber) {
        this.measureNumber = measureNumber;
    }

    public long getLastMeasureTime() {
        return lastMeasureTime;
    }

    public void setLastMeasureTime(long lastMeasureTime) {
        this.lastMeasureTime = lastMeasureTime;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public int getPwUnreadNumber() {
        return pwUnreadNumber;
    }

    public void setPwUnreadNumber(int pwUnreadNumber) {
        this.pwUnreadNumber = pwUnreadNumber;
    }

    public int getAbnormalUnreadNumber() {
        return abnormalUnreadNumber;
    }

    public void setAbnormalUnreadNumber(int abnormalUnreadNumber) {
        this.abnormalUnreadNumber = abnormalUnreadNumber;
    }

    public boolean isReadHisData() {
        return readHisData;
    }

    public void setReadHisData(boolean readHisData) {
        this.readHisData = readHisData;
    }

    public boolean isGetHisPushData() {
        return getHisPushData;
    }

    public void setGetHisPushData(boolean getHisPushData) {
        this.getHisPushData = getHisPushData;
    }

    public boolean isHaveData() {
        return haveData;
    }

    public void setHaveData(boolean haveData) {
        this.haveData = haveData;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public boolean isAllowHimReadData() {
        return allowHimReadData;
    }

    public void setAllowHimReadData(boolean allowHimReadData) {
        this.allowHimReadData = allowHimReadData;
    }

    public boolean isAllowPushData() {
        return allowPushData;
    }

    public void setAllowPushData(boolean allowPushData) {
        this.allowPushData = allowPushData;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public boolean isMan(){
        return sex == MyConstant.MAN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendModel that = (FriendModel) o;
        return Float.compare(that.hr, hr) == 0 &&
                Float.compare(that.ps, ps) == 0 &&
                Float.compare(that.pd, pd) == 0 &&
                measureNumber == that.measureNumber &&
                lastMeasureTime == that.lastMeasureTime &&
                relationType == that.relationType &&
                readHisData == that.readHisData &&
                getHisPushData == that.getHisPushData &&
                allowHimReadData == that.allowHimReadData &&
                allowPushData == that.allowPushData &&
                haveData == that.haveData &&
                sex == that.sex &&
                pwUnreadNumber == that.pwUnreadNumber &&
                abnormalUnreadNumber == that.abnormalUnreadNumber &&
                Objects.equals(relationId, that.relationId) &&
                Objects.equals(avatarUrl, that.avatarUrl) &&
                Objects.equals(name, that.name) &&
                Objects.equals(noteName, that.noteName) &&
                Objects.equals(targetUserId, that.targetUserId) &&
                Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relationId, avatarUrl, name, noteName, targetUserId, hr, ps, pd, measureNumber, lastMeasureTime, relationType, readHisData, getHisPushData, allowHimReadData, allowPushData, haveData, phone, sex, pwUnreadNumber, abnormalUnreadNumber);
    }

    @Override
    public String toString() {
        return "FriendModel{" +
                "relationId='" + relationId + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", name='" + name + '\'' +
                ", noteName='" + noteName + '\'' +
                ", targetUserId='" + targetUserId + '\'' +
                ", hr=" + hr +
                ", ps=" + ps +
                ", pd=" + pd +
                ", measureNumber=" + measureNumber +
                ", lastMeasureTime=" + lastMeasureTime +
                ", relationType=" + relationType +
                ", readHisData=" + readHisData +
                ", getHisPushData=" + getHisPushData +
                ", allowHimReadData=" + allowHimReadData +
                ", allowPushData=" + allowPushData +
                ", haveData=" + haveData +
                ", phone='" + phone + '\'' +
                ", sex=" + sex +
                ", pwUnreadNumber=" + pwUnreadNumber +
                ", abnormalUnreadNumber=" + abnormalUnreadNumber +
                '}';
    }
}
