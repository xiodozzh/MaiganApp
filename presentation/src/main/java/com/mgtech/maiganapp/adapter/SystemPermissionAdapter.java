package com.mgtech.maiganapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.SystemPermissionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesse
 */
public class SystemPermissionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SystemPermissionModel> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private int textColorOpen;
    private int textColorClose;
    private String textOpen;
    private String textClose;
    private Listener listener;

    public interface Listener {
        void selectType(SystemPermissionModel model);

        void checkedCustomer(boolean checked);
    }

    public SystemPermissionAdapter(RecyclerView recyclerView, Listener listener) {
        this.recyclerView = recyclerView;
        this.listener = listener;
        textColorOpen = ContextCompat.getColor(recyclerView.getContext(), R.color.light_grey_text);
        textColorClose = ContextCompat.getColor(recyclerView.getContext(), R.color.colorPrimary);
        textOpen = recyclerView.getContext().getString(R.string.system_permission_on);
        textClose = recyclerView.getContext().getString(R.string.system_permission_off);
    }

    public void setData(List<SystemPermissionModel> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case SystemPermissionModel.HEADER:
                return new HeaderViewHolder(LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.layout_system_permission_header, parent, false));
            case SystemPermissionModel.ITEM:
                view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.layout_system_permission_item, parent, false);
                SystemViewHolder systemViewHolder =  new SystemViewHolder(view);
                systemViewHolder.tvOpen.setOnClickListener(onClickListener);
                return systemViewHolder;
            case SystemPermissionModel.CUSTOM:
                view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.layout_system_permission_customer_service, parent, false);
                CustomerServiceViewHolder customerServiceViewHolder = new CustomerServiceViewHolder(view);
                customerServiceViewHolder.sw.setOnCheckedChangeListener(customerCheckedListener);
                return customerServiceViewHolder;
            default:
        }
        throw new RuntimeException("没有此类型");
    }


    private CompoundButton.OnCheckedChangeListener customerCheckedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            listener.checkedCustomer(b);
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (listener != null) {
                int index = recyclerView.getChildAdapterPosition((View) view.getParent());
                listener.selectType(data.get(index));
            }
        }
    };

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SystemPermissionModel model = data.get(position);
        if (holder instanceof HeaderViewHolder && model.getType() == SystemPermissionModel.HEADER) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.tv.setText(model.getTitle());
        } else if (holder instanceof SystemViewHolder && model.getType() == SystemPermissionModel.ITEM) {
            SystemViewHolder systemViewHolder = (SystemViewHolder) holder;
            systemViewHolder.tvTitle.setText(model.getTitle());
            systemViewHolder.tvContent.setText(model.getSubtitle());
            systemViewHolder.tvOpen.setTextColor(model.isOpen() ? textColorOpen : textColorClose);
            systemViewHolder.tvOpen.setText(model.isOpen() ? textOpen : textClose);
        } else if (holder instanceof CustomerServiceViewHolder && model.getType() == SystemPermissionModel.CUSTOM) {
            CustomerServiceViewHolder customerServiceViewHolder = (CustomerServiceViewHolder) holder;
            customerServiceViewHolder.tvTitle.setText(model.getTitle());
            customerServiceViewHolder.tvContent.setText(model.getSubtitle());
            customerServiceViewHolder.sw.setChecked(model.isOpen());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }

    public static class SystemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        TextView tvOpen;

        SystemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvOpen = itemView.findViewById(R.id.tv_open);
        }
    }

    public static class CustomerServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        Switch sw;

        CustomerServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            sw = itemView.findViewById(R.id.sw);
        }
    }
}
