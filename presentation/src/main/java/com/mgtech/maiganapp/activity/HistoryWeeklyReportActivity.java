package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.WeeklyReportAdapter;
import com.mgtech.maiganapp.data.model.WeeklyReportModel;
import com.mgtech.maiganapp.viewmodel.HistoryWeeklyReportViewModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class HistoryWeeklyReportActivity extends BaseActivity<HistoryWeeklyReportViewModel> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @Bind(R.id.layout_refresh_empty)
    SwipeRefreshLayout layoutRefreshEmpty;
    @Bind(R.id.layout_refresh_error)
    SwipeRefreshLayout layoutError;

    private WeeklyReportAdapter adapter;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, HistoryWeeklyReportActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        initObs();
        adapter = new WeeklyReportAdapter(recyclerView, new WeeklyReportAdapter.Listener() {
            @Override
            public void check(int index) {
                WeeklyReportModel model = viewModel.models.get(index);
                String url = model.weekReportDetailUrl;
                String text = model.shareContent;
                String title = model.shareTitle;
                String id = model.weekReportGuid;
                model.read = true;
                adapter.notifyItemChanged(index);
                if (!TextUtils.isEmpty(url)) {
                    startActivity(WeeklyReportActivity.getCallingIntent(HistoryWeeklyReportActivity.this, url,text,title,id));
                }
            }
        });
        layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getWeeklyReportList();
            }
        });
        layoutRefreshEmpty.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getWeeklyReportList();
            }
        });
        layoutError.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getWeeklyReportList();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel.getWeeklyReportList();
    }

    private void initObs() {
        viewModel.loading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                layoutRefresh.setRefreshing(viewModel.loading.get());
                layoutRefreshEmpty.setRefreshing(viewModel.loading.get());
                layoutError.setRefreshing(viewModel.loading.get());
            }
        });
        viewModel.reloadModel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.setData(viewModel.models);
            }
        });
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @OnClick(R.id.layout_guide)
    void guide(){
        startActivity(GuideShareWeekReportActivity.getCallingIntent(this));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_history_weekly_report;
    }
}
