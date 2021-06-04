package com.mgtech.maiganapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.AuthorizedCompanyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jesse
 */
public class SettingAuthManagementAdapter extends RecyclerView.Adapter<SettingAuthManagementAdapter.AuthViewHolder> {
    private List<AuthorizedCompanyModel> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private Callback callback;

    public SettingAuthManagementAdapter(RecyclerView recyclerView, Callback callback) {
        this.recyclerView = recyclerView;
        this.callback = callback;
    }

    public void setData(List<AuthorizedCompanyModel> data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public interface Callback{
        void onDelete(int index);
    }

    @NonNull
    @Override
    public AuthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.layout_setting_auth_management_item,parent,false);
        AuthViewHolder holder = new AuthViewHolder(view);
        holder.btnDelete.setOnClickListener(onDeleteListener);
        return holder;
    }

    private View.OnClickListener onDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (callback != null){
                callback.onDelete(recyclerView.getChildAdapterPosition((View) view.getParent()));
            }
        }
    } ;

    @Override
    public void onBindViewHolder(@NonNull AuthViewHolder holder, int position) {
        AuthorizedCompanyModel model = data.get(position);
        holder.tvName.setText(model.title);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class AuthViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        View btnDelete;

         AuthViewHolder(@NonNull View itemView) {
            super(itemView);
             btnDelete = itemView.findViewById(R.id.btn_delete);
             tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
