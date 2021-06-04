package com.mgtech.maiganapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;
import androidx.lifecycle.Observer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mgtech.domain.entity.UnreadMessage;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.activity.BraceletReminderActivity;
import com.mgtech.maiganapp.activity.HistoryEcgActivity;
import com.mgtech.maiganapp.activity.HistoryHealthActivity;
import com.mgtech.maiganapp.activity.HistoryPwRecordActivity;
import com.mgtech.maiganapp.activity.MedicationReminderActivity;
import com.mgtech.maiganapp.data.model.HomeDataModel;
import com.mgtech.maiganapp.viewmodel.HomeViewModel;
import com.mgtech.maiganapp.widget.EcgPartDataGraphView;
import com.mgtech.maiganapp.widget.EmergencyCallButton;
import com.mgtech.maiganapp.widget.HomeHeaderCircleView;
import com.mgtech.maiganapp.widget.UnreadImageView;
import com.mgtech.maiganapp.window.GuideHomePopupWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * @author zhaixiang
 * 主页
 */

public class HomeFragment extends BaseFragment<HomeViewModel> {
    @Bind(R.id.cv_bp)
    LineChartView cvBp;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @Bind(R.id.ecg_view)
    EcgPartDataGraphView ecgView;
    @Bind(R.id.tvEcgDate)
    TextView tvEcgDate;
    @Bind(R.id.tvPwDate)
    TextView tvPwDate;
    @Bind(R.id.bp_circle)
    HomeHeaderCircleView circleView;
    @Bind(R.id.medicalIcon)
    UnreadImageView ivMedicalReminder;
    @Bind(R.id.iv_history)
    UnreadImageView ivHistoryRecord;
    @Bind(R.id.healthIcon)
    UnreadImageView ivHistoryGraph;
    private GuideHomePopupWindow guidePopupWindow;
    @Bind(R.id.btn_emergency)
    EmergencyCallButton emergencyCallButton;
    private static final String TEL = "tel:";

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        layoutRefresh.setOnRefreshListener(this::getData);
        emergencyCallButton.setOnClick(new EmergencyCallButton.Listener() {
            @Override
            public void onClick() {
                showToast(getString(R.string.home_emergency_click_message));
            }

            @Override
            public void onLongClick() {
                if (viewModel.firstAidPhones != null && viewModel.firstAidPhones.length != 0) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri uri = Uri.parse(TEL + viewModel.firstAidPhones[0]);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        });
        initObs();
        initHealthView();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.updateEmergencyState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideGuide();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            hideGuide();
        } else {
            layoutRefresh.post(this::showGuid);
            getData();
        }
    }

    private boolean needShowGuide() {
        return !SaveUtils.doesGuideCheckHrWatched() || !SaveUtils.doesGuideCheckBpWatched() || !SaveUtils.doesGuideMeasureWatched();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideGuide();
    }

    private void showGuid() {
        if (needShowGuide() && getActivity() != null) {
            guidePopupWindow = new GuideHomePopupWindow(getActivity());
            guidePopupWindow.setOutsideTouchable(false);
            guidePopupWindow.showAtLocation(layoutRefresh, Gravity.CENTER, 0, 0);
            guidePopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
        }
    }

    private void hideGuide() {
        if (guidePopupWindow != null && guidePopupWindow.isShowing()) {
            guidePopupWindow.dismiss();
        }
    }

    private void initObs() {
        viewModel.loading.observe(this, loading -> layoutRefresh.setRefreshing(loading));
        viewModel.renderPwData.observe(this, aBoolean -> {
            if (getActivity() == null) {
                return;
            }
            List<HomeDataModel.Data> psList = viewModel.model.psList;
            List<HomeDataModel.Data> pdList = viewModel.model.pdList;
            refreshPwData(psList, pdList);
        });
        viewModel.renderEcgData.observe(this, aBoolean -> {
            if (getActivity() == null) {
                return;
            }
            ecgView.setData(viewModel.model.ecgData, 488);
        });
    }


    public void updateUnread() {
        if (ivHistoryRecord == null){
            return;
        }
        ivHistoryRecord.setUnread(UnreadMessage.getNormalPwUnreadNumber() > 0);
    }

    @OnClick(R.id.cv_medical_reminder)
    void jumpMedicalReminder() {
        startActivity(MedicationReminderActivity.getCallingIntent(getActivity()));
    }

    @OnClick(R.id.bp_circle)
    void circleClick() {
        if (!circleView.isAnimationOn()) {
            circleView.changeState();
            viewModel.switchHeaderDisplay();
        }
    }

    @OnClick(R.id.layout_auto)
    void clickAutoSet() {
        startActivity(BraceletReminderActivity.getCallingIntent(getActivity()));
    }

    @OnClick(R.id.card_history)
    void clickHistoryRecord() {
        startActivity(HistoryPwRecordActivity.getCallingIntent(getActivity(), SaveUtils.getUserId(getActivity()), viewModel.isMan(), viewModel.model.pwTime));
    }

    @OnClick(R.id.btn_emergency_disable)
    void clickEmergencyDisable() {
//        if (getFragmentManager() == null) {
//            return;
//        }
//        ServiceUnavailableDialogFragment fragment = ServiceUnavailableDialogFragment.getInstance();
//        fragment.setCallback(new ServiceUnavailableDialogFragment.Callback() {
//            @Override
//            public void check() {
////                startActivity(ServiceActivity.getCallingIntent(getActivity()));
//            }
//        });
//        fragment.show(getFragmentManager(), "clickEmergencyDisable");
        if (getActivity() == null){
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(R.string.home_this_service_has_not_been_launched)
                .setPositiveButton(R.string.home_this_service_has_not_been_launched_submit,null)
                .create();
        dialog.show();
    }


    /**
     * 初始化心血管健康数据
     */
    private void initHealthView() {
        cvBp.setViewportCalculationEnabled(false);
        cvBp.setZoomType(ZoomType.HORIZONTAL);
        cvBp.setValueSelectionEnabled(false);
        cvBp.setMaxZoom(1f);
        cvBp.setOnClickListener(v -> clickHealthCardView());

        Viewport v = new Viewport(cvBp.getMaximumViewport());

        v.bottom = 0f;
        v.top = 180f;
        v.left = 0f;
        v.right = 24f;
        cvBp.setMaximumViewport(v);
        cvBp.setCurrentViewport(v);
    }

    private void getData() {
        viewModel.getFirstAidPhone();
        viewModel.getHomeData();
    }

    private void refreshPwData(List<HomeDataModel.Data> psList, List<HomeDataModel.Data> pdList) {
        if (getActivity() == null) {
            return;
        }
        Axis axisY = new Axis();
        Axis axisX = new Axis();
        ArrayList<AxisValue> axisValuesX = new ArrayList<>();
        ArrayList<AxisValue> axisValuesY = new ArrayList<>();
        axisX.setValues(axisValuesX);
        axisY.setValues(axisValuesY);
        axisX.setLineColor(Color.parseColor("#aaaaaa"));
        axisY.setLineColor(Color.parseColor("#aaaaaa"));
        axisX.setInside(true);
        LineChartData data = new LineChartData();
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        List<Line> lines = new ArrayList<>();
        List<PointValue> psValue = new ArrayList<>();
        List<PointValue> pdValue = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (HomeDataModel.Data dataModel : psList) {
            calendar.setTimeInMillis(dataModel.time);
            float x = Utils.getTimeLineX(calendar);
            psValue.add(new PointValue(x, dataModel.value));
        }
        for (HomeDataModel.Data dataModel : pdList) {
            calendar.setTimeInMillis(dataModel.time);
            float x = Utils.getTimeLineX(calendar);
            pdValue.add(new PointValue(x, dataModel.value));
        }
        lines.add(createLine(psValue, ContextCompat.getColor(getActivity(), R.color.ps_line_color)));
        lines.add(createLine(pdValue, ContextCompat.getColor(getActivity(), R.color.pd_line_color)));
        data.setLines(lines);
        cvBp.setLineChartData(data);
    }

    private Line createLine(List<PointValue> values, int color) {
        return new Line(values).setColor(color).setPointRadius(3).setHasLines(true).setAreaTransparency(50)
                .setStrokeWidth(2).setCubic(false).setHasLabelsOnlyForSelected(false);
    }


    @OnClick(R.id.cd_health)
    void clickHealthCardView() {
        startActivity(HistoryHealthActivity.getCallingIntent(getActivity(), SaveUtils.getUserId(getActivity()), viewModel.isMan(), viewModel.model.pwTime));
    }

    @OnClick(R.id.cd_ecg)
    void clickEcgCardView() {
        if (viewModel.model != null && viewModel.model.ecgTime != 0) {
            startActivity(HistoryEcgActivity.getCallingIntent(getActivity(), SaveUtils.getUserId(getActivity()), viewModel.model.ecgTime));
        } else {
            startActivity(HistoryEcgActivity.getCallingIntent(getActivity(), SaveUtils.getUserId(getActivity()), System.currentTimeMillis()));
        }
    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home_new;
    }


}
