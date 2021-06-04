package com.mgtech.maiganapp.fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.PersonalInfoInitViewModel;

import java.util.Objects;

import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class PersonalInfoInitPage1Fragment extends BaseFragment<PersonalInfoInitViewModel> {

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_personal_info_page1;
    }


    @OnClick(R.id.btn_next)
    void nextStep() {
        viewModel.page.setValue(2);
    }


    @OnClick(R.id.layout_height)
    void setHeight() {
        OptionsPickerView<String> optionsPickerView = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                viewModel.setHeight(options1);
                viewModel.enableStep1.set(!TextUtils.isEmpty(viewModel.weightString.get()));
            }
        })
                .setCancelColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimary))
                .setSubmitColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .setSubmitText(getString(R.string.submit))
                .setCancelText(getString(R.string.cancel))
                .build();
        optionsPickerView.setPicker(viewModel.heightList);
        optionsPickerView.setSelectOptions(viewModel.getHeight() - MyConstant.HEIGHT_MIN);
        optionsPickerView.show();
    }

    @OnClick(R.id.layout_weight)
    void setWeight() {
        OptionsPickerView<String> optionsPickerView = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                viewModel.setWeight(options1);
                viewModel.enableStep1.set(!TextUtils.isEmpty(viewModel.heightString.get()));
            }
        })
                .setCancelColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimary))
                .setSubmitColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .setSubmitText(getString(R.string.submit))
                .setCancelText(getString(R.string.cancel))
                .build();
        optionsPickerView.setPicker(viewModel.weightList);
        optionsPickerView.setSelectOptions(viewModel.getWeight() - MyConstant.WEIGHT_MIN);
        optionsPickerView.show();
    }

    @Override
    protected PersonalInfoInitViewModel initViewModel() {
        return ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PersonalInfoInitViewModel.class);
    }


}
