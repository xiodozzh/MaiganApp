package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.MedicationPlanEditTimeViewModel;

import java.util.Calendar;
import java.util.Date;

import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class MedicationPlanEditTimeActivity extends BaseActivity<MedicationPlanEditTimeViewModel> {
    public static final String START = "start";
    public static final String END = "end";
    private Calendar calendarMin;
    private Calendar calendarMax;

    private static final boolean[] TIME_PICKER_STYLE = {true , true ,true ,false,false,false};

    public static Intent getCallingIntent(Context context, long startTime,long endTime){
        Intent intent = new Intent(context,MedicationPlanEditTimeActivity.class);
        intent.putExtra(START,startTime);
        intent.putExtra(END,endTime);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        Intent intent = getIntent();
        Calendar c = Calendar.getInstance();
        viewModel.setStartCalendar(intent.getLongExtra(START,c.getTimeInMillis()));
        c.add(Calendar.YEAR,1);
        viewModel.setEndCalendar(intent.getLongExtra(END,c.getTimeInMillis()));

        calendarMin = Calendar.getInstance();
        calendarMax = Calendar.getInstance();
        calendarMax.add(Calendar.YEAR,100);

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_medication_plan_edit_time;
    }

    @OnClick(R.id.btn_back)
    void back(){
//        finish();
        done();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        done();
    }

    //    @OnClick(R.id.btn_done)
    void done(){
        if (viewModel.isValid()) {
            Intent intent = new Intent();
            intent.putExtra(START, viewModel.getStartCalendar().getTimeInMillis());
            intent.putExtra(END, viewModel.getEndCalendar().getTimeInMillis());
            setResult(RESULT_OK, intent);
            finish();
        }else{
            showShortToast(getString(R.string.activity_medication_plan_time_span_cannot_over_one_year));
        }
    }

    @OnClick(R.id.layout_start)
    void setStartTime(){
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                viewModel.setStartCalendar(date.getTime());
            }
        })
                .setDate(viewModel.getStartCalendar())
                .setCancelColor(Color.WHITE)
                .setTitleColor(Color.WHITE)
                .setSubmitColor(Color.WHITE)
                .setSubmitText(getString(R.string.submit))
                .setTitleText(getString(R.string.begin_time))
                .setTitleBgColor(ContextCompat.getColor(getApplicationContext(),R.color.primaryBlue))
                .setType(TIME_PICKER_STYLE)
                .setRangDate(calendarMin,calendarMax)
                .setCancelText(getString(R.string.cancel))
                .build();
        pvTime.show();
    }

    @OnClick(R.id.layout_end)
    void setEndTime(){
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                viewModel.setEndCalendar(date.getTime());
            }
        })
                .setDate(viewModel.getEndCalendar())
                .setCancelColor(Color.WHITE)
                .setTitleColor(Color.WHITE)
                .setSubmitColor(Color.WHITE)
                .setSubmitText(getString(R.string.submit))
                .setTitleText(getString(R.string.end_time))
                .setTitleBgColor(ContextCompat.getColor(getApplicationContext(),R.color.primaryBlue))
                .setType(TIME_PICKER_STYLE)
                .setRangDate(calendarMin,calendarMax)
                .setCancelText(getString(R.string.cancel))
                .build();
        pvTime.show();
    }
}
