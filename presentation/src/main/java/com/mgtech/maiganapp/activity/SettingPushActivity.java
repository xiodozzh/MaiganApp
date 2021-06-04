package com.mgtech.maiganapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.ThresholdModel;
import com.mgtech.maiganapp.viewmodel.SettingPushViewModel;

import androidx.lifecycle.Observer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * @author zhaixiang
 * @date 2018/1/3
 * 手环信息
 */

public class SettingPushActivity extends BaseActivity<SettingPushViewModel> {
    public static final int SET_RANGE_REQUEST = 300;
    @Bind(R.id.sw_ps)
    Switch swPs;
    @Bind(R.id.sw_pd)
    Switch swPd;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @Bind(R.id.layout_refresh_error)
    SwipeRefreshLayout layoutRefreshEmpty;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SettingPushActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        viewModel.psOn.observe(this, on -> {
            if (swPs.isChecked() != on) {
                swPs.setChecked(on);
            }
        });
        viewModel.pdOn.observe(this, on -> {
            if (swPd.isChecked() != on) {
                swPd.setChecked(on);
            }
        });
        viewModel.loading.observe(this, loading -> {
            layoutRefresh.setRefreshing(loading);
            layoutRefreshEmpty.setRefreshing(loading);
        });
        layoutRefresh.setOnRefreshListener(() -> viewModel.getThreshold());
        layoutRefreshEmpty.setOnRefreshListener(() -> viewModel.getThreshold());
        initSwitchBtn();
        viewModel.getThreshold();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getNotificationAuth();
    }

    @OnClick(R.id.layout_notification_auth)
    public void goToSetAuth(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startActivity(new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS));
            startActivity(new Intent(Settings.ACTION_SETTINGS));
        }else{
            startActivity(new Intent(Settings.ACTION_SETTINGS));
        }
    }

    private void initSwitchBtn() {
        swPs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != viewModel.thresholdModel.openPs) {
                    viewModel.setPsOn(isChecked);
                }
            }
        });
        swPd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != viewModel.thresholdModel.openPd) {
                    viewModel.setPdOn(isChecked);
                }
            }
        });
    }

    @OnClick({R.id.layout_pd, R.id.layout_ps})
    void setAbnormalPush(View view) {
        int type;
        if (view.getId() == R.id.layout_pd) {
            type = SetAbnormalRangeActivity.PD;
        } else {
            type = SetAbnormalRangeActivity.PS;
        }
        if (viewModel.thresholdModel == null) {
            return;
        }
        Intent intent = SetAbnormalRangeActivity.getCallingIntent(this, type, viewModel.thresholdModel);
        startActivityForResult(intent, SET_RANGE_REQUEST);
    }


    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting_push;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_RANGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                showShortToast(getString(R.string.set_success));
                viewModel.setModel(data.getParcelableExtra(SetAbnormalRangeActivity.ENTITY));
            }
        }
    }
}
