package com.mgtech.maiganapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.entity.AppConfigNew;
import com.mgtech.domain.entity.IndicatorDescription;
import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.request.GetBraceletConfigRequestEntity;
import com.mgtech.domain.entity.net.request.GetDeviceBindInfoRequestEntity;
import com.mgtech.domain.entity.net.request.SaveMobileInfoRequestEntity;
import com.mgtech.domain.entity.net.request.SavePushIdRequestEntity;
import com.mgtech.domain.entity.net.request.SetBraceletInfoRequestEntity;
import com.mgtech.domain.entity.net.request.UnbindBraceletRequestEntity;
import com.mgtech.domain.entity.net.response.BraceletConfigEntity;
import com.mgtech.domain.entity.net.response.GetAppConfigResponseEntity;
import com.mgtech.domain.entity.net.response.GetBindInfoResponseEntity;
import com.mgtech.domain.entity.net.response.IndicatorDescriptionResponseEntity;
import com.mgtech.domain.entity.net.response.LoginResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.AccountUseCase;
import com.mgtech.domain.interactor.AppConfigUseCase;
import com.mgtech.domain.interactor.DataUseCase;
import com.mgtech.domain.interactor.DeviceUseCase;
import com.mgtech.domain.interactor.NotificationUseCase;
import com.mgtech.domain.interactor.PersonalInfoUseCase;
import com.mgtech.domain.interactor.SingleSignOnUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.NetUtils;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.data.event.BindInfoUpdate;
import com.mgtech.maiganapp.data.event.LogoutEvent;
import com.mgtech.maiganapp.data.event.SocketEvent;
import com.mgtech.maiganapp.data.wrapper.BraceletConfigWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.Nullable;

import cn.jpush.android.api.JPushInterface;
import rx.Subscriber;

/**
 * @author zhaixiang
 * 网络通讯service
 */

public class NetService extends IntentService {
    private DeviceUseCase deviceUseCase;
    private AccountUseCase accountUseCase;
    private NotificationUseCase notificationUseCase;
    private AppConfigUseCase appConfigUseCase;
    private PersonalInfoUseCase personalInfoUseCase;
    private SingleSignOnUseCase singleSignOnUseCase;
    private DataUseCase dataUseCase;

    private static final String TYPE = "type";
    public static final int TYPE_LOGIN = 1;
    public static final int TYPE_GET_BIND = 2;
    public static final int TYPE_GET_PARAMETERS = 3;
    public static final int TYPE_SAVE_DISPLAY = 4;
    public static final int TYPE_SAVE_REMINDERS = 5;
    public static final int TYPE_LOGIN_FORCE = 6;
    public static final int TYPE_UN_BIND = 7;
    public static final int TYPE_SET_BRACELET_INFO = 8;
    public static final int TYPE_SAVE_PUSH_ID = 9;
    public static final int TYPE_GET_PERSONAL_INFO = 10;
    public static final int TYPE_GET_CONFIG = 11;
    public static final int TYPE_SAVE_PHONE_INFO = 12;
    public static final int TYPE_INDICATOR_PARAMETERS = 13;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NetService(String name) {
        super(name);
    }

    public NetService() {
        super("test");
    }

    public static Intent getCallingIntent(Context context, int type) {
        Intent intent = new Intent(context, NetService.class);
        intent.putExtra(TYPE, type);
        return intent;
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        Log.i("NetService", "onHandleIntent: " + intent.getIntExtra(TYPE, 0));
        switch (intent.getIntExtra(TYPE, 0)) {
            case TYPE_LOGIN:
                login(true);
                break;
            case TYPE_LOGIN_FORCE:
                login(false);
                break;
            case TYPE_GET_BIND:
                getBondInfo();
                break;
            case TYPE_SAVE_DISPLAY:
            case TYPE_SAVE_REMINDERS:
                saveBraceletParameters();
                break;
            case TYPE_GET_PARAMETERS:
                getBraceletParameter();
                break;
            case TYPE_UN_BIND:
                unBind();
                break;
            case TYPE_SET_BRACELET_INFO:
                setFirmwareVersion();
                break;
            case TYPE_SAVE_PUSH_ID:
                savePushId();
                break;
            case TYPE_GET_PERSONAL_INFO:
                getPersonalInfo();
                break;
            case TYPE_GET_CONFIG:
                getConfig();
                break;
            case TYPE_SAVE_PHONE_INFO:
                savePhoneInfo();
                break;
            case TYPE_INDICATOR_PARAMETERS:
                getIndicatorDescription();
                break;
            default:
        }
    }

