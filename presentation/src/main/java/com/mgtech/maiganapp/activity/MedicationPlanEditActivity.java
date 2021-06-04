package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.ActivityMedicationPlanEditAdapter;
import com.mgtech.maiganapp.data.event.DosageResultEvent;
import com.mgtech.maiganapp.data.event.EditDosageEvent;
import com.mgtech.maiganapp.data.model.MedicationPlanModel;
import com.mgtech.maiganapp.viewmodel.MedicationPlanEditViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class MedicationPlanEditActivity extends BaseActivity<MedicationPlanEditViewModel> {
    private static final int SET_NAME = 110;
    private static final int SET_DOSAGE = 120;
    private static final int SET_RANGE = 130;
    private static final String CREATE = "isCreate";

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private ActivityMedicationPlanEditAdapter adapter;

    public static Intent getCallingIntent(Context context,boolean isCreate ) {
        Intent intent = new Intent(context, MedicationPlanEditActivity.class);
        intent.putExtra(CREATE,isCreate);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        viewModel.setCreateNew(getIntent().getBooleanExtra(CREATE,false));
        adapter = new ActivityMedicationPlanEditAdapter(recyclerView, new ActivityMedicationPlanEditAdapter.Listener() {
            @Override
            public void setName() {
                startActivityForResult(SelectMedicineActivity.getCallingIntent(MedicationPlanEditActivity.this), SET_NAME);
            }

            @Override
            public void setCycle() {
                startActivityForResult(MedicationPlanEditTimeActivity.getCallingIntent(MedicationPlanEditActivity.this,
                        viewModel.getStartTimeInMillis(), viewModel.getEndTimeInMillis()), SET_RANGE);
            }

            @Override
            public void setDosage() {
                EventBus.getDefault().postSticky(new EditDosageEvent(viewModel.list));
                startActivityForResult(MedicationPlanEditDosageActivity.getCallingIntent(
                        MedicationPlanEditActivity.this, true), SET_DOSAGE);
            }

            @Override
            public void stopPlan() {
                showStopDialog();
            }
        },viewModel.list,!viewModel.isCreate && !viewModel.isStopped);
        initObs();
        setHeader();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (viewModel.isCreate) {
            viewModel.initCreate();
        }
    }

    private void showStopDialog(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.caution)
                .setMessage(R.string.activity_medication_plan_sure_to_delete)
                .setNegativeButton(R.string.cancel,null)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.stopPlan();
                    }
                }).create().show();
    }


    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void getData(MedicationPlanModel model){
        EventBus.getDefault().removeStickyEvent(model);
        viewModel.initEdit(model);
    }

    private void initObs() {
        viewModel.headerUpdate.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                setHeader();
                adapter.notifyItemChanged(0);
            }
        });
        viewModel.listUpdate.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                setHeader();
                adapter.setHasStopBtn(!viewModel.isCreate && !viewModel.isStopped);
                adapter.notifyDataSetChanged();
            }
        });
        viewModel.saveSuccess.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                setResult(RESULT_OK);
                finish();
            }
        });
        viewModel.stopSuccess.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void setHeader() {
        adapter.setName(viewModel.getMedicineName());
        adapter.setMedicineCycleDays(viewModel.getMedicineCycleDays());
        adapter.setRangeTime(viewModel.getMedicineTimeRange());
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_medication_plan_edit;
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_NAME) {
            if (resultCode == RESULT_OK && data != null) {
                viewModel.setMedicine(data.getStringExtra(SelectMedicineActivity.MEDICINE_GUID),
                        data.getStringExtra(SelectMedicineActivity.MEDICINE_NAME),data.getBooleanExtra(SelectMedicineActivity.MEDICINE_CUSTOM,false));
            }
        } else if (requestCode == SET_RANGE) {
            if (resultCode == RESULT_OK && data != null) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 1);
                start.setTimeInMillis(data.getLongExtra(MedicationPlanEditTimeActivity.START, start.getTimeInMillis()));
                end.setTimeInMillis(data.getLongExtra(MedicationPlanEditTimeActivity.END, end.getTimeInMillis()));
                viewModel.setTimeRange(start, end);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onDosageResultReceive(DosageResultEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        if (event.getList() != null) {
            viewModel.setList(event.getList());
        }
    }

    @OnClick(R.id.btn_done)
    public void submit(){
        viewModel.submit();
    }
}
