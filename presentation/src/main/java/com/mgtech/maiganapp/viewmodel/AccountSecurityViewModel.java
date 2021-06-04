package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.request.BindAccountRequestEntity;
import com.mgtech.domain.entity.net.response.BindAccountResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.AccountUseCase;
import com.mgtech.domain.interactor.PersonalInfoUseCase;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Jesse
 */
public class AccountSecurityViewModel extends BaseViewModel {
    public MutableLiveData<String> qq = new MutableLiveData<>();
    public MutableLiveData<String> wechat = new MutableLiveData<>();
    public MutableLiveData<String> phone = new MutableLiveData<>();
    public MutableLiveData<String> zone = new MutableLiveData<>();
    public ObservableBoolean loading = new ObservableBoolean(false);
    private AccountUseCase accountUseCase;
    private PersonalInfoUseCase useCase;

    public AccountSecurityViewModel(@NonNull Application application) {
        super(application);
        useCase = ((MyApplication) application).getPersonInfoUseCase();
        accountUseCase = ((MyApplication) application).getAccountUseCase();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
        accountUseCase.unSubscribe();
    }

    public void initPersonalInfo() {
        String wxName = SaveUtils.getWeChatName();
        String qqName = SaveUtils.getQQName();
        if (TextUtils.isEmpty(wxName)) {
            wxName = getApplication().getString(R.string.not_bond);
        }
        wechat.setValue(wxName);
        if (TextUtils.isEmpty(qqName)) {
            qqName = getApplication().getString(R.string.not_bond);
        }
        qq.setValue(qqName);
        phone.setValue(SaveUtils.getPhone(getApplication()));
        zone.setValue(SaveUtils.getZone(getApplication()));
    }

    /**
     * 绑定微信账号
     *
     * @param token ShareSDK 的Token
     *              {country=中国, unionid=, province=上海, city=黄浦, openid=, sex=1, nickname=六一居士Jesse,
     *              headimgurl=, userTags=, language=zh_CN, privilege=[]}
     */
    public void bindWeChat(String openId, String unionId, String token, String nickName, String avatarUrl) {
        BindAccountRequestEntity entity = new BindAccountRequestEntity();
        entity.setAccessToken(token);
        entity.setAvatarUrl(avatarUrl);
        entity.setNickName(nickName);
        entity.setLoginType(SaveUtils.getLoginType(getApplication()));
        entity.setOpenId(openId);
        entity.setUnionId(unionId);
        entity.setAccountGuid(SaveUtils.getUserId(getApplication()));
        entity.setBindAccountType(NetConstant.WE_CHAT_LOGIN);
        entity.setPhone(SaveUtils.getPhone(getApplication()));
        entity.setPassword(SaveUtils.getPassword(getApplication()));
        entity.setZone(SaveUtils.getZone(getApplication()));
        bindAccount(entity);
    }

    /**
     * 绑定微信账号
     *
     * @param openId  openId
     * @param unionId unionId
     *                {ret=0, msg=, is_lost=0, gender=女, is_yellow_vip=0, city=黄浦, year=1988, level=0, figureurl_2=,
     *                figureurl_1=, is_yellow_year_vip=0, province=上海, constellation=, figureurl=,
     *                figureurl_type=0, figureurl_qq=, nickname=脉迹mystrace, yellow_vip_level=0, userTags=,
     *                figureurl_qq_1=, vip=0, figureurl_qq_2=}
     **/
    public void bindQQ(String openId, String unionId, String token, String name, String avatarUrl) {
        BindAccountRequestEntity entity = new BindAccountRequestEntity();
        entity.setAccessToken(token);
        entity.setAvatarUrl(avatarUrl);
        entity.setNickName(name);
        entity.setLoginType(SaveUtils.getLoginType(getApplication()));
        entity.setOpenId(openId);
        entity.setUnionId(unionId);
        entity.setAccountGuid(SaveUtils.getUserId(getApplication()));
        entity.setBindAccountType(NetConstant.QQ_LOGIN);
        entity.setPhone(SaveUtils.getPhone(getApplication()));
        entity.setPassword(SaveUtils.getPassword(getApplication()));
        entity.setZone(SaveUtils.getZone(getApplication()));
        bindAccount(entity);
    }

    public void bindAccount(BindAccountRequestEntity entity) {
        loading.set(true);
        accountUseCase.bindAccount(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NetResponseEntity<BindAccountResponseEntity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        loading.set(false);
                        showToast(getApplication().getString(R.string.network_error));
                    }

                    @Override
                    public void onNext(NetResponseEntity<BindAccountResponseEntity> netResponseEntity) {
                        loading.set(false);
                        if (netResponseEntity.getCode() == 0) {
                            showToast(getApplication().getString(R.string.bind_wechat_or_qq_success));
                            getInfo();
                        } else {
                            showToast(netResponseEntity.getMessage());
                        }
                    }
                });
    }

    private void getInfo() {
        loading.set(true);
        this.useCase.getInfo(NetConstant.NO_CACHE, SaveUtils.getUserId(getApplication()),
                new Subscriber<NetResponseEntity<PersonalInfoEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        loading.set(false);
                    }

                    @Override
                    public void onNext(NetResponseEntity<PersonalInfoEntity> netResponseEntity) {
                        if (netResponseEntity.getCode() == 0) {
                            PersonalInfoEntity entity = netResponseEntity.getData();
                            if (entity != null) {
                                SaveUtils.savePersonalInfo(getApplication(), entity);
                                UserInfo.saveLocalUserInfo(getApplication(), entity);
//                                initViewList();
                                initPersonalInfo();
                            }
                        } else {
                            showToast(netResponseEntity.getMessage());
                        }
                        loading.set(false);
                    }
                });
    }
}
