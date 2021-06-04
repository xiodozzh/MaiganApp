package com.mgtech.maiganapp.data.model;

import android.text.format.DateFormat;

import com.mgtech.domain.utils.MyConstant;

import java.util.Objects;

/**
 * 数据
 * @author zhaixiang
 */
public class IndicatorDataModel {
    public long time;
    public String id;
    public float hr;
    public float ps;
    public float pd;
    public float pm;
    public float v0;
    public float co;
    public float tpr;

    public int hrLevel;
    public int psLevel;
    public int pdLevel;
    public int pmLevel;
    public int v0Level;
    public int coLevel;
    public int tprLevel;

    public String hrUnit;
    public String psUnit;
    public String pdUnit;
    public String pmUnit;
    public String v0Unit;
    public String coUnit;
    public String tprUnit;

    public int hrScore;
    public int psScore;
    public int pdScore;
    public int pmScore;
    public int v0Score;
    public int coScore;
    public int tprScore;

    public boolean achieveGoal;
    public String remark;
    public boolean isAuto;
    public String rawDataFileId;

    @Override
    public String toString() {
        return "IndicatorDataModel{" +
                "time=" + DateFormat.format(MyConstant.FULL_DATETIME_FORMAT,time) +
                ", ps=" + ps +
                ", pd=" + pd +
                ", hr=" + hr +
                ", remark=" + remark +
                ", achieveGoal=" + achieveGoal +
                ", isAuto=" + isAuto +
                ", rawDataFileId=" + rawDataFileId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndicatorDataModel model = (IndicatorDataModel) o;
        return time == model.time &&
                Float.compare(model.hr, hr) == 0 &&
                Float.compare(model.ps, ps) == 0 &&
                Float.compare(model.pd, pd) == 0 &&
                Float.compare(model.pm, pm) == 0 &&
                Float.compare(model.v0, v0) == 0 &&
                Float.compare(model.co, co) == 0 &&
                Float.compare(model.tpr, tpr) == 0 &&
                hrLevel == model.hrLevel &&
                psLevel == model.psLevel &&
                pdLevel == model.pdLevel &&
                pmLevel == model.pmLevel &&
                v0Level == model.v0Level &&
                coLevel == model.coLevel &&
                tprLevel == model.tprLevel &&
                hrScore == model.hrScore &&
                psScore == model.psScore &&
                pdScore == model.pdScore &&
                pmScore == model.pmScore &&
                v0Score == model.v0Score &&
                coScore == model.coScore &&
                tprScore == model.tprScore &&
                achieveGoal == model.achieveGoal &&
                Objects.equals(id, model.id) &&
                Objects.equals(hrUnit, model.hrUnit) &&
                Objects.equals(psUnit, model.psUnit) &&
                Objects.equals(pdUnit, model.pdUnit) &&
                Objects.equals(pmUnit, model.pmUnit) &&
                Objects.equals(v0Unit, model.v0Unit) &&
                Objects.equals(coUnit, model.coUnit) &&
                Objects.equals(tprUnit, model.tprUnit) &&
                Objects.equals(remark,model.remark) &&
                Objects.equals(rawDataFileId,model.rawDataFileId) &&
                isAuto == model.isAuto;
    }

    @Override
    public int hashCode() {

        return Objects.hash(time, id, hr, ps, pd, pm, v0, co, tpr, hrLevel, psLevel, pdLevel, pmLevel,
                v0Level, coLevel, tprLevel, hrUnit, psUnit, pdUnit, pmUnit, v0Unit, coUnit, tprUnit, hrScore,
                psScore, pdScore, pmScore, v0Score, coScore, tprScore, achieveGoal,remark,isAuto,rawDataFileId);
    }
}
