package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.WeeklyReportModel;
import com.mgtech.maiganapp.widget.ActivityWeeklyReportGraphView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhaixiang
 */
public class WeeklyReportAdapter extends RecyclerView.Adapter<WeeklyReportAdapter.ReportViewHolder> {
    private List<WeeklyReportModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private Listener listener;
    private DiffCallback diffCallback;

    public interface Listener{
        void check(int index);
    }

    public WeeklyReportAdapter(RecyclerView recyclerView,Listener listener) {
        this.recyclerView = recyclerView;
        this.listener = listener;
        this.diffCallback = new DiffCallback();
    }

    public void setData(List<WeeklyReportModel> list){
        diffCallback.setNewList(list);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback,false);
        this.list.clear();
        this.list.addAll(list);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_history_weekly_report_item,
                viewGroup, false);
        ReportViewHolder viewHolder = new ReportViewHolder(view);
        view.setOnClickListener(onCheckClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder viewHolder, int i) {
        WeeklyReportModel model = list.get(i);
        viewHolder.tvTitle.setText(recyclerView.getContext().getString(R.string.activity_weekly_report_bp_control_situation));
        String dateString = DateFormat.format(MyConstant.DATE_FORMAT2, model.startTime) + "-" +
                DateFormat.format(MyConstant.DATE_FORMAT2, model.endTime-1);
        viewHolder.tvDate.setText(dateString);
        viewHolder.graphView.setCalendar(model.startTime,model.endTime);
        viewHolder.graphView.setData(model.indicators);
        viewHolder.ivUnread.setVisibility(model.read?View.INVISIBLE:View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDate;
        TextView tvCheck;
        ImageView ivUnread;
        ActivityWeeklyReportGraphView graphView;

        ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvCheck = itemView.findViewById(R.id.tv_check);
            graphView = itemView.findViewById(R.id.graph);
            ivUnread = itemView.findViewById(R.id.iv_unread);
        }
    }

    private View.OnClickListener onCheckClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.check(recyclerView.getChildLayoutPosition(v));
            }
        }
    };

    private class DiffCallback extends DiffUtil.Callback{
        private List<WeeklyReportModel> newList;

        void setNewList(List<WeeklyReportModel> newList) {
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return list.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return Objects.equals(list.get(i).weekReportGuid,newList.get(i1).weekReportGuid);
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return Objects.equals(list.get(i),newList.get(i1));
        }
    }
}
