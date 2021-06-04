package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.request.GetDeviceBindInfoRequestEntity;
import com.mgtech.domain.entity.net.response.GetBindInfoResponseEntity;
import com.mgtech.domain.entity.net.response.LoginResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.AccountUseCase;
import com.mgtech.domain.interactor.DeviceUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetUtils;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.SocketEvent;
import com.mgtech.maiganapp.service.NetJobService;

import org.greenrobot.eventbus.EventBus;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 * view-model 抽象类
 */

public abstract class BaseViewModel extends AndroidViewModel {
    protected static final String TAG = "viewModel";
//    public ObservableBoolean toastField = new ObservableBoolean(false);
    public MutableLiveData<String> toastField = new MutableLiveData<>();
    public ObservableBoolean showDialogField = new ObservableBoolean(false);
    public ObservableBoolean reLoginFail = new ObservableBoolean(false);
    public ObservableBoolean reLoginNetworkError = new ObservableBoolean(false);
    public String dialogText = "";
    public String dialogTitle = "";
    private AccountUseCase accountUseCase;
    private DeviceUseCase deviceUseCase;
    private IBraceletInfoManager manager;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        manager = new BraceletInfoManagerBuilder(application).create();
    }

    /**
     * 显示 Toast
     *
     * @param text 显示的string
     */
    public void showToast(String text) {
        this.toastField.setValue(text);
    }

    public void getUnreadMessage(){
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_UNREAD_MESSAGE));
    }

    /**
     * 是否绑定手环
     *
     * @return true绑定
     */
    public boolean isPaired() {
        return manager.isPaired();
    }

    public void reLogin() {
        if (accountUseCase == null) {
            this.accountUseCase = ((MyApplication) getApplication()).getAccountUseCase();
        }
        this.accountUseCase.login(this.getApplication(), false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NetResponseEntity<LoginResponseEntity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showToast(getApplication().getString(R.string.network_error));
                        reLoginNetworkError.set(!reLoginNetworkError.get());
                    }

                    @Override
                    public void onNext(NetResponseEntity<LoginResponseEntity> netResponseEntity) {
                        if (netResponseEntity != null) {
                            if (netResponseEntity.getCode() != 0) {
                                showToast(netResponseEntity.getMessage());
                                reLoginFail.set(!reLoginFail.get());
                            } else {
                                LoginResponseEntity loginResponseEntity = netResponseEntity.getData();
                                if (loginResponseEntity != null) {
                                    Log.i("logout", "onNext: " + loginResponseEntity);
                                    SaveUtils.saveToken( loginResponseEntity.getAccessToken(),
                                            loginResponseEntity.getRefreshToken(),loginResponseEntity.getExpiresTime());
                                    PersonalInfoEntity personalInfoEntity = loginResponseEntity.getUserInfo();
                                    int loginType = SaveUtils.getLoginType(getApplication());
                                    if (personalInfoEntity != null) {
                                        SaveUtils.saveAccount(getApplication(), personalInfoEntity.getAccountGuid(),
                                                personalInfoEntity.getPhone(),
                                                personalInfoEntity.getZone(), "",
                                                loginResponseEntity.getAccessToken(),
                                                loginResponseEntity.getRefreshToken(),
                                                loginResponseEntity.getExpiresTime(),
                                                loginType);
                                        loginInit(getApplication());
                                        EventBus.getDefault().postSticky(new SocketEvent(true));
                                    } else {
                                        reLoginFail.set(!reLoginFail.get());
                                    }
                                } else {
                                    reLoginFail.set(!reLoginFail.get());
                                }
                            }
                        }
                    }
                });
    }

    @Override
    protected void onCleared() {
        if (accountUseCase != null){
            accountUseCase.unSubscribe();
        }
        super.onCleared();
    }

    private void loginInit(Context context) {
        ((MyApplication)getApplication()).openPush();
//        context.startService(NetService.getCallingIntent(context, NetService.TYPE_GET_BIND));
//        context.startService(NetService.getCallingIntent(context, NetService.TYPE_GET_PARAMETERS));
//        context.startService(NetService.getCallingIntent(context, NetService.TYPE_GET_CONFIG));
//        context.startService(NetService.getCallingIntent(context, NetService.TYPE_SAVE_PUSH_ID));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_BIND));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_PARAMETERS));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_CONFIG));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_SAVE_PUSH_ID));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_FIRST_AID_PHONE));

    }

    private void getBondInfo() {
        final Context context = getApplication();
        if (deviceUseCase == null) {
            deviceUseCase = ((MyApplication) context).getDeviceUseCase();
        }
        GetDeviceBindInfoRequestEntity entity = new GetDeviceBindInfoRequestEntity(MyConstant.BRACELET_TYPE,
                SaveUtils.getUserId(getApplication()));
        deviceUseCase.getBracelet(entity, false, new Subscriber<NetResponseEntity<GetBindInfoResponseEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(NetResponseEntity<GetBindInfoResponseEntity> netResponseEntity) {
                Log.e("ttt", "onNext: " + netResponseEntity.getData().toString());
                if (netResponseEntity.getCode() == 0) {
                    GetBindInfoResponseEntity e = netResponseEntity.getData();
                    IBraceletInfoManager manager = new BraceletInfoManagerBuilder(context).create();
                    if (e == null) {
                        manager.deletePairInformation();
                        return;
                    }
                    if (e.getBindTime() != 0) {
                        manager.setPairStatus(true);
                        manager.savePairInformation(e.getBraceletName(), e.getMacAddress().toUpperCase(),
                                e.getPairingCode(), NetUtils.hexStringToBytes(e.getEncryptionKey()),
                                NetUtils.hexStringToBytes(e.getEncryptionVector()), "", e.getBindTime(),
                                "", e.getFirmwareVersion());
                    } else {
                        manager.deletePairInformation();
                    }
                }
            }
        });
    }



    /**
     * 显示进度
     *
     * @param dialogText 文字说明
     */
    public final void showDialog(String dialogTitle, String dialogText) {
        this.dialogText = dialogText;
        this.dialogTitle = dialogTitle;
        this.showDialogField.set(true);
    }

    /**
     * 取消显示进度
     */
    public final void cancelDialog() {
        this.showDialogField.set(false);
    }



}
