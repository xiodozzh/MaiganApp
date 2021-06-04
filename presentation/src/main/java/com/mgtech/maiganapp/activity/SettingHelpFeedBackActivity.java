package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;
import com.mgtech.maiganapp.viewmodel.SettingGeneralViewModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Jesse
 */
public class SettingHelpFeedBackActivity extends BaseActivity<DefaultViewModel>{

    public static Intent getCallingIntent(Context context){
        return new Intent(context, SettingHelpFeedBackActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
    }

    @OnClick(R.id.layout_feedback)
    void clickFeedback(){
        startActivity(SettingContactUsActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_help)
    void clickHelp(){
        startActivity(SettingHelpActivity.getCallingIntent(this));
    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting_help_and_feedback;
    }
}
