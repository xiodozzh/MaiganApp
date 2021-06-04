package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.MedicationPlanModel;
import com.mgtech.maiganapp.widget.ActivityMedicationPlanBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author zhaixiang
 */
public class MedicationPlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerView recyclerView;
    private List<MedicationPlanModel> dataList;
    private String startFormat;
    private String takeFormat;
    private String notTakeFormat;
    private String notRecordFormat;
    private String leftFormat;
    private String timeFormat;
    private DiffUtilCallback diffUtilCallback;
    private Listener listener;

    public interface Listener {
        /**
         * item被点击
         *
         * @param index 点击序号
         */
        void onClick(int index);

        /**
         * 长按删除
         *
         * @param index 序号
         */
        void onLongClick(int index);
    }

    public MedicationPlanAdapter(RecyclerView recyclerView, Listener listener) {
        this.listener = listener;
        this.recyclerView = recyclerView;
        this.dataList = new ArrayList<>();
        this.diffUtilCallback = new DiffUtilCallback();
        this.diffUtilCallback.setOldList(dataList);
        this.startFormat = recyclerView.getContext().getString(R.string.activity_medication_plan_start_format);
        this.takeFormat = recyclerView.getContext().getString(R.string.activity_medication_plan_take_format);
        this.notTakeFormat = recyclerView.getContext().getString(R.string.activity_medication_plan_not_take_format);
        this.notRecordFormat = recyclerView.getContext().getString(R.string.activity_medication_plan_not_record_format);
        this.leftFormat = recyclerView.getContext().getString(R.string.activity_medication_plan_left_format);
        this.timeFormat = recyclerView.getContext().getString(R.string.activity_medication_plan_time_format);
    }

    public void setData(List<MedicationPlanModel> dataList) {
        diffUtilCallback.setNewList(dataList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback, false);
        this.dataList.clear();
        this.dataList.addAll(dataList);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_medication_plan_item,
                viewGroup, false);
        view.setOnClickListener(onClickListener);
        view.setOnLongClickListener(onLongClickListener);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof PlanViewHolder) {
            PlanViewHolder holder = (PlanViewHolder) viewHolder;
            holder.bindTo(dataList.get(i), timeFormat, startFormat, takeFormat, notTakeFormat, notRecordFormat, leftFormat);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvMedicationTime;
        TextView tvTakeDays;
        TextView tvNotRecord;
        TextView tvNotTake;
        TextView tvLeftDays;
        ActivityMedicationPlanBarView barView;

        PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMedicationTime = itemView.findViewById(R.id.tv_medication_time);
            tvTakeDays = itemView.findViewById(R.id.tv_take_days);
            tvNotTake = itemView.findViewById(R.id.tv_forget_days);
            tvNotRecord = itemView.findViewById(R.id.tv_default_days);
            tvLeftDays = itemView.findViewById(R.id.tv_left_days);
            barView = itemView.findViewById(R.id.progressBar);
        }

        void bindTo(MedicationPlanModel model, String timeFormat, String startFormat, String takeFormat,
                    String notTakeFormat, String notRecordFormat, String leftFormat) {
            tvName.setText(model.name);
            tvMedicationTime.setText(String.format(Locale.getDefault(), timeFormat,
                    DateFormat.format(startFormat, model.startTime), DateFormat.format(startFormat, model.endTime)));
            tvTakeDays.setText(String.format(Locale.getDefault(), takeFormat, model.completeNumber));
            tvNotTake.setText(String.format(Locale.getDefault(), notTakeFormat, model.ignoreNumber));
            tvNotRecord.setText(String.format(Locale.getDefault(), notRecordFormat, model.forgetNumber));
            tvLeftDays.setText(String.format(Locale.getDefault(), leftFormat,
                    model.totalNumber - model.completeNumber - model.ignoreNumber - model.forgetNumber));
            barView.setRatio(model.ignoreNumber / (float) model.totalNumber,
                    model.completeNumber / (float) model.totalNumber,
                    model.forgetNumber / (float) model.totalNumber);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                int index = recyclerView.getChildLayoutPosition(v);
                listener.onClick(index);
            }
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                int index = recyclerView.getChildLayoutPosition(v);
                listener.onLongClick(index);
            }
            return true;
        }
    };

    private class DiffUtilCallback extends DiffUtil.Callback {
        private List<MedicationPlanModel> oldList;
        private List<MedicationPlanModel> newList;

        void setOldList(List<MedicationPlanModel> oldList) {
            this.oldList = oldList;
        }

        void setNewList(List<MedicationPlanModel> newList) {
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return Objects.equals(oldList.get(i).planId, newList.get(i1).planId);
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return oldList.get(i).equals(newList.get(i1));
        }
    }
}
