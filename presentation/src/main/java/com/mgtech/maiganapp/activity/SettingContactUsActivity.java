package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;
import com.mgtech.maiganapp.viewmodel.SettingAboutViewModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * @author zhaixiang
 * 联系我们
 */

public class SettingContactUsActivity extends BaseActivity<DefaultViewModel> {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SettingContactUsActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting_contact_us;
    }

}
