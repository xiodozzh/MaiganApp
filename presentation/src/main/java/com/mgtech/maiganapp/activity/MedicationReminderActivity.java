package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.MedicationReminderAdapter;
import com.mgtech.maiganapp.data.model.MedicationReminderModel;
import com.mgtech.maiganapp.viewmodel.MedicationReminderViewModel;
import com.mgtech.maiganapp.window.AlertDoubleButtonPopupWindow;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class MedicationReminderActivity extends BaseActivity<MedicationReminderViewModel> implements
        MedicationReminderAdapter.OnButtonClickListener {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @Bind(R.id.layout_refresh_empty)
    SwipeRefreshLayout layoutRefreshEmpty;
    @Bind(R.id.root)
    View root;
    private MedicationReminderAdapter adapter;
    private AlertDoubleButtonPopupWindow alertPopupWindow;

    public static Intent getCallingIntent(Context context){
        return new Intent(context,MedicationReminderActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        adapter = new MedicationReminderAdapter(recyclerView,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initObs();
        layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getReminderList();
            }
        });
        layoutRefreshEmpty.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getReminderList();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getReminderList();
    }

    private void initObs(){
        viewModel.getDataSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.setData(viewModel.data);
                adapter.notifyDataSetChanged();
            }
        });
        viewModel.loading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                layoutRefresh.setRefreshing(viewModel.loading.get());
                layoutRefreshEmpty.setRefreshing(viewModel.loading.get());
            }
        });
        viewModel.setSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                adapter.notifyItemChanged(viewModel.changedIndex);
                showShortToast(getString(R.string.set_success));
            }
        });
    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_medication_reminder;
    }

    @OnClick(R.id.btn_back)
    void back(){
        onBackPressed();
    }

    @OnClick({R.id.imageView3,R.id.textView2})
    void addPlan(){
        startActivity(MedicationPlanEditActivity.getCallingIntent(this,true));
    }

    @OnClick(R.id.iv_set)
    void setPlan(){
//        startActivity(MedicationPlanEditActivity.getCallingIntent(this,true));
        startActivity(MedicationPlanActivity.getCallingIntent(this));
    }

    @Override
    public void onBackPressed() {
        if (alertPopupWindow != null && alertPopupWindow.isShowing()){
            alertPopupWindow.dismiss();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onIgnoreButtonClick(int index) {
        showPopupWindow(index, getString(R.string.medication_is_cancel), getString(R.string.cancel), getString(R.string.sure), new AlertDoubleButtonPopupWindow.Callback() {
            @Override
            public void onNegative() {
            }

            @Override
            public void onPositive() {
                viewModel.setReminder(index,MedicationReminderModel.IGNORE);
            }
        });
    }

    @Override
    public void onDoneButtonClick(int index) {
        showPopupWindow(index, getString(R.string.medication_is_done), getString(R.string.cancel), getString(R.string.sure), new AlertDoubleButtonPopupWindow.Callback() {
            @Override
            public void onNegative() {

            }

            @Override
            public void onPositive() {
                viewModel.setReminder(index,MedicationReminderModel.TAKEN);
            }
        });
    }

    private void cancelAlert(){
        if (alertPopupWindow != null && alertPopupWindow.isShowing()){
            alertPopupWindow.dismiss();
        }
    }

    private void showPopupWindow(final  int index, String title,String negative,String positive,AlertDoubleButtonPopupWindow.Callback callback){
        cancelAlert();
        alertPopupWindow = new AlertDoubleButtonPopupWindow(this, title, negative, positive, callback);
        alertPopupWindow.setOutsideTouchable(true);
        alertPopupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        alertPopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
        backgroundAlpha(0.6f);
        alertPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });
    }

    @Override
    public void onReset(int index) {
        viewModel.setReminder(index, MedicationReminderModel.DEFAULT);
    }
}
