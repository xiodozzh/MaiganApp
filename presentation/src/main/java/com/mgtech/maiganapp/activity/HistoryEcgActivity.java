package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.EcgViewPagerAdapter;
import com.mgtech.maiganapp.data.model.EcgModel;
import com.mgtech.maiganapp.viewmodel.HistoryEcgViewModel;
import com.mgtech.maiganapp.widget.CalendarViewPager;
import com.mgtech.maiganapp.widget.EcgPartDataGraphView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * @author zhaixiang
 * ecg
 */

public class HistoryEcgActivity extends BaseActivity<HistoryEcgViewModel> {
    private static final String USER_ID = "targetUserId";
    private static final String INIT_TIME = "initTime";
    @Bind(R.id.calendarView)
    MaterialCalendarView calendarView;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.calendarViewPager)
    CalendarViewPager calendarViewPager;
    private List<View> pagers;
    @Bind(R.id.root)
    RelativeLayout root;
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.8f;

    public static Intent getCallingIntent(Context context,String userId,long time) {
        Intent intent = new Intent(context, HistoryEcgActivity.class);
        intent.putExtra(USER_ID,userId);
        intent.putExtra(INIT_TIME,time);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        pagers = new ArrayList<>();
        hideActionbar();
        // 自己或好友id
        String userId = getIntent().getStringExtra(USER_ID);
        long time = getIntent().getLongExtra(INIT_TIME,Calendar.getInstance().getTimeInMillis());
        if (TextUtils.isEmpty(userId)){
            userId = SaveUtils.getUserId(this);
        }
        viewModel.setInitData(userId,time);
        calendarView.setTopbarVisible(false);
        calendarView.setWeekDayLabels(R.array.week);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                viewModel.setCurrentTime(date.getYear(), date.getMonth(), date.getDay());
            }
        });
        calendarView.setSelectedDate(viewModel.getCurrentCalendar());
        calendarViewPager.setListener(new CalendarViewPager.CalendarSelectListener() {
            @Override
            public void onSelect(Calendar calendar) {
                viewModel.setCurrentTime(calendar);
            }
        });
        calendarViewPager.selectCalendar(viewModel.getCurrentCalendar());

        viewModel.updateListData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                setPagers(viewModel.currentEcgList);
            }
        });
        initPagers();
    }

    private void initPagers() {
        viewPager.setPageTransformer(true, transformer);
        viewPager.setOffscreenPageLimit(3);
    }

    /**
     * 设置viewPager页
     * @param data 数据
     */
    private void setPagers(List<EcgModel> data){
        pagers.clear();
        for (int i = 0; i < data.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.activity_ecg_history_view_pager_item, null, false);
            final EcgModel model = data.get(i);
            ((TextView)view.findViewById(R.id.tv_time)).setText(DateFormat.format(MyConstant.DISPLAY_TIME,model.measureTime));
            view.findViewById(R.id.btn_detail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EcgModel ecgModel = new EcgModel();
                    ecgModel.measureGuid = model.measureGuid;
                    ecgModel.userId = viewModel.getUserId();
                    EventBus.getDefault().postSticky(ecgModel);
                    startActivity(EcgDataDetailActivity.getCallingIntent(HistoryEcgActivity.this));
                }
            });

            EcgPartDataGraphView graphView = view.findViewById(R.id.graphView);
            graphView.setSampleRate(model.sampleRate);
            TextView tvHr = view.findViewById(R.id.tv_hr);
//            TextView tvEcg = view.findViewById(R.id.tv_ecg_result);
            graphView.setData(model.ecgData,model.sampleRate);
            tvHr.setText(String.valueOf(Math.round(model.hr)));
//            tvEcg.setText(model.ecgResult==0 ? getString(R.string.ecg_normal):getString(R.string.ecg_abnormal));
            pagers.add(view);
        }
        EcgViewPagerAdapter adapter = new EcgViewPagerAdapter(this,pagers);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private ViewPager.PageTransformer transformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }
                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                        / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    };

    @OnClick(R.id.btn_back_today)
    void backToToday() {
        viewModel.showBackToday.set(false);
        viewModel.setCurrentTime(Calendar.getInstance());
        calendarViewPager.selectCalendar(Calendar.getInstance());
    }

//    @OnClick(R.id.layout_select_calendar)
//    void selectCalendar() {
//        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date, View v) {
//                viewModel.setCurrentTime(date);
//                calendarViewPager.selectCalendar(viewModel.getCurrentCalendar());
//            }
//        })
//                .setDate(viewModel.getCurrentCalendar())
//                .setCancelColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary))
//                .setSubmitColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary))
//                .setSubmitText(getString(R.string.submit))
//                .setCancelText(getString(R.string.cancel))
//                .build();
//        pvTime.show();
//    }

    @OnClick(R.id.btn_back)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.btn_measure)
    void measure(){
        startActivity(MeasureEcgActivity.getCallingIntent(this));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_history_ecg;
    }

}
