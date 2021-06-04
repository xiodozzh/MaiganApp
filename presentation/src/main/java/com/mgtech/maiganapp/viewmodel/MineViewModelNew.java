package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.entity.UnreadMessage;
import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.request.BindAccountRequestEntity;
import com.mgtech.domain.entity.net.response.BindAccountResponseEntity;
import com.mgtech.domain.entity.net.response.BoughtServiceResponseEntity;
import com.mgtech.domain.entity.net.response.CheckServiceAuthorityResponseEntity;
import com.mgtech.domain.entity.net.response.GetServiceAuthCodeResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.ServiceCompanyResponseEntity;
import com.mgtech.domain.interactor.AccountUseCase;
import com.mgtech.domain.interactor.PersonalInfoUseCase;
import com.mgtech.domain.interactor.ServeUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.CompanyModel;
import com.mgtech.maiganapp.data.model.MineCardModel;
import com.mgtech.maiganapp.data.model.ServiceModel;
import com.mgtech.maiganapp.data.wrapper.CompanyWrapper;
import com.mgtech.maiganapp.data.wrapper.ServiceModelWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 */
public class MineViewModelNew extends BaseFragmentViewModel {
    public MutableLiveData<List<MineCardModel>> dataList = new MutableLiveData<>();
    public MutableLiveData<List<MineCardModel>> toolList = new MutableLiveData<>();
    public MutableLiveData<List<CompanyModel>> companyList = new MutableLiveData<>();
    public MutableLiveData<CompanyModel> needAuthCompany = new MutableLiveData<>();
    public MutableLiveData<String> pageUrl = new MutableLiveData<>();
    public MutableLiveData<String> waitingString = new MutableLiveData<>();
    public MutableLiveData<List<ServiceModel>> boughtServiceList = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> qq = new MutableLiveData<>();
    public MutableLiveData<String> wechat = new MutableLiveData<>();
    public MutableLiveData<String> avatar = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPaired = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> syncTime = new MutableLiveData<>();
    //    public ObservableBoolean loading = new ObservableBoolean(false);
    private IBraceletInfoManager manager;
    private PersonalInfoUseCase useCase;
    private AccountUseCase accountUseCase;
    private ServeUseCase serveUseCase;

    public MineViewModelNew(@NonNull Application application) {
        super(application);
        manager = new BraceletInfoManagerBuilder(application).create();
        useCase = ((MyApplication) application).getPersonInfoUseCase();
        accountUseCase = ((MyApplication) application).getAccountUseCase();
        serveUseCase = ((MyApplication) application).getServeUseCase();
    }

    public boolean isMan() {
        UserInfo userInfo = UserInfo.getLocalUserInfo(getApplication());
        return userInfo.getSex() == MyConstant.MAN;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
        accountUseCase.unSubscribe();
    }

    public void initView() {
        initDataList();
        initPersonalInfo();
        initToolList();
        initBracelet();
    }

    public void initNetView() {
        initService();
        initBoughtService();
    }


    public void initDataList() {
        List<MineCardModel> list = new ArrayList<>();
        list.add(new MineCardModel(MineCardModel.BP, getApplication().getString(R.string.mine_my_bp), R.drawable.mine_bp, true));
        list.add(new MineCardModel(MineCardModel.ABNORMAL, getApplication().getString(R.string.mine_abnormal_bp), R.drawable.mine_abnormal, true));
        list.add(new MineCardModel(MineCardModel.MEDICATION_PLAN, getApplication().getString(R.string.mine_medication_plan), R.drawable.mine_medication, true));
        list.add(new MineCardModel(MineCardModel.ECG, getApplication().getString(R.string.mine_ecg), R.drawable.mine_ecg, true));
        list.add(new MineCardModel(MineCardModel.SPORT, getApplication().getString(R.string.mine_sport), R.drawable.mine_sport, true));
        list.add(new MineCardModel(MineCardModel.WEEKLY_REPORT, getApplication().getString(R.string.mine_weekly_report), R.drawable.mine_report, UnreadMessage.getWeekReportUnreadNumber() == 0));
        list.add(new MineCardModel(MineCardModel.KNOWLEDGE, getApplication().getString(R.string.mine_knowledge), R.drawable.mine_knowledge, true));
        dataList.setValue(list);
    }


    private void initToolList() {
        List<MineCardModel> list = new ArrayList<>();
        list.add(new MineCardModel(MineCardModel.WEIGHT, getApplication().getString(R.string.mine_weight_update), R.drawable.mine_weight, true));
        list.add(new MineCardModel(MineCardModel.DISEASE_HISTORY, getApplication().getString(R.string.mine_disease_update), R.drawable.mine_disease_history, true));
        list.add(new MineCardModel(MineCardModel.FONT_SIZE, getApplication().getString(R.string.mine_font_size), R.drawable.mine_font_size, true));
        if (isPaired()) {
            list.add(new MineCardModel(MineCardModel.ALARM, getApplication().getString(R.string.mine_measure_alarm), R.drawable.mine_alarm, true));
        }
        toolList.setValue(list);
    }

