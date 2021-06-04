package com.mgtech.maiganapp.utils;

import com.mgtech.domain.utils.NetConstant;
import com.mgtech.maiganapp.data.event.LoginOtherEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @author zhaixiang
 */
public class LoginUtils {

    public static void loginQQ(){
        final Platform plat = ShareSDK.getPlatform(QQ.NAME);
        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Logger.d(hashMap);
                String openId = null;
                String unionId = null;
                String token = null;
                String url = (String) hashMap.get("figureurl_qq_2");
                String name = (String) hashMap.get("nickname");
                if (i == Platform.ACTION_USER_INFOR) {
                    PlatformDb platDB = platform.getDb();
                    token = platDB.getToken();
                    openId = platDB.getUserId();
                    unionId = platDB.get("unionid");
                }
                EventBus.getDefault().post(new LoginOtherEvent(LoginOtherEvent.SUCCESS,openId,unionId,token,name,url,
                        NetConstant.QQ_LOGIN));
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();
                EventBus.getDefault().post(new LoginOtherEvent(LoginOtherEvent.FAIL, NetConstant.QQ_LOGIN));
            }

            @Override
            public void onCancel(Platform platform, int i) {
                EventBus.getDefault().post(new LoginOtherEvent(LoginOtherEvent.CANCEL, NetConstant.QQ_LOGIN));
            }
        });
        plat.showUser(null);
    }

    public static void loginWeChat(){
        final Platform plat = ShareSDK.getPlatform(Wechat.NAME);
        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Logger.d(hashMap);
                String openId = null;
                String unionId = null;
                String token = null;
                String url = (String) hashMap.get("figureurl_qq_2");
                String name = (String) hashMap.get("nickname");
                if (i == Platform.ACTION_USER_INFOR) {
                    PlatformDb platDB = platform.getDb();
                    token = platDB.getToken();
                    openId = platDB.getUserId();
                    unionId = platDB.get("unionid");
                }
                EventBus.getDefault().post(new LoginOtherEvent(LoginOtherEvent.SUCCESS,openId,unionId,token,name,url,
                        NetConstant.WE_CHAT_LOGIN));

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();
                EventBus.getDefault().post(new LoginOtherEvent(LoginOtherEvent.FAIL, NetConstant.WE_CHAT_LOGIN));
            }

            @Override
            public void onCancel(Platform platform, int i) {
                EventBus.getDefault().post(new LoginOtherEvent(LoginOtherEvent.CANCEL, NetConstant.WE_CHAT_LOGIN));
            }
        });
        plat.showUser(null);
    }
}
