package com.mgtech.maiganapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;

import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.ExceptionRecordAdapter;
import com.mgtech.maiganapp.adapter.ExceptionRecordPageAdapter;
import com.mgtech.maiganapp.data.model.ExceptionRecordModel;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.ExceptionRecordViewModel;
import com.mgtech.maiganapp.window.GuideNotificationsPopupWindow;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class ExceptionRecordActivity extends BaseActivity<ExceptionRecordViewModel> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.layout_refresh_empty)
    SwipeRefreshLayout refreshEmptyLayout;
    @Bind(R.id.anchor)
    View root;
    //    private ExceptionRecordAdapter adapter;
    private ExceptionRecordPageAdapter pageAdapter;
    private static final String USER_ID = "targetUserId";
    private GuideNotificationsPopupWindow guidePopupWindow;

    public static Intent getCallingIntent(Context context, String userId) {
        Intent intent = new Intent(context, ExceptionRecordActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        String id = getIntent().getStringExtra(USER_ID);
        if (TextUtils.isEmpty(id)) {
            id = SaveUtils.getUserId(this);
        }
        viewModel.setUserId(id);
//        adapter = new ExceptionRecordAdapter(recyclerView, new ExceptionRecordAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int index) {
//                ExceptionRecordModel model = viewModel.data.get(index);
//                model.targetId = viewModel.getUserId();
//                EventBus.getDefault().postSticky(model);
//                startActivity(MeasurePwResultActivity.getCallingIntent(ExceptionRecordActivity.this,viewModel.isMan(),viewModel.getUserId()));
//            }
//
//            @Override
//            public void onLongClick(int index) {
//                deleteException(index);
//            }
//        });
        initObs();
//        recyclerView.setAdapter(adapter);
        pageAdapter = new ExceptionRecordPageAdapter(recyclerView, new ExceptionRecordPageAdapter.OnItemClickListener() {
            @Override
            public void onClick(ExceptionRecordModel model) {
                model.targetId = viewModel.getUserId();
                EventBus.getDefault().postSticky(model);
                startActivity(MeasurePwResultActivity.getCallingIntent(ExceptionRecordActivity.this, viewModel.isMan(), viewModel.getUserId()));
            }

            @Override
            public void onLongClick(ExceptionRecordModel model) {
                deleteException(model);
            }
        }, new ExceptionRecordPageAdapter.DiffCallback());
        recyclerView.setAdapter(pageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = ViewUtils.dp2px(5);
                outRect.top = ViewUtils.dp2px(5);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                viewModel.getException();
                refresh();
            }
        });
        refreshEmptyLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                viewModel.getException();
                refresh();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        root.post(new Runnable() {
            @Override
            public void run() {
                showGuide();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideGuide();
    }

    @OnClick(R.id.tv_setting)
    void setting() {
        startActivity(SettingPushActivity.getCallingIntent(this));
    }

    private void showGuide() {
        if (needShowGuide()) {
            guidePopupWindow = new GuideNotificationsPopupWindow(this);
            guidePopupWindow.setOutsideTouchable(false);
            guidePopupWindow.showAsDropDown(root, 0, 0);
            guidePopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
        }
    }

    private boolean needShowGuide() {
        return !SaveUtils.doesGuideSetNotificationWatched();
    }

    private void hideGuide() {
        if (guidePopupWindow != null && guidePopupWindow.isShowing()) {
            guidePopupWindow.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        viewModel.getException();
    }

    private void initObs() {
        viewModel.models.observe(this, new Observer<PagedList<ExceptionRecordModel>>() {
            @Override
            public void onChanged(PagedList<ExceptionRecordModel> exceptionRecordModels) {
                Log.i("initObs", "onChanged: " + exceptionRecordModels.snapshot().size());
                viewModel.loading.postValue(false);
                pageAdapter.submitList(exceptionRecordModels);
//                if (exceptionRecordModels.size() == 0){
//                    refreshEmptyLayout.setVisibility(View.VISIBLE);
//                    refreshLayout.setVisibility(View.GONE);
//                }else{
//                    refreshEmptyLayout.setVisibility(View.GONE);
//                    refreshLayout.setVisibility(View.VISIBLE);
//                }
            }
        });
        viewModel.empty.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean empty) {
                refreshEmptyLayout.setVisibility(empty?View.VISIBLE:View.GONE);
                refreshLayout.setVisibility(empty?View.GONE:View.VISIBLE);
            }
        });
        viewModel.deleteSuccess.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                refresh();
            }
        });
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                refreshLayout.setRefreshing(loading);
                refreshEmptyLayout.setRefreshing(loading);
            }
        });
    }

    private void refresh() {
        viewModel.invalidateDataSource();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_exception_record;
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    private void deleteException(final ExceptionRecordModel model) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.caution)
                .setMessage(R.string.bracelet_reminder_are_you_sure_to_delete)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.deleteException(model);
                    }
                }).create();
        alertDialog.show();
    }
}
