package com.mgtech.maiganapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgtech.maiganapp.R;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhaixiang on 2017/1/9.
 * 选择数字
 */
public class NumberEditorDialogFragment extends AppCompatDialogFragment {
    //    private static NumberEditorDialogFragment fragment;
    private OnNumberPickListener listener;
    public static final String PARA_TITLE = "text";
    public static final String PARA_MIN_VALUE = "min";
    public static final String PARA_MAX_VALUE = "max";
    public static final String PARA_CURRENT_VALUE = "current";
    public static final String PARA_UNIT = "unit";

    //    @Bind(R.planId.tv_unit)
//    TextView tvUnit;
    @Bind(R.id.et)
    EditText et;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    private int minValue;
    private int maxValue;
    private static Handler handler = new Handler();

    public static NumberEditorDialogFragment getInstance() {
        return new NumberEditorDialogFragment();
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_edit, null);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String unit = bundle.getString(PARA_UNIT, "");
            maxValue = bundle.getInt(PARA_MAX_VALUE, 100);
            minValue = bundle.getInt(PARA_MIN_VALUE, 0);
            tvTitle.setText(String.format(Locale.getDefault(),bundle.getString(PARA_TITLE) + "(%s)",unit));
            int current = bundle.getInt(PARA_CURRENT_VALUE, 0);
            if (current != 0) {
                et.setText(String.valueOf(current));
            } else {
                et.setText("");
            }
        }
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        checkSubmitEnable();
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkSubmitEnable();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        et.setSelection(et.getText().length());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return builder.create();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onResume() {
        try {
            int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
            int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
            Window window = getDialog().getWindow();
            if (window != null) {
                window.setLayout(width, height);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
        handler.postDelayed(hideInputRunnable, 500);
    }

    private Runnable hideInputRunnable = new Runnable() {
        @Override
        public void run() {
            if (et != null) {
                InputMethodManager inManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    };

    @Override
    public void onDestroy() {
        handler.removeCallbacks(hideInputRunnable);
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    /**
     * 检查确定按钮是否可用
     */
    private void checkSubmitEnable() {
        String text = et.getText().toString().trim();
        btnSubmit.setEnabled(!text.isEmpty());
    }


    @OnClick(R.id.btn_submit)
    void commit() {
        if (listener != null) {
            String text = et.getText().toString().trim();
            try {
                int number = Integer.parseInt(text);
                if (number > maxValue || number < minValue) {
                    Toast.makeText(getActivity(), getString(R.string.fragment_number_editor_dialog_out_of_range), Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.pickNumber(number);
            } catch (Exception e) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), getString(R.string.fragment_number_editor_dialog_please_input_right_format),
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        dismiss();
    }

    @OnClick(R.id.btn_cancel)
    void cancel() {
        dismiss();
    }


}
