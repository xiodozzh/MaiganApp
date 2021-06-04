package com.mgtech.maiganapp.adapter;

import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.FriendRequestModel;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhaixiang
 */
public class ActivityFriendDealRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FriendRequestModel> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private String haveDenied;
    private String haveAccepted;
    private Listener listener;
    private DiffCallback diffCallback;

    public interface Listener {
        void onAccept(int index);
        void onDeny(int index);
    }

    public ActivityFriendDealRequestAdapter(RecyclerView recyclerView,Listener listener) {
        this.recyclerView = recyclerView;
        Resources res = recyclerView.getResources();
        this.haveAccepted = res.getString(R.string.friend_relation_request_have_accepted);
        this.haveDenied = res.getString(R.string.friend_relation_request_have_denied);
        this.listener = listener;
        this.diffCallback = new DiffCallback();
    }

    public void setData(List<FriendRequestModel> list){
        diffCallback.setList(list);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback,false);
        this.dataList.clear();
        this.dataList.addAll(list);

        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_friend_deal_request_item,viewGroup,false);
        RequestViewHolder viewHolder = new RequestViewHolder(view);
        viewHolder.tvAccept.setOnClickListener(onAcceptClickListener);
        viewHolder.tvDeny.setOnClickListener(onDenyClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RequestViewHolder){
            FriendRequestModel model = dataList.get(i);
            RequestViewHolder holder = (RequestViewHolder) viewHolder;
            holder.tvName.setText(model.name);
            holder.tvMessage.setText(model.message);
            if (!TextUtils.isEmpty(model.avatarUri)){
                ViewUtils.loadImageUsingGlide(recyclerView.getContext(),R.drawable.avatar_default_round,
                        model.avatarUri,holder.ivAvatar,true);
            }
            if (model.result == FriendRequestModel.RESULT_NONE){
                holder.tvDeny.setVisibility(View.VISIBLE);
                holder.tvAccept.setVisibility(View.VISIBLE);
                holder.tvDone.setVisibility(View.GONE);
            }else{
                holder.tvDeny.setVisibility(View.INVISIBLE);
                holder.tvAccept.setVisibility(View.INVISIBLE);
                holder.tvDone.setVisibility(View.VISIBLE);
                if (model.result == FriendRequestModel.RESULT_ACCEPT){
                    holder.tvDone.setText(haveAccepted);
                }else if (model.result == FriendRequestModel.RESULT_DENY){
                    holder.tvDone.setText(haveDenied);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private static class RequestViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvName;
        TextView tvMessage;
        TextView tvAccept;
        TextView tvDeny;
        TextView tvDone;
//        Group group;

        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvAccept = itemView.findViewById(R.id.tv_accept);
            tvDeny = itemView.findViewById(R.id.tv_deny);
            tvDone = itemView.findViewById(R.id.tv_done);
        }
    }

    private View.OnClickListener onAcceptClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = recyclerView.getChildLayoutPosition((View) v.getParent());
            if (listener != null){
                listener.onAccept(index);
            }
        }
    };

    private View.OnClickListener onDenyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = recyclerView.getChildLayoutPosition((View) v.getParent());
            if (listener != null){
                listener.onDeny(index);
            }
        }
    };

    private class DiffCallback extends DiffUtil.Callback {
        private List<FriendRequestModel> list;

        public void setList(List<FriendRequestModel> list) {
            this.list = list;
        }

        @Override
        public int getOldListSize() {
            return dataList.size();
        }

        @Override
        public int getNewListSize() {
            return list.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return Objects.equals(dataList.get(i).id,list.get(i1).id);
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return Objects.equals(dataList.get(i),list.get(i1));
        }
    }
}
