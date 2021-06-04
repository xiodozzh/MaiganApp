package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;

import butterknife.OnClick;

/**
 *
 * @author zhaixiang
 * @date 2018/1/4
 */

public class SettingHelpActivity extends BaseActivity<DefaultViewModel> {

    public static Intent getCallingIntent(Context context){
        return new Intent(context,SettingHelpActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting_help;
    }

}
