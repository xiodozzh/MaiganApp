package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.SetFontSizeViewModel;
import com.mgtech.maiganapp.widget.FontSizePickView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class SetFontSizeActivity extends BaseActivity<SetFontSizeViewModel> {
    @Bind(R.id.fontSizePickView)
    FontSizePickView fontSizePickView;
    @Bind(R.id.tv_item_title)
    TextView tvTitle;
    @Bind(R.id.tv_item_title1)
    TextView tvTitle1;
    @Bind(R.id.tv_item_title2)
    TextView tvTitle2;
    @Bind(R.id.tv_item_content1)
    TextView tvContent1;
    @Bind(R.id.tv_item_content2)
    TextView tvContent2;


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SetFontSizeActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        fontSizePickView.setListener(new FontSizePickView.SelectListener() {
            @Override
            public void onFontSizeLevel(int index) {
                setDisplayFontSize(index);
                setResult(RESULT_OK);
            }
        });
        int size = SaveUtils.getFontSizeIndex(this);
        setDisplayFontSize(size);
        fontSizePickView.setIndex(size);
    }

    //重写字体缩放比例 api<25
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }


    private void setDisplayFontSize(int index) {
        SaveUtils.saveFontSizeIndex(this, index);
        float scale = Utils.getFontSizeScale(index);
        int titleSize = 16;
        int contentSize = 14;
        tvTitle.setTextSize(scale * contentSize);
        tvTitle1.setTextSize(scale * titleSize);
        tvTitle2.setTextSize(scale * titleSize);
        tvContent1.setTextSize(scale * contentSize);
        tvContent2.setTextSize(scale * contentSize);
    }

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_set_font_size;
    }
}
