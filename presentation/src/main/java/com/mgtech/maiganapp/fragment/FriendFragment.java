package com.mgtech.maiganapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mgtech.domain.entity.UnreadMessage;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.activity.FriendAddListActivity;
import com.mgtech.maiganapp.activity.FriendDealRequestActivity;
import com.mgtech.maiganapp.activity.FriendInformationActivity;
import com.mgtech.maiganapp.activity.FriendRemindActivity;
import com.mgtech.maiganapp.adapter.FriendRecyclerViewAdapter;
import com.mgtech.maiganapp.data.model.FriendModel;
import com.mgtech.maiganapp.viewmodel.FriendViewModel;
import com.mgtech.maiganapp.widget.UnreadImageView;
import com.mgtech.maiganapp.window.GuideFriendPopupWindow;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class FriendFragment extends BaseFragment<FriendViewModel> {
    private static final int MONITOR_CHANGE = 344;
    private static final int MONITORED_CHANGE = 32;
    @Bind(R.id.root)
    ViewGroup root;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.iv_message)
    UnreadImageView ivMessage;

    private GuideFriendPopupWindow guidePopupWindow;
    private FriendRecyclerViewAdapter recyclerViewAdapter;

    public static FriendFragment newInstance() {
        return new FriendFragment();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initRecyclerView();
        viewModel.friendModelsLiveData.observe(this, new Observer<List<FriendModel>>() {
            @Override
            public void onChanged(List<FriendModel> friendModels) {
                recyclerViewAdapter.setData(friendModels);
            }
        });
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                refreshLayout.setRefreshing(aBoolean);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getRelations();
            }
        });
    }

    public void updateUnread(){
        ivMessage.setUnread(UnreadMessage.getFriendRequestUnreadNumber() > 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getRelations();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            hideGuide();
        } else {
            root.post(new Runnable() {
                @Override
                public void run() {
                    showGuid();
                }
            });
        }
    }

    private void hideGuide() {
        if (guidePopupWindow != null && guidePopupWindow.isShowing()) {
            guidePopupWindow.dismiss();
        }
    }

    private void showGuid() {
        if (!SaveUtils.doesGuideCheckFriendDataWatched() && viewModel.friendModelsLiveData.getValue() != null &&
                !viewModel.friendModelsLiveData.getValue().isEmpty() && getActivity() != null) {
            guidePopupWindow = new GuideFriendPopupWindow(getActivity());
            guidePopupWindow.setOutsideTouchable(false);
            guidePopupWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
            guidePopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
        }
    }


    private void initRecyclerView() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getRelations();
            }
        });
        recyclerViewAdapter = new FriendRecyclerViewAdapter(recyclerView, new FriendRecyclerViewAdapter.Listener() {
            @Override
            public void onPersonInfoClick(int index) {
                List<FriendModel> list = viewModel.friendModelsLiveData.getValue();
                if (list == null || list.size() <= index) {
                    return;
                }
                FriendModel model = list.get(index);
                startActivityForResult(FriendInformationActivity.getCallingIntent(getActivity(),
                        model.getTargetUserId()), MONITOR_CHANGE);
            }

            @Override
            public void onDataClick(int index) {
                List<FriendModel> list = viewModel.friendModelsLiveData.getValue();
                if (list == null || list.size() <= index) {
                    return;
                }
                FriendModel model = list.get(index);
//                if ( model.isReadHisData()) {
                    startActivity(FriendRemindActivity.getCallingIntent(getActivity(), model));
//                }
            }

            @Override
            public void onLongClick(int index) {
                showDeleteRelationDialog(index);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void showDeleteRelationDialog(final int index) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.caution)
                .setMessage(R.string.remove_relationship)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.deleteRelation(index);
                    }
                }).setNegativeButton(R.string.cancel, null).
                        create();
        dialog.show();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_friend;
    }

    @OnClick(R.id.btn_add)
    void addFriend() {
        startActivity(FriendAddListActivity.getCallingIntent(getActivity()));
    }

    @OnClick(R.id.btn_message)
    void haveFriendMessage() {
        startActivityForResult(FriendDealRequestActivity.getCallingIntent(getActivity()), MONITORED_CHANGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            viewModel.getRelations();
        }
    }
}
