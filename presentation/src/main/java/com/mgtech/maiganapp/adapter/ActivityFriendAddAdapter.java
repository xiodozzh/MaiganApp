package com.mgtech.maiganapp.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.FriendAddEditTextModel;
import com.mgtech.maiganapp.data.model.FriendAddItemModel;
import com.mgtech.maiganapp.data.model.IFriendAddModel;
import com.mgtech.maiganapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhaixiang
 */
public class ActivityFriendAddAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<IFriendAddModel> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private static final int TYPE_HEADER_EDIT = IFriendAddModel.TYPE_HEADER_EDIT;
    private static final int TYPE_HEADER_PHONE = IFriendAddModel.TYPE_HEADER_PHONE;
    private static final int TYPE_HEADER_HEADER = IFriendAddModel.TYPE_HEADER_HEADER;
    private static final int TYPE_HEADER_ITEM = IFriendAddModel.TYPE_HEADER_ITEM;
    private int textColorWhite;
    private int textColorGrey;
    private Drawable handleBgRes;
    private Listener listener;
    private DiffCallback diffCallback;

    public interface Listener {
        void selectItem(int index);

        void editTextChange(String text);

        void onPhoneSearch();
    }

    public ActivityFriendAddAdapter(RecyclerView recyclerView, Listener listener) {
        this.recyclerView = recyclerView;
        textColorWhite = Color.WHITE;
        textColorGrey = ContextCompat.getColor(recyclerView.getContext(), R.color.grey_text);
        handleBgRes = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.activity_friend_add_handle_btn);
        diffCallback = new DiffCallback();
        this.listener = listener;
    }

    public void setData(List<IFriendAddModel> data) {
        diffCallback.setList(dataList, data);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback, true);
        dataList.clear();
        dataList.addAll(data);
