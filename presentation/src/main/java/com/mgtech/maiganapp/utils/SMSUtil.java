//package com.mgtech.maiganapp.utils;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.widget.Toast;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import cn.smssdk.EventHandler;
//import cn.smssdk.SMSSDK;
//
///**
// * 短信验证
// */
//public class SMSUtil {
//
//    private static final String AppKey = "16491bc645503";
//    private static final String AppSecrete = "f08646247f4b545e81f960e73a93e93a";
//
//    public static void sendShortMessage(final Context context, String phone) {
//        SMSSDK.initSDK(context, AppKey, AppSecrete);
//        EventHandler eh = new EventHandler() {
//
//            @Override
//            public void afterEvent(int event, int result, Object data) {
//                if (result == SMSSDK.RESULT_COMPLETE) {
//                    //回调完成
//                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                        // 提交验证码成功,启用服务端验证，不会进入此方法
//                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
//                        //获取验证码成功
//                        Toast.makeText(context, "验证码已经发送", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Throwable throwable = (Throwable) data;
//                    throwable.printStackTrace();
//                    JSONObject object;
//                    try {
//                        object = new JSONObject(throwable.getMessage());
//                        String des = object.optString("detail");// 错误描述
//                        int status = object.optInt("status");// 错误代码
//                        if (status > 0 && !TextUtils.isEmpty(des)) {
//                            Toast.makeText(context, des, Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                //启用服务器端验证，此处注销掉回掉函数
//                SMSSDK.unregisterAllEventHandler();
//            }
//        };
//        SMSSDK.registerEventHandler(eh); // 注册短信回调
//
//        // 通过sdk发送短信验证
//        SMSSDK.getVerificationCode("86", phone);
//
//    }
//
//}
