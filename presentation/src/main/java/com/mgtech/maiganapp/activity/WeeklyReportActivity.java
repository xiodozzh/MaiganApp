package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.service.NetJobService;
import com.mgtech.maiganapp.utils.ShareUtils;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;
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
public class WeeklyReportActivity extends BaseActivity<DefaultViewModel> {
    private static final String WEEK_URL = "url";
    private static final String WEEK_ID = "id";
    private static final String WEEK_TITLE = "text";
    private static final String WEEK_TEXT = "text";
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.root)
    View root;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    private String url;
    private String title;
    private String text;
    private SharePopupWindow popupWindow;

    public static Intent getCallingIntent(Context context, String url, String title, String text,String id) {
        Intent intent = new Intent(context, WeeklyReportActivity.class);
        intent.putExtra(WEEK_URL, url);
        intent.putExtra(WEEK_TITLE, title);
        intent.putExtra(WEEK_TEXT, text);
        intent.putExtra(WEEK_ID, id);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        Intent intent = getIntent();
        url = intent.getStringExtra(WEEK_URL);
        String id = intent.getStringExtra(WEEK_ID);
//        url = "https://www.jianshu.com/p/fa1ee3912d90";
        title = intent.getStringExtra(WEEK_TITLE);
        text = intent.getStringExtra(WEEK_TEXT);
        if (title == null) {
            title = "";
        }
        if (text == null) {
            text = "";
        }
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        webView = new WebView(getApplicationContext());
//        webView.setLayoutParams(params);
//        layout.addView(webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);

//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //设定加载开始的操作
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        webView.loadUrl(url);
        NetJobService.markWeeklyReportRead(this,id);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {
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
//            layout.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_weekly_report;
    }

    @OnClick(R.id.btn_share)
    void share() {
        int[] icons = {
                R.drawable.share_wx, R.drawable.share_moments, R.drawable.share_qq,
                R.drawable.share_zone,R.drawable.share_link
        };
        final String[] texts = {
                getString(R.string.wechat), getString(R.string.wechat_moments), getString(R.string.qq), getString(R.string.qq_zone),getString(R.string.share_copy_link)
        };
        final String[] platform = {
                Wechat.NAME, WechatMoments.NAME, QQ.NAME, QZone.NAME
        };
        popupWindow = new SharePopupWindow(this, icons, texts, new SharePopupWindow.Callback() {

            @Override
            public void select(int index) {
                if (index == 4){
                    ShareUtils.copyLink(WeeklyReportActivity.this,"weekly report",url);
                    Toast.makeText(WeeklyReportActivity.this, getString(R.string.link_copy_success), Toast.LENGTH_SHORT).show();
                }else {
                    ShareUtils.shareUrl(WeeklyReportActivity.this, url, title, text,
                            platform[index % platform.length]);
                }
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
