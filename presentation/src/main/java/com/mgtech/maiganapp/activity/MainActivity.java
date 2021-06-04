package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.mgtech.domain.entity.UnreadMessage;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.NotificationPageEvent;
import com.mgtech.maiganapp.data.event.UnreadMessageEvent;
import com.mgtech.maiganapp.fragment.FriendFragment;
import com.mgtech.maiganapp.fragment.HealthManagerFragment;
import com.mgtech.maiganapp.fragment.HomeFragment;
import com.mgtech.maiganapp.fragment.MineFragmentNew;
import com.mgtech.maiganapp.viewmodel.MainViewModel;
import com.mgtech.maiganapp.widget.MainBottomView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

/**
 * @author zhaixiang
 * @date 2017/1/3
 * 主页
 */
public class MainActivity extends BleActivity<MainViewModel> implements
        MainBottomView.OnItemSelectedListener {
    private static final int DEFAULT_POSITION = 0;
    public static final String EXTRA_POSITION = "position";
    public static final int PAGE_MEASURE = 2;
    public static final int PAGE_SET = 4;
    public static final int PAGE_FRIEND = 3;
    public static final int PAGE_HOME = 0;
    public static final int PAGE_HEALTH = 1;
    @Bind(R.id.bottomNavigatorView)
    MainBottomView mainBottomView;
    private AlertDialog appDialog;
    private static final String EXIT = "exit";
    private static final String LOGOUT = "logout";
    private HomeFragment fragmentHome;
    private HealthManagerFragment fragmentHealth;
    private FriendFragment fragmentFriend;
    private MineFragmentNew fragmentMine;
    private int currentPage;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    public static Intent getExitIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXIT, true);
        return intent;
    }

    public static Intent getLogoutIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(LOGOUT, true);
        return intent;
    }

    public static Intent getCallingIntent(Context context, int index) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_POSITION, index);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        hideActionbar();
        viewModel.startService(this);
        initFragment();

        int pos = getIntent().getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        switchFragment(pos);

        mainBottomView.setOnBottomNavigatorViewItemClickListener(this);
        setCurrentTab(pos);

        openPush();

        // 判断是否有需要上传的数据
        if (viewModel.haveSavedData()) {
            haveSaveData();
        }
        // 判断是否需要升级
        if (viewModel.isAppNeedUpgrade()) {
            showAskUpgradeAppDialog();
        }
        Log.i(TAG, "upgrade: "+viewModel.isAppNeedUpgrade());
        viewModel.getUserInfo();
        EventBus.getDefault().register(this);

        MyLocationListener listener = new MyLocationListener();
        getLifecycle().addObserver(listener);
        askForPermission();
    }

    @Override
    protected void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("MyLocationListener", "onSaveInstanceState: ");
    }

    private class MyLocationListener implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        void start() {
            Log.i("MyLocationListener", "start: ");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        void stop() {
            Log.i("MyLocationListener", "stop: ");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        void create() {
            Log.i("MyLocationListener", "create: ");
        }
    }


    private void initFragment() {
        fragmentHome = HomeFragment.newInstance();
        fragmentHealth = HealthManagerFragment.newInstance();
        fragmentFriend = FriendFragment.newInstance();
        fragmentMine = MineFragmentNew.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, fragmentHome, "home");
        transaction.add(R.id.container, fragmentHealth, "health");
        transaction.add(R.id.container, fragmentFriend, "friend");
        transaction.add(R.id.container, fragmentMine, "mine");
        transaction.hide(fragmentHome);
        transaction.hide(fragmentHealth);
        transaction.hide(fragmentFriend);
        transaction.hide(fragmentMine);
//        transaction.add(R.id.container,fragmentHome,"home");
        transaction.commit();
    }


    private void switchFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (index != currentPage) {
            hideFragment(currentPage, transaction);
        }
        showFragment(index, transaction);
        currentPage = index;
        transaction.commit();


