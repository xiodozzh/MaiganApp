package com.mgtech.maiganapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.SelectMedicineAdapter;
import com.mgtech.maiganapp.data.model.IMedicineModel;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.SelectMedicineViewModel;
import com.mgtech.maiganapp.widget.SideBarView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */

public class SelectMedicineActivity extends BaseActivity<SelectMedicineViewModel> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.side_bar)
    SideBarView sideBarView;
    @Bind(R.id.letter)
    TextView tvLetter;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.layout_refresh_error)
    SwipeRefreshLayout errorLayout;
    private SelectMedicineAdapter adapter;
    private boolean move;
    private int mIndex;
    private LinearLayoutManager mLinearLayoutManager;
    private static Handler mHandler = new Handler();
    public static final String MEDICINE_NAME = "medicineName";
    public static final String MEDICINE_CUSTOM = "medicineCustom";
    public static final String MEDICINE_GUID = "medicineGuid";
    public static final String MEDICINE_DOSAGE = "medicineDosage";
    private static final int REQUEST_SEARCH = 291;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SelectMedicineActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        sideBarView.setOnTouchingLetterChangedListener(new SideBarView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                mIndex = adapter.getPosition(s);
                moveToPosition(mIndex);
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText(s);
                mHandler.removeCallbacks(hideLetterViewRunnable);
                mHandler.postDelayed(hideLetterViewRunnable, 1000);
            }
        });
        viewModel.loadSuccess.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(androidx.databinding.Observable observable, int i) {
                adapter.notifyDataSetChanged();
            }
        });
        viewModel.loading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                refreshLayout.setRefreshing(viewModel.loading.get());
                errorLayout.setRefreshing(viewModel.loading.get());
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getAllMedicine();
            }
        });
        errorLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getAllMedicine();
            }
        });
        initView();
        viewModel.getAllMedicine();
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @OnClick(R.id.btn_add)
    void add(){
        ViewUtils.showEditDialog(this, getString(R.string.select_medicine_input_medicine), "",
                getString(R.string.select_medicine_input_medicine_name), "", "",
                InputType.TYPE_CLASS_TEXT, new ViewUtils.EditCallback() {
                    @Override
                    public void onEdit(String result) {
                        viewModel.addMedicine(result);
                    }
                });
    }

    private Runnable hideLetterViewRunnable = new Runnable() {
        @Override
        public void run() {
            tvLetter.setVisibility(View.GONE);
        }
    };

    private void initView() {
        adapter = new SelectMedicineAdapter(viewModel.list, recyclerView);
        adapter.setListener(new SelectMedicineAdapter.Listener() {
            @Override
            public void onSelect(int position) {
                IMedicineModel.Content model = (IMedicineModel.Content) viewModel.list.get(position);
                Intent intent = new Intent();
                intent.putExtra(MEDICINE_NAME, model.name);
                intent.putExtra(MEDICINE_DOSAGE, model.dosage);
                intent.putExtra(MEDICINE_GUID, model.id);
                intent.putExtra(MEDICINE_CUSTOM, model.custom);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onDelete(int position) {
                deleteMedicine(position);
            }

            @Override
            public void onSearch() {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(recyclerView,0,0,0,0);
                startActivityForResult(SelectMedicineSearchActivity.getCallingIntent(SelectMedicineActivity.this),REQUEST_SEARCH,options.toBundle());
//                overridePendingTransition(R.anim.alpha_in,R.anim.alpha_out);
            }
        });
        recyclerView.setAdapter(adapter);
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerViewListener());
    }

    private void deleteMedicine(final int index){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.caution)
                .setMessage(R.string.select_medicine_whether_to_delete_medicine)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.deleteMedicine(index);
                    }
                }).setNegativeButton(R.string.cancel,null)
                .create();
        dialog.show();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_select_medicine;
    }


    class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //在这里进行第二次滚动
            if (move) {
                move = false;
                //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                if (0 <= n && n < recyclerView.getChildCount()) {
                    //获取要置顶的项顶部离RecyclerView顶部的距离
                    int top = recyclerView.getChildAt(n).getTop();
                    //最后的移动
                    recyclerView.scrollBy(0, top);
                }
            }
        }
    }

    private void moveToPosition(int n) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            recyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = recyclerView.getChildAt(n - firstItem).getTop();
            recyclerView.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            recyclerView.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SEARCH && resultCode == RESULT_OK){
            Intent intent = new Intent();
            if (data != null) {
                intent.putExtra(MEDICINE_NAME, data.getStringExtra(SelectMedicineSearchActivity.MEDICINE_NAME));
                intent.putExtra(MEDICINE_DOSAGE,data.getStringExtra(SelectMedicineSearchActivity.MEDICINE_DOSAGE));
                intent.putExtra(MEDICINE_GUID, data.getStringExtra(SelectMedicineSearchActivity.MEDICINE_GUID));
                intent.putExtra(MEDICINE_CUSTOM, data.getBooleanExtra(SelectMedicineSearchActivity.MEDICINE_CUSTOM,false));
                setResult(RESULT_OK, intent);
            }
            finish();
        }
    }
}
