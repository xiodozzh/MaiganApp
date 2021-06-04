package com.mgtech.blelib.biz;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.mgtech.blelib.constant.BleConstant;
import com.mgtech.blelib.core.BleClient2;
import com.mgtech.blelib.core.BleClientCallback2;
import com.mgtech.blelib.core.BleCoreConstant;
import com.mgtech.blelib.core.ParsedAd;
import com.mgtech.blelib.core.ParsedBleBroadcast;
import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.blelib.entity.AutoMeasurePeriodData;
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
import com.mgtech.blelib.entity.PairRequestResponse;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.entity.StepHistory;
import com.mgtech.blelib.entity.StoredSampleData;
import com.mgtech.blelib.utils.AESUtils;
import com.mgtech.blelib.utils.UpgradeFirmwareManager;
import com.mgtech.blelib.utils.Utils;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.zip.CRC32;

/**
 * @author zhaixiang
 */
public class BleCore {
    private static final String TAG = "BleCore";
    private static final int MTU = 512;
    private BleClient2 bleClient;
    private Context context;
    private IBraceletInfoManager manager;
    private BleSender bleSender;
    private IResponseBytes responseBytes;
    private static Handler mHandler = new Handler();

    private BleOrder bleOrder;
    private BleState bleState;



    public BleCore(Context context, BleClient2 bleClient, IBraceletInfoManager manager, BleSender bleSender,
                   BleOrder bleOrder, BleState bleState,IResponseBytes responseBytes) {
        this.bleClient = bleClient;
        this.bleClient.setCallback(clientCallback);
        this.context = context;
        this.responseBytes = responseBytes;
        this.manager = manager;
        this.bleSender = bleSender;
        this.bleOrder = bleOrder;
        this.bleState = bleState;
    }


    private BleClientCallback2 clientCallback = new BleClientCallback2() {

        @Override
        public void onConnected() {
            Logger.e(TAG + " onConnected: ");
            bleSender.connected();
            boolean success = bleClient.requestMtu(MTU);
            launchConnectTimeOutClock();
            if (!success) {
                Log.i(TAG, "设置mtu失败");
                bleClient.disconnect();
            }
        }

        @Override
        public void connectExistingConnection(boolean exist) {
        }

        @Override
        public void onDisConnected() {
            bleSender.disconnect();
//            bleState.setReady(false, "onDisConnected");
            bleState.setStatus(BleState.STATUS_DISCONNECT,"onDisConnected");
            bleState.clear();
        }

        @Override
        public void onServiceDiscovered() {
            enableVerificationNotification();
        }

        @Override
        public void onWrite(boolean status, String s) {
        }

        @Override
        public void onRead(boolean status, String s, byte[] value) {
        }

        @Override
        public void onDataTransferEnabled(String uuid) {
            if (TextUtils.equals(uuid, BleCoreConstant.CHAR_VERIFICATION_RESPONSE)) {
                // 非加密使能后使能加密通知
                enableNotification();
            } else {
                // 加密使能后设置
                BroadcastData linkedBroadcastData = bleState.getLinkedBroadcastData();
                if (linkedBroadcastData != null) {
                    bleSender.startVerify();
                    bleOrder.verifyWork(linkedBroadcastData.getMacAddressBytes(), linkedBroadcastData.getPairCode());
                } else if (manager.isPaired()) {
                    bleSender.startVerify();
                    bleOrder.verifyWork(bleOrder.getMacBytes(), manager.getCodePair());
                } else {
                    Log.i(TAG, "onMtuChanged: macAddressBytes == null");
                    bleClient.disconnect();
                }
            }
        }

        @Override
        public void onDataReceived(String uuid, byte[] receivedData) {
            Log.i(TAG, "onDataReceived: " + uuid);
            if (uuid.equals(BleCoreConstant.CHAR_RESPONSE)) {
                onReceivedData(receivedData);
            } else if (uuid.equals(BleCoreConstant.CHAR_VERIFICATION_RESPONSE)) {
                onReceivedVerificationData(receivedData);
            }
        }

        @Override
        public void onRRSIChanged(int rssi) {
        }

        @Override
        public void onError() {
            bleClient = BleClient2.getInstance(context);
        }

        @Override
        public void onMtuChanged(int mtu, int status) {
            Log.i(TAG, "MTU 修改成为: " + mtu);
            mHandler.removeCallbacks(connectTimeOutRunnable);
            bleClient.discoverServices();
        }
    };

    public void startPairScan(String[] name) {
        bleClient.startScan(context, name, pairScan);
    }

    public void startScan(String[] name, String mac) {
        bleClient.startScan(context, name, mac, linkScan);
    }

