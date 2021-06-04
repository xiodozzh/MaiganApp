package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.EcgModel;
import com.mgtech.maiganapp.viewmodel.MeasureEcgResultViewModel;
import com.mgtech.maiganapp.widget.EcgPartDataGraphView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class MeasureEcgResultActivity extends BaseActivity<MeasureEcgResultViewModel>{
    @Bind(R.id.graphView)
    EcgPartDataGraphView graphView;

    public static Intent getCallingIntent(Context context){
        return new Intent(context,MeasureEcgResultActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        EventBus.getDefault().register(this);
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void getModel(EcgModel model){
        EventBus.getDefault().removeStickyEvent(model);
        viewModel.setModel(model);
        graphView.setData(model.ecgData,model.sampleRate);
    }

    @OnClick(R.id.btn_detail)
    void goToDetail(){
        startActivity(EcgDataDetailActivity.getCallingIntent(this));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_measure_ecg_result;
    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
