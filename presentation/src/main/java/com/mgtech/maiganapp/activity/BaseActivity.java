package com.mgtech.maiganapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.rx.GoToLoginEvent;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.BR;
import com.mgtech.maiganapp.BleApplication;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.FirmwareUpgradeEvent;
import com.mgtech.maiganapp.data.event.LogoutEvent;
import com.mgtech.maiganapp.data.model.SetAlarmModel;
import com.mgtech.maiganapp.utils.PermissionUtils;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.BaseViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

/**
 * @author zhaixiang
 * @date 2017/1/3
 * base
 */

public abstract class BaseActivity<T extends BaseViewModel> extends AppCompatActivity {
    protected static final String TAG = "BaseActivity";
    protected ViewDataBinding binding;
    protected T viewModel;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;
    protected static final int REQUEST_ENABLE_LOCATION = 200;
    protected static final int REQUEST_EXTERNAL_STORAGE = 300;
    protected static final int REQUEST_READ_CONTACT = 400;
    protected static final int REQUEST_CAMERA = 500;
    protected static final int REQUEST_CALL = 600;
    private static final int REQUEST_LOCATION_STORAGE = 700;
    protected static final int REQUEST_PERMISSION_ACTIVITY = 800;
    protected static final int REQUEST_LOCATION_PERMISSION = 900;

    protected static final int ENABLE_BLE_REQUEST = 1;
    private ProgressDialog dialog;
    private static long toastTime;
    private AlertDialog deadDialog;
    protected AlertDialog localDialog;
    private AlertDialog logoutDialog;
    private AlertDialog goToLoginDialog;
    private AlertDialog loginFailDialog;
    private static final long TOAST_TIME = 2000;
    // 系统的ScaledDensity
    private static float sNoncompatScaledDensity;
    private static float sNoncompatDensity;
    private static float sFontScale;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomDensity(this, getApplication());
        binding = DataBindingUtil.setContentView(this, getContentViewId());
        viewModel = ViewModelProviders.of(this).get(getModelClass());
        binding.setVariable(BR.model, viewModel);
//        viewModel.toastField.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable observable, int i) {
//                showShortToast(viewModel.toastText);
//            }
//        });
        viewModel.toastField.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showShortToast(s);
            }
        });
        viewModel.showDialogField.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (viewModel.showDialogField.get()) {
                    showDialog();
                } else {
                    cancelDialog();
                }
            }
        });
        viewModel.reLoginFail.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                showLoginFailDialog();
            }
        });
        viewModel.reLoginNetworkError.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                showLogoutDialog(getString(R.string.your_account_is_login_on_other_device));
            }
        });
        ButterKnife.bind(this);
        init(savedInstanceState);
    }

    public static void setCustomDensity(AppCompatActivity activity, Application application) {
        DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();
        float fontScale = application.getResources().getConfiguration().fontScale;
        if (sNoncompatDensity == 0 || sFontScale != fontScale) {
            sNoncompatDensity = displayMetrics.density;
            sNoncompatScaledDensity = displayMetrics.scaledDensity / fontScale;
            sFontScale = fontScale;

            // 监听在系统设置中切换字体
//            application.registerComponentCallbacks(new ComponentCallbacks() {
//                @Override
//                public void onConfigurationChanged(Configuration newConfig) {
//                    if (newConfig != null && newConfig.fontScale > 0) {
//                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
//                    }
//                }
//
//                @Override
//                public void onLowMemory() {
//
//                }
//            });
        }
        float designWidth = 375;
        float targetDensity = displayMetrics.widthPixels / designWidth;
        float targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        int targetDensityDpi = (int) (160 * targetDensity);
        displayMetrics.density = targetDensity;
        displayMetrics.scaledDensity = targetScaledDensity * Utils.getFontSizeScale(SaveUtils.getFontSizeIndex(application));
        displayMetrics.densityDpi = targetDensityDpi;

        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity * Utils.getFontSizeScale(SaveUtils.getFontSizeIndex(application));
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    public void showShortToast(String text) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - toastTime > TOAST_TIME) {
            Toast.makeText(BaseActivity.this, text, Toast.LENGTH_SHORT).show();
            toastTime = currentTime;
        }
    }

