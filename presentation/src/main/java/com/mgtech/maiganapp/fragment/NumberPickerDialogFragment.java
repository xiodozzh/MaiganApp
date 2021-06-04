package com.mgtech.maiganapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.View;
import android.view.animation.Animation;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.mgtech.maiganapp.R;

import java.lang.reflect.Method;
import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhaixiang on 2017/1/9.
 * 选择数字
 */
public class NumberPickerDialogFragment extends AppCompatDialogFragment {
    private OnNumberPickListener listener;
    public static final String PARA_TITLE = "text";
    public static final String PARA_MIN_VALUE = "min";
    public static final String PARA_MAX_VALUE = "max";
    public static final String PARA_FLOAT_VALUE = "float";
    public static final String PARA_CURRENT_VALUE = "current";
    public static final String PARA_UNIT = "unit";
    @Bind(R.id.picker_digital)
    NumberPicker pickerDigital;
    @Bind(R.id.picker_float)
    NumberPicker pickerFloat;
    @Bind(R.id.tv_unit)
    TextView tvUnit;
    private int scale;
    private DecimalFormat decimalFormat;

    public static NumberPickerDialogFragment getInstance() {
        return new NumberPickerDialogFragment();
    }

    public void setListener(OnNumberPickListener listener) {
        this.listener = listener;
    }

    public interface OnNumberPickListener {
        void pickNumber(int number);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_number_picker, null);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        int value = bundle.getInt(PARA_CURRENT_VALUE, 150);
        tvUnit.setText(bundle.getString(PARA_UNIT, ""));
        decimalFormat = new DecimalFormat("#.00");
        scale = (int) Math.pow(10, bundle.getInt(PARA_FLOAT_VALUE, 0));
        pickerDigital.setMaxValue(bundle.getInt(PARA_MAX_VALUE, 100));
        pickerDigital.setMinValue(bundle.getInt(PARA_MIN_VALUE, 0));
        pickerDigital.setValue(value / scale);

        pickerFloat.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return decimalFormat.format(value / (float) scale);
            }
        });
        pickerFloat.setMaxValue(scale - 1);
        pickerFloat.setMinValue(0);
        pickerFloat.setValue(value % scale);
        try {
            Method method = pickerFloat.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(pickerFloat, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (scale == 1) {
            pickerFloat.setVisibility(View.GONE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(bundle.getString(PARA_TITLE, ""));
        return builder.create();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }


    @OnClick(R.id.btn_commit)
    void commit() {
        if (listener != null) {
            listener.pickNumber(pickerDigital.getValue() * scale + pickerFloat.getValue());
        }
        dismiss();
    }

    @OnClick(R.id.btn_cancel)
    void cancel() {
        dismiss();
    }


}
