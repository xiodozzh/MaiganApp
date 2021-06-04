package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.MedicationPlanAdapter;
import com.mgtech.maiganapp.viewmodel.MedicationPlanHistoryViewModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class MedicationPlanHistoryActivity extends BaseActivity<MedicationPlanHistoryViewModel> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.layout_refresh_empty)
    SwipeRefreshLayout refreshEmptyLayout;
    private MedicationPlanAdapter adapter;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MedicationPlanHistoryActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        initObs();
        adapter = new MedicationPlanAdapter(recyclerView, new MedicationPlanAdapter.Listener() {
            @Override
            public void onClick(int index) {
            }

            @Override
            public void onLongClick(int index) {
                showDeleteDialog(index);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getMedicationPlan();
            }
        });
        refreshEmptyLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getMedicationPlan();
            }
        });
        viewModel.getMedicationPlan();
    }

    /**
     * 弹出是否删除对话框
     * @param index 序号
     */
    private void showDeleteDialog(final int index) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.caution)
                .setMessage(R.string.bracelet_reminder_are_you_sure_to_delete)
                .setNegativeButton(R.string.cancel,null)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.delete(index);
                    }
                }).create();
        dialog.show();
    }


    private void initObs() {
        viewModel.getDataSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.setData(viewModel.dataList);
            }
        });
        viewModel.loading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                refreshLayout.setRefreshing(viewModel.loading.get());
                refreshEmptyLayout.setRefreshing(viewModel.loading.get());
            }
        });
        viewModel.deleteSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.setData(viewModel.dataList);
                showShortToast(getString(R.string.delete_success));
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_medication_plan_history;
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            showShortToast(getString(R.string.set_success));
            viewModel.getMedicationPlan();
        }
    }
}
