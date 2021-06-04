package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.LocationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhaixiang
 */
public class ActivitySetLocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private DiffCallback diffCallback;
    private SelectListener listener;
    private static final int TITLE = 229;
    private static final int ITEM = 441;

    public ActivitySetLocationAdapter(RecyclerView recyclerView,SelectListener listener) {
        this.recyclerView = recyclerView;
        this.diffCallback = new DiffCallback();
        this.listener = listener;
    }

    public interface SelectListener{
        void onItemSelect(int index);
    }

    public void setData(List<Object> list){
        diffCallback.setNewList(list);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback,false);
        this.list.clear();
        this.list.addAll(list);
        result.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof String){
            return TITLE;
        }else if (list.get(position) instanceof LocationModel){
            return ITEM;
        }
        return TITLE;
    }

    public int getPosition(String letter) {
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            if (obj instanceof String) {
                String title = (String) obj;
                if (letter.toUpperCase().charAt(0) <= title.toUpperCase().charAt(0)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i){
            case ITEM:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_set_country_item,viewGroup,false);
                view.setOnClickListener(onClickListener);
                return new LocationViewHolder(view);
            case TITLE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_set_country_title,viewGroup,false);
                return new TitleViewHolder(view);
                default:
        }
        throw new RuntimeException("无此类型");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof LocationViewHolder){
            LocationViewHolder holder = (LocationViewHolder) viewHolder;
            LocationModel model = (LocationModel) list.get(i);
            holder.bindTo(model);
        }else if (viewHolder instanceof TitleViewHolder){
            TitleViewHolder holder = (TitleViewHolder) viewHolder;
            String title = (String) list.get(i);
            holder.tv.setText(title);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private static class LocationViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;

         LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_country_name);
        }

        void bindTo(LocationModel model){
             tvName.setText(model.name);
        }
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder{
        TextView tv;

        TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_title);
        }
    }


    private class DiffCallback extends DiffUtil.Callback {
        private List<Object> newList;

         void setNewList(List<Object> newList) {
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
            return Objects.equals(list.get(i),newList.get(i1));
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return Objects.equals(list.get(i),newList.get(i1));
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemSelect(recyclerView.getChildLayoutPosition(v));
            }
        }
    };
}
