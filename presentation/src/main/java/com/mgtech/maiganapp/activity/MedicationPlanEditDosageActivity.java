package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.ActivityMedicationPlanEditDosageAdapter;
import com.mgtech.maiganapp.data.event.DosageResultEvent;
import com.mgtech.maiganapp.data.event.EditDosageEvent;
import com.mgtech.maiganapp.data.model.MedicationPlanModel;
import com.mgtech.maiganapp.viewmodel.MedicationPlanEditDosageViewModel;
import com.mgtech.maiganapp.widget.ActivityMedicationPlanEditDosageLayout;
import com.mgtech.maiganapp.window.SetDosageAndTimePopupWindow;
import com.mgtech.maiganapp.window.SetDosageCycleDaysWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class MedicationPlanEditDosageActivity extends BaseActivity<MedicationPlanEditDosageViewModel> {
    @Bind(R.id.root)
    View root;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private static final String INTENT_EDIT_DOSAGE = "editDosage";
    private ActivityMedicationPlanEditDosageAdapter adapter;

    private Calendar calendar = Calendar.getInstance();
    private java.text.DateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public static Intent getCallingIntent(Context context, boolean hasData) {
        Intent intent = new Intent(context, MedicationPlanEditDosageActivity.class);
        intent.putExtra(INTENT_EDIT_DOSAGE, hasData);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        initRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getInitData(EditDosageEvent editDosageEvent) {
        EventBus.getDefault().removeStickyEvent(editDosageEvent);
        SparseArray<List<MedicationPlanModel.DosageItem>> list = editDosageEvent.getList();
        if (list != null && list.size() != 0) {
            viewModel.initWithEventData(editDosageEvent.getList());
        } else {
            viewModel.initWithDefaultData();
        }

        renderData(true);
    }


    private void initRecyclerView() {
        adapter = new ActivityMedicationPlanEditDosageAdapter(recyclerView, new ActivityMedicationPlanEditDosageAdapter.Listener() {
            @Override
            public void add(int index, ActivityMedicationPlanEditDosageLayout layout) {
                addItem(index, layout);
            }

            @Override
            public void update(int index, int itemNumber, ActivityMedicationPlanEditDosageLayout layout) {
                updateItem(index, itemNumber, layout);
            }

            @Override
            public void setCycleDay() {
                repeat();
            }

            @Override
            public void remove(int day, int itemIndex, ActivityMedicationPlanEditDosageLayout layout) {
                removeItem(day, itemIndex, layout);
            }
        });
        adapter.setDosage(viewModel.dosageNumberString);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void renderData(boolean notifyAdapter) {
        adapter.setDosage(viewModel.dosageNumberString);
        adapter.setData(viewModel.list, notifyAdapter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_medication_plan_edit_dosage;
    }

    @OnClick(R.id.btn_back)
    void back() {
//        finish();
        submit();
    }

    @Override
    public void onBackPressed() {
        submit();
//        super.onBackPressed();
    }

    public void submit() {
        EventBus.getDefault().postSticky(new DosageResultEvent(viewModel.list));
        finish();
    }

    void repeat() {
        SetDosageCycleDaysWindow popupWindow = new SetDosageCycleDaysWindow(this,
                new SetDosageCycleDaysWindow.Callback() {
                    @Override
                    public void submit(int dayNumber) {
                        viewModel.setDosageCycleDays(dayNumber);
                        adapter.setDosage(viewModel.dosageNumberString);
                        renderData(true);
                    }
                }, viewModel.list.size());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
        backgroundAlpha(0.6f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });
    }

    /**
     * 添加某天的一项
     *
     * @param index         天数
     * @param layoutDosages 点击的layout
     */
    private void addItem(final int index, final ActivityMedicationPlanEditDosageLayout layoutDosages) {
        Calendar calendar = Calendar.getInstance();
        int size = viewModel.list.get(index).size();
        calendar.set(Calendar.HOUR_OF_DAY, 8 + size * 4);
        calendar.set(Calendar.MINUTE, 0);
        SetDosageAndTimePopupWindow popupWindow = new SetDosageAndTimePopupWindow(this,
                new SetDosageAndTimePopupWindow.Callback() {

                    @Override
                    public void submit(float data, int unitIndex, long time) {
                        MedicationPlanModel.DosageItem item = new MedicationPlanModel.DosageItem();
                        item.dosage = data;
                        item.time = DateFormat.format("HH:mm", time).toString();
                        item.dosageUnitType = unitIndex;
                        viewModel.list.get(index).add(item);
                        layoutDosages.addData(item);
                        renderData(false);
                    }
                }, 1, 0, calendar.getTimeInMillis());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
        backgroundAlpha(0.6f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });
    }

    /**
     * 更新某一项
     *
     * @param index         天数
     * @param itemNumber    项序号
     * @param layoutDosages 点击的layout
     */
    private void updateItem(final int index, final int itemNumber,
                            final ActivityMedicationPlanEditDosageLayout layoutDosages) {
        final MedicationPlanModel.DosageItem item = viewModel.list.get(index).get(itemNumber);
        try {
            Log.i(TAG, "updateItem: " + format.format(calendar.getTime()));
            Date date = format.parse(item.time);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SetDosageAndTimePopupWindow popupWindow = new SetDosageAndTimePopupWindow(this,
                new SetDosageAndTimePopupWindow.Callback() {

                    @Override
                    public void submit(float data, int unitIndex, long time) {
                        item.dosage = data;
                        item.time = DateFormat.format("HH:mm", time).toString();
                        item.dosageUnitType = unitIndex;
                        layoutDosages.update(itemNumber, item);
                        renderData(false);
                    }
                }, item.dosage, item.dosageUnitType, calendar.getTimeInMillis());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
        backgroundAlpha(0.6f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
                layoutDosages.update(itemNumber, item);
                renderData(false);
            }
        });
    }

    /**
     * 删除某天的某一项
     *
     * @param day       天数
     * @param itemIndex 项序号
     * @param layout    点击的layout
     */
    private void removeItem(int day, int itemIndex, ActivityMedicationPlanEditDosageLayout layout) {
        viewModel.removeData(day, itemIndex);
        layout.removeData(itemIndex);
        renderData(false);
    }


}
