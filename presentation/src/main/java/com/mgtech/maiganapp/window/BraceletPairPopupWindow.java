package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mgtech.maiganapp.R;

import butterknife.Bind;

/**
 * 绑定
 */

public class BraceletPairPopupWindow extends PopupWindow {
    private static final String TAG = "BraceletPair";
    private ImageView ivResult;
    private TextView tvTitle;
    private ImageView ivSubmit;
    private ImageView ivLink;
    private  TextView tvCaution;
    private  TextView tvCautionText;
    private  View layoutSuccess;
    private  View layoutCaution;
    private   TextView tvNote;
    private Context context;

    public interface Callback{
        void goToSetReminder();
        void done();
    }

    public BraceletPairPopupWindow(Context context,Callback callback) {
        super(context);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_bracelet_pair, null);
        init(context,view,callback);
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //实例化一个ColorDrawable颜色为半透明
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popup_window);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private void init(final Context context,View view,final Callback callback){
        ivResult = view.findViewById(R.id.iv_result);
        tvTitle = view.findViewById(R.id.tv_title);
        ivSubmit = view.findViewById(R.id.iv_submit);
        ivLink = view.findViewById(R.id.iv_link);
        tvCaution = view.findViewById(R.id.tv_caution);
        tvCautionText = view.findViewById(R.id.tv_caution_text);
        layoutSuccess = view.findViewById(R.id.layout_success);
        layoutCaution = view.findViewById(R.id.layout_caution);
        tvNote = view.findViewById(R.id.tv_note);
        view.findViewById(R.id.tv_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null){
                    callback.goToSetReminder();
                }
            }
        });
        view.findViewById(R.id.tv_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null){
                    callback.done();
                }
            }
        });
        tvTitle.setText(R.string.bracelet_is_connecting);
        Glide.with(context)
                .load(R.drawable.pair_fragment_link_animation)
                .into(ivLink);
    }

    public void link() {
        Log.i(TAG, "link: ");
        tvTitle.setText(R.string.bind_please_click_bracelet_button_for_submit);
        ivResult.setVisibility(View.INVISIBLE);
        ivLink.setVisibility(View.GONE);
        Glide.with(context)
                .asGif()
                .load(R.drawable.pair_fragment_submit_animation)
                .into(ivSubmit);
        ivSubmit.setVisibility(View.VISIBLE);
    }

    public void result(boolean isSuccess) {
        ivLink.setVisibility(View.GONE);
        ivSubmit.setVisibility(View.GONE);
        ivResult.setVisibility(View.VISIBLE);
        if (isSuccess) {
            Glide.with(context).asBitmap().load(R.drawable.mine_binding_link_success).into(ivResult);
            tvTitle.setText(R.string.bind_success);
        } else {
            Glide.with(context).asBitmap().load(R.drawable.mine_binding_link_fail).into(ivResult);
            tvTitle.setText(R.string.bind_fail);
        }
    }

    public void pairSuccess(){
        ivLink.setVisibility(View.GONE);
        ivSubmit.setVisibility(View.GONE);
        ivResult.setVisibility(View.VISIBLE);
        Glide.with(context).asBitmap().load(R.drawable.mine_binding_link_success).into(ivResult);
        tvTitle.setText(R.string.bind_success);
    }

    public void pairFail(){
        ivLink.setVisibility(View.GONE);
        ivSubmit.setVisibility(View.GONE);
        ivResult.setVisibility(View.VISIBLE);
        Glide.with(context).asBitmap().load(R.drawable.mine_binding_link_fail).into(ivResult);
        tvTitle.setText(R.string.bind_fail);
    }

    public void setAlarmSuccess(boolean isNew){
        Log.i(TAG, "setAlarmSuccess: "+ isNew);
        tvTitle.setText("");
        ivLink.setVisibility(View.GONE);
        ivSubmit.setVisibility(View.GONE);
        ivResult.setVisibility(View.VISIBLE);
        Glide.with(context).asBitmap().load(R.drawable.mine_binding_link_success).into(ivResult);
        tvTitle.setText(R.string.bind_success);
        layoutCaution.setVisibility(View.GONE);
        layoutSuccess.setVisibility(View.VISIBLE);
        if (isNew) {
            tvNote.setText(R.string.bracelet_pair_set_new_reminder_note);
        }else{
            tvNote.setText(R.string.bracelet_pair_set_old_reminder_note);
        }
    }
}
