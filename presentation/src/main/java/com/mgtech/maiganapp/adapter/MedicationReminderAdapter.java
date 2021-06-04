package com.mgtech.maiganapp.adapter;

import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.MedicationReminderModel;
import com.mgtech.maiganapp.widget.ActivityMedicationReminderStateView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author zhaixiang
 */
public class MedicationReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MedicationReminderModel> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private static final int TODAY = 0;
    private static final int FUTURE = 1;
    private OnButtonClickListener listener;
    private String[] units;
    private String futureTimeFormat;
    private String hasTaken;
    private String hasIgnore;
    private java.text.DateFormat dateFormat;
    private Calendar current = Calendar.getInstance();
    private int normalColor;
    private int abnormalColor;

    public interface OnButtonClickListener {
        /**
         * 点击忽略
         *
         * @param index 点击条目
         */
        void onIgnoreButtonClick(int index);

        /**
         * 点击确定
         *
         * @param index 点击条目
         */
        void onDoneButtonClick(int index);

        /**
         * 点击重置
         *
         * @param index 点击条目
         */
        void onReset(int index);
    }

    public MedicationReminderAdapter(RecyclerView recyclerView, OnButtonClickListener listener) {
        this.recyclerView = recyclerView;
        this.listener = listener;
        this.units = recyclerView.getResources().getStringArray(R.array.medicine_dosage_unit);
        this.hasIgnore = recyclerView.getResources().getString(R.string.medication_has_ignore);
        this.hasTaken = recyclerView.getResources().getString(R.string.medication_has_taken);
        this.futureTimeFormat = recyclerView.getResources().getString(R.string.sport_date_format_month_day) + " EEE";
//        this.dateFormat = new SimpleDateFormat(recyclerView.getResources().getString(R.string.sport_date_format_month_day), Locale.getDefault());
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        this.normalColor = ContextCompat.getColor(recyclerView.getContext(),R.color.black_text);
        this.abnormalColor = ContextCompat.getColor(recyclerView.getContext(),R.color.light_text);
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);
    }

    public void setData(List<MedicationReminderModel> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        MedicationReminderModel model = data.get(position);
        return model.future ? FUTURE : TODAY;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case TODAY:
                view = LayoutInflater.from(recyclerView.getContext()).inflate(
                        R.layout.activity_medication_reminder_item_today, viewGroup, false);
                TodayViewHolder todayViewHolder = new TodayViewHolder(view);
                view.setOnClickListener(onClickCardViewListener);
                todayViewHolder.btnDone.setOnClickListener(onClickDoneListener);
                todayViewHolder.btnIgnore.setOnClickListener(onClickIgnoreListener);
                return new TodayViewHolder(view);
            case FUTURE:
                view = LayoutInflater.from(recyclerView.getContext()).inflate(
                        R.layout.activity_medication_reminder_item_future, viewGroup, false);
                return new FutureViewHolder(view);
            default:
        }
        throw new RuntimeException("没有这种类型");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof TodayViewHolder) {
            MedicationReminderModel model = data.get(i);
            TodayViewHolder holder = (TodayViewHolder) viewHolder;
            holder.tvTime.setText(model.time);
            String dosage;
            String value;
            if (Math.round(model.dosage) == model.dosage){
                value = Math.round(model.dosage)+"";
            }else{
                value = model.dosage + "";
            }
            if (model.dosageUnitType >= 0 && model.dosageUnitType < units.length) {
                dosage = value + units[model.dosageUnitType];
            } else {
                dosage = value;
            }
            holder.tvDosage.setText(dosage);
            holder.tvTitle.setText(model.name);
            if (model.state == MedicationReminderModel.TAKEN) {
                holder.dot.setState(ActivityMedicationReminderStateView.TAKE);
                setState(abnormalColor,holder,hasTaken);
            } else if (model.state == MedicationReminderModel.IGNORE) {
                holder.dot.setState(ActivityMedicationReminderStateView.IGNORE);
                setState(abnormalColor,holder,hasIgnore);
            } else if (model.state == MedicationReminderModel.DEFAULT) {
                holder.dot.setState(ActivityMedicationReminderStateView.DEFAULT);
                setState(normalColor,holder,"");
            }
//            setDeleteLine(showLine, holder.tvTitle);
        } else if (viewHolder instanceof FutureViewHolder) {

            MedicationReminderModel model = data.get(i);
            Date date = new Date();
            try {
                date = dateFormat.parse(model.time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            FutureViewHolder holder = (FutureViewHolder) viewHolder;
            String dosageText;
            String value;
            if (Math.round(model.dosage) == model.dosage){
                value = Math.round(model.dosage)+"";
            }else{
                value = model.dosage + "";
            }
            if (model.dosageUnitType >= 0 && model.dosageUnitType < units.length) {
                dosageText = value + units[model.dosageUnitType];
            } else {
                dosageText = value;
            }
            holder.tvDosage.setText(dosageText);
            holder.tvTitle.setText(model.name);
            holder.tvTime.setText(DateFormat.format(futureTimeFormat, date));
            holder.tvDosageTime.setText(DateFormat.format("HH:mm", date));
            int remainDays = (int) ((date.getTime() - current.getTimeInMillis()) / (24 * 3600_000));
            if (remainDays <= 0) {
                remainDays = 1;
            }
            holder.tvRemain.setText(String.valueOf(remainDays));
            if (i > 0 && data.get(i - 1).future) {
                holder.tvTag.setVisibility(View.GONE);
            } else {
                holder.tvTag.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setDeleteLine(boolean showLine, TextView tv) {
        if (showLine) {
            tv.getPaint().setFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tv.getPaint().setFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    private void setState(int color, TodayViewHolder holder,String resultText){
        holder.tvTitle.setTextColor(color);
        holder.tvTime.setTextColor(color);
        holder.tvDosage.setTextColor(color);
        if (TextUtils.isEmpty(resultText)){
            holder.btnDone.setVisibility(View.VISIBLE);
            holder.btnIgnore.setVisibility(View.VISIBLE);
            holder.tvResult.setVisibility(View.GONE);
        }else{
            holder.btnDone.setVisibility(View.GONE);
            holder.btnIgnore.setVisibility(View.GONE);
            holder.tvResult.setVisibility(View.VISIBLE);
            holder.tvResult.setText(resultText);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class TodayViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDosage;
        TextView tvTime;
        TextView btnDone;
        TextView btnIgnore;
        TextView tvResult;
//        Group groupSelect;
        ActivityMedicationReminderStateView dot;

        TodayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDosage = itemView.findViewById(R.id.tv_dosage);
            tvTime = itemView.findViewById(R.id.tv_time);
            btnDone = itemView.findViewById(R.id.tv_done);
            btnIgnore = itemView.findViewById(R.id.tv_ignore);
            tvResult = itemView.findViewById(R.id.tv_result);
//            groupSelect = itemView.findViewById(R.id.group_have_set);
            dot = itemView.findViewById(R.id.dot);
        }
    }

    private static class FutureViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDosage;
        TextView tvTime;
        TextView tvRemain;
        TextView tvTag;
        TextView tvDosageTime;

        FutureViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDosage = itemView.findViewById(R.id.tv_dosage);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvRemain = itemView.findViewById(R.id.tv_remain_days);
            tvTag = itemView.findViewById(R.id.tv_tag);
            tvDosageTime = itemView.findViewById(R.id.tv_dosage_time);
        }
    }

    private View.OnClickListener onClickCardViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    private View.OnClickListener onClickIgnoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = recyclerView.getChildLayoutPosition((View) v.getParent().getParent());
            if (listener != null) {
                listener.onIgnoreButtonClick(index);
            }
        }
    };

    private View.OnClickListener onClickDoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = recyclerView.getChildLayoutPosition((View) v.getParent().getParent());
            if (listener != null) {
                listener.onDoneButtonClick(index);
            }
        }
    };

    private View.OnClickListener onSelectWrongListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = recyclerView.getChildLayoutPosition((View) v.getParent().getParent());
//            data.get(index).state = MedicationReminderModel.DEFAULT;
//            notifyItemChanged(index);
            if (listener != null) {
                listener.onReset(index);
            }
        }
    };
}
