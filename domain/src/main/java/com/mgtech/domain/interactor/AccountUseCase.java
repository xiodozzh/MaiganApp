package com.mgtech.domain.interactor;

import android.content.Context;

import com.mgtech.domain.entity.net.request.BindAccountRequestEntity;
import com.mgtech.domain.entity.net.request.GetVerificationCodeEntity;
import com.mgtech.domain.entity.net.request.LoginByOtherAppRequestEntity;
import com.mgtech.domain.entity.net.request.LoginByPhoneRequestEntity;
import com.mgtech.domain.entity.net.request.RefreshTokenRequestEntity;
import com.mgtech.domain.entity.net.request.RegisterRequestEntity;
import com.mgtech.domain.entity.net.request.ResetPasswordRequestEntity;
import com.mgtech.domain.entity.net.request.VerifyRequestEntity;
import com.mgtech.domain.entity.net.response.BindAccountResponseEntity;
import com.mgtech.domain.entity.net.response.LoginResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.RefreshTokenResponse;
import com.mgtech.domain.entity.net.response.RegisterResponseEntity;
import com.mgtech.domain.entity.net.response.WXGetInfoResponseEntity;
import com.mgtech.domain.entity.net.response.WXLoginResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.rx.ObservableLoader;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.orhanobut.logger.Logger;

import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 */
public class AccountUseCase {
    private NetRepository.Account repository;
    private Subscription loginSubscription;
    private Subscription loginWithIdentifySubscription;
    private Subscription registerSubscription;
    private Subscription verifySubscription;
    private Subscription resetPasswordSubscription;
    private Subscription getCodeSubscription;
    private Subscription getWXTokenSubscription;
    private ObservableLoader observableLoader;

    public AccountUseCase(NetRepository.Account repository) {
        this.repository = repository;
        this.observableLoader = new ObservableLoader();
    }

    /**
     * 登录
     *
     * @param entity   用户信息
     * @param subscriber 返回值订阅
     */
    public void login(LoginByPhoneRequestEntity entity, Subscriber<NetResponseEntity<LoginResponseEntity>> subscriber) {
        loginSubscription = observableLoader.observeMain(this.repository.login(entity))
                .subscribe(subscriber);
    }

    public Single<NetResponseEntity<LoginResponseEntity>> login(Context context,boolean isAuto) {
        int loginType = SaveUtils.getLoginType(context);
        if (loginType == NetConstant.APP_LOGIN){
            LoginByPhoneRequestEntity entity = new LoginByPhoneRequestEntity(SaveUtils.getPhone(context),
                    SaveUtils.getPassword(context),SaveUtils.getZone(context),Utils.getPhoneMac(context),isAuto);
            return repository.login(entity);
        }else if (loginType == NetConstant.QQ_LOGIN){
            LoginByOtherAppRequestEntity entity = new LoginByOtherAppRequestEntity(loginType,SaveUtils.getOpenIdQQ(context),
                    SaveUtils.getUnionIdQQ(context),SaveUtils.getTokenQQ(context),isAuto?1:0,Utils.getPhoneMac(context));
            return repository.loginByOther(entity);
        }else {
            LoginByOtherAppRequestEntity entity = new LoginByOtherAppRequestEntity(loginType,SaveUtils.getOpenIdWX(context),
                    SaveUtils.getUnionIdWX(context),SaveUtils.getTokenWX(context),isAuto?1:0,Utils.getPhoneMac(context));
            return repository.loginByOther(entity);
        }
    }

    public Single<NetResponseEntity<BindAccountResponseEntity>> bindAccount(BindAccountRequestEntity entity){
        return this.repository.bindAccount(entity);
    }

    public void refreshToken(){
        repository.refreshToken(new RefreshTokenRequestEntity(SaveUtils.getUserId(),SaveUtils.getRefreshToken()))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<NetResponseEntity<RefreshTokenResponse>>() {
                    @Override
                    public void onSuccess(NetResponseEntity<RefreshTokenResponse> netResponseEntity) {
                        RefreshTokenResponse refreshTokenResponse = netResponseEntity.getData();
                        Logger.e(refreshTokenResponse.toString());
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });
    }


