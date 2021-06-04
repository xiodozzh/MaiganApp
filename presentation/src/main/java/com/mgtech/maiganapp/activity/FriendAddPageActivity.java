package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.FriendAddEvent;
import com.mgtech.maiganapp.data.model.FriendAddItemModel;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.FriendAddPageViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class FriendAddPageActivity extends BaseActivity<FriendAddPageViewModel>{
    @Bind(R.id.et_message)
    EditText etMessage;
    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;

    public static Intent getCallingIntent(Context context){
        return new Intent(context,FriendAddPageActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        viewModel.sendSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                finish();
            }
        });
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.message = (s.toString().trim());
                viewModel.valid.set(s.toString().trim().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ViewUtils.hideKeyboard(FriendAddPageActivity.this,etMessage);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        etMessage.setText(viewModel.message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(sticky = true)
    public void initFriendModel(FriendAddEvent event){
        EventBus.getDefault().removeStickyEvent(event);
        FriendAddItemModel model = event.getModel();
        if (model != null) {
            viewModel.setModel(event.getModel());
            String url = model.avatarUri;
            if (!TextUtils.isEmpty(url)) {
                ViewUtils.loadImageUsingGlide(this, R.drawable.avatar_default_round_large, url, ivAvatar, true);
            }
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_friend_add_page;
    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }

    @OnClick(R.id.iv_clear)
    void clear(){
        viewModel.message = "";
        etMessage.setText("");
    }

    @OnClick(R.id.btn_send_request)
    void sendRequest(){
        viewModel.sendRequest();
    }
}
