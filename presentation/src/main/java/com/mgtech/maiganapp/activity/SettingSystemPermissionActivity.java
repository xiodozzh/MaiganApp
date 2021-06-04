package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.SystemPermissionAdapter;
import com.mgtech.maiganapp.data.model.SystemPermissionModel;
import com.mgtech.maiganapp.utils.PermissionUtils;
import com.mgtech.maiganapp.viewmodel.SystemPermissionViewModel;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Jesse
 */
public class SettingSystemPermissionActivity extends BaseActivity<SystemPermissionViewModel> {
    private static final int REQUEST_EXTERNAL_STORAGE = 371;
    private static final int REQUEST_LOCATION = 561;
    private static final int REQUEST_CAMERA = 572;
    private static final int REQUEST_CONTACT = 699;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private SystemPermissionAdapter adapter;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SettingSystemPermissionActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        viewModel.data.observe(this, new Observer<List<SystemPermissionModel>>() {
            @Override
            public void onChanged(List<SystemPermissionModel> systemPermissionModels) {
                adapter.setData(systemPermissionModels);
            }
        });
        adapter = new SystemPermissionAdapter(recyclerView, new SystemPermissionAdapter.Listener() {
            @Override
            public void selectType(SystemPermissionModel model) {
                if (model.isOpen()) {
                    goToAppDetailSettingIntent(SettingSystemPermissionActivity.this);
                } else {
                    openPermission(model.getPermissionType());
                }
            }

            @Override
            public void checkedCustomer(boolean checked) {
                viewModel.setCustomerContactPermission(checked);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.init();
        viewModel.getCustomerContactPermission();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting_system_permission;
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }


    /**
     * 跳转到权限设置界面
     */
    private void goToAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }

    public void openPermission(int type) {
        switch (type) {
            case SystemPermissionModel.PERMISSION_NOTIFICATION:
                PermissionUtils.goToAppDetailSettingIntent(this);
                break;
            case SystemPermissionModel.PERMISSION_STORAGE:
                PermissionUtils.requestStoragePermission(this,REQUEST_EXTERNAL_STORAGE);
                break;
            case SystemPermissionModel.PERMISSION_LOCATION:
                PermissionUtils.requestLocationPermission(this,REQUEST_LOCATION);
                break;
            case SystemPermissionModel.PERMISSION_CAMERA:
                PermissionUtils.requestCameraPermission(this,REQUEST_CAMERA);
                break;
            case SystemPermissionModel.PERMISSION_CONTACT:
                PermissionUtils.requestReadContactPermission(this,REQUEST_CONTACT);
                break;
            default:
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        viewModel.init();
        if (grantResults[0] == PackageManager.PERMISSION_DENIED){
            goToAppDetailSettingIntent(this);
        }
    }

}
