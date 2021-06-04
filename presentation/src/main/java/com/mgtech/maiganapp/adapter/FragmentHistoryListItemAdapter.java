package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.IIndicatorModel;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.utils.IndicatorUtils;
import com.mgtech.maiganapp.widget.HistoryDataListCircleDotView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author zhaixiang
 * 数据列表 adapter
 */

public class FragmentHistoryListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<IndicatorDataModel> data;
    private Callback callback;
    private RecyclerView recyclerView;
    private DiffUtilCallback diffUtilCallback;

    public interface Callback {
        void onItemClick(int position);
        void onLongClick(int position);
    }

    public FragmentHistoryListItemAdapter(RecyclerView recyclerView) {
        this.data = new ArrayList<>();
        this.recyclerView = recyclerView;
        this.diffUtilCallback = new DiffUtilCallback();
    }

    public void setData(List<IndicatorDataModel> data){
//        diffUtilCallback.setNewData(data);
//        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtilCallback,false);
        this.data.clear();
        this.data.addAll(data);
//        result.dispatchUpdatesTo(this);
        notifyDataSetChanged();
    }

    private class DiffUtilCallback extends DiffUtil.Callback {
        private List<IndicatorDataModel> newData;

        void setNewData(List<IndicatorDataModel> newData) {
            this.newData = newData;
        }

        @Override
        public int getOldListSize() {
            return data.size();
        }

        @Override
        public int getNewListSize() {
            return newData.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return data.get(i).time == newData.get(i1).time;
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            IndicatorDataModel oldModel = data.get(i);
            IndicatorDataModel newModel = newData.get(i1);
            return Objects.equals(oldModel,newModel);
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_history_health_list_item_layout,
                parent, false);
        IndicatorViewHolder indicatorViewHolder = new IndicatorViewHolder(view);
        indicatorViewHolder.view.setOnClickListener(onItemClick);
        indicatorViewHolder.view.setOnLongClickListener(onItemLongClick);
        return indicatorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof IndicatorViewHolder) {
            IndicatorViewHolder viewHolder = (IndicatorViewHolder) holder;
            IndicatorDataModel indicatorDataModel = data.get(position);
            String bpString = Math.round(indicatorDataModel.ps) +"/" + Math.round(indicatorDataModel.pd);
            String hrString = String.valueOf(Math.round(indicatorDataModel.hr));

            viewHolder.tvTime.setText(DateFormat.format(MyConstant.DISPLAY_TIME, indicatorDataModel.time));
            viewHolder.tvBpValue.setText(bpString);
            viewHolder.tvHrValue.setText(hrString);
            boolean isAbnormal = indicatorDataModel.psLevel != 0 || indicatorDataModel.pdLevel != 0 || indicatorDataModel.hrLevel != 0;
            viewHolder.dotView.setAbnormal(isAbnormal);
            viewHolder.autoMeasure.setVisibility(indicatorDataModel.isAuto?View.VISIBLE:View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class IndicatorViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvBpValue;
        TextView tvHrValue;
        HistoryDataListCircleDotView dotView;
        View view;
        View autoMeasure;

        IndicatorViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvBpValue = itemView.findViewById(R.id.tv_bp_value);
            tvHrValue = itemView.findViewById(R.id.tv_hr_value);
            dotView = itemView.findViewById(R.id.dotView);
            autoMeasure = itemView.findViewById(R.id.iv_auto);
            view = itemView;
        }
    }

    private View.OnClickListener onItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (callback != null) {
                int position = recyclerView.getChildLayoutPosition(v);
                callback.onItemClick(position);
            }
        }
    };

    private View.OnLongClickListener onItemLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (callback != null) {
                int position = recyclerView.getChildLayoutPosition(v);
                callback.onLongClick(position);
            }
            return true;
        }
    };

}
