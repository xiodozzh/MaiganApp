package com.mgtech.maiganapp.fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.PersonalInfoInitViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class PersonalInfoInitNecessaryPageFragment extends BaseFragment<PersonalInfoInitViewModel> {
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.tv_male)
    TextView tvMale;
    @Bind(R.id.tv_female)
    TextView tvFemale;
    @Bind(R.id.layout_male)
    CardView cvMale;
    @Bind(R.id.layout_female)
    CardView cvFemale;
    @Bind(R.id.tv_birth)
    TextView tvBirth;
    @Bind(R.id.tv_height)
    TextView tvHeight;
    @Bind(R.id.tv_weight)
    TextView tvWeight;
    private boolean haveSetGender;
    private Calendar startCalendar;
    private Calendar endCalendar;

    @Override
    protected void init(Bundle savedInstanceState) {
        initObs();
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.YEAR, -120);
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
                !TextUtils.isEmpty(viewModel.birthString.get()) && haveSetGender &&
                !TextUtils.isEmpty(viewModel.weightString.get()) &&
                !TextUtils.isEmpty(viewModel.heightString.get()));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_personal_info_page_necessary;
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

    @OnClick({R.id.layout_male, R.id.layout_female})
    void setGender(View view) {
        if (getActivity() == null) {
            return;
        }
        if (view.getId() == R.id.layout_male) {
            viewModel.setMale(true);
            tvMale.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            tvFemale.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            cvMale.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            cvFemale.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        } else {
            viewModel.setMale(false);
            tvMale.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            tvFemale.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            cvMale.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            cvFemale.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        }
        haveSetGender = true;
        checkValid();
    }

    @OnClick(R.id.layout_birth)
    void setBirth() {
        if (getActivity() == null) {
            return;
        }
        ViewUtils.hideKeyboard(getActivity(), etName);
        TimePickerView pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                viewModel.setCalendar(date);
                tvBirth.setText(viewModel.birthString.get());
                tvBirth.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_text));
                checkValid();
            }
        })
                .setDate(viewModel.getCalendar())
                .setCancelColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimary))
                .setSubmitColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .setSubmitText(getString(R.string.submit))
                .setCancelText(getString(R.string.cancel))
                .setRangDate(startCalendar, endCalendar)
                .build();
        pvTime.show();
    }

    @OnClick(R.id.layout_height)
    void setHeight() {
        if (getActivity() == null) {
            return;
        }
//        OptionsPickerView<String> optionsPickerView = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                viewModel.setHeight(options1);
//                tvHeight.setText(viewModel.heightString.get());
//                tvHeight.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_text));
//                checkValid();
//            }
//        })
//                .setCancelColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimary))
//                .setSubmitColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
//                .setSubmitText(getString(R.string.submit))
//                .setCancelText(getString(R.string.cancel))
//                .build();
//        optionsPickerView.setPicker(viewModel.heightList);
//        optionsPickerView.setSelectOptions(viewModel.getHeight() - BleConstant.HEIGHT_MIN);
//        optionsPickerView.show();
        ViewUtils.showSelectHeightDialog(getActivity(), getString(R.string.submit), getString(R.string.cancel),
                Math.round(viewModel.model.height), new ViewUtils.SelectCallback() {
                    @Override
                    public void onSelect(float result) {
                        viewModel.setHeight(result);
                        tvHeight.setText(viewModel.heightString.get());
                        tvHeight.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_text));
                        checkValid();
                    }
                });
    }

    @OnClick(R.id.layout_weight)
    void setWeight() {
        if (getActivity() == null) {
            return;
        }
//        OptionsPickerView<String> optionsPickerView = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                viewModel.setWeight(options1);
//                tvWeight.setText(viewModel.weightString.get());
//                tvWeight.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_text));
//                checkValid();
//            }
//        })
//                .setCancelColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorPrimary))
//                .setSubmitColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
//                .setSubmitText(getString(R.string.submit))
//                .setCancelText(getString(R.string.cancel))
//                .build();
//        optionsPickerView.setPicker(viewModel.weightList);
//        optionsPickerView.setSelectOptions(viewModel.getWeight() - BleConstant.WEIGHT_MIN);
//        optionsPickerView.show();
        ViewUtils.showSelectWeightDialog(getActivity(), getString(R.string.submit), getString(R.string.cancel),
                viewModel.getWeight(), new ViewUtils.SelectCallback() {
                    @Override
                    public void onSelect(float result) {
                        viewModel.setWeight(result);
                        tvWeight.setText(viewModel.weightString.get());
                        tvWeight.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_text));
                        checkValid();
                    }
                });
    }

    @Override
    protected PersonalInfoInitViewModel initViewModel() {
        return ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PersonalInfoInitViewModel.class);
    }
}
