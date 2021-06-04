package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgtech.maiganapp.R;


/**
 * 设置手环显示页面
 */

public class SharePopupWindow extends PopupWindow {

    public interface Callback {
        void select(int index);

    }

    public SharePopupWindow(Context context, int[] icons, String[] titles, Callback callback) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_share, null);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        Adapter adapter = new Adapter(icons,titles,callback,recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context,3));

        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //实例化一个ColorDrawable颜色为半透明
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popup_window);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ItemViewHolder>{
        private int[] icons;
        private String[] text;
        private Callback callback;
        private RecyclerView recyclerView;

        public Adapter(int[] icons, String[] text, Callback callback, RecyclerView recyclerView) {
            this.icons = icons;
            this.text = text;
            this.callback = callback;
            this.recyclerView = recyclerView;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popup_window_share_list_item,
                    viewGroup,false);
            view.setOnClickListener(onClickListener);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int i) {
            holder.iv.setImageResource(icons[i]);
            holder.tv.setText(text[i]);
        }

        @Override
        public int getItemCount() {
            return icons.length;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder{
            ImageView iv;
            TextView tv;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                iv = itemView.findViewById(R.id.iv);
                tv = itemView.findViewById(R.id.tv);
            }
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null){
                    callback.select(recyclerView.getChildLayoutPosition(v));
                }
            }
        };
    }
}
