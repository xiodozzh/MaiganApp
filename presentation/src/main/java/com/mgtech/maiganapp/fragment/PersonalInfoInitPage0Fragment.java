package com.mgtech.maiganapp.fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.PersonalInfoInitViewModel;

import java.util.Date;
import java.util.Objects;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class PersonalInfoInitPage0Fragment extends BaseFragment<PersonalInfoInitViewModel> {
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.iv_female)
    ImageView ivFemale;
    @Bind(R.id.iv_male)
    ImageView ivMale;
    private boolean haveSetGender;

    @Override
    protected void init(Bundle savedInstanceState) {
        initObs();
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.name.set(etName.getText().toString().trim());
                checkValid();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void checkValid() {
        viewModel.enableStep0.set(etName.getText().length() != 0 &&
                !TextUtils.isEmpty(viewModel.birthString.get()) && haveSetGender);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_personal_info_page0;
    }

    private void initObs() {
        viewModel.updatePage0.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
            }
        });
    }

    @OnClick(R.id.btn_next)
    void nextStep() {
        viewModel.page.setValue(1);
    }

    @OnClick({R.id.iv_male,R.id.iv_female})
    void setGender(View view){
        if (view.getId() == R.id.iv_female){
            viewModel.setMale(false);
            ivFemale.setImageResource(R.drawable.activity_personal_info_init_female_select);
            ivMale.setImageResource(R.drawable.activity_personal_info_init_male);
        }else{
            viewModel.setMale(true);
            ivMale.setImageResource(R.drawable.activity_personal_info_init_male_select);
            ivFemale.setImageResource(R.drawable.activity_personal_info_init_female);
        }
        haveSetGender = true;
        checkValid();
    }

    @OnClick(R.id.layout_birth)
    void setBirth() {
        ViewUtils.hideKeyboard(getActivity(),etName);
        TimePickerView pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                viewModel.setCalendar(date);
                checkValid();
            }
        })
                .setDate(viewModel.getCalendar())
                .setCancelColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimary))
                .setSubmitColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .setSubmitText(getString(R.string.submit))
                .setCancelText(getString(R.string.cancel))
                .build();
        pvTime.show();
    }

    @Override
    protected PersonalInfoInitViewModel initViewModel() {
        return ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PersonalInfoInitViewModel.class);
    }
}
