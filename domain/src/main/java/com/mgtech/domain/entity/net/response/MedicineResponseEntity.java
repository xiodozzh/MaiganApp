package com.mgtech.domain.entity.net.response;

/**
 *
 * @author zhaixiang
 * 药品信息
 */

public class MedicineResponseEntity {

    private String drugGuid;
    private String name;
    private String metering;
    private String specs;
    private String dosage;

    public String getDrugGuid() {
        return drugGuid;
    }

    public void setDrugGuid(String drugGuid) {
        this.drugGuid = drugGuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMetering() {
        return metering;
    }

    public void setMetering(String metering) {
        this.metering = metering;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    @Override
    public String toString() {
        return "MedicineResponseEntity{" +
                "drugGuid='" + drugGuid + '\'' +
                ", name='" + name + '\'' +
                ", metering='" + metering + '\'' +
                ", specs='" + specs + '\'' +
                ", dosage='" + dosage + '\'' +
                '}';
    }
}
