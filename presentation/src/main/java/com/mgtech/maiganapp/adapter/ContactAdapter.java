package com.mgtech.maiganapp.adapter;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.domain.entity.net.response.CheckPhoneLoginResponseEntity;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.List;

/**
 * Created by zhaixiang on 2017/1/20.
 * 好友列表 adapter
 */

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CheckPhoneLoginResponseEntity> data;
    private String tvString = "";
    private static final int TYPE_SEARCH = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_ITEM = 2;
    private Callback callback;
    private String searchString;

    public interface Callback {
        void onItemClick(int position);

        void onSearchClick();
    }

    public ContactAdapter(RecyclerView recyclerView, List<CheckPhoneLoginResponseEntity> data) {
        this.data = data;
        this.searchString = recyclerView.getContext().getString(R.string.search);
    }

    /**
     * 设置搜索显示的号码
     *
     * @param phone 数字
     */
    public void setPhone(String phone) {
        tvString = phone;
        notifyDataSetChanged();
    }

    /**
     * 设置点击回调
     *
     * @param callback 回调
     */
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SEARCH) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact_search, parent, false);
            return new SearchViewHolder(view);
        } else if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact_item, parent, false);
            return new ContactViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact_header, parent, false);
            return new HeaderViewHolder(view);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchViewHolder) {
            SearchViewHolder searchViewHolder = (SearchViewHolder) holder;
            if (tvString.isEmpty()) {
                searchViewHolder.cardView.setVisibility(View.GONE);
            } else {
                searchViewHolder.cardView.setVisibility(View.VISIBLE);
                searchViewHolder.tvPhone.setText(searchString + ":" + tvString);
                searchViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback != null) {
                            callback.onSearchClick();
                        }
                    }
                });
            }
        } else if (holder instanceof ContactViewHolder) {
            final int index = position - 2;
            ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
            CheckPhoneLoginResponseEntity entity = data.get(index);
            contactViewHolder.tvName.setText(entity.getDisplayName());
            contactViewHolder.tvPhone.setText(entity.getPhone());
            contactViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onItemClick(index);
                    }
                }
            });
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.layout.setVisibility(data.isEmpty() ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_SEARCH;
        } else if (position == 1) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return data.size() + 2;
    }

    private static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPhone;
        CardView cardView;

        ContactViewHolder(View itemView) {
            super(itemView);
            tvName =  itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            cardView =  itemView.findViewById(R.id.card);
        }
    }

    private static class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView tvPhone;
        CardView cardView;

        SearchViewHolder(View itemView) {
            super(itemView);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            cardView =  itemView.findViewById(R.id.card_search);
            ImageView iv =  itemView.findViewById(R.id.iv_search_in_card);
            ViewUtils.changeViewDrawableColor(itemView.getContext(), iv, R.color.colorPrimary);

        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        View layout;

        HeaderViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout_header);
        }
    }

}
