package com.mgtech.maiganapp.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mgtech.maiganapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhaixiang
 * 绑定
 */
public class PairGuideDialogFragment extends AppCompatDialogFragment {
    private static final String TAG = "PairGuideDialogFragment";
    @Bind(R.id.iv_result)
    ImageView ivResult;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_submit)
    ImageView ivSubmit;
    @Bind(R.id.iv_link)
    ImageView ivLink;
    @Bind(R.id.tv_caution)
    TextView tvCaution;
    @Bind(R.id.tv_caution_text)
    TextView tvCautionText;
    @Bind(R.id.layout_success)
    View layoutSuccess;
    @Bind(R.id.constraintLayout2)
    View layoutCaution;
    @Bind(R.id.tv_note)
    TextView tvNote;
    private Callback callback;

    public interface Callback{
        void goToSetReminder();
        void done();
    }

    public static PairGuideDialogFragment getInstance() {
        return new PairGuideDialogFragment();
    }

    public void link() {
        Log.i(TAG, "link: ");
        tvTitle.setText(R.string.bind_please_click_bracelet_button_for_submit);
        ivResult.setVisibility(View.INVISIBLE);
        ivLink.setVisibility(View.GONE);
        Glide.with(this)
                .asGif()
                .load(R.drawable.pair_fragment_submit_animation)
                .into(ivSubmit);
        ivSubmit.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_set)
    void goToSetAlarm(){
        if (callback != null){
            callback.goToSetReminder();
        }
    }

    @OnClick(R.id.tv_done)
    void done(){
        if (callback != null){
            callback.done();
        }
    }

    public void result(boolean isSuccess) {
        if (ivSubmit == null) {
            return;
        }
        ivLink.setVisibility(View.GONE);
        ivSubmit.setVisibility(View.GONE);
//        tvCaution.setVisibility(View.INVISIBLE);
//        tvCautionText.setVisibility(View.INVISIBLE);
        ivResult.setVisibility(View.VISIBLE);
        if (isSuccess) {
            Glide.with(this).asBitmap().load(R.drawable.mine_binding_link_success).into(ivResult);
            tvTitle.setText(R.string.bind_success);
        } else {
            if (TextUtils.equals(tvTitle.getText(), getString(R.string.bind_fail))) {
                return;
            }
            Glide.with(this).asBitmap().load(R.drawable.mine_binding_link_fail).into(ivResult);
            tvTitle.setText(R.string.bind_fail);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setAlarmSuccess(boolean isNew){
        if (ivSubmit == null) {
            return;
        }
        ivLink.setVisibility(View.GONE);
        ivSubmit.setVisibility(View.GONE);
//        tvCaution.setVisibility(View.INVISIBLE);
//        tvCautionText.setVisibility(View.INVISIBLE);
        ivResult.setVisibility(View.VISIBLE);
        Glide.with(this).asBitmap().load(R.drawable.mine_binding_link_success).into(ivResult);
        tvTitle.setText(R.string.bind_success);
        layoutCaution.setVisibility(View.GONE);
        layoutSuccess.setVisibility(View.VISIBLE);
        if (isNew) {
            tvNote.setText(getActivity().getString(R.string.bracelet_pair_set_new_reminder_note));
        }else{
            tvNote.setText(getActivity().getString(R.string.bracelet_pair_set_old_reminder_note));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
        if (context instanceof Callback){
            callback = (Callback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        callback = null;
    }

    @Subscribe
    public void getEvent(Event event) {
        switch (event.code) {
            case Event.LINK:
                link();
                break;
            case Event.SUCCESS:
                result(true);
                break;
            case Event.SET_OLD_ALARM:
                setAlarmSuccess(false);
                break;
            case Event.SET_NEW_ALARM:
                setAlarmSuccess(true);
                break;
            case Event.FAIL:
                result(false);
                break;
            case Event.DISMISS:
                dismiss();
                break;
            default:
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_pair, null);
        ButterKnife.bind(this, view);
        setCancelable(false);
        tvTitle.setText(R.string.bracelet_is_connecting);
//        linkLayout.setRatio(1f);
//        submitLayout.setRatio(1.176f);

        Glide.with(this)
//                .asGif()
                .load(R.drawable.pair_fragment_link_animation)
                .into(ivLink);
//        ivLink.setBackgroundResource(R.drawable.pair_fragment_link_animation_list);
//        AnimationDrawable submitDrawable = (AnimationDrawable) ivLink.getBackground();
//        submitDrawable.start();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return builder.create();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    public static class Event {
        public static final int LINK = 0;
        public static final int SUCCESS = 1;
        public static final int FAIL = 2;
        public static final int DISMISS = 3;
        public static final int SET_OLD_ALARM = 4;
        public static final int SET_NEW_ALARM = 5;
        private int code;

        public Event(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }


//    @Override
//    public void onResume() {
//        try {
//            int width = getResources().getDimensionPixelSize(R.dimen.pair_popup_width);
//            int height = getResources().getDimensionPixelSize(R.dimen.pair_popup_height);
//            Window window = getDialog().getWindow();
//            if (window != null) {
////                window.setBackgroundDrawableResource(R.drawable.pair_bracelet_fragment_guide_bg);
//                window.setLayout(width, height);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.onResume();
//    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

}
