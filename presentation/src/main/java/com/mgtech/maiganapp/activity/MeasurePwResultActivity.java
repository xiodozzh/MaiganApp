package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.MeasurePwResultAdapter;
import com.mgtech.maiganapp.data.model.ExceptionRecordModel;
import com.mgtech.maiganapp.data.model.IIndicatorModel;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.viewmodel.MeasurePwResultViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 * 全部参数展示
 */

public class MeasurePwResultActivity extends BaseActivity<MeasurePwResultViewModel> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.layout_root)
    LinearLayout root;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.error)
    View errorView;
    private static final int REQUEST_REMARK = 564;
    private MeasurePwResultAdapter adapter;
    private static final String USER_IS_MAN = "isMan";
    private static final String USER_ID = "userId";
    public static final String REMARK = "remark";

    public static Intent getCallingIntent(Context context, boolean userIsMan, String userId) {
        Intent intent = new Intent(context, MeasurePwResultActivity.class);
        intent.putExtra(USER_IS_MAN, userIsMan);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        initRecyclerView();
        viewModel.itemListLiveData.observe(this, new Observer<List<IIndicatorModel>>() {
            @Override
            public void onChanged(List<IIndicatorModel> iIndicatorModels) {
                adapter.setList(iIndicatorModels);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getData();
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
                errorView.setVisibility(error ? View.VISIBLE : View.GONE);
                recyclerView.setVisibility(error ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getIndicator(IndicatorDataModel model) {
        EventBus.getDefault().removeStickyEvent(model);
        viewModel.setIndicatorId(model.id, getIntent().getStringExtra(USER_ID), getIntent().getBooleanExtra(USER_IS_MAN, true));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getException(ExceptionRecordModel model) {
        EventBus.getDefault().removeStickyEvent(model);
        viewModel.setIndicatorId(model.indicator.id, getIntent().getStringExtra(USER_ID), getIntent().getBooleanExtra(USER_IS_MAN, true));
        viewModel.setExceptionRead(model.noticeId, model.targetId);
    }

    private void setMark() {
        startActivityForResult(MeasurePwResultRemarkActivity.getCallingIntent(this, viewModel.model.id, viewModel.model.remark), REQUEST_REMARK);
    }

    /**
     * 初始化list
     */
    private void initRecyclerView() {
        adapter = new MeasurePwResultAdapter(recyclerView, new MeasurePwResultAdapter.Callback() {
            @Override
            public void onMarkClick() {
                if (viewModel.enableSetRemark) {
                    setMark();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_measure_pw_result;
    }


    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REMARK && resultCode == RESULT_OK && data != null) {
            viewModel.model.remark = data.getStringExtra(REMARK);
            viewModel.render();
        }
    }
}
