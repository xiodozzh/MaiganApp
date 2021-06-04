package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.FriendInformationViewModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class FriendInformationActivity extends BaseActivity<FriendInformationViewModel> {
    private static final String TARGET_ID = "targetId";
    @Bind(R.id.sw_read_his_data)
    Switch swReadHisData;
    @Bind(R.id.sw_get_his_push_data)
    Switch swGetHisPushData;
    @Bind(R.id.sw_allow_him_read)
    Switch swAllowHimRead;
    @Bind(R.id.sw_allow_push_to_him)
    Switch swAllowPushToHim;

    @Bind(R.id.tv_get_his_push_data)
    TextView tvGetHisPush;
    @Bind(R.id.tv_read_his_data)
    TextView tvReadHisData;
    @Bind(R.id.tv_allow_push_to_him)
    TextView tvAllowPushToHim;
    @Bind(R.id.tv_allow_him_read)
    TextView tvAllowHimRead;
    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;

    public static Intent getCallingIntent(Context context, String targetId) {
        Intent intent = new Intent(context, FriendInformationActivity.class);
        intent.putExtra(TARGET_ID, targetId);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        swGetHisPushData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != viewModel.getHisPushData) {
                    Log.i(TAG, "onCheckedChanged:swGetHisPushData ");
                    viewModel.getHisPushData = (isChecked);
                    viewModel.update();
                }
            }
        });
        swReadHisData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != viewModel.readHisData) {
                    viewModel.readHisData = (isChecked);
                    viewModel.update();
                }
            }
        });

        swAllowPushToHim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != viewModel.allowPushToHim) {
                    Log.i(TAG, "onCheckedChanged: swAllowPushToHim");
                    viewModel.allowPushToHim = (isChecked);
                    viewModel.update();
                }
            }
        });
        swAllowHimRead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != viewModel.allowHimRead) {
                    viewModel.allowHimRead = (isChecked);
                    viewModel.update();
                }
            }
        });
        initObs();
        Intent intent = getIntent();
        String targetId = intent.getStringExtra(TARGET_ID);
        viewModel.getInfo(targetId);
    }

    private void initObs() {
        viewModel.renderData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Log.i(TAG, "onPropertyChanged: "+ viewModel.readHisData + viewModel.getHisPushData);
                swReadHisData.setChecked(viewModel.readHisData);
                swGetHisPushData.setChecked(viewModel.getHisPushData);
                swAllowHimRead.setChecked(viewModel.allowHimRead);
                swAllowPushToHim.setChecked(viewModel.allowPushToHim);
                ViewUtils.loadImageUsingGlide(FriendInformationActivity.this, R.drawable.avatar_default_round, viewModel.avatarUrl,
                        ivAvatar, true);
            }
        });
        viewModel.change.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setResult(RESULT_OK);
            }
        });

    }


    @OnClick(R.id.layout_set_note_name)
    void setNoteName() {
        ViewUtils.showEditDialog(this, getString(R.string.friend_info_set_note_name),
                viewModel.noteName.get(), getString(R.string.friend_info_note_name), "",
                getString(R.string.friend_info_input_note_name), InputType.TYPE_CLASS_TEXT, new ViewUtils.EditCallback() {
                    @Override
                    public void onEdit(String result) {
                        viewModel.noteName.set(result);
                        viewModel.update();
                    }
                });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_friend_information;
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }
}
