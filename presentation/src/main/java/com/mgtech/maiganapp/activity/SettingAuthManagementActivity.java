package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.SettingAuthManagementAdapter;
import com.mgtech.maiganapp.data.model.AuthorizedCompanyModel;
import com.mgtech.maiganapp.fragment.AuthorizedServiceDeleteDialogFragment;
import com.mgtech.maiganapp.viewmodel.SettingAuthManagementViewModel;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Jesse
 */
public class SettingAuthManagementActivity extends BaseActivity<SettingAuthManagementViewModel> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.layout_empty)
    View emptyLayout;
    @Bind(R.id.layout_net_error)
    View errorLayout;
    private SettingAuthManagementAdapter adapter;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SettingAuthManagementActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        adapter = new SettingAuthManagementAdapter(recyclerView, new SettingAuthManagementAdapter.Callback() {
            @Override
            public void onDelete(int index) {
                showDeleteDialog(index);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getAuthorizedService();
            }
        });
        initObs();
    }



    private void initObs() {
        viewModel.networkError.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean error) {
                errorLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
            }
        });
        viewModel.serviceModels.observe(this, new Observer<List<AuthorizedCompanyModel>>() {
            @Override
            public void onChanged(List<AuthorizedCompanyModel> authorizedCompanyModels) {
                errorLayout.setVisibility(View.GONE);
                adapter.setData(authorizedCompanyModels);
                if (authorizedCompanyModels != null && !authorizedCompanyModels.isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                }

            }
        });
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                refreshLayout.setRefreshing(loading);
            }
        });
        viewModel.networkError.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean error) {
                recyclerView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
            }
        });
    }

    private void showDeleteDialog(int index) {
        AuthorizedServiceDeleteDialogFragment fragment = AuthorizedServiceDeleteDialogFragment.getInstance();
        fragment.setCallback(new AuthorizedServiceDeleteDialogFragment.Callback() {
            @Override
            public void submit() {
                viewModel.deleteAuth(index);
            }
        });
        fragment.show(getSupportFragmentManager(), "PrivacyRejectDialogFragment");
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getAuthorizedService();
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting_auth_management;
    }
}
