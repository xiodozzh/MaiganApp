package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.MeasurePwResultRemarkViewModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Jesse
 */
public class MeasurePwResultRemarkActivity extends BaseActivity<MeasurePwResultRemarkViewModel> {
    private static final String ID = "id";
    private static final String DEFAULT_TEXT = "defaultText";
    @Bind(R.id.et)
    EditText et;
    @Bind(R.id.tv_number)
    TextView tvNumber;

    public static Intent getCallingIntent(Context context, String resultId, String defaultValue) {
        Intent intent = new Intent(context, MeasurePwResultRemarkActivity.class);
        intent.putExtra(ID, resultId);
        intent.putExtra(DEFAULT_TEXT, defaultValue);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        viewModel.remark.observe(this, s -> {
            Intent intent = new Intent();
            intent.putExtra(MeasurePwResultActivity.REMARK,s);
            setResult(RESULT_OK,intent);
            finish();
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int numberLeft = viewModel.getMaxNumber() - charSequence.length();
                if (numberLeft < 0) {
                    numberLeft = 0;
                }
                tvNumber.setText(String.valueOf(numberLeft));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        String value = getIntent().getStringExtra(DEFAULT_TEXT);
        if (TextUtils.isEmpty(value)) {
            value = "";
        }
        et.setText(value);
        int numberLeft = viewModel.getMaxNumber() - value.length();
        if (numberLeft < 0) {
            numberLeft = 0;
        }
        tvNumber.setText(String.valueOf(numberLeft));
    }

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.btn_submit)
    public void commit() {
        viewModel.setRemark(getIntent().getStringExtra(ID), et.getText().toString());
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_measure_pw_result_remark;
    }
}
