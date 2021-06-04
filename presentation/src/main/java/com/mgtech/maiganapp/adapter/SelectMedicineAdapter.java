package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.IMedicineModel;

import java.util.List;

/**
 * @author zhaixiang
 * 选择药物
 */

public class SelectMedicineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<IMedicineModel> data;
    private RecyclerView recyclerView;
    private static final int TITLE = IMedicineModel.TYPE_TITLE;
    private static final int ENTITY = IMedicineModel.TYPE_CONTENT;
    private static final int SEARCH = IMedicineModel.TYPE_SEARCH;
    private Listener listener;

    public SelectMedicineAdapter(List<IMedicineModel> data, RecyclerView recyclerView) {
        this.data = data;
        this.recyclerView = recyclerView;
    }

    public interface Listener {
        void onSelect(int position);
        void onDelete(int position);
        void onSearch();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ENTITY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_medicine_name_item, parent, false);
            MedicineViewHolder viewHolder = new MedicineViewHolder(view);
            view.setOnClickListener(selectListener);
            view.setOnLongClickListener(deleteListener);
            return viewHolder;
        } else if (viewType == TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_zone_fragment_item_title,
                    parent, false);
            return new TitleViewHolder(view);
        } else if (viewType == SEARCH) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_zone_fragment_item_search,
                    parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onSearch();
                    }
                }
            });
            return new SearchViewHolder(view);
        }
        throw new RuntimeException("无此类型");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MedicineViewHolder) {
            MedicineViewHolder viewHolder = (MedicineViewHolder) holder;
            IMedicineModel.Content entity = (IMedicineModel.Content) data.get(position);
            viewHolder.tvName.setText(entity.name);
        } else if (holder instanceof TitleViewHolder) {
            TitleViewHolder viewHolder = (TitleViewHolder) holder;
            IMedicineModel.Title title = (IMedicineModel.Title) data.get(position);
            viewHolder.tvTitle.setText(title.value.toUpperCase());
        } else if (holder instanceof SearchViewHolder){
            SearchViewHolder viewHolder = (SearchViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getPosition(String letter) {
        for (int i = 0; i < data.size(); i++) {
            IMedicineModel obj = data.get(i);
            if (obj.getType() == IMedicineModel.TYPE_TITLE) {
                String title = ((IMedicineModel.Title) obj).value;
                if (letter.toUpperCase().charAt(0) <= title.toUpperCase().charAt(0)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }

    private class MedicineViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        MedicineViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        TitleViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder {

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private View.OnClickListener selectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                int index = recyclerView.getChildLayoutPosition(v);
                listener.onSelect(index);
            }
        }
    };

    private View.OnLongClickListener deleteListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                int index = recyclerView.getChildLayoutPosition(v);
                IMedicineModel model = data.get(index);
                if (model instanceof IMedicineModel.Content){
                    IMedicineModel.Content content = (IMedicineModel.Content) model;
                    if (content.custom) {
                        listener.onDelete(index);
                    }
                }
            }
            return true;
        }
    };
}
