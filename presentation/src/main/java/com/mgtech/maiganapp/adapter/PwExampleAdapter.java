package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.PwExampleItem;

import java.util.List;

/**
 * @author zhaixiang
 */
public class PwExampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PwExampleItem> data;
    private RecyclerView recyclerView;
    private Callback callback;

    public PwExampleAdapter(RecyclerView recyclerView,List<PwExampleItem> data,Callback callback) {
        this.data = data;
        this.recyclerView = recyclerView;
        this.callback = callback;
    }

    public interface Callback{
        void onButtonClick(int index);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pw_example_content, parent, false);
        holder = new PwContentViewHolder(view);
        ((PwContentViewHolder) holder).btn.setOnClickListener(onClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PwExampleItem item = data.get(position);
        if (holder instanceof PwContentViewHolder){
            PwContentViewHolder pwContentViewHolder = (PwContentViewHolder) holder;
            pwContentViewHolder.iv.setImageResource(item.getMainPicResource());
            pwContentViewHolder.tvTitle.setText(item.getText());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private static class PwContentViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        View btn;
        TextView tvTitle;

        PwContentViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            btn = itemView.findViewById(R.id.btn_movie);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (callback != null) {
                int position = recyclerView.getChildLayoutPosition((View) v.getParent().getParent());
                callback.onButtonClick(position);
            }
        }
    };
}
