package com.mgtech.maiganapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.FriendModel;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.widget.UnreadImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhaixiang
 */
public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FriendModel> list;
    private RecyclerView recyclerView;
    private DiffCallback diffCallback;
    private Listener listener;
    private static final int EMPTY = 124;
    private static final int LIST = 125;

    public interface Listener {
        void onDataClick(int index);

        void onPersonInfoClick(int index);

        void onLongClick(int index);
    }

    public FriendRecyclerViewAdapter(RecyclerView recyclerView, Listener listener) {
        this.list = new ArrayList<>();
        this.recyclerView = recyclerView;
        this.diffCallback = new DiffCallback();
        diffCallback.setOldList(list);
        this.listener = listener;
    }

    public void setData(List<FriendModel> list) {
        diffCallback.setNewList(list);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback, false);
        this.list.clear();
        this.list.addAll(list);
        result.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemViewType(int position) {
        return position == list.size() ? EMPTY : LIST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case EMPTY:
                return new EmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.fragment_friend_viewpager_recycler_padding, viewGroup, false));
            case LIST:
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.fragment_friend_i_follow_viewpager_recycler_item, viewGroup, false);
                view.setOnLongClickListener(onLongClickListener);
                ItemViewHolder viewHolder = new ItemViewHolder(view);
                view.setOnClickListener(onMessageClick);
                viewHolder.ivAvatar.setOnClickListener(onPersonClick);
                return viewHolder;
            default:
        }
        throw new RuntimeException("没有这种类型");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ItemViewHolder) {
            FriendModel model = list.get(i);
            ItemViewHolder holder = (ItemViewHolder) viewHolder;
            holder.bindTo(recyclerView.getContext(), model);
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvNameOnly;
        ImageView ivAvatar;
        TextView tvBp;
        TextView tvHr;
        TextView tvMeasureTime;
        TextView tvHrTitle;
        TextView tvBpTitle;
        TextView tvMeasureTimeTitle;
        UnreadImageView ivMessage;
//        Group group;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvBp = itemView.findViewById(R.id.tv_bp_value);
            tvHr = itemView.findViewById(R.id.tv_hr_value);
            tvMeasureTime = itemView.findViewById(R.id.tv_measure_value);
            ivMessage = itemView.findViewById(R.id.iv_message);
            tvHrTitle = itemView.findViewById(R.id.tv_hr_title);
            tvBpTitle = itemView.findViewById(R.id.tv_bp_title);
            tvMeasureTimeTitle = itemView.findViewById(R.id.tv_measure_title);
            tvNameOnly = itemView.findViewById(R.id.tv_name_only);
        }

        void hideGroup() {
            tvName.setVisibility(View.INVISIBLE);
            tvBp.setVisibility(View.INVISIBLE);
            tvHr.setVisibility(View.INVISIBLE);
            tvMeasureTime.setVisibility(View.INVISIBLE);
            tvHrTitle.setVisibility(View.INVISIBLE);
            tvBpTitle.setVisibility(View.INVISIBLE);
            tvMeasureTimeTitle.setVisibility(View.INVISIBLE);

            tvNameOnly.setVisibility(View.VISIBLE);
//            ivMessage.setVisibility(View.GONE);
        }

        void showGroup() {
            tvName.setVisibility(View.VISIBLE);
            tvBp.setVisibility(View.VISIBLE);
            tvHr.setVisibility(View.VISIBLE);
            tvMeasureTime.setVisibility(View.VISIBLE);
            tvHrTitle.setVisibility(View.VISIBLE);
            tvBpTitle.setVisibility(View.VISIBLE);

            tvMeasureTimeTitle.setVisibility(View.VISIBLE);

            tvNameOnly.setVisibility(View.GONE);
        }


        void bindTo(Context context, FriendModel model) {
            ivMessage.setUnread(model.getPwUnreadNumber() > 0 || model.getAbnormalUnreadNumber() > 0);
            if (model.isHaveData()) {
                showGroup();
            } else {
                hideGroup();
            }
//            ivMessage.setUnreadNumber(90);
            tvMeasureTime.setText(DateFormat.format(MyConstant.FULL_DATETIME_FORMAT, model.getLastMeasureTime()));
            String bp = Math.round(model.getPs()) + "/" + Math.round(model.getPd());
            tvBp.setText(bp);
            tvHr.setText(String.valueOf(Math.round(model.getHr())));
            String name = model.getNoteName();
            if (TextUtils.isEmpty(name)) {
                name = model.getName();
            }
            tvName.setText(name);
            tvNameOnly.setText(name);
            ViewUtils.loadImageUsingGlide(context, R.drawable.avatar_default_round, model.getAvatarUrl(), ivAvatar, true);
        }
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private static class DiffCallback extends DiffUtil.Callback {
        private List<FriendModel> oldList = new ArrayList<>();
        private List<FriendModel> newList = new ArrayList<>();

        void setOldList(List<FriendModel> oldList) {
            this.oldList = oldList;
        }

        void setNewList(List<FriendModel> newList) {
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
            return Objects.equals(oldList.get(i).getRelationId(), newList.get(i1).getRelationId());
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return Objects.equals(oldList.get(i), newList.get(i1));
        }
    }

    private View.OnClickListener onMessageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = recyclerView.getChildLayoutPosition(v);
            if (listener != null) {
                listener.onDataClick(index);
            }
        }
    };

    private View.OnClickListener onPersonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = recyclerView.getChildLayoutPosition((View) v.getParent());
            if (listener != null) {
                listener.onPersonInfoClick(index);
            }
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int index = recyclerView.getChildLayoutPosition(v);
            if (listener != null) {
                listener.onLongClick(index);
            }
            return true;
        }
    };
}
