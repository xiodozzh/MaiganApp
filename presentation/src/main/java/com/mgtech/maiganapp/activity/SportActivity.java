package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.SportAdapter;
import com.mgtech.maiganapp.data.model.StepModel;
import com.mgtech.maiganapp.viewmodel.SportViewModel;
import com.mgtech.maiganapp.widget.BarChartView;
import com.mgtech.maiganapp.widget.StepBarChartView;
import com.mgtech.maiganapp.widget.WrapContentLinearLayoutManager;

import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class SportActivity extends BaseActivity<SportViewModel> {
    @Bind({R.id.btn_day, R.id.btn_week, R.id.btn_month})
    TextView[] btnTimeSpan;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private SportAdapter adapter;
    private StepBarChartView chartView;
    private TextView tvDesc;
    private TextView tvValue;
    private TextView tvFoot;
    private TextView tvDistance;
    private TextView tvHeat;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SportActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        View header = getLayoutInflater().inflate(R.layout.layout_sport_header, null, false);
        chartView = header.findViewById(R.id.chart_view);
        tvDesc = header.findViewById(R.id.tv_description);
        tvValue = header.findViewById(R.id.tv_value);
        tvFoot = header.findViewById(R.id.tv_foot_value);
        tvHeat = header.findViewById(R.id.tv_heat_value);
        tvDistance = header.findViewById(R.id.tv_distance_value);
        chartView.setItemSelectListener(new BarChartView.ItemSelectListener() {
            @Override
            public void onSelect(int index) {
                viewModel.getDataDetail(index);
                renderHeader();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshData();
            }
        });
        initObs();
        selectTimeSpan(0);
        adapter = new SportAdapter(viewModel.stepDetailList, header);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new WrapContentLinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initObs() {
        viewModel.dataDataUpdate.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                switch (viewModel.type) {
                    case SportViewModel.TYPE_DAY:
                        chartView.setNumberOfBarInSight(7);
                        chartView.setTextSizeInSp(12);
                        break;
                    case SportViewModel.TYPE_WEEK:
                        chartView.setNumberOfBarInSight(5);
                        chartView.setTextSizeInSp(10);
                        break;
                    case SportViewModel.TYPE_MONTH:
                        chartView.setNumberOfBarInSight(5);
                        chartView.setTextSizeInSp(12);
                        break;
                    default:
                }
                chartView.setData(viewModel.stepList);
                chartView.invalidateView();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        viewModel.detailDataUpdate.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                renderHeader();
                String format;
                if (viewModel.type == SportViewModel.TYPE_DAY) {
                    format = MyConstant.DISPLAY_TIME;
                } else {
                    format = getString(R.string.sport_date_format_month_day);
                }
                adapter.setDateFormat(format);
                adapter.notifyDataSetChanged();
            }
        });

        viewModel.loading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                swipeRefreshLayout.setRefreshing(viewModel.loading.get());
            }
        });
    }

    private void renderHeader() {
        StepModel entity = viewModel.selectModel;
        if (entity == null) {
            return;
        }
        tvValue.setText(String.valueOf(Math.round(entity.duration)));
        tvFoot.setText(String.valueOf(Math.round(entity.stepCount)));
        tvDistance.setText(String.valueOf(Math.round(entity.distance)));
        tvHeat.setText(String.format(Locale.getDefault(), "%.1f", entity.heat / 1000f));
        tvDesc.setText(viewModel.descTitle.get());
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshData();
    }

    @OnClick({R.id.btn_day, R.id.btn_week, R.id.btn_month})
    void switchTimeSpan(TextView view) {
        switch (view.getId()) {
            case R.id.btn_day:
                selectTimeSpan(0);
                viewModel.setType(SportViewModel.TYPE_DAY);
                break;
            case R.id.btn_week:
                selectTimeSpan(1);
                viewModel.setType(SportViewModel.TYPE_WEEK);
                break;
            case R.id.btn_month:
                selectTimeSpan(2);
                viewModel.setType(SportViewModel.TYPE_MONTH);
                break;
            default:
        }
        viewModel.refreshData();
    }

    /**
     * 选择时间跨度
     *
     * @param index 选择
     */
    private void selectTimeSpan(int index) {
        for (TextView tv : btnTimeSpan) {
            tv.setSelected(false);
            tv.setElevation(1);
        }
        for (int i = 0; i < btnTimeSpan.length; i++) {
            TextView tv = btnTimeSpan[i];
            if (index == i) {
                tv.setSelected(true);
                tv.setElevation(3);
            } else {
                tv.setSelected(false);
                tv.setElevation(1);
            }
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_sport;
    }
}
