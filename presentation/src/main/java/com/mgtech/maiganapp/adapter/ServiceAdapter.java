//package com.mgtech.maiganapp.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.mgtech.maiganapp.R;
//import com.mgtech.maiganapp.data.model.ServiceModel;
//import com.mgtech.maiganapp.utils.ViewUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
//    private List<ServiceModel> data;
//    private RecyclerView recyclerView;
//    private Callback callback;
//
//    public interface Callback{
//        void onClick(int pos);
//    }
//
//    public ServiceAdapter(RecyclerView recyclerView,Callback callback) {
//        this.recyclerView = recyclerView;
//        this.callback = callback;
//        this.data = new ArrayList<>();
//    }
//
//    public void setData(List<ServiceModel> models){
//        this.data.clear();
//        this.data.addAll(models);
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_service_item,parent,false);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callback.onClick(recyclerView.getChildAdapterPosition(v));
//            }
//        });
//        return new ServiceViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
//        ServiceModel model = data.get(position);
//        holder.tvTitle.setText(model.name);
//        holder.tvContent.setText(model.remark);
//        holder.tvServiceBought.setVisibility(model.haveBought ? View.VISIBLE : View.GONE);
//        ViewUtils.loadImageUsingGlide(recyclerView.getContext(),model.iconUrl,holder.iv,false);
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.size();
//    }
//
//     static class ServiceViewHolder extends RecyclerView.ViewHolder {
//        TextView tvTitle;
//        TextView tvContent;
//        TextView tvServiceBought;
//        ImageView iv;
//
//         ServiceViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvTitle = itemView.findViewById(R.id.tv_title);
//            tvContent = itemView.findViewById(R.id.tv_content);
//            iv = itemView.findViewById(R.id.iv);
//            tvServiceBought = itemView.findViewById(R.id.tv_has_bought);
//        }
//    }
//}
