package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.PhoneBindViewModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class PhoneBindActivity extends BaseActivity<PhoneBindViewModel>{
    @Bind(R.id.tv_zone)
    TextView tvZone;
    @Bind(R.id.tv_country)
    TextView tvCountry;

    public static Intent getCallingIntent(Context context){
        return new Intent(context,PhoneBindActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_phone_bind;
    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }

    @OnClick(R.id.btn_bind)
    void bind(){
        Toast.makeText(this, "绑定成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnClick({R.id.tv_zone,R.id.layout_zone})
    void selectZone(){
        startActivityForResult(SelectCountryCodeActivity.getCallingIntent(this),SelectCountryCodeActivity.REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectCountryCodeActivity.REQUEST && resultCode == RESULT_OK){
            String s = "+" + data.getStringExtra(SelectCountryCodeActivity.CODE);
            tvZone.setText(s);
            tvCountry.setText(data.getStringExtra(SelectCountryCodeActivity.NAME));
        }
    }
}
