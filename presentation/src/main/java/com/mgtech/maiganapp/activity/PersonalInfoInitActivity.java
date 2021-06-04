package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.fragment.PersonalInfoInitNecessaryPageFragment;
import com.mgtech.maiganapp.fragment.PersonalInfoInitUnnecessaryPageFragment;
import com.mgtech.maiganapp.viewmodel.PersonalInfoInitViewModel;
import com.mgtech.maiganapp.window.PersonalInfoAlertPopupWindow;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 * 设置基础信息 身高、体重、性别、年龄,病史等
 */
public class PersonalInfoInitActivity extends BaseActivity<PersonalInfoInitViewModel> {
    private static final String USER_ID = "userId";
    @Bind(R.id.root)
    View root;
    private Fragment[] fragments;
    private int lastPage = 0;
    private PersonalInfoAlertPopupWindow popupWindow;

    public static Intent getCallingIntent(Context context,String userId) {
        Intent intent = new Intent(context, PersonalInfoInitActivity.class);
        intent.putExtra(USER_ID,userId);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        String userId = getIntent().getStringExtra(USER_ID);
        if (!TextUtils.isEmpty(userId)){
            viewModel.setUserId(userId);
        }
        viewModel.saveSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (TextUtils.isEmpty(SaveUtils.getUserId(getApplicationContext()))){
                    Intent intent = LoginActivity.getCallingIntent(PersonalInfoInitActivity.this,false);
                    startActivity(intent);
                }else {
                    Intent intent = MainActivity.getCallingIntent(PersonalInfoInitActivity.this);
                    startActivity(intent);
                }
                showShortToast(getString(R.string.set_success));
                finish();
            }
        });
        initFragment();
//        viewModel.page.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable sender, int propertyId) {
//                int curr = viewModel.page.get();
//                switchPage(curr,lastPage);
//                lastPage = curr;
//            }
//        });
        viewModel.page.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (viewModel.page.getValue() == null){
                    return;
                }
                int curr = viewModel.page.getValue();
                switchPage(curr,lastPage);
                lastPage = curr;
            }
        });
        viewModel.getDiseases();
    }

    @Override
    protected void onResume() {
        super.onResume();
        root.post(new Runnable() {
            @Override
            public void run() {
                showAlert();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelAlert();
    }

    private void showAlert(){
//        cancelAlert();
        popupWindow = new PersonalInfoAlertPopupWindow(this);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(root, Gravity.CENTER,0,0);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
//        popupWindow.showAsDropDown(root);
    }

    private void cancelAlert(){
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        fragments = new Fragment[2];
        fragments[0] = new PersonalInfoInitNecessaryPageFragment();
        fragments[1] = new PersonalInfoInitUnnecessaryPageFragment();
        transaction.add(R.id.container, fragments[0]);
        transaction.add(R.id.container, fragments[1]);
        transaction.hide(fragments[1]);
        transaction.commit();
    }

    private void switchPage(int currPage,int lastPage) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (currPage >= lastPage) {
            transaction.setCustomAnimations(
                    R.anim.right_in,
                    R.anim.left_out
            );
        }else{
            transaction.setCustomAnimations(
                    R.anim.left_in,
                    R.anim.right_out
            );
        }
        transaction.hide(fragments[lastPage]);
        transaction.show(fragments[currPage]);
        transaction.commit();
    }

    @OnClick(R.id.btn_back)
    void back() {
//        viewModel.page.set(viewModel.page.get() - 1);
        if (viewModel.page.getValue() == null){
            return;
        }
        viewModel.page.setValue(viewModel.page.getValue() - 1);
    }

    @Override
    public void onBackPressed() {
        if (viewModel.page.getValue() == null){
            return;
        }
        if (viewModel.page.getValue()==0){
            finish();
        }else {
            back();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_personal_info_init;
    }

}
