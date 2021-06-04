package com.mgtech.maiganapp.fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.Observable;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.ActivityPersonalInfoInitAdapter;
import com.mgtech.maiganapp.viewmodel.PersonalInfoInitViewModel;

import java.util.Objects;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class PersonalInfoInitUnnecessaryPageFragment extends BaseFragment<PersonalInfoInitViewModel> {
    @Bind(R.id.recyclerView_bp)
    RecyclerView recyclerViewBp;
    @Bind(R.id.recyclerView_disease)
    RecyclerView recyclerViewDisease;
    @Bind(R.id.recyclerView_organ)
    RecyclerView recyclerOrgan;
    @Bind(R.id.recyclerView_other)
    RecyclerView recyclerOther;
    @Bind(R.id.layout_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.layout_refresh_error)
    SwipeRefreshLayout refreshErrorLayout;
    private ActivityPersonalInfoInitAdapter bpAdapter;
    private ActivityPersonalInfoInitAdapter diseaseAdapter;
    private ActivityPersonalInfoInitAdapter organAdapter;
    private ActivityPersonalInfoInitAdapter otherAdapter;

    @Override
    protected void init(Bundle savedInstanceState) {
        bpAdapter = new ActivityPersonalInfoInitAdapter(recyclerViewBp, viewModel.highBloodPressureList, viewModel.highBpType);
        diseaseAdapter = new ActivityPersonalInfoInitAdapter(recyclerViewDisease, viewModel.diseaseHistoryList, viewModel.diseaseType);
        organAdapter = new ActivityPersonalInfoInitAdapter(recyclerOrgan, viewModel.organDamageList, viewModel.organType);
        otherAdapter = new ActivityPersonalInfoInitAdapter(recyclerOther, viewModel.otherRiskFactorsList, viewModel.otherType);

        recyclerViewBp.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewBp.setAdapter(bpAdapter);

        recyclerViewDisease.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewDisease.setAdapter(diseaseAdapter);

        recyclerOrgan.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerOrgan.setAdapter(organAdapter);

        recyclerOther.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerOther.setAdapter(otherAdapter);
        initObs();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getDiseases();
            }
        });
        refreshErrorLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.getDiseases();
            }
        });
    }

    private void initObs(){
        viewModel.loadDiseasesSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.loadDiseasesSuccess.get()) {
                    bpAdapter.notifyDataSetChanged();
                    diseaseAdapter.notifyDataSetChanged();
                    organAdapter.notifyDataSetChanged();
                    otherAdapter.notifyDataSetChanged();
                }
            }
        });
        viewModel.loadingDiseases.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                refreshLayout.setRefreshing(viewModel.loadingDiseases.get());
                refreshErrorLayout.setRefreshing(viewModel.loadingDiseases.get());
            }
        });
        viewModel.saveSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                showToast(getString(R.string.set_success));
            }
        });
    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_personal_info_page_unnecessary;
    }


    @OnClick(R.id.btn_commit)
    void commit(){
        viewModel.savePersonalInfo();
    }

    @Override
    protected PersonalInfoInitViewModel initViewModel() {
        return ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PersonalInfoInitViewModel.class);
    }

}
