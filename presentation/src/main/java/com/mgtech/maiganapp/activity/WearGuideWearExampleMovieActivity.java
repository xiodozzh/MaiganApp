package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;
import com.mgtech.maiganapp.viewmodel.WearGuideWearMethodViewModel;

import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * @author zhaixiang
 */
public class WearGuideWearExampleMovieActivity extends BaseActivity<DefaultViewModel> {
    private static final String INDEX = "index";
    @Bind(R.id.gif)
    GifImageView ivGif;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    private final int[] gifResources = new int[]{
            R.drawable.wear_guide_recoginize_pw_correct_gif,
            R.drawable.wear_guide_recoginize_pw_error_gif1,
            R.drawable.wear_guide_recoginize_pw_error_gif2,
            R.drawable.wear_guide_recoginize_pw_error_gif3
    };

    public static Intent getCallingIntent(Context context, int index) {
        Intent intent = new Intent(context, WearGuideWearExampleMovieActivity.class);
        intent.putExtra(INDEX, index);
        return intent;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        int index = getIntent().getIntExtra(INDEX, 0);
        String title;
        if (index == 0){
            title = "正确脉图";
        }else{
            title = "错误脉图"+ index;
        }
        tvTitle.setText(title);

        GifDrawable gifFromResource;
        try {
            gifFromResource = new GifDrawable(getResources(), gifResources[index]);
            gifFromResource.setLoopCount(0);
            ivGif.setBackground(gifFromResource);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_wear_guide_example_movie;
    }
}
