package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;

import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.domain.rx.GoToLoginEvent;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.LoginOtherEvent;
import com.mgtech.maiganapp.data.event.LogoutEvent;
import com.mgtech.maiganapp.utils.LoginUtils;
import com.mgtech.maiganapp.viewmodel.LoginViewModel;
import com.mgtech.maiganapp.widget.PermissionPrivacySpan;
import com.mgtech.maiganapp.widget.PermissionProtocolSpan;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 * 登录页面
 */

public class LoginActivity extends BaseActivity<LoginViewModel> {
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.tv_zone)
    TextView tvZone;
    @Bind(R.id.tv_country)
    TextView tvCountry;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.iv_show_password)
    ImageView ivShowPassword;
    @Bind(R.id.tv_protocol)
    TextView tvProtocol;
    private boolean showPassword;

    public static Intent getCallingIntent(Context context, boolean multiLogin) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (multiLogin) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        viewModel.toHomePage.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                startActivity(MainActivity.getCallingIntent(LoginActivity.this));
                finish();
            }
        });
        viewModel.toSetPage.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                startActivity(PersonalInfoInitActivity.getCallingIntent(LoginActivity.this, SaveUtils.getUserId(getApplicationContext())));
                finish();
            }
        });
        initProtocol();
        etPassword.addTextChangedListener(textWatcher);
        etPhone.addTextChangedListener(textWatcher);
        EventBus.getDefault().register(this);
    }

    private void initProtocol() {
        String preString = getString(R.string.login_agree_protocol);
        String userString = getString(R.string.activity_register_user_protocol);
        String privacyString = getString(R.string.activity_register_privacy);

        SpannableString ss = new SpannableString(preString + userString + "、" + privacyString);
        int protocolStart = preString.length();
        int protocolEnd = protocolStart + userString.length() + 1;
        int privacyEnd = protocolEnd + privacyString.length();
        ss.setSpan(new PermissionProtocolSpan(this), protocolStart, protocolEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new PermissionPrivacySpan(this), protocolEnd, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvProtocol.setText(ss);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.btn_login)
    void login() {
        viewModel.login(etPhone.getText().toString(), etPassword.getText().toString(), tvZone.getText().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.btn_forget_password)
    void forgetPassword() {
        Intent intent = ResetPasswordActivity.getCallingIntent(this);
        intent.putExtra("phone", etPhone.getText().toString());
        startActivity(intent);
    }

    @OnClick(R.id.layout_show_password)
    void changeShowPassword() {
        showPassword = !showPassword;
        if (showPassword) {
            ivShowPassword.setImageResource(R.drawable.activity_login_show_password);
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            ivShowPassword.setImageResource(R.drawable.activity_login_close_password);
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        etPassword.setSelection(etPassword.length());
    }

    @OnClick(R.id.tv_register)
    void newAccount() {
        startActivity(RegisterActivity.getCallingIntent(this));
        finish();
    }

    @OnClick({R.id.tv_zone, R.id.tv_country})
    void selectZone() {
        startActivityForResult(SelectCountryCodeActivity.getCallingIntent(this), SelectCountryCodeActivity.REQUEST);
    }

    @OnClick(R.id.iv_wechat)
    void weChatLogin() {
        IWXAPI api = ((MyApplication) getApplication()).getWXApi();
        if (!api.isWXAppInstalled()) {
            showShortToast(getString(R.string.not_install_wechat));
            return;
        }
        if (!viewModel.weChatLogining.get()) {
            viewModel.weChatLogining.set(true);
            LoginUtils.loginWeChat();
        }
    }


    @OnClick(R.id.iv_QQ)
    void qqLogin() {
        if (!viewModel.qqLogining.get()) {
            viewModel.qqLogining.set(true);
            LoginUtils.loginQQ();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginOtherEvent event) {
        viewModel.qqLogining.set(false);
        viewModel.weChatLogining.set(false);
        if (event.type == NetConstant.QQ_LOGIN) {
            if (event.status == LoginOtherEvent.FAIL) {
                showShortToast(getString(R.string.qq_logion_fail));
            } else if (event.status == LoginOtherEvent.SUCCESS) {
                viewModel.loginByQQ(event.openId, event.unionId, event.token);
            }
        } else if (event.type == NetConstant.WE_CHAT_LOGIN) {
            if (event.status == LoginOtherEvent.FAIL) {
                showShortToast(getString(R.string.wechat_login_fail));
            } else if (event.status == LoginOtherEvent.SUCCESS) {
                viewModel.loginByWeChat(event.openId, event.unionId, event.token);
            }
        }
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void logout(LogoutEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void goToLogin(GoToLoginEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectCountryCodeActivity.REQUEST && resultCode == RESULT_OK) {
            String s = "+" + data.getStringExtra(SelectCountryCodeActivity.CODE);
            tvZone.setText(s);
            tvCountry.setText(data.getStringExtra(SelectCountryCodeActivity.NAME));
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            viewModel.valid.set(etPhone.getText().length() > 0 && etPassword.getText().length() != 0);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
