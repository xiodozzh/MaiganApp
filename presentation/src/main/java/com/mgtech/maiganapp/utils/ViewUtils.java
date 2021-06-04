package com.mgtech.maiganapp.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.fragment.DeleteDialogFragment;
import com.mgtech.maiganapp.fragment.EditFragment;
import com.mgtech.maiganapp.fragment.SingleSelectFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by zhaixiang on 2017/1/18.
 * View工具
 */

public class ViewUtils {
    public interface SelectCallback {
        void onSelect(float result);
    }

    public interface EditCallback {
        void onEdit(String result);
    }

    public interface BinarySelectCallback {
        void onSelect(boolean result);
    }

    /**
     * 选择身高对话框
     *
     * @param context       Activity
     * @param currentHeight 默认身高
     * @param callback      回调函数
     */
    public static void showSelectHeightDialog(final FragmentActivity context, String submitString, String cancelString,
                                              int currentHeight, final SelectCallback callback) {
        if (currentHeight == 0f) {
            currentHeight = MyConstant.HEIGHT_DEFAULT;
        }
        final List<Integer> heightList = new ArrayList<>();
        for (int i = MyConstant.HEIGHT_MIN; i <= MyConstant.HEIGHT_MAX; i++) {
            heightList.add(i);
        }
        OptionsPickerView<Integer> optionsPickerView = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                callback.onSelect(heightList.get(options1));
            }
        })
                .setCancelColor(ContextCompat.getColor(Objects.requireNonNull(context), R.color.white))
                .setSubmitColor(ContextCompat.getColor(context, R.color.white))
                .setTitleColor(ContextCompat.getColor(context, R.color.white))
                .setTitleBgColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setTitleText(context.getString(R.string.height_with_unit))
                .setSubmitText(submitString)
                .setCancelText(cancelString)
                .build();
        optionsPickerView.setPicker(heightList);
        optionsPickerView.setSelectOptions(currentHeight - MyConstant.HEIGHT_MIN);
        optionsPickerView.show();
    }

    /**
     * 选择体重对话框
     *
     * @param context       Activity
     * @param currentWeight 默认体重
     * @param callback      回调函数
     */
    public static void showSelectWeightDialog(final FragmentActivity context, String submitString, String cancelString,
                                              float currentWeight, final SelectCallback callback) {
        if (currentWeight == 0f) {
            currentWeight = MyConstant.WEIGHT_DEFAULT;
        }
        final List<Float> weightList = new ArrayList<>();
        for (float i = MyConstant.WEIGHT_MIN; i <= MyConstant.WEIGHT_MAX; i = i + 0.5f) {
            weightList.add(i);
        }
        OptionsPickerView<Float> optionsPickerView = new OptionsPickerBuilder(context,
                new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        callback.onSelect(weightList.get(options1));
                    }
                })
                .setCancelColor(ContextCompat.getColor(Objects.requireNonNull(context), R.color.white))
                .setSubmitColor(ContextCompat.getColor(context, R.color.white))
                .setTitleColor(ContextCompat.getColor(context, R.color.white))
                .setTitleBgColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setTitleText(context.getString(R.string.weight_with_unit))
                .setSubmitText(submitString)
                .setCancelText(cancelString)
                .build();
        optionsPickerView.setPicker(weightList);
        optionsPickerView.setSelectOptions((int) ((currentWeight - MyConstant.WEIGHT_MIN) * 2));
        optionsPickerView.show();
    }

    /**
     * 单选对话框
     *
     * @param context          Activity
     * @param title            标题
     * @param currentItemIndex 默认选择
     * @param items            选项字符串数组
     * @param callback         回调函数
     */
    public static void showSingleSelectDialog(FragmentActivity context, String title, int currentItemIndex, String[] items,
                                              final SelectCallback callback) {
        SingleSelectFragment fragment = SingleSelectFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putString(SingleSelectFragment.PARA_TITLE, title);
        bundle.putStringArray(SingleSelectFragment.PARA_ITEMS, items);
        bundle.putInt(SingleSelectFragment.PARA_DEFAULT_VALUE, currentItemIndex);
        fragment.setArguments(bundle);
        fragment.setListener(new SingleSelectFragment.OnSelectChangedListener() {
            @Override
            public void onSelectChanged(int position) {
                callback.onSelect(position);
            }
        });
        fragment.show(context.getSupportFragmentManager(), "set single");
    }

    /**
     * 编辑对话框
     *
     * @param context       Activity
     * @param title         标题
     * @param defaultString 默认
     * @param hint          提示
     * @param unit          单位
     * @param emptyWarning  空白报错
     * @param inputType     输入类型
     * @param callback      回调函数
     */
    public static void showEditDialog(FragmentActivity context, String title, String defaultString, String hint, String unit,
                                      String emptyWarning, int inputType, final EditCallback callback) {
        EditFragment fragment = EditFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putString(EditFragment.PARA_TITLE, title);
        bundle.putString(EditFragment.PARA_HINT, hint);
        bundle.putInt(EditFragment.PARA_TYPE, inputType);
        bundle.putString(EditFragment.PARA_UNIT, unit);
        bundle.putString(EditFragment.PARA_EMPTY_WARNING, emptyWarning);
        bundle.putString(EditFragment.PARA_DEFAULT_VALUE, defaultString);
        fragment.setArguments(bundle);
        fragment.setListener(new EditFragment.OnTextChangeListener() {
            @Override
            public void textChange(String editString) {
                callback.onEdit(editString);
            }
        });
        fragment.show(context.getSupportFragmentManager(), "edit");
    }

    /**
     * 显示删除对话框
     *
     * @param context 上下文
     */
    public static void showDeleteDialog(FragmentActivity context, DeleteDialogFragment.Callback callback) {
        DeleteDialogFragment fragment = DeleteDialogFragment.getInstance();
        fragment.setCallback(callback);
        fragment.show(context.getSupportFragmentManager(), "remove");
    }

    /**
     * 改变 ImageView 图片颜色
     *
     * @param context context
     * @param view    ImageView
     * @param color   色彩
     */
    public static void changeViewDrawableColor(Context context, ImageView view,  int color) {
        if (context == null) {
            return;
        }
        Drawable drawable = view.getDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * 通知栏信息
     *
     * @param context getBaseContext
     * @param title   主标题
     * @param text    主要内容
     */
    public static void showNotification(Context context, Intent intent,
                                        String title, String text, int id) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(id, getNotification(context, intent, title, text, CHANNEL_ID + id));
        }
    }

    public static void closeJPush(Context context) {

    }

    /**
     * 取消notification
     *
     * @param context 上下文
     * @param id      notification planId
     */
    public static void cancelNotification(Context context, int id) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(id);
    }

    private static final String CHANNEL_NAME = "Mystrace";
    public static final String CHANNEL_ID = "Mystrace";
    public static final String CHANNEL_NOTIFICATION_ID = "NotificationBar";
    public static final int NOTIFICATION_DISCONNECT_ID = 100;
    public static final int NOTIFICATION_FORGROUND_ID = 200;

