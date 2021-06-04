package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.ResetPasswordViewModel;

import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * @author zhaixiang
 * 重新设置密码页面
 */

public class ResetPasswordActivity extends BaseActivity<ResetPasswordViewModel> {
    public static final String EDITABLE = "editable";
    public static final String PHONE = "phone";
    public static final String ZONE = "zone";
    @Bind(R.id.et_password)
    EditText etPassword;
    private String phone;
    private String zone;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.tv_zone)
    TextView tvZone;
    @Bind(R.id.et_verification_code)
    EditText etVerificationCode;
    @Bind(R.id.btn_get_verification_code)
    TextView btnGetVerificationCode;
    @Bind(R.id.tv_country)
    TextView tvCountry;
    @Bind(R.id.iv_show_password)
    ImageView ivShowPassword;
    private boolean showPassword;
    private boolean editable;


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ResetPasswordActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        phone = getIntent().getStringExtra(PHONE);
        zone = getIntent().getStringExtra(ZONE);
        editable = getIntent().getBooleanExtra(EDITABLE, true);
        etPhone.setText(phone);
        etPhone.setEnabled(editable);

        if (TextUtils.isEmpty(zone)){
            zone = "+86";
        }
        viewModel.resetPasswordSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                finish();
            }
        });
        etPhone.addTextChangedListener(textWatcher);
        etVerificationCode.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
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
                etPassword.getText().length()!= 0;
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    /**
     * 获取国家号码首位
     */
    @OnClick({R.id.tv_zone,R.id.layout_zone})
    void setCountryCode() {
        if (editable) {
            startActivityForResult(SelectCountryCodeActivity.getCallingIntent(this), SelectCountryCodeActivity.REQUEST);
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_reset_password;
    }

    /**
     * 重置密码
     */
    @OnClick(R.id.btn_submit)
    void submit() {
        viewModel.resetPassword(tvZone.getText().toString().trim(),etPhone.getText().toString().trim(),
                etVerificationCode.getText().toString().trim(),etPassword.getText().toString().trim());
    }


    /**
     * 获取验证码
     */
    @OnClick(R.id.btn_get_verification_code)
    void getVerificationCode() {
//        btnGetVerificationCode.setEnabled(!viewModel.getVerificationCode(tvZone.getText().toString(),etPhone.getText().toString()));
//        hideSoftKeyboard();
        viewModel.getVerificationCode(tvZone.getText().toString(),etPhone.getText().toString());
        hideSoftKeyboard();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectCountryCodeActivity.REQUEST && resultCode == RESULT_OK) {
            zone  = "+" + data.getStringExtra(SelectCountryCodeActivity.CODE);
            tvZone.setText(zone);
            tvCountry.setText(data.getStringExtra(SelectCountryCodeActivity.NAME));
        }
    }
}
