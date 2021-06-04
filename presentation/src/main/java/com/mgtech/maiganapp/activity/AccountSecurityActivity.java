package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.mgtech.domain.utils.NetConstant;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.LoginOtherEvent;
import com.mgtech.maiganapp.utils.LoginUtils;
import com.mgtech.maiganapp.viewmodel.AccountSecurityViewModel;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author xiang
 */
public class AccountSecurityActivity extends BaseActivity<AccountSecurityViewModel> {
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_qq)
    TextView tvQQ;
    @Bind(R.id.tv_wechat)
    TextView tvWechat;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, AccountSecurityActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        viewModel.phone.observe(this, s -> {
            int phoneLength = s.length();
            String phoneStars ;
            if (phoneLength >= 11){
                phoneStars = s.substring(0,3) + "****" + s.substring(phoneLength-4,phoneLength);
            }else if (phoneLength >= 6){
                phoneStars = s.substring(0,2) + "****" + s.substring(phoneLength-2,phoneLength);
            }else{
                phoneStars = s;
            }
            tvPhone.setText(phoneStars);
        });
        viewModel.qq.observe(this, s -> tvQQ.setText(s));
        viewModel.wechat.observe(this, s -> tvWechat.setText(s));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.initPersonalInfo();
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_account_security;
    }

    @OnClick({R.id.layout_qq})
    public void bindQQ() {
        LoginUtils.loginQQ();
    }

    @OnClick({R.id.layout_wechat})
    public void bindWx() {
        IWXAPI api = ((MyApplication) getApplication()).getWXApi();
        if (!api.isWXAppInstalled()) {
            showShortToast(getString(R.string.not_install_wechat));
            return;
        }
        LoginUtils.loginWeChat();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginOtherEvent event) {
        if (event.type == NetConstant.QQ_LOGIN) {
            if (event.status == LoginOtherEvent.FAIL) {
                showShortToast(getString(R.string.qq_bind_fail));
            } else if (event.status == LoginOtherEvent.SUCCESS) {
                viewModel.bindQQ(event.openId, event.unionId, event.token, event.name, event.avatarUrl);
            }
        } else if (event.type == NetConstant.WE_CHAT_LOGIN) {
            if (event.status == LoginOtherEvent.FAIL) {
                showShortToast(getString(R.string.wechat_bind_fail));
            } else if (event.status == LoginOtherEvent.SUCCESS) {
                viewModel.bindWeChat(event.openId, event.unionId, event.token, event.name, event.avatarUrl);
            }
        }
    }

    @OnClick(R.id.layout_password)
    void changePassword() {
        Intent intent = ResetPasswordActivity.getCallingIntent(this);
        intent.putExtra(ResetPasswordActivity.PHONE, viewModel.phone.getValue());
        intent.putExtra(ResetPasswordActivity.ZONE, viewModel.zone.getValue());
        intent.putExtra(ResetPasswordActivity.EDITABLE, false);
        startActivity(intent);
    }
}
