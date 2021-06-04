package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.DiseaseModel;

import java.util.List;

/**
 * @author zhaixiang
 */
public class ActivityPersonalInfoInitAdapter extends RecyclerView.Adapter<ActivityPersonalInfoInitAdapter.ItemViewHolder> {

    private List<DiseaseModel> diseaseList;
    private List<DiseaseModel> selected;
    private RecyclerView recyclerView;

    public ActivityPersonalInfoInitAdapter(RecyclerView recyclerView, List<DiseaseModel> diseaseList, List<DiseaseModel> selected) {
        this.recyclerView = recyclerView;
        this.diseaseList = diseaseList;
        this.selected = selected;
    }

    @NonNull
    @Override
    public ActivityPersonalInfoInitAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_personal_info_init_recycler_item,viewGroup,false);
        ItemViewHolder holder = new ItemViewHolder(view);
        holder.tv.setOnClickListener(listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int i) {
        DiseaseModel model = diseaseList.get(i);
        viewHolder.tv.setText(model.name);
        viewHolder.tv.setSelected(selected.contains(model));
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tv;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index =recyclerView.getChildLayoutPosition((View) v.getParent());
            DiseaseModel model = diseaseList.get(index);
            boolean have = selected.contains(model);
            if (have){
                selected.remove(model);
            }else{
                selected.add(model);
            }
            TextView tv = (TextView) v;
            tv.setSelected(!have);
        }
    };
}
