package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;
import com.mgtech.maiganapp.viewmodel.SettingFeedbackViewModel;

import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * @author zhaixiang
 * 意见反馈
 */

public class SettingFeedbackActivity extends BaseActivity<SettingFeedbackViewModel> {
    @Bind(R.id.et_suggest)
    EditText etSuggest;
    @Bind({R.id.cb0,R.id.cb1,R.id.cb2,R.id.cb3})
    CheckBox[] checkBoxes;
    private String[] titles;

    public static Intent getCallingIntent(Context context){
        return new Intent(context,SettingFeedbackActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        titles = new String[]{
                "功能异常：功能故障或不可用",
                "产品建议：不满意，我有建议",
                "安全问题：密码隐私等",
                "其它问题"
        };
        etSuggest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.textSize.set(String.format(Locale.getDefault(),"%d/240",s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick(R.id.btn_submit)
    void submit(){
        String title = "";
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()){
                title += titles[i] + " ";
            }
        }
        String content = etSuggest.getText().toString().trim();

    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting_feedback;
    }

}
