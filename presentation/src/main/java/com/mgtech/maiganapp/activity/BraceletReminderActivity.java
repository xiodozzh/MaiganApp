package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.BraceletReminderAdapter;
import com.mgtech.maiganapp.data.event.BindInfoUpdate;
import com.mgtech.maiganapp.fragment.DeleteDialogFragment;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.BraceletReminderViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 * @date 2017/8/2
 * 设置手环提醒
 */

public class BraceletReminderActivity extends BleActivity<BraceletReminderViewModel> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private BraceletReminderAdapter adapter;
    private static final int UPDATE = 123;
    private static final int INSERT = 223;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BraceletReminderActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        hideActionbar();
        initObservable();
        adapter = new BraceletReminderAdapter(recyclerView);
        adapter.setCallback(new BraceletReminderAdapter.Callback() {

            @Override
            public void onItemClick(int position) {
                Intent intent = AddBraceletReminderActivity.getCallingIntent(BraceletReminderActivity.this,
                        viewModel.reminders, position);
                startActivityForResult(intent, UPDATE);
            }

            @Override
            public void onSwitch(int position, boolean isChecked) {
                Log.e(TAG, "onSwitch: " + position + isChecked);
                viewModel.enableChange(position, isChecked);
            }

            @Override
            public void onDelete(int position) {
                showDeleteItemDialog(position);
            }


        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initObservable() {
        viewModel.dataLoadSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                adapter.setData(viewModel.data);
                viewModel.isEmpty.set(viewModel.reminders.isEmpty());
            }
        });
        viewModel.actionFail.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Toast.makeText(BraceletReminderActivity.this, R.string.error_occur_try_again, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @OnClick(R.id.btn_open_bluetooth)
    public void openBluetooth() {
        if (!isBleOn()) {
            openBle();
        }
    }

    @OnClick(R.id.btn_add)
    void addItem() {
        add();
    }



    @Override
    protected void onResume() {
        super.onResume();
        viewModel.checkLink();
        viewModel.loadReminders();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bindInfoUpdate(BindInfoUpdate bindInfoUpdate) {
        Log.i(TAG, "bindInfoUpdate: 23");
        viewModel.checkLink();
        viewModel.loadReminders();
    }

    private void showDeleteItemDialog(final int position) {
        ViewUtils.showDeleteDialog(this, new DeleteDialogFragment.Callback() {
            @Override
            public void delete(boolean isDelete) {
                if (isDelete) {
                    viewModel.deleteReminder(position);
                }
            }
        });
    }

    void add() {
        if (viewModel.reminders.size() >= 4) {
            Toast.makeText(this, R.string.activity_bracelet_reminder_cannot_add_more_reminder, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = AddBraceletReminderActivity.getCallingIntent(this, viewModel.reminders, -1);
        startActivityForResult(intent, INSERT);
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @OnClick(R.id.btn_bond)
    void bond() {
        startActivity(BraceletPairActivity.getCallingIntent(this));
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bracelet_reminder;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UPDATE) {
                int index = data.getIntExtra(AddBraceletReminderActivity.INDEX, -2);
//                List<AlertReminder> systemParamData = data.getParcelableArrayListExtra(AddBraceletReminderActivity.DATA);
                if (index != -1) {
                    viewModel.refresh(index);
                }
            } else if (requestCode == INSERT) {
                int index = data.getIntExtra(AddBraceletReminderActivity.INDEX, -2);
//                List<AlertReminder> systemParamData = data.getParcelableArrayListExtra(AddBraceletReminderActivity.DATA);
                if (index == -1) {
                    viewModel.refresh(index);
                }
            }
        }
    }

    @Override
    protected void openBleFail() {
        Toast.makeText(this, R.string.ble_is_unavailable, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void openBleSuccess() {
        Toast.makeText(this, R.string.ble_has_open, Toast.LENGTH_SHORT).show();
        viewModel.linkIfAllowed();
    }
}
