package com.mgtech.domain.entity;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

/**
 * Created by zhaixiang on 2017/2/7.
 * 纯数据的计算结果
 */

public class CleanIndicator implements Parcelable,Comparable<CleanIndicator>{
    private String result = "";
    private String message = "";
    private String id;
    private long time;
    private int isSport;
    private float hr;
    private float ps;
    private float pd;
    private float pm;
    private float sv;
    private float si;
    private float co;
    private float tpr;
    private float ac;
    private float alk;
    private float alt;
    private float tm;
    private float bv;
    private float v0;
    private float k;

    private int hrJudge;
    private int psJudge;
    private int pdJudge;
    private int pmJudge;
    private int svJudge;
    private int siJudge;
    private int coJudge;
    private int tprJudge;
    private int acJudge;
    private int alkJudge;
    private int altJudge;
    private int tmJudge;
    private int bvJudge;
    private int v0Judge;
    private int kJudge;

    public CleanIndicator(){}

    protected CleanIndicator(Parcel in) {
        result = in.readString();
        message = in.readString();
        id = in.readString();
        time = in.readLong();
        isSport = in.readInt();
        hr = in.readFloat();
        ps = in.readFloat();
        pd = in.readFloat();
        pm = in.readFloat();
        sv = in.readFloat();
        si = in.readFloat();
        co = in.readFloat();
        tpr = in.readFloat();
        ac = in.readFloat();
        alk = in.readFloat();
        alt = in.readFloat();
        tm = in.readFloat();
        bv = in.readFloat();
        v0 = in.readFloat();
        k = in.readFloat();
        hrJudge = in.readInt();
        psJudge = in.readInt();
        pdJudge = in.readInt();
        pmJudge = in.readInt();
        svJudge = in.readInt();
        siJudge = in.readInt();
        coJudge = in.readInt();
        tprJudge = in.readInt();
        acJudge = in.readInt();
        alkJudge = in.readInt();
        altJudge = in.readInt();
        tmJudge = in.readInt();
        bvJudge = in.readInt();
        v0Judge = in.readInt();
        kJudge = in.readInt();
    }

    public static final Creator<CleanIndicator> CREATOR = new Creator<CleanIndicator>() {
        @Override
        public CleanIndicator createFromParcel(Parcel in) {
            return new CleanIndicator(in);
        }

        @Override
        public CleanIndicator[] newArray(int size) {
            return new CleanIndicator[size];
        }
    };

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void set(CleanIndicator indicator){
        id = indicator.getId();
        time = indicator.getTime();
        isSport = indicator.getIsSport();
        hr = indicator.getHr();
        ps = indicator.getPs();
        pd = indicator.getPd();
        pm = indicator.getPm();
        sv = indicator.getSv();
        si = indicator.getSi();
        co = indicator.getCo();
        tpr = indicator.getTpr();
        ac = indicator.getAc();
        alk = indicator.getAlk();
        alt = indicator.getAlt();
        tm = indicator.getTm();
        bv = indicator.getBv();
        v0 = indicator.getV0();
        k = indicator.getK();

        hrJudge = indicator.getHrJudge();
        psJudge = indicator.getPsJudge();
        pdJudge = indicator.getPdJudge();
        pmJudge = indicator.getPmJudge();
        svJudge = indicator.getSvJudge();
        siJudge = indicator.getSiJudge();
        coJudge = indicator.getCoJudge();
        tprJudge = indicator.getTprJudge();
        acJudge = indicator.getAcJudge();
        alkJudge = indicator.getAlkJudge();
        altJudge = indicator.getAltJudge();
        tmJudge = indicator.getTmJudge();
        bvJudge = indicator.getBvJudge();
        v0Judge = indicator.getV0Judge();
        kJudge = indicator.getkJudge();
    }



    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public float getPm() {
        return pm;
    }

    public void setPm(float pm) {
        this.pm = pm;
    }

    public float getSv() {
        return sv;
    }

    public void setSv(float sv) {
        this.sv = sv;
    }

    public float getSi() {
        return si;
    }

    public void setSi(float si) {
        this.si = si;
    }

    public float getCo() {
        return co;
    }

    public void setCo(float co) {
        this.co = co;
    }

    public float getTpr() {
        return tpr;
    }

    public int getIsSport() {
        return isSport;
    }

    public void setIsSport(int isSport) {
        this.isSport = isSport;
    }

    public void setTpr(float tpr) {
        this.tpr = tpr;
    }

    public float getAc() {
        return ac;
    }

    public void setAc(float ac) {
        this.ac = ac;
    }

    public float getAlk() {
        return alk;
    }

    public void setAlk(float alk) {
        this.alk = alk;
    }

    public float getAlt() {
        return alt;
    }

    public void setAlt(float alt) {
        this.alt = alt;
    }

    public float getTm() {
        return tm;
    }

    public void setTm(float tm) {
        this.tm = tm;
    }

