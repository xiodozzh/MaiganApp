package com.mgtech.maiganapp.widget;

import android.app.Activity;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.activity.PermissionPrivacyActivity;

import java.lang.ref.WeakReference;

/**
 * @author Jesse
 */
public class PermissionPrivacySpan extends ClickableSpan {
    private WeakReference<Activity> weakReference;

    public PermissionPrivacySpan(Activity activity) {
        this.weakReference = new WeakReference<>(activity);
    }

    @Override
    public void onClick(@NonNull View view) {
        Activity activity = weakReference.get();
        if (activity != null) {
            activity.startActivity(PermissionPrivacyActivity.getCallingIntent(activity));
        }
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        Activity activity = weakReference.get();
        if (activity != null) {
            ds.setColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        }
    }
}
