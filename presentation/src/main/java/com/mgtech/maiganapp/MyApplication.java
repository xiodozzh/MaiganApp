package com.mgtech.maiganapp;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.Nullable;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.mgtech.blelib.biz.AsyncBleCenter;
import com.mgtech.blelib.biz.IBizController;
import com.mgtech.data.repository.ContactRepository;
import com.mgtech.data.repository.WeeklyReportRepository;
import com.mgtech.data.repository.retrofit.AccountRepository;
import com.mgtech.data.repository.retrofit.AppConfigRepository;
import com.mgtech.data.repository.retrofit.DeviceRepository;
import com.mgtech.data.repository.retrofit.EcgDataRepository;
import com.mgtech.data.repository.retrofit.ExceptionRepository;
import com.mgtech.data.repository.retrofit.FileRepository;
import com.mgtech.data.repository.retrofit.HealthManagementRepository;
import com.mgtech.data.repository.retrofit.IndicatorRepository;
import com.mgtech.data.repository.retrofit.MedicineRepository;
import com.mgtech.data.repository.retrofit.NotificationRepository;
import com.mgtech.data.repository.retrofit.PersonalInfoRepository;
import com.mgtech.data.repository.retrofit.RelationRepository;
import com.mgtech.data.repository.retrofit.ServeRepository;
import com.mgtech.data.repository.retrofit.SingleSignOnRepository;
import com.mgtech.data.repository.retrofit.StepRepository;
import com.mgtech.data.repository.retrofit.UnreadMessageRepository;
import com.mgtech.domain.interactor.AccountUseCase;
import com.mgtech.domain.interactor.AppConfigUseCase;
import com.mgtech.domain.interactor.ContactUseCase;
import com.mgtech.domain.interactor.DataUseCase;
import com.mgtech.domain.interactor.DeviceUseCase;
import com.mgtech.domain.interactor.EcgUseCase;
import com.mgtech.domain.interactor.ExceptionUseCase;
import com.mgtech.domain.interactor.FileUseCase;
import com.mgtech.domain.interactor.HealthManagementUseCase;
import com.mgtech.domain.interactor.MedicineUseCase;
import com.mgtech.domain.interactor.NotificationUseCase;
import com.mgtech.domain.interactor.PersonalInfoUseCase;
import com.mgtech.domain.interactor.RelationUseCase;
import com.mgtech.domain.interactor.ServeUseCase;
import com.mgtech.domain.interactor.SingleSignOnUseCase;
import com.mgtech.domain.interactor.StepUseCase;
import com.mgtech.domain.interactor.UnreadMessageUseCase;
import com.mgtech.domain.interactor.WeeklyReportUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.data.event.AppForegroundEvent;
import com.mob.MobSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mmkv.MMKV;
import com.tencent.tauth.Tencent;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;

/**
 * @author zhaixiang
 */
public class MyApplication extends BleApplication {
    private IWXAPI api;
    private Tencent mTencent;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }


//    /**
//     * 字体不变
//     */
//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        if (res.getConfiguration().fontScale != 1) {//非默认值
//            Configuration newConfig = new Configuration();
//            newConfig.setToDefaults();//设置默认
//            res.updateConfiguration(newConfig, res.getDisplayMetrics());
//        }
//        return res;
//    }

    //    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration newConfig = res.getConfiguration();
//        newConfig.fontScale = SavePreferenceUtils.getFontSize(getApplicationContext());
//        res.updateConfiguration(newConfig,res.getDisplayMetrics());
//        return res;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this, "2183a41d2c52f", "3392531d392202c010827c0cc7a6b284");
//        Fresco.initialize(this);
        String root = MMKV.initialize(this);
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
//                return BuildConfig.DEBUG;
                return true;
            }
        });
//        if (!isDebug()) {
            CrashReport.initCrashReport(getApplicationContext(), "4829301d77", false);
