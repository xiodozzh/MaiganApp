package com.mgtech.maiganapp.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.mgtech.domain.utils.FileUtil;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 */
public class ShareUtils {
    /**
     * 分享图片
     *
     * @param context      上下文
     * @param bitmap       图片
     * @param title        标题
     * @param platformName 平台
     */
    public static void shareBitmap(final Activity context, final Bitmap bitmap, final String title, final String platformName) {
        Single.create(new Single.OnSubscribe<Uri>() {
            @Override
            public void call(SingleSubscriber<? super Uri> singleSubscriber) {
                File file = FileUtil.saveTempBitmap(bitmap);
                singleSubscriber.onSuccess(Uri.fromFile(file));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Uri uri) {
                        OnekeyShare oks = new OnekeyShare();
                        oks.setPlatform(platformName);
                        //关闭sso授权
                        oks.disableSSOWhenAuthorize();
                        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                        oks.setTitle(title);
                        oks.setTitleUrl(MyConstant.WEBSITE);
                        // text是分享文本，所有平台都需要这个字段
                        oks.setText(title);
                        // url仅在微信（包括好友和朋友圈）中使用
                        oks.setImagePath(uri.getPath());
                        // site是分享此内容的网站名称，仅在QQ空间使用
                        oks.setSite(context.getString(R.string.app_name));
                        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                        final String path = uri.getPath();
                        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
                            @Override
                            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                                if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
                                    paramsToShare.setText(null);
                                    paramsToShare.setTitle(null);
                                    paramsToShare.setTitleUrl(null);
                                    paramsToShare.setImagePath(path);
                                } else if (platform.getName().equalsIgnoreCase(QZone.NAME)) {
                                    paramsToShare.setText(null);
                                    paramsToShare.setTitle(null);
                                    paramsToShare.setTitleUrl(null);
                                    paramsToShare.setImagePath(path);
                                }

                            }
                        });
                        oks.show(context);
                    }
                });
    }

    public static void copyLink(Context context, String tag,String url){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText(tag, url);
        cm.setPrimaryClip(mClipData);
    }


    public static void shareUrl(final Activity context, final String url, final String title,
                                final String text, final String platformName) {
        Single.create(new Single.OnSubscribe<Uri>() {
            @Override
            public void call(SingleSubscriber<? super Uri> singleSubscriber) {
                File file = FileUtil.saveTempBitmap(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.logo_normal));
                singleSubscriber.onSuccess(Uri.fromFile(file));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Uri uri) {
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setShareType(Platform.SHARE_WEBPAGE);
                        sp.setTitle(title);
                        sp.setTitleUrl(url);
                        sp.setText(text);
                        sp.setImagePath(uri.getPath());
                        sp.setUrl(url);
                        sp.setSite(context.getString(R.string.app_name));
                        sp.setSiteUrl(MyConstant.WEBSITE);
                        Platform platform = ShareSDK.getPlatform(platformName);
                        platform.share(sp);
                    }
                });
    }

}
