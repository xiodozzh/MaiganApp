//package com.mgtech.domain.entity.net.response;
//
//import com.google.gson.annotations.SerializedName;
//import com.mgtech.domain.utils.NetConstant;
//
//import java.util.Arrays;
//
///**
// * Created by zhaixiang on 2017/12/14.
// * 首页数据
// */
//
//public class HomeDataResponseEntity {
//    @SerializedName(NetConstant.JSON_CODE)
//    private int code;
//    @SerializedName(NetConstant.JSON_MESSAGE)
//    private String message;
//    @SerializedName("LastHR")
//    private IndicatorItemResponseEntity hr;
//    @SerializedName("LastPS")
//    private IndicatorItemResponseEntity ps;
//    @SerializedName("LastPD")
//    private IndicatorItemResponseEntity pd;
//    @SerializedName("PW")
//    private PW pw;
//    @SerializedName("ECG")
//    private ECG ecg;
//    @SerializedName("Drugs")
//    private Drugs drugs;
//    @SerializedName("LastMeasureTime")
//    private String time;
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public IndicatorItemResponseEntity getHr() {
//        return hr;
//    }
//
//    public void setHr(IndicatorItemResponseEntity hr) {
//        this.hr = hr;
//    }
//
//    public IndicatorItemResponseEntity getPs() {
//        return ps;
//    }
//
//    public void setPs(IndicatorItemResponseEntity ps) {
//        this.ps = ps;
//    }
//
//    public IndicatorItemResponseEntity getPd() {
//        return pd;
//    }
//
//    public void setPd(IndicatorItemResponseEntity pd) {
//        this.pd = pd;
//    }
//
//    public PW getPw() {
//        return pw;
//    }
//
//    public void setPw(PW pw) {
//        this.pw = pw;
//    }
//
//    public ECG getEcg() {
//        return ecg;
//    }
//
//    public void setEcg(ECG ecg) {
//        this.ecg = ecg;
//    }
//
//    public Drugs getDrugs() {
//        return drugs;
//    }
//
//    public void setDrugs(Drugs drugs) {
//        this.drugs = drugs;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    @Override
//    public String toString() {
//        return "HomeDataResponseEntity{" +
//                "code=" + code +
//                ", message='" + message + '\'' +
//                ", hr=" + hr +
//                ", ps=" + ps +
//                ", pd=" + pd +
//                ", pw=" + pw +
//                ", ecg=" + ecg +
//                ", drugs=" + drugs +
//                '}';
//    }
//
//    public class Drugs{
//        @SerializedName("UsingCount")
//        private int count;
//
//        public int getCount() {
//            return count;
//        }
//
//        public void setCount(int count) {
//            this.count = count;
//        }
//
//        @Override
//        public String toString() {
//            return "Drugs{" +
//                    "count=" + count +
//                    '}';
//        }
//    }
//
//
//    public class ECG{
//        @SerializedName("Date")
//        private String date;
//        @SerializedName("Data")
//        private float[] data;
//
//        public String getDate() {
//            return date;
//        }
//
//        public void setDate(String date) {
//            this.date = date;
//        }
//
//        public float[] getData() {
//            return data;
//        }
//
//        public void setData(float[] data) {
//            this.data = data;
//        }
//
//        @Override
//        public String toString() {
//            return "ECG{" +
//                    "date='" + date + '\'' +
//                    ", data=" + Arrays.toString(data) +
//                    '}';
//        }
//    }
//
//    public class PW{
//        @SerializedName("Date")
//        private String date;
//        @SerializedName("AvgHR")
//        private float avgHr;
//        @SerializedName("AvgPS")
//        private float avgPs;
//        @SerializedName("AvgPD")
//        private float avgPd;
//        @SerializedName("PDData")
//        private ItemValue[] pdData;
//        @SerializedName("PSData")
//        private ItemValue[] psData;
//
//        public String getDate() {
//            return date;
//        }
//
//        public void setDate(String date) {
//            this.date = date;
//        }
//
//        public float getAvgHr() {
//            return avgHr;
//        }
//
//        public void setAvgHr(float avgHr) {
//            this.avgHr = avgHr;
//        }
//
//        public float getAvgPs() {
//            return avgPs;
//        }
//
//        public void setAvgPs(float avgPs) {
//            this.avgPs = avgPs;
//        }
//
//        public float getAvgPd() {
//            return avgPd;
//        }
//
//        public void setAvgPd(float avgPd) {
//            this.avgPd = avgPd;
//        }
//
//        public ItemValue[] getPdData() {
//            return pdData;
//        }
//
//        public void setPdData(ItemValue[] pdData) {
//            this.pdData = pdData;
//        }
//
//        public ItemValue[] getPsData() {
//            return psData;
//        }
//
//        public void setPsData(ItemValue[] psData) {
//            this.psData = psData;
//        }
//
//        @Override
//        public String toString() {
//            return "PW{" +
//                    "date='" + date + '\'' +
//                    ", avgHr=" + avgHr +
//                    ", avgPs=" + avgPs +
//                    ", avgPd=" + avgPd +
//                    ", pdData=" + Arrays.toString(pdData) +
//                    ", psData=" + Arrays.toString(psData) +
//                    '}';
//        }
//    }
//
//    public class ItemValue{
//        @SerializedName("Date")
//        private String date;
//        @SerializedName("Value")
//        private float value;
//
//        public String getDate() {
//            return date;
//        }
//
//        public void setDate(String date) {
//            this.date = date;
//        }
//
//        public float getValue() {
//            return value;
//        }
//
//        public void setValue(float value) {
//            this.value = value;
//        }
//
//        @Override
//        public String toString() {
//            return "ItemValue{" +
//                    "date='" + date + '\'' +
//                    ", value=" + value +
//                    '}';
//        }
//    }
//}