//        }
        Logger.i("mmkv root %s",root);
        ForegroundCallbacks foregroundCallbacks = new ForegroundCallbacks();
        foregroundCallbacks.addListener(new ForegroundCallbacks.Listener() {
            @Override
            public void onBecameForeground() {
                Log.i("AppForegroundEvent","foreground");
                EventBus.getDefault().postSticky(new AppForegroundEvent(true));
            }

            @Override
            public void onBecameBackground() {
                Log.i("AppForegroundEvent","background");
                EventBus.getDefault().postSticky(new AppForegroundEvent(false));
            }
        });
        registerActivityLifecycleCallbacks(foregroundCallbacks);
        JPushInterface.setDebugMode(isDebug());
        JPushInterface.init(this);

        if (isDebug()) {
            BlockCanary.install(this, new BlockCanaryContext()).start();
            if (!LeakCanary.isInAnalyzerProcess(this)) {
                LeakCanary.install(this);
            }
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        regToWx();
        initBle();
    }

    public void closePush(){
        JPushInterface.clearAllNotifications(this);
        JPushInterface.stopPush(this);
    }

    public void openPush(){
        if (JPushInterface.isPushStopped(this)){
            JPushInterface.resumePush(this);
        }
    }


    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, MyConstant.WX_LOGIN_APP_ID, true);
        api.registerApp(MyConstant.WX_LOGIN_APP_ID);
    }

    public IWXAPI getWXApi() {
        return api;
    }

    public Tencent getTencent() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance(MyConstant.QQ_LOGIN_APP_ID, getApplicationContext());
        }
        return mTencent;
    }

    public boolean isDebug() {
        return false;
//        return getApplicationContext().getApplicationInfo() != null &&
//                (getApplicationContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
//        return BuildConfig.DEBUG;
    }

    /**
     * 返回 AppConfig 用例
     *
     * @return AppConfigUseCase
     */
    public AppConfigUseCase getAppConfigUseCase() {
        return new AppConfigUseCase(new AppConfigRepository(this));
    }

    public AccountUseCase getAccountUseCase() {
        return new AccountUseCase(new AccountRepository(this));
    }

    public PersonalInfoUseCase getPersonInfoUseCase() {
        return new PersonalInfoUseCase(new PersonalInfoRepository(this));
    }

    public DeviceUseCase getDeviceUseCase() {
        return new DeviceUseCase(new DeviceRepository(this));
    }

    public EcgUseCase getEcgUseCase() {
        return new EcgUseCase(new EcgDataRepository(this));
    }

    public ExceptionUseCase getExceptionUseCase() {
        return new ExceptionUseCase(new ExceptionRepository(this));
    }

    public DataUseCase getDataUseCase() {
        return new DataUseCase(new IndicatorRepository(this));
    }

    public SingleSignOnUseCase getSingleSignOnUseCase() {
        return new SingleSignOnUseCase(new SingleSignOnRepository(this));
    }

    public RelationUseCase getRelationUseCase() {
        return new RelationUseCase(new RelationRepository(this));
    }

    public ContactUseCase getContactUseCase() {
        return new ContactUseCase(new ContactRepository(this));
    }

    public MedicineUseCase getMedicineUseCase() {
        return new MedicineUseCase(new MedicineRepository(this));
    }

    public StepUseCase getStepUseCase() {
        return new StepUseCase(new StepRepository(this));
    }


    public WeeklyReportUseCase getWeeklyReportUseCase() {
        return new WeeklyReportUseCase(new WeeklyReportRepository(this));
    }

    public NotificationUseCase getNotificationUseCase() {
        return new NotificationUseCase(new NotificationRepository(this));
    }

    public HealthManagementUseCase getHealthManagementUseCase() {
        return new HealthManagementUseCase(new HealthManagementRepository(this));
    }

    public FileUseCase getFileUseCase() {
        return new FileUseCase(new FileRepository(this));
    }

    public UnreadMessageUseCase getUnreadMessageUseCase() {
        return new UnreadMessageUseCase(new UnreadMessageRepository(this));
    }

    public ServeUseCase getServeUseCase() {
        return new ServeUseCase(new ServeRepository(this));
    }

//    public UseCaseComponent getCaseComponent(){
//        return caseComponent;
//    }
}