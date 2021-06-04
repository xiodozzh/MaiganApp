package com.mgtech.maiganapp.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;
import androidx.lifecycle.Observer;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.BindInfoUpdate;
import com.mgtech.maiganapp.data.event.NotificationPageEvent;
import com.mgtech.maiganapp.utils.ViewUtils;
import com.mgtech.maiganapp.viewmodel.MeasurePwViewModel;
import com.mgtech.maiganapp.window.MeasurePwRecognizeTimeoutPopupWindow;
import com.mgtech.maiganapp.widget.PulseWaveGraphView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 * 测量pw
 */

public class MeasurePwActivity extends BleActivity<MeasurePwViewModel> {
    @Bind(R.id.dv_progress)
    DecoView dv;
    @Bind(R.id.graph)
    PulseWaveGraphView dynamicGraphView;

    @Bind(R.id.iv_circle)
    ImageView ivCircle;
    @Bind(R.id.root)
    View root;
    private AlertDialog quitDialog;
    private AlertDialog failDialog;
    private AlertDialog errorDialog;
    private AlertDialog uploadFailDialog;
    private AlertDialog timeoutNoResponseDialog;
    private MeasurePwRecognizeTimeoutPopupWindow timeoutGuideWearPopupWindow;

    private int progressIndex;
    private ValueAnimator rotationAnimation;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MeasurePwActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        hideActionbar();
        initDecoView();
        initObservableCallback();
//        if (!isBleOn()) {
//            openBle();
//        }
    }

    @Override
    protected void onDestroy() {
        cancelDialog();
        if (rotationAnimation != null) {
            rotationAnimation.cancel();
        }
        super.onDestroy();
    }

    private void initObservableCallback() {
        viewModel.sampleData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                short[] data = viewModel.sampleData.get();
                if (data == null) {
                    return;
                }
                dynamicGraphView.appendWithoutDraw(data);
//                dynamicGraphView.appendWithoutDraw(data,viewModel.sampleRate);
            }
        });
        viewModel.complete.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                measureComplete();
            }
        });
        viewModel.errorText.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Log.i(TAG, " viewModel.errorText onPropertyChanged: " + viewModel.errorText.get());
                if (!TextUtils.isEmpty(viewModel.errorText.get())) {
                    getRotationAnimation().cancel();
                    showFailDialog(viewModel.errorTitle, viewModel.errorText.get());
                }
            }
        });
        viewModel.measureProgress.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                int progress = viewModel.measureProgress.get();
                setMeasureProgress(progress);
            }
        });
        viewModel.cancelMeasure.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                cancelDialog();
                viewModel.destroy();
                finish();
            }
        });
        viewModel.clearScreen.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                dynamicGraphView.reset();
                setMeasureProgress(0);
                viewModel.isPreparing.set(true);
                getRotationAnimation().cancel();
            }
        });

        viewModel.showErrorDialog.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                showUploadFailDialog();
            }
        });

        viewModel.showCalculateErrorDialog.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                String msg = viewModel.showCalculateErrorDialog.get();
                if (!TextUtils.isEmpty(msg)) {
                    showCalculateErrorDialog(msg);
                }
            }
        });
        viewModel.openBle.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!Utils.isBluetoothOpen()) {
                    openBle();
                    getRotationAnimation().cancel();
                }
            }
        });
        viewModel.isPreparing.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!viewModel.isPreparing.get()) {
                    getRotationAnimation().cancel();
                }
            }
        });
        viewModel.timeoutError.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                showTimeoutNoResponseDialog();
                getRotationAnimation().cancel();
            }
        });
//        viewModel.stuck30.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable sender, int propertyId) {
//                getRotationAnimation().cancel();
//                showTimeoutWearErrorDialog();
//            }
//        });
        viewModel.stuck30.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                getRotationAnimation().cancel();
                showTimeoutWearErrorDialog();
            }
        });
        viewModel.bleClosed.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