//        notifyDataSetChanged();
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case TYPE_HEADER_EDIT:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_friend_add_edit, viewGroup, false);
                EditViewHolder editViewHolder = new EditViewHolder(view);
                editViewHolder.et.addTextChangedListener(textWatcher);
                editViewHolder.et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        ViewUtils.hideKeyboard(recyclerView.getContext(),editViewHolder.et);
                    }
                });
                editViewHolder.et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            //执行对应的操作
                            ViewUtils.hideKeyboard(recyclerView.getContext(),editViewHolder.et);
                            return true;
                        }
                        return false;
                    }
                });
                return editViewHolder;
            case TYPE_HEADER_PHONE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_friend_add_edit_text, viewGroup, false);
                view.setOnClickListener(onSearchClickListener);
                return new TextViewHolder(view);
            case TYPE_HEADER_HEADER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_friend_add_edit_list_header, viewGroup, false);
                return new ListHeaderViewHolder(view);
            case TYPE_HEADER_ITEM:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_friend_add_item, viewGroup, false);
                ItemViewHolder itemViewHolder = new ItemViewHolder(view);
                itemViewHolder.tvHandle.setOnClickListener(onItemClickListener);
                return itemViewHolder;
            default:
        }
        throw new RuntimeException("没有该类型" + i);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        IFriendAddModel model = dataList.get(i);
        if (viewHolder instanceof ItemViewHolder && model.type == TYPE_HEADER_ITEM && model instanceof FriendAddItemModel) {
            ItemViewHolder holder = (ItemViewHolder) viewHolder;
            FriendAddItemModel itemModel = (FriendAddItemModel) model;
            holder.tvName.setText(itemModel.contactName);
            holder.tvPhone.setText(String.format(recyclerView.getContext().getString(R.string.friend_add_phone_number)
                    , itemModel.phone));
            String handleText;
            switch (itemModel.relation) {
                case FriendAddItemModel.RELATION_NOT_REGISTER:
                    handleText = recyclerView.getContext().getString(R.string.friend_add_invite);
                    holder.tvHandle.setTextColor(textColorWhite);
                    holder.tvHandle.setBackground(handleBgRes);
                    break;
                case FriendAddItemModel.RELATION_FOLLOWED:
                    handleText = recyclerView.getContext().getString(R.string.friend_add_have_monitored);
                    holder.tvHandle.setTextColor(textColorGrey);
                    holder.tvHandle.setBackgroundColor(textColorWhite);
                    break;
                case FriendAddItemModel.RELATION_NOT_FOLLOW:
                    handleText = recyclerView.getContext().getString(R.string.friend_add_monitor);
                    holder.tvHandle.setTextColor(textColorWhite);
                    holder.tvHandle.setBackground(handleBgRes);
                    break;
                default:
                    handleText = "";
            }
            holder.tvHandle.setText(handleText);
            String url = itemModel.avatarUri;
            if (!TextUtils.isEmpty(url)) {
                ViewUtils.loadImageUsingGlide(recyclerView.getContext(), R.drawable.avatar_default_round, url, holder.ivAvatar, true);
            } else {
                holder.ivAvatar.setImageResource(R.drawable.avatar_default_round);
            }
        }else if (viewHolder instanceof TextViewHolder && model.type == TYPE_HEADER_PHONE && model instanceof FriendAddEditTextModel){
            TextViewHolder textViewHolder = (TextViewHolder) viewHolder;
            FriendAddEditTextModel friendAddEditTextModel = (FriendAddEditTextModel) model;
            textViewHolder.tv.setText(friendAddEditTextModel.editText);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private static class EditViewHolder extends RecyclerView.ViewHolder {
        EditText et;

        EditViewHolder( View itemView) {
            super(itemView);
            et = itemView.findViewById(R.id.et_search);
        }
    }

    private static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        TextViewHolder( View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_search_number);
        }
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {

        ListHeaderViewHolder( View itemView) {
            super(itemView);
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvName;
        TextView tvPhone;
        TextView tvHandle;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvHandle = itemView.findViewById(R.id.tv_handle);
        }
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = recyclerView.getChildLayoutPosition((View) v.getParent());
            if (listener != null) {
                listener.selectItem(index);
            }
        }
    };

    private View.OnClickListener onSearchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onPhoneSearch();
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (listener != null) {
                listener.editTextChange(charSequence.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private class DiffCallback extends DiffUtil.Callback {
        private List<IFriendAddModel> oldList = new ArrayList<>();
        private List<IFriendAddModel> newList = new ArrayList<>();

        public void setList(List<IFriendAddModel> oldList, List<IFriendAddModel> newList) {
            this.newList.clear();
            this.newList.addAll(newList);
            this.oldList.clear();
            this.oldList.addAll(oldList);
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            IFriendAddModel oldModel = oldList.get(oldItemPosition);
            IFriendAddModel newModel = newList.get(newItemPosition);
            if (oldModel.type != newModel.type){
                return false;
            }else if (oldModel.type == IFriendAddModel.TYPE_HEADER_ITEM){
                FriendAddItemModel oldItemModel = (FriendAddItemModel) oldModel;
                FriendAddItemModel newItemModel = (FriendAddItemModel) newModel;
                return Objects.equals(oldItemModel.id, newItemModel.id);
            }else if(oldModel.type == IFriendAddModel.TYPE_HEADER_PHONE){
                return true;
            }else{
                return true;
            }
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            IFriendAddModel oldModel = oldList.get(oldItemPosition);
            IFriendAddModel newModel = newList.get(newItemPosition);
            if (oldModel.type != newModel.type){
                return false;
            }else if (oldModel.type == IFriendAddModel.TYPE_HEADER_ITEM){
                FriendAddItemModel oldItemModel = (FriendAddItemModel) oldModel;
                FriendAddItemModel newItemModel = (FriendAddItemModel) newModel;
                return Objects.equals(oldItemModel, newItemModel);
            }else if (oldModel.type == IFriendAddModel.TYPE_HEADER_PHONE){
                return false;
            }else{
                return true;
            }

        }
    }


}
