package com.mgtech.maiganapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mgtech.domain.entity.AppConfigNew;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.ActivityFriendAddAdapter;
import com.mgtech.maiganapp.data.event.FriendAddEvent;
import com.mgtech.maiganapp.data.model.FriendAddItemModel;
import com.mgtech.maiganapp.data.model.IFriendAddModel;
import com.mgtech.maiganapp.utils.PermissionUtils;
import com.mgtech.maiganapp.utils.ShareUtils;
import com.mgtech.maiganapp.viewmodel.FriendAddListViewModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @author zhaixiang
 */
public class FriendAddListActivity extends BaseActivity<FriendAddListViewModel> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.root)
    ViewGroup root;
    private static final int REQUEST_CONTACT = 396;
    private ActivityFriendAddAdapter adapter;

    public static Intent getCallingIntent(Context context){
        return new Intent(context,FriendAddListActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        adapter = new ActivityFriendAddAdapter(recyclerView, new ActivityFriendAddAdapter.Listener() {
            @Override
            public void selectItem(int index) {

                IFriendAddModel iFriendAddModel = viewModel.filterList.get(index);
                if (iFriendAddModel.type != IFriendAddModel.TYPE_HEADER_ITEM || !(iFriendAddModel instanceof FriendAddItemModel)){
                    return;
                }
                FriendAddItemModel model = (FriendAddItemModel) iFriendAddModel;
                if (model.relation == FriendAddItemModel.RELATION_NOT_FOLLOW) {
                    EventBus.getDefault().postSticky(new FriendAddEvent(model));
                    startActivity(FriendAddPageActivity.getCallingIntent(FriendAddListActivity.this));
                }else if (model.relation == FriendAddItemModel.RELATION_NOT_REGISTER){
                    String url = AppConfigNew.getAppUrl(getApplication());
                    if (TextUtils.isEmpty(url)){
                        url = MyConstant.APP_DOWNLOAD_SITE;
                    }
                    ShareUtils.shareUrl(FriendAddListActivity.this,
                            url,
                            getString(R.string.share_app_title),
                            getString(R.string.share_app_content),
                            Wechat.NAME);
                }
            }

            @Override
            public void editTextChange(String text) {
                viewModel.filterPhone(text);
            }


            @Override
            public void onPhoneSearch() {
                viewModel.checkUser(viewModel.editTextModel.editText);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initObs();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        getData();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getData(){
        viewModel.haveAuthToReadContact.set(PermissionUtils.isContactPermissionOpen(this));
        if (viewModel.haveAuthToReadContact.get()){
            viewModel.getFriendList();
        }else{
            viewModel.clearList();
        }
    }


    private void initObs(){
        viewModel.modelList.observe(this, friendAddItemModels -> adapter.setData(friendAddItemModels));
        viewModel.searchSuccess.observe(this, aBoolean -> {
            EventBus.getDefault().postSticky(new FriendAddEvent(viewModel.checkedFriendModel));
            startActivity(FriendAddPageActivity.getCallingIntent(FriendAddListActivity.this));
        });
        viewModel.loading.observe(this, loading -> refreshLayout.setRefreshing(loading));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_friend_add_list;
    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }

    @OnClick(R.id.layout_request_read_auth)
    void requestAuthToReadContract(){
        PermissionUtils.requestReadContactPermission(this,REQUEST_CONTACT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACT) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                PermissionUtils.goToAppDetailSettingIntent(this);
            }
            if (permissions.length > 0 && permissions[0].equals(Manifest.permission.READ_CONTACTS)) {
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // 如果没有相应权限则关闭
                    viewModel.haveAuthToReadContact.set(false);
//                    viewModel.filterList.clear();
//                    viewModel.filterList.setValue(viewModel.empty);
//                    adapter.notifyDataSetChanged();
                }else{
                    getData();
                }
            }
        }
    }
}
