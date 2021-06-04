package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.widget.EcgPartDataGraphView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
/**
 * @author zhaixiang
 */
public class EcgDetailAdapter extends RecyclerView.Adapter<EcgDetailAdapter.GraphViewHolder>{
    private List<float[]> data = new ArrayList<>();
    private float sampleRate;
    private boolean reverse;
    private boolean isVertical;

    public void setData(List<float[]> data, float sampleRate, boolean isVertical) {
        this.data = data;
        this.sampleRate = sampleRate;
        this.isVertical = isVertical;
        notifyDataSetChanged();
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GraphViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int res = isVertical ? R.layout.activity_ecg_data_detail_item:R.layout.activity_ecg_data_detail_item_horizontal;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(res,viewGroup,false);
        return new GraphViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GraphViewHolder viewHolder, int i) {
        float[] partData = data.get(i);
        viewHolder.graphView.setDataAndReverse(partData,sampleRate,reverse);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class GraphViewHolder extends RecyclerView.ViewHolder{
        EcgPartDataGraphView graphView;

        GraphViewHolder(@NonNull View itemView) {
            super(itemView);
            graphView = itemView.findViewById(R.id.graph);
        }
    }
}
