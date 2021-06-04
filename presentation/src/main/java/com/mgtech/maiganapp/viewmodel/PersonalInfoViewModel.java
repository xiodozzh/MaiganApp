package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.request.BindAccountRequestEntity;
import com.mgtech.domain.entity.net.response.BindAccountResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.AccountUseCase;
import com.mgtech.domain.interactor.AppConfigUseCase;
import com.mgtech.domain.interactor.FileUseCase;
import com.mgtech.domain.interactor.PersonalInfoUseCase;
import com.mgtech.domain.utils.CacheUtil;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.IPersonalInfoModel;
import com.mgtech.maiganapp.data.model.PersonalInfoModel;
import com.mgtech.maiganapp.data.wrapper.PersonalInfoModelWrapper;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileWithBitmapCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 */
public class PersonalInfoViewModel extends BaseViewModel {
    public List<IPersonalInfoModel> list = new ArrayList<>();
    private static final String FILE_PATH ="avatar";
    public ObservableBoolean getModelListSuccess = new ObservableBoolean(false);
    public ObservableBoolean loading = new ObservableBoolean(false);
    public String phone;
    public String zone;
    private PersonalInfoUseCase useCase;
    private AccountUseCase accountUseCase;
    private AppConfigUseCase appConfigUseCase;
    public PersonalInfoModel model;
    private FileUseCase fileUseCase;
    private String weChatToken;

    public PersonalInfoViewModel(@NonNull Application application) {
        super(application);
        useCase = ((MyApplication) application).getPersonInfoUseCase();
        appConfigUseCase = ((MyApplication) application).getAppConfigUseCase();
        accountUseCase = ((MyApplication) application).getAccountUseCase();
        phone = SaveUtils.getPhone(application);
        zone = SaveUtils.getZone(application);
        fileUseCase = ((MyApplication) getApplication()).getFileUseCase();
    }

