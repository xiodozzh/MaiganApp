package com.mgtech.maiganapp.utils;

import android.graphics.Bitmap;

import com.bumptech.glide.util.Util;
import com.mgtech.domain.utils.MyConstant;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WXUtils {
    public static final String TRANSACTION_SHARE = "share";
    public static final String TRANSACTION_LOGIN = "login";
    public static final String TRANSACTION_BIND = "bind";

    private static final int THUMB_SIZE = ViewUtils.dp2px(50);

    /**
     *
     * @param api
     * @param bitmap
     * @param scene 分享到对话:
     *              SendMessageToWX.Req.WXSceneSession
     *              分享到朋友圈:
     *              SendMessageToWX.Req.WXSceneTimeline ;
     *              分享到收藏:
     *              SendMessageToWX.Req.WXSceneFavorite
     *
     * @param transaction
     */
    public static void sharePicture(IWXAPI api, Bitmap bitmap, int scene,String transaction, String openId){
        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = ViewUtils.getBitmapByte(thumbBmp);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = transaction;
        req.message = msg;
        req.scene = scene;
        req.userOpenId = openId;
        //调用api接口，发送数据到微信
        api.sendReq(req);
    }


}
