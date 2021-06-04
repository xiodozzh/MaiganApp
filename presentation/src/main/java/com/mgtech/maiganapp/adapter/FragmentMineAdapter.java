package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.IMineModel;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.widget.UnreadImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class FragmentMineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<IMineModel> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private static final int INFO = 660;
    private static final int ITEM = 532;
    private static final int UNBIND = 172;
    private static final int BIND = 768;
    private Listener listener;

    public interface Listener{
        void onTypeClick(int type);
    }

    public FragmentMineAdapter(RecyclerView recyclerView,Listener listener) {
        this.recyclerView = recyclerView;
        this.listener = listener;
    }

    public void setDataList(List<IMineModel> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        IMineModel model = dataList.get(position);
        if (model instanceof IMineModel.Info) {
            return INFO;
        } else if (model instanceof IMineModel.UnBind) {
            return UNBIND;
        } else if (model instanceof IMineModel.Bind) {
            return BIND;
        } else {
            return ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case INFO:
                view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.fragment_i_recycler_info, viewGroup, false);
                view.setOnClickListener(onClickListener);
                return new InfoViewHolder(view);
            case ITEM:
                view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.fragment_i_recycler_item, viewGroup, false);
                view.setOnClickListener(onClickListener);
                return new ItemViewHolder(view);
            case UNBIND:
                view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.fragment_i_recycler_un_bind, viewGroup, false);
                view.setOnClickListener(onClickListener);
                return new UnBindViewHolder(view);
            case BIND:
                view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.fragment_i_recycler_bind, viewGroup, false);
                view.setOnClickListener(onClickListener);
                return new BindViewHolder(view);
            default:
        }
        throw new RuntimeException("没有此类型");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof InfoViewHolder){
            IMineModel.Info model = (IMineModel.Info) dataList.get(i);
            InfoViewHolder holder = (InfoViewHolder) viewHolder;
            holder.tvName.setText(model.name);
            ViewUtils.loadImageUsingGlide(recyclerView.getContext(),R.drawable.avatar_default,model.avatarUrl,holder.ivAvatar,false);
        }else if (viewHolder instanceof ItemViewHolder){
            IMineModel.Item model = (IMineModel.Item) dataList.get(i);
            ItemViewHolder holder = (ItemViewHolder) viewHolder;
            holder.ivIcon.setImageResource(model.icon);
            holder.ivIcon.setUnread(!model.isRead());
            holder.tvTitle.setText(model.text);
            holder.tvValue.setText(model.value);
        }else if (viewHolder instanceof BindViewHolder){
            IMineModel.Bind model = (IMineModel.Bind) dataList.get(i);
            BindViewHolder holder = (BindViewHolder) viewHolder;
            holder.tvSyncTime.setText(model.time);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private static class InfoViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivAvatar;

        InfoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvValue;
        UnreadImageView ivIcon;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvValue = itemView.findViewById(R.id.tv_value);
            ivIcon = itemView.findViewById(R.id.iv_icon);
        }
    }

    private static class UnBindViewHolder extends RecyclerView.ViewHolder {

        UnBindViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    private static class BindViewHolder extends RecyclerView.ViewHolder {
        TextView tvSyncTime;

        BindViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSyncTime = itemView.findViewById(R.id.tv_sync_time);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null){
                int index = recyclerView.getChildLayoutPosition(v);
                listener.onTypeClick(dataList.get(index).getType());
            }
        }
    };
}
