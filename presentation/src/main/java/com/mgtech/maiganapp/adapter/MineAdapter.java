package com.mgtech.maiganapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.IMineModel;
import com.mgtech.maiganapp.data.model.MineCardModel;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.widget.UnreadImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class MineAdapter extends RecyclerView.Adapter<MineAdapter.CardViewHolder> {
    private List<MineCardModel> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Listener listener;

    public interface Listener{
        void onTypeClick(int type);
    }

    public MineAdapter(RecyclerView recyclerView, Listener listener) {
        this.recyclerView = recyclerView;
        this.listener = listener;
    }

    public void setDataList(List<MineCardModel> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.layout_mine_card_item, viewGroup, false);
        view.setOnClickListener(onClickListener);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MineAdapter.CardViewHolder viewHolder, int i) {
        MineCardModel model = dataList.get(i);
        viewHolder.tv.setText(model.title);
        viewHolder.iv.setImageResource(model.imgSrc);
        viewHolder.iv.setUnread(!model.isRead);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


     static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
         UnreadImageView iv;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            iv = itemView.findViewById(R.id.iv);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null){
                int index = recyclerView.getChildLayoutPosition(v);
                listener.onTypeClick(dataList.get(index).type);
            }
        }
    };
}
