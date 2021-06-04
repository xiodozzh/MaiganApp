package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;

import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.interactor.AccountUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.service.NetJobService;

/**
 * @author zhaixiang
 * 进入界面view-model
 */

public class EnterViewModel extends BaseViewModel {
    public final ObservableBoolean showGuide = new ObservableBoolean(false);
    public final ObservableBoolean showProtocol = new ObservableBoolean(true);
    public final ObservableBoolean lastPage = new ObservableBoolean(false);
    public final ObservableBoolean jumpPage = new ObservableBoolean(false);
    public boolean multiLogin;
    private AccountUseCase accountUseCase;
    public boolean isLogin;
    public boolean personalInfoComplete;

    public EnterViewModel(Application context) {
        super(context);
        this.accountUseCase = ((MyApplication) context).getAccountUseCase();
    }

    /**
     * 检查是否为新用户，新用户显示引导页，老用户加载启动页
     */
    public void checkIsNew() {
        showProtocol.set(!SaveUtils.isProtocolSigned());
        if (!showProtocol.get()) {
            showGuide.set(!SaveUtils.isDirtyUse(getApplication()));
            if (!showGuide.get()) {
                String id = SaveUtils.getUserId(getApplication());
                String token = SaveUtils.getToken(getApplication());
                isLogin = !TextUtils.isEmpty(id) && !TextUtils.isEmpty(token);
                if (isLogin) {
                    backgroundLoadData();
                    checkUserInfo();
                }
                finishEnterWaiting();
            }
        }
    }

    public void agreeSigned(){
        SaveUtils.setProtocolSigned(true);
        checkIsNew();
    }

    /**
     * 后台检查登录状况，是否绑定，是否有未上传的参数
     */
    private void backgroundLoadData() {
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_LOGIN));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_BIND));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_PERSONAL_INFO));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_SAVE_DISPLAY));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_PARAMETERS));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_CONFIG));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_INDICATOR_PARAMETERS));
        NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_GET_FIRST_AID_PHONE));
        NetJobService.enqueueWork(getApplication(), NetJobService.getCallingIntent(getApplication(), NetJobService.TYPE_SET_CUSTOMER_CONTACT_PERMISSION));

        if (SaveUtils.isNeedPushId()) {
            NetJobService.enqueueWork(getApplication(),NetJobService.getCallingIntent(getApplication(),NetJobService.TYPE_SAVE_PUSH_ID));
        }
    }

    /**
     * 前台检查
     */
    private void checkUserInfo() {
        UserInfo userInfo = UserInfo.getLocalUserInfo(getApplication());
        personalInfoComplete = userInfo.isComplete();
    }


    /**
     * 结束启动页面
     */
    public void finishEnterWaiting() {
        SaveUtils.setDirtyUse(getApplication());
        jumpPage.set(!jumpPage.get());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        accountUseCase.unSubscribe();
    }
}
