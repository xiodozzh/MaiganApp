package com.mgtech.maiganapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.Observable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.SelectMedicineSearchAdapter;
import com.mgtech.maiganapp.data.model.IMedicineModel;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.SelectMedicineSearchViewModel;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */

public class SelectMedicineSearchActivity extends BaseActivity<SelectMedicineSearchViewModel> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.layout_refresh_error)
    SwipeRefreshLayout errorLayout;
    private SelectMedicineSearchAdapter adapter;
    public static final String MEDICINE_NAME = "medicineName";
    public static final String MEDICINE_GUID = "medicineGuid";
    public static final String MEDICINE_DOSAGE = "medicineDosage";
    public static final String MEDICINE_CUSTOM = "medicineCustom";

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SelectMedicineSearchActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        viewModel.loadSuccess.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(androidx.databinding.Observable observable, int i) {
                adapter.notifyDataSetChanged();
            }
        });
        viewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                refreshLayout.setRefreshing(loading);
                errorLayout.setRefreshing(loading);
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

    private void initView() {
        adapter = new SelectMedicineSearchAdapter(viewModel.list, recyclerView);
        adapter.setListener(new SelectMedicineSearchAdapter.Listener() {
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
                addMedicine(etSearch.getText().toString());
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //执行对应的操作
                    ViewUtils.hideKeyboard(SelectMedicineSearchActivity.this,etSearch);
                    return true;
                }
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick(R.id.iv_clear)
    void clear(){
        etSearch.setText("");
    }

    private void addMedicine(String name){
        ViewUtils.showEditDialog(this, getString(R.string.select_medicine_input_medicine), name,
                getString(R.string.select_medicine_input_medicine_name), "", "",
                InputType.TYPE_CLASS_TEXT, new ViewUtils.EditCallback() {
                    @Override
                    public void onEdit(String result) {
                        viewModel.addMedicine(result);
                    }
                });
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
        return R.layout.activity_select_medicine_search;
    }





}
