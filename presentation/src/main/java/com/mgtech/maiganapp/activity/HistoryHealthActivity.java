package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.fragment.HistoryHealthGraphFragment;
import com.mgtech.maiganapp.fragment.HistoryHealthListFragment;
import com.mgtech.maiganapp.viewmodel.HistoryHealthViewModel;

import java.util.Calendar;


/**
 * @author zhaixiang
 */
public class HistoryHealthActivity extends BaseOtherBleActivity<HistoryHealthViewModel> implements HistoryActivityCallback {
    private static final String USER_ID = "targetUserId";
    private static final String USER_IS_MAN = "userIsMan";
    private static final String TIME = "time";

    public static Intent getCallingIntent(Context context, String userId, boolean isMan,long time) {
        Intent intent = new Intent(context, HistoryHealthActivity.class);
        intent.putExtra(USER_ID, userId);
        intent.putExtra(USER_IS_MAN, isMan);
        intent.putExtra(TIME, time);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        String id = getIntent().getStringExtra(USER_ID);
        Calendar calendar = Calendar.getInstance();
        boolean man = getIntent().getBooleanExtra(USER_IS_MAN, true);
        long time = getIntent().getLongExtra(TIME,calendar.getTimeInMillis());
        if (TextUtils.isEmpty(id)) {
            showShortToast(getString(R.string.error_occur));
            finish();
        }
        if (time != 0){
            calendar.setTimeInMillis(time);
        }
        viewModel.setUserId(id);
        viewModel.setMan(man);
        viewModel.setCalendar(calendar);
        initFragment();
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, new HistoryHealthGraphFragment());
        transaction.commit();
    }

    private void switchFragment(int index) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (index == 0) {
            transaction.setCustomAnimations(R.animator.card_shift_left_in, R.animator.card_shift_right_out);
            transaction.replace(R.id.container, new HistoryHealthGraphFragment());
        } else {
            transaction.setCustomAnimations(R.animator.card_shift_right_in, R.animator.card_shift_left_out);
            transaction.replace(R.id.container, new HistoryHealthListFragment());
        }
        transaction.commit();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_history_health;
    }

    @Override
    public void back() {
        finish();
    }

    @Override
    public void goToGraph() {
        switchFragment(0);
    }

    @Override
    public void goToList() {
        switchFragment(1);
    }

    @Override
    public void setCalendar(Calendar calendar) {
        Log.i(TAG, "setCalendar: " + calendar.toString());
        viewModel.setCalendar(calendar);
    }

    @Override
    public Calendar getCalendar() {
        return viewModel.getCalendar();
    }

    @Override
    public String getUserId() {
        return viewModel.getId();
    }

    @Override
    public boolean isMan() {
        return viewModel.isMan();
    }
}
