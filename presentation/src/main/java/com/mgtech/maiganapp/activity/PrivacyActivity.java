package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;
import com.mgtech.maiganapp.viewmodel.SettingAuthManagementViewModel;

import butterknife.OnClick;

/**
 * @author Jesse
 */
public class PrivacyActivity extends BaseActivity<DefaultViewModel>{
    public static Intent getCallingIntent(Context context){
        return new Intent(context, PrivacyActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_privacy;
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @OnClick(R.id.layout_friend)
    void clickFriendPrivacy(){
        startActivity(PrivacyFriendActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_system)
    void clickSystemPrivacy(){
        startActivity(SettingSystemPermissionActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_auth_management)
    void clickAuthManagement(){
        startActivity(SettingAuthManagementActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_user)
    void clickPoliticsUser(){
        startActivity(PermissionProtocolActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_politics)
    void clickPoliticsPrivacy(){
        startActivity(PermissionPrivacyActivity.getCallingIntent(this));
    }

}
