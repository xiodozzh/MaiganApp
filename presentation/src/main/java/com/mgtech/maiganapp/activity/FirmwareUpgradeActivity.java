package com.mgtech.maiganapp.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.blelib.entity.FirmStateData;
import com.mgtech.blelib.entity.FirmwareUpgradeData;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.FirmwareUpgradeResult;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.FirmwareUpgradeViewModel;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.Observable;
import androidx.lifecycle.Observer;

import java.util.Locale;

import butterknife.Bind;

/**
 * @author zhaixiang
 */
public class FirmwareUpgradeActivity extends BleActivity<FirmwareUpgradeViewModel> {
    @Bind(R.id.iv_progress)
    ImageView ivProgress;
    @Bind({R.id.iv_arrow0,R.id.iv_arrow1,R.id.iv_arrow2,R.id.iv_arrow3,R.id.iv_arrow4,R.id.iv_arrow5})
    ImageView[] ivArrows;
    @Bind(R.id.tv_progress)
    TextView tvProgress;
    private ObjectAnimator rotateAnimator;
    private ValueAnimator switchAnimator;
    private AlertDialog alertDialog;
    private static final String IS_DEBUG = "debug";
    private Bitmap arrowBlue;
    private Bitmap arrowGrey;

    public static Intent getCallingIntent(Context context, boolean isDebug) {
        Intent intent = new Intent(context, FirmwareUpgradeActivity.class);
        intent.putExtra(IS_DEBUG, isDebug);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        int sideLength = ViewUtils.dp2px(12);
        arrowBlue = ViewUtils.decodeSampleBitmapFromResource(getResources(),R.drawable.firmware_upgrade_arrow_blue,sideLength,sideLength);
        arrowGrey = ViewUtils.decodeSampleBitmapFromResource(getResources(),R.drawable.firmware_upgrade_arrow_grey,sideLength,sideLength);
        viewModel.setIsDebug(getIntent().getBooleanExtra(IS_DEBUG, false));
//        viewModel.progress.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable observable, int i) {
//                progressBar.setProgress(viewModel.progress.get() / (float) viewModel.total);
//            }
//        });
        viewModel.upgradeFinish.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                showShortToast(getString(R.string.upgrade_success));
                EventBus.getDefault().post(new FirmwareUpgradeResult(true));
                setResult(RESULT_OK);
                finish();
            }
        });
        viewModel.upgrading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.upgrading.get()){
                    startUpgrade();
                }else{
                    stopUpgrade();
                }
            }
        });
        viewModel.upgradeFail.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                showFailDialog(viewModel.failReason);
            }
        });
        viewModel.powerLow.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                showFailDialog(getString(R.string.battery_low_please_try_after_charging));
            }
        });
//        if (Utils.isLanguageChinese()) {
//            sdvHeader.setImageResource(R.drawable.firmware_upgrade_header_bg_ch);
//        } else {
//            sdvHeader.setImageResource(R.drawable.firmware_upgrade_header_bg_en);
//        }
//        SpannableString ss = new SpannableString(getString(R.string.activity_firmware_upgrade_bracelet_upgrade_log));
//        int appNameNumber = Utils.isLanguageChinese() ? 3 : 8;
//        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#1b9dce")), 0, appNameNumber, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tvTitle.setText(ss);
        viewModel.registerBleCallback();
        viewModel.firmProgressLiveData.observe(this, new Observer<FirmStateData>() {
            @Override
            public void onChanged(FirmStateData value) {
                switch (value.getStatus()){
                    case FirmStateData.START:
                        tvProgress.setText("0%");
                        break;
                    case FirmStateData.CONTINUE:
                        String p = Math.round(value.getProgress()* 100/(float)value.getTotal()) + "%";
                        tvProgress.setText(p);
                        break;
                    case FirmStateData.END:
                        tvProgress.setText("100%");
                        break;
                    default:
                        break;
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getFirmwareFile();
    }

    @Override
    protected void openBleFail() {
    }

    @Override
    protected void openBleSuccess() {
    }

    @Override
    protected boolean doesAutoSetRegisterBleCallback() {
        return false;
    }

    private void startUpgrade(){
        getRotateAnimator().start();
        getSwitchAnimator().start();
    }

    private ObjectAnimator getRotateAnimator(){
        if (rotateAnimator == null){
            rotateAnimator = ObjectAnimator.ofFloat(ivProgress,"rotation",0,360);
            rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            rotateAnimator.setDuration(1000);
            rotateAnimator.setInterpolator(new LinearInterpolator());
        }
        return rotateAnimator;
    }

    private ValueAnimator getSwitchAnimator(){
        if (switchAnimator == null){
            switchAnimator = ValueAnimator.ofInt(0,6);
            switchAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            switchAnimator.setDuration(600);
            switchAnimator.setInterpolator(new LinearInterpolator());
            switchAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (ivArrows == null){
                        return;
                    }
                    for (int i = 0; i < ivArrows.length; i++) {
                        if (i== (int)animation.getAnimatedValue()){
                            ivArrows[i].setImageBitmap(arrowBlue);
                        }else{
                            ivArrows[i].setImageBitmap(arrowGrey);
                        }
                    }
                }
            });
        }
        return switchAnimator;
    }


    private void stopUpgrade(){
        getRotateAnimator().cancel();
        getSwitchAnimator().cancel();
    }


    @Override
    public void onBackPressed() {
        if (viewModel.canGoBack){
            super.onBackPressed();
            return;
        }
        showShortToast(getString(R.string.upgrading_please_wait));
    }


    private void showFailDialog(final String reason) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        if (TextUtils.isEmpty(reason)) {
            return;
        }
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.caution)
                .setCancelable(false)
                .setMessage(reason)
                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post(new FirmwareUpgradeResult(false));
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                })
                .create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        viewModel.unRegisterBleCallback();
        stopUpgrade();
        super.onDestroy();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_firmware_upgrade;
    }

}
