package com.mgtech.blelib.biz;

import android.util.Log;

import com.mgtech.blelib.core.BleClient2;
import com.mgtech.blelib.entity.BroadcastData;
import com.mgtech.blelib.entity.CheckDeviceData;
import com.mgtech.blelib.entity.CurrentStepData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.FirmStateData;
import com.mgtech.blelib.entity.FirmwareInfoData;
import com.mgtech.blelib.entity.GetSystemParamResponse;
import com.mgtech.blelib.entity.HeightWeightData;
import com.mgtech.blelib.entity.ManualEcgError;
import com.mgtech.blelib.entity.ManualMeasureNewOrder;
import com.mgtech.blelib.entity.ManualPwError;
import com.mgtech.blelib.entity.ManualSampleData;
import com.mgtech.blelib.entity.MeasureEcgData;
import com.mgtech.blelib.entity.MeasurePwData;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.entity.SampleCompleteData;
import com.mgtech.blelib.entity.StepHistory;
import com.mgtech.blelib.entity.StoredSampleData;
import com.mgtech.blelib.utils.UpgradeFirmwareManager;

import java.util.Calendar;

public class BluetoothResponseBytes implements IResponseBytes {
    private static final String TAG = "ResponseBytes";
    private static final int AUTO_SAMPLE_DOUBLE_POINT_SIZE = ManualMeasureNewOrder.COMPLETE_POINTS;
    private BleState bleState;
    private BleSender bleSender;
    private BleOrder bleOrder;
    private IBraceletInfoManager manager;
    private BleClient2 bleClient;
    private MeasureEcgData measureEcgData;
    private MeasurePwData measurePwData;

    public BluetoothResponseBytes(BleState bleState,BleOrder bleOrder, BleSender bleSender, IBraceletInfoManager manager, BleClient2 bleClient) {
        this.bleOrder = bleOrder;
        this.bleState = bleState;
        this.bleSender = bleSender;
        this.manager = manager;
        this.bleClient = bleClient;
    }

    public void setMeasureEcgData(MeasureEcgData measureEcgData) {
        this.measureEcgData = measureEcgData;
    }

    public void setMeasurePwData(MeasurePwData measurePwData) {
        this.measurePwData = measurePwData;
    }

    @Override
    public void receivedData(byte[] data) {
        switch (data[0]) {
            case BluetoothConfig.CODE_SAVE_PAIR_INFO:
                // 确认绑定
                respondCommitPair(data);
                break;
            case BluetoothConfig.CODE_BATTERY_INFO:
                // 电池电量
                respondGetPower(data);
                break;
            case BluetoothConfig.CODE_STOP_MANUAL_SAMPLE:
                // 停止采样
                respondStopManualSample(data);
                break;
            case BluetoothConfig.CODE_START_MANUAL_SAMPLE:
                // 手动采谱
                respondManualSample(data);
                break;
            case BluetoothConfig.CODE_GET_AUTO_SAMPLE_INFO:
                // 自动采样信息
                respondAutoSampleInfo(data);
                break;
            case BluetoothConfig.CODE_GET_STORED_SAMPLE_RET:
                // 自动采样
                respondStoredSampleRet(data);
                break;
            case BluetoothConfig.CODE_DEL_STORED_SAMPLE_RET:
                // 删除自动采样数据
                respondDeleteStoredSample(data);
                break;
            case BluetoothConfig.CODE_GET_SYS_PARA:
                // 获得系统参数应答
                respondGetSystemParam(data);
                break;
            case BluetoothConfig.CODE_SET_SYS_PARA:
                // 设置系统参数应答
                respondSetSystemParam(data);
                break;
            case BluetoothConfig.CODE_GET_FW_INFO:
                // 获取固件信息
                respondGetFirmwareInfo(data);
                break;
            case BluetoothConfig.CODE_FOTA_INFO:
                // 固件升级信息包应答
                respondFotaInfo(data);
                break;
            case BluetoothConfig.CODE_FOTA_DATA:
                // 固件升级文件应答
                respondFotaData(data);
                break;
            case BluetoothConfig.CODE_FOTA_END:
                // 固件升级结束应答
                respondFotaEnd(data);
                break;
            case BluetoothConfig.CODE_REEXE_AUTO_SAMPLE:
                // 重新采样
                respondReExeAutoSample(data);
                break;
            case BluetoothConfig.CODE_RESET_AUTO_SAMPLE:
                // 重置自动采样时间
                respondResetAutoSampleTime(data);
                break;
            case BluetoothConfig.CODE_UPDATE_TIME:
                // 更新时间
                respondUpdateTime(data);
                break;
            case BluetoothConfig.CODE_UNBIND:
                // 解绑
                respondUnPair(data);
                break;
            case BluetoothConfig.CODE_STATUS_UPDATE:
                // 数据有更新
                respondStatusUpdate(data);
                break;
            case BluetoothConfig.CODE_SYNC_BLOOD_PRESSURE:
                // 设置血压值
                respondSetCalculateResult(data);
                break;
            case BluetoothConfig.CODE_SYNC_STEP_DATA:
                // 获取计步数据
                respondStepData(data);
                break;
            case BluetoothConfig.CODE_SYNC_STEP_HISTORY_DATA:
                // 获取历史计步数据（不包括15min内的数据）
                respondHistoryStepData(data);
                break;
            case BluetoothConfig.CODE_CALIBRATE_STEP_DATA:
                // 发送计步数据到手环
                respondCalibrateStepData(data);
                break;
            case BluetoothConfig.CODE_SYNC_STEP_DATA_CTRL:
                respondEnableStepUpdate(data);
                break;
            case BluetoothConfig.CODE_LOOK_FOR_BAND:
                respondLookForBand(data);
                break;
            default:
        }
    }

