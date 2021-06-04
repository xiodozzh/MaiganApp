package com.mgtech.maiganapp.fragment;

import androidx.databinding.Observable;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mgtech.domain.entity.UnreadMessage;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.activity.ExceptionRecordActivity;
import com.mgtech.maiganapp.activity.HealthKnowledgeListActivity;
import com.mgtech.maiganapp.activity.MedicationPlanActivity;
import com.mgtech.maiganapp.activity.SportActivity;
import com.mgtech.maiganapp.data.event.UnreadMessageEvent;
import com.mgtech.maiganapp.viewmodel.HealthManagerViewModel;
import com.mgtech.maiganapp.widget.ActivityHealthManagerGraphView;
import com.mgtech.maiganapp.widget.ActivityHealthManagerRingView;
import com.mgtech.maiganapp.widget.MainBottomView;
import com.mgtech.maiganapp.widget.UnreadImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class HealthManagerFragment extends BaseFragment<HealthManagerViewModel> {
    @Bind(R.id.circle)
    ActivityHealthManagerRingView ringView;
    @Bind(R.id.graph_bp)
    ActivityHealthManagerGraphView graphView;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.iv_exception)
    UnreadImageView ivException;
    @Bind(R.id.iv_knowledge)
    UnreadImageView ivKnowledge;

    public static HealthManagerFragment newInstance(){
        return new HealthManagerFragment();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initObs();
        graphView.setTimes(viewModel.getStartTime(),viewModel.getEndTime());
        viewModel.getData();
        refreshLayout.setOnRefreshListener(() -> viewModel.getData());
    }

    private void initObs(){
        viewModel.showMonthData.observe(this, aBoolean -> {
            if (viewModel.model != null) {
                graphView.setData(viewModel.model.list);
                ringView.setNumber(viewModel.model.normalDays,viewModel.model.abnormalDays,viewModel.getTotalDays());
            }
        });
        viewModel.loading.observe(this, loading -> refreshLayout.setRefreshing(loading));
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!EventBus.getDefault().isRegistered(this)){
//            EventBus.getDefault().register(this);
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (EventBus.getDefault().isRegistered(this)){
//            EventBus.getDefault().unregister(this);
//        }
//    }

//    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
//    public void setRead(UnreadMessageEvent event){
//        ivException.setUnread(UnreadMessage.getAbnormalPwUnreadNumber() > 0);
//        ivKnowledge.setUnread(UnreadMessage.getHealthKnowledgeUnreadNumber() > 0);
//    }

    public void updateUnread(){
        ivException.setUnread(UnreadMessage.getAbnormalPwUnreadNumber() > 0);
        ivKnowledge.setUnread(UnreadMessage.getHealthKnowledgeUnreadNumber() > 0);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_health_manager;
    }

    @OnClick(R.id.layout_exception)
    void clickException(){
        startActivity(ExceptionRecordActivity.getCallingIntent(getActivity(),SaveUtils.getUserId(getActivity())));
    }

    @OnClick(R.id.layout_medication)
    void clickMedication(){
        startActivity(MedicationPlanActivity.getCallingIntent(getActivity()));
    }

    @OnClick(R.id.layout_sport)
    void clickSport(){
        startActivity(SportActivity.getCallingIntent(getActivity()));
    }

    @OnClick(R.id.layout_knowledge)
    void clickKnowledge(){
        startActivity(HealthKnowledgeListActivity.getCallingIntent(getActivity()));
    }
}
