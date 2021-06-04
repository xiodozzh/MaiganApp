package com.mgtech.data.net.http;

import android.accounts.NetworkErrorException;
import android.content.Context;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.TokenResponseEntity;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.domain.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okio.Buffer;


/**
 * Created by zhaixiang on 2017/7/25.
 * 网络请求
 */

class NetworkConnection2 {
    public static final int GET = 0;
    public static final int POST = 1;

    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";
    private static final MediaType JSON = MediaType.parse(CONTENT_TYPE_VALUE_JSON);
    private static final String AUTHORIZATION = "Authorization";
    private static final String API_VERSION = "MG_APIVERSION";
    private static final String VERSION = MyConstant.API_VERSION;
    private URL url;
    private int method;
    private RequestBody body = RequestBody.create(JSON, "{}");
    private String bodyString = "";
    private String charsetType;
    private OkHttpClient client;
    private String lang;
    private static final String HEADER_LANGUAGE = "MG_LANG";
    private String authorizationHeader = "";

     static final String LANG_ZH = Locale.CHINESE.toString();
     static final String LANG_EN = Locale.ENGLISH.toString();
    private static final boolean LOG_OPEN = true;

    private NetworkConnection2(Context context, String url, int method, int readTimeOut, int writeTimeOut, boolean useToken) throws MalformedURLException {
        this.url = new URL(url);
        this.method = method;
        this.client = createClient(context, readTimeOut, writeTimeOut, useToken);
    }

    public static NetworkConnection2 create(Context context, String url, int method, boolean useToken) throws MalformedURLException {
        return new NetworkConnection2(context, url, method, 10, 10, useToken);
    }

    private OkHttpClient createClient(final Context context, int readTimeOut, int writeTimeOut, final boolean useToken) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .connectTimeout(writeTimeOut, TimeUnit.SECONDS);
        if (useToken) {
            builder.authenticator(new Authenticator() {
                @Override
                public Request authenticate(@NonNull Route route, @NonNull Response response) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetResponseEntity<TokenResponseEntity>>() {
                    }.getType();
                    NetResponseEntity<TokenResponseEntity> entity = gson.fromJson(getToken(context), type);
                    Log.i("authenticate", "authenticate: " + entity.toString());
                    if (entity.getCode() == 0 && entity.getData() != null) {
                        TokenResponseEntity tokenResponseEntity = entity.getData();
                        return response.request().newBuilder().addHeader(AUTHORIZATION, tokenResponseEntity.getAccessToken()).build();
                    }
                    return null;
                }
            });
        }
        return builder.build();
    }

    private String getToken(Context context) {
        String phone = SaveUtils.getPhone(context);
        String password = SaveUtils.getPassword(context);
        String zone = SaveUtils.getZone(context);
        String mac = Utils.getPhoneMac(context);
        String body = "UserName=" + phone + "&Password=" + password +
                "&login_type=2&Zone" + zone + "&grant_type=password&device_id=" + mac;
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(HttpApi.API_LOGIN_URL)
                .addHeader(API_VERSION, VERSION)
                .post(RequestBody.create(JSON, body))
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    return responseBody.string();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    String requestSyncCall() throws NetworkErrorException, IOException {
        String responseString = null;
        Request request = commonRequest().build();
        Response response = client.newCall(request).execute();
        if (response.code() == 504) {
            throw new NetworkErrorException();
        }
        if (response.isSuccessful()) {
            if (charsetType != null && !charsetType.isEmpty()) {
                responseString = new String(response.body().bytes(), charsetType);
            } else {
                responseString = response.body().string();
            }
            int l = this.bodyString.length();
            Log.i("net", l + " 请求: " + this.url);
            Log.i("net", l + " 请求: " + request.header(AUTHORIZATION));
            Log.i("net", l + " body: " + this.bodyString);
            Log.i("net", l + " seq: " + response.header("Seq", ""));
            Log.i("net", l + " Expires: " + response.header("Expires", ""));
            Log.i("net", l + " Last-Modified: " + response.header("Last-Modified", ""));
            Log.i("net", l + " ETag: " + response.header("ETag", ""));
            Log.i("net", l + " Age: " + response.header("Age", ""));
            Log.i("net", l + responseString);
        } else {
            Log.i("net", "error 请求: " + this.url);
            Log.i("net", "error body: " + this.bodyString);
            Log.e("net", "error: " + response);
        }
        return responseString;
    }

    private Request.Builder commonRequest() {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader(API_VERSION, VERSION)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON);
        if (!TextUtils.isEmpty(authorizationHeader)) {
            builder.addHeader(AUTHORIZATION, authorizationHeader);
        }
        if (!TextUtils.isEmpty(lang)) {
            builder.addHeader(HEADER_LANGUAGE, lang);
        }
        if (method == POST && body != null) {
            builder.post(body);
        } else if (method == GET) {
            builder.get();
        }
        return builder;
    }

    void setLanguage(String language) {
        this.lang = language;
    }

    public void setBody(String body) {
        this.bodyString = body;
        this.body = RequestBody.create(JSON, body);
    }

    void setToken(String token){
        this.authorizationHeader = token;
    }

    public void setResponseCharsetType(String type) {
        this.charsetType = type;
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
            String language = Locale.getDefault().getLanguage();
            if (!LANG_ZH.equals(language)) {
                language = LANG_EN;
            }
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader(API_VERSION, MyConstant.API_VERSION)
                    .addHeader(HEADER_LANGUAGE, language);

            if (useToken) {
                requestBuilder.addHeader(AUTHORIZATION, "bearer " + SaveUtils.getToken(context));
            } else {
                requestBuilder.addHeader(AUTHORIZATION, "Basic " +
                        Utils.Base64_Encode(NetConstant.clientId + ":" + NetConstant.clientSecret));
            }
            return chain.proceed(requestBuilder.build());
        }
    }

    private static class LogInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request original = chain.request();
            Response response = chain.proceed(original);
            if (LOG_OPEN) {
                Log.i("net", " 请求: " + original.url());
                Log.i("net", " Authorization: " + original.header(AUTHORIZATION));
                if (original.body() != null) {
                    Buffer buffer = new Buffer();
                    original.body().writeTo(buffer);
                    Log.i("net", " body: " + buffer.toString());
                }
                Log.i("net", " seq: " + response.header("Seq", ""));
                Log.i("net", "response: " + response);
            }
            return response;
        }
    }
}