    @Override
    public void respondCommitPair(byte[] data) {
//        bleState.setWorking(false, "respondCommitPair");
        bleState.setStatus(BleState.STATUS_READY,"respondCommitPair");
        CheckDeviceData unconfirmedDeviceData = bleState.getUnconfirmedDeviceData();
        if (data.length == 3 && bleState.randomCodeSuccess(data[1]) && data[2] == 0x00 && unconfirmedDeviceData != null) {
            savePairInfo(unconfirmedDeviceData.getKey(), unconfirmedDeviceData.getVector(), unconfirmedDeviceData.getId());
            bleSender.pairSuccess();
            bleState.setUnconfirmedDeviceData(null);
        } else {
            bleSender.pairFail();
            bleClient.disconnect();
        }
    }

    /**
     * 保存配对需要的信息 密钥和配对码
     */
    private void savePairInfo(byte[] encryptKey, byte[] encryptVector, String deviceUUID) {
        BroadcastData linkedBroadcastData = bleState.getLinkedBroadcastData();
        int pairCode = linkedBroadcastData.getPairCode();
        if (pairCode == 0XFFFFFFFF) {
            pairCode = 1;
        } else {
            pairCode++;
        }
        manager.savePairInformation(linkedBroadcastData.getDeviceName(), linkedBroadcastData.getMacAddress(),
                pairCode, encryptKey,
                encryptVector, deviceUUID, Calendar.getInstance().getTimeInMillis(), linkedBroadcastData.getProtocolVersion(),
                linkedBroadcastData.getFirmwareVersion(), linkedBroadcastData.getHardwareVersion());
        linkedBroadcastData.setPairCode(pairCode);
    }

    @Override
    public void respondGetPower(byte[] data) {
//        bleState.setWorking(false, "respondGetPower");
        bleState.setStatus(BleState.STATUS_READY,"respondGetPower");
        if (data.length == 5 && bleState.randomCodeSuccess(data[1])) {
            if (data[2] == BluetoothConfig.ERROR_NONE) {
                // 电量获取成功
                Log.e(TAG, "电量获取成功");
                manager.savePower(data[4] & 0xFF, data[3] & 0xFF);
                bleSender.power();
            } else {
                Log.i(TAG, "获取电量失败 " + BluetoothConfig.getErrorMessage(data[2]));
                bleClient.disconnect();
            }
        } else {
            bleClient.disconnect();
        }
    }

