package com.mgtech.maiganapp.adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.blelib.entity.BroadcastData;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 *
 * @author zhaixiang
 * @date 2018/3/2
 * 广播
 */

public class BroadcastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerView recyclerView;
    private List<BroadcastData> dataList = new ArrayList<>();
    private BroadcastListener listener;
    private Bitmap[] signalMaps;
    private String deviceOld;
    private String deviceName;
    private DiffCallback diffCallback ;

    public interface BroadcastListener {
        void onSelect(int position);
    }

    public BroadcastAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.signalMaps = new Bitmap[4];
        Resources resources = recyclerView.getResources();
        int side = resources.getDimensionPixelSize(R.dimen.medium_padding);
        signalMaps[0] = ViewUtils.decodeSampleBitmapFromResource(resources, R.drawable.mine_binding_weakestsignal, side, side);
        signalMaps[1] = ViewUtils.decodeSampleBitmapFromResource(resources, R.drawable.mine_binding_weaksignal, side, side);
        signalMaps[2] = ViewUtils.decodeSampleBitmapFromResource(resources, R.drawable.mine_binding_normalsignal, side, side);
        signalMaps[3] = ViewUtils.decodeSampleBitmapFromResource(resources, R.drawable.mine_binding_strongsignal, side, side);

        deviceOld = recyclerView.getContext().getString(R.string.device_name_old_version);
        deviceName = recyclerView.getContext().getString(R.string.device_name_format);
        diffCallback = new DiffCallback();
    }

    public void setListener(BroadcastListener listener) {
        this.listener = listener;
    }

    public void setDataList(List<BroadcastData> newList){
        diffCallback.setNewList(newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback,false);
        dataList.clear();
        dataList.addAll(newList);
        result.dispatchUpdatesTo(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_broadcast_item, parent, false);
        view.setOnClickListener(clickListener);
        return new BroadcastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BroadcastViewHolder) {
            BroadcastViewHolder broadcastViewHolder = (BroadcastViewHolder) holder;
            BroadcastData data = dataList.get(position);
            int random = data.getRandomNumber();
            int signal = data.getSignalLevel();

            String randomText = random == 0 ?  deviceOld: String.format(Locale.getDefault(), deviceName, getRandomString(random));
            broadcastViewHolder.tvRandom.setText(randomText);
            broadcastViewHolder.ivSignal.setImageBitmap(signalMaps[signal]);
        }
    }

    private String getRandomString(int random) {
        String s = String.valueOf(random);
        int digitNumber = s.length();
        int zeroNumber = 4 - digitNumber;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zeroNumber; i++) {
            sb.append(0);
        }
        sb.append(random);
        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private static class BroadcastViewHolder extends RecyclerView.ViewHolder {
        TextView tvRandom;
        ImageView ivSignal;

        BroadcastViewHolder(View itemView) {
            super(itemView);
            tvRandom = itemView.findViewById(R.id.tv_random);
            ivSignal =  itemView.findViewById(R.id.iv_signal);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = recyclerView.getChildLayoutPosition(v);
            if (listener != null) {
                listener.onSelect(i);
            }
        }
    };

    private class DiffCallback extends DiffUtil.Callback{
        private List<BroadcastData> newList = new ArrayList<>();

        public void setNewList(List<BroadcastData> newList) {
            this.newList.clear();
            this.newList.addAll(newList);
        }

        @Override
        public int getOldListSize() {
            return dataList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return Objects.equals(dataList.get(i).getMacAddress(),newList.get(i1).getMacAddress());
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return Objects.equals(dataList.get(i),newList.get(i1));
        }
    }
}
