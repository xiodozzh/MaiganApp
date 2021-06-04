package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;

import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class WearGuideActivity extends BaseActivity<DefaultViewModel> {

    public static Intent getCallingIntent(Context context){
        return new Intent(context,WearGuideActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
    }

    @OnClick(R.id.layout_go_to_find)
    void goToFind(){
        startActivity(WearGuideWearMethodActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_go_to_recognize)
    void goToRecognize(){
        startActivity(WearGuideRecognizePwActivity.getCallingIntent(this));
    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_wear_guide;
    }
}
