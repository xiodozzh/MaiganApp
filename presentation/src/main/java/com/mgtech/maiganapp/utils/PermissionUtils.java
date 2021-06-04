package com.mgtech.maiganapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Jesse
 */
public class PermissionUtils {
    public static boolean isNotificationPermissionOpen(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    public static boolean isStoragePermissionOpen(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED&& ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isLocationPermissionOpen(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isLocationPermissionDenied(Context context){
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED;
    }

    public static boolean isCameraPermissionOpen(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isContactPermissionOpen(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isCallPhonePermissionOpen(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void openPermission(Activity context,List<String> permissions,int requestCode){
        Iterator<String>iterator = permissions.iterator();
        while (iterator.hasNext()){
            String permission = iterator.next();
            if(ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED){
                iterator.remove();
            }
        }
        if (permissions.isEmpty()){
            return;
        }
        String[] p = new String[permissions.size()];
        for (int i = 0; i < p.length; i++) {
            p[i] = permissions.get(i);
        }
        ActivityCompat.requestPermissions(
                context,
                p,
                requestCode
        );
    }

    /**
     * 跳转到权限设置界面
     */
    public static void goToAppDetailSettingIntent(Activity activity) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(intent);
    }

    public static void requestNotificationPermission(Activity context) {
        goToAppDetailSettingIntent(context);
    }

    public static void requestStoragePermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                requestCode
        );
    }

    public static void requestLocationPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                requestCode
        );
    }

    public static void requestCameraPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        Manifest.permission.CAMERA
                },
                requestCode
        );
    }

    public static void requestReadContactPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        Manifest.permission.READ_CONTACTS
                },
                requestCode
        );
        ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS);
    }

    public static void requestCallPhonePermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        Manifest.permission.CALL_PHONE
                },
                requestCode
        );
    }
}
