package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.Observable;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.SettingAboutViewModel;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 *
 * @author zhaixiang
 * 意见反馈
 */

public class SettingAboutActivity extends BaseActivity<SettingAboutViewModel> {
    @Bind(R.id.tv_version)
    TextView tvVersion;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SettingAboutActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        viewModel.initVersion();
    }

    @OnClick(R.id.btn_upgrade_version)
    void upgrade(){
        if (viewModel.hasNewVersion) {
            String url = viewModel.getUrl();
            if (url != null && !url.isEmpty()) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }else{
            showShortToast(getString(R.string.current_version_is_new));
        }
    }


    @OnClick({R.id.tv_privacy0,R.id.tv_privacy2})
    void privacy(){
        startActivity(ProtocolActivity.getCallingIntent(this));
    }

//    @OnClick(R.id.layout_version)
//    void clickVersion() {
//        viewModel.getVersion();
//    }


    private void showUpdateDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.find_new_version_need_upgrade)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = viewModel.getUrl();
                        if (url != null && !url.isEmpty()) {
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }
                }).create();
        dialog.show();
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting_about;
    }

}
