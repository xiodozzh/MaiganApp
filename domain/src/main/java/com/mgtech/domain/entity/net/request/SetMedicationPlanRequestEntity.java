package com.mgtech.domain.entity.net.request;

public class SetMedicationPlanRequestEntity {
    private String planId;
    private String userId;

    public SetMedicationPlanRequestEntity(String planId,String userId) {
        this.planId = planId;
        this.userId = userId;
    }

    public String getPlanId() {
        return planId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "SetMedicationPlanRequestEntity{" +
                "planId='" + planId + '\'' +
                '}';
    }
}
