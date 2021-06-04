package com.mgtech.maiganapp.data.model;

/**
 * Created by zhaixiang on 2018/3/23.
 * Eventbus 传递固件升级结果
 */

public class FirmwareUpgradeResult {
    private boolean success;

    public FirmwareUpgradeResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "FirmwareUpgradeResult{" +
                "saveSuccess=" + success +
                '}';
    }
}
