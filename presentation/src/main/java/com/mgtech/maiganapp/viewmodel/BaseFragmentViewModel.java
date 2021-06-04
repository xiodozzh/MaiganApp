package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;

/**
 *
 * @author zhaixiang
 * view-model 抽象类
 */

public abstract class BaseFragmentViewModel extends BaseViewModel{
    protected static final String TAG = "viewModel";
    public ObservableBoolean toastField = new ObservableBoolean(false);
    public String toastText = "";

    public BaseFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 显示 Toast
     *
     * @param text 显示的string
     */
    @Override
    public void showToast(String text) {
        toastText = text;
        this.toastField.set(!toastField.get());
    }

}