    @Override
    public void respondStopManualSample(byte[] data) {
//        bleState.setWorking(false, "respondStopManualSample");
        bleState.setStatus(BleState.STATUS_READY,"respondStopManualSample");

        if (data.length == 3) {
            if (bleState.randomCodeSuccess(data[1]) && data[2] == BluetoothConfig.ERROR_NONE) {
//                bleState.setSampling(false);
                bleState.setStatus(BleState.STATUS_DISCONNECT,"stopSample");
                bleSender.stopSample();
            } else {
                bleClient.disconnect();
            }
        } else {
            bleClient.disconnect();
        }
    }

    @Override
    public void respondManualSample(byte[] data) {
        // 手动采谱
        if (data.length >= 3) {
            ManualSampleData sampleData = new ManualSampleData.Builder().setInProfile400(data);
            int errorCode = sampleData.getErrorCode();
            if (sampleData.getTaq() == ManualMeasureNewOrder.TAG_ECG_PRE ||
                    sampleData.getTaq() == ManualMeasureNewOrder.TAG_ECG_REAL_DATA) {
                if (sampleData.isHaveData()) {
//                    bleState.setSampling(true);
                    bleState.setStatus(BleState.STATUS_SAMPLING,"sampling");

                    if (measureEcgData != null) {
                        measureEcgData.addData(sampleData);
                    }
                } else {
//                    bleState.setSampling(false);
                    bleState.setStatus(BleState.STATUS_READY,"sampling");

                    bleSender.sampleEcgError(new ManualEcgError(errorCode));
                }
            } else if (sampleData.getTaq() == ManualMeasureNewOrder.TAG_PRE ||
                    sampleData.getTaq() == ManualMeasureNewOrder.TAG_REAL_DATA) {
                if (sampleData.isHaveData()) {
//                    bleState.setSampling(true);
                    bleState.setStatus(BleState.STATUS_SAMPLING,"sampling");

                    if (measurePwData != null) {
                        measurePwData.addData(sampleData);
                    }
                } else {
//                    bleState.setSampling(false);
                    bleState.setStatus(BleState.STATUS_READY,"sampling");
                    bleSender.samplePwError(new ManualPwError(errorCode));
                }
            } else if (sampleData.getTaq() == ManualMeasureNewOrder.TAG_AUTO) {
                if (sampleData.isHaveData()) {
//                    bleState.setSampling(true);
                    bleState.setStatus(BleState.STATUS_SAMPLING,"sampling");

                    bleSender.manualPwData(sampleData.getManualPwData());
                } else {
//                    bleState.setSampling(false);
                    bleState.setStatus(BleState.STATUS_READY,"sampling");
                    bleSender.samplePwError(new ManualPwError(errorCode));
                }
            } else if (sampleData.getTaq() == ManualMeasureNewOrder.TAG_PW_RAW_DATA) {
                if (sampleData.isHaveData()) {
//                    bleState.setSampling(true);
                    bleState.setStatus(BleState.STATUS_SAMPLING,"sampling");

                    bleSender.manualPwData(sampleData.getManualPwData());
                } else {
                    bleState.setStatus(BleState.STATUS_READY,"sampling");
//                    bleState.setSampling(false);
                    bleSender.samplePwError(new ManualPwError(errorCode));
                }
            }
        } else {
            bleClient.disconnect();
        }
    }

    @Override
    public void respondAutoSampleInfo(byte[] data) {
//        bleState.setWorking(false, "respondAutoSampleInfo");
        bleState.setStatus(BleState.STATUS_READY,"respondAutoSampleInfo");

        if (data.length == 4) {
            // 发生错误
            if (bleState.randomCodeSuccess(data[1]) && data[2] == BluetoothConfig.ERROR_NONE) {
                if (data[3] > 0) {
                    bleOrder.getStoredAutoPwData();
                    bleSender.startReceivingAutoPwData();
                } else {
                    Log.i(TAG, "获取自动采样数据完成 ");
//                    bleState.setSampling(false);
                    bleState.setStatus(BleState.STATUS_READY,"autoSampling");
                    bleSender.stopReceivingAutoPwData();
                }
                manager.saveLatestSyncTime(Calendar.getInstance().getTimeInMillis());
            } else {
                bleClient.disconnect();
            }
        } else {
            bleClient.disconnect();
        }
    }

