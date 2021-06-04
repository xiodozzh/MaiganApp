package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.PwExampleAdapter;
import com.mgtech.maiganapp.viewmodel.WearGuideRecognizePwViewModel;
import com.mgtech.maiganapp.widget.DynamicGraphView;
import com.mgtech.maiganapp.widget.WrapContentLinearLayoutManager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class WearGuideRecognizePwActivity extends BleActivity<WearGuideRecognizePwViewModel> {
    @Bind(R.id.graph)
    DynamicGraphView dynamicGraphView;
    @Bind(R.id.recyclerView_pw)
    RecyclerView recyclerView;
    private PwExampleAdapter adapter;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, WearGuideRecognizePwActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        hideActionbar();
        viewModel.complete.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                finish();
            }
        });
        viewModel.sampleData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                short[] data = viewModel.sampleData.get();
                if (data == null) {
                    return;
                }
                for (short i : data) {
                    dynamicGraphView.appendWithoutDraw(i);
                }
            }
        });
        viewModel.showErrorText.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.showErrorText.get()) {
                    dynamicGraphView.reset();
                }
            }
        });
        initList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.checkLinkState();
    }


    @Override
    protected void openBleFail() {
        showOpenBleFailDialog();
    }

    @Override
    protected void openBleSuccess() {
        Toast.makeText(this, R.string.ble_has_open, Toast.LENGTH_SHORT).show();
        viewModel.linkIfAllowed();
    }

    private void initList() {
        adapter = new PwExampleAdapter(recyclerView, viewModel.getItems(), new PwExampleAdapter.Callback() {
            @Override
            public void onButtonClick(int index) {
                Intent intent = WearGuideWearExampleMovieActivity.getCallingIntent(WearGuideRecognizePwActivity.this,index);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
    }

    @OnClick(R.id.btn_back)
    void back() {
        leave();
    }

    @OnClick(R.id.btn_retry)
    void retryMeasure() {
        viewModel.startMeasure();
    }

    @Override
    public void onBackPressed() {
        leave();
    }

    private void leave() {
        Log.i(TAG, "leave: " + viewModel.isWorking.get());
        if (viewModel.isWorking.get()) {
            viewModel.leave();
        } else {
            finish();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_wear_guide_recognize_pw;
    }
}
