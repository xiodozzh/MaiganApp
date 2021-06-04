package com.mgtech.domain.entity.net.request;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.entity.net.RequestString;
import com.mgtech.domain.utils.NetConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaixiang on 2017/2/7.
 * 上传Pw数据
 */

public class SendAutoPwDataRequestEntity implements RequestString{
    private static final int ASYNC = 1;
    private String id;
    private String phone;
    private User user;
    private Data data;
    private DataAdditional additional;

    public SendAutoPwDataRequestEntity(String id,String phone, User user, Data data,DataAdditional additional) {
        this.id = id;
        this.phone = phone;
        this.user = user;
        this.data = data;
        this.additional = additional;
        modify(data,user,additional);
    }

    private void modify(Data dataEntity, User userEntity, DataAdditional dataAdditional) {
        List<Object> data = dataEntity.getData();
        if (data == null) {
            return;
        }
        List<Object> extraData = new ArrayList<>();
        extraData.add(1);
        extraData.add(0xFFFF);
        extraData.add(userEntity.getSex());
        extraData.add(userEntity.getAge());
        extraData.add(userEntity.getHeight());
        extraData.add(userEntity.getWeight());
        extraData.add(0xFFFF);
        data.addAll(0, extraData);
        dataAdditional.setTotalCount(String.valueOf(data.size()));
        dataEntity.setCount(String.valueOf(data.size()));
    }

    @Override
    public String getString() {
        Gson gson = new Gson();

        JsonArray array = new JsonArray();

        JsonObject info = new JsonObject();
        info.addProperty(NetConstant.HEADER, NetConstant.TABLE_RAW_DATA_USER);
        info.addProperty(NetConstant.FLAG, NetConstant.ACTION_INSERT);
        JsonArray infoObjArray = new JsonArray();
        JsonObject infoObj = new JsonObject();
        infoObj.addProperty(NetConstant.JSON_FREQUENCY,user.getFrequency());
        infoObj.addProperty(NetConstant.JSON_SEX,user.getSex());
        infoObj.addProperty(NetConstant.JSON_AGE,user.getAge());
        infoObj.addProperty(NetConstant.JSON_HEIGHT,user.getHeight());
        infoObj.addProperty(NetConstant.JSON_WEIGHT,user.getWeight());
        infoObjArray.add(infoObj);
        info.add(NetConstant.BODY,infoObjArray);
        info.add(NetConstant.ADDITIONAL,new JsonArray());

        JsonObject data = new JsonObject();
        JsonArray additionalArray = new JsonArray();
        JsonObject additionalObj1 = new JsonObject();
        additionalObj1.addProperty(NetConstant.JSON_USER_ID,id);
        additionalObj1.addProperty(NetConstant.JSON_PHONE,phone);
        additionalObj1.addProperty(NetConstant.JSON_ASYNC,ASYNC);
//        JsonObject additionalObj2 = new JsonObject();
//        additionalObj2.addProperty(NetConstant.JSON_MAC_ADDRESS,additional.getMac());
//        additionalObj2.addProperty(NetConstant.JSON_REQUEST_DATE,additional.getMeasureTime());
//        additionalObj2.addProperty(NetConstant.JSON_TARGET_USER_GUID,"");
//        additionalObj2.addProperty(NetConstant.JSON_TOTAL_COUNT,additional.getTotalCount());
        additionalArray.add(additionalObj1);
//        additionalArray.add(additionalObj2);
        additionalArray.add(gson.toJsonTree(additional));
        JsonArray body = new JsonArray();
        body.add(gson.toJsonTree(this.data));
        data.add(NetConstant.ADDITIONAL,additionalArray);
        data.add(NetConstant.BODY,body);
        data.addProperty(NetConstant.FLAG,NetConstant.ACTION_INSERT);
        data.addProperty(NetConstant.HEADER,NetConstant.TABLE_RAW_DATA);

        array.add(info);
        array.add(data);

        return array.toString();
    }

    public static class Data{
        @SerializedName(NetConstant.JSON_DATA_DETAIL)
        private List<Object> data;
        @SerializedName(NetConstant.JSON_COUNT)
        private String count;

        public Data(List<Object> data) {
            this.data = data;
            if (data != null) {
                this.count = String.valueOf(data.size());
            }else{
                this.count = "";
            }
        }

        public List<Object> getData() {
            return data;
        }

        public void setData(List<Object> data) {
            this.data = data;
            if (data != null) {
                this.count = String.valueOf(data.size());
            }else{
                this.count = "";
            }
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "data='" + data + '\'' +
                    ", count='" + count + '\'' +
                    '}';
        }
    }

    public static class DataAdditional{
        @SerializedName(NetConstant.JSON_REQUEST_DATE)
        private String requestDate;
        @SerializedName(NetConstant.JSON_TOTAL_COUNT)
        private String totalCount;
        @SerializedName(NetConstant.JSON_TARGET_USER_GUID)
        private String targetUserId;
        @SerializedName(NetConstant.JSON_MAC_ADDRESS)
        private String mac;

        public DataAdditional(String requestDate,  String targetUserId, String mac) {
            this.requestDate = requestDate;
            this.targetUserId = targetUserId;
            this.mac = mac;
        }

        public String getRequestDate() {
            return requestDate;
        }

        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getTargetUserId() {
            return targetUserId;
        }

        public void setTargetUserId(String targetUserId) {
            this.targetUserId = targetUserId;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        @Override
        public String toString() {
            return "DataAdditional{" +
                    "requestDate='" + requestDate + '\'' +
                    ", totalCount='" + totalCount + '\'' +
                    ", targetUserId='" + targetUserId + '\'' +
                    ", mac='" + mac + '\'' +
                    '}';
        }
    }

    public static class User{
        @SerializedName(NetConstant.JSON_FREQUENCY)
        private int frequency;
        @SerializedName(NetConstant.JSON_SEX)
        private int sex;
        @SerializedName(NetConstant.JSON_DATA_HEIGHT)
        private int height;
        @SerializedName(NetConstant.JSON_DATA_WEIGHT)
        private int weight;
        @SerializedName(NetConstant.JSON_AGE)
        private int age;

        public User(int frequency, int sex, int height, int weight, int age) {
            this.frequency = frequency;
            this.sex = sex;
            this.height = height;
            this.weight = weight;
            this.age = age;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "frequency='" + frequency + '\'' +
                    ", sex='" + sex + '\'' +
                    ", height='" + height + '\'' +
                    ", weight='" + weight + '\'' +
                    ", age='" + age + '\'' +
                    '}';
        }
    }
}