    @Override
    public void respondStoredSampleRet(byte[] data) {
        if (data.length >= 2) {
            StoredSampleData.Builder storedSampleDataBuilder = bleState.getStoredSampleDataBuilder();
            if (storedSampleDataBuilder == null) {
                storedSampleDataBuilder = new StoredSampleData.Builder(AUTO_SAMPLE_DOUBLE_POINT_SIZE);
                bleState.setStoredSampleDataBuilder(storedSampleDataBuilder);
            }
            if (!bleState.randomCodeSuccess(data[1])) {
                bleClient.disconnect();
                return;
            }
            storedSampleDataBuilder.setInProfile400(data);
            if (storedSampleDataBuilder.isError()) {
                Log.e(TAG, "自动采样信息错误 ");
                error();
                return;
            }
            if (storedSampleDataBuilder.isFinish(AUTO_SAMPLE_DOUBLE_POINT_SIZE)) {
                if (storedSampleDataBuilder.isCrcCorrect()) {
                    if (bleState.isAutoDataState()) {
                        Log.e(TAG, "auto sample complete");
                        StoredSampleData storedSampleData = storedSampleDataBuilder.create();
                        bleSender.getSampleCompleteAutoData(new SampleCompleteData(storedSampleData.getUnzippedData(),
                                storedSampleData.getCalendar().getTimeInMillis(), true));
                        bleOrder.deleteStoredData(true);
                    } else {
                        Log.e(TAG, "manual sample complete");
//                        bleState.setSampling(false);
                        bleState.setStatus(BleState.STATUS_READY,"autoSampling");
                        StoredSampleData storedSampleData = storedSampleDataBuilder.create();
                        bleSender.getSampleCompleteManualData(new SampleCompleteData(storedSampleData.getUnzippedData(),
                                storedSampleData.getCalendar().getTimeInMillis(), false));
                        bleOrder.deleteStoredData(false);
                    }
//                    deleteStoredData(storedSampleData.isAuto());
                } else {
                    Log.e(TAG, "auto sample crc error");
                    bleOrder.deleteStoredData(true);
                }
            }
        } else {
            bleClient.disconnect();
        }
    }

    @Override
    public void respondDeleteStoredSample(byte[] data) {
//        bleState.setWorking(false, "respondDeleteStoredSample");
        bleState.setStatus(BleState.STATUS_READY,"respondDeleteStoredSample");

        if (data.length == 3 && bleState.randomCodeSuccess(data[1])) {
            Log.i(TAG, BluetoothConfig.getErrorMessage(data[2]));
            if (bleState.isAutoDataState()) {
                bleOrder.getAutoSampleInfo();
            } else {
                bleSender.deleteStoredManualSampleDataSuccess();
            }
            bleState.setStoredSampleDataBuilder(null);
        } else {
            bleClient.disconnect();
        }
    }

    @Override
    public void respondGetSystemParam(byte[] data) {
//        bleState.setWorking(false, "respondGetSystemParam");
        bleState.setStatus(BleState.STATUS_READY,"respondGetSystemParam");
        if (data.length >= 4) {
            if (data[2] == BluetoothConfig.ERROR_NONE) {
                GetSystemParamResponse response = new GetSystemParamResponse();
                response.set(data, manager.getDeviceProtocolVersion());
                Log.i(TAG, "respondGetSystemParam: " + response.getSystemParamData());
                if (data[1] == BluetoothConfig.PARAM_HEIGHT_WEIGHT) {
                    HeightWeightData heightWeightData = response.getSystemParamData().getHeightWeightData();
                    bleSender.getHeightAndWeight(heightWeightData);
                } else if (data[1] == BluetoothConfig.PARAM_DISPLAY_PAGE) {
                    DisplayPage displayPage = response.getSystemParamData().getPageDisplayData();
                    bleSender.getDisplayPage(displayPage);
                    manager.setDisplayPage(displayPage);
                } else if (data[1] == BluetoothConfig.PARAM_ALERT_REMINDERS) {
                    ReminderData reminders = response.getSystemParamData().getReminderData();
                    manager.setReminder(reminders);
                    bleSender.getAlertReminders(reminders);
                }
            } else {
                Log.i(TAG, BluetoothConfig.getErrorMessage(data[2]));
                bleClient.disconnect();
            }
        } else {
            bleClient.disconnect();
        }
    }

