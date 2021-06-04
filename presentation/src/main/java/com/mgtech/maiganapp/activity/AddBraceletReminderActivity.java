package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.mgtech.blelib.entity.AlertReminder;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.BindInfoUpdate;
import com.mgtech.maiganapp.viewmodel.AddBraceletReminderViewModel;
import com.mgtech.maiganapp.widget.FlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 * @date 2017/8/7
 * 添加提醒
 */

public class AddBraceletReminderActivity extends BleActivity<AddBraceletReminderViewModel> {
    @Bind(R.id.rb_everyday)
    RadioButton rbEveryday;
    @Bind(R.id.rb_everyWeek)
    RadioButton rbEveryWeek;
    @Bind(R.id.rb_workday)
    RadioButton rbWorkday;
    @Bind(R.id.rb_weekend)
    RadioButton rbWeekend;
    @Bind({R.id.tv_sun, R.id.tv_mon, R.id.tv_tue, R.id.tv_wed, R.id.tv_thr, R.id.tv_fri, R.id.tv_sat})
    TextView[] tvWeek;
    @Bind(R.id.layout_week)
    FlowLayout layoutWeek;
    @Bind(R.id.rb_continuous)
    RadioButton rbContinuous;
    @Bind(R.id.rb_daily)
    RadioButton rbDaily;
    private static final boolean[] TIME_PICKER_STYLE = {false, false, false, true, false, false};
//    public static final String DATA = "data";
    public static final String INDEX = "index";