    public void initService() {
        serveUseCase.getCompanies(SaveUtils.getUserId(), new Subscriber<NetResponseEntity<List<ServiceCompanyResponseEntity>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(getApplication().getString(R.string.network_error));
                companyList.setValue(new ArrayList<>());
            }

            @Override
            public void onNext(NetResponseEntity<List<ServiceCompanyResponseEntity>> listNetResponseEntity) {
                if (listNetResponseEntity.getCode() == 0) {
                    companyList.setValue(CompanyWrapper.getListFromNet(listNetResponseEntity.getData()));
                } else {
                    companyList.setValue(new ArrayList<>());
                    showToast(listNetResponseEntity.getMessage());
                }
            }
        });
    }

    public void initBoughtService() {
        serveUseCase.getBoughtServices(SaveUtils.getUserId(), new Subscriber<NetResponseEntity<List<BoughtServiceResponseEntity>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                boughtServiceList.setValue(new ArrayList<>());
                showToast(getApplication().getString(R.string.network_error));
            }

            @Override
            public void onNext(NetResponseEntity<List<BoughtServiceResponseEntity>> listNetResponseEntity) {
                if (listNetResponseEntity.getCode() == 0) {
                    boughtServiceList.setValue(ServiceModelWrapper.getListFromNet(listNetResponseEntity.getData()));
                } else {
                    boughtServiceList.setValue(new ArrayList<>());
                    showToast(listNetResponseEntity.getMessage());
                }
            }
        });
    }

    private void initPersonalInfo() {
        UserInfo userInfo = UserInfo.getLocalUserInfo(getApplication());
        name.setValue(userInfo.getName());
        avatar.setValue(userInfo.getAvatarUrl());
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

    }

    private void initBracelet() {
        if (manager.isPaired()) {
            long time = manager.getLatestSyncTime();
            String timeString = "";
            if (time != 0) {
                timeString = String.format(Locale.getDefault(), getApplication().getString(R.string.i_sync_time),
                        DateFormat.format(MyConstant.FULL_DATETIME_FORMAT, time));
            }
            isPaired.setValue(true);
            syncTime.setValue(timeString);
        } else {
            isPaired.setValue(false);
        }
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
        loading.setValue(true);
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
                        loading.setValue(false);
                        showToast(getApplication().getString(R.string.network_error));
                    }

                    @Override
                    public void onNext(NetResponseEntity<BindAccountResponseEntity> netResponseEntity) {
                        loading.setValue(false);
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
        loading.setValue(true);
        this.useCase.getInfo(NetConstant.NO_CACHE, SaveUtils.getUserId(getApplication()),
                new Subscriber<NetResponseEntity<PersonalInfoEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        loading.setValue(false);
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
                        loading.setValue(false);
                    }
                });
    }


    public void getAuthorCode(CompanyModel company) {
        serveUseCase.getCompanyAuthorCode(SaveUtils.getUserId(), company.id,
                new Subscriber<NetResponseEntity<GetServiceAuthCodeResponseEntity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showToast(getApplication().getString(R.string.network_error));
                    }

                    @Override
                    public void onNext(NetResponseEntity<GetServiceAuthCodeResponseEntity> netResponseEntity) {
                        if (netResponseEntity.getCode() == 0) {
                            GetServiceAuthCodeResponseEntity getServiceAuthCodeResponseEntity = netResponseEntity.getData();
                            if (getServiceAuthCodeResponseEntity != null) {
                                SaveUtils.saveServiceAuthCode(getServiceAuthCodeResponseEntity.getAuthCode());
                                jump(company, getServiceAuthCodeResponseEntity.getAuthCode());
                            } else {
                                showToast(getApplication().getString(R.string.network_error));
                            }
                        } else {
                            showToast(netResponseEntity.getMessage());
                        }
                    }
                });
    }


    public void checkAuth(final CompanyModel company) {
        serveUseCase.checkServiceAuthor(SaveUtils.getUserId(), company.id,
                new Subscriber<NetResponseEntity<CheckServiceAuthorityResponseEntity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showToast(getApplication().getString(R.string.network_error));
                    }

                    @Override
                    public void onNext(NetResponseEntity<CheckServiceAuthorityResponseEntity> netResponseEntity) {
                        if (netResponseEntity.getCode() == 0) {
                            CheckServiceAuthorityResponseEntity entity = netResponseEntity.getData();
                            if (entity != null) {
                                if (entity.getAuthStatus() == CheckServiceAuthorityResponseEntity.CHECKED) {
                                    jump(company, "");
                                } else if (entity.getAuthStatus() == CheckServiceAuthorityResponseEntity.CHECK_SUCCESS) {
                                    jump(company, entity.getAuthCode());
                                } else {
                                    needAuthCompany.setValue(company);
                                }
                                //TEST
//                                needAuthCompany.setValue(company);
                            } else {
                                needAuthCompany.setValue(company);
                            }
                        }
                    }
                });
    }

    private void jump(CompanyModel company, String code) {
        String param;
        if (company.associatedServiceModel == null) {
            param = "?phone=" + SaveUtils.getPhone(getApplication()) + "&zone=" + SaveUtils.getZone(getApplication()) + "&code=" + code;
        } else {
            param = "?phone=" + SaveUtils.getPhone(getApplication()) + "&zone=" + SaveUtils.getZone(getApplication()) + "&code=" + code + "&serviceCode=" + company.associatedServiceModel.serviceCode;
        }
        pageUrl.setValue(company.pageUrl + param);
    }

}