    @Override
    public void respondSetSystemParam(byte[] data) {
//        bleState.setWorking(false, "respondSetSystemParam");
        bleState.setStatus(BleState.STATUS_READY,"respondSetSystemParam");

        if (data.length == 3) {
            if (data[1] == BluetoothConfig.PARAM_ALERT_REMINDERS) {
                if (data[2] == BluetoothConfig.ERROR_NONE) {
                    if (bleState.getReminders() != null) {
                        manager.setReminder(bleState.getReminders());
                    }
                    bleSender.setParamAlertRemindersSuccess();
                } else {
                    Log.e(TAG, "respondSetSystemParam: " + BluetoothConfig.getErrorMessage(data[2]));
                    error();
                }
            } else if (data[1] == BluetoothConfig.PARAM_DISPLAY_PAGE) {
                if (data[2] == BluetoothConfig.ERROR_NONE) {
                    if (bleState.getDisplayPage() != null) {
                        manager.setDisplayPage(bleState.getDisplayPage());
                    }
                    bleSender.setDisplayPageSuccess();
                } else {
                    Log.e(TAG, "respondSetSystemParam: " + BluetoothConfig.getErrorMessage(data[2]));
                    error();
                }
            } else if (data[1] == BluetoothConfig.PARAM_HEIGHT_WEIGHT) {
                if (data[2] == BluetoothConfig.ERROR_NONE) {
                    bleSender.setHeightAndWeightSuccess();
                } else {
                    Log.e(TAG, "respondSetSystemParam: " + BluetoothConfig.getErrorMessage(data[2]));
                    error();
                }
            }
        } else {
            bleClient.disconnect();
        }
    }

    @Override
    public void respondStatusUpdate(byte[] data) {
        if (data.length == 7) {
            byte[] key = manager.getEncryptKey();
            byte[] vector = manager.getEncryptVector();
            if (data[2] == (byte) (key[2] ^ vector[3]) && data[3] == (byte) (key[4] ^ vector[5])) {
                if (data[4] == 0x01) {
                    bleSender.onPowerChange();
                }
                if (data[5] == 0x01) {
                    // 自动采样信息变化
                    bleSender.onAutoDataChange();
                } else {
                    bleSender.noAutoSampleData();
                }

            } else {
                bleClient.disconnect();
            }
        } else {
            bleClient.disconnect();
        }
    }

    @Override
    public void respondUnPair(byte[] data) {
//        bleState.setWorking(false, "respondUnPair");
        bleState.setStatus(BleState.STATUS_READY,"respondUnPair");

        if (data.length == 3 && data[0] == BluetoothConfig.CODE_UNBIND && bleState.randomCodeSuccess(data[1])) {
            if (data[2] == BluetoothConfig.ERROR_NONE) {
                Log.i(TAG, "解绑成功");
                bleSender.unPairSuccess();
                bleClient.disconnect();
            } else {
                error();
            }
        } else {
            error();
        }
    }

    @Override
    public void respondUpdateTime(byte[] data) {
//        bleState.setWorking(false, "respondUpdateTime");
        bleState.setStatus(BleState.STATUS_READY,"respondUpdateTime");

        // 更新时间
        if (data.length == 3 && bleState.randomCodeSuccess(data[1])) {
            if (data[2] == BluetoothConfig.ERROR_NONE) {
                Log.i(TAG, "更新时间成功");
                bleSender.setTimeSuccess();
            } else {
                Log.i(TAG, BluetoothConfig.getErrorMessage(data[2]));
                error();
            }
        } else {
            error();
        }
    }