//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
//            Configuration config = res.getConfiguration();
//            config.fontScale = Utils.getFontSizeScale(SaveUtils.getFontSizeIndex(this));
//            res.updateConfiguration(config, res.getDisplayMetrics());
//        }
//        return res;
//    }
//
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
//            final Resources res = newBase.getResources();
//            final Configuration config = res.getConfiguration();
//            config.fontScale = Utils.getFontSizeScale(SaveUtils.getFontSizeIndex(newBase));
//            final Context newContext = newBase.createConfigurationContext(config);
//            super.attachBaseContext(newContext);
//        } else {
//            super.attachBaseContext(newBase);
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void logout(LogoutEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        String text;
        if (TextUtils.isEmpty(event.getDeviceName())) {
            text = getString(R.string.your_account_is_login_on_other_device);
        } else {
            text = String.format(Locale.getDefault(), getString(R.string.format_your_account_is_login_on_device), event.getDeviceName());
        }
        new BraceletInfoManagerBuilder(this).create().deletePairInformation();
//        BluetoothService2.disConnect(this);
        ((BleApplication)getApplication()).getRequest().disconnect();
        showLogoutDialog(text);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void goToLogin(GoToLoginEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        String text;
        if (!TextUtils.isEmpty(SaveUtils.getToken(getApplicationContext()))) {
            return;
        }
        new BraceletInfoManagerBuilder(this).create().deletePairInformation();
        ((BleApplication)getApplication()).getRequest().disconnect();
        showGoToLoginDialog();
    }

    private void showGoToLoginDialog() {
        if (goToLoginDialog != null && goToLoginDialog.isShowing()) {
            return;
        }
        dismissAll();
        goToLoginDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.logout_msg)
                .setMessage(getString(R.string.base_login_again_message))
                .setPositiveButton(getString(R.string.base_go_to_login), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .create();
        goToLoginDialog.show();
    }

    private void showLogoutDialog(String text) {
        if (logoutDialog != null && logoutDialog.isShowing()) {
            return;
        }
        dismissAll();
        logoutDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.logout_msg)
                .setMessage(text)
                .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setPositiveButton(R.string.login_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.reLogin();
                    }
                })
                .create();
        logoutDialog.show();
    }

    /**
     * 退出登录
     */
    private void logout() {
        SaveUtils.deleteUserInfo(getApplication());
        new BraceletInfoManagerBuilder(getApplicationContext()).create().deletePairInformation();
        startActivity(LoginActivity.getCallingIntent(BaseActivity.this, true));
        ((MyApplication) getApplication()).closePush();
        finish();
    }

    private void dismissAll() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (deadDialog != null && deadDialog.isShowing()) {
            deadDialog.dismiss();
        }
        if (localDialog != null && localDialog.isShowing()) {
            localDialog.dismiss();
        }
        if (logoutDialog != null && logoutDialog.isShowing()) {
            logoutDialog.dismiss();
        }
        if (loginFailDialog != null && loginFailDialog.isShowing()) {
            loginFailDialog.dismiss();
        }
        if (goToLoginDialog != null && goToLoginDialog.isShowing()) {
            goToLoginDialog.dismiss();
        }

    }

    private void showLoginFailDialog() {
        if (loginFailDialog != null && loginFailDialog.isShowing()) {
            return;
        }
        loginFailDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.activity_base_password_error)
                .setMessage(R.string.activity_base_please_login_again)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new SetAlarmModel(BaseActivity.this).stopAndRemoveAllReminders();
                        SaveUtils.deleteUserInfo(getApplication());
                        startActivity(LoginActivity.getCallingIntent(BaseActivity.this, true));
                        finish();
                    }
                })
                .create();
        loginFailDialog.show();
    }


    private void showDialog() {
        cancelDialog();
        dialog = ProgressDialog.show(this, viewModel.dialogTitle, viewModel.dialogText, true, false);
    }

    private void cancelDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

//    public void showProcessingDialog(String text,String text){
//        cancelDialog();
//        dialog = ProgressDialog.show(this, text, text, true, false);
//    }
//
//    public void hideProcessingDialog(){
//        cancelDialog();
//    }

    protected void hideActionbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (getActionBar() != null) {
            getActionBar().hide();
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        dismissAll();
        ViewUtils.fixInputMethodManagerLeak(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel.isPaired()) {
            ((BleApplication)getApplication()).getRequest().linkIfAvailable();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goToUpgrade(FirmwareUpgradeEvent event) {
        if (this instanceof FirmwareUpgradeActivity || this instanceof BraceletPairActivity) {
            return;
        }
        Log.i(TAG, "goToUpgrade: ");
        startActivity(FirmwareUpgradeActivity.getCallingIntent(BaseActivity.this, false));
    }

    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }


    /**
     * 设置状态栏透明
     */
    public void setStatusBarTranslucent() {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    /**
     * 初始化
     *
     * @param savedInstanceState 保存信息
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 获取layout资源地址id
     *
     * @return layout planId
     */
    protected abstract int getContentViewId();

//    protected abstract T getModel();

    @SuppressWarnings("unchecked")
    protected Class<T> getModelClass() {
        Type type = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) type).getActualTypeArguments();
        return (Class<T>) params[0];
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorRes 色彩
     */
    public void setStatusBarColor(@ColorRes int colorRes) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, colorRes));
    }

    /**
     * 设置屏幕变暗
     *
     * @param bgAlpha 1 最亮 0 最暗
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }


    public void askForPermission(){
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (SaveUtils.getPermissionAccessStorage()){
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        PermissionUtils.openPermission(this,permissions,REQUEST_LOCATION_STORAGE);
    }


    /**
     * 判断位置信息是否开启
     */
    protected boolean isLocationOpen() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //gps定位
        boolean isGpsProvider = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //网络定位
        boolean isNetWorkProvider = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isGpsProvider || isNetWorkProvider;
    }





    private void showDeadDialog() {
        if (deadDialog != null && deadDialog.isShowing()) {
            return;
        }
        deadDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.caution)
                .setMessage(R.string.bluetooth_can_not_work_warning)
                .setPositiveButton(R.string.ok, null).create();
        deadDialog.show();
    }


    protected void openPush() {
        ((MyApplication) getApplication()).openPush();
    }

}
