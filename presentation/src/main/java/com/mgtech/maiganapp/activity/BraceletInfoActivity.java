package com.mgtech.maiganapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;

import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.BindInfoUpdate;
import com.mgtech.maiganapp.viewmodel.BraceletInfoViewModel;
import com.mgtech.maiganapp.window.BraceletUnPairPopupWindow;
import com.mgtech.maiganapp.window.SetDisplayConfigPopupWindow;
import com.mgtech.maiganapp.window.SetDisplayTimePopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 * 手环信息
 */
public class BraceletInfoActivity extends BleActivity<BraceletInfoViewModel> {
    private SetDisplayTimePopupWindow timePopupWindow;
    private SetDisplayConfigPopupWindow popupWindow;
    private BraceletUnPairPopupWindow unPairPopupWindow;
    @Bind(R.id.root)
    View root;
    private AlertDialog notPairDialog;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BraceletInfoActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        hideActionbar();
        viewModel.braceletNotPair.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                showNotPairDialog();
            }
        });
        viewModel.bleClosed.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!isBleOn()) {
                    dismissPopupWindow();
                }
            }
        });
        viewModel.isLink.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (unPairPopupWindow != null && unPairPopupWindow.isShowing()) {
                    unPairPopupWindow.setLink(viewModel.isLink.get());
                }
            }
        });
        viewModel.unPairSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                dismissPopupWindow();
                finish();
            }
        });
        viewModel.unPairFail.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                dismissPopupWindow();
                Toast.makeText(BraceletInfoActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNotPairDialog() {
        if (notPairDialog != null && notPairDialog.isShowing()) {
            return;
        }
        notPairDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.caution)
                .setMessage(R.string.bracelet_need_bind_again)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create();
        notPairDialog.show();
    }

    @OnClick(R.id.btn_open_bluetooth)
    void openBluetooth() {
        if (!isBleOn()) {
            openBle();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bindInfoUpdate(BindInfoUpdate bindInfoUpdate) {
        viewModel.checkLink();
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewModel.checkLink();
        viewModel.getBraceletInfo();
        viewModel.setReminderNumber();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }

    @OnClick(R.id.btn_remove_pair)
    void removePair() {
        unPairPopupWindow = new BraceletUnPairPopupWindow(this, new BraceletUnPairPopupWindow.Callback() {
            @Override
            public void unPair(boolean notifyBracelet) {
                viewModel.unbind();
            }
        },viewModel.isLink.get());
        unPairPopupWindow.setOutsideTouchable(true);
        unPairPopupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        unPairPopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
        backgroundAlpha(0.6f);
        unPairPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });
    }

    @OnClick(R.id.layout_version)
    void clickVersion() {
        if (!viewModel.stateViewModel.showMask.get() && viewModel.braceletHasNewVersion.get()) {
//            startActivity(FirmwareUpgradeActivity.getCallingIntent(this, false));
        }
    }

    @OnClick(R.id.layout_version_debug)
    void clickDebugVersion() {
        if (!viewModel.stateViewModel.showMask.get() && viewModel.braceletHasNewVersionDebug.get()) {
            startActivity(FirmwareUpgradeActivity.getCallingIntent(this, true));
        }
    }

    @OnClick(R.id.layout_reminder)
    void setReminder() {
        if (!viewModel.stateViewModel.showMask.get()) {
            startActivity(BraceletReminderActivity.getCallingIntent(this));
        }
    }

    @OnClick(R.id.layout_display)
    void setDisplay() {
        if (!viewModel.stateViewModel.showMask.get()) {
            popupWindow = new SetDisplayConfigPopupWindow(this, new SetDisplayConfigPopupWindow.Callback() {
                @Override
                public void submit(DisplayPage config) {
                    viewModel.setConfig(config);
                }
            });
            popupWindow.setOutsideTouchable(true);
            popupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
            backgroundAlpha(0.6f);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1);
                }
            });
        }
    }

    @OnClick(R.id.layout_display_time)
    void setTime() {
        if (!viewModel.stateViewModel.showMask.get()) {
            timePopupWindow = new SetDisplayTimePopupWindow(this, new SetDisplayTimePopupWindow.Callback() {
                @Override
                public void submit(DisplayPage isFull) {
                    viewModel.setConfig(isFull);
                }
            });
            timePopupWindow.setOutsideTouchable(true);
            timePopupWindow.showAtLocation(root, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            timePopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
            backgroundAlpha(0.6f);
            timePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1);
                }
            });
        }
    }

    @OnClick(R.id.layout_find_bracelet)
    void findBracelet() {
        startActivity(BraceletFindActivity.getCallingIntent(this));
    }

    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        if (timePopupWindow != null && timePopupWindow.isShowing()) {
            timePopupWindow.dismiss();
        }
        if (unPairPopupWindow != null && unPairPopupWindow.isShowing()) {
            unPairPopupWindow.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissPopupWindow();
        super.onDestroy();
    }

    /**
     * 设置屏幕变暗
     *
     * @param bgAlpha 1 最亮 0 最暗
     */
    @Override
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bracelet_info;
    }

    @Override
    protected void openBleFail() {
        showOpenBleFailDialog();
    }

    @Override
    protected void openBleSuccess() {
        Toast.makeText(this, R.string.ble_has_open, Toast.LENGTH_SHORT).show();
    }

}
