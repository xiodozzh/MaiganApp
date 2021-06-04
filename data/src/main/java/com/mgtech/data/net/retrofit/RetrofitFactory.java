package com.mgtech.data.net.retrofit;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mgtech.data.net.http.CacheUtil;
import com.mgtech.domain.entity.net.request.RefreshTokenRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.RefreshTokenResponse;
import com.mgtech.domain.exception.RefreshTokenErrorException;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author zhaixiang
 */
public class RetrofitFactory {
    private static final String TAG = RetrofitFactory.class.getSimpleName();

    private static final String HEADER_LANGUAGE = "Language";
    private static final String HEADER_TOKEN = "AccessToken";
    private static final String HEADER_API_VERSION = "APIVersion";
    private static final String HEADER_TIMEZONE = "TimeZone";
    private static final String HEADER_PHONE_INFO = "PhoneInfo";
    private static final String HEADER_APP_INFO = "AppInfo";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    private static final String CONTENT_TYPE_VALUE_JSON = "application/json";
    private static final String GET = "GET";

    private static final String VERSION_VALUE = MyConstant.API_VERSION;
    private static final boolean LOG_OPEN = true;

    private String baseUrl;
    private boolean useToken;
    private static final String PHONE_INFO = "{" +
            "\"platform\": \""+"android "+ "\"," +
            "\"systemVersion\": \"" + Utils.getSystemVersion() +"\"," +
            "\"phoneModel\": \"" + Utils.getBand() + "\"}";
    private static Gson gson = new GsonBuilder().
            registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();


    public RetrofitFactory setBaseUrl(String url) {
        this.baseUrl = url;
        return this;
    }

    public RetrofitFactory setUseToken(boolean useToken) {
        this.useToken = useToken;
        return this;
    }


    public Retrofit build(Context context) {
        return create(context);
    }