    @Override
    public void onCreate() {
        deviceUseCase = ((MyApplication) getApplication()).getDeviceUseCase();
        accountUseCase = ((MyApplication) getApplication()).getAccountUseCase();
        appConfigUseCase = ((MyApplication) getApplication()).getAppConfigUseCase();
        personalInfoUseCase = ((MyApplication) getApplication()).getPersonInfoUseCase();
        singleSignOnUseCase = ((MyApplication) getApplication()).getSingleSignOnUseCase();
        notificationUseCase = ((MyApplication) getApplication()).getNotificationUseCase();
        dataUseCase = ((MyApplication) getApplication()).getDataUseCase();
        super.onCreate();
    }

    private void getBondInfo() {
        GetDeviceBindInfoRequestEntity entity = new GetDeviceBindInfoRequestEntity(MyConstant.BRACELET_TYPE,
                SaveUtils.getUserId(getApplication()));
        deviceUseCase.getBracelet(entity, true, new Subscriber<NetResponseEntity<GetBindInfoResponseEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(NetResponseEntity<GetBindInfoResponseEntity> netResponseEntity) {
                if (netResponseEntity.getCode() == 0) {
                    GetBindInfoResponseEntity e = netResponseEntity.getData();
                    IBraceletInfoManager manager = new BraceletInfoManagerBuilder(NetService.this).create();
                    if (e == null) {
                        manager.deletePairInformation();
                        return;
                    }
                    if (e.getBindTime() != 0) {
                        manager.savePairInformation(e.getBraceletName(), e.getMacAddress().toUpperCase(),
                                e.getPairingCode(), NetUtils.hexStringToBytes(e.getEncryptionKey()),
                                NetUtils.hexStringToBytes(e.getEncryptionVector()), "", e.getBindTime(),
                                "", e.getFirmwareVersion());
                        EventBus.getDefault().post(new BindInfoUpdate());
                    } else {
                        manager.deletePairInformation();
                    }
                }
            }
        });
    }

    private void setFirmwareVersion() {
        IBraceletInfoManager manager = new BraceletInfoManagerBuilder(this).create();
        this.deviceUseCase.setInfo(new SetBraceletInfoRequestEntity(manager.getAddress(), manager.getDeviceFirmwareVersionString(),
                        manager.getDeviceName()), true,
                new Subscriber<NetResponseEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NetResponseEntity netResponseEntity) {

                    }
                });
    }

    /**
     * 获取手环配置信息
     */
    private void getBraceletParameter() {
        GetBraceletConfigRequestEntity entity = new GetBraceletConfigRequestEntity(MyConstant.BRACELET_TYPE,
                SaveUtils.getUserId(getApplication()));
        if (SaveUtils.isParamRemindersUpdate(this) || SaveUtils.isParam5Update(this)) {
            return;
        }
        deviceUseCase.getConfig(entity, true, new Subscriber<NetResponseEntity<BraceletConfigEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(NetResponseEntity<BraceletConfigEntity> netResponseEntity) {
                if (netResponseEntity.getCode() == 0) {
                    BraceletConfigEntity e = netResponseEntity.getData();
                    Log.e("ttttt", "onNext: " + e);
                    if (e == null) {
                        return;
                    }
                    IBraceletInfoManager manager = new BraceletInfoManagerBuilder(getApplicationContext()).create();
                    manager.setDisplayPage(BraceletConfigWrapper.getDisplayPageFromNet(e));
                    manager.setReminder(BraceletConfigWrapper.getRemindersFromEntity(e));
                }
            }
        });
    }

    private void saveBraceletParameters() {
        if (!SaveUtils.isParamRemindersUpdate(this) && !SaveUtils.isParam5Update(this)) {
            return;
        }
        IBraceletInfoManager manager = new BraceletInfoManagerBuilder(this).create();
        BraceletConfigEntity entity = BraceletConfigWrapper.paramDataToNetEntity(
                manager.getReminderData(),
                manager.getDisplayPage());
        entity.setAccountGuid(SaveUtils.getUserId());
        this.deviceUseCase.setConfig(entity, true, new Subscriber<NetResponseEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(NetResponseEntity netResponseEntity) {
                if (netResponseEntity != null && netResponseEntity.getCode() == 0) {
                    SaveUtils.setRemindersNeedUpdate(getApplicationContext(), false);
                    SaveUtils.setParamDisplayUpdate(getApplicationContext(), false);
                }
            }
        });
    }

    private void unBind() {
        IBraceletInfoManager manager = new BraceletInfoManagerBuilder(getApplicationContext()).create();
        UnbindBraceletRequestEntity entity = new UnbindBraceletRequestEntity(manager.getDeleteMac(),
                SaveUtils.getUserId(getApplication()));
        this.deviceUseCase.unbindInOtherThread(entity, new Subscriber<NetResponseEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(NetResponseEntity netResponseEntity) {
                Log.i("unBind", "onNext: " + netResponseEntity);
                if (netResponseEntity != null && netResponseEntity.getCode() == 0) {
                    IBraceletInfoManager manager = new BraceletInfoManagerBuilder(getApplicationContext()).create();
                    manager.deletePairInformation();
                }
            }
        });
    }


    /**
     * 自动登陆目的是查看是否有多点登录
     */
    private void login(final boolean isAuto) {
        this.accountUseCase.login(this.getApplication(), isAuto)
                .subscribe(new Subscriber<NetResponseEntity<LoginResponseEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (!isAuto) {
                            loginFail();
                        }
                    }

                    @Override
                    public void onNext(NetResponseEntity<LoginResponseEntity> netResponseEntity) {
                        if (netResponseEntity != null) {
                            if (netResponseEntity.getCode() != 0) {
                                loginFail();
                            } else {
                                Log.i("logout", "login auto " + isAuto);
                                LoginResponseEntity loginResponseEntity = netResponseEntity.getData();
                                if (loginResponseEntity != null) {
                                    if (!loginResponseEntity.isLoginOtherDevice()) {
                                        Log.i("logout", "onNext: " + loginResponseEntity);
                                        SaveUtils.saveToken(loginResponseEntity.getAccessToken(),
                                                loginResponseEntity.getRefreshToken(), loginResponseEntity.getExpiresTime());
                                        getBondInfo();
                                        if (!isAuto) {
                                            savePushId();
                                        }
//                                        BluetoothService2.startServiceWithBleScan(NetService.this);
                                    } else {
                                        loginFail();
                                    }
                                }
                                EventBus.getDefault().postSticky(new SocketEvent(true));
                            }
                        }
                    }
                });
    }

    private void getPersonalInfo() {
        this.personalInfoUseCase.getInfo(NetConstant.NO_CACHE, SaveUtils.getUserId(getApplication()),
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
                        if (netResponseEntity != null) {
                            if (netResponseEntity.getCode() == 0) {
                                PersonalInfoEntity entity = netResponseEntity.getData();
                                if (entity != null) {
                                    SaveUtils.savePersonalInfo(getApplication(), entity);
                                    UserInfo.saveLocalUserInfo(getApplication(), entity);
                                }
                            }
                        }
                    }
                });
    }

    private void getConfig() {
        this.appConfigUseCase.getAppConfigInService(new Subscriber<NetResponseEntity<GetAppConfigResponseEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(NetResponseEntity<GetAppConfigResponseEntity> netResponseEntity) {
                if (netResponseEntity != null && netResponseEntity.getCode() == 0) {
                    GetAppConfigResponseEntity entity = netResponseEntity.getData();
                    if (entity != null) {
                        AppConfigNew.saveToLocal(getApplication(), entity);
                    }
                }
            }
        });
    }

    private void savePhoneInfo() {
        this.singleSignOnUseCase.savePhoneInfoInService(new SaveMobileInfoRequestEntity(Utils.getPhoneMac(getApplication()),
                Build.MODEL), new Subscriber<NetResponseEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NetResponseEntity netResponseEntity) {

            }
        });
    }

    private void getIndicatorDescription() {
        dataUseCase.getIndicatorDescription(NetConstant.NO_CACHE, true
                , new Subscriber<NetResponseEntity<List<IndicatorDescriptionResponseEntity>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(NetResponseEntity<List<IndicatorDescriptionResponseEntity>> listNetResponseEntity) {
                        if (listNetResponseEntity != null && listNetResponseEntity.getCode() == 0) {
                            List<IndicatorDescriptionResponseEntity> list = listNetResponseEntity.getData();
                            if (list != null) {
                                IndicatorDescription.saveToLocal(list);
                            }
                        }
                    }
                });
    }

    private void savePushId() {
        String id = SaveUtils.getUserId(this);
        if (TextUtils.isEmpty(id)) {
            return;
        }
        notificationUseCase.savePushIdInService(new SavePushIdRequestEntity(id, JPushInterface.getRegistrationID(this)));
    }

    private void loginFail() {
        EventBus.getDefault().postSticky(new LogoutEvent());
    }

    @Override
    public void onDestroy() {
        deviceUseCase.unSubscribe();
        accountUseCase.unSubscribe();
        appConfigUseCase.unSubscribe();
        personalInfoUseCase.unSubscribe();
        singleSignOnUseCase.unSubscribe();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
