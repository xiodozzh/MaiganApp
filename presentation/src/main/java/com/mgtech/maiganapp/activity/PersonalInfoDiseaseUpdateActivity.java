package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.ActivityPersonalInfoInitAdapter;
import com.mgtech.maiganapp.viewmodel.PersonalInfoDiseaseViewModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class PersonalInfoDiseaseUpdateActivity extends BaseActivity<PersonalInfoDiseaseViewModel> {
    @Bind(R.id.recyclerView_bp)
    RecyclerView recyclerViewBp;
    @Bind(R.id.recyclerView_disease)
    RecyclerView recyclerViewDisease;
    @Bind(R.id.recyclerView_organ)
    RecyclerView recyclerOrgan;
    @Bind(R.id.recyclerView_other)
    RecyclerView recyclerOther;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.layout_net_error)
    View layoutError;
    @Bind(R.id.layout_data)
    View layoutData;
    private ActivityPersonalInfoInitAdapter bpAdapter;
    private ActivityPersonalInfoInitAdapter diseaseAdapter;
    private ActivityPersonalInfoInitAdapter organAdapter;
    private ActivityPersonalInfoInitAdapter otherAdapter;
    private AlertDialog backDialog;

    public static Intent getCallingIntent(Context context){
        return new Intent(context,PersonalInfoDiseaseUpdateActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        bpAdapter = new ActivityPersonalInfoInitAdapter(recyclerViewBp, viewModel.highBloodPressureList, viewModel.highBpType);
        diseaseAdapter = new ActivityPersonalInfoInitAdapter(recyclerViewDisease, viewModel.diseaseHistoryList, viewModel.diseaseType);
        organAdapter = new ActivityPersonalInfoInitAdapter(recyclerOrgan, viewModel.organDamageList, viewModel.organType);
        otherAdapter = new ActivityPersonalInfoInitAdapter(recyclerOther, viewModel.otherRiskFactorsList, viewModel.otherType);

        recyclerViewBp.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewBp.setAdapter(bpAdapter);

        recyclerViewDisease.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewDisease.setAdapter(diseaseAdapter);

        recyclerOrgan.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerOrgan.setAdapter(organAdapter);

        recyclerOther.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerOther.setAdapter(otherAdapter);
        initObs();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getInfo();
            }
        });
        viewModel.getInfo();
    }

    private void initObs(){
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                refreshLayout.setRefreshing(loading);
            }
        });
        viewModel.loadSuccess.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success) {
                    layoutError.setVisibility(View.GONE);
                    layoutData.setVisibility(View.VISIBLE);
                    bpAdapter.notifyDataSetChanged();
                    diseaseAdapter.notifyDataSetChanged();
                    organAdapter.notifyDataSetChanged();
                    otherAdapter.notifyDataSetChanged();
                }else{
                    layoutError.setVisibility(View.VISIBLE);
                    layoutData.setVisibility(View.GONE);
                }
            }
        });
        viewModel.saveSuccess.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                showShortToast(getString(R.string.set_success));
                finish();
            }
        });
    }

    @OnClick(R.id.btn_back)
    void back(){
        Log.i(TAG, "back: "+ viewModel.isDirtyData());
        if (viewModel.isDirtyData()){
            showBackDiaolog();
        }else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void showBackDiaolog(){
        if (backDialog != null && backDialog.isShowing()){
            return;
        }
        backDialog = new AlertDialog.Builder(this).setMessage("您的修改尚未保存，是否保存?")
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        commit();
                    }
                }).create();
        backDialog.show();
    }

    @OnClick(R.id.btn_commit)
    void commit(){
        viewModel.savePersonalInfo();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_personal_info_disease_update;
    }
}