    private Retrofit create(final Context context) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new HeaderInterceptor(useToken, context));
        okHttpClientBuilder.addNetworkInterceptor(new LogInterceptor());
        okHttpClientBuilder.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                refreshToken(context);
                return response.request().newBuilder()
                        .removeHeader(HEADER_TOKEN)
                        .addHeader(HEADER_TOKEN, SaveUtils.getToken(context))
                        .build();
            }
        });
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClientBuilder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    public static class StringAdapter extends TypeAdapter<String> {
        @Override
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        @Override
        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

//    private Observable<NetResponseEntity<LoginResponseEntity>> loginByPhone(Context context) {
//        String phone = SaveUtils.getPhone(context);
//        String password = SaveUtils.getPassword(context);
//        String zone = SaveUtils.getZone(context);
//        String mac = Utils.getPhoneMac(context);
//        AccountApi accountApi = new RetrofitFactory().setUseToken(false)
//                .setBaseUrl(HttpApi.API_LOGIN_URL).create(context).create(AccountApi.class);
//        return accountApi.login(new LoginByPhoneRequestEntity(phone,password,zone,mac,true));
//    }

    private synchronized static void refreshToken(Context context) throws IOException {
        Logger.e("refreshToken");
        String language = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
        String appInfo = "{" +
                "\"version\": \"" + Utils.getVersionName(context) + "\"," +
                "\"buildVersion\": \"" + Utils.getVersionCode(context) + "\"," +
                "\"appConfigKey\": \"mystrace\"," +
                "\"type\": " + MyConstant.BRACELET_TYPE +
                "}";
        String timeZone = new SimpleDateFormat("'GMT'ZZZZZ", Locale.getDefault()).format(new Date());
        String refreshToken = SaveUtils.getRefreshToken();
        String token = SaveUtils.getToken(context);
        String id = SaveUtils.getUserId(context);
        RefreshTokenRequestEntity entity = new RefreshTokenRequestEntity(id,refreshToken);
        Gson gson = new Gson();
        Request request = new Request.Builder()
                .url(HttpApi.API_REFRESH_TOKEN)
                .addHeader(HEADER_API_VERSION, VERSION_VALUE)
                .addHeader(HEADER_TIMEZONE, timeZone)
                .addHeader(HEADER_PHONE_INFO, PHONE_INFO)
                .addHeader(HEADER_APP_INFO, appInfo)
                .addHeader(HEADER_LANGUAGE, language)
                .addHeader(HEADER_TOKEN, token)
                .post(RequestBody.create(MediaType.parse(CONTENT_TYPE_VALUE_JSON), gson.toJson(entity)))
                .build();
        Response response = new OkHttpClient.Builder().build().newCall(request).execute();
        RefreshTokenResponse refreshTokenResponse;
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            if (body != null) {
                String responseString = body.string();
                Logger.i("token: "+token + "refresh:"+refreshToken+" \ntokenSuccess: " + responseString);
                Type type = new TypeToken<NetResponseEntity<RefreshTokenResponse>>() {
                }.getType();
                NetResponseEntity<RefreshTokenResponse> netResponseEntity = gson.fromJson(responseString, type);
                if (netResponseEntity.getCode() == 0) {
                    refreshTokenResponse = netResponseEntity.getData();
                    SaveUtils.saveToken(refreshTokenResponse.getAccessToken(), refreshTokenResponse.getRefreshToken(),
                            refreshTokenResponse.getExpiresTime());
                } else{
                    Logger.e("Objects.equals(SaveUtils.getRefreshToken(), refreshToken)");
                    if (logoutIfInvalid()) {
                        throw new RefreshTokenErrorException("token refresh error w");
                    }
                }
            } else {
                Logger.e("token refresh error: " + response);
                logoutIfInvalid();
                throw new RefreshTokenErrorException("token refresh error");
            }
        } else {
            Logger.e("error: " + response + "\n" + entity.toString());
            logoutIfInvalid();
            throw new RefreshTokenErrorException("token refresh error");
        }
    }

    private synchronized static boolean logoutIfInvalid() {
        boolean clear = false;
        Logger.e("logoutIfInvalid " + SaveUtils.getTokenTime()
                + " " +Calendar.getInstance().getTimeInMillis());
        if (SaveUtils.getTokenTime()  < Calendar.getInstance().getTimeInMillis()) {
            SaveUtils.saveToken("", "", 0);
            clear = true;
        }
        return clear;
    }


    private static class HeaderInterceptor implements Interceptor {
        private boolean useToken;
        private Context context;

        HeaderInterceptor(boolean useToken, Context context) {
            this.useToken = useToken;
            this.context = context;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request original = chain.request();
            String language = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
            String appInfo = "{" +
                    "\"version\": \"" + Utils.getVersionName(context) + "\"," +
                    "\"buildVersion\": \"" + Utils.getVersionCode(context) + "\"," +
                    "\"appConfigKey\": \"mystrace\"," +
                    "\"type\": " + MyConstant.BRACELET_TYPE +
                    "}";
            String timeZone = new SimpleDateFormat("'GMT'ZZZZZ", Locale.getDefault()).format(new Date());
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader(HEADER_API_VERSION, VERSION_VALUE)
//                    .addHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_VALUE_JSON)
                    .addHeader(HEADER_TIMEZONE, timeZone)
                    .addHeader(HEADER_PHONE_INFO, PHONE_INFO)
                    .addHeader(HEADER_APP_INFO, appInfo)
                    .addHeader(HEADER_LANGUAGE, language);
            if (!TextUtils.isEmpty(SaveUtils.getToken(context))) {
                requestBuilder.addHeader(HEADER_TOKEN, SaveUtils.getToken(context));
            }
            return chain.proceed(requestBuilder.build());
        }
    }

    private static class LogInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request original = chain.request();
            long time = System.currentTimeMillis();
            Response response = chain.proceed(original);

            if (LOG_OPEN) {
                String log = original.url() + " headers: \n" + original.headers();
                if (original.body() != null) {
                    Buffer buffer = new Buffer();
                    original.body().writeTo(buffer);
                    log += buffer.readString(Charset.defaultCharset());
                }
                ResponseBody logBody = response.peekBody(1024 * 10);
//                String s = log.source().readString(Charset.defaultCharset());
//                int index = 0;
//                while (index < s.length()){
//                    int next = Math.min(300,s.length()-index);
//                    Log.i("net", s.substring(index,index+next));
//                    index+=next;
//                }
                log += "\nresponse: " + logBody.source().readString(Charset.defaultCharset());
                log += "\ntotalTime: " + (System.currentTimeMillis() - time);
                Logger.i(log);
            }
            return response;
        }
    }

    private static class CacheInterceptor implements Interceptor {
        private Context context;
        private boolean useCache;
        private boolean saveCache;

        CacheInterceptor(Context context, boolean useCache, boolean saveCache) {
            this.context = context;
            this.useCache = useCache;
            this.saveCache = saveCache;
        }

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request original = chain.request();
            Response response;
            CacheUtil cacheUtil = CacheUtil.getInstance();
            String key;
            if (GET.equalsIgnoreCase(original.method())) {
                key = MyConstant.API_VERSION + original.url();
            } else {
                RequestBody body = original.body();
                if (body != null) {
                    Buffer buffer = new Buffer();
                    body.writeTo(buffer);
                    key = MyConstant.API_VERSION + original.url() + buffer.toString();
                } else {
                    key = MyConstant.API_VERSION + original.url();
                }
            }
            if (!useCache) {
                response = chain.proceed(original);
                if (saveCache) {
                    save(key, response);
                }
            } else {
                String cache;
                cache = cacheUtil.get(context, key);
                if (cache.isEmpty()) {
                    return null;
                } else {
                    response = new Response.Builder()
                            .body(ResponseBody.create(MediaType.parse(CONTENT_TYPE_VALUE_JSON), cache))
                            .request(original)
                            .protocol(Protocol.HTTP_1_1)
                            .code(200)
                            .message("OK")
                            .build();
                }

            }

            return response;
        }

        private void save(String url, Response netResponse) {
            try {
                ResponseBody body = netResponse.peekBody(Integer.MAX_VALUE);
                CacheUtil cacheUtil = CacheUtil.getInstance();
                cacheUtil.save(context, url, body.string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
