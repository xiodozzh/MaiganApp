package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.HealthKnowledgeModel;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.widget.UnreadImageView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhaixiang
 */
public class HealthKnowledgeAdapter extends RecyclerView.Adapter<HealthKnowledgeAdapter.KnowledgeViewHolder> {
    private List<HealthKnowledgeModel> data;
    private RecyclerView recyclerView;
    private Listener listener;
    private DiffCallback diffCallback;

    public interface Listener{
        /**
         * @param index 点击的下标
         */
        void onClick(int index);
    }

    public HealthKnowledgeAdapter(RecyclerView recyclerView, Listener listener) {
        this.recyclerView = recyclerView;
        this.listener = listener;
        this.data = new ArrayList<>();
        this.diffCallback = new DiffCallback();
    }

    public void setData(List<HealthKnowledgeModel> data){
        diffCallback.setNewData(data);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback,false);
        this.data.clear();
        this.data.addAll(data);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public KnowledgeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_knowledge_list_item,
                viewGroup,false);
        view.setOnClickListener(onClickListener);
        return new KnowledgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KnowledgeViewHolder knowledgeViewHolder, int i) {
        HealthKnowledgeModel model = data.get(i);
        knowledgeViewHolder.tvTitle.setText(model.title);
        knowledgeViewHolder.tvSubTitle.setText(model.subTitle);
        Logger.i( "onBindViewHolder: model.isRead "+ model.isRead);
        knowledgeViewHolder.ivUnread.setVisibility(model.isRead ?View.INVISIBLE:View.VISIBLE);
        ViewUtils.loadBigImageUsingGlide(recyclerView.getContext(),R.drawable.activity_health_knowledge_item_network_error,model.imgUrl,knowledgeViewHolder.iv);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class KnowledgeViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tvTitle;
        TextView tvSubTitle;
        ImageView ivUnread;

        KnowledgeViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubTitle = itemView.findViewById(R.id.tv_sub_title);
            ivUnread = itemView.findViewById(R.id.iv_unread);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null){
                int index = recyclerView.getChildLayoutPosition(v);
                listener.onClick(index);
            }
        }
    };

    private class DiffCallback extends DiffUtil.Callback {
        private List<HealthKnowledgeModel> newData;

        void setNewData(List<HealthKnowledgeModel> newData) {
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
            return Objects.equals(data.get(i).knowledgeId,newData.get(i1).knowledgeId);
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return Objects.equals(data.get(i),newData.get(i1));
        }
    }
}
