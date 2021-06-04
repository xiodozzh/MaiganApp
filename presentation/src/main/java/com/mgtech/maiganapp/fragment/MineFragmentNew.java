package com.mgtech.maiganapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.activity.BraceletInfoActivity;
import com.mgtech.maiganapp.activity.BraceletPairActivity;
import com.mgtech.maiganapp.activity.BraceletReminderActivity;
import com.mgtech.maiganapp.activity.CompanyServiceActivity;
import com.mgtech.maiganapp.activity.CompanyServiceAuthActivity;
import com.mgtech.maiganapp.activity.ExceptionRecordActivity;
import com.mgtech.maiganapp.activity.HealthKnowledgeListActivity;
import com.mgtech.maiganapp.activity.HistoryEcgActivity;
import com.mgtech.maiganapp.activity.HistoryHealthActivity;
import com.mgtech.maiganapp.activity.HistoryWeeklyReportActivity;
import com.mgtech.maiganapp.activity.MainActivity;
import com.mgtech.maiganapp.activity.MedicationPlanActivity;
import com.mgtech.maiganapp.activity.PersonalInfoActivity;
import com.mgtech.maiganapp.activity.PersonalInfoDiseaseUpdateActivity;
import com.mgtech.maiganapp.activity.SetFontSizeActivity;
import com.mgtech.maiganapp.activity.SettingActivity;
import com.mgtech.maiganapp.activity.SportActivity;
import com.mgtech.maiganapp.adapter.MineAdapter;
import com.mgtech.maiganapp.adapter.MineServiceAdapter;
import com.mgtech.maiganapp.adapter.ServicePagerAdapter;
import com.mgtech.maiganapp.data.event.BindInfoUpdate;
import com.mgtech.maiganapp.data.event.LoginOtherEvent;
import com.mgtech.maiganapp.data.event.UnreadMessageEvent;
import com.mgtech.maiganapp.data.model.CompanyModel;
import com.mgtech.maiganapp.data.model.MineCardModel;
import com.mgtech.maiganapp.data.model.ServiceModel;
import com.mgtech.maiganapp.utils.LoginUtils;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.MineViewModelNew;
import com.mgtech.maiganapp.widget.MineCompanyBarView;
import com.mgtech.maiganapp.window.GuideMePopupWindow;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class MineFragmentNew extends BaseFragment<MineViewModelNew> {
    private static final int REQUEST_TEXT_SIZE = 698;
    @Bind(R.id.recyclerView_data)
    RecyclerView recyclerViewData;
    @Bind(R.id.recyclerView_tool)
    RecyclerView recyclerViewTool;
    @Bind(R.id.recyclerView_bought_service)
    RecyclerView recyclerViewBoughtService;
    @Bind(R.id.root)
    View root;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_qq)
    TextView tvQQ;
    @Bind(R.id.tv_wechat)
    TextView tvWechat;
    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;
    @Bind(R.id.layout_bind)
    View layoutBind;
    @Bind(R.id.layout_un_bind)
    View layoutUnBind;
    @Bind(R.id.tv_sync_time)
    TextView tvSyncTime;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.service_view_pager)
    ViewPager viewPager;
    @Bind(R.id.layout_dots)
    LinearLayout layoutDots;
    private MineCompanyBarView[] barViews;
    private int currentPage = 0;
    private List<View> pageList = new ArrayList<>();
    private MainActivity mainActivity;
    private MineAdapter dataAdapter;
    private MineAdapter toolAdapter;
    private MineServiceAdapter broughtAdapter;
    private GuideMePopupWindow guidePopupWindow;
    private ServicePagerAdapter pagerAdapter;

    public static MineFragmentNew newInstance() {
        return new MineFragmentNew();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initRefresh();
        initDataAdapter();
        initToolAdapter();
        initObs();
        initPagerAdapter();
        initBoughtService();
    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.initService();
                viewModel.initBoughtService();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initPagerAdapter() {
        viewModel.companyList.observe(this, companyModels -> {
            pageList.clear();
            for (CompanyModel model : companyModels) {
                View page = LayoutInflater.from(getContext()).inflate(R.layout.layout_service_view_pager_item, null, false);
                ImageView iv = page.findViewById(R.id.iv_service);
                TextView tvTitle = page.findViewById(R.id.tv_service_title);
                TextView tvSubtitle = page.findViewById(R.id.tv_service_subtitle);
                ViewUtils.loadCompanyImage(getContext(), R.drawable.mine_service_bg, model.imageUrl, iv);
                tvTitle.setText(model.title);
                if (TextUtils.isEmpty(model.subTitle)){
                    tvSubtitle.setVisibility(View.GONE);
                }else {
                    tvSubtitle.setVisibility(View.VISIBLE);
                    tvSubtitle.setText(model.subTitle);
                }
                page.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(model.pageUrl)) {
                            viewModel.checkAuth(model);
                        }
                    }
                });
                pageList.add(page);
            }
            viewPager.setAdapter(pagerAdapter);
            pagerAdapter.notifyDataSetChanged();

            layoutDots.removeAllViews();
            int size = companyModels.size();
            if (size != 0) {
                viewPager.setVisibility(View.VISIBLE);
                barViews = new MineCompanyBarView[size];
                for (int i = 0; i < size; i++) {
                    barViews[i] = new MineCompanyBarView(getContext());
                    layoutDots.addView(barViews[i]);
                }
                if (currentPage >= size) {
                    currentPage = size - 1;
                }
                if (currentPage < 0) {
                    currentPage = 0;
                }
                barViews[currentPage].select();
                viewPager.setCurrentItem(currentPage, false);
            }else{
                viewPager.setVisibility(View.GONE);
            }
        });
        pagerAdapter = new ServicePagerAdapter(pageList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        pagerAdapter.notifyDataSetChanged();
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < barViews.length; i++) {
                if (position < barViews.length) {
                    if (i != position) {
                        barViews[i].disSelect();
                    } else {
                        barViews[i].select();
                    }
                }
            }
            currentPage = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void initBoughtService() {
        broughtAdapter = new MineServiceAdapter(recyclerViewBoughtService, new MineServiceAdapter.Listener() {
            @Override
            public void onClick(int pos) {
                selectService(pos);
            }
        });

        recyclerViewBoughtService.setAdapter(broughtAdapter);
        recyclerViewBoughtService.setLayoutManager(new GridLayoutManager(getActivity(), 4));
    }

    private void selectService(int pos) {
        List<ServiceModel> list = viewModel.boughtServiceList.getValue();
        if (list != null && pos < list.size()) {
            ServiceModel model = list.get(pos);
            int size = model.companies.size();
            if (size == 0) {
                showToast("抱歉，没有提供服务的公司！");
            } else if (size == 1) {
                CompanyModel companyModel = model.companies.get(0);
                viewModel.checkAuth(companyModel);
            } else {
                showCompanyList(model.companies);
            }
        }
    }

    private void showCompanyList(final List<CompanyModel> companyModels) {
        List<String> list = new ArrayList<>();
        for (CompanyModel model : companyModels) {
            list.add(model.name);
        }
        new AlertDialog.Builder(getActivity()).setTitle("请选择服务公司")
                .setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CompanyModel companyModel = companyModels.get(i);
                        viewModel.checkAuth(companyModel);
                    }
                }).create().show();
    }

    private void initDataAdapter() {
        dataAdapter = new MineAdapter(recyclerViewData, new MineAdapter.Listener() {
            @Override
            public void onTypeClick(int type) {
                switch (type) {
                    case MineCardModel.BP:
                        long pwTime = SaveUtils.getLastPwTime();
                        startActivity(HistoryHealthActivity.getCallingIntent(getActivity(), SaveUtils.getUserId(), viewModel.isMan(), pwTime));
                        break;
                    case MineCardModel.ABNORMAL:
                        startActivity(ExceptionRecordActivity.getCallingIntent(getActivity(), SaveUtils.getUserId()));
                        break;
                    case MineCardModel.MEDICATION_PLAN:
                        startActivity(MedicationPlanActivity.getCallingIntent(getActivity()));
                        break;
                    case MineCardModel.ECG:
                        long ecgTime = SaveUtils.getLastEcgTime();
                        if (ecgTime == 0){
                            ecgTime = System.currentTimeMillis();
                        }
                        startActivity(HistoryEcgActivity.getCallingIntent(getActivity(), SaveUtils.getUserId(),ecgTime));
                        break;
                    case MineCardModel.SPORT:
                        startActivity(SportActivity.getCallingIntent(getActivity()));
                        break;
                    case MineCardModel.WEEKLY_REPORT:
                        startActivity(HistoryWeeklyReportActivity.getCallingIntent(getActivity()));
                        break;
                    case MineCardModel.KNOWLEDGE:
                        startActivity(HealthKnowledgeListActivity.getCallingIntent(getActivity()));
                        break;
                    default:
                }
            }
        });
        recyclerViewData.setAdapter(dataAdapter);
        recyclerViewData.setLayoutManager(new GridLayoutManager(getActivity(), 4));

    }

    private void initToolAdapter() {
        toolAdapter = new MineAdapter(recyclerViewTool, new MineAdapter.Listener() {
            @Override
            public void onTypeClick(int type) {
                switch (type) {
                    case MineCardModel.FONT_SIZE:
                        startActivityForResult(SetFontSizeActivity.getCallingIntent(getActivity()), REQUEST_TEXT_SIZE);
                        break;
                    case MineCardModel.DISEASE_HISTORY:
                        startActivity(PersonalInfoDiseaseUpdateActivity.getCallingIntent(getActivity()));
                        break;
                    case MineCardModel.ALARM:
                        startActivity(BraceletReminderActivity.getCallingIntent(getActivity()));
                        break;
                    case MineCardModel.WEIGHT:
                        startActivity(PersonalInfoActivity.getCallingIntent(getActivity()));
                        break;
                    default:
                }
            }
        });
        recyclerViewTool.setAdapter(toolAdapter);
        recyclerViewTool.setLayoutManager(new GridLayoutManager(getActivity(), 4));
    }

    @OnClick(R.id.layout_bind)
    public void braceletInfo() {
        startActivity(BraceletInfoActivity.getCallingIntent(getActivity()));
    }

    @OnClick(R.id.layout_un_bind)
    public void braceletPair() {
        startActivity(BraceletPairActivity.getCallingIntent(getActivity()));
    }

    @OnClick({R.id.iv_qq, R.id.tv_qq})
    public void bindQQ() {
        LoginUtils.loginQQ();
    }

    @OnClick({R.id.iv_wechat, R.id.tv_wechat})
    public void bindWx() {
        if (getActivity() == null) {
            return;
        }
        IWXAPI api = ((MyApplication) getActivity().getApplication()).getWXApi();
        if (!api.isWXAppInstalled()) {
            showToast(getString(R.string.not_install_wechat));
            return;
        }
        LoginUtils.loginWeChat();
    }

    @OnClick(R.id.iv_setting)
    void setting() {
        startActivity(SettingActivity.getCallingIntent(getActivity()));
    }

    @OnClick({R.id.iv_avatar, R.id.tv_name})
    void personal() {
        startActivity(PersonalInfoActivity.getCallingIntent(getActivity()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        barViews = null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            viewModel.initView();
            viewModel.initNetView();
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
        if (!SaveUtils.doesGuideCheckBandWatched() && viewModel.isPaired() && !isHidden() && getActivity() != null) {
            guidePopupWindow = new GuideMePopupWindow(getActivity());
            guidePopupWindow.setOutsideTouchable(true);
            guidePopupWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
            guidePopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
        }
    }

    private void initObs() {
        viewModel.dataList.observe(this, mineCardModels -> dataAdapter.setDataList(mineCardModels));
        viewModel.toolList.observe(this, mineCardModels -> toolAdapter.setDataList(mineCardModels));
        viewModel.name.observe(this, s -> tvName.setText(s));
        viewModel.qq.observe(this, s -> tvQQ.setText(s));
        viewModel.wechat.observe(this, s -> tvWechat.setText(s));
        viewModel.avatar.observe(this, s -> ViewUtils.loadImageUsingGlide(getActivity(), R.drawable.avatar_default, s, ivAvatar, false));
        viewModel.isPaired.observe(this, isPaired -> {
            layoutBind.setVisibility(isPaired ? View.VISIBLE : View.GONE);
            layoutUnBind.setVisibility(isPaired ? View.GONE : View.VISIBLE);
        });
        viewModel.syncTime.observe(this, s -> tvSyncTime.setText(s));

        viewModel.boughtServiceList.observe(this, serviceModels -> {
            recyclerViewBoughtService.setVisibility(serviceModels.isEmpty() ? View.GONE : View.VISIBLE);
            broughtAdapter.setDataList(serviceModels);
        });
//        viewModel.needAuthCompany.observe(this, serviceCompanyModel -> new AlertDialog.Builder(getActivity())
//                .setMessage("是否授权 " + serviceCompanyModel.name + " 获取您的信息")
//                .setPositiveButton("是的", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        viewModel.getAuthorCode(serviceCompanyModel);
//                    }
//                }).setNegativeButton(R.string.cancel, null)
//                .create().show());
        viewModel.needAuthCompany.observe(this, new Observer<CompanyModel>() {
            @Override
            public void onChanged(CompanyModel companyModel) {
                EventBus.getDefault().postSticky(companyModel);
                startActivity(CompanyServiceAuthActivity.getCallingIntent(getActivity()));
            }
        });
        viewModel.pageUrl.observe(this, s -> startActivity(CompanyServiceActivity.getCallingIntent(getActivity(), s, SaveUtils.getUserId())));
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.e("onResume mine");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        viewModel.initNetView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void setRead(UnreadMessageEvent event) {
        viewModel.initView();
    }

    public void updateUnread() {
        viewModel.initDataList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bindInfoUpdate(BindInfoUpdate bindInfoUpdate) {
        viewModel.initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_mine;
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
    public void loginEvent(LoginOtherEvent event) {
        if (event.type == NetConstant.QQ_LOGIN) {
            if (event.status == LoginOtherEvent.FAIL) {
                showToast(getString(R.string.qq_bind_fail));
            } else if (event.status == LoginOtherEvent.SUCCESS) {
                viewModel.bindQQ(event.openId, event.unionId, event.token, event.name, event.avatarUrl);
            }
        } else if (event.type == NetConstant.WE_CHAT_LOGIN) {
            if (event.status == LoginOtherEvent.FAIL) {
                showToast(getString(R.string.wechat_bind_fail));
            } else if (event.status == LoginOtherEvent.SUCCESS) {
                viewModel.bindWeChat(event.openId, event.unionId, event.token, event.name, event.avatarUrl);
            }
        }
    }
}