    public static Intent getCallingIntent(Context context, ArrayList<AlertReminder> reminders, int index) {
        Intent intent = new Intent(context, AddBraceletReminderActivity.class);
//        intent.putParcelableArrayListExtra(DATA, reminders);
        intent.putExtra(INDEX, index);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        hideActionbar();
        viewModel.success.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                showShortToast(getString(R.string.set_success));
                Intent intent = new Intent();
//                intent.putParcelableArrayListExtra(DATA, viewModel.getData());
                intent.putExtra(INDEX, viewModel.getIndex());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        viewModel.isDaily.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setDaily(viewModel.isDaily.get());
            }
        });
        int index = getIntent().getIntExtra(INDEX, -1);
        viewModel.initData(index);
        initWeekDay();
        initRadioButton();
    }

    private void setDaily(boolean isDaily){
        rbDaily.setChecked(isDaily);
        rbContinuous.setChecked(!isDaily);
    }

    @OnClick(R.id.btn_open_bluetooth)
    public void openBluetooth() {
        if (!isBleOn()) {
            openBle();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewModel.checkLink();
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
        viewModel.checkLink();
    }

    @Override
    protected void openBleFail() {
        showShortToast(getString(R.string.ble_is_unavailable));
        finish();
    }

    @Override
    protected void openBleSuccess() {
        showShortToast(getString(R.string.ble_has_open));
        viewModel.linkIfAllowed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initWeekDay() {
        for (int i = 0; i < tvWeek.length; i++) {
            tvWeek[i].setSelected(viewModel.getSelectedWeeks()[i]);
        }
    }

    @OnClick(R.id.btn_bond)
    void bond() {
        startActivity(BraceletPairActivity.getCallingIntent(this));
        finish();
    }

    @OnClick({R.id.tv_sun, R.id.tv_mon, R.id.tv_tue, R.id.tv_wed, R.id.tv_thr, R.id.tv_fri, R.id.tv_sat})
    void selectDay(View view) {
        view.setSelected(!view.isSelected());
        setReminderDays();
    }

    @OnClick(R.id.layout_start)
    void setStartTime() {
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                viewModel.setStartTime(date);
            }
        })
                .setDate(viewModel.getStartTime())
                .setCancelColor(Color.WHITE)
                .setTitleColor(Color.WHITE)
                .setSubmitColor(Color.WHITE)
                .setSubmitText(getString(R.string.submit))
                .setTitleText(getString(R.string.begin_time))
                .setTitleBgColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryBlue))
                .setType(TIME_PICKER_STYLE)
                .setCancelText(getString(R.string.cancel))
                .build();
        pvTime.show();
    }

    @OnClick(R.id.layout_stop)
    void setStopTime() {
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                viewModel.setEndTime(date);
            }
        })
                .setDate(viewModel.getEndTime())
                .setCancelColor(Color.WHITE)
                .setTitleColor(Color.WHITE)
                .setSubmitColor(Color.WHITE)
                .setSubmitText(getString(R.string.submit))
                .setTitleText(getString(R.string.end_time))
                .setTitleBgColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryBlue))
                .setType(TIME_PICKER_STYLE)
                .setCancelText(getString(R.string.cancel))
                .build();
        pvTime.show();
    }

    private void initRadioButton() {
        rbEveryday.setOnCheckedChangeListener(onRepeatChangeListener);
        rbEveryWeek.setOnCheckedChangeListener(onRepeatChangeListener);
        rbWeekend.setOnCheckedChangeListener(onRepeatChangeListener);
        rbWorkday.setOnCheckedChangeListener(onRepeatChangeListener);
        rbContinuous.setOnCheckedChangeListener(onAlertChangeListener);
        rbDaily.setOnCheckedChangeListener(onAlertChangeListener);
        if (viewModel.isDaily.get()){
            rbDaily.setChecked(true);
        }else{
            rbContinuous.setChecked(true);
        }
        switch (viewModel.getRemindDays()) {
            case 0:
            case AlertReminder.EVERYDAY:
                rbEveryday.setChecked(true);
                break;
            case AlertReminder.WEEKEND:
                rbWeekend.setChecked(true);
                break;
            case AlertReminder.WORKDAY:
                rbWorkday.setChecked(true);
                break;
            default:
                rbEveryWeek.setChecked(true);
        }
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @OnClick(R.id.btn_save)
    void save() {
        viewModel.save();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_bracelet_reminder;
    }


    private CompoundButton.OnCheckedChangeListener onRepeatChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                clearRepeatRbBtn();
                switch (buttonView.getId()) {
                    case R.id.rb_everyday:
                        rbEveryday.setChecked(true);
                        viewModel.setRemindDays(AlertReminder.EVERYDAY);
                        break;
                    case R.id.rb_everyWeek:
                        rbEveryWeek.setChecked(true);
                        layoutWeek.setVisibility(View.VISIBLE);
                        setReminderDays();
                        break;
                    case R.id.rb_workday:
                        rbWorkday.setChecked(true);
                        viewModel.setRemindDays(AlertReminder.WORKDAY);
                        break;
                    case R.id.rb_weekend:
                        rbWeekend.setChecked(true);
                        viewModel.setRemindDays(AlertReminder.WEEKEND);
                        break;
                    default:
                }
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener onAlertChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                clearAlertRbBtn();
                switch (buttonView.getId()) {
                    case R.id.rb_daily:
                        rbContinuous.setChecked(false);
                        viewModel.isDaily.set(true);
                        break;
                    case R.id.rb_continuous:
                        rbDaily.setChecked(false);
                        viewModel.isDaily.set(false);
                        break;
                    default:
                }
            }
        }
    };

    private void clearRepeatRbBtn() {
        rbEveryday.setChecked(false);
        rbEveryWeek.setChecked(false);
        rbWeekend.setChecked(false);
        rbWorkday.setChecked(false);
        layoutWeek.setVisibility(View.GONE);
    }

    private void clearAlertRbBtn(){
        rbDaily.setChecked(false);
        rbContinuous.setChecked(false);
    }

    private void setReminderDays() {
        int days = 0;
        for (int i = 0; i < tvWeek.length; i++) {
            TextView tv = tvWeek[i];
            if (tv.isSelected()) {
                days += (1 << i);
            }
        }
        viewModel.setRemindDays(days);
    }
}
