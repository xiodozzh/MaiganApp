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
import com.mgtech.maiganapp.data.model.SetAlarmModel;
import com.mgtech.maiganapp.service.BluetoothService;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

/**
 *
 * @author zhaixiang
 * @date 2018/1/4
 * 设置其他
 */

public class OtherActivity extends BaseActivity<DefaultViewModel> {

    public static Intent getCallingIntent(Context context){
        return new Intent(context,OtherActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }

    @OnClick(R.id.layout_feedback)
    void clickFeedback(){
        startActivity(SettingFeedbackActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_user_protocol)
    void clickUser(){
        startActivity(ProtocolActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_help)
    void clickHelp(){
        startActivity(SettingHelpActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_about)
    void clickAbout(){
        startActivity(SettingAboutActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_option)
    void clickOption(){
        startActivity(OptionActivity.getCallingIntent(this));
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
                        new SetAlarmModel(OtherActivity.this).stopAndRemoveAllReminders();
                        BluetoothService.stopMeasureService(OtherActivity.this);
                        EventBus.getDefault().postSticky(new SocketEvent(false));
                        ((MyApplication)getApplication()).closePush();
                        Intent intent = MainActivity.getLogoutIntent(OtherActivity.this);
                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_other;
    }

}
