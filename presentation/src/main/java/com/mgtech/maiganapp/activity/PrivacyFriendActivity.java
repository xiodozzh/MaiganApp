package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.lifecycle.Observer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.SearchPermissionsModel;
import com.mgtech.maiganapp.viewmodel.PrivacyFriendViewModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Jesse
 */
public class PrivacyFriendActivity extends BaseActivity<PrivacyFriendViewModel> {
    @Bind(R.id.layout_find)
    View findView;
    @Bind(R.id.sw_search)
    Switch swSearch;
    @Bind(R.id.error)
    View errorView;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, PrivacyFriendActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        swSearch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (viewModel.currentForbidFindByPhone == b) {
                viewModel.setPermission(!b);
            }
        });
        viewModel.error.observe(this, isError -> {
            errorView.setVisibility(isError ? View.VISIBLE : View.GONE);
            findView.setVisibility(isError ? View.GONE : View.VISIBLE);
        });
        viewModel.model.observe(this, model -> {
            if (model.forbidFindByPhone == swSearch.isChecked()) {
                swSearch.setChecked(!model.forbidFindByPhone);
            }
        });
        viewModel.loading.observe(this, loading -> refreshLayout.setRefreshing(loading));
        refreshLayout.setOnRefreshListener(() -> viewModel.getPermission());
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getPermission();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_privacy_friend;
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }


}
