package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

import com.mgtech.domain.entity.AppConfigNew;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.utils.ShareUtils;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;
import com.mgtech.maiganapp.viewmodel.HealthKnowledgeViewModel;
import com.mgtech.maiganapp.window.SharePopupWindow;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author zhaixiang
 */
public class GuideShareWeekReportActivity extends BaseActivity<DefaultViewModel> {
    private static final String URL = "url";
    private static final String ID = "id";
    WebView webView;
    @Bind(R.id.root)
    View root;
    @Bind(R.id.layout)
    LinearLayout layout;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, GuideShareWeekReportActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        String url = AppConfigNew.getWeeklyReportShareGuideUrl();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(getApplicationContext());
        webView.setLayoutParams(params);
        layout.addView(webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //设定加载开始的操作
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);

            }
        });
        webView.loadUrl(url);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }



    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.setWebViewClient(null);
            layout.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_guide_share_week_report;
    }

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }
}
