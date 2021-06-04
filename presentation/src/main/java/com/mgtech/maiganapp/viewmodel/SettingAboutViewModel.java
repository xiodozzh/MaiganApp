package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.mgtech.blelib.utils.Utils;
import com.mgtech.domain.entity.AppConfigNew;
import com.mgtech.domain.entity.net.response.GetAppConfigResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.AppConfigUseCase;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;

import rx.Subscriber;

/**
 *
 * @author zhaixiang
 * @date 2018/11/1
 * 关于
 */

public class SettingAboutViewModel extends BaseViewModel {
    private AppConfigUseCase useCase;
    public final ObservableBoolean getVersionSuccess = new ObservableBoolean(false);
    public final ObservableField<String> localVersion = new ObservableField<>("");
    public boolean hasNewVersion = true;
    private String url;

    public SettingAboutViewModel(Application context) {
        super(context);
        this.useCase = ((MyApplication)context).getAppConfigUseCase();
    }

    public void initVersion(){
        localVersion.set(getApplication().getString(R.string.setting_app_version)+Utils.getVersionName(getApplication()));
        url = AppConfigNew.getAppUrl(getApplication());
        hasNewVersion = (AppConfigNew.getVersion(getApplication()) > Utils.getVersionCode(getApplication()));
    }

//    /**
//     * 获取版本信息
//     */
//    public void getVersion(){
//        this.useCase.getAppConfig(new Subscriber<NetResponseEntity<GetAppConfigResponseEntity>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                showToast(getApplication().getString(R.string.network_error));
//            }
//
//            @Override
//            public void onNext(NetResponseEntity<GetAppConfigResponseEntity> netResponseEntity) {
//                if (netResponseEntity != null) {
//                    if (netResponseEntity.getCode() == 0) {
//                        GetAppConfigResponseEntity entity = netResponseEntity.getData();
//                        if (entity != null) {
//                            AppConfigNew.saveToLocal(context,entity);
////                            GetAppConfigResponseEntity.AndroidInfo androidInfo = entity.getAndroidInfo();
//                            if (Utils.getVersionCode(context) < AppConfigNew.getVersion(context)) {
//                                hasNewVersion.set(true);
//                                getVersionSuccess.set(!getVersionSuccess.get());
//                                url = AppConfigNew.getAppUrl(context);
//                            } else {
//                                showToast(context.getString(R.string.current_version_is_new));
//                                hasNewVersion.set(false);
//                            }
//                        }else{
//                            showToast(context.getString(R.string.network_error));
//                        }
//                    } else {
//                        showToast(netResponseEntity.getMessage());
//                    }
//                }else{
//                    showToast(context.getString(R.string.network_error));
//                }
//            }
//        });
//    }

    public String getUrl(){
        return url;
    }


}
