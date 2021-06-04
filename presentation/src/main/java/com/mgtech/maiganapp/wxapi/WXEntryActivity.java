package com.mgtech.maiganapp.wxapi;

import android.content.Intent;

import androidx.annotation.Nullable;

import android.widget.Toast;

import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;

/**
 * @author zhaixiang
 */
public class WXEntryActivity extends WechatHandlerActivity  {
    @Override
    public void onGetMessageFromWXReq(WXMediaMessage msg) {
        Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
        startActivity(iLaunchMyself);
    }

    //#if def{lang} == cn
    /**
     * 处理微信向第三方应用发起的消息
     * <p>
     * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
     * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
     * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
     * 回调。
     * <p>
     * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
     */
    //#elif def{lang} == en
    /**
     * Handling message which sent from wechat
     * <p>
     * This message will be sent from wechat by chlicking this app-type
     * message in the chatting list
     */
    //#endif
    @Override
    public void onShowMessageFromWXReq(WXMediaMessage msg) {
        if (msg != null && msg.mediaObject != null
                && (msg.mediaObject instanceof WXAppExtendObject)) {
            WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
            Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
        }
    }
}
//public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
//    private static final String TAG = "WXEntryActivity";
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ((MyApplication) getApplication()).getWXApi()
//                .handleIntent(getIntent(), this);
//    }
//
//    @Override
//    public void onReq(BaseReq baseReq) {
//
//    }
//
//    @Override
//    public void onResp(BaseResp baseResp) {
//        Log.e(TAG, "onResp: "+baseResp.errCode);
//        switch (baseResp.errCode) {
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                Toast.makeText(this, "微信登录失败", Toast.LENGTH_SHORT).show();
//                break;
//            case BaseResp.ErrCode.ERR_OK:
//                String code = ((SendAuth.Resp) baseResp).code;
//                String state = ((SendAuth.Resp) baseResp).state;
//                Log.i(TAG, "onResp: " + state);
//                authoritySuccess(code);
//            default:
//        }
//        finish();
//    }
//
//
//    private void authoritySuccess(String code){
//        EventBus.getDefault().postSticky(new WeChatAuthorEvent(code));
//    }
//
//}
