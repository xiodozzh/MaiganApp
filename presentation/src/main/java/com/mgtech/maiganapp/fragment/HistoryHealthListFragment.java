package com.mgtech.maiganapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.Observable;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.activity.HistoryActivityCallback;
import com.mgtech.maiganapp.activity.MeasurePwResultActivity;
import com.mgtech.maiganapp.activity.MeasurePwActivity;
import com.mgtech.maiganapp.adapter.FragmentHistoryListItemAdapter;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.viewmodel.HistoryHealthListViewModel;
import com.mgtech.maiganapp.widget.CalendarViewPager;
import com.mgtech.maiganapp.widget.WrapContentLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiangBarrier
 */
public class HistoryHealthListFragment extends BaseFragment<HistoryHealthListViewModel> implements
        FragmentHistoryListItemAdapter.Callback{
    private HistoryActivityCallback activity;
    @Bind(R.id.calendarView)
    CalendarViewPager calendarView;
    @Bind(R.id.rv_list)
    RecyclerView recyclerView;
    @Bind(R.id.layout_refresh_list)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.root)
    View root;
    private FragmentHistoryListItemAdapter adapter;

    @Override
    protected void init(Bundle savedInstanceState) {
        calendarView.setListener(new CalendarViewPager.CalendarSelectListener() {
            @Override
            public void onSelect(Calendar calendar) {
                viewModel.setCalendar(calendar);
                if (activity != null){
                    activity.setCalendar(viewModel.getCalendar());
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getData();
            }
        });
        adapter = new FragmentHistoryListItemAdapter(recyclerView);
        adapter.setCallback(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        initObs();
        if (activity != null) {
            calendarView.selectCalendar(activity.getCalendar());
            viewModel.setUserId(activity.getUserId());
            viewModel.setMan(activity.isMan());
            viewModel.setCalendar(activity.getCalendar());
        }
    }


    private void initObs() {
        viewModel.getDataSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.setData(viewModel.dataList);
            }
        });
        viewModel.listDateLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                refreshLayout.setRefreshing(viewModel.listDateLoading.get());
            }
        });
        viewModel.clearData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.setData(new ArrayList<IndicatorDataModel>());
            }
        });
        viewModel.deleteSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.setData(viewModel.dataList);
            }
        });
    }

    @OnClick(R.id.iv_measure)
    public void goToMeasure(){
        startActivity(MeasurePwActivity.getCallingIntent(getActivity()));
    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_history_health_list;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HistoryActivityCallback){
            activity = (HistoryActivityCallback) context;
        }
    }

    @OnClick(R.id.btn_back)
    void back(){
        if (activity != null) {
            activity.back();
        }
    }

    @OnClick(R.id.btn_graph)
    void goToGraph(){
        if (activity != null) {
            activity.goToGraph();
        }
    }

    @OnClick(R.id.btn_back_today)
    void backToday(){
        Calendar calendar = Calendar.getInstance();
        setCalendar(calendar);
    }

    private void setCalendar(Calendar calendar){
        viewModel.setCalendar(calendar);
        if (activity != null){
            activity.setCalendar(viewModel.getCalendar());
        }
        calendarView.selectCalendar(calendar);
    }

    @Override
    public void onItemClick(int position) {
        if (position < viewModel.dataList.size()) {
            EventBus.getDefault().postSticky(viewModel.dataList.get(position));
            startActivity(MeasurePwResultActivity.getCallingIntent(getActivity(),viewModel.isMan,viewModel.userId));
        }
    }

    @Override
    public void onLongClick(final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.caution)
                .setMessage(R.string.bracelet_reminder_are_you_sure_to_delete)
                .setNegativeButton(R.string.cancel,null)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.deleteData(position);
                    }
                }).create();
        alertDialog.show();
    }
}
