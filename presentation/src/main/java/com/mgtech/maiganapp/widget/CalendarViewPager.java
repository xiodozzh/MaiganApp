package com.mgtech.maiganapp.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhaixiang on 2017/11/24.
 * 日历
 */

public class CalendarViewPager extends LinearLayoutCompat {
    private ViewPager viewPager;
    private Calendar currentCalendar = Calendar.getInstance();
    private CalendarSelectListener listener;
    private static final int calendarHalfRange = 52 * 100;

    public interface CalendarSelectListener {
        void onSelect(Calendar calendar);
    }

    public CalendarViewPager(Context context) {
        super(context);
        init(context);
    }

    public CalendarViewPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarViewPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        inflate(context, R.layout.layout_calendar_view_pager_item, this);
        viewPager = findViewById(R.id.pager);
        final ViewAdapter adapter = new ViewAdapter(context, calendarHalfRange);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(calendarHalfRange, false);
    }

    public void setListener(CalendarSelectListener listener) {
        this.listener = listener;
    }

    public void selectCalendar(Calendar calendar) {
        Calendar c = Utils.getFirstDayOfWeek(calendar);
        Calendar thisFirstWeek = Utils.getFirstDayOfWeek(Calendar.getInstance());
        int week = (int) ((c.getTimeInMillis() - thisFirstWeek.getTimeInMillis()) / (7 * 24 * 3600 * 1000)) -
                (viewPager.getCurrentItem() - calendarHalfRange);
        currentCalendar.setTimeInMillis(calendar.getTimeInMillis());
        if (viewPager.getAdapter() != null) {
            if (week != 0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + week, true);
                if (week <= 1 && week >= -1) {
                    // 刷新单周数据
                    viewPager.getAdapter().notifyDataSetChanged();
                }
            } else {
                viewPager.getAdapter().notifyDataSetChanged();
            }
        }
    }

    private class ViewAdapter extends PagerAdapter {
        private List<CalendarViewPagerItem> list;
        private Context context;
        private int rangeOfWeeks = 100 * 52;

        ViewAdapter(Context context, int rangeOfWeeks) {
            this.list = new ArrayList<>();
            this.context = context;
            this.rangeOfWeeks = rangeOfWeeks;
        }

        @Override
        public int getCount() {
            return rangeOfWeeks * 2 + 1;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Calendar calendar = Calendar.getInstance();
            CalendarViewPagerItem view = new CalendarViewPagerItem(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            calendar.add(Calendar.DATE, (position - rangeOfWeeks) * 7);
            view.setDisplayDay(calendar);
            view.setCallback(callback);
            container.addView(view);
            list.add(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            view.invalidate();
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            CalendarViewPagerItem item = (CalendarViewPagerItem) object;
            list.remove(item);
            container.removeView(item);
        }

        @Override
        public void notifyDataSetChanged() {
            for (View view : list) {
                view.invalidate();
            }
            super.notifyDataSetChanged();
        }
    }

    private CalendarViewPagerItem.Callback callback = new CalendarViewPagerItem.Callback() {
        @Override
        public boolean isSelectDay(Calendar calendar) {
            return calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                    calendar.get(Calendar.DATE) == currentCalendar.get(Calendar.DATE);
        }

        @Override
        public void selectDay(Calendar calendar) {
            if (!isSelectDay(calendar)) {
                currentCalendar = calendar;
                if (listener != null) {
                    listener.onSelect(calendar);
                }
            }
        }
    };

}
