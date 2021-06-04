package com.mgtech.maiganapp.fragment;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.ActivityPersonalInfoInitAdapter;
import com.mgtech.maiganapp.viewmodel.PersonalInfoInitViewModel;

import java.util.Objects;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class PersonalInfoInitPage2Fragment extends BaseFragment<PersonalInfoInitViewModel> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_next)
    TextView btnNext;
    private ActivityPersonalInfoInitAdapter bpAdapter;
    private int page = PAGE_HIGH_BP;
    private static final String PAGE = "page";
    private static final int PAGE_HIGH_BP = 2;
    private static final int PAGE_DISEASE = 3;
    private static final int PAGE_ORGAN = 4;
    private static final int PAGE_OTHER = 5;

    public static Fragment newInstance(int page) {
        PersonalInfoInitPage2Fragment fragment = new PersonalInfoInitPage2Fragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE, page);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            page = bundle.getInt(PAGE);
        }
        switch (page) {
            case PAGE_HIGH_BP:
                bpAdapter = new ActivityPersonalInfoInitAdapter(recyclerView, viewModel.highBloodPressureList, viewModel.highBpType);
                tvTitle.setText("高血压类型");
                break;
            case PAGE_DISEASE:
                bpAdapter = new ActivityPersonalInfoInitAdapter(recyclerView, viewModel.diseaseHistoryList, viewModel.diseaseType);
                tvTitle.setText("病史清单");
                break;
            case PAGE_ORGAN:
                tvTitle.setText("靶器官损伤");
                bpAdapter = new ActivityPersonalInfoInitAdapter(recyclerView, viewModel.organDamageList, viewModel.organType);
                break;
            case PAGE_OTHER:
                tvTitle.setText("其它");
                bpAdapter = new ActivityPersonalInfoInitAdapter(recyclerView, viewModel.otherRiskFactorsList, viewModel.otherType);
                btnNext.setText("完成");
                break;
            default:
                bpAdapter = new ActivityPersonalInfoInitAdapter(recyclerView, viewModel.highBloodPressureList, viewModel.highBpType);

        }
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(bpAdapter);
    }


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_personal_info_page2;
    }


    @OnClick(R.id.btn_next)
    void nextStep() {
        if (page == PAGE_OTHER){
            viewModel.savePersonalInfo();
        }else {
            viewModel.page.setValue(page + 1);
        }
    }

    @Override
    protected PersonalInfoInitViewModel initViewModel() {
        return ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PersonalInfoInitViewModel.class);
    }

}
