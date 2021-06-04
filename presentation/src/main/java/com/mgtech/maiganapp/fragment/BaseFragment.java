package com.mgtech.maiganapp.fragment;

import android.annotation.SuppressLint;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.mgtech.maiganapp.BR;
import com.mgtech.maiganapp.viewmodel.BaseFragmentViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import butterknife.ButterKnife;

/**
 *
 * @author zhaixiang
 * fragment 基本类
 */

public abstract class BaseFragment<T extends BaseFragmentViewModel> extends Fragment {
    protected static final String TAG = "fragment";
    protected ViewDataBinding binding;
    protected T viewModel;
    protected View view;
    private Toast toast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getContentViewId(), container, false);
        binding = DataBindingUtil.bind(view);
        viewModel = initViewModel();
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(getModelClass());
        }
        binding.setVariable(BR.model, viewModel);
        viewModel.toastField.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (getActivity() != null) {
                    showToast(viewModel.toastText);
                }
            }
        });

        ButterKnife.bind(this, view);
        init(savedInstanceState);
        return view;
    }

    @SuppressLint("ShowToast")
    protected void showToast(String text){
        if (toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 初始化
     *
     * @param savedInstanceState 保存信息
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 获取layout资源地址id
     *
     * @return layout planId
     */
    protected abstract int getContentViewId();

    @SuppressWarnings("unchecked")
    protected Class<T> getModelClass() {
        Type type = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) type).getActualTypeArguments();
        return (Class<T>) params[0];
    }

    /**
     * 设置屏幕变暗
     *
     * @param bgAlpha 1 最亮 0 最暗
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 可以使用从activity中传来的viewModel
     * @return
     */
    protected T initViewModel(){
        return null;
    }
}