//    public static Notification getNotification(Context context, Intent intent, String uri,
//                                               String text, String text, String channelId) {
//        Notification.Builder builder;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            @SuppressLint("WrongConstant")
//            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH);
//            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            if (nm != null) {
//                nm.createNotificationChannel(notificationChannel);
//            }
//            builder = new Notification.Builder(context, channelId);
//        } else {
//            builder = new Notification.Builder(context);
//        }
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 16;
//        int side = context.getResources().getDimensionPixelSize(R.dimen.large_padding);
//        Bitmap icon = decodeSampleBitmapFromResource(context.getResources(), R.mipmap.ic_launcher, side, side);
//        builder.setAutoCancel(true);
//        builder.setContentTitle(text);
//        builder.setLargeIcon(icon);
//        builder.setContentText(text);
//        builder.setSmallIcon(R.drawable.logo_small);
//        builder.setWhen(System.currentTimeMillis());
//        if (uri != null) {
//            if (TextUtils.isEmpty(uri)) {
//                builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//            } else {
//                builder.setSound(Uri.parse(uri));
//            }
//        }
//        PendingIntent pi = PendingIntent.getActivity(context, 1000, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pi);
//        return builder.build();
//    }

    public static Notification getBackgroundNotification(Context context, Intent intent,
                                                         String title, String text, String channelId) {
        Notification.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_LOW);
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) {
                notificationChannel.enableLights(false);
                notificationChannel.enableVibration(false);
                Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.silence);
                if (uri != null) {
                    notificationChannel.setSound(uri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
                }
                notificationChannel.setShowBadge(false);
                nm.createNotificationChannel(notificationChannel);
            }
            builder = new Notification.Builder(context, channelId);
        } else {
            builder = new Notification.Builder(context);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 16;
        int side = context.getResources().getDimensionPixelSize(R.dimen.large_padding);
        Bitmap icon = decodeSampleBitmapFromResource(context.getResources(), R.mipmap.ic_launcher, side, side);
        //点击后自动消失
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        // 主标题
        builder.setContentTitle(title);
        // 设置大图标
        builder.setLargeIcon(icon);
        // 设置主要内容
        builder.setContentText(text);
        // 设置小图标
        builder.setPriority(Notification.PRIORITY_LOW);
        builder.setSmallIcon(R.drawable.logo_small);
        builder.setWhen(System.currentTimeMillis());
        PendingIntent pi = PendingIntent.getActivity(context, 1000, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);

//        Notification notification = builder.build();
//        notification.flags |= ~Notification.DEFAULT_SOUND;
//        return notification;
        return builder.build();
    }

    public static Notification getNotification(Context context, Intent intent,
                                                         String title, String text, String channelId) {
        Notification.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) {
                notificationChannel.enableLights(false);
                notificationChannel.enableVibration(false);
//                notificationChannel.setSound(null, Notification.AUDIO_ATTRIBUTES_DEFAULT);
                notificationChannel.setShowBadge(false);
                nm.createNotificationChannel(notificationChannel);
            }
            builder = new Notification.Builder(context, channelId);
        } else {
            builder = new Notification.Builder(context);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 16;
        int side = context.getResources().getDimensionPixelSize(R.dimen.large_padding);
        Bitmap icon = decodeSampleBitmapFromResource(context.getResources(), R.mipmap.ic_launcher, side, side);
        //点击后自动消失
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        // 主标题
        builder.setContentTitle(title);
        // 设置大图标
        builder.setLargeIcon(icon);
        // 设置主要内容
        builder.setContentText(text);
        // 设置小图标
        builder.setPriority(Notification.PRIORITY_LOW);
        builder.setSmallIcon(R.drawable.logo_small);
        builder.setWhen(System.currentTimeMillis());
        PendingIntent pi = PendingIntent.getActivity(context, 1000, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);

//        Notification notification = builder.build();
//        notification.flags |= ~Notification.DEFAULT_SOUND;
//        return notification;
        return builder.build();
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param mContext  上下文
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(1000);
        if (serviceList.isEmpty()) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        Log.i("ViewUtils", "isServiceRunning: "+ isRunning);
        return isRunning;
    }

    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isInteractive();
    }

    public static int dp2px(float density, int dp) {
        if (dp == 0) {
            return 0;
        }
        return (int) (dp * density + 0.5f);
    }

    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static int dp2px(float dp,Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,  Resources.getSystem().getDisplayMetrics());
    }

    public static int sp2px(float sp,Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }


    public static int px2dp(float density, int px) {
        return (int) Math.ceil(px / density);
    }

    public static int sp2px(DisplayMetrics displayMetrics, int sp) {
        if (sp == 0) {
            return 0;
        }
        return (int) (sp * displayMetrics.scaledDensity + 0.5f);
    }

    public static int px2sp(float scaledDensity, int px) {
        return (int) Math.ceil(px / scaledDensity);
    }

    public static int getTextOffset(Paint textPaint) {
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        return (int) Math.ceil(metrics.bottom + metrics.top) / 2;
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    public static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = getScreenHeight();
        final int screenWidth = getScreenWidth();
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

    /**
     * 压缩图片
     *
     * @param resId     图片id
     * @param reqWidth  需要的宽度
     * @param reqHeight 需要的高度
     * @return 压缩后的图片
     */
    public static Bitmap decodeSampleBitmapFromResource(Resources resources, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }


    /**
     * 计算压缩率
     *
     * @param options   图片参数信息
     * @param reqWidth  需要的宽度
     * @param reqHeight 需要的高度
     * @return 压缩率
     */
    private static int calculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int scale = Math.min(options.outWidth / reqWidth, options.outHeight / reqHeight);
        if (scale < 1) {
            scale = 1;
        }
        return scale;
    }

    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

