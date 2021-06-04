package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.request.LoginByOtherAppRequestEntity;
import com.mgtech.domain.entity.net.request.LoginByPhoneRequestEntity;
import com.mgtech.domain.entity.net.response.LoginResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.AccountUseCase;
import com.mgtech.domain.interactor.PersonalInfoUseCase;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
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
 * 登录view-model
 */

public class LoginViewModel extends BaseViewModel {
    public final ObservableBoolean toHomePage = new ObservableBoolean(false);
    public final ObservableBoolean toSetPage = new ObservableBoolean(false);
    public final ObservableBoolean valid = new ObservableBoolean(false);
    public final ObservableBoolean loading = new ObservableBoolean(false);

    public final ObservableBoolean qqLogining = new ObservableBoolean(false);
    public final ObservableBoolean weChatLogining = new ObservableBoolean(false);

    private AccountUseCase useCase;
    private PersonalInfoUseCase personalInfoUseCase;

    public LoginViewModel(Application context) {
        super(context);
        this.useCase = ((MyApplication) context).getAccountUseCase();
        this.personalInfoUseCase = ((MyApplication) context).getPersonInfoUseCase();
    }

    public void login(@NonNull String phone, @NonNull String password, @NonNull String country) {
        final String phoneTrim = phone.trim();
        final String zone = country.replaceAll("\\+", "");
        final String passwordTrim = password.trim();
        if (isPhoneAndPasswordValid(phone, passwordTrim)) {
            showDialog(getApplication().getString(R.string.logining), getApplication().getString(R.string.please_wait));
            LoginByPhoneRequestEntity entity = new LoginByPhoneRequestEntity(
                    phone, passwordTrim, zone, Utils.getPhoneMac(getApplication()), false);
            loading.set(true);
            this.useCase.login(entity, new Subscriber<NetResponseEntity<LoginResponseEntity>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    showToast(getApplication().getString(R.string.network_error));
                    cancelDialog();
                    e.printStackTrace();
                    loading.set(false);
                }

                @Override
                public void onNext(NetResponseEntity<LoginResponseEntity> netResponseEntity) {
                    if (netResponseEntity != null) {
                        if (netResponseEntity.getCode() == 0) {
                            LoginResponseEntity loginResponseEntity = netResponseEntity.getData();
                            PersonalInfoEntity userInfoResponseEntity = loginResponseEntity.getUserInfo();
                            if (loginResponseEntity.getAccessToken() != null && userInfoResponseEntity != null) {
                                SaveUtils.saveAccount(getApplication(), userInfoResponseEntity.getAccountGuid(), phoneTrim,
                                        zone, passwordTrim,
                                        loginResponseEntity.getAccessToken(), loginResponseEntity.getRefreshToken(), loginResponseEntity.getExpiresTime(),
                                        NetConstant.APP_LOGIN);
                                loginSuccessSet();
                                UserInfo userInfo = UserInfo.saveLocalUserInfo(getApplication(), userInfoResponseEntity);
                                if (userInfo.isComplete()) {
                                    cancelDialog();
                                    toHomePage.set(!toHomePage.get());
                                } else {
                                    cancelDialog();
                                    toSetPage.set(!toSetPage.get());
                                }
                            } else {
                                cancelDialog();
                                toSetPage.set(!toSetPage.get());
                            }
                            EventBus.getDefault().postSticky(new SocketEvent(true));
                        } else {
                            showToast(netResponseEntity.getMessage());
                        }
                    } else {
                        Log.e(TAG, "注册结果null");
                        showToast(getApplication().getString(R.string.network_error));
                    }
                    cancelDialog();
                    loading.set(false);
                }
            });
        }
    }

    private void loginSuccessSet() {
        ((MyApplication) getApplication()).openPush();
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_BIND));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_PARAMETERS));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_SAVE_PUSH_ID));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_CONFIG));
        NetJobService.enqueueWork(getApplication(), NetJobService.getCallingIntent(getApplication(), NetJobService.TYPE_SET_CUSTOMER_CONTACT_PERMISSION));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_FIRST_AID_PHONE));
    }

    public void loginByQQ(String openId, String unionId, String token) {
        LoginByOtherAppRequestEntity entity = new LoginByOtherAppRequestEntity();
        entity.setOpenId(entity.getOpenId());
        if (!TextUtils.isEmpty(unionId)) {
            entity.setUnionId(unionId);
        }
        entity.setLoginType(NetConstant.QQ_LOGIN);
        entity.setDeviceId(Utils.getPhoneMac(getApplication()));
        entity.setIsAuto(0);
        entity.setOpenId(openId);
        entity.setAccessToken(token);
        loginByOtherApp(entity, NetConstant.QQ_LOGIN);
    }

    public void loginByWeChat(String openId, String unionId, String token) {
        LoginByOtherAppRequestEntity entity = new LoginByOtherAppRequestEntity();
        entity.setAccessToken(token);
        entity.setIsAuto(0);
        entity.setDeviceId(Utils.getPhoneMac(getApplication()));
        entity.setLoginType(NetConstant.WE_CHAT_LOGIN);
        entity.setOpenId(openId);
        entity.setUnionId(unionId);
        loginByOtherApp(entity, NetConstant.WE_CHAT_LOGIN);
    }

    private void loginByOtherApp(LoginByOtherAppRequestEntity entity, final int otherType) {
        loading.set(true);
        useCase.getLoginByOtherAppObservable(entity)
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
                        loading.set(false);
                    }

                    @Override
                    public void onNext(NetResponseEntity<LoginResponseEntity> netResponseEntity) {
                        if (netResponseEntity.getCode() == 0) {
                            LoginResponseEntity loginResponseEntity = netResponseEntity.getData();
                            if (loginResponseEntity != null) {
                                PersonalInfoEntity personalInfoEntity = loginResponseEntity.getUserInfo();
                                if (personalInfoEntity != null) {
                                    SaveUtils.saveAccount(getApplication(),
                                            personalInfoEntity.getAccountGuid(),
                                            personalInfoEntity.getPhone(),
                                            personalInfoEntity.getZone(),
                                            "",
                                            loginResponseEntity.getAccessToken(),
                                            loginResponseEntity.getRefreshToken(),
                                            loginResponseEntity.getExpiresTime(),
                                            otherType);
                                    loginSuccessSet();
                                    UserInfo userInfo = UserInfo.saveLocalUserInfo(getApplication(), personalInfoEntity);
                                    if (userInfo.isComplete()) {
                                        cancelDialog();
                                        toHomePage.set(!toHomePage.get());
                                    } else {
                                        cancelDialog();
                                        toSetPage.set(!toSetPage.get());
                                    }
                                    EventBus.getDefault().postSticky(new SocketEvent(true));

                                } else {
                                    cancelDialog();
                                    toSetPage.set(!toSetPage.get());
                                }
                            } else {
                                showToast(getApplication().getString(R.string.network_error));
                            }
                        } else {
                            showToast(netResponseEntity.getMessage());
                        }
                        loading.set(false);
                    }
                });
    }

    private boolean isPhoneAndPasswordValid(String phone, String password) {
        boolean isCorrect = true;
        if (phone.isEmpty() && password.isEmpty()) {
            isCorrect = false;
            showToast(getApplication().getString(R.string.phone_password_is_empty));
        } else if (phone.isEmpty()) {
            isCorrect = false;
            showToast(getApplication().getString(R.string.phone_is_empty));
        } else if (password.isEmpty()) {
            isCorrect = false;
            showToast(getApplication().getString(R.string.password_is_empty));
        } else if (password.length() < 6) {
            isCorrect = false;
            showToast(getApplication().getString(R.string.password_is_too_short));
        }
        return isCorrect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.useCase.unSubscribe();
        this.personalInfoUseCase.unSubscribe();
    }
}
