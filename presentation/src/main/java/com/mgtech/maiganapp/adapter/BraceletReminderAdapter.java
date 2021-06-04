package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.AlertReminderModel;
import com.mgtech.maiganapp.widget.GuideDotView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author zhaixiang
 * @date 2017/8/10
 * 手环提醒列表 adapter
 */

public class BraceletReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AlertReminderModel> data = new ArrayList<>();
    private Callback callback;
    private RecyclerView recyclerView;
    private String[] weekString;
    private List<View> viewList;
    private String everydayText;
    private String workdayText;
    private String weekendText;
    private static final int HEAD = 1;
    private static final int CONTENT = 2;
    private static final int[] gifSrc = {
            R.drawable.bracelet_reminder_guide_img0,
            R.drawable.bracelet_reminder_guide_img1,
            R.drawable.bracelet_reminder_guide_img2,
            R.drawable.bracelet_reminder_guide_img3,
            R.drawable.bracelet_reminder_guide_img4
    };

    public interface Callback {
//        void onAdd();

        void onItemClick(int position);

        void onSwitch(int position, boolean isChecked);

        void onDelete(int position);

//        void onStyleChange(int style);
    }

    public void setData(List<AlertReminderModel> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public BraceletReminderAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.weekString = recyclerView.getContext().getResources().getStringArray(R.array.week_ch_long);
        this.everydayText = recyclerView.getContext().getString(R.string.bracelet_reminder_everyday);
        this.workdayText = recyclerView.getContext().getString(R.string.bracelet_reminder_workday);
        this.weekendText = recyclerView.getContext().getString(R.string.bracelet_reminder_weekend);
        this.viewList = new ArrayList<>();
        String[] tags = new String[]{
                recyclerView.getContext().getString(R.string.bracelet_reminder_tips0),
                recyclerView.getContext().getString(R.string.bracelet_reminder_tips1),
                recyclerView.getContext().getString(R.string.bracelet_reminder_tips2),
                recyclerView.getContext().getString(R.string.bracelet_reminder_tips3),
                recyclerView.getContext().getString(R.string.bracelet_reminder_tips4)
        };
        for (int i = 0; i < gifSrc.length; i++) {
            View view = LayoutInflater.from(recyclerView.getContext())
                    .inflate(R.layout.layout_bracelet_reminder_head_viewpager, null, false);
            ImageView iv = view.findViewById(R.id.iv_pic);
            if (i == 1 || i == 0) {
                Glide.with(recyclerView.getContext())
                        .asBitmap()
                        .load(gifSrc[i])
                        .into(iv);
            } else {
                Glide.with(recyclerView.getContext())
                        .asGif()
                        .load(gifSrc[i])
                        .into(iv);
            }
            TextView tv = view.findViewById(R.id.tv_info);
            tv.setText(tags[i]);
            viewList.add(view);
        }
    }

    /**
     * 设置点击回调
     *
     * @param callback 回调
     */
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@Nullable ViewGroup parent, int viewType) {
        if (viewType == CONTENT) {
            View view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.layout_bracelet_reminder_item, parent, false);
            ReminderViewHolder viewHolder = new ReminderViewHolder(view);
            viewHolder.sw.setOnCheckedChangeListener(onCheckedChangeListener);
            viewHolder.card.setOnClickListener(onItemClick);
            viewHolder.card.setOnLongClickListener(onDeleteClick);
            return viewHolder;
        } else if (viewType == HEAD) {
            View view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.layout_bracelet_reminder_head, parent, false);
            HeadViewHolder viewHolder = new HeadViewHolder(view);
            final GuideDotView[] dotViews = new GuideDotView[5];
            dotViews[0] = view.findViewById(R.id.dot0);
            dotViews[1] = view.findViewById(R.id.dot1);
            dotViews[2] = view.findViewById(R.id.dot2);
            dotViews[3] = view.findViewById(R.id.dot3);
            dotViews[4] = view.findViewById(R.id.dot4);

            dotViews[0].setColor(R.color.background_grey, R.color.primaryBlue);
            dotViews[1].setColor(R.color.background_grey, R.color.primaryBlue);
            dotViews[2].setColor(R.color.background_grey, R.color.primaryBlue);
            dotViews[3].setColor(R.color.background_grey, R.color.primaryBlue);
            dotViews[4].setColor(R.color.background_grey, R.color.primaryBlue);

            dotViews[0].select();
            viewHolder.viewPager.setAdapter(new MyViewPagerAdapter(viewList));
            viewHolder.viewPager.addOnPageChangeListener(new MyPageChangeListener(dotViews));
