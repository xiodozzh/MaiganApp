package com.mgtech.maiganapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.IPersonalInfoModel;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityPersonalInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int AVATAR = 656;
    private static final int ITEM = 566;
    private List<IPersonalInfoModel> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Listener listener;
    private DiffCallback diffCallback;
//    private LazyHeaders headers;

    public ActivityPersonalInfoAdapter(RecyclerView recyclerView, Listener listener) {
        this.recyclerView = recyclerView;
        this.listener = listener;
        this.diffCallback = new DiffCallback();
//        headers = new LazyHeaders.Builder().addHeader("AccessToken",SavePreferenceUtils.getToken(recyclerView.getContext()))
//                .addHeader("Language",Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry())
//                .addHeader("APIVersion",BleConstant.API_VERSION)
//                .addHeader("TimeZone",new SimpleDateFormat("'GMT'ZZZZZ", Locale.getDefault()).format(new Date()))
//                .addHeader("AppInfo","{" +
//                        "\"version\": \"" + Utils.getVersionName(recyclerView.getContext()) + "\"," +
//                        "\"buildVersion\": \"" + Utils.getVersionCode(recyclerView.getContext()) + "\"," +
//                        "\"appConfigKey\": \"mystrace\"," +
//                        "\"type\": " + BleConstant.BRACELET_TYPE +
//                        "}")
//                .addHeader("PhoneInfo", "{" +
//                        "\"platform\": \"android\"," +
//                        "\"systemVersion\": \"" + Build.VERSION.SDK_INT + "\"," +
//                        "\"phoneModel\": \"" + Build.DEVICE + "\"}")
//                .build();
    }

    public interface Listener {
        /**
         * 点击类型
         *
         * @param type 类型
         */
        void onTypeClick(int type);
    }

    public void setDataList(List<IPersonalInfoModel> list) {
        diffCallback.setList(list);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback, false);
        dataList.clear();
        dataList.addAll(list);
        result.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? AVATAR : ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case AVATAR:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.activity_personal_info_recycler_avatar, viewGroup, false);
                view.setOnClickListener(onTypeClick);
                return new AvatarViewHolder(view);
            case ITEM:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.activity_personal_info_recycler_item, viewGroup, false);
                view.setOnClickListener(onTypeClick);
                return new ItemViewHolder(view);
            default:
        }
        throw new RuntimeException("没有该类型");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof AvatarViewHolder) {
            IPersonalInfoModel.Avatar avatarModel = (IPersonalInfoModel.Avatar) dataList.get(0);
            AvatarViewHolder holder = (AvatarViewHolder) viewHolder;
            ViewUtils.loadImageUsingGlide(recyclerView.getContext(),R.drawable.avatar_default,
                    avatarModel.avatarUrl,holder.iv,false);
        } else if (viewHolder instanceof ItemViewHolder) {
            IPersonalInfoModel.Item itemModel = (IPersonalInfoModel.Item) dataList.get(i);
            ItemViewHolder holder = (ItemViewHolder) viewHolder;
            holder.tvValue.setText(itemModel.value);
            holder.tvTitle.setText(itemModel.title);
            holder.ivArrow.setVisibility(itemModel.showArrow?View.VISIBLE:View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private static class AvatarViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;

        AvatarViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_avatar);
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvValue;
        ImageView ivArrow;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvValue = itemView.findViewById(R.id.tv_value);
            ivArrow = itemView.findViewById(R.id.imageView2);
        }
    }

    private class DiffCallback extends DiffUtil.Callback {
        private List<IPersonalInfoModel> list;

        public void setList(List<IPersonalInfoModel> list) {
            this.list = list;
        }

        @Override
        public int getOldListSize() {
            return dataList.size();
        }

        @Override
        public int getNewListSize() {
            return list.size();
        }

        @Override
        public boolean areItemsTheSame(int i, int i1) {
            return dataList.get(i).getType() == list.get(i1).getType();
        }

        @Override
        public boolean areContentsTheSame(int i, int i1) {
            return Objects.equals(dataList.get(i), list.get(i1));
        }
    }

    private View.OnClickListener onTypeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                int index = recyclerView.getChildLayoutPosition(v);
                listener.onTypeClick(dataList.get(index).getType());
            }
        }
    };
}
