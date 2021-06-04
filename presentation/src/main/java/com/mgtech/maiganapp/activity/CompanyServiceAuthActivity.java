package com.mgtech.maiganapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.CompanyModel;
import com.mgtech.maiganapp.viewmodel.CompanyServiceAuthViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Jesse
 */
public class CompanyServiceAuthActivity extends BaseActivity<CompanyServiceAuthViewModel> {
    @Bind(R.id.tv_content)
    TextView tvContent;
    private CompanyModel companyModel;

    public static Intent getCallingIntent(Context context){
        return new Intent(context,CompanyServiceAuthActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        initProtocol();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        viewModel.pageUrl.observe(this, s -> {
            startActivity(CompanyServiceActivity.getCallingIntent(this, s, SaveUtils.getUserId()));
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void receiveCompanyModel(CompanyModel company){
        this.companyModel = company;
    }

    @OnClick(R.id.btn_agree)
    public void clickAgree(){
        viewModel.getAuthorCode(companyModel);
    }

    @OnClick(R.id.btn_disagree)
    public void clickDisagree(){
        finish();
    }

    private void initProtocol() {
        String preString = getString(R.string.permission_service_user_pre_text) +" ";
        String postString = getString(R.string.permission_service_user_post_text);
        String privacyString = getString(R.string.permission_service_user);

        SpannableString ss = new SpannableString(preString + privacyString + postString);
        int protocolStart = preString.length();
        int protocolEnd = protocolStart + privacyString.length();
        ss.setSpan(new PermissionProtocolSpan(this), protocolStart, protocolEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvContent.setText(ss);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

    class PermissionProtocolSpan extends ClickableSpan {
        private WeakReference<Activity> weakReference;

        PermissionProtocolSpan(Activity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onClick(@NonNull View view) {
            Activity activity = weakReference.get();
            if (activity != null) {
                activity.startActivity(PermissionServiceAuthActivity.getCallingIntent(activity));
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

    @Override
    protected int getContentViewId() {
        return R.layout.activity_company_service_auth;
    }
}
