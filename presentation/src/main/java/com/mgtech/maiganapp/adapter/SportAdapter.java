package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.StepModel;

import java.util.List;
import java.util.Locale;

/**
 * Created by zhaixiang on 2017/11/14.
 * 运动
 */

public class SportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER = 1;
    private static final int ITEM = 2;
    private List<StepModel> detailData;
    private View headerView;
    private String dateFormat;


    public SportAdapter(List<StepModel> data, View headerView) {
        this.detailData = data;
        this.headerView = headerView;
        this.dateFormat = headerView.getContext().getString(R.string.sport_date_format_month_day);
    }


    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                return new HeaderViewHolder(headerView);
            case ITEM:
                View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sport_item, parent, false);
                return new SportViewHolder(item);
            default:
        }
        throw new RuntimeException("没有这种类型");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position != 0 && holder instanceof SportViewHolder) {
            StepModel item = detailData.get(position - 1);
            SportViewHolder sportViewHolder = (SportViewHolder) holder;

            sportViewHolder.tvDate.setText(DateFormat.format(dateFormat, item.startTime));
            sportViewHolder.tvStep.setText(String.valueOf(Math.round(item.stepCount)));
            sportViewHolder.tvDistance.setText(String.valueOf(Math.round(item.distance)));
            sportViewHolder.tvHeat.setText(String.format(Locale.getDefault(), "%.1f", item.heat / 1000f));
            sportViewHolder.tvTime.setText(String.format(Locale.getDefault(),
                    headerView.getContext().getString(R.string.sport_n_minute), Math.round(item.duration)));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else {
            return ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return detailData.size() + 1;
    }

    private static class SportViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvTime;
        private TextView tvStep;
        private TextView tvDistance;
        private TextView tvHeat;

        SportViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvStep = itemView.findViewById(R.id.tv_step);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            tvHeat = itemView.findViewById(R.id.tv_heat);
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
