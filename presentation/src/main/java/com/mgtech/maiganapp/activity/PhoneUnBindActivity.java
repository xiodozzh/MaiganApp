package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.PhoneUnBindViewModel;

import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class PhoneUnBindActivity extends BaseActivity<PhoneUnBindViewModel>{

    public static Intent getCallingIntent(Context context){
        return new Intent(context,PhoneUnBindActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getPhoneNumber();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_phone_unbind;
    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }

    @OnClick(R.id.btn_unbind)
    void unbind(){
        startActivity(PhoneBindActivity.getCallingIntent(this));
        finish();
    }
}