    public float getBv() {
        return bv;
    }

    public void setBv(float bv) {
        this.bv = bv;
    }

    public float getV0() {
        return v0;
    }

    public void setV0(float v0) {
        this.v0 = v0;
    }

    public float getK() {
        return k;
    }

    public void setK(float k) {
        this.k = k;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHrJudge() {
        return hrJudge;
    }

    public void setHrJudge(int hrJudge) {
        this.hrJudge = hrJudge;
    }

    public int getPsJudge() {
        return psJudge;
    }

    public void setPsJudge(int psJudge) {
        this.psJudge = psJudge;
    }

    public int getPdJudge() {
        return pdJudge;
    }

    public void setPdJudge(int pdJudge) {
        this.pdJudge = pdJudge;
    }

    public int getPmJudge() {
        return pmJudge;
    }

    public void setPmJudge(int pmJudge) {
        this.pmJudge = pmJudge;
    }

    public int getSvJudge() {
        return svJudge;
    }

    public void setSvJudge(int svJudge) {
        this.svJudge = svJudge;
    }

    public int getSiJudge() {
        return siJudge;
    }

    public void setSiJudge(int siJudge) {
        this.siJudge = siJudge;
    }

    public int getCoJudge() {
        return coJudge;
    }

    public void setCoJudge(int coJudge) {
        this.coJudge = coJudge;
    }

    public int getTprJudge() {
        return tprJudge;
    }

    public void setTprJudge(int tprJudge) {
        this.tprJudge = tprJudge;
    }

    public int getAcJudge() {
        return acJudge;
    }

    public void setAcJudge(int acJudge) {
        this.acJudge = acJudge;
    }

    public int getAlkJudge() {
        return alkJudge;
    }

    public void setAlkJudge(int alkJudge) {
        this.alkJudge = alkJudge;
    }

    public int getAltJudge() {
        return altJudge;
    }

    public void setAltJudge(int altJudge) {
        this.altJudge = altJudge;
    }

    public int getTmJudge() {
        return tmJudge;
    }

    public void setTmJudge(int tmJudge) {
        this.tmJudge = tmJudge;
    }

    public int getBvJudge() {
        return bvJudge;
    }

    public void setBvJudge(int bvJudge) {
        this.bvJudge = bvJudge;
    }

    public int getV0Judge() {
        return v0Judge;
    }

    public void setV0Judge(int v0Judge) {
        this.v0Judge = v0Judge;
    }

    public int getkJudge() {
        return kJudge;
    }

    public void setkJudge(int kJudge) {
        this.kJudge = kJudge;
    }

    @Override
    public String toString() {
        return "CleanIndicator{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", id='" + id + '\'' +
                ", time=" + time +
                ", isSport=" + isSport +
                ", hr=" + hr +
                ", ps=" + ps +
                ", pd=" + pd +
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
                ", hrJudge=" + hrJudge +
                ", psJudge=" + psJudge +
                ", pdJudge=" + pdJudge +
                ", pmJudge=" + pmJudge +
                ", svJudge=" + svJudge +
                ", siJudge=" + siJudge +
                ", coJudge=" + coJudge +
                ", tprJudge=" + tprJudge +
                ", acJudge=" + acJudge +
                ", alkJudge=" + alkJudge +
                ", altJudge=" + altJudge +
                ", tmJudge=" + tmJudge +
                ", bvJudge=" + bvJudge +
                ", v0Judge=" + v0Judge +
                ", kJudge=" + kJudge +
                '}';
    }

    @Override
    public int compareTo(@NonNull CleanIndicator another) {
        return (int) (time - another.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result);
        dest.writeString(message);
        dest.writeString(id);
        dest.writeLong(time);
        dest.writeInt(isSport);
        dest.writeFloat(hr);
        dest.writeFloat(ps);
        dest.writeFloat(pd);
        dest.writeFloat(pm);
        dest.writeFloat(sv);
        dest.writeFloat(si);
        dest.writeFloat(co);
        dest.writeFloat(tpr);
        dest.writeFloat(ac);
        dest.writeFloat(alk);
        dest.writeFloat(alt);
        dest.writeFloat(tm);
        dest.writeFloat(bv);
        dest.writeFloat(v0);
        dest.writeFloat(k);
        dest.writeInt(hrJudge);
        dest.writeInt(psJudge);
        dest.writeInt(pdJudge);
        dest.writeInt(pmJudge);
        dest.writeInt(svJudge);
        dest.writeInt(siJudge);
        dest.writeInt(coJudge);
        dest.writeInt(tprJudge);
        dest.writeInt(acJudge);
        dest.writeInt(alkJudge);
        dest.writeInt(altJudge);
        dest.writeInt(tmJudge);
        dest.writeInt(bvJudge);
        dest.writeInt(v0Judge);
        dest.writeInt(kJudge);
    }
}
