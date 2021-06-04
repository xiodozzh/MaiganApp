package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import android.os.CountDownTimer;
import android.util.Log;

import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.request.GetVerificationCodeEntity;
import com.mgtech.domain.entity.net.request.LoginByPhoneRequestEntity;
import com.mgtech.domain.entity.net.response.LoginResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.RegisterResponseEntity;
import com.mgtech.domain.interactor.AccountUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;

import java.util.Locale;

import rx.Subscriber;

/**
 * @author zhaixiang
 * @date 2018/11/7
 * 注册
 */

public class RegisterViewModel extends BaseViewModel {
    public final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    public final MutableLiveData<Boolean> backToLogin = new MutableLiveData<>();
    public final ObservableBoolean codeSendEnable = new ObservableBoolean(true);
    public final ObservableField<String> btnSendText = new ObservableField<>("");

    public final ObservableBoolean valid = new ObservableBoolean(false);
    private AccountUseCase useCase;
    private Application context;
    private static final int MIN_LENGTH = 1;
    public String userId;
    private CountDownTimer timer;

    public RegisterViewModel(Application context) {
        super(context);
        this.useCase = ((MyApplication) context).getAccountUseCase();
        this.context = context;
        this.btnSendText.set(context.getString(R.string.reset_password_get_verification_code));
    }

    /**
     * 获取验证号码
     *
     * @param phone 手机号
     */
    public boolean getVerificationCode(String phone, String zone) {
        if (isPhoneNumberValid(phone.trim())) {
            sendShortMessage(phone.trim(), zone.replaceAll("\\+", ""));
            return true;
        }
        return false;
    }

    /**
     * 注册
     *
     * @param phone            手机号
     * @param password         密码
     * @param verificationCode 验证码
     */
    public void register(String phone, String password, String verificationCode, String country) {
        if (isPhoneNumberValid(phone.trim()) && isVerificationValid(verificationCode.trim()) &&
                isPasswordValid(password.trim())) {
            final String zone = country.replaceAll("\\+", "");
            final String pass = password.trim();
            useCase.register(phone.trim(), password.trim(), verificationCode.trim(), zone,
                    new Subscriber<NetResponseEntity<RegisterResponseEntity>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast(context.getString(R.string.network_error));
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(NetResponseEntity<RegisterResponseEntity> netResponseEntity) {
                            if (netResponseEntity != null) {
                                if (netResponseEntity.getCode() != 0) {
                                    showToast(netResponseEntity.getMessage());
                                } else {
                                    RegisterResponseEntity entity = netResponseEntity.getData();
                                    login(entity.getPhone(), pass, entity.getZone());
                                }
                            } else {
                                showToast(context.getString(R.string.network_error));
                            }
                        }
                    });
        }
    }

    /**
     * 注册后直接登录
     */
    private void login(final String phone, final String password, final String zone) {
        LoginByPhoneRequestEntity entity = new LoginByPhoneRequestEntity(phone, password,
                zone, Utils.getPhoneMac(context), false);
        this.useCase.login(entity, new Subscriber<NetResponseEntity<LoginResponseEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                showToast(context.getString(R.string.login_fail_login_again));
                backToLogin.setValue(true);
            }

            @Override
            public void onNext(NetResponseEntity<LoginResponseEntity> netResponseEntity) {
                if (netResponseEntity != null) {
                    if (netResponseEntity.getCode() == 0) {
                        LoginResponseEntity loginResponseEntity = netResponseEntity.getData();
                        PersonalInfoEntity userInfoResponseEntity = loginResponseEntity.getUserInfo();
                        if (userInfoResponseEntity.getAccountGuid() != null && loginResponseEntity.getAccessToken() != null) {
//                            SaveUtils.saveAccountInfoByPhone(context, userInfoResponseEntity.getAccountGuid(), phone,
//                                    zone, password,
//                                    loginResponseEntity.getAccessToken(), loginResponseEntity.getRefreshToken());
                            SaveUtils.saveTempAccount(context, userInfoResponseEntity.getAccountGuid(), loginResponseEntity.getAccessToken());
                            showToast(context.getString(R.string.register_success));
                            registerSuccess.setValue(true);
                        } else {
                            showToast(context.getString(R.string.login_fail_login_again));
                            backToLogin.setValue(true);
                        }
                    } else {
                        showToast(netResponseEntity.getMessage());
                    }
                } else {
                    showToast(context.getString(R.string.login_fail_login_again));
                }
            }
        });
    }

    /**
     * 手机号是否有效
     *
     * @param phone 手机号
     * @return 是否有效
     */
    private boolean isPhoneNumberValid(String phone) {
        if (phone.length() < MIN_LENGTH) {
            showToast(context.getString(R.string.activity_register_please_input_correct_phone_number));
            return false;
        }
        return true;
    }

    /**
     * 验证码是否有效
     *
     * @param verification 验证码
     * @return 是否有效
     */
    private boolean isVerificationValid(String verification) {
        if (verification.isEmpty()) {
            showToast(context.getString(R.string.activity_register_please_input_verification_code));
            return false;
        }
        return true;
    }

    /**
     * 密码是否有效
     *
     * @param password 密码
     * @return 是否有效
     */
    private boolean isPasswordValid(String password) {
        if (password.length() < MIN_LENGTH) {
            showToast(context.getString(R.string.activity_register_password_is_too_short));
            return false;
        }
        return true;
    }

    private void sendShortMessage(String phone, String zone) {
        codeSendEnable.set(false);
        this.useCase.getVerificationCode(new GetVerificationCodeEntity(phone, zone, GetVerificationCodeEntity.REGISTER), new Subscriber<NetResponseEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                codeSendEnable.set(true);
                showToast(context.getString(R.string.network_error));
            }

            @Override
            public void onNext(NetResponseEntity netResponseEntity) {
                Log.i(TAG, "onNext: " + netResponseEntity);
                if (netResponseEntity != null) {
                    if (netResponseEntity.getCode() != 0) {
                        codeSendEnable.set(true);
                        showToast(netResponseEntity.getMessage());
                    } else {
                        verificationCodeSendSuccess();
                        showToast(netResponseEntity.getMessage());
                    }
                } else {
                    showToast(context.getString(R.string.network_error));
                }
            }
        });
    }

    /**
     * 验证码不能连续发送
     */
    private void verificationCodeSendSuccess() {
        codeSendEnable.set(false);
        timer = new CountDownTimer(MyConstant.MSM_TIME_INTERVAL, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                int second = (int) (millisUntilFinished / 1000);
                btnSendText.set(String.format(Locale.getDefault(), getApplication().getString(R.string.activity_register_second_left), second));
            }

            @Override
            public void onFinish() {
                btnSendText.set(getApplication().getString(R.string.activity_register_get_verification_code));
                codeSendEnable.set(true);
            }
        };
        timer.start();
    }


    @Override
    protected void onCleared() {
        useCase.unSubscribe();
        if (timer != null) {
            timer.cancel();
        }
        super.onCleared();
    }

}