//            viewHolder.rbContinuous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (callback != null){
//                        callback.onStyleChange(isChecked?0:1);
//                    }
//                }
//            });
//            viewHolder.rbDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (callback != null){
//                        callback.onStyleChange(isChecked?1:0);
//                    }
//                }
//            });
//            viewHolder.tvAdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (callback != null){
//                        callback.onAdd();
//                    }
//                }
//            });
            return viewHolder;
        } else {
            throw new RuntimeException("there is unknown type ");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReminderViewHolder) {
            ReminderViewHolder viewHolder = (ReminderViewHolder) holder;
            AlertReminderModel item = data.get(position - 1);
            Calendar c = item.getStartTime();
            int date1 = c.get(Calendar.DATE);
            String startTimeString = DateFormat.format(MyConstant.DISPLAY_TIME, c).toString();
            c.add(Calendar.MINUTE, item.getSpanTime());
            int date2 = c.get(Calendar.DATE);
            String endTimeString = DateFormat.format(MyConstant.DISPLAY_TIME, c).toString();
            String nextDay = "";
            if (date2 != date1) {
                nextDay += recyclerView.getContext().getString(R.string.bracelet_reminder_next_day);
            }
            String time = startTimeString + " - " + nextDay + endTimeString;
            viewHolder.tvTime.setText(time);
            StringBuilder builder = new StringBuilder();
            boolean[] week = item.getLocalWeek();
            boolean everyday = week[1] && week[2] && week[3] && week[4] && week[5] && week[0] && week[6];
            boolean workday = week[1] && week[2] && week[3] && week[4] && week[5] && !week[0] && !week[6];
            boolean weekend = !week[1] && !week[2] && !week[3] && !week[4] && !week[5] && week[0] && week[6];
            if (everyday) {
                builder.append(everydayText);
            } else if (workday) {
                builder.append(workdayText);
            } else if (weekend) {
                builder.append(weekendText);
            } else {
                for (int i = 0; i < 7; i++) {
                    if (week[i]) {
                        builder.append(weekString[i]);
                        builder.append(" ");
                    }
                }
            }
            viewHolder.tvWeek.setText(builder.toString());
            viewHolder.sw.setChecked(item.isEnable());
        } else if (holder instanceof HeadViewHolder) {
//            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
//            headViewHolder.layoutStyle.setVisibility(showStyle?View.VISIBLE:View.GONE);
//            if (showStyle) {
//                if (style == 0) {
//                    if (!headViewHolder.rbContinuous.isChecked()){
//                        headViewHolder.rbContinuous.setChecked(true);
//                    }
//                    if (headViewHolder.rbDaily.isChecked()){
//                        headViewHolder.rbDaily.setChecked(false);
//                    }
//                }else {
//                    if (headViewHolder.rbContinuous.isChecked()){
//                        headViewHolder.rbContinuous.setChecked(false);
//                    }
//                    if (!headViewHolder.rbDaily.isChecked()){
//                        headViewHolder.rbDaily.setChecked(true);
//                    }
//                }
//            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD;
        } else {
            return CONTENT;
        }
    }

    private static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvWeek;
        SwitchCompat sw;
        View card;

        ReminderViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvWeek = itemView.findViewById(R.id.tv_week);
            sw = itemView.findViewById(R.id.sw_remind);
            card = itemView.findViewById(R.id.card);
        }
    }

    private static class HeadViewHolder extends RecyclerView.ViewHolder {
        private ViewPager viewPager;
//        RadioButton rbContinuous;
//        RadioButton rbDaily;
//        View layoutStyle;
//        View tvAdd;

        HeadViewHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.viewPager);
//            rbContinuous = itemView.findViewById(R.id.rb_continuous);
//            rbDaily = itemView.findViewById(R.id.rb_daily);
//            layoutStyle = itemView.findViewById(R.id.layout_style);
//            tvAdd = itemView.findViewById(R.id.tv_add);
        }
    }

    private View.OnClickListener onItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (callback != null) {
                int position = recyclerView.getChildLayoutPosition((View) v.getParent());
                callback.onItemClick(position - 1);
            }
        }
    };

    private View.OnLongClickListener onDeleteClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (callback != null) {
                int position = recyclerView.getChildLayoutPosition((View) v.getParent());
                callback.onDelete(position - 1);
                return true;
            }
            return false;
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (callback != null) {
                int position = recyclerView.getChildLayoutPosition((View) buttonView.getParent().getParent());
                callback.onSwitch(position - 1, isChecked);
            }
        }
    };


    private class MyViewPagerAdapter extends PagerAdapter {
        private List<View> views;

        MyViewPagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View item = (View) object;
            container.removeView(item);
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private GuideDotView[] dotViews;

        MyPageChangeListener(GuideDotView[] dotViews) {
            this.dotViews = dotViews;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < dotViews.length; i++) {
                if (i != position) {
                    dotViews[i].disSelect();
                } else {
                    dotViews[i].select();
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
