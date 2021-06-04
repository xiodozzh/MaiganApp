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
import com.mgtech.maiganapp.data.model.ExceptionRecordModel;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.widget.DotColorfulView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author zhaixiang
 */
public class ExceptionRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private List<ExceptionRecordModel> data;
    private RecyclerView recyclerView;
    private Callback callback;
    private OnItemClickListener listener;
    private String timePattern;

    public ExceptionRecordAdapter(RecyclerView recyclerView,OnItemClickListener listener) {
        this.recyclerView = recyclerView;
        this.data = new ArrayList<>();
        this.callback = new Callback();
        this.listener = listener;
        this.timePattern =recyclerView.getResources().getString(R.string.notification_record_measure_time);
    }

    public interface OnItemClickListener{
        void onClick(int index);
        void onLongClick(int index);
    }

    public void setData(List<ExceptionRecordModel> data){
        callback.setNewList(data);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback,false);
        this.data.clear();
        this.data.addAll(data);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_exception_record_item,
                viewGroup,false);
        view.setOnClickListener(onClickListener);
        view.setOnLongClickListener(onLongClickListener);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ExceptionRecordModel model = data.get(i);
        if (viewHolder instanceof RecordViewHolder){
            RecordViewHolder holder = (RecordViewHolder) viewHolder;
            holder.bindTo(model,timePattern);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class RecordViewHolder extends RecyclerView.ViewHolder{
        TextView tvTime;
        TextView tvHrValue;
        TextView tvBpValue;
//        TextView tvTitle;
        TextView tvHrUnit;
        TextView tvBpUnit;
        DotColorfulView dotView;

        RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvHrValue = itemView.findViewById(R.id.tv_hr_value);
            tvBpValue = itemView.findViewById(R.id.tv_bp_value);
//            tvTitle = itemView.findViewById(R.id.tv_title);
            tvHrUnit = itemView.findViewById(R.id.tv_hr_unit);
            tvBpUnit = itemView.findViewById(R.id.tv_bp_unit);
            dotView = itemView.findViewById(R.id.dotView);
        }

        void bindTo(ExceptionRecordModel model,String timePattern){
            IndicatorDataModel indicatorDataModel = model.indicator;
            if (indicatorDataModel != null) {
                tvTime.setText(String.format(Locale.getDefault(),timePattern,DateFormat.format(DATE_FORMAT, indicatorDataModel.time)));
                tvHrValue.setText(String.valueOf(Math.round(indicatorDataModel.hr)));
                String bp = Math.round(indicatorDataModel.ps) + "/" + Math.round(indicatorDataModel.pd);
                tvBpValue.setText(bp);
                tvHrUnit.setText(indicatorDataModel.hrUnit);
                tvBpUnit.setText(indicatorDataModel.pdUnit);
//                tvTitle.setText(model.description);
//                dotView.setVisibility(model.isRead?View.GONE:View.VISIBLE);
            }
        }
    }
    private class Callback extends DiffUtil.Callback {
        private List<ExceptionRecordModel> newList;

        void setNewList(List<ExceptionRecordModel> newList) {
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return data.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return Objects.equals(data.get(i).noticeId, newList.get(i1).noticeId);
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return Objects.equals(data.get(i), newList.get(i1));
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = recyclerView.getChildLayoutPosition(v);
            if (listener != null){
                listener.onClick(index);
            }
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            int index = recyclerView.getChildLayoutPosition(v);
            if (listener != null){
                listener.onLongClick(index);
            }
            return true;
        }
    };
}
