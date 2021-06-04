package com.mgtech.blelib.biz;

import com.mgtech.blelib.entity.MeasureEcgData;
import com.mgtech.blelib.entity.MeasurePwData;

public interface IResponseBytes {

    void setMeasurePwData(MeasurePwData data);

    void setMeasureEcgData(MeasureEcgData data);

    void receivedData(byte[] data);

    void respondCommitPair(byte[] data);

    void respondGetPower(byte[] data);

    void respondStopManualSample(byte[] data);

    void respondManualSample(byte[] data);

    void respondAutoSampleInfo(byte[] data);

    void respondStoredSampleRet(byte[] data);

    void respondDeleteStoredSample(byte[] data);

    void respondGetSystemParam(byte[] data);

    void respondSetSystemParam(byte[] data);

    void respondStatusUpdate(byte[] data);

    void respondUnPair(byte[] data);

    void respondUpdateTime(byte[] data);

    void respondResetAutoSampleTime(byte[] data);

    void respondReExeAutoSample(byte[] data);

    void respondFotaEnd(byte[] data);

    void respondFotaData(byte[] data);

    void respondFotaInfo(byte[] data);

    void respondSetCalculateResult(byte[] data);

    void respondGetFirmwareInfo(byte[] data);

    void respondStepData(byte[] data);

    void respondHistoryStepData(byte[] data);

    void respondCalibrateStepData(byte[] data);

    void respondEnableStepUpdate(byte[] data);

    void respondLookForBand(byte[] data);

}
