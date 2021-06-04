package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.SocketEvent;
import com.mgtech.maiganapp.service.BluetoothService;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

/**
 * @author Jesse
 */
public class SettingActivity extends BaseActivity<DefaultViewModel>{
    public static Intent getCallingIntent(Context context){
        return new Intent(context,SettingActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @OnClick(R.id.btn_logout)
    void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.caution)
                .setMessage(R.string.do_you_want_logout)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SaveUtils.deleteUserInfo(getApplication());
                        BluetoothService.stopMeasureService(SettingActivity.this);
                        EventBus.getDefault().postSticky(new SocketEvent(false));
                        ((MyApplication)getApplication()).closePush();
                        Intent intent = MainActivity.getLogoutIntent(SettingActivity.this);
                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    @OnClick(R.id.layout_account_security)
    void clickAccountSecurity(){
        startActivity(AccountSecurityActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_privacy)
    void clickPrivacy(){
        startActivity(PrivacyActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_general)
    void clickGeneral(){
        startActivity(SettingGeneralActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_help)
    void clickHelp(){
        startActivity(SettingHelpFeedBackActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_about)
    void clickAbout(){
        startActivity(SettingAboutActivity.getCallingIntent(this));
    }
}
