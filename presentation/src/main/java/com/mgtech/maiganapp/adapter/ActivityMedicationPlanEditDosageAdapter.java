package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.MedicationPlanModel;
import com.mgtech.maiganapp.widget.ActivityMedicationPlanEditDosageLayout;

import java.util.List;

/**
 * @author zhaixiang
 */
public class ActivityMedicationPlanEditDosageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TITLE = 0;
    private static final int ITEM = 1;
    private SparseArray<List<MedicationPlanModel.DosageItem>> dataList;
    private RecyclerView recyclerView;
    private String dosageText;
    private Listener listener;
    private String format;
    private DiffUtilCallback diffUtilCallback;

    public ActivityMedicationPlanEditDosageAdapter(RecyclerView recyclerView,Listener listener) {
        this.recyclerView = recyclerView;
        this.dataList = new SparseArray<>();
        this.listener = listener;
        this.format = recyclerView.getContext().getString(R.string.activity_medication_plan_day);
        this.diffUtilCallback = new DiffUtilCallback();
        this.diffUtilCallback.setOldData(dataList);
    }

    public void setData(SparseArray<List<MedicationPlanModel.DosageItem>> newList,boolean notifyNow){
//        diffUtilCallback.setNewData(newList);
//        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtilCallback,false);

        this.dataList.clear();
        int size = newList.size();
        for (int i = 0; i < size; i++) {
            this.dataList.put(i,newList.valueAt(i));
        }
        if (notifyNow) {
            notifyDataSetChanged();
        }
//        result.dispatchUpdatesTo(this);
    }


    public interface Listener{
        /**
         * 需要为第index个list中添加
         * @param day 第index天
         * @param layout 点击的layout
         */
        void add(int day,ActivityMedicationPlanEditDosageLayout layout);

        /**
         * 修改第index天中第itemNumber项的提醒
         * @param day 第day天
         * @param itemIndex 第itemIndex项
         * @param layout 点击的layout
         */
        void update(int day, int itemIndex,ActivityMedicationPlanEditDosageLayout layout);

        /**
         * 修改重复天数
         */
        void setCycleDay();

        /**
         * 删除某天的一项
         * @param day 第几天
         * @param itemIndex 删除的项
         * @param layout 点击的layout
         */
        void remove(int day, int itemIndex,ActivityMedicationPlanEditDosageLayout layout);
    }


    public void setDosage(String text){
        dosageText = text;
        notifyItemChanged(0);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TITLE : ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TITLE){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.activity_medication_plan_edit_dosage_item_title,viewGroup,false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.setCycleDay();
                    }
                }
            });
            return new TitleViewHolder(view);
        }else if (i == ITEM){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.activity_medication_plan_edit_dosage_item_item,viewGroup,false);
            ItemViewHolder holder = new ItemViewHolder(view);
            holder.dosageLayout.setListener(dosageListener);
            return holder;
        }
        throw new RuntimeException("不支持的该种类型");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof TitleViewHolder){
            TitleViewHolder holder = (TitleViewHolder) viewHolder;
            holder.tvDosage.setText(dosageText);
        }else if (viewHolder instanceof ItemViewHolder){
            List<MedicationPlanModel.DosageItem> model = dataList.get(i-1);
            ItemViewHolder holder = (ItemViewHolder) viewHolder;
            holder.dosageLayout.setDataList(model);
            holder.tvTitle.setText(String.format(format,i));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder{
        TextView tvDosage;

        TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDosage = itemView.findViewById(R.id.tv_dosage);
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvDosage;
        TextView tvTitle;
        ActivityMedicationPlanEditDosageLayout dosageLayout;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDosage = itemView.findViewById(R.id.tv_dosage);
            tvTitle = itemView.findViewById(R.id.tv_index);
            dosageLayout = itemView.findViewById(R.id.layout_card);
        }
    }

    private ActivityMedicationPlanEditDosageLayout.Listener dosageListener = new ActivityMedicationPlanEditDosageLayout.Listener() {
        @Override
        public void clickUpdate(ActivityMedicationPlanEditDosageLayout view, int itemIndex, MedicationPlanModel.DosageItem item) {
            if (listener != null){
                int index = recyclerView.getChildLayoutPosition((View) view.getParent())-1;
                listener.update(index,itemIndex, view);
            }
        }

        @Override
        public void clickAdd(ActivityMedicationPlanEditDosageLayout view) {
            if (listener != null){
                int index = recyclerView.getChildLayoutPosition((View) view.getParent())-1;
                listener.add(index,view);
            }
        }

        @Override
        public void clickDelete(ActivityMedicationPlanEditDosageLayout view, int itemIndex) {
            if (listener != null){
                int index = recyclerView.getChildLayoutPosition((View) view.getParent())-1;
                listener.remove(index,itemIndex,view);
            }
        }


    };

    private class DiffUtilCallback extends DiffUtil.Callback{
        private SparseArray<List<MedicationPlanModel.DosageItem>> oldData;
        private SparseArray<List<MedicationPlanModel.DosageItem>> newData;

        void setOldData(SparseArray<List<MedicationPlanModel.DosageItem>> oldData) {
            this.oldData = oldData;
        }

        public void setNewData(SparseArray<List<MedicationPlanModel.DosageItem>> newData) {
            this.newData = newData;
        }

        @Override
        public int getOldListSize() {
            return oldData.size();
        }

        @Override
        public int getNewListSize() {
            return newData.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return false;
//            List<MedicationPlanModel.DosageItem> oldList = oldData.get(i);
//            List<MedicationPlanModel.DosageItem> newList = newData.get(i1);
//            if (oldList == null && newList == null){
//                return true;
//            }else if (oldList == null || newList == null){
//                return false;
//            }else if (oldList.size() != newList.size()){
//                return false;
//            }else{
//                boolean flag = true;
//                for (int j = 0; j < oldList.size(); j++) {
//                    if (!Objects.equals(oldList.get(j),newList.get(j))){
//                        return false;
//                    }
//                }
//            }
//            return true;
        }
    }

}
