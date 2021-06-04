package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.text.format.DateFormat;

import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.entity.UnreadMessage;
import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.request.BindAccountRequestEntity;
import com.mgtech.domain.entity.net.response.BindAccountResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.AccountUseCase;
import com.mgtech.domain.interactor.PersonalInfoUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.IMineModel;
import com.mgtech.maiganapp.data.model.PersonalInfoModel;
import com.mgtech.maiganapp.data.wrapper.PersonalInfoModelWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 */
public class MineViewModel extends BaseFragmentViewModel {
    public List<IMineModel> list = new ArrayList<>();
    public ObservableBoolean viewInitSuccess = new ObservableBoolean(false);
    public ObservableBoolean loading = new ObservableBoolean(false);
    private IBraceletInfoManager manager;
    private PersonalInfoUseCase useCase;
    private AccountUseCase accountUseCase;

    public MineViewModel(@NonNull Application application) {
        super(application);
        manager = new BraceletInfoManagerBuilder(application).create();
        useCase = ((MyApplication)application).getPersonInfoUseCase();
        accountUseCase = ((MyApplication) application).getAccountUseCase();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
        accountUseCase.unSubscribe();
    }

    public void initViewList(){
        list.clear();
        IMineModel.Info infoModel = new IMineModel.Info();
        UserInfo userInfo =  UserInfo.getLocalUserInfo(getApplication());
        infoModel.name =userInfo.getName();
        infoModel.avatarUrl = userInfo.getAvatarUrl();
        infoModel.healthIndex = 90;
        String wxName = SaveUtils.getWeChatName();
        String qqName = SaveUtils.getQQName();
        IMineModel.Item itemWeChat = new IMineModel.Item(R.drawable.fragment_i_we_chat,
                getApplication().getString(R.string.personal_info_wechat), TextUtils.isEmpty(wxName) ?
                getApplication().getString(R.string.not_bond) : wxName,IMineModel.TYPE_WE_CHAT,false,true);
        IMineModel.Item itemQQ = new IMineModel.Item(R.drawable.fragment_i_qq,
                getApplication().getString(R.string.personal_info_qq),(TextUtils.isEmpty(qqName) ?
                getApplication().getString(R.string.not_bond) : qqName),IMineModel.TYPE_QQ,false,true);

        IMineModel.Item itemHealth = new IMineModel.Item(R.drawable.fragment_i_health_report,
                getApplication().getString(R.string.i_health_report),IMineModel.TYPE_HEALTH_REPORT,true, UnreadMessage.getWeekReportUnreadNumber()==0);
        IMineModel.Item itemDisease = new IMineModel.Item(R.drawable.fragment_i_disease_history,
                getApplication().getString(R.string.i_disease_history),IMineModel.TYPE_DISEASE_UPDATE,false,true);
        IMineModel.Item itemFontSize = new IMineModel.Item(R.drawable.fragment_i_font_size,
                getApplication().getString(R.string.i_font_size),IMineModel.TYPE_FONT_SIZE,true,true);
        IMineModel.Item itemException = new IMineModel.Item(R.drawable.fragment_i_exception,
                getApplication().getString(R.string.i_exception),IMineModel.TYPE_EXCEPTION_REMINDER,false,true);
        IMineModel.Item itemService = new IMineModel.Item(R.drawable.fragment_i_service,
                getApplication().getString(R.string.i_service),IMineModel.TYPE_SERVICE,true,true);
        IMineModel.Item itemSetting = new IMineModel.Item(R.drawable.fragment_i_other,
                getApplication().getString(R.string.i_other),IMineModel.TYPE_SETTING,true,true);
        list.add(infoModel);
        list.add(itemWeChat);
        list.add(itemQQ);
        if (manager.isPaired()){
            long time = manager.getLatestSyncTime();
            String timeString = "";
            if (time != 0){
                timeString = String.format(Locale.getDefault(),getApplication().getString(R.string.i_sync_time),
                        DateFormat.format(MyConstant.FULL_DATETIME_FORMAT,time));
            }
            IMineModel.Bind itemBind = new IMineModel.Bind(timeString);
            list.add(itemBind);

        }else{
            IMineModel.UnBind bindModel = new IMineModel.UnBind();
            list.add(bindModel);
        }
        list.add(itemHealth);
        list.add(itemDisease);
        list.add(itemFontSize);
        list.add(itemException);
//        list.add(itemService);
        list.add(itemSetting);
        viewInitSuccess.set(!viewInitSuccess.get());
    }


    /**
     * 绑定微信账号
     * @param token ShareSDK 的Token
     * {country=中国, unionid=, province=上海, city=黄浦, openid=, sex=1, nickname=六一居士Jesse,
     *            headimgurl=, userTags=, language=zh_CN, privilege=[]}
     */
    public void bindWeChat(String openId,String unionId,String token,String nickName,String avatarUrl){
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
     * @param openId openId
     * @param unionId unionId
     * {ret=0, msg=, is_lost=0, gender=女, is_yellow_vip=0, city=黄浦, year=1988, level=0, figureurl_2=,
     *            figureurl_1=, is_yellow_year_vip=0, province=上海, constellation=, figureurl=,
     *            figureurl_type=0, figureurl_qq=, nickname=脉迹mystrace, yellow_vip_level=0, userTags=,
     *            figureurl_qq_1=, vip=0, figureurl_qq_2=}
     **/
    public void bindQQ(String openId,String unionId,String token,String name,String avatarUrl){
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
                        }else{
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
                                SaveUtils.savePersonalInfo(getApplication(),entity);
                                UserInfo.saveLocalUserInfo(getApplication(), entity);
                                initViewList();
                            }
                        } else {
                            showToast(netResponseEntity.getMessage());
                        }
                        loading.set(false);
                    }
                });
    }
}
