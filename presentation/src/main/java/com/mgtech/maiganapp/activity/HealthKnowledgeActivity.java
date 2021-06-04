package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.mgtech.domain.entity.UnreadMessage;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.event.UnreadMessageEvent;
import com.mgtech.maiganapp.utils.ShareUtils;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;
import com.mgtech.maiganapp.viewmodel.HealthKnowledgeViewModel;
import com.mgtech.maiganapp.window.SharePopupWindow;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author zhaixiang
 */
public class HealthKnowledgeActivity extends BaseActivity<HealthKnowledgeViewModel> {
    private static final String URL = "url";
    private static final String ID = "id";
    WebView webView;
    @Bind(R.id.root)
    View root;
    @Bind(R.id.layout)
    LinearLayout layout;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    private String url;
    private SharePopupWindow popupWindow;

    public static Intent getCallingIntent(Context context, String url,String id) {
        Intent intent = new Intent(context, HealthKnowledgeActivity.class);
        intent.putExtra(URL, url);
        intent.putExtra(ID, id);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        url = getIntent().getStringExtra(URL);
        String id = getIntent().getStringExtra(ID);
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
        viewModel.markRead(id);
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
        return R.layout.activity_health_knowledge;
    }

    @OnClick(R.id.btn_share)
    void share() {
//        showShare(url);
        int[] icons = {
                R.drawable.share_wx, R.drawable.share_moments, R.drawable.share_qq,
                R.drawable.share_zone
        };
        final String[] platform = {
                Wechat.NAME, WechatMoments.NAME, QQ.NAME, QZone.NAME
        };
        final String[] texts = {
                getString(R.string.wechat),
                getString(R.string.wechat_moments),
                getString(R.string.qq),
                getString(R.string.qq_zone)
        };
        popupWindow = new SharePopupWindow(this, icons, texts, new SharePopupWindow.Callback() {

            @Override
            public void select(int index) {
                ShareUtils.shareUrl(HealthKnowledgeActivity.this, url, getString(R.string.activity_health_knowledge), "",
                        platform[index % platform.length]);
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

    @OnClick(R.id.btn_back)
    void back() {
        finish();
    }
}
