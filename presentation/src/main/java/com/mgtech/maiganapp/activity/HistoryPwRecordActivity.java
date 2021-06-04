package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.fragment.HistoryHealthGraphFragment;
import com.mgtech.maiganapp.fragment.HistoryHealthListFragment;
import com.mgtech.maiganapp.viewmodel.HistoryHealthViewModel;

import java.util.Calendar;

import butterknife.Bind;


/**
 * @author zhaixiang
 */
public class HistoryPwRecordActivity extends BaseOtherBleActivity<HistoryHealthViewModel> implements HistoryActivityCallback {
    private static final String USER_ID = "targetUserId";
    private static final String USER_IS_MAN = "userIsMan";
    private static final String TIME = "time";
    @Bind(R.id.container)
    View container;

    public static Intent getCallingIntent(Context context, String userId, boolean isMan, long time) {
        Intent intent = new Intent(context, HistoryPwRecordActivity.class);
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
        long time = getIntent().getLongExtra(TIME,calendar.getTimeInMillis());
        boolean man = getIntent().getBooleanExtra(USER_IS_MAN, true);
        if (TextUtils.isEmpty(id)) {
            showShortToast(getString(R.string.error_occur));
            finish();
        }
//        setCamera();
        viewModel.setUserId(id);
        viewModel.setMan(man);
        if (time != 0) {
            calendar.setTimeInMillis(time);
        }
        viewModel.setCalendar(calendar);
        initFragment();
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, new HistoryHealthListFragment());
        transaction.commit();
    }

    private void switchFragment(int index) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (index == 0) {
            transaction.setCustomAnimations(R.animator.card_shift_right_in, R.animator.card_shift_left_out);
//            transaction.setCustomAnimations(R.animator.card_flip_right_in,
//                    R.animator.card_flip_right_out,
//                    R.animator.card_flip_left_in,
//                    R.animator.card_flip_left_out);
            transaction.replace(R.id.container, new HistoryHealthListFragment());
        } else {
            transaction.setCustomAnimations(R.animator.card_shift_left_in, R.animator.card_shift_right_out);
//            transaction.setCustomAnimations(
//                    R.animator.card_flip_left_in,
//                    R.animator.card_flip_left_out,
//                    R.animator.card_flip_right_in,
//                    R.animator.card_flip_right_out);
            transaction.replace(R.id.container, new HistoryHealthGraphFragment());
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
        switchFragment(1);
    }

    @Override
    public void goToList() {
        switchFragment(0);
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
