package com.mgtech.domain.entity.net.response;

import java.util.List;

/**
 * @author zhaixiang
 */
public class AllDiseaseEntity {

    private List<DiseaseBean> diseaseHistory;
    private List<DiseaseBean> organDamage;
    private List<DiseaseBean> highBloodPressure;
    private List<DiseaseBean> otherRiskFactors;

    public List<DiseaseBean> getDiseaseHistory() {
        return diseaseHistory;
    }

    public void setDiseaseHistory(List<DiseaseBean> diseaseHistory) {
        this.diseaseHistory = diseaseHistory;
    }

    public List<DiseaseBean> getOrganDamage() {
        return organDamage;
    }

    public void setOrganDamage(List<DiseaseBean> organDamage) {
        this.organDamage = organDamage;
    }

    public List<DiseaseBean> getHighBloodPressure() {
        return highBloodPressure;
    }

    public void setHighBloodPressure(List<DiseaseBean> highBloodPressure) {
        this.highBloodPressure = highBloodPressure;
    }

    public List<DiseaseBean> getOtherRiskFactors() {
        return otherRiskFactors;
    }

    public void setOtherRiskFactors(List<DiseaseBean> otherRiskFactors) {
        this.otherRiskFactors = otherRiskFactors;
    }

    @Override
    public String toString() {
        return "AllDiseaseEntity{" +
                "diseaseHistory=" + diseaseHistory +
                ", organDamage=" + organDamage +
                ", highBloodPressure=" + highBloodPressure +
                '}';
    }

    public static class DiseaseBean {
        private String name;
        private String guid;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return guid;
        }

        public void setId(String id) {
            this.guid = id;
        }

        @Override
        public String toString() {
            return "DiseaseBean{" +
                    "name='" + name + '\'' +
                    ", id='" + guid + '\'' +
                    '}';
        }
    }
}
