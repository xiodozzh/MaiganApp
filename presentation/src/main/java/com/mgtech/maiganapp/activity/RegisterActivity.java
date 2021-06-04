package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;
import androidx.lifecycle.Observer;

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
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.RegisterViewModel;
import com.mgtech.maiganapp.widget.PermissionPrivacySpan;
import com.mgtech.maiganapp.widget.PermissionProtocolSpan;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * @author zhaixiang
 * 注册页面
 */

public class RegisterActivity extends BaseActivity<RegisterViewModel> {
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.tv_zone)
    TextView btnCountryCode;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_verification_code)
    EditText etVerificationCode;
    @Bind(R.id.btn_get_verification_code)
    TextView btnGetVerificationCode;
    @Bind(R.id.cb_protocol)
    CheckBox cbProtocol;
    @Bind(R.id.iv_show_password)
    ImageView ivShowPassword;
    @Bind(R.id.tv_country)
    TextView tvCountry;
    @Bind(R.id.tv_protocol)
    TextView tvProtocol;
    private boolean showPassword;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
//        viewModel.registerSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable observable, int i) {
//                startActivity(PersonalInfoInitActivity.getCallingIntent(RegisterActivity.this,viewModel.userId));
//                finish();
//            }
//        });
//        viewModel.backToLogin.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable observable, int i) {
//                Intent intent = LoginActivity.getCallingIntent(RegisterActivity.this,false);
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//                finish();
//            }
//        });
        viewModel.registerSuccess.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                startActivity(PersonalInfoInitActivity.getCallingIntent(RegisterActivity.this,viewModel.userId));
                finish();
            }
        });
        viewModel.backToLogin.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Intent intent = LoginActivity.getCallingIntent(RegisterActivity.this,false);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        etPhone.addTextChangedListener(textWatcher);
        etVerificationCode.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
        cbProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    viewModel.valid.set(isValid());
                }else{
                    viewModel.valid.set(false);
                }
            }
        });

        String preString = getString(R.string.activity_register_agree_user_protocol);
        String userString = getString(R.string.activity_register_user_protocol);
        String privacyString = getString(R.string.activity_register_privacy);

        SpannableString ss = new SpannableString(preString + userString + "、" + privacyString);
        ss.setSpan(new PermissionProtocolSpan(this),preString.length(),preString.length() + userString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new PermissionPrivacySpan(this),preString.length() + userString.length()+1,preString.length() + userString.length()+privacyString.length()+1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvProtocol.setText(ss);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0){
                viewModel.valid.set(false);
            }else{
                viewModel.valid.set(isValid());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean isValid(){
        return etPhone.getText().length()!= 0 && etVerificationCode.getText().length() != 0 &&
                etPassword.getText().length()!= 0 && cbProtocol.isChecked();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.layout_show_password)
    void changeShowPassword(){
        showPassword = !showPassword;
        if (showPassword){
            ivShowPassword.setImageResource(R.drawable.activity_login_show_password);
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            ivShowPassword.setImageResource(R.drawable.activity_login_close_password);
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        etPassword.setSelection(etPassword.length());
    }

    /**
     * 获取国家号码首位
     */
    @OnClick({R.id.tv_zone,R.id.tv_country})
    void setCountryCode() {
        startActivityForResult(SelectCountryCodeActivity.getCallingIntent(this),SelectCountryCodeActivity.REQUEST);
    }

    @OnClick(R.id.btn_back)
    void back(){
        startActivity(LoginActivity.getCallingIntent(this,false));
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.btn_get_verification_code)
    void getVerificationCode() {
        btnGetVerificationCode.setEnabled(!viewModel.getVerificationCode(etPhone.getText().toString(),btnCountryCode.getText().toString()));
    }

//    @OnClick(R.id.tv_protocol)
//    void readProtocol(){
//        startActivity(ProtocolActivity.getCallingIntent(this));
//    }

    /**
     * 验证码不能连续发送
     */
//    private void verificationCodeSendSuccess(){
//        btnGetVerificationCode.setEnabled(false);
//        timer = new CountDownTimer(MyConstant.MSM_TIME_INTERVAL,500) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                if (btnGetVerificationCode != null) {
//                    int second = (int) (millisUntilFinished / 1000);
//                    btnGetVerificationCode.setText(String.format(Locale.getDefault(),getString(R.string.activity_register_second_left),second));
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                if (btnGetVerificationCode != null) {
//                    btnGetVerificationCode.setText(getText(R.string.activity_register_get_verification_code));
//                    btnGetVerificationCode.setEnabled(true);
//                }
//            }
//        }.start();
//    }

//    @Override
//    protected void onDestroy() {
//        if (timer != null){
//            timer.cancel();
//        }
//        super.onDestroy();
//    }

    /**
     * 注册
     */
    @OnClick(R.id.btn_register)
    void register() {
        viewModel.register(etPhone.getText().toString(), etPassword.getText().toString(),
                etVerificationCode.getText().toString(), btnCountryCode.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectCountryCodeActivity.REQUEST && resultCode == RESULT_OK){
            String s = "+" + data.getStringExtra(SelectCountryCodeActivity.CODE);
            btnCountryCode.setText(s);
            tvCountry.setText(data.getStringExtra(SelectCountryCodeActivity.NAME));
        }
    }
}
