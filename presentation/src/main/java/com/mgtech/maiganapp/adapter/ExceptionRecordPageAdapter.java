package com.mgtech.maiganapp.adapter;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.ExceptionRecordModel;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.widget.DotColorfulView;

import java.util.Locale;
import java.util.Objects;

/**
 * @author zhaixiang
 */
public class ExceptionRecordPageAdapter extends PagedListAdapter<ExceptionRecordModel,RecyclerView.ViewHolder> {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private RecyclerView recyclerView;
    private OnItemClickListener listener;
    private String timePattern;


    public ExceptionRecordPageAdapter(RecyclerView recyclerView, OnItemClickListener listener,DiffUtil.ItemCallback<ExceptionRecordModel> diffCallback) {
        super(diffCallback);
        this.recyclerView = recyclerView;
        this.listener = listener;
        this.timePattern =recyclerView.getResources().getString(R.string.notification_record_measure_time);
    }

    public  static class DiffCallback extends DiffUtil.ItemCallback<ExceptionRecordModel>{

        @Override
        public boolean areItemsTheSame(@NonNull ExceptionRecordModel oldItem, @NonNull ExceptionRecordModel newItem) {
            return Objects.equals(oldItem.noticeId, newItem.noticeId);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ExceptionRecordModel oldItem, @NonNull ExceptionRecordModel newItem) {
            return Objects.equals(oldItem, newItem);
        }
    }

    public interface OnItemClickListener{
        void onClick(ExceptionRecordModel model);
        void onLongClick(ExceptionRecordModel model);
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
        ExceptionRecordModel model = getItem(i);
        if (viewHolder instanceof RecordViewHolder && model != null){
            RecordViewHolder holder = (RecordViewHolder) viewHolder;
            holder.bindTo(model,timePattern);
        }
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


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = recyclerView.getChildLayoutPosition(v);
            if (listener != null){
                listener.onClick(getItem(index));
            }
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            int index = recyclerView.getChildLayoutPosition(v);
            if (listener != null){
                listener.onLongClick(getItem(index));
            }
            return true;
        }
    };
}
