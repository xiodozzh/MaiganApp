package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.constant.BleStatusConstant;
import com.mgtech.blelib.entity.BleStateEvent;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.entity.AppConfigNew;
import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.AccountUseCase;
import com.mgtech.domain.interactor.DataUseCase;
import com.mgtech.domain.interactor.MedicineUseCase;
import com.mgtech.domain.interactor.PersonalInfoUseCase;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.RawDataFileUtil;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.SocketEvent;
import com.mgtech.maiganapp.service.BluetoothService;
import com.mgtech.maiganapp.service.NetJobService;
import com.mgtech.maiganapp.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;

import rx.Subscriber;

/**
 * @author zhaixiang
 * @date 2017/4/26
 * 主页
 */

public class MainViewModel extends BaseBleViewModel {
    private Application context;
    private DataUseCase dataUseCase;
    private MedicineUseCase medicineUseCase;
    private AccountUseCase accountUseCase;
    private PersonalInfoUseCase personalInfoUseCase;
    private IBraceletInfoManager manager;

    private static Handler mHandler = new Handler();

    public MainViewModel(Application context) {
        super(context);
        this.context = context;
        this.manager = new BraceletInfoManagerBuilder(context).create();
        int timeZone = manager.getTimeZone();
        if (timeZone != Utils.getTimeZone()) {
            manager.setIsStepHistorySyncToBracelet(false);
            manager.setTimeZone(Utils.getTimeZone());
        }
        this.dataUseCase = ((MyApplication) context).getDataUseCase();
        this.medicineUseCase = ((MyApplication) context).getMedicineUseCase();
        this.accountUseCase = ((MyApplication) context).getAccountUseCase();
        this.personalInfoUseCase = ((MyApplication) context).getPersonInfoUseCase();
        BluetoothService.startBluetoothService(context);
    }


    public void startService(Context context) {
        BluetoothService.startBluetoothService(context);
        bleRequest.linkIfAvailable();
    }


    public void stopService() {
        EventBus.getDefault().postSticky(new SocketEvent(false));
        BluetoothService.stopMeasureService(context);
    }

    @Override
    protected void onCleared() {
        medicineUseCase.unSubscribe();
        accountUseCase.unSubscribe();
        personalInfoUseCase.unSubscribe();
        dataUseCase.unSubscribe();
        mHandler.removeCallbacks(reConnectBle);
        super.onCleared();
    }


    /**
     * app是否需要升级
     *
     * @return 升级条件：有新版本、配置信息为强制升级
     */
    public boolean isAppNeedUpgrade() {
        return AppConfigNew.isForceUpgrade(context) && AppConfigNew.getVersion(context) > Utils.getVersionCode(context);
    }

    /**
     * 获取app下载链接
     *
     * @return 链接
     */
    public String getAppUrl() {
        return AppConfigNew.getAppUrl(context);
    }


    @Override
    protected void onBluetoothStatus(int statusType) {
        switch (statusType) {
            case BleStatusConstant.STATUS_PAIR_CODE_DIFFERENT:
//                context.startService(NetService.getCallingIntent(context, NetService.TYPE_UN_BIND));
                NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_UN_BIND));
                showToast(context.getString(R.string.activity_bracelet_pair_please_bond_again));
                break;
            case BleStatusConstant.STATUS_DISCONNECTED:
                cancelDialog();
                mHandler.postDelayed(reConnectBle, 1000);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onLinkStatusReceived(boolean isConnected) {
        if (!isConnected) {
            bleRequest.bluetoothLink();
        }
    }

    @Override
    protected void onStateChange(int bleState) {
        if (bleState == BleStateEvent.STATE_ON) {
            if (manager.isPaired()) {
                getLinkStatus();
            }
        }
    }


    @Override
    protected DefaultResponseCallback getResponseCallback() {
        return new DefaultResponseCallback();
    }

    /**
     * 连接手环
     */
    public void getLinkStatus() {
        Log.e(TAG, "main getLinkStatus:");
        this.bleRequest.bluetoothGetLinkStatus();
    }

    /**
     * 注销
     */
    public void unRegister() {
        this.bleRequest.bluetoothDisconnect();
    }

    /**
     * 是否需要有保存的数据
     *
     * @return true 有保存的数据
     */
    public boolean haveSavedData() {
        return RawDataFileUtil.haveData(context);
    }

    private Runnable reConnectBle = new Runnable() {
        @Override
        public void run() {
            if (ViewUtils.isScreenOn(context) && !background && isBleOn()) {
                getLinkStatus();
            }
        }
    };

    /**
     * 上传数据
     */
    public void sendSavedData() {
        Log.e(TAG, "sendSavedData: ");
        this.dataUseCase.sendOneSavedData(context, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean netResponseEntity) {
                if (netResponseEntity != null && netResponseEntity) {
                    if (haveSavedData()) {
                        sendSavedData();
                    }
                }
            }
        });
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        this.personalInfoUseCase.getInfo(NetConstant.CACHE_IF_NET_ERROR, SaveUtils.getUserId(context),
                new Subscriber<NetResponseEntity<PersonalInfoEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(NetResponseEntity<PersonalInfoEntity> netResponseEntity) {
                        Log.e(TAG, "get User: " + netResponseEntity);
                        if (netResponseEntity != null) {
                            if (netResponseEntity.getCode() == 0) {
                                PersonalInfoEntity entity = netResponseEntity.getData();
                                if (entity != null) {
                                    SaveUtils.savePersonalInfo(getApplication(), entity);
                                    UserInfo.saveLocalUserInfo(context, entity);
                                }
                            }
                        }
                    }
                });
    }
}
