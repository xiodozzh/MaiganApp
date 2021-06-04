package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mgtech.blelib.entity.BroadcastData;
import com.mgtech.maiganapp.BleApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.BroadcastAdapter;
import com.mgtech.maiganapp.viewmodel.BraceletPairViewModel;
import com.mgtech.maiganapp.widget.WrapContentLinearLayoutManager;
import com.mgtech.maiganapp.window.BleStatePairPopupWindow;
import com.mgtech.maiganapp.window.BraceletPairPopupWindow;
import com.mgtech.maiganapp.window.WearMethodPopupWindow;
import com.zxy.tiny.common.Logger;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class BraceletPairActivity extends BleActivity<BraceletPairViewModel> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.sdv_guide)
    ImageView ivGuide;
    @Bind(R.id.root)
    View root;
    private BroadcastAdapter adapter;
    private AlertDialog checkFailDialog;
    private AlertDialog pairFailDialog;
    private BraceletPairPopupWindow pairPopupWindow;
    private BleStatePairPopupWindow bleStatePairPopupWindow;
    private static Handler handler = new Handler();
    private static final int UPGRADE_FIRMWARE = 123;
    private long clickTime = 0;
    private WearMethodPopupWindow wearMethodPopupWindow;


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, BraceletPairActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        hideActionbar();
        if (!isBleOn()) {
            openBle();
        }
        initObservableCallback();
//        BluetoothService2.startServiceWithBlePair(this);
        ((BleApplication)getApplication()).getRequest().pair();
        adapter = new BroadcastAdapter(recyclerView);
        adapter.setListener(new BroadcastAdapter.BroadcastListener() {
            @Override
            public void onSelect(int position) {
                long now = Calendar.getInstance().getTimeInMillis();
                if (now - clickTime < 500) {
                    return;
                }
                clickTime = now;
                BroadcastData data = viewModel.broadcastData.get(position);
                viewModel.broadcastData.clear();
                adapter.setDataList(viewModel.broadcastData);

                if (Calendar.getInstance().getTimeInMillis() - data.getInitTime() > 5000) {
                    showTimeOutDialog();
                } else {
                    viewModel.pair(data);
                    showPairDialog();
                }
            }
        });
        Glide.with(this).asGif().load(R.drawable.bracelet_pair_long_touch).into(ivGuide);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        root.post(new Runnable() {
            @Override
            public void run() {
                showWearMethod();
            }
        });
