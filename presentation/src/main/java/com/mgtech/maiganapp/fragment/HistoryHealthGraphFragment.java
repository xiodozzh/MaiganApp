package com.mgtech.maiganapp.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.activity.HistoryActivityCallback;
import com.mgtech.maiganapp.activity.MeasurePwActivity;
import com.mgtech.maiganapp.viewmodel.HistoryHealthGraphViewModel;
import com.mgtech.maiganapp.window.GuideHistoryListPopupWindow;
import com.mgtech.maiganapp.window.GuideRadarPopupWindow;
import com.mgtech.maiganapp.widget.IndicatorLineChartView;
import com.mgtech.maiganapp.widget.RadarGraphView;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class HistoryHealthGraphFragment extends BaseFragment<HistoryHealthGraphViewModel> {
    @Bind({R.id.btn_day, R.id.btn_month})
    TextView[] btnSection;
    private HistoryActivityCallback activity;
    @Bind(R.id.line_chart)
    IndicatorLineChartView chartView;
    @Bind(R.id.radarView)
    RadarGraphView radarGraphView;
    @Bind(R.id.root)
    View root;
    @Bind(R.id.anchor)
    View anchor;
    private int dataPickerColor;
    private static final int INDEX_BP = HistoryHealthGraphViewModel.BP;
    private static final int INDEX_V0 = HistoryHealthGraphViewModel.V0;
    private static final int INDEX_TPR = HistoryHealthGraphViewModel.TPR;
    private static final int INDEX_HR = HistoryHealthGraphViewModel.HR;
    private static final int INDEX_CO = HistoryHealthGraphViewModel.CO;
    private static final int INDEX_PM = HistoryHealthGraphViewModel.PM;
    private GuideHistoryListPopupWindow guidePopupWindow;
    private GuideRadarPopupWindow guideRadarPopupWindow;

    private boolean[] timeStyle = {true, true, true, false, false, false};

    @Override
    protected void init(Bundle savedInstanceState) {
        initObservable();
        selectTimeSection(0);
        dataPickerColor = ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimary);
        radarGraphView.setListener(new RadarGraphView.Listener() {
            @Override
            public void onItemSelect(int index) {
                switch (index) {
                    case INDEX_BP:
                        chartView.showIndex(IndicatorLineChartView.INDEX_BP);
                        break;
                    case INDEX_V0:
                        chartView.showIndex(IndicatorLineChartView.INDEX_V0);
                        break;
                    case INDEX_TPR:
                        chartView.showIndex(IndicatorLineChartView.INDEX_TPR);
                        break;
                    case INDEX_HR:
                        chartView.showIndex(IndicatorLineChartView.INDEX_HR);
                        break;
                    case INDEX_CO:
                        chartView.showIndex(IndicatorLineChartView.INDEX_CO);
                        break;
                    case INDEX_PM:
                        chartView.showIndex(IndicatorLineChartView.INDEX_PM);
                        break;
                    default:
                }
                viewModel.setIndicatorType(index);
            }
        });
        chartView.showIndex(IndicatorLineChartView.INDEX_BP);
        viewModel.setUserId(activity.getUserId());
        initChartView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity != null) {
            viewModel.setDate(activity.getCalendar());
            anchor.post(new Runnable() {
                @Override
                public void run() {
                    showGuid();
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideGuide();
    }



    private void showGuid() {
        if (!SaveUtils.doesGuideCheckHistoryWatched() && getActivity() != null) {
            guidePopupWindow = new GuideHistoryListPopupWindow(getActivity(), new GuideHistoryListPopupWindow.Callback() {
                @Override
                public void onDismiss() {
                    showRadarGuide();
                }
            });
            guidePopupWindow.setOutsideTouchable(false);
            guidePopupWindow.showAsDropDown(anchor, 0, 0);
            guidePopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
        }
    }

    @OnClick(R.id.iv_radar_guide)
    public void showRadarGuide() {
        if (getActivity() != null) {
            guideRadarPopupWindow = new GuideRadarPopupWindow(getActivity());
            guideRadarPopupWindow.setOutsideTouchable(false);
            guideRadarPopupWindow.showAsDropDown(anchor, 0, 0);
            backgroundAlpha(0.6f);
            guideRadarPopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
            guideRadarPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
        }
    }

    private void hideGuide() {
        if (guidePopupWindow != null && guidePopupWindow.isShowing()) {
            guidePopupWindow.dismiss();
        }
        if (guideRadarPopupWindow != null && guideRadarPopupWindow.isShowing()) {
            guideRadarPopupWindow.dismiss();
        }
    }



    private void initChartView() {
        chartView.setTimeMode(IndicatorLineChartView.DAY);
    }

    private void initObservable() {
        viewModel.updateGraphData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.dataModel != null) {
                    radarGraphView.setData(viewModel.dataModel.hrScore,
                            (viewModel.dataModel.psScore + viewModel.dataModel.pdScore) / 2,
                            viewModel.dataModel.pmScore, viewModel.dataModel.tprScore, viewModel.dataModel.coScore,
                            viewModel.dataModel.v0Score);
                    chartView.setSex(activity.isMan() ? MyConstant.MAN : MyConstant.WOMEN);
                    chartView.setData(viewModel.dataModel);
                }
            }
        });
        viewModel.clearGraphData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                chartView.setData(null);
            }
        });
    }

    @OnClick(R.id.iv_measure)
    public void goToMeasure() {
        startActivity(MeasurePwActivity.getCallingIntent(getActivity()));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_history_health_graph;
    }

    /**
     * 选择日、月、周
     *
     * @param index 序号
     */
    private void selectTimeSection(int index) {
        viewModel.setTimeType(index);
        for (int i = 0; i < btnSection.length; i++) {
            btnSection[i].setSelected(i == index);
        }
    }


    /**
     * 选择平均值种类
     *
     * @param view 按钮
     */
    @OnClick({R.id.btn_day, R.id.btn_month})
    void selectTimeSection(View view) {
        switch (view.getId()) {
            case R.id.btn_day:
                selectTimeSection(HistoryHealthGraphViewModel.DAY);
                chartView.setTimeMode(IndicatorLineChartView.DAY);
                break;
            case R.id.btn_month:
                selectTimeSection(HistoryHealthGraphViewModel.MONTH);
                chartView.setTimeMode(IndicatorLineChartView.MONTH);
                break;
            default:
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HistoryActivityCallback) {
            this.activity = (HistoryActivityCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @OnClick(R.id.btn_back)
    void back() {
        activity.back();
    }

    @OnClick(R.id.btn_last)
    void lastDayOrMonth() {
        viewModel.lastDate();
        if (activity != null) {
            activity.setCalendar(viewModel.getCalendar());
        }
    }

    @OnClick(R.id.btn_next)
    void nextDayOrMonth() {
        viewModel.nextDate();
        if (activity != null) {
            activity.setCalendar(viewModel.getCalendar());
        }
    }

    @OnClick(R.id.tv_date)
    void setDay() {
        if (viewModel.getTimeType() == HistoryHealthGraphViewModel.DAY) {
            timeStyle[2] = true;
        } else {
            timeStyle[2] = false;
        }
        TimePickerView pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                viewModel.setDate(calendar);
                if (activity != null) {
                    activity.setCalendar(viewModel.getCalendar());
                }
            }
        })
                .setDate(viewModel.getCalendar())
                .setCancelColor(dataPickerColor)
                .setType(timeStyle)
                .setRangDate(viewModel.rangeCalendarStart, viewModel.rangeCalendarEnd)
                .setSubmitColor(dataPickerColor)
                .setSubmitText(getString(R.string.submit))
                .setCancelText(getString(R.string.cancel))
                .build();
        pvTime.show();
    }

    @OnClick(R.id.btn_list)
    void goToList() {
        activity.goToList();
    }

}
