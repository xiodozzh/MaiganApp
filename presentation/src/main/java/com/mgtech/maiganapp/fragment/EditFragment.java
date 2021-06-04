package com.mgtech.maiganapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.mgtech.maiganapp.R;

import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhaixiang on 2016/10/18.
 * 展示数据
 */

public class EditFragment extends AppCompatDialogFragment {

//    @Bind(R.planId.tv_unit)
//    TextView tvUnit;
    @Bind(R.id.et)
    EditText et;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    private OnTextChangeListener listener;
    public static final String PARA_TITLE = "text";
    public static final String PARA_HINT = "hint";
    public static final String PARA_EMPTY_WARNING = "emptyWarning";
    public static final String PARA_DEFAULT_VALUE = "default";
    public static final String PARA_TYPE = "type";
    public static final String PARA_UNIT = "unit";
    private String emptyWarning;

    public static EditFragment getInstance() {
        return new EditFragment();
    }

    public EditFragment() {
    }

    public void setListener(OnTextChangeListener listener) {
        this.listener = listener;
    }

    public interface OnTextChangeListener {
        void textChange(String editString);
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_edit, container, false);
//        ButterKnife.bind(this, view);
//        Bundle bundle = getArguments();
//        tvTitle.setText(bundle.getString(PARA_TITLE));
//        et.setText(bundle.getString(PARA_DEFAULT_VALUE));
//        et.setHint(bundle.getString(PARA_HINT));
//        tvUnit.setText(bundle.getString(PARA_UNIT));
//        et.setInputType(bundle.getInt(PARA_TYPE, InputType.TYPE_TEXT_VARIATION_PERSON_NAME));
//        checkSubmitEnable();
//        et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                checkSubmitEnable();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//        emptyWarning = bundle.getString(PARA_EMPTY_WARNING);
//        return view;
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.fragment_edit, null);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        tvTitle.setText(bundle.getString(PARA_TITLE));
        et.setHint(bundle.getString(PARA_HINT));
        et.setText(bundle.getString(PARA_DEFAULT_VALUE));
//        Selection.setSelection(editText,editText.length());
//        tvUnit.setText(bundle.getString(PARA_UNIT));
        et.setInputType(bundle.getInt(PARA_TYPE, InputType.TYPE_TEXT_VARIATION_PERSON_NAME));
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
        emptyWarning = bundle.getString(PARA_EMPTY_WARNING);
        et.setSelection(et.getText().length());
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);
        return builder.create();
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
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onResume();
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                if (et != null) {
                    InputMethodManager inManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        },500);
    }

    /**
     * 检查确定按钮是否可用
     */
    private void checkSubmitEnable() {
        String text = et.getText().toString().trim();
        btnSubmit.setEnabled(!text.isEmpty());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_submit)
    void commit() {
        if (listener != null) {
            String s = et.getText().toString().trim();
            if (s.isEmpty()) {
                Toast.makeText(getActivity(), emptyWarning, Toast.LENGTH_SHORT).show();
                return;
            }
            listener.textChange(s);
        }
        dismiss();
    }

    @OnClick(R.id.btn_cancel)
    void cancel() {
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