//        askForBlePermission();
    }

    private void showWearMethod(){
        hideWearMethod();
        if (wearMethodPopupWindow == null){
            wearMethodPopupWindow = new WearMethodPopupWindow(this, new WearMethodPopupWindow.Callback() {
                @Override
                public void onSubmit() {
//                    startActivity(BraceletPairActivity.getCallingIntent(BraceletPairActivity.this));
                }
            });
            wearMethodPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
        }
        backgroundAlpha(0.6f);
        wearMethodPopupWindow.show(root);
    }

    private void hideWearMethod(){
        if (wearMethodPopupWindow != null && wearMethodPopupWindow.isShowing()) {
            wearMethodPopupWindow.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getLinkedStatus();
    }



    /**
     * 监听view-model observable变化
     */
    private void initObservableCallback() {
        viewModel.broadcastUpdate.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                adapter.setDataList(viewModel.broadcastData);
            }
        });
        viewModel.pairError.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                failPairDialog();
                handler.postDelayed(pairErrorRunnable, 1000);
                dismissPairDialog();
            }
        });
        viewModel.checkFail.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                showCheckFailDialog();
            }
        });
        // 蓝牙绑定成功
        viewModel.blePairSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
            }
        });
        // 网络绑定完成
        viewModel.netPairSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                successPairDialog();
                refreshMask();
            }
        });
        viewModel.goToUpgradeActivity.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                startActivityForResult(FirmwareUpgradeActivity.getCallingIntent(BraceletPairActivity.this, false), UPGRADE_FIRMWARE);
                viewModel.setUpgradeDone();
            }
        });
        viewModel.syncSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setParamSuccess();
            }
        });
        viewModel.showConnectGuideGif.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                linkPairDialog();
            }
        });
        viewModel.bleClosed.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!isBleOn()) {
                    openBle();
                    dismissPairDialog();
                }
            }
        });
        viewModel.connecting.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                refreshMask();
            }
        });

    }

    private void refreshMask() {
        if (viewModel.netPairSuccess.get() && viewModel.connecting.get()) {
            showMask();
        } else {
            hideMask();
        }
    }

    private void showMask() {
        if (bleStatePairPopupWindow != null && bleStatePairPopupWindow.isShowing()) {
            return;
        }
        bleStatePairPopupWindow = new BleStatePairPopupWindow(this, new BleStatePairPopupWindow.Callback() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
        bleStatePairPopupWindow.setOutsideTouchable(false);
        bleStatePairPopupWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
        bleStatePairPopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
    }

    private void hideMask() {
        if (bleStatePairPopupWindow != null && bleStatePairPopupWindow.isShowing()) {
            bleStatePairPopupWindow.dismiss();
        }
    }

    private Runnable pairErrorRunnable = new Runnable() {
        @Override
        public void run() {
            pairError();
        }
    };


    private Runnable dismissPairDialogRunnable = new Runnable() {
        @Override
        public void run() {
            cancelPopupWindow();
        }
    };

    private void dismissPairDialog() {
        handler.postDelayed(dismissPairDialogRunnable, 1000);
    }

    private void linkPairDialog() {
        showPairPopupWindow();
        pairPopupWindow.link();
    }

    private void successPairDialog() {
        showPairPopupWindow();
        pairPopupWindow.pairSuccess();
    }

    private void failPairDialog() {
        showPairPopupWindow();
        pairPopupWindow.pairFail();
    }

    private void setParamSuccess() {
        showPairPopupWindow();
        pairPopupWindow.setAlarmSuccess(!viewModel.hadReminders);
    }

    private void showPairPopupWindow() {
        if (pairPopupWindow != null && pairPopupWindow.isShowing()) {
            return;
        }
        pairPopupWindow = new BraceletPairPopupWindow(this, new BraceletPairPopupWindow.Callback() {
            @Override
            public void goToSetReminder() {
                startActivity(BraceletReminderActivity.getCallingIntent(BraceletPairActivity.this));
                finish();
            }

            @Override
            public void done() {
                cancelPopupWindow();
                finish();
            }
        });
        pairPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        pairPopupWindow.setOutsideTouchable(false);
        pairPopupWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6f);
        pairPopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
    }

    private void cancelPopupWindow() {
        if (pairPopupWindow != null && pairPopupWindow.isShowing()) {
            pairPopupWindow.dismiss();
        }
    }

    private void showTimeOutDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.timeout_please_try_again)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * 显示绑定对话框
     */
    private void showPairDialog() {
        showPairPopupWindow();
    }

    private void clearList() {
        viewModel.broadcastData.clear();
        adapter.setDataList(viewModel.broadcastData);
    }

    private void pairError() {
        clearList();
        if (pairFailDialog != null && pairFailDialog.isShowing()) {
            return;
        }
        pairFailDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.bind_fail)
                .setMessage(R.string.activity_bracelet_pair_please_bond_again)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create();
        pairFailDialog.show();
    }

    private void showCheckFailDialog() {
        cancelPopupWindow();
        if (checkFailDialog != null && checkFailDialog.isShowing()) {
            return;
        }
        clearList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.activity_bracelet_pair_can_not_bond)
                .setMessage(viewModel.errorText)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        checkFailDialog = builder.create();
        checkFailDialog.show();
    }

    @Override
    public void onBackPressed() {
        viewModel.leave();
        super.onBackPressed();
    }

    @OnClick(R.id.btn_back)
    void back() {
        viewModel.leave();
        finish();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(dismissPairDialogRunnable);
        handler.removeCallbacks(pairErrorRunnable);
        hideMask();
        cancelPopupWindow();
        hideWearMethod();
        super.onDestroy();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bracelet_pair;
    }


    @Override
    protected void openBleFail() {
        showShortToast(getString(R.string.activity_bracelet_pair_open_ble_fail_cannot_bond));
        finish();
    }

    @Override
    protected void openBleSuccess() {
        viewModel.startPairScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPGRADE_FIRMWARE) {
            viewModel.startScan();
        }
    }
}