    /**
     * 注册
     *
     * @param phone      手机号码
     * @param password   密码
     * @param sms        短信验证信息
     * @param zone       手机区域号
     * @param subscriber 返回值订阅
     */
    public void register(String phone, String password, String sms, String zone, Subscriber<NetResponseEntity<RegisterResponseEntity>> subscriber) {
        RegisterRequestEntity entity = new RegisterRequestEntity(phone, password, sms, zone);
        unSubscribeRegister();
        registerSubscription = this.repository.register(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 短信验证
     *
     * @param phone      手机号码
     * @param sms        短信验证信息
     * @param zone       手机区域号
     * @param subscriber 返回值订阅
     */
    public void verify(String phone, String sms, String zone, Subscriber<NetResponseEntity> subscriber) {
        VerifyRequestEntity entity = new VerifyRequestEntity(phone, sms, zone);
        unSubscribeLogin();
        verifySubscription = this.repository.verify(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 重置密码
     *
     * @param phone      手机号
     * @param password   密码
     * @param subscriber 订阅
     */
    public void resetPassword(String phone, String password,String zone,String verificationCode, Subscriber<NetResponseEntity> subscriber) {
        ResetPasswordRequestEntity entity = new ResetPasswordRequestEntity(phone, password,zone,verificationCode);
        resetPasswordSubscription = this.repository.resetPassword(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取校验码
     * @param entity 获取校验码
     * @param subscriber 订阅者
     */
    public void getVerificationCode(GetVerificationCodeEntity entity, Subscriber<NetResponseEntity>subscriber){
        unSubscribeGetCode();
        this.getCodeSubscription = this.repository.getVerificationCode(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getWXToken(String appId,String secret, String code, Subscriber<WXLoginResponseEntity>subscriber){
        unSubscribeGetWXToken();
        this.getWXTokenSubscription = this.repository.getWXToken(appId,secret,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public Single<WXLoginResponseEntity> getWXTokenObservable(String appId, String secret, String code){
        return this.repository.getWXToken(appId,secret,code);
    }

    public Single<WXGetInfoResponseEntity> getWXInfoObservable(String token, String openId){
        return this.repository.getWXInfo(token,openId);
    }

    public Single<NetResponseEntity<LoginResponseEntity>> getLoginByOtherAppObservable(LoginByOtherAppRequestEntity entity){
        return this.repository.loginByOther(entity);
    }

    public Single<NetResponseEntity<RefreshTokenResponse>> refreshToken(RefreshTokenRequestEntity entity){
        return this.repository.refreshToken(entity);
    }


    /**
     * 解除subscriber
     */
    public void unSubscribe() {
        unSubscribeLogin();
        unSubscribeRegister();
        unSubscribeGetCode();
        unSubscribeGetWXToken();
        if (verifySubscription != null && !verifySubscription.isUnsubscribed()) {
            verifySubscription.unsubscribe();
        }
        if (loginWithIdentifySubscription != null && !loginWithIdentifySubscription.isUnsubscribed()) {
            loginWithIdentifySubscription.unsubscribe();
        }
        if (resetPasswordSubscription != null && !resetPasswordSubscription.isUnsubscribed()) {
            resetPasswordSubscription.unsubscribe();
        }
    }

    private void unSubscribeRegister(){
        if (registerSubscription != null && !registerSubscription.isUnsubscribed()) {
            registerSubscription.unsubscribe();
        }
    }

    private void unSubscribeLogin(){
        if (loginSubscription != null && !loginSubscription.isUnsubscribed()) {
            loginSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetCode(){
        if (getCodeSubscription != null && !getCodeSubscription.isUnsubscribed()) {
            getCodeSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetWXToken(){
        if (getWXTokenSubscription != null && !getWXTokenSubscription.isUnsubscribed()) {
            getWXTokenSubscription.unsubscribe();
        }
    }


}
