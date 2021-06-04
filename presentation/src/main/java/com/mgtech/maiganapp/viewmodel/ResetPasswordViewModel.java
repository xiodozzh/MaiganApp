package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import android.os.CountDownTimer;

import com.mgtech.domain.entity.net.request.GetVerificationCodeEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.AccountUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;

import java.util.Locale;

import rx.Subscriber;

/**
 * @author zhaixiang
 * 重置密码 view-model
 */

public class ResetPasswordViewModel extends BaseViewModel {
    public final ObservableBoolean resetPasswordSuccess = new ObservableBoolean(false);
    public final ObservableBoolean codeSendEnable = new ObservableBoolean(true);
    private Application context;
    /**
     * 短信验证码发送成功
     */
    public final ObservableBoolean verificationCodeSendSuccess = new ObservableBoolean(false);

    public final ObservableBoolean valid = new ObservableBoolean(false);

    public final ObservableField<String> btnSendText = new ObservableField<>("");

    private AccountUseCase useCase;
    private CountDownTimer timer;

    public ResetPasswordViewModel(Application context) {
        super(context);
        this.context = context;
        this.useCase = ((MyApplication) context).getAccountUseCase();
        btnSendText.set(context.getString(R.string.reset_password_get_verification_code));
    }


    /**
     * 密码是否有效
     *
     * @param password 密码
     * @return 是否有效
     */
    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            showToast(context.getString(R.string.input_at_least_6_password));
            return false;
        }
        return true;
    }

    /**
     * 获取验证号码
     *
     * @param zone  区号
     * @param phone 手机号
     */
//    public boolean getVerificationCode(String zone, String phone) {
//        if (isPhoneNumberValid(phone.trim())) {
//            sendShortMessage(zone, phone);
//            return true;
//        }
//        return false;
//    }
    public void getVerificationCode(String zone, String phone) {
        if (!isPhoneNumberValid(phone.trim())) {
            showToast(context.getString(R.string.reset_password_input_correct_phone_number));
        }
        sendShortMessage(zone, phone);
    }


    /**
     * 手机号是否有效
     *
     * @param phone 手机号
     * @return 是否有效
     */
    private boolean isPhoneNumberValid(String phone) {
        return phone.length() >= 6;
    }

    /**
     * 通知服务器发送验证码
     *
     * @param zone  区号
     * @param phone 手机号
     */
    private void sendShortMessage(String zone, String phone) {
        codeSendEnable.set(false);
        this.useCase.getVerificationCode(new GetVerificationCodeEntity(phone, zone, GetVerificationCodeEntity.FORGET_PASSWORD), new Subscriber<NetResponseEntity>() {
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
                if (netResponseEntity != null) {
                    if (netResponseEntity.getCode() != 0) {
                        codeSendEnable.set(true);
                        showToast(netResponseEntity.getMessage());
                    } else {
                        showToast(getApplication().getString(R.string.sms_send_success));
                        verificationCodeSendSuccess();
                        verificationCodeSendSuccess.set(!verificationCodeSendSuccess.get());
                    }
                } else {
                    showToast(context.getString(R.string.network_error));
                }
            }
        });
    }

    private void verificationCodeSendSuccess() {
        timer = new CountDownTimer(MyConstant.MSM_TIME_INTERVAL, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                int second = (int) (millisUntilFinished / 1000);
                btnSendText.set(String.format(Locale.getDefault(), getApplication().getString(R.string.reset_password_second_left), second));
            }

            @Override
            public void onFinish() {
                btnSendText.set(getApplication().getString(R.string.reset_password_get_verification_code));
                codeSendEnable.set(true);
            }
        };
        timer.start();
    }

    /**
     * 重置密码
     *
     * @param password 密码
     */
    public void resetPassword(String zone, String phone, String verificationCode, final String password) {
        final String zoneNumber = zone.replaceAll("\\+", "");
        if (isPhoneNumberValid(phone) && isPasswordValid(password)) {
            this.useCase.resetPassword(phone, password.trim(), zoneNumber, verificationCode, new Subscriber<NetResponseEntity>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    showToast(context.getString(R.string.network_error));
                }

                @Override
                public void onNext(NetResponseEntity netResponseEntity) {
                    if (netResponseEntity != null) {
                        if (netResponseEntity.getCode() != 0) {
                            showToast(netResponseEntity.getMessage());
                        } else {
                            SaveUtils.savePassword(context, password);
                            showToast(context.getString(R.string.reset_password_success));
                            resetPasswordSuccess.set(!resetPasswordSuccess.get());
                        }
                    } else {
                        showToast(context.getString(R.string.network_error));
                    }
                }
            });
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
        if (timer != null) {
            timer.cancel();
        }
    }

}
