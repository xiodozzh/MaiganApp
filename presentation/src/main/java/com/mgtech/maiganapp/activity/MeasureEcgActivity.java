package com.mgtech.maiganapp.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;
import androidx.lifecycle.Observer;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.BindInfoUpdate;
import com.mgtech.maiganapp.data.event.NotificationPageEvent;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.MeasureEcgViewModel;
import com.mgtech.maiganapp.widget.EcgDrawingGraphView;
import com.mgtech.maiganapp.window.EcgGuidePopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 * 测量ecg
 */

public class MeasureEcgActivity extends BleActivity<MeasureEcgViewModel> {
    @Bind(R.id.graphView)
    EcgDrawingGraphView graphView;
    @Bind(R.id.dv)
    DecoView dv;
    @Bind(R.id.iv_circle)
    ImageView ivCircle;
    @Bind(R.id.root)
    View root;
    private int progressIndex;
    private int progressBarColor;
    private int progressBarBackgroundColor;
    private AlertDialog failDialog;
    private AlertDialog errorDialog;
    private AlertDialog uploadFailDialog;

    private ValueAnimator rotationAnimation;
    private long clickTime;
    private EcgGuidePopupWindow popupWindow;
    private static Handler handler = new Handler();

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MeasureEcgActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        hideActionbar();
        initGraphView();
        viewModel.sampleData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                double[] data = viewModel.sampleData.get();
                graphView.appendPoints(data);
            }
        });
        viewModel.clearScreen.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                progressClear();
                getRotationAnimation().cancel();
                graphView.reset();
            }
        });
        viewModel.networkError.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                showUploadFailDialog();
            }
        });
        viewModel.cancelMeasure.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                finish();
            }
        });
        viewModel.complete.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.resultModel != null) {
                    EventBus.getDefault().postSticky(viewModel.resultModel);
                    dismissDialog();
                    startActivity(EcgDataDetailActivity.getCallingIntent(MeasureEcgActivity.this));
                }
            }
        });
