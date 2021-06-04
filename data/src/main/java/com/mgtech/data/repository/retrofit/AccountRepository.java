package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.mgtech.data.net.retrofit.AccountApi;
import com.mgtech.data.net.retrofit.RetrofitFactory;
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
import com.mgtech.domain.entity.net.response.ResetPasswordResponseEntity;
import com.mgtech.domain.entity.net.response.WXGetInfoResponseEntity;
import com.mgtech.domain.entity.net.response.WXLoginResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.rx.ObservableLoader;
import com.mgtech.domain.utils.HttpApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Single;

/**
 *
 * @author zhaixiang
 * 登录注册
 */
public class AccountRepository implements NetRepository.Account {
    private AccountApi api;

    public AccountRepository(Context context) {
        this.api = new RetrofitFactory()
                .setBaseUrl(HttpApi.API_BASE_URL)
                .build(context).create(AccountApi.class);
    }

    @Override
    public Single<NetResponseEntity<LoginResponseEntity>> login(LoginByPhoneRequestEntity entity) {
        return api.login(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Single<NetResponseEntity<LoginResponseEntity>> loginByOther(LoginByOtherAppRequestEntity entity) {
        return api.loginByOtherApp(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Single<NetResponseEntity<BindAccountResponseEntity>> bindAccount(BindAccountRequestEntity entity) {
        return api.bindAccount(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<RegisterResponseEntity>> register(RegisterRequestEntity entity) {
        return api.register(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> verify(VerifyRequestEntity entity) {
        return api.verify(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> resetPassword(ResetPasswordRequestEntity entity) {
        return api.resetPassword(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> getVerificationCode(GetVerificationCodeEntity entity) {
        return api.getVerificationCode(entity.getPhone(),entity.getZone(),entity.getType()).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Single<NetResponseEntity<RefreshTokenResponse>> refreshToken(RefreshTokenRequestEntity entity) {
        return api.refreshToken(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Single<WXLoginResponseEntity> getWXToken(String appId, String secret, String code) {
        return new Retrofit.Builder()
                .baseUrl("https://api.weixin.qq.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(AccountApi.class).getWXToken(appId,secret,code,"authorization_code");
    }

    @Override
    public Single<WXGetInfoResponseEntity> getWXInfo(String token, String openId) {
        return new Retrofit.Builder()
                .baseUrl("https://api.weixin.qq.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(AccountApi.class).getWXInfo(token, openId);
    }

}