    @Override
    public void respondResetAutoSampleTime(byte[] data) {
//        bleState.setWorking(false, "respondResetAutoSampleTime");
        bleState.setStatus(BleState.STATUS_READY,"respondResetAutoSampleTime");

        // 重置自动采样时间
        if (data.length == 3 && data[0] == BluetoothConfig.CODE_RESET_AUTO_SAMPLE &&
                bleState.randomCodeSuccess(data[1])) {
            if (data[2] == BluetoothConfig.ERROR_NONE) {
                Log.i(TAG, "设置自动采样时间成功");
                bleSender.resetAutoSampleTimeSuccess();
            } else {
                Log.i(TAG, BluetoothConfig.getErrorMessage(data[2]));
                error();
            }
        } else {
            error();
        }
    }

    @Override
    public void respondReExeAutoSample(byte[] data) {
        // 重新采样
//        bleState.setWorking(false, "respondReExeAutoSample");
        bleState.setStatus(BleState.STATUS_READY,"respondReExeAutoSample");

        if (data.length == 3 && data[0] == BluetoothConfig.CODE_REEXE_AUTO_SAMPLE
                && bleState.randomCodeSuccess(data[1])) {
            if (data[2] == BluetoothConfig.ERROR_NONE) {
                Log.i(TAG, "重新自动采样执行成功");
                bleSender.setSampleAgainSuccess();
            } else {
                Log.i(TAG, BluetoothConfig.getErrorMessage(data[2]));
                error();
            }
        } else {
            error();
        }
    }

    @Override
    public void respondFotaEnd(byte[] data) {
        // 固件升级结束应答
//        bleState.setWorking(false, "respondFotaEnd");
        bleState.setStatus(BleState.STATUS_READY,"respondFotaEnd");
        if (data.length == 3 && data[0] == BluetoothConfig.CODE_FOTA_END) {
            if (bleState.randomCodeSuccess(data[1]) && data[2] == BluetoothConfig.ERROR_NONE) {
                Log.i(TAG, BluetoothConfig.getErrorMessage(data[2]));
                UpgradeFirmwareManager upgradeFileManager = bleState.getUpgradeFileManager();
                bleSender.upgrade(new FirmStateData(FirmStateData.END, upgradeFileManager.getTotalPkgNumber(),
                        upgradeFileManager.getTotalPkgNumber()));
//                bleState.setUpgrading(false);
                bleState.setStatus(BleState.STATUS_READY,"upgrading");
                bleState.setUpgradeSuccess(upgradeFileManager.getTotalPkgNumber() == upgradeFileManager.getTotalPkgNumber());
            } else {
                error();
            }
        } else {
            error();
        }
    }

    @Override
    public void respondFotaData(byte[] data) {
        // 固件升级文件应答
        if (data.length == 3 && data[0] == BluetoothConfig.CODE_FOTA_DATA) {
            if (bleState.randomCodeSuccess(data[1]) && data[2] == BluetoothConfig.ERROR_NONE) {
                UpgradeFirmwareManager upgradeFileManager = bleState.getUpgradeFileManager();
                if (upgradeFileManager != null) {
                    bleSender.upgrade(new FirmStateData(FirmStateData.CONTINUE, upgradeFileManager.getCurrentIndex(), upgradeFileManager.getTotalPkgNumber()));
//                    bleState.setUpgrading(true);
                    bleState.setStatus(BleState.STATUS_UPGRADING_CONTINUE,"upgrading");
                    bleState.setUpgradeSuccess(false);
                }
                if (!bleState.isCancelUpgrading()) {
                    if (upgradeFileManager != null && upgradeFileManager.hasNextBody()) {
                        bleOrder.upgradeBody();
                    } else {
                        bleOrder.upgradeComplete();
                    }
                } else {
                    bleSender.upgradeCancel();
                }
            } else {
                error();
            }
        } else {
            error();
        }
    }

