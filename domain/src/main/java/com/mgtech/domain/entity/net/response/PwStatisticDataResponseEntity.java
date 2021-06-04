package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;
import com.mgtech.domain.utils.NetConstant;

import java.util.List;

/**
 * @author zhaixiang
 */
public class PwStatisticDataResponseEntity {

    /**
     * 0 : [{"measureTime":1541416522433,"value":"85.1653"}]
     * 1 : [{"measureTime":1541416522433,"value":"76.6357"}]
     * 2 : [{"measureTime":1541416522433,"value":"132.1522"}]
     * 13 : [{"measureTime":1541416522433,"value":"7.7048"}]
     * score : {"0":78,"1":82,"2":80,"13":90}
     */

    private ScoreBean score;
    private Param paramInfos;


    public ScoreBean getScore() {
        return score;
    }

    public void setScore(ScoreBean score) {
        this.score = score;
    }

    public Param getParamInfos() {
        return paramInfos;
    }

    public void setParamInfos(Param paramInfos) {
        this.paramInfos = paramInfos;
    }

    public static class Param{
        @SerializedName(NetConstant.HR+"")
        private List<ShortPwBean> hr;
        @SerializedName(NetConstant.PD+"")
        private List<ShortPwBean> pd;
        @SerializedName(NetConstant.PS+"")
        private List<ShortPwBean> ps;
        @SerializedName(NetConstant.PM+"")
        private List<ShortPwBean> pm;
        @SerializedName(NetConstant.SV+"")
        private List<ShortPwBean> sv;
        @SerializedName(NetConstant.SI+"")
        private List<ShortPwBean> si;
        @SerializedName(NetConstant.CO+"")
        private List<ShortPwBean> co;
        @SerializedName(NetConstant.TPR+"")
        private List<ShortPwBean> tpr;
        @SerializedName(NetConstant.AC+"")
        private List<ShortPwBean> ac;
        @SerializedName(NetConstant.ALK+"")
        private List<ShortPwBean> alk;
        @SerializedName(NetConstant.ALT+"")
        private List<ShortPwBean> alt;
        @SerializedName(NetConstant.TM+"")
        private List<ShortPwBean> tm;
        @SerializedName(NetConstant.BV+"")
        private List<ShortPwBean> bv;
        @SerializedName(NetConstant.V0+"")
        private List<ShortPwBean> v0;
        @SerializedName(NetConstant.K+"")
        private List<ShortPwBean> k;

        public List<ShortPwBean> getHr() {
            return hr;
        }

        public void setHr(List<ShortPwBean> hr) {
            this.hr = hr;
        }

        public List<ShortPwBean> getPd() {
            return pd;
        }

        public void setPd(List<ShortPwBean> pd) {
            this.pd = pd;
        }

        public List<ShortPwBean> getPs() {
            return ps;
        }

        public void setPs(List<ShortPwBean> ps) {
            this.ps = ps;
        }

        public List<ShortPwBean> getPm() {
            return pm;
        }

        public void setPm(List<ShortPwBean> pm) {
            this.pm = pm;
        }

        public List<ShortPwBean> getSv() {
            return sv;
        }

        public void setSv(List<ShortPwBean> sv) {
            this.sv = sv;
        }

        public List<ShortPwBean> getSi() {
            return si;
        }

        public void setSi(List<ShortPwBean> si) {
            this.si = si;
        }

        public List<ShortPwBean> getCo() {
            return co;
        }

        public void setCo(List<ShortPwBean> co) {
            this.co = co;
        }

        public List<ShortPwBean> getTpr() {
            return tpr;
        }

        public void setTpr(List<ShortPwBean> tpr) {
            this.tpr = tpr;
        }

        public List<ShortPwBean> getAc() {
            return ac;
        }

        public void setAc(List<ShortPwBean> ac) {
            this.ac = ac;
        }

        public List<ShortPwBean> getAlk() {
            return alk;
        }

        public void setAlk(List<ShortPwBean> alk) {
            this.alk = alk;
        }

        public List<ShortPwBean> getAlt() {
            return alt;
        }

        public void setAlt(List<ShortPwBean> alt) {
            this.alt = alt;
        }

        public List<ShortPwBean> getTm() {
            return tm;
        }

        public void setTm(List<ShortPwBean> tm) {
            this.tm = tm;
        }

        public List<ShortPwBean> getBv() {
            return bv;
        }

        public void setBv(List<ShortPwBean> bv) {
            this.bv = bv;
        }

        public List<ShortPwBean> getK() {
            return k;
        }

        public void setK(List<ShortPwBean> k) {
            this.k = k;
        }