//        viewModel.progress.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable observable, int i) {
//                int progress = viewModel.progress.get();
//                viewModel.progressText.set(String.valueOf(progress));
//                setMeasureProgress(MeasureEcgViewModel.MAX_PROGRESS - progress);
//            }
//        });

        viewModel.progress.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (viewModel.progress.getValue() == null){
                    return;
                }
                int progress = viewModel.progress.getValue();
                viewModel.progressText.set(String.valueOf(progress));
                setMeasureProgress(MeasureEcgViewModel.MAX_PROGRESS - progress);
            }
        });
        viewModel.showErrorDialog.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                String msg = viewModel.showErrorDialog.get();
                if (!TextUtils.isEmpty(msg)) {
                    getRotationAnimation().cancel();
                    showErrorDialog(msg);
                }
            }
        });
        viewModel.errorText.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (!TextUtils.isEmpty(viewModel.errorText.get())) {
                    getRotationAnimation().cancel();
                    showFailDialog(viewModel.errorText.get());
                }
            }
        });
        viewModel.isEcgPrepare.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!viewModel.isEcgPrepare.get()) {
                    getRotationAnimation().cancel();
                }
            }
        });
        viewModel.timeout.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                getRotationAnimation().cancel();
                showFailDialog(getString(R.string.measure_ecg_timeout));
            }
        });
        viewModel.bleClosed.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                getRotationAnimation().cancel();
            }
        });
        viewModel.showGuide.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
            }
        });
        initColor();
        initDecoView();
        setMeasureProgress(0);
        if (!SaveUtils.doesGuideMeasureEcgWatched()) {
            handler.postDelayed(showGuideRunnable,500);
        }
    }

    private Runnable showGuideRunnable = new Runnable() {
        @Override
        public void run() {
            showGuide();
        }
    };

    private void showGuide() {
        popupWindow = new EcgGuidePopupWindow(this);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
        backgroundAlpha(0.6f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1);
            }
        });
    }

    private ValueAnimator getRotationAnimation() {
        if (rotationAnimation == null) {
            rotationAnimation = ValueAnimator.ofFloat(0, 360);
            rotationAnimation.setDuration(2000);
            rotationAnimation.setInterpolator(new LinearInterpolator());
            rotationAnimation.setRepeatCount(-1);
            rotationAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ivCircle.setRotation((float) animation.getAnimatedValue());
                }
            });
        }
        return rotationAnimation;
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.checkLinkState();
        graphView.setDrawing(true);

        EventBus.getDefault().postSticky(new NotificationPageEvent(NotificationPageEvent.MEASURE_ECG_ACTIVITY));
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }


    @OnClick(R.id.layout_to_wear_guide)
    void goToGuid() {
        showGuide();
    }

    @OnClick(R.id.btn_open_bluetooth)
    void openBluetooth() {
        if (!isBleOn()) {
            openBle();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        graphView.setDrawing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bindInfoUpdate(BindInfoUpdate bindInfoUpdate) {
        viewModel.checkLinkState();
    }

    @Override
    protected void openBleFail() {
        showOpenBleFailDialog();
    }

    @Override
    protected void openBleSuccess() {
        Toast.makeText(this, R.string.ble_has_open, Toast.LENGTH_SHORT).show();
    }


    private void initColor() {
        progressBarBackgroundColor = ContextCompat.getColor(this, R.color.colorPrimaryWhite);
        progressBarColor = ContextCompat.getColor(this, R.color.colorPrimary);
    }

    private void showFailDialog(String text) {
        dismissDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.measure_fail)
                .setCancelable(false)
                .setMessage(text)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.clear();
                    }
                });
        failDialog = builder.create();
        failDialog.show();
    }

    @Override
    protected void onDestroy() {
        dismissDialog();
        getRotationAnimation().cancel();
        handler.removeCallbacks(showGuideRunnable);
        super.onDestroy();
    }

    /**
     * 设置测量心率进度条
     *
     * @param progress 进度
     */
    private void setMeasureProgress(int progress) {
        dv.addEvent(new DecoEvent.Builder(progress)
                .setIndex(progressIndex)
                .setColor(progressBarColor)
                .setDuration(100)
                .build());
    }

    private void progressClear() {
        dv.addEvent(new DecoEvent.Builder(0)
                .setIndex(progressIndex)
                .setDuration(100)
                .setColor(progressBarColor)
                .build());
    }

    private void initDecoView() {
        final SeriesItem bgItem = new SeriesItem.Builder(progressBarBackgroundColor)
                .setRange(0, MeasureEcgViewModel.MAX_PROGRESS, MeasureEcgViewModel.MAX_PROGRESS)
                .setLineWidth(ViewUtils.dp2px(10))
                .build();
        dv.addSeries(bgItem);
        final SeriesItem item = new SeriesItem.Builder(progressBarBackgroundColor)
                .setRange(0, MeasureEcgViewModel.MAX_PROGRESS, 0)
                .setLineWidth(ViewUtils.dp2px(10))
                .build();
        progressIndex = dv.addSeries(item);
    }

    @OnClick(R.id.btn_measure)
    public void measureOrStop() {
        long now = Calendar.getInstance().getTimeInMillis();
        if (now - clickTime < 500) {
            return;
        }
        clickTime = now;
        if (Utils.isBluetoothOpen()) {
            if (viewModel.isWorking.get()) {
                stopMeasure();
            } else {
                startMeasure();
            }
        } else {
            openBle();
        }
    }

    private void startMeasure() {
        graphView.reset();
        viewModel.startPrepareMeasure();
        ivCircle.setVisibility(View.VISIBLE);
        getRotationAnimation().start();
    }

    private void stopMeasure() {
        viewModel.stopMeasureNotLeave();
        getRotationAnimation().cancel();
    }

    /**
     * 显示测量异常对话框
     *
     * @param errorMessage 显示的内容
     */
    private void showErrorDialog(String errorMessage) {
        dismissDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.caution)
                .setCancelable(false)
                .setMessage(errorMessage)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.clear();
                    }
                });
        errorDialog = builder.create();
        errorDialog.show();
    }

    private void dismissDialog() {
        if (errorDialog != null && errorDialog.isShowing()) {
            errorDialog.dismiss();
        }
        if (failDialog != null && failDialog.isShowing()) {
            failDialog.dismiss();
        }
        if (uploadFailDialog != null && uploadFailDialog.isShowing()) {
            uploadFailDialog.dismiss();
        }
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 显示测量异常对话框
     */
    private void showUploadFailDialog() {
        dismissDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.caution)
                .setCancelable(false)
                .setMessage(R.string.activity_measure_send_data_fail)
                .setPositiveButton(R.string.activity_measure_send_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.calculateData();
                    }
                }).setNegativeButton(R.string.cancel, null);
        uploadFailDialog = builder.create();
        uploadFailDialog.show();
    }

    @OnClick(R.id.btn_bond)
    void goToBond() {
        startActivity(BraceletPairActivity.getCallingIntent(this));
        finish();
    }

    /**
     * 设置graphView
     */
    private void initGraphView() {
        graphView.setLineWidth(getResources().getDimensionPixelSize(R.dimen.line_width));
        graphView.reset();
    }


    @Override
    public void onBackPressed() {
        leavePage();
    }

    @OnClick(R.id.btn_back)
    void back() {
        leavePage();
    }

    private void leavePage() {
        if (!viewModel.isPaired()) {
            finish();
            return;
        }
        if (viewModel.isWorking.get()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage(R.string.measure_ecg_stop_measure_question)
                    .setPositiveButton(R.string.do_not_stop, null)
                    .setNegativeButton(R.string.stop, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.stopMeasureAndLeave();
                        }
                    });
            builder.create().show();
        } else {
            finish();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_measure_ecg;
    }

}
