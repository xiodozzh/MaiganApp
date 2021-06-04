package com.mgtech.maiganapp.widget;

import android.app.Activity;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.activity.PermissionProtocolActivity;

import java.lang.ref.WeakReference;

/**
 * @author Jesse
 */
public class PermissionProtocolSpan extends ClickableSpan {
    private WeakReference<Activity> weakReference;

    public PermissionProtocolSpan(Activity activity) {
        this.weakReference = new WeakReference<>(activity);
    }

    @Override
    public void onClick(@NonNull View view) {
        Activity activity = weakReference.get();
        if (activity != null) {
            activity.startActivity(PermissionProtocolActivity.getCallingIntent(activity));
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