    @Override
    public void respondFotaInfo(byte[] data) {
        // 固件升级信息包应答
        if (data.length == 3 && data[0] == BluetoothConfig.CODE_FOTA_INFO) {
            if (data[2] == BluetoothConfig.ERROR_NONE && bleState.randomCodeSuccess(data[1])) {
                bleSender.upgrade(new FirmStateData(FirmStateData.START, 0,
                        bleState.getUpgradeFileManager().getTotalPkgNumber()));
//                bleState.setUpgrading(true);
                bleState.setStatus(BleState.STATUS_UPGRADING_START,"upgrading");
                bleState.setUpgradeSuccess(false);
                if (!bleState.isCancelUpgrading()) {
                    bleOrder.upgradeBody();
                } else {
                    bleSender.upgradeCancel();
                }
            } else {
                error();
            }
        } else {
            error();
        }
    }

    @Override
    public void respondSetCalculateResult(byte[] data) {
//        bleState.setWorking(false, "respondSetCalculateResult");
        bleState.setStatus(BleState.STATUS_READY,"respondSetCalculateResult");

        if (data.length == 3 && bleState.randomCodeSuccess(data[1])) {
            if (data[2] == BluetoothConfig.ERROR_NONE) {
                bleSender.setBloodPressureComplete();
            } else {
                Log.e(TAG, BluetoothConfig.getErrorMessage(data[2]));
                error();
            }
        } else {
            Log.e(TAG, "设置血压: 格式错误 ");
            error();
        }
    }

    @Override
    public void respondGetFirmwareInfo(byte[] data) {
//        bleState.setWorking(false, "respondGetFirmwareInfo");
        bleState.setStatus(BleState.STATUS_READY,"respondGetFirmwareInfo");

        if (data.length == 7 && bleState.randomCodeSuccess(data[1])) {
            if (data[2] == BluetoothConfig.ERROR_NONE) {
                FirmwareInfoData firmwareInfoData = new FirmwareInfoData();
                firmwareInfoData.mirror = data[3];
                firmwareInfoData.version = new int[]{data[4] & 0xFF, data[5] & 0xFF, data[6] & 0xFF};
                Log.i(TAG, "获取固件信息: " + firmwareInfoData.toString());
                manager.saveDeviceFirmwareVersion(firmwareInfoData.version);
                bleSender.getFirmwareInfo(firmwareInfoData);
            } else {
                Log.i(TAG, "获取固件信息错误 " + BluetoothConfig.getErrorMessage(data[2]));
                bleClient.disconnect();
            }
        } else {
            bleClient.disconnect();
        }
    }

    @Override
    public void respondStepData(byte[] data) {
//        bleState.setWorking(false, "respondStepData");
        bleState.setStatus(BleState.STATUS_READY,"respondStepData");

        if (data.length == 18 && bleState.randomCodeSuccess(data[1])) {
            if (data[2] == BluetoothConfig.ERROR_NONE) {
                CurrentStepData currentStepData = new CurrentStepData();
                int hour = (data[3] & 0xFF) + ((data[4] & 0xFF) << 8) + ((data[5] & 0xFF) << 16) + ((data[6] & 0xFF) << 24);
                int quarterOffset = data[7] & 0xFF;
                int quarterStep = (data[8] & 0xFF) + ((data[9] & 0xFF) << 8) + ((data[10] & 0xFF) << 16) + ((data[11] & 0xF) << 24);
                int quarterMin = (data[11] >> 8) & 0xF;
                int step = (data[12] & 0xFF) + ((data[13] & 0xFF) << 8) + ((data[14] & 0xFF) << 16) + ((data[15] & 0xFF) << 24);
                int min = (data[16] & 0xFF) + ((data[17] & 0xFF) << 8);
                currentStepData.setHour(hour);
                currentStepData.setQuarterOffset(quarterOffset);
                currentStepData.setQuarterStep(quarterStep);
                currentStepData.setQuarterMinute(quarterMin);
                currentStepData.setDayStep(step);
                currentStepData.setDayMinute(min);
                bleSender.currentStep(currentStepData);
            } else {
                Log.e(TAG, "获取步数错误 " + BluetoothConfig.getErrorMessage(data[2]));
                error();
            }
        } else {
            bleClient.disconnect();
        }
    }

