package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.adapter.EcgDetailAdapter;
import com.mgtech.maiganapp.data.model.EcgModel;
import com.mgtech.maiganapp.utils.PermissionUtils;
import com.mgtech.maiganapp.utils.ShareUtils;
import com.mgtech.maiganapp.viewmodel.EcgDataDetailViewModel;
import com.mgtech.maiganapp.widget.EcgPartDataGraphView;
import com.mgtech.maiganapp.window.SharePopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author zhaixiang
 * @date 2018/1/10
 * Ecg 数据展示
 */

public class EcgDataDetailActivity extends BaseActivity<EcgDataDetailViewModel> {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.iv_reverse)
    View ivReverse;
    @Bind(R.id.root)
    View root;
    @Bind(R.id.layout_screen)
    LinearLayout layoutScreen;
    @Bind(R.id.layout_screen_horizontal)
    LinearLayout layoutScreenHorizontal;
    private EcgDetailAdapter adapter;
    private AlertDialog goToOpenStoragePermissionDialog;
    private SharePopupWindow popupWindow;


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, EcgDataDetailActivity.class);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void measureResult(EcgModel model) {
        EventBus.getDefault().removeStickyEvent(model);
        viewModel.setModel(model);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        viewModel.horizontal.set(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        viewModel.ecgDataLoadFail.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                showShortToast(getString(R.string.network_error));
                finish();
            }
        });
        viewModel.ecgDataLoadSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                EcgModel model = viewModel.model;
                if (model != null) {
                    initScreen();
                    adapter.setData(viewModel.partEcgList, model.sampleRate, !viewModel.horizontal.get());
                }
            }
        });
        adapter = new EcgDetailAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, viewModel.horizontal.get() ?
                RecyclerView.HORIZONTAL : RecyclerView.VERTICAL, false
        ));
        if (viewModel.model != null) {
            viewModel.setModel(viewModel.model);
        }
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @OnClick(R.id.btn_back)
    void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        }
        super.onBackPressed();
    }

    @OnClick(R.id.btn_reverse)
    void reverse() {
        viewModel.reverse = (!viewModel.reverse);
        ivReverse.animate()
                .rotation(viewModel.reverse ? 180 : 0)
                .start();
        adapter.setReverse(viewModel.reverse);
    }

    @OnClick(R.id.btn_share)
    void share() {
        if (PermissionUtils.isStoragePermissionOpen(this)){
            showShareDialog();
        }else{
            PermissionUtils.requestStoragePermission(this,REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void showShareDialog(){
        int[] icons = {
                R.drawable.share_wx, R.drawable.share_moments, R.drawable.share_qq,
                R.drawable.share_zone, R.drawable.share_gallary
        };
        final String[] texts = {
                getString(R.string.wechat),
                getString(R.string.wechat_moments),
                getString(R.string.qq),
                getString(R.string.qq_zone),
                getString(R.string.ecg_detail_save_to_local)
        };
        final String[] platform = {
                Wechat.NAME, WechatMoments.NAME, QQ.NAME, QZone.NAME
        };
        popupWindow = new SharePopupWindow(this, icons, texts, new SharePopupWindow.Callback() {

            @Override
            public void select(int index) {
                layoutScreen.setVisibility(View.VISIBLE);
                if (index == 4) {
                    if (saveToLocal()) {
                        showShortToast(getString(R.string.ecg_detail_save_success));
                    } else {
                        showShortToast(getString(R.string.ecg_detail_save_fail));
                    }
                } else {
                    Bitmap bitmap = getPic();
                    if (bitmap != null) {
                        ShareUtils.shareBitmap(EcgDataDetailActivity.this, bitmap,
                                getString(R.string.ecg_detail_ecg_graph),
                                platform[index % platform.length]);
                    }
                }
                layoutScreen.setVisibility(View.INVISIBLE);
                popupWindow.dismiss();
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

    private Bitmap getPic() {
        if (layoutScreen.getMeasuredWidth() <= 0 || layoutScreen.getMeasuredHeight() <= 0) {
            return null;
        }
        Bitmap bmp = Bitmap.createBitmap(layoutScreen.getMeasuredWidth(), layoutScreen.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        layoutScreen.draw(canvas);
        return bmp;
    }

    private boolean saveToLocal() {
        LinearLayout layout;
        if (viewModel.horizontal.get()) {
            layout = layoutScreenHorizontal;
        } else {
            layout = layoutScreen;
        }
        if (layout.getMeasuredWidth() <= 0 || layout.getMeasuredHeight() <= 0) {
            return false;
        }
        Bitmap bmp = Bitmap.createBitmap(layout.getMeasuredWidth(), layout.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        layout.draw(canvas);
        Calendar calendar = Calendar.getInstance();
        MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "ECG",
                DateFormat.format(MyConstant.FULL_DATETIME_FORMAT, calendar).toString());
        return true;
    }


    private void initScreen() {
        if (!viewModel.share.get()) {
            return;
        }
        boolean isHorizontal = viewModel.horizontal.get();
        if (isHorizontal) {
            initHorizontalCaptureScreen();
        } else {
            initVerticalCaptureScreen();
        }
    }

    private void initVerticalCaptureScreen() {
        if (layoutScreen == null) {
            return;
        }
        List<float[]> data = viewModel.partEcgList;
        layoutScreen.removeAllViews();
        View header = getVerticalCaptureHeader();
        layoutScreen.addView(header);
        for (float[] arr : data) {
            View view = LayoutInflater.from(this).inflate(R.layout.activity_ecg_data_detail_item, layoutScreen, false);
            EcgPartDataGraphView graphView = view.findViewById(R.id.graph);
            graphView.setDataAndReverse(arr, viewModel.model.sampleRate, viewModel.reverse);
            layoutScreen.addView(view);
        }
        View foot = LayoutInflater.from(this).inflate(R.layout.activity_ecg_data_detail_capture_foot_vertical,
                layoutScreen, false);
        layoutScreen.addView(foot);
    }

    private View getVerticalCaptureHeader() {
        View header = LayoutInflater.from(this).inflate(R.layout.activity_ecg_data_detail_capture_header_vertical,
                layoutScreen, false);
        final TextView tvName = header.findViewById(R.id.tv_name_value);
        final TextView tvSex = header.findViewById(R.id.tv_sex_value);
        final TextView tvAge = header.findViewById(R.id.tv_age_value);
        final TextView tvHeight = header.findViewById(R.id.tv_height_value);
        final TextView tvWeight = header.findViewById(R.id.tv_weight_value);
        final TextView tvMeasureTime = header.findViewById(R.id.tv_measure_time_value);
        final TextView tvDuration = header.findViewById(R.id.tv_duration_value);
        final TextView tvWalkSpeed = header.findViewById(R.id.tv_walk_speed_value);
        tvName.setText(viewModel.name);
        tvSex.setText(viewModel.sex);
        tvAge.setText(viewModel.age);
        tvHeight.setText(viewModel.height + "cm");
        tvWeight.setText(viewModel.weight + "kg");
        tvMeasureTime.setText(DateFormat.format(MyConstant.FULL_DATETIME_FORMAT, viewModel.model.measureTime));
        tvDuration.setText(MyConstant.ECG_SAVING_TIME + "s");
        tvWalkSpeed.setText(R.string.walk_speed_value);
        return header;
    }

    private void initHorizontalCaptureScreen() {
        if (layoutScreenHorizontal == null) {
            return;
        }
        List<float[]> data = viewModel.partEcgList;
        layoutScreenHorizontal.removeAllViews();
        View header = getHorizontalCaptureHeader();
        layoutScreenHorizontal.addView(header);

        for (float[] arr : data) {

            View view = LayoutInflater.from(this).inflate(R.layout.activity_ecg_data_detail_item_horizontal,
                    layoutScreenHorizontal, false);
            EcgPartDataGraphView graphView = view.findViewById(R.id.graph);
            graphView.setDataAndReverse(arr, viewModel.model.sampleRate, viewModel.reverse);
            layoutScreenHorizontal.addView(view);
        }

        View foot = LayoutInflater.from(this).inflate(R.layout.activity_ecg_data_detail_capture_foot_horizontal,
                layoutScreenHorizontal, false);
        layoutScreenHorizontal.addView(foot);
    }

    private View getHorizontalCaptureHeader() {
        View header = LayoutInflater.from(this).inflate(R.layout.activity_ecg_data_detail_capture_header_horizontal,
                layoutScreenHorizontal, false);
        final TextView tvName = header.findViewById(R.id.tv_name_value);
        final TextView tvSex = header.findViewById(R.id.tv_sex_value);
        final TextView tvAge = header.findViewById(R.id.tv_age_value);
        final TextView tvHeight = header.findViewById(R.id.tv_height_value);
        final TextView tvWeight = header.findViewById(R.id.tv_weight_value);
        final TextView tvMeasureTime = header.findViewById(R.id.tv_measure_time_value);
        final TextView tvDuration = header.findViewById(R.id.tv_duration_value);
        final TextView tvWalkSpeed = header.findViewById(R.id.tv_walk_speed_value);
        tvName.setText(viewModel.name);
        tvSex.setText(viewModel.sex);
        tvAge.setText(viewModel.age);
        tvHeight.setText(viewModel.height);
        tvWeight.setText(viewModel.weight);
        tvMeasureTime.setText(DateFormat.format(MyConstant.FULL_DATETIME_FORMAT, viewModel.model.measureTime));
        tvDuration.setText(String.valueOf(MyConstant.ECG_SAVING_TIME) + "s");
        tvWalkSpeed.setText(R.string.walk_speed_value);
        return header;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ecg_data_detail;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult: " + Arrays.toString(grantResults));
        if (requestCode == REQUEST_EXTERNAL_STORAGE && grantResults.length >= 1 ){
            if (grantResults[0]== PackageManager.PERMISSION_DENIED) {
                showGoToOpenStoragePermissionDialog();
            }else if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                showShareDialog();
            }
        }
    }

    private void showGoToOpenStoragePermissionDialog(){
        if (goToOpenStoragePermissionDialog != null && goToOpenStoragePermissionDialog.isShowing()){
            return;
        }
        goToOpenStoragePermissionDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.ecg_data_detail_storage_permission_closed))
                .setMessage(getString(R.string.ecg_data_detail_storage_permission_is_open))
                .setNegativeButton(R.string.do_not_open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(EcgDataDetailActivity.this, R.string.ecg_data_detail_share_cannot_use,
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton(R.string.go_to_open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(SettingSystemPermissionActivity.getCallingIntent(EcgDataDetailActivity.this),REQUEST_PERMISSION_ACTIVITY);
                    }
                }).create();
        goToOpenStoragePermissionDialog.show();
    }

}