//    public static boolean isScreenOn(Context context) {
//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        return pm.isInteractive();
//    }

    public static String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }

            cursor.close();
        }
        return path;
    }

    /**
     * 计算一屏需要的ecg最大点数
     *
     * @param context 上下文
     * @return ecg数（实际会小于这个数字）
     */
    public static int calculateEcgDataLength(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return (int) (point.x / 25 / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1,
                Resources.getSystem().getDisplayMetrics()) * 512);
    }

    public static int calculateEcgDataLength(int px) {
        return (int) (px / 25 / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1,
                Resources.getSystem().getDisplayMetrics()) * 512);
    }


    public static byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


    public static void loadImageUsingGlide(Context context, int defaultRes, String url, ImageView iv, boolean roundImage) {
        RequestOptions requestOptions = new RequestOptions()
                .error(defaultRes)
                .placeholder(defaultRes)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (roundImage) {
            requestOptions = requestOptions.circleCrop();
        } else {
            requestOptions = requestOptions.centerCrop();
        }
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .asBitmap()
                .load(HttpApi.API_DOWNLOAD_IMG + "?fileId=" + url)
                .into(iv);
    }

    public static void loadImageUsingGlide(Context context, String url, ImageView iv, boolean roundImage) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (roundImage) {
            requestOptions = requestOptions.circleCrop();
        } else {
            requestOptions = requestOptions.centerCrop();
        }
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .asBitmap()
                .load(HttpApi.API_DOWNLOAD_IMG + "?fileId=" + url)
                .into(iv);
    }

    public static void loadBigImageUsingGlide(Context context, int defaultRes, String url, ImageView iv) {
        RequestOptions requestOptions = new RequestOptions()
                .error(defaultRes)
                .placeholder(defaultRes)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        requestOptions = requestOptions.fitCenter();
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .asBitmap()
                .load(HttpApi.API_DOWNLOAD_IMG + "?fileId=" + url)
                .into(iv);
    }

    public static void loadCompanyImage(Context context, int defaultRes, String url, ImageView iv) {
        Glide.with(context)
                .setDefaultRequestOptions(new RequestOptions()
                        .error(defaultRes)
                        .placeholder(defaultRes)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .asBitmap()
                .load(HttpApi.API_DOWNLOAD_IMG + "?fileId=" + url)
                .into(iv);
    }

    public static void hideKeyboard(Context context, View view) {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive()) {
            //拿到view的token 不为空
            if (view.getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 内存泄漏InputMethodManager的解决
     *
     * @param destContext 上下文
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }




}
