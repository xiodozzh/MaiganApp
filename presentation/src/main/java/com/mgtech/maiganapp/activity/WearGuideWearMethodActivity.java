package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.WearGuideWearMethodViewModel;
import com.orhanobut.logger.Logger;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author zhaixiang
 */
public class WearGuideWearMethodActivity extends BaseActivity<WearGuideWearMethodViewModel> {
    @Bind(R.id.videoPlayer)
    StandardGSYVideoPlayer videoPlayer;
//    private OrientationUtils orientationUtils;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, WearGuideWearMethodActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        String source1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.guide).toString();
        videoPlayer.setUp(source1, false, "");
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        videoPlayer.getFullscreenButton().setVisibility(View.GONE);
        //设置旋转
//        orientationUtils = new OrientationUtils(this, videoPlayer);
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Logger.i("create Activity");
        videoPlayer.startPlayLogic();

        SaveUtils.guideVideoWatched();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        GSYVideoManager.releaseAllVideos();
//        if (orientationUtils != null) {
//            orientationUtils.releaseListener();
//        }
        videoPlayer.setVideoAllCallBack(null);
        super.onDestroy();

    }


    @OnClick(R.id.btn_back)
    public void back() {
        onBackPressed();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_wear_guide_wear_method;
    }
}
