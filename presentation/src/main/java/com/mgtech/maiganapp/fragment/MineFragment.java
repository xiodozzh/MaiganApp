package com.mgtech.maiganapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.activity.BraceletInfoActivity;
import com.mgtech.maiganapp.activity.BraceletPairActivity;
import com.mgtech.maiganapp.activity.HistoryWeeklyReportActivity;
import com.mgtech.maiganapp.activity.MainActivity;
import com.mgtech.maiganapp.activity.OtherActivity;
import com.mgtech.maiganapp.activity.PersonalInfoActivity;
import com.mgtech.maiganapp.activity.PersonalInfoDiseaseUpdateActivity;
import com.mgtech.maiganapp.activity.SetFontSizeActivity;
import com.mgtech.maiganapp.activity.SettingPushActivity;
import com.mgtech.maiganapp.adapter.FragmentMineAdapter;
import com.mgtech.maiganapp.data.event.BindInfoUpdate;
import com.mgtech.maiganapp.data.event.LoginOtherEvent;
import com.mgtech.maiganapp.data.model.IMineModel;
import com.mgtech.maiganapp.utils.LoginUtils;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.MineViewModel;
import com.mgtech.maiganapp.window.GuideMePopupWindow;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;

/**
 * @author zhaixiang
 */
public class MineFragment extends BaseFragment<MineViewModel> {
    private static final int REQUEST_TEXT_SIZE = 698;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.root)
    View root;
    private FragmentMineAdapter adapter;
    private MainActivity mainActivity;
    private GuideMePopupWindow guidePopupWindow;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        adapter = new FragmentMineAdapter(recyclerView, new FragmentMineAdapter.Listener() {
            @Override
            public void onTypeClick(int type) {
                switch (type) {
                    case IMineModel.TYPE_PERSONAL_INFO:
                        startActivity(PersonalInfoActivity.getCallingIntent(getActivity()));
                        break;
                    case IMineModel.TYPE_HEALTH_REPORT:
                        startActivity(HistoryWeeklyReportActivity.getCallingIntent(getActivity()));
                        break;
                    case IMineModel.TYPE_DISEASE_UPDATE:
                        startActivity(PersonalInfoDiseaseUpdateActivity.getCallingIntent(getActivity()));
                        break;
                    case IMineModel.TYPE_FONT_SIZE:
                        startActivityForResult(SetFontSizeActivity.getCallingIntent(getActivity()), REQUEST_TEXT_SIZE);
                        break;
                    case IMineModel.TYPE_EXCEPTION_REMINDER:
                        startActivity(SettingPushActivity.getCallingIntent(getActivity()));
                        break;
                    case IMineModel.TYPE_BRACELET_BIND:
                        startActivity(BraceletInfoActivity.getCallingIntent(getActivity()));
                        break;
                    case IMineModel.TYPE_BRACELET_UNBIND:
                        startActivity(BraceletPairActivity.getCallingIntent(getActivity()));
                        break;
                    case IMineModel.TYPE_SETTING:
                        startActivity(OtherActivity.getCallingIntent(getActivity()));
//                        goToSetAlarm();
//                        goToCancel();
                        break;
                    case IMineModel.TYPE_QQ:
                        bindQQ();
                        break;
                    case IMineModel.TYPE_WE_CHAT:
                        bindWx();
                        break;
                    case IMineModel.TYPE_SERVICE:
//                        startActivity(ServiceActivity.getCallingIntent(getActivity()));
                        break;
                    default:
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int index = parent.getChildLayoutPosition(view);
                outRect.top = viewModel.list.get(index).getMarginTop();
                if (index == viewModel.list.size() - 1) {
                    outRect.bottom = ViewUtils.dp2px(16);
                }
            }
        });
        initObs();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void bindWx() {
        IWXAPI api = ((MyApplication) getActivity().getApplication()).getWXApi();
        if (!api.isWXAppInstalled()) {
            showToast(getString(R.string.not_install_wechat));
            return;
        }
        LoginUtils.loginWeChat();
    }

    private void bindQQ() {
        LoginUtils.loginQQ();
    }

    private void goToSetAlarm(){
        ArrayList<Integer> days = new ArrayList<>();
        days.add(Calendar.SUNDAY);
        days.add(Calendar.MONDAY);
        days.add(Calendar.TUESDAY);
        days.add(Calendar.WEDNESDAY);
        days.add(Calendar.THURSDAY);
        days.add(Calendar.FRIDAY);
        days.add(Calendar.SATURDAY);
        Intent alarms = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarms.putExtra(AlarmClock.EXTRA_HOUR,10);
        alarms.putExtra(AlarmClock.EXTRA_MINUTES,23);
        alarms.putExtra(AlarmClock.EXTRA_MESSAGE,"到测量时间啦");
        alarms.putExtra(AlarmClock.EXTRA_DAYS,days);
        startActivity(alarms);
    }

    private void goToCancel(){
        Intent alarms = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
        alarms.putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE,AlarmClock.ALARM_SEARCH_MODE_LABEL);
        alarms.putExtra(AlarmClock.EXTRA_MESSAGE,"到测量时间啦");
        startActivity(alarms);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i(TAG, "MineFragment: onHiddenChanged " + hidden);
        if (!hidden) {
            viewModel.initViewList();
            root.post(new Runnable() {
                @Override
                public void run() {
                    showGuid();
                }
            });
        } else {
            hideGuide();
        }
    }

    private void hideGuide() {
        if (guidePopupWindow != null && guidePopupWindow.isShowing()) {
            guidePopupWindow.dismiss();
        }
    }

    private void showGuid() {
        if (guidePopupWindow != null && guidePopupWindow.isShowing()) {
            return;
        }
        if (!SaveUtils.doesGuideCheckBandWatched() && viewModel.isPaired() && !isHidden()) {
            guidePopupWindow = new GuideMePopupWindow(getActivity());
            guidePopupWindow.setOutsideTouchable(true);
            guidePopupWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
            guidePopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
        }
    }

    private void initObs() {
        viewModel.viewInitSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.setDataList(viewModel.list);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.e("onResume mine");
        viewModel.initViewList();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        root.post(new Runnable() {
            @Override
            public void run() {
                showGuid();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

//    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
//    public void setRead(UnreadMessageEvent event){
//        viewModel.initViewList();
//    }

    public void updateUnread(){
        viewModel.initViewList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bindInfoUpdate(BindInfoUpdate bindInfoUpdate) {
        viewModel.initViewList();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_i;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TEXT_SIZE) {
            if (resultCode == Activity.RESULT_OK && mainActivity != null) {
                mainActivity.changeText();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.mainActivity = (MainActivity) context;
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginOtherEvent event){
        if (event.type == NetConstant.QQ_LOGIN){
            if (event.status == LoginOtherEvent.FAIL){
                showToast(getString(R.string.qq_logion_fail));
            }else if (event.status == LoginOtherEvent.SUCCESS){
                viewModel.bindQQ(event.openId,event.unionId,event.token,event.name,event.avatarUrl);
            }
        }else if (event.type == NetConstant.WE_CHAT_LOGIN){
            if (event.status == LoginOtherEvent.FAIL){
                showToast(getString(R.string.wechat_login_fail));
            }else if (event.status == LoginOtherEvent.SUCCESS){
                viewModel.bindWeChat(event.openId,event.unionId,event.token,event.name,event.avatarUrl);
            }
        }
    }
}