    public void stopScan() {
//        bleState.setReady(false,"stopScan");
        if (bleClient.getConnectionState() != BleClient2.STATE_CONNECTED) {
//            bleState.setReady(false, "stopScan");
            bleState.setStatus(BleState.STATUS_DISCONNECT,"stopScan");
        }
        bleClient.stopScan(context, pairScan);
        bleClient.stopScan(context, linkScan);
    }

    public void connect(String mac) {
        bleClient.connect(context, mac);
    }

    public void disconnect() {
        stopScan();
        bleClient.disconnect();
        if (bleClient.getConnectionState() != BleClient2.STATE_CONNECTED) {
            bleSender.disconnect();
//            bleState.setReady(false, "disconnect");
            bleState.setStatus(BleState.STATUS_DISCONNECT,"disconnect");
        }
    }

    private ScanCallback pairScan = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            Log.i(TAG, "onScanResult: " + device.getName() + "  " + device.getAddress());
            Log.i(TAG, "onScanResult: " + result.getScanRecord());
            int rssi = result.getRssi();
            if (result.getScanRecord() != null) {
                byte[] data = result.getScanRecord().getBytes();
                ParsedAd parsedAd = ParsedAd.parseData(data);
                if (!manager.isPaired()) {
                    ParsedBleBroadcast broadcastCandidate = parsedAd.getParsedBleBroadcast();
                    if (broadcastCandidate != null) {
                        Logger.e("broadcast: " + broadcastCandidate.toString());
                        if (isBroadcastSignatureCorrect(broadcastCandidate)
                                && broadcastCandidate.getBroadcastReason() == BluetoothConfig.BROADCAST_ALLOW_BOND) {
                            Log.e(TAG, "广播签名正确 ");
                            broadcastCandidate.setDeviceName(new String(parsedAd.getLocalName()));
                            BroadcastData broadcastData = new BroadcastData(device.getAddress().toUpperCase(), rssi,
                                    Calendar.getInstance().getTimeInMillis(), broadcastCandidate);
                            bleSender.sendBroadcast(broadcastData);
                            bleState.setLinkedBroadcastData(broadcastData);
                        } else {
                            Log.e(TAG, "onScanResults: 签名不正确或广播方式不正确");
                        }
                    }

                }
            }
        }
    };

    private ScanCallback linkScan = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            if (result.getScanRecord() != null) {
                byte[] data = result.getScanRecord().getBytes();
                int rssi = result.getRssi();
                ParsedAd parsedAd = ParsedAd.parseData(data);
                String mac = manager.getAddress();
                if (manager.isPaired() && mac.equals(device.getAddress())) {
                    ParsedBleBroadcast broadcastCandidate = parsedAd.getParsedBleBroadcast();
//                    broadcastCandidate.setDeviceName(device.getName());
                    broadcastCandidate.setDeviceName(new String(parsedAd.getLocalName()));
                    if (!isPaired(broadcastCandidate.getPairCode())) {
                        // 配对码错误,链接断开
                        manager.deletePairInformation();
                        manager.setDeleteMac(mac);
                        stopScan();
                        bleSender.sendPairCodeDifferent();
                        return;
                    }
                    if (isBroadcastSignatureCorrect(broadcastCandidate)) {
                        bleSender.sendFindDevice();
                        bleState.setLinkedBroadcastData(new BroadcastData(device.getAddress().toUpperCase(), rssi,
                                Calendar.getInstance().getTimeInMillis(), broadcastCandidate));
                        connectDevice(broadcastCandidate);
                    } else {
                        Log.e(TAG, "onScanResults: 签名不正确");
                    }
                }
            }


        }
    };

    /**
     * 连接广播
     *
     * @param data 广播包
     */
    void connectDevice(BroadcastData data) {
        stopScan();
        bleClient.connect(context, data.getMacAddress());
    }

    private void connectDevice(ParsedBleBroadcast broadcastCandidate) {
        stopScan();
        bleClient.connect(context, manager.getAddress());
        if (broadcastCandidate != null) {
            manager.saveDeviceName(broadcastCandidate.getDeviceName());
            manager.saveDeviceProtocolVersion(broadcastCandidate.getProtocolVersion());
        }
    }

    /**
     * 广播包签名是否正确
     *
     * @return boolean
     */
    private boolean isBroadcastSignatureCorrect(ParsedBleBroadcast broad) {
        byte[] mac = broad.getMacAddress();
        byte[] sign = broad.getSignature();
        int pairCode = broad.getPairCode();
        if (mac == null || sign == null) {
            return false;
        }
        if (sign.length == 2) {
            return mac.length == 6 && (byte) (mac[1] ^ mac[2]) == sign[0] &&
                    (byte) (mac[0] ^ (pairCode & 0xFF)) == sign[1];
        } else if (sign.length == 1) {
            return mac.length == 6 && (byte) (mac[0] ^ (pairCode & 0xFF)) == sign[0];
        }
        return false;
    }

    private boolean isPaired(int pairCode) {
        int storedPairCode = manager.getCodePair();
        Log.e(TAG, "broadcast: " + pairCode + "  stored " + storedPairCode);
        return manager.isPaired() && pairCode != 0 && storedPairCode == pairCode;
    }

    /**
     * 使能非加密通道
     */
    private void enableVerificationNotification() {
        Log.e(TAG, "run: enableVerificationNotification");
//        bleClient.toggleNotification(BleCoreConstant.MG_PROFILE_SERVICE,
//                BleCoreConstant.CHAR_VERIFICATION_RESPONSE, true);
        bleClient.toggleNotification(BleCoreConstant.MG_PROFILE_SERVICE, BleCoreConstant.CHAR_VERIFICATION_RESPONSE, true);
    }

    /**
     * 使能加密通道
     */
    private void enableNotification() {
        Log.e(TAG, "run: enableNotification ");
        bleClient.toggleNotification(BleCoreConstant.MG_PROFILE_SERVICE, BleCoreConstant.CHAR_RESPONSE, true);
    }

    /**
     * 收到校验数据
     *
     * @param receivedData 数据
     */
    private void onReceivedVerificationData(byte[] receivedData) {
        byte[] data = AESUtils.decrypt(getNormalEncryptKey(), receivedData, getNormalEncryptVector());
        if (data.length < 1) {
            bleClient.disconnect();
            return;
        }
        Log.e(TAG, "onReceivedVerificationData: " + Arrays.toString(data));
        switch (data[0]) {
            case BluetoothConfig.CODE_AUTH_LINK:
                onAuthData(data);
                break;
            case BluetoothConfig.CODE_REQUEST_PAIR:
                onPairRequest(data);
                break;
            default:
        }
    }

    /**
     * 手环校验应答
     *
     * @param data byte[]
     */
    private void onAuthData(byte[] data) {
        if (data.length == 3 && bleState.randomCodeSuccess(data[1]) && data[2] == BluetoothConfig.ERROR_NONE) {
            // ready的状态
//            bleState.setReady(true, "onAuthData");
            bleState.setStatus(BleState.STATUS_READY,"onAuthData");
            bleSender.link();
        } else {
            bleClient.disconnect();
        }
    }

    /**
     * 手环配对请求应答
     *
     * @param data byte[]
     */
    private void onPairRequest(byte[] data) {
        mHandler.removeCallbacks(pairTimeOutRunnable);
        PairRequestResponse pairRequestResponse = new PairRequestResponse(data);
        if (pairRequestResponse.isError()) {
            Log.e(TAG, "配对请求返回错误 ");
            bleClient.disconnect();
            return;
        }
        byte[] encryptKey = pairRequestResponse.getEncryptKey();
        byte[] encryptVector = pairRequestResponse.getEncryptVector();
        if (isExchangeEncryptSignatureCorrect(encryptKey, encryptVector, pairRequestResponse.getId(),
                pairRequestResponse.getEncryptSignature())) {
            String deviceUUID = bytesToHex(pairRequestResponse.getId());
            CheckDeviceData checkDeviceData = new CheckDeviceData(deviceUUID, bleState.getLinkedBroadcastData().getMacAddress(),
                    encryptKey, encryptVector);
            bleSender.braceletNeedCheck(checkDeviceData);
        } else {
            Log.e(TAG, "加密认证错误");
            bleClient.disconnect();
        }
    }

    private Runnable pairTimeOutRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "run: pairTimeOutRunnable");
            bleClient.disconnect();
        }
    };

    private Runnable connectTimeOutRunnable = new Runnable() {
        @Override
        public void run() {
            bleClient.disconnect();
        }
    };

    /**
     * 启动绑定超时倒计时
     */
    public void launchPairTimeOutClock() {
        mHandler.removeCallbacks(pairTimeOutRunnable);
        mHandler.postDelayed(pairTimeOutRunnable, BleConstant.PAIR_TIME_OUT_TIME);
    }

    private void launchConnectTimeOutClock() {
        mHandler.removeCallbacks(connectTimeOutRunnable);
        mHandler.postDelayed(connectTimeOutRunnable, BleConstant.CONNECT_TIME_OUT_TIME);
    }

    /**
     * 收到数据
     *
     * @param receivedData 数据
     */
    private void onReceivedData(byte[] receivedData) {
        byte[] encryptKey;
        byte[] encryptVector;
        CheckDeviceData unconfirmedDeviceData = bleState.getUnconfirmedDeviceData();
        if (unconfirmedDeviceData != null) {
            encryptKey = unconfirmedDeviceData.getKey();
            encryptVector = unconfirmedDeviceData.getVector();
        } else {
            encryptKey = manager.getEncryptKey();
            encryptVector = manager.getEncryptVector();
        }
        if (encryptKey == null || encryptVector == null) {
            return;
        }
        byte[] data = AESUtils.decrypt(encryptKey, receivedData, encryptVector);
        Logger.i("收到信息: " + Arrays.toString(data));
        responseBytes.receivedData(data);
        mHandler.removeCallbacks(pairTimeOutRunnable);
    }

    /**
     * 获取普通密钥
     *
     * @return 密钥
     */
    private byte[] getNormalEncryptKey() {
        byte[] address;
        if (manager.isPaired()) {
            address = bleOrder.getMacBytes();
        } else {
            BroadcastData linkedBroadcastData = bleState.getLinkedBroadcastData();
            address = linkedBroadcastData.getMacAddressBytes();
        }
        byte[] key = new byte[16];
        System.arraycopy(BluetoothConfig.ENCRYPT_FIXED_CODE, 0, key, 0, 7);
        key[7] = address[0];
        System.arraycopy(BluetoothConfig.ENCRYPT_FIXED_CODE, 0, key, 8, 7);
        key[15] = address[2];
        return key;
    }

    /**
     * 获取普通加密向量
     *
     * @return 向量
     */
    private byte[] getNormalEncryptVector() {
        byte[] address;
        int pairCode;
        if (manager.isPaired()) {
            address = bleOrder.getMacBytes();
            pairCode = manager.getCodePair();
        } else {
            BroadcastData linkedBroadcastData = bleState.getLinkedBroadcastData();
            address = linkedBroadcastData.getMacAddressBytes();
            pairCode = linkedBroadcastData.getPairCode();
        }
        byte[] vector = new byte[16];
        System.arraycopy(BluetoothConfig.ENCRYPT_FIXED_CODE, 0, vector, 0, 7);
        vector[7] = address[1];
        System.arraycopy(BluetoothConfig.ENCRYPT_FIXED_CODE, 0, vector, 8, 7);
        vector[15] = (byte) (pairCode & 0xFF);
        return vector;
    }

    /**
     * 交换密钥签名是否正确
     *
     * @param encryptKey       密钥
     * @param encryptVector    向量
     * @param encryptSignature 签名
     * @return boolean
     */
    private boolean isExchangeEncryptSignatureCorrect(byte[] encryptKey, byte[] encryptVector, byte[] id, byte[] encryptSignature) {
        if (encryptKey == null || encryptKey.length != 16 || encryptVector == null || id == null || encryptVector.length != 16
                || encryptSignature == null || encryptSignature.length != 4 || id.length != 16) {
            return false;
        }
        byte[] beforeCrc = new byte[48];
        System.arraycopy(encryptKey, 0, beforeCrc, 0, 16);
        System.arraycopy(encryptVector, 0, beforeCrc, 16, 16);
        System.arraycopy(id, 0, beforeCrc, 32, 16);
        byte[] crcResult = intToByteArray(getCRC32(beforeCrc));
        return crcResult[0] == encryptSignature[0] && crcResult[1] == encryptSignature[1] &&
                crcResult[2] == encryptSignature[2] && crcResult[3] == encryptSignature[3];
    }

    private void error() {
//        bleState.setSampling(false);
//        bleState.setUpgrading(false);
        bleClient.disconnect();
    }

    /**
     * int 转换为byte
     *
     * @param i long
     * @return 0 低位 4 高位
     */
    private byte[] intToByteArray(long i) {
        byte[] result = new byte[4];
        result[3] = (byte) ((i >> 24) & 0xFF);
        result[2] = (byte) ((i >> 16) & 0xFF);
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[0] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * 获得crc校验结果
     *
     * @param data 数据
     * @return 校验值
     */
    private long getCRC32(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return crc32.getValue();
    }

    private static String bytesToHex(byte[] src) {
        if (src == null || src.length < 16) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            builder.append(byteToHex(src[i]));
        }
        builder.append("-");
        for (int i = 4; i < 6; i++) {
            builder.append(byteToHex(src[i]));
        }
        builder.append("-");
        for (int i = 6; i < 8; i++) {
            builder.append(byteToHex(src[i]));
        }
        builder.append("-");
        for (int i = 8; i < 10; i++) {
            builder.append(byteToHex(src[i]));
        }
        builder.append("-");
        for (int i = 10; i < 16; i++) {
            builder.append(byteToHex(src[i]));
        }
        return builder.toString();
    }

    private static String byteToHex(byte b) {
        String string = Integer.toHexString(b & 0xFF);
        if (string.length() < 2) {
            string = "0" + string;
        }
        return string;
    }

}
