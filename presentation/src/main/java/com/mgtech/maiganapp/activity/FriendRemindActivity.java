package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.Observable;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.FriendDataModel;
import com.mgtech.maiganapp.data.model.FriendModel;
import com.mgtech.maiganapp.viewmodel.FriendRemindViewModel;
import com.mgtech.maiganapp.widget.EcgDisplayGraphView;
import com.mgtech.maiganapp.widget.UnreadImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * @author zhaixiang
 */
public class FriendRemindActivity extends BaseActivity<FriendRemindViewModel>{
    private static final String MODEL = "friendModel";
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.layout_refresh_error)
    SwipeRefreshLayout refreshErrorLayout;
    @Bind(R.id.layout_refresh_no_author)
    SwipeRefreshLayout refreshNoAuthor;
    @Bind(R.id.chart_view_ecg)
    EcgDisplayGraphView ecgView;
    @Bind(R.id.chart_view_pw)
    LineChartView bpView;
    @Bind(R.id.iv_icon_exception)
    UnreadImageView ivException;
    @Bind(R.id.iv_history)
    UnreadImageView ivHistory;
    @Bind(R.id.iv_icon_pw)
    UnreadImageView ivPwGraph;

    private AlertDialog remindDialog;

    public static Intent getCallingIntent(Context context, FriendModel model){
        Intent intent = new Intent(context,FriendRemindActivity.class);
        intent.putExtra(MODEL,model);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        initObs();
        initBpView();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getData();
            }
        });
        refreshErrorLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getData();
            }
        });
        refreshNoAuthor.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getData();
            }
        });

        viewModel.setFriendModel(getIntent().getParcelableExtra(MODEL));
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getData();
    }

    private void initObs(){
        viewModel.loadPwDataSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                List<FriendDataModel.Data> psList = viewModel.friendDataModel.psList;
                List<FriendDataModel.Data>pdList = viewModel.friendDataModel.pdList;
                refreshPwData(psList,pdList);
            }
        });
        viewModel.loadEcgDataSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ecgView.setData(viewModel.friendDataModel.ecgData);
            }
        });
        viewModel.loading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                refreshLayout.setRefreshing(viewModel.loading.get());
                refreshErrorLayout.setRefreshing(viewModel.loading.get());
                refreshNoAuthor.setRefreshing(viewModel.loading.get());
            }
        });
        viewModel.havePwUnread.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ivHistory.setUnread(viewModel.havePwUnread.get());
//                ivPwGraph.setUnread(viewModel.havePwUnread.get());
            }
        });
        viewModel.haveExceptionUnread.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ivException.setUnread(viewModel.haveExceptionUnread.get());
            }
        });
    }

    /**
     * 初始化心血管健康数据
     */
    private void initBpView() {
        bpView.setViewportCalculationEnabled(false);
        bpView.setZoomType(ZoomType.HORIZONTAL);
        bpView.setValueSelectionEnabled(false);
        bpView.setMaxZoom(1f);
        bpView.setClickable(false);
        Viewport v = new Viewport(bpView.getMaximumViewport());

        v.bottom = 0f;
        v.top = 180f;
        v.left = 0f;
        v.right = 24f;
        bpView.setMaximumViewport(v);
        bpView.setCurrentViewport(v);
        bpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HistoryHealthActivity.getCallingIntent(FriendRemindActivity.this,viewModel.id,viewModel.isMan,viewModel.friendDataModel.lastMeasureTime));
            }
        });
    }

    private void refreshPwData(List<FriendDataModel.Data> psList, List<FriendDataModel.Data>pdList) {
        LineChartData data = new LineChartData();

        List<Line> lines = new ArrayList<>();
        List<PointValue> psValue =new ArrayList<>();
        List<PointValue> pdValue = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for ( FriendDataModel.Data dataModel : psList) {
            calendar.setTimeInMillis(dataModel.time);
            float x = Utils.getTimeLineX(calendar);
            psValue.add(new PointValue(x,dataModel.value));
        }
        for ( FriendDataModel.Data dataModel : pdList) {
            calendar.setTimeInMillis(dataModel.time);
            float x = Utils.getTimeLineX(calendar);
            pdValue.add(new PointValue(x,dataModel.value));
        }
        lines.add(createLine(psValue, ContextCompat.getColor(this,R.color.ps_line_color)));
        lines.add(createLine(pdValue, ContextCompat.getColor(this,R.color.pd_line_color)));
        data.setLines(lines);
        bpView.setLineChartData(data);
    }

    private Line createLine(List<PointValue> values, int color) {
        return new Line(values).setColor(color).setPointRadius(3).setHasLines(true).setAreaTransparency(50)
                .setStrokeWidth(2).setCubic(false).setHasLabelsOnlyForSelected(false);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_friend_remind;
    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }


    @OnClick(R.id.cv_remind)
    void remindClick(){
        dismissDialog();
        remindDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.activity_friend_remind_remind_measure)
                .setMessage(R.string.activity_friend_remind_remind_measure_dialog_text)
                .setNegativeButton(R.string.cancel,null)
                .setPositiveButton(R.string.activity_friend_remind_remind_measure_dialog_submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.remindFriend();
                    }
                }).create();
        remindDialog.show();
    }

    private void dismissDialog(){
        if (remindDialog != null && remindDialog.isShowing()){
            remindDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    @OnClick(R.id.cv_ecg)
    void ecgClick(){
        if (viewModel.friendDataModel != null && viewModel.friendDataModel.ecgTime !=0) {
            startActivity(HistoryEcgActivity.getCallingIntent(this, viewModel.id, viewModel.friendDataModel.ecgTime));
        }else{
            startActivity(HistoryEcgActivity.getCallingIntent(this, viewModel.id, System.currentTimeMillis()));
        }
    }

    @OnClick({R.id.cv_pw,R.id.chart_view_pw})
    void pwClick(){
        startActivity(HistoryHealthActivity.getCallingIntent(this,viewModel.id,viewModel.isMan,viewModel.friendDataModel.lastMeasureTime));
    }

    @OnClick(R.id.card_history)
    void clickHistoryRecord(){
        startActivity(HistoryPwRecordActivity.getCallingIntent(this, viewModel.id,viewModel.isMan,viewModel.friendDataModel.lastMeasureTime));
    }

    @OnClick(R.id.cv_exception)
    void exceptionClick(){
        startActivity(ExceptionRecordActivity.getCallingIntent(this,viewModel.id));
    }
}
