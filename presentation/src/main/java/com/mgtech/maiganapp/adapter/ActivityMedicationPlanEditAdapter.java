package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.MedicationPlanModel;

import java.util.List;
import java.util.Locale;

/**
 * @author zhaixiang
 */
public class ActivityMedicationPlanEditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SparseArray<List<MedicationPlanModel.DosageItem>> list;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_DOSAGE = 2;
    private static final int TYPE_STOP = 3;
    private String name;
    private String nameTitle;
    private String rangeTime = "";
    private String medicineCycleDays = "";
    private Listener listener;
    private String[] unitStrings;
    private String day;
    private boolean hasStopBtn;

    public interface Listener {
        /**
         * 点击设置名字
         */
        void setName();

        /**
         * 点击设置周期
         */
        void setCycle();

        /**
         * 点击设置剂量
         */
        void setDosage();

        /**
         * 终止用药
         */
        void stopPlan();
    }

    public ActivityMedicationPlanEditAdapter(RecyclerView recyclerView, Listener listener,
                                             SparseArray<List<MedicationPlanModel.DosageItem>> list,boolean hasStopButton) {
        this.list = list;
        this.listener = listener;
        this.unitStrings = recyclerView.getContext().getResources().getStringArray(R.array.medicine_dosage_unit);
        this.nameTitle = recyclerView.getContext().getString(R.string.activity_medication_plan_select_medicine);
        this.day = recyclerView.getContext().getString(R.string.activity_medication_plan_day);
        this.hasStopBtn = hasStopButton;
    }

    public void setHasStopBtn(boolean hasStopBtn) {
        this.hasStopBtn = hasStopBtn;
    }

    public void setList(SparseArray<List<MedicationPlanModel.DosageItem>> list) {
        this.list = list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRangeTime(String rangeTime) {
        this.rangeTime = rangeTime;
    }

    public void setMedicineCycleDays(String medicineCycleDays) {
        this.medicineCycleDays = medicineCycleDays;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == list.size() + 1) {
            return TYPE_STOP;
        } else {
            return TYPE_DOSAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case TYPE_HEADER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.activity_medication_plan_edit_list_header, viewGroup, false);
                HeaderViewHolder holder = new HeaderViewHolder(view);
                holder.layoutName.setOnClickListener(setNameListener);
                holder.layoutCycle.setOnClickListener(setCycleListener);
                holder.layoutDosage.setOnClickListener(setDosageListener);
                return holder;
            case TYPE_DOSAGE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.activity_medication_plan_edit_list_dosage, viewGroup, false);
                view.setOnClickListener(setDosageListener);
                return new DosageViewHolder(view);
            case TYPE_STOP:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.activity_medication_plan_edit_list_stop, viewGroup, false);
                StopViewHolder stopViewHolder = new StopViewHolder(view);
                stopViewHolder.tvStop.setOnClickListener(stopListener);
                return stopViewHolder;
            default:
        }
        throw new RuntimeException("没有此类型");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HeaderViewHolder) {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            if (TextUtils.isEmpty(name)) {
                holder.tvNameValue.setText(nameTitle);
            } else {
                holder.tvNameValue.setText(name);
            }
            holder.tvRangeTime.setText(rangeTime);
            holder.tvCycle.setText(medicineCycleDays);
        } else if (viewHolder instanceof DosageViewHolder) {
            List<MedicationPlanModel.DosageItem> dosageItems = list.valueAt(i - 1);
            DosageViewHolder holder = (DosageViewHolder) viewHolder;
            holder.bindTo(dosageItems,i,unitStrings,day);
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1 + (hasStopBtn?1:0);
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
//        TextView tvNameTitle;
        TextView tvNameValue;
        TextView tvRangeTime;
        TextView tvCycle;
        View layoutName;
        View layoutCycle;
        View layoutDosage;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
//            tvNameTitle = itemView.findViewById(R.id.tv_name_title);
            tvNameValue = itemView.findViewById(R.id.tv_name);
            tvRangeTime = itemView.findViewById(R.id.tv_range_time);
            tvCycle = itemView.findViewById(R.id.tv_cycle);
            layoutName = itemView.findViewById(R.id.layout_name);
            layoutCycle = itemView.findViewById(R.id.layout_repeat);
            layoutDosage = itemView.findViewById(R.id.layout_dosage);
        }
    }

    private static class DosageViewHolder extends RecyclerView.ViewHolder {
        TextView tvCycleDay;
        TextView[] tvTimes;
        TextView[] tvDosages;
        Group[] groups;

        DosageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimes = new TextView[4];
            tvDosages = new TextView[4];
            groups = new Group[4];
            tvCycleDay = itemView.findViewById(R.id.tv_cycle_day);
            tvTimes[0] = itemView.findViewById(R.id.tv_time0);
            tvDosages[0] = itemView.findViewById(R.id.tv_dosage0);
            tvTimes[1] = itemView.findViewById(R.id.tv_time1);
            tvDosages[1] = itemView.findViewById(R.id.tv_dosage1);
            tvTimes[2] = itemView.findViewById(R.id.tv_time2);
            tvDosages[2] = itemView.findViewById(R.id.tv_dosage2);
            tvTimes[3] = itemView.findViewById(R.id.tv_time3);
            tvDosages[3] = itemView.findViewById(R.id.tv_dosage3);
            groups[0] = itemView.findViewById(R.id.group0);
            groups[1] = itemView.findViewById(R.id.group1);
            groups[2] = itemView.findViewById(R.id.group2);
            groups[3] = itemView.findViewById(R.id.group3);
        }

        void bindTo(List<MedicationPlanModel.DosageItem> dosageItems, int index, String[] unit,String dayFormat) {
            tvCycleDay.setText(String.format(Locale.getDefault(),dayFormat,index));
            String unitString;
            for (int j = 0; j < groups.length; j++) {
                if (dosageItems != null) {
                    if (j < dosageItems.size()) {
                        float value = dosageItems.get(j).dosage;
                        if (value == Math.round(value)){
                            unitString = Math.round(value) + unit[dosageItems.get(j).dosageUnitType% unit.length];
                        }else {
                            unitString = value + unit[dosageItems.get(j).dosageUnitType % unit.length];
                        }
                        groups[j].setVisibility(View.VISIBLE);
                        tvTimes[j].setText(dosageItems.get(j).time);
                        tvDosages[j].setText(unitString);
                    } else {
                        groups[j].setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    private static class StopViewHolder extends RecyclerView.ViewHolder {
        TextView tvStop;

        StopViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStop = itemView.findViewById(R.id.tv_stop);
        }
    }

    private View.OnClickListener setNameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listener.setName();
        }
    };

    private View.OnClickListener setDosageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listener.setDosage();
        }
    };

    private View.OnClickListener stopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listener.stopPlan();
        }
    };

    private View.OnClickListener setCycleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listener.setCycle();
        }
    };
}