//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        if (index != currentPage) {
//            replaceFragment(index, transaction);
//        }
//        currentPage = index;
//        transaction.commit();
    }

    private void showFragment(int index, FragmentTransaction transaction) {
        switch (index) {
            case PAGE_HOME:
                transaction.show(fragmentHome);
                break;
            case PAGE_HEALTH:
                transaction.show(fragmentHealth);
                break;
            case PAGE_FRIEND:
                transaction.show(fragmentFriend);
                break;
            case PAGE_SET:
                transaction.show(fragmentMine);
                break;
            default:
        }
    }

    private void replaceFragment(int index, FragmentTransaction transaction) {
        switch (index) {
            case PAGE_HOME:
                transaction.replace(R.id.container, fragmentHome);
                break;
            case PAGE_HEALTH:
                transaction.replace(R.id.container, fragmentHealth);
                break;
            case PAGE_FRIEND:
                transaction.replace(R.id.container, fragmentFriend);
                break;
            case PAGE_SET:
                transaction.replace(R.id.container, fragmentMine);
                break;
            default:
        }
    }

    private void hideFragment(int index, FragmentTransaction transaction) {
        switch (index) {
            case PAGE_HOME:
                transaction.hide(fragmentHome);
                break;
            case PAGE_HEALTH:
                transaction.hide(fragmentHealth);
                break;
            case PAGE_FRIEND:
                transaction.hide(fragmentFriend);
                break;
            case PAGE_SET:
                transaction.hide(fragmentMine);
                break;
            default:
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().postSticky(new NotificationPageEvent(NotificationPageEvent.MAIN_ACTIVITY));
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getUnreadMessage();
        if (viewModel.isPaired()) {
            Logger.i("main getLinkStatus: ");
            link();
        }
    }

    public void changeText() {
        Intent intent = MainActivity.getCallingIntent(this, PAGE_SET);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }


    /**
     * 更新app
     */
    private void showAskUpgradeAppDialog() {
        if (appDialog != null && appDialog.isShowing()) {
            return;
        }
        appDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.find_new_version_need_upgrade)
                .setMessage(R.string.main_app_new_version_upgrade_text)
                .setPositiveButton(R.string.upgrade_right_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = viewModel.getAppUrl();
                        if (url != null && !url.isEmpty()) {
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }
                }).setNegativeButton(R.string.not_upgrade_right_now, null).create();
        appDialog.show();
    }


    @Override
    public void onBackPressed() {
        if (currentPage == 0) {
            super.onBackPressed();
        } else {
            setCurrentTab(0);
        }
    }

    @Override
    protected void onDestroy() {
        if (!viewModel.isPaired()) {
            viewModel.stopService();
        }
        viewModel.unRegister();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_home;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent: ");
        if (intent != null) {
            // 是否退出App的标识
            boolean isExitApp = intent.getBooleanExtra(EXIT, false);
            if (isExitApp) {
                // 关闭自身
                this.finish();
            }
            boolean logout = intent.getBooleanExtra(LOGOUT, false);
            if (logout) {
                startActivity(LoginActivity.getCallingIntent(this, false));
                this.finish();
            }
        }
    }

    /**
     * 切换页面
     *
     * @param position 位置
     */
    private void setCurrentTab(int position) {
        if (position == PAGE_MEASURE) {
            startActivity(MeasurePwActivity.getCallingIntent(this));
        } else {
//            mNavigator.showFragment(position, false);
            switchFragment(position);
            mainBottomView.select(position);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void setRead(UnreadMessageEvent event){
        mainBottomView.setUnread(UnreadMessage.getNormalPwUnreadNumber() > 0,MainBottomView.POSITION_HOME);
        mainBottomView.setUnread(UnreadMessage.getFriendRequestUnreadNumber() > 0 ,MainBottomView.POSITION_FRIEND);
        mainBottomView.setUnread(UnreadMessage.getHealthKnowledgeUnreadNumber() > 0 || UnreadMessage.getAbnormalPwUnreadNumber() > 0 ,
                MainBottomView.POSITION_HEALTH);
        mainBottomView.setUnread(UnreadMessage.getWeekReportUnreadNumber() > 0 ,MainBottomView.POSITION_MINE);

        fragmentHome.updateUnread();
        fragmentHealth .updateUnread();
        fragmentFriend.updateUnread();
        fragmentMine.updateUnread();
    }


    public boolean isLink() {
        return viewModel.isLink.get();
    }


    @Override
    protected void openBleFail() {
        Toast.makeText(this, R.string.ble_is_unavailable, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void openBleSuccess() {
        Toast.makeText(this, R.string.ble_has_open, Toast.LENGTH_SHORT).show();
    }

    private void haveSaveData() {
        viewModel.sendSavedData();
    }

//    @Override
//    public void onAttachFragment(Fragment fragment) {
//        if (fragment instanceof MainFragmentCallback) {
//            this.callback = (MainFragmentCallback) fragment;
//        }
//        super.onAttachFragment(fragment);
//    }


    private void link() {
        if (isBleOn()) {
            viewModel.getLinkStatus();
        } else {
            openBle();
        }
    }

    @Override
    public void onBottomNavigatorItemClick(int position) {
//        viewModel.guideViewModel.display(position == 0);
        Logger.e("onBottomNavigatorItemClick:" + position);
        switchFragment(position);
    }

    @Override
    public void onMeasurePwClick() {
        startActivity(MeasurePwActivity.getCallingIntent(this));
    }

    @Override
    public void onMeasureEcgClick() {
        startActivity(MeasureEcgActivity.getCallingIntent(this));
    }
}
