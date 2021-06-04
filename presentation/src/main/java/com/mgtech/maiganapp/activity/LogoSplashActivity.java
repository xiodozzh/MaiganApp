package com.mgtech.maiganapp.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mgtech.maiganapp.service.BluetoothService;

/**
 * @author zhaixiang
 */
public class LogoSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 注意, 这里并没有setContentView, 单纯只是用来跳转到相应的Activity.
        // 目的是减少首屏渲染
        if (BluetoothService.isServiceOn(this)) {
            startActivity(MainActivity.getCallingIntent(this));
        } else {
            startActivity(EnterActivity.getCallingIntent(this));
        }

        finish();
    }
}
