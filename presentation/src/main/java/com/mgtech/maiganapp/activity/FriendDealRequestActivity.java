package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.ActivityFriendDealRequestAdapter;
import com.mgtech.maiganapp.data.model.FriendModel;
import com.mgtech.maiganapp.data.model.FriendRequestModel;
import com.mgtech.maiganapp.viewmodel.FriendDealRequestViewModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class FriendDealRequestActivity extends BaseActivity<FriendDealRequestViewModel> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.layout_refresh_empty)
    SwipeRefreshLayout refreshEmptyLayout;

    private ActivityFriendDealRequestAdapter adapter;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, FriendDealRequestActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        adapter = new ActivityFriendDealRequestAdapter(recyclerView, new ActivityFriendDealRequestAdapter.Listener() {
            @Override
            public void onAccept(int index) {
                viewModel.dealRequest(index,true);
            }

            @Override
            public void onDeny(int index) {
                viewModel.dealRequest(index, false);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getFriendRequest();
            }
        });
        refreshEmptyLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getFriendRequest();
            }
        });
        initObs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getFriendRequest();
    }

    private void initObs() {
        viewModel.getFriendRequestSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.setData(viewModel.list);
            }
        });
        viewModel.getFriendLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                refreshLayout.setRefreshing(viewModel.getFriendLoading.get());
                refreshEmptyLayout.setRefreshing(viewModel.getFriendLoading.get());
            }
        });
        viewModel.acceptRequestSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setResult(RESULT_OK);
                adapter.notifyItemChanged(viewModel.dealIndex);
                FriendRequestModel model = viewModel.list.get(viewModel.dealIndex);
                startActivity(FriendInformationActivity.getCallingIntent(FriendDealRequestActivity.this,
                        model.targetUserId));
            }
        });
        viewModel.denyRequestSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.notifyItemChanged(viewModel.dealIndex);
            }
        });

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_friend_deal_request;
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }
}
