package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.HealthKnowledgeAdapter;
import com.mgtech.maiganapp.data.model.HealthKnowledgeModel;
import com.mgtech.maiganapp.viewmodel.KnowledgeListViewModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class HealthKnowledgeListActivity extends BaseActivity<KnowledgeListViewModel> {
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.layout_refresh_empty)
    SwipeRefreshLayout refreshEmptyLayout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private HealthKnowledgeAdapter adapter;

    public static Intent getCallingIntent(Context context){
        return new Intent(context,HealthKnowledgeListActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        initObs();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getKnowledgeList();
            }
        });
        refreshEmptyLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getKnowledgeList();
            }
        });
        adapter = new HealthKnowledgeAdapter(recyclerView, new HealthKnowledgeAdapter.Listener() {
            @Override
            public void onClick(int index) {
                HealthKnowledgeModel model = viewModel.data.get(index);
                String url = model.url;
                String id = model.knowledgeId;
                startActivity(HealthKnowledgeActivity.getCallingIntent(HealthKnowledgeListActivity.this,url,id));
                model.isRead = true;
                adapter.notifyItemChanged(index);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel.getKnowledgeList();
    }

    private void initObs(){
        viewModel.loading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                refreshLayout.setRefreshing(viewModel.loading.get());
                refreshEmptyLayout.setRefreshing(viewModel.loading.get());
            }
        });
        viewModel.loadSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.setData(viewModel.data);
            }
        });
    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_knowledge_list;
    }
}
