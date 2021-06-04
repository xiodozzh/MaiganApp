package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mgtech.domain.entity.net.request.SetCustomerContactPermissionRequestEntity;
import com.mgtech.domain.entity.net.response.GetCustomerContactPermissionResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.ServeUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.SystemPermissionModel;
import com.mgtech.maiganapp.utils.PermissionUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author Jesse
 */
public class SystemPermissionViewModel extends BaseViewModel {
    public MutableLiveData<List<SystemPermissionModel>> data = new MutableLiveData<>();
    private ServeUseCase serveUseCase;
    private boolean contactPermissionOpen;

    public SystemPermissionViewModel(@NonNull Application application) {
        super(application);
        contactPermissionOpen =  SaveUtils.doesAllowCustomerServiceContactByPhone();
        serveUseCase = ((MyApplication) application).getServeUseCase();
    }

    public void init() {
        List<SystemPermissionModel> modelList = new ArrayList<>();
        modelList.add(new SystemPermissionModel(getApplication().getString(R.string.system_permission_description)));
        modelList.add(new SystemPermissionModel(getApplication().getString(R.string.system_permission_notification_title),
                getApplication().getString(R.string.system_permission_notification_content),
                SystemPermissionModel.PERMISSION_NOTIFICATION, PermissionUtils.isNotificationPermissionOpen(getApplication())));
        modelList.add(new SystemPermissionModel(getApplication().getString(R.string.system_permission_storage_title),
                getApplication().getString(R.string.system_permission_storage_content),
                SystemPermissionModel.PERMISSION_STORAGE, PermissionUtils.isStoragePermissionOpen(getApplication())));
        modelList.add(new SystemPermissionModel(getApplication().getString(R.string.system_permission_location_title),
                getApplication().getString(R.string.system_permission_location_content),
                SystemPermissionModel.PERMISSION_LOCATION, PermissionUtils.isLocationPermissionOpen(getApplication())));
        modelList.add(new SystemPermissionModel(getApplication().getString(R.string.system_permission_camera_title),
                getApplication().getString(R.string.system_permission_camera_content),
                SystemPermissionModel.PERMISSION_CAMERA, PermissionUtils.isCameraPermissionOpen(getApplication())));
        modelList.add(new SystemPermissionModel(getApplication().getString(R.string.system_permission_read_contact_title),
                getApplication().getString(R.string.system_permission_read_contact_content),
                SystemPermissionModel.PERMISSION_CONTACT, PermissionUtils.isContactPermissionOpen(getApplication())));
        modelList.add(new SystemPermissionModel("允许脉迹授权客服主动联系",
                "允许脉迹授权客服人员通过您的注册手机号码主动联系您或邀请您成为微信好友，为您提供脉迹产品使用支持服务",
                contactPermissionOpen));
        data.setValue(modelList);
    }

    public void getCustomerContactPermission() {
        serveUseCase.getCustomerServiceContactPermission(SaveUtils.getUserId(),
                new Subscriber<NetResponseEntity<GetCustomerContactPermissionResponseEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(NetResponseEntity<GetCustomerContactPermissionResponseEntity> netResponseEntity) {
                        if (netResponseEntity.getCode() == 0) {
                            GetCustomerContactPermissionResponseEntity entity = netResponseEntity.getData();
                            if (entity != null) {
                                contactPermissionOpen = entity.getAllowByPhone() == GetCustomerContactPermissionResponseEntity.ALLOW;
                                init();
                            }
                        }
                    }
                });
    }

    public void setCustomerContactPermission(boolean checked){
        if (checked == contactPermissionOpen){
            return;
        }
        SetCustomerContactPermissionRequestEntity entity = new SetCustomerContactPermissionRequestEntity();
        entity.setAccountGuid(SaveUtils.getUserId());
        entity.setAllowByPhone(checked ?
                SetCustomerContactPermissionRequestEntity.ALLOW: SetCustomerContactPermissionRequestEntity.DENY);
        entity.setAllowByWechat(checked ?
                SetCustomerContactPermissionRequestEntity.ALLOW: SetCustomerContactPermissionRequestEntity.DENY);
        serveUseCase.setCustomerServiceContactPermission(entity, new Subscriber<NetResponseEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(getApplication().getString(R.string.network_error));
                init();
            }

            @Override
            public void onNext(NetResponseEntity netResponseEntity) {
                Logger.i("setCustomerContactPermission:"+ netResponseEntity);
                if (netResponseEntity.getCode() == 0) {
                    SaveUtils.setAllowCustomerContact(checked);
                    contactPermissionOpen = checked;
                }else{
                    showToast(netResponseEntity.getMessage());
                }
                init();
            }
        });
    }


}