    @Override
    public void respondHistoryStepData(byte[] data) {
        if (data.length >= 3 && bleState.randomCodeSuccess(data[1])) {
            if (data.length >= 4 && (data[3] & 0xFF) * 20 + 4 != data.length) {
                Log.e(TAG, "获取历史步数: 包大小错误");
                error();
                return;
            }
            if (data[2] == BluetoothConfig.ERROR_NONE && data.length >= 4) {
                StepHistory.Builder stepHistoryBuilder = bleState.getStepHistoryBuilder();
                if (stepHistoryBuilder == null) {
                    stepHistoryBuilder = new StepHistory.Builder();
                }
                stepHistoryBuilder.setInProfile400(data);
                bleState.setStepHistoryBuilder(stepHistoryBuilder);
            } else if (data[2] == BluetoothConfig.ERROR_END_OF_STREAM && data.length >= 4) {
                StepHistory.Builder stepHistoryBuilder = bleState.getStepHistoryBuilder();
                if (stepHistoryBuilder == null) {
                    stepHistoryBuilder = new StepHistory.Builder();
                }
                stepHistoryBuilder.setInProfile400(data);
                Log.e(TAG, "发送计步数据 ");
                bleSender.stepHistory(stepHistoryBuilder.create());
//                bleState.setWorking(false, "respondHistoryStepData");
                bleState.setStatus(BleState.STATUS_READY,"respondHistoryStepData");

                bleState.setStepHistoryBuilder(null);
            } else {
                Log.i(TAG, BluetoothConfig.getErrorMessage(data[2]));
                error();
            }
        } else {
            Log.e(TAG, "获取历史步数失败");
            bleClient.disconnect();
        }
    }

    @Override
    public void respondCalibrateStepData(byte[] data) {
//        bleState.setWorking(false, "respondCalibrateStepData");
        bleState.setStatus(BleState.STATUS_READY,"respondCalibrateStepData");

        if (data.length == 3 && bleState.randomCodeSuccess(data[1])) {
            if (data[2] == BluetoothConfig.ERROR_NONE) {
                manager.setIsStepHistorySyncToBracelet(true);
                bleSender.setHistoryStepSuccess();
                Log.i(TAG, "同步计步数据成功");
            } else {
                Log.e(TAG, "同步计步失败:" + BluetoothConfig.getErrorMessage(data[2]));
                error();
            }
        } else {
            Log.i(TAG, "respondCalibrateStepData: error");
            bleClient.disconnect();
        }
    }

    @Override
    public void respondEnableStepUpdate(byte[] data) {
//        bleState.setWorking(false, "respondCalibrateStepData");
        bleState.setStatus(BleState.STATUS_READY,"respondEnableStepUpdate");

        if (data.length == 3 && bleState.randomCodeSuccess(data[1])) {
            if (data[2] == BluetoothConfig.ERROR_NONE) {
                Log.i(TAG, "开启或关闭计步更新成功");
                bleSender.setStepUpdateSuccess();
            } else {
                Log.i(TAG, BluetoothConfig.getErrorMessage(data[2]));
                error();
            }
        } else {
            Log.e(TAG, "开启计步更新失败");
            bleClient.disconnect();
        }
    }

    @Override
    public void respondLookForBand(byte[] data) {
//        bleState.setWorking(false, "respondLookForBand");
        bleState.setStatus(BleState.STATUS_READY,"respondLookForBand");

        if (data.length == 3 && bleState.randomCodeSuccess(data[1])) {
            bleSender.setFindBraceletSuccess(data[2]);
        } else {
            Log.e(TAG, "查找手环错误");
            bleClient.disconnect();
        }
    }

    private void error() {
//        bleState.setUpgrading(false);
        bleClient.disconnect();
    }
}
