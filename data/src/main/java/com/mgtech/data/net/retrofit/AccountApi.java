package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.request.BindAccountRequestEntity;
import com.mgtech.domain.entity.net.request.GetVerificationCodeEntity;
import com.mgtech.domain.entity.net.request.LoginByOtherAppRequestEntity;
import com.mgtech.domain.entity.net.request.LoginByPhoneRequestEntity;
import com.mgtech.domain.entity.net.request.LoginRequestEntity;
import com.mgtech.domain.entity.net.request.RefreshTokenRequestEntity;
import com.mgtech.domain.entity.net.request.RegisterRequestEntity;
import com.mgtech.domain.entity.net.request.ResetPasswordRequestEntity;
import com.mgtech.domain.entity.net.request.VerifyRequestEntity;
import com.mgtech.domain.entity.net.response.BindAccountResponseEntity;
import com.mgtech.domain.entity.net.response.LoginResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.RefreshTokenResponse;
import com.mgtech.domain.entity.net.response.RegisterResponseEntity;
import com.mgtech.domain.entity.net.response.ResetPasswordResponseEntity;
import com.mgtech.domain.entity.net.response.WXGetInfoResponseEntity;
import com.mgtech.domain.entity.net.response.WXLoginResponseEntity;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;

import java.lang.reflect.Type;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

/**
 * @author zhaixiang
 */
public interface AccountApi {

    /**
     * 登录
     *
     * @param entity     登录错误
     * @return 登录结果
     */
    @POST(HttpApi.API_LOGIN_URL)
    Single<NetResponseEntity<LoginResponseEntity>> login(@Body LoginByPhoneRequestEntity entity);


    @POST(HttpApi.API_LOGIN_BY_OTHER_APP_URL)
    Single<NetResponseEntity<LoginResponseEntity>> loginByOtherApp(@Body LoginByOtherAppRequestEntity entity);

    @POST(HttpApi.API_BIND_ACCOUNT)
    Single<NetResponseEntity<BindAccountResponseEntity>> bindAccount(@Body BindAccountRequestEntity entity);

    /**
     * 注册
     *
     * @param entity 注册信息
     * @return 登录结果
     */
    @POST(HttpApi.API_REGISTER_URL)
    Observable<NetResponseEntity<RegisterResponseEntity>> register(@Body RegisterRequestEntity entity);


    /**
     * 短信验证
     *
     * @param entity 验证信息
     * @return 验证结果
     */
    @POST("api/Register/MessageValidate")
    Observable<NetResponseEntity> verify(@Body VerifyRequestEntity entity);

    /**
     * 重置密码
     *
     * @param entity 新密码
     * @return 结果
     */
    @POST(HttpApi.API_RESET_PASSWORD_URL)
    Observable<NetResponseEntity> resetPassword(@Body ResetPasswordRequestEntity entity);

    /**
     * 获取验证码
     *
     * @param phone 手机号
     * @param zone 区号
     * @param type 验证码类型 0 注册，1 忘记密码（不需要token）,2 修改密码（需要token）
     * @return 是否发送成功
     */
    @GET(HttpApi.API_GET_VERIFICATION_CODE)
    Observable<NetResponseEntity> getVerificationCode(@Query("phone") String phone, @Query("zone")String zone, @Query("Type")int type);


    @POST(HttpApi.API_REFRESH_TOKEN)
    Single<NetResponseEntity<RefreshTokenResponse>> refreshToken(@Body RefreshTokenRequestEntity entity);


    @GET("sns/oauth2/access_token")
    Single<WXLoginResponseEntity> getWXToken(@Query("appid")String appId, @Query("secret")String secret, @Query("code")String code,
                                             @Query("grant_type")String grantType);


    @GET("sns/userinfo")
    Single<WXGetInfoResponseEntity> getWXInfo(@Query("access_token")String token, @Query("openid")String openId);
}