//                if (!isBleOn()){
//                    openBle();
//                }
                getRotationAnimation().cancel();
            }
        });
    }

    @OnClick(R.id.btn_open_bluetooth)
    void openBluetooth() {
        if (!isBleOn()) {
            openBle();
        }
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
                    if (ivCircle != null) {
                        ivCircle.setRotation((float) animation.getAnimatedValue());
                    }
                }
            });
        }
        return rotationAnimation;
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.checkCanUserMeasure();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (!SaveUtils.doesGuidPwVideoWatched() && new BraceletInfoManagerBuilder(this).create().isPaired() ) {
            root.post(new Runnable() {
                @Override
                public void run() {
                    startActivity(WearGuideWearMethodActivity.getCallingIntent(MeasurePwActivity.this));
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bindInfoUpdate(BindInfoUpdate bindInfoUpdate) {
        viewModel.checkCanUserMeasure();
    }

    private void initDecoView() {
        final SeriesItem bgItem = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.colorPrimaryWhite))
                .setRange(0, 100, 100)
                .setLineWidth(ViewUtils.dp2px(10))
                .build();
        dv.addSeries(bgItem);
        final SeriesItem item = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.colorPrimary))
                .setRange(0, 100, 0)
                .setLineWidth(ViewUtils.dp2px(10))
                .build();
        progressIndex = dv.addSeries(item);
    }

    private void showTimeoutNoResponseDialog() {
        cancelDialog();
        timeoutNoResponseDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.caution)
                .setMessage(R.string.measure_ecg_timeout)
                .setPositiveButton(R.string.ok, null)
                .create();
        timeoutNoResponseDialog.show();
    }

    private void showTimeoutWearErrorDialog() {
        Log.i(TAG, "showGuid: ");
        timeoutGuideWearPopupWindow = new MeasurePwRecognizeTimeoutPopupWindow(this, new MeasurePwRecognizeTimeoutPopupWindow.Callback() {
            @Override
            public void measureAgain() {
                measureOrStop();
            }

            @Override
            public void goToGuide() {
                goToWearGuide();
            }

            @Override
            public void neverMind() {
                SaveUtils.setNeverShowWearErrorDialog();
            }
        });
        timeoutGuideWearPopupWindow.setOutsideTouchable(false);
        timeoutGuideWearPopupWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
        timeoutGuideWearPopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
    }

    private void hideTimeoutWearErrorDialog() {
        Log.i(TAG, "hideTimeoutWearErrorDialog: ");
        if (timeoutGuideWearPopupWindow != null && timeoutGuideWearPopupWindow.isShowing()) {
            timeoutGuideWearPopupWindow.dismiss();
        }
    }

    /**
     * 计算完成，跳入结果页面
     */
    private void measureComplete() {
        if (viewModel.resultModel != null) {
            Intent intent = MeasurePwResultActivity.getCallingIntent(this, viewModel.isMan,SaveUtils.getUserId());
            startActivity(intent);
            EventBus.getDefault().postSticky(viewModel.resultModel);
        } else {
            viewModel.destroy();
            finish();
        }
    }

    private void cancelDialog() {
        if (quitDialog != null && quitDialog.isShowing()) {
            quitDialog.dismiss();
        }
        if (failDialog != null && failDialog.isShowing()) {
            failDialog.dismiss();
        }
        if (timeoutNoResponseDialog != null && timeoutNoResponseDialog.isShowing()) {
            timeoutNoResponseDialog.dismiss();
        }
        if (errorDialog != null && errorDialog.isShowing()) {
            errorDialog.dismiss();
        }
        if (uploadFailDialog != null && uploadFailDialog.isShowing()) {
            uploadFailDialog.dismiss();
        }
        hideTimeoutWearErrorDialog();
    }

    private void showFailDialog(String title, String content) {
        if (failDialog != null && failDialog.isShowing()) {
            return;
        }
        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.measure_fail);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setCancelable(false)
                .setMessage(content)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.clear();
                    }
                });
        failDialog = builder.create();
        failDialog.show();
    }

    /**
     * 显示测量异常对话框
     */
    private void showUploadFailDialog() {
        cancelDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.caution)
                .setCancelable(false)
                .setMessage(R.string.activity_measure_send_data_fail)
                .setPositiveButton(R.string.activity_measure_send_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.uploadAgain();
                    }
                }).setNegativeButton(R.string.cancel, null);
        uploadFailDialog = builder.create();
        uploadFailDialog.show();
    }

    private void showCalculateErrorDialog(String errorMessage) {
        cancelDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.calculate_error)
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

    /**
     * 设置测量心率进度条
     *
     * @param progress 进度
     */
    private void setMeasureProgress(int progress) {
        Log.e(TAG, "setMeasureProgress: " + progress);
        dv.addEvent(new DecoEvent.Builder(progress)
                .setIndex(progressIndex)
                .build());
    }

    private void progressClear() {
        dv.addEvent(new DecoEvent.Builder(0)
                .setIndex(progressIndex)
                .setDuration(100)
                .build());
    }

    @OnClick(R.id.btn_bond)
    void goToBond() {
        startActivity(BraceletPairActivity.getCallingIntent(this));
    }

    @OnClick(R.id.layout_to_wear_guide)
    void goToWearGuide() {
        stopMeasure();
        startActivity(WearGuideWearMethodActivity.getCallingIntent(this));
//        startActivity(WearGuideActivity.getCallingIntent(this));
    }

    @OnClick(R.id.btn_back)
    void back() {
        leavePage();
    }

    @Override
    public void onBackPressed() {
        leavePage();
    }

    private void leavePage() {
        if (!viewModel.isPaired()) {
            viewModel.destroy();
            finish();
        }
        if (viewModel.isWorking.get()) {
            viewModel.stopMeasureAndLeave();
//            if (quitDialog != null && quitDialog.isShowing()) {
//                return;
//            }
//            AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                    .setMessage(R.string.is_sampling_pw_now_do_use_need_stop)
//                    .setPositiveButton(R.string.do_not_stop, null)
//                    .setNegativeButton(R.string.stop, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            viewModel.stopMeasureAndLeave();
//                        }
//                    });
//            quitDialog = builder.create();
//            quitDialog.show();
        } else {
            viewModel.destroy();
            finish();
        }
    }

    @OnClick(R.id.btn_start_stop)
    void measureOrStop() {
        if (Utils.isBluetoothOpen()) {
            if (viewModel.isWorking.get()) {
                // 点击停止
                stopMeasure();
            } else {
                // 点击开始
                startMeasure();
            }
        } else {
            openBle();
        }
    }

    private void startMeasure() {
        dynamicGraphView.reset();
        progressClear();
        viewModel.startPrepareMeasure();
        ivCircle.setVisibility(View.VISIBLE);
        getRotationAnimation().start();
    }


    private void stopMeasure() {
//        dynamicGraphView.reset();
        viewModel.stopMeasureNotLeave();
        getRotationAnimation().cancel();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_measure_pw;
    }

    @Override
    protected void openBleFail() {
//        Toast.makeText(this, R.string.ble_is_unavailable, Toast.LENGTH_SHORT).show();
//        finish();
        showOpenBleFailDialog();
//        viewModel.destroy();
    }


    @Override
    protected void openBleSuccess() {
        Toast.makeText(this, R.string.ble_has_open, Toast.LENGTH_SHORT).show();
        viewModel.linkIfAllowed();
    }
}
