package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.OptionViewModel;

import butterknife.Bind;
import butterknife.OnClick;

public class OptionActivity extends BaseActivity<OptionViewModel> {
    @Bind(R.id.sw_emergency)
    SwitchCompat swEmergency;

    public static Intent getCallingIntent(Context context){
        return new Intent(context,OptionActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        swEmergency.setChecked(viewModel.getEmergencyHide());
        swEmergency.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setEmergencyHide(isChecked);
        });
    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_option;
    }
}