    private void initViewList(PersonalInfoModel model) {
        list.clear();
        String avatarUrl;
        if (SaveUtils.getLoginType(getApplication()) == NetConstant.QQ_LOGIN) {
            avatarUrl = model.qqAvatar;
        } else {
            avatarUrl = model.wxAvatar;
        }
        int phoneLength = phone.length();
        String phoneStars ;
        if (phoneLength >= 11){
            phoneStars = phone.substring(0,3) + "****" + phone.substring(phoneLength-4,phoneLength);
        }else if (phoneLength >= 6){
            phoneStars = phone.substring(0,2) + "****" + phone.substring(phoneLength-2,phoneLength);
        }else{
            phoneStars = phone;
        }
        String sex = "";
        if (model.gender == MyConstant.MAN){
            sex = getApplication().getString(R.string.man);
        }else if (model.gender == MyConstant.WOMEN){
            sex = getApplication().getString(R.string.woman);
        }
        IPersonalInfoModel.Avatar avatarModel = new IPersonalInfoModel.Avatar(model.avatarUrl, avatarUrl);

        IPersonalInfoModel.Item nameModel = new IPersonalInfoModel.Item(IPersonalInfoModel.TYPE_NAME,
                getApplication().getString(R.string.personal_info_name), model.userName, false,true);
        IPersonalInfoModel.Item birthModel = new IPersonalInfoModel.Item(IPersonalInfoModel.TYPE_BIRTH,
                getApplication().getString(R.string.personal_info_birthday),
                DateFormat.format(MyConstant.DATE_FORMAT_BIRTHDAY, model.birthday).toString(),
                false,true);
        IPersonalInfoModel.Item sexModel = new IPersonalInfoModel.Item(IPersonalInfoModel.TYPE_SEX,
                getApplication().getString(R.string.personal_info_sex), sex, false,true);
        IPersonalInfoModel.Item heightModel = new IPersonalInfoModel.Item(IPersonalInfoModel.TYPE_HEIGHT,
                getApplication().getString(R.string.personal_info_height), String.format(Locale.getDefault(), "%d cm", Math.round(model.height)), true,true);
        IPersonalInfoModel.Item weightModel = new IPersonalInfoModel.Item(IPersonalInfoModel.TYPE_WEIGHT,
                getApplication().getString(R.string.personal_info_weight), String.format(Locale.getDefault(), "%.1f kg",
                model.weight), false,true);
        IPersonalInfoModel.Item phoneModel = new IPersonalInfoModel.Item(IPersonalInfoModel.TYPE_PHONE,
                getApplication().getString(R.string.personal_info_phone), phoneStars, true,false);
        IPersonalInfoModel.Item passwordModel = new IPersonalInfoModel.Item(IPersonalInfoModel.TYPE_PASSWORD,
                getApplication().getString(R.string.personal_info_password), "", false,true);

//        IPersonalInfoModel.Item wxModel = new IPersonalInfoModel.Item(IPersonalInfoModel.TYPE_WX,
//                getApplication().getString(R.string.personal_info_wechat), TextUtils.isEmpty(model.wxName) ?
//                getApplication().getString(R.string.not_bond) : model.wxName, true,true);
//        IPersonalInfoModel.Item qqModel = new IPersonalInfoModel.Item(IPersonalInfoModel.TYPE_QQ,
//                getApplication().getString(R.string.personal_info_qq), TextUtils.isEmpty(model.qqName) ?
//                getApplication().getString(R.string.not_bond) : model.qqName, false,true);

        IPersonalInfoModel.Item locationModel = new IPersonalInfoModel.Item(IPersonalInfoModel.TYPE_LOCATION,
                getApplication().getString(R.string.personal_info_location), model.location, true,true);

        list.add(avatarModel);
        list.add(nameModel);
        list.add(birthModel);
        list.add(sexModel);
        list.add(heightModel);
        list.add(weightModel);
//        list.add(phoneModel);
//        list.add(passwordModel);

//        list.add(wxModel);
//        list.add(qqModel);
        list.add(locationModel);

        getModelListSuccess.set(!getModelListSuccess.get());
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



    public void getInfo() {
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
                                model = PersonalInfoModelWrapper.getModelFromNet(entity);
                                SaveUtils.savePersonalInfo(getApplication(),entity);
                                UserInfo.saveLocalUserInfo(getApplication(), entity);
                                initViewList(model);
                            }
                        } else {
                            showToast(netResponseEntity.getMessage());
                        }
                        loading.set(false);
                    }
                });
    }

    public void setName(String name) {
        PersonalInfoEntity entity = PersonalInfoModelWrapper.modelToNet(model);
        entity.setDisplayName(name);
        update(entity);
    }

    public void setGender(int gender) {
        PersonalInfoEntity entity = PersonalInfoModelWrapper.modelToNet(model);
        entity.setSex(gender);
        update(entity);
    }

    public void setHeight(float height) {
        PersonalInfoEntity entity = PersonalInfoModelWrapper.modelToNet(model);
        entity.setHeight(height);
        update(entity);
    }

    public void setWeight(float weight) {
        PersonalInfoEntity entity = PersonalInfoModelWrapper.modelToNet(model);
        entity.setWeight(weight);
        update(entity);
    }

    public void setBirthday(Date date) {
        PersonalInfoEntity entity = PersonalInfoModelWrapper.modelToNet(model);
        entity.setBirthday(DateFormat.format(MyConstant.DATE_FORMAT_BIRTHDAY, date).toString());
        update(entity);
    }

    private void setAvatarUrl(String url) {
        PersonalInfoEntity entity = PersonalInfoModelWrapper.modelToNet(model);
        entity.setAvatarUrl(url);
        update(entity);
    }

    private void update(PersonalInfoEntity entity) {
        useCase.setInfo(entity, new Subscriber<NetResponseEntity<PersonalInfoEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                initViewList(model);
            }

            @Override
            public void onNext(NetResponseEntity<PersonalInfoEntity> netResponseEntity) {
                if (netResponseEntity.getCode() == 0) {
                    PersonalInfoEntity personalInfoEntity = netResponseEntity.getData();
                    if (personalInfoEntity != null) {
                        model = PersonalInfoModelWrapper.getModelFromNet(personalInfoEntity);
                        UserInfo.saveLocalUserInfo(getApplication(), personalInfoEntity);
                    }
                } else {
                    showToast(netResponseEntity.getMessage());
                }
                initViewList(model);
            }
        });
    }

    public void bindAccount(BindAccountRequestEntity entity) {
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
                        showToast(getApplication().getString(R.string.network_error));
                    }

                    @Override
                    public void onNext(NetResponseEntity<BindAccountResponseEntity> netResponseEntity) {
                        if (netResponseEntity.getCode() == 0) {
                            getInfo();
                        }else{
                            showToast(netResponseEntity.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
        appConfigUseCase.unSubscribe();
        accountUseCase.unSubscribe();
    }

    public void setLocation(String countryId, String provinceId, String location) {
        model.location = location;
        PersonalInfoEntity entity = PersonalInfoModelWrapper.modelToNet(model);
        entity.setProvinceId(provinceId);
        entity.setCountryId(countryId);
        update(entity);
    }

    public File getTempleFile(){
        File dir = new File(CacheUtil.getCachePath(getApplication()) + File.separator + FILE_PATH);
        if (!dir.exists()){
            dir.mkdir();
        }
        return new File(CacheUtil.getCachePath(getApplication()) + File.separator + FILE_PATH,"avatarTemp.jpg");
    }

    public void compressAndUpload(){
        File file = getTempleFile();
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source(file)
                .asFile().withOptions(options).compress(new FileWithBitmapCallback() {

            @Override
            public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                if (isSuccess) {
                    Log.i(TAG, "outfile: "+ outfile);
                    File file = new File(outfile);
                    setAvatar(file);
                }else{
                    showToast(getApplication().getString(R.string.upload_image_fail));
                }
            }
        });
    }

    public void compressAndUpload(Uri uri){
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source(uri)
                .asFile().withOptions(options).compress(new FileWithBitmapCallback() {

            @Override
            public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                if (isSuccess) {
                    File file = new File(outfile);
                    setAvatar(file);
                }else{
                    showToast(getApplication().getString(R.string.upload_image_fail));
                }
            }
        });
    }

    public void setAvatar(File file) {
        fileUseCase.setAvatarFromFile(file, new Subscriber<NetResponseEntity<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(NetResponseEntity<String> netResponseEntity) {
                if (netResponseEntity.getCode() == 0) {
                    String url = netResponseEntity.getData();
                    if (!TextUtils.isEmpty(url)) {
                        setAvatarUrl(netResponseEntity.getData());
                    }
                } else {
                    showToast(netResponseEntity.getMessage());
                }
            }
        });
    }
}