        public List<ShortPwBean> getV0() {
            return v0;
        }

        public void setV0(List<ShortPwBean> v0) {
            this.v0 = v0;
        }

        @Override
        public String toString() {
            return "Param{" +
                    "hr=" + hr +
                    ", pd=" + pd +
                    ", ps=" + ps +
                    ", pm=" + pm +
                    ", sv=" + sv +
                    ", si=" + si +
                    ", co=" + co +
                    ", tpr=" + tpr +
                    ", ac=" + ac +
                    ", alk=" + alk +
                    ", alt=" + alt +
                    ", tm=" + tm +
                    ", bv=" + bv +
                    ", v0=" + v0 +
                    ", k=" + k +
                    '}';
        }
    }

    public static class ScoreBean {
        /**
         * 0 : 78
         * 1 : 82
         * 2 : 80
         * 13 : 90
         */

        @SerializedName(NetConstant.HR+"")
        private int hr;
        @SerializedName(NetConstant.PD+"")
        private int pd;
        @SerializedName(NetConstant.PS+"")
        private int ps;
        @SerializedName(NetConstant.PM+"")
        private int pm;
        @SerializedName(NetConstant.SV+"")
        private int sv;
        @SerializedName(NetConstant.SI+"")
        private int si;
        @SerializedName(NetConstant.CO+"")
        private int co;
        @SerializedName(NetConstant.TPR+"")
        private int tpr;
        @SerializedName(NetConstant.AC+"")
        private int ac;
        @SerializedName(NetConstant.ALK+"")
        private int alk;
        @SerializedName(NetConstant.ALT+"")
        private int alt;
        @SerializedName(NetConstant.TM+"")
        private int tm;
        @SerializedName(NetConstant.BV+"")
        private int bv;
        @SerializedName(NetConstant.V0+"")
        private int v0;
        @SerializedName(NetConstant.K+"")
        private int k;

        public int getHr() {
            return hr;
        }

        public void setHr(int hr) {
            this.hr = hr;
        }

        public int getPd() {
            return pd;
        }

        public void setPd(int pd) {
            this.pd = pd;
        }

        public int getPs() {
            return ps;
        }

        public void setPs(int ps) {
            this.ps = ps;
        }

        public int getV0() {
            return v0;
        }

        public void setV0(int v0) {
            this.v0 = v0;
        }

        public int getPm() {
            return pm;
        }

        public void setPm(int pm) {
            this.pm = pm;
        }

        public int getSv() {
            return sv;
        }

        public void setSv(int sv) {
            this.sv = sv;
        }

        public int getSi() {
            return si;
        }

        public void setSi(int si) {
            this.si = si;
        }

        public int getCo() {
            return co;
        }

        public void setCo(int co) {
            this.co = co;
        }

        public int getTpr() {
            return tpr;
        }

        public void setTpr(int tpr) {
            this.tpr = tpr;
        }

        public int getAc() {
            return ac;
        }

        public void setAc(int ac) {
            this.ac = ac;
        }

        public int getAlk() {
            return alk;
        }

        public void setAlk(int alk) {
            this.alk = alk;
        }

        public int getAlt() {
            return alt;
        }

        public void setAlt(int alt) {
            this.alt = alt;
        }

        public int getTm() {
            return tm;
        }

        public void setTm(int tm) {
            this.tm = tm;
        }

        public int getBv() {
            return bv;
        }

        public void setBv(int bv) {
            this.bv = bv;
        }

        public int getK() {
            return k;
        }

        public void setK(int k) {
            this.k = k;
        }

        @Override
        public String toString() {
            return "ScoreBean{" +
                    "hr=" + hr +
                    ", pd=" + pd +
                    ", ps=" + ps +
                    ", pm=" + pm +
                    ", sv=" + sv +
                    ", si=" + si +
                    ", co=" + co +
                    ", tpr=" + tpr +
                    ", ac=" + ac +
                    ", alk=" + alk +
                    ", alt=" + alt +
                    ", tm=" + tm +
                    ", bv=" + bv +
                    ", v0=" + v0 +
                    ", k=" + k +
                    '}';
        }
    }

    public static class ShortPwBean {
        /**
         * measureTime : 1541416522433
         * value : 85.1653
         */

//        private long measureTime;
        private long date;
        private float value;

//        public long getMeasureTime() {
//            return measureTime;
//        }
//
//        public void setMeasureTime(long measureTime) {
//            this.measureTime = measureTime;
//        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "ShortPwBean{" +
                    ", date='" + date + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PwStatisticDataResponseEntity{" +
                "score=" + score +
                ", paramInfos=" + paramInfos +
                '}';
    }
}
