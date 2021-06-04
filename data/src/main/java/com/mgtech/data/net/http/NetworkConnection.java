package com.mgtech.data.net.http;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.mgtech.domain.utils.MyConstant;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;


/**
 * Created by zhaixiang on 2017/7/25.
 * 网络请求
 */

class NetworkConnection {
    public static final int GET = 0;
    public static final int POST = 1;

    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";
    private static final MediaType JSON = MediaType.parse(CONTENT_TYPE_VALUE_JSON);
    private static final String Authorization = "Authorization";
    private static final String API_VERSION = "MG_APIVERSION";
    private static final String VERSION = MyConstant.API_VERSION;
    private URL url;
    private String authorizationHeader = "";
    private int method;
    private RequestBody body = RequestBody.create(JSON, "{}");
    private String bodyString = "";
    private String charsetType;
    private OkHttpClient client;
    private String lang;
    private String HEADER_LANG = "MG_LANG";
    public static final String LANG_ZH = Locale.CHINESE.toString();
    public static final String LANG_EN = Locale.ENGLISH.toString();

    private NetworkConnection(String url, int method, int readTimeOut, int writeTimeOut) throws MalformedURLException {
        this.url = new URL(url);
        this.method = method;
        this.client = createClient(readTimeOut, writeTimeOut);
    }

    public static NetworkConnection create(String url, int method) throws MalformedURLException {
        return new NetworkConnection(url, method,10,10);
    }

    private OkHttpClient createClient(int readTimeOut, int writeTimeOut) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .connectTimeout(writeTimeOut, TimeUnit.SECONDS);
        return builder.build();
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
            Log.i("net", l + " body: " + this.bodyString);
            Log.i("net", l + " seq: " + response.header("Seq", ""));
            Log.i("net", l + " Expires: " + response.header("Expires", ""));
            Log.i("net", l + " Last-Modified: " + response.header("Last-Modified", ""));
            Log.i("net", l + " ETag: " + response.header("ETag", ""));
            Log.i("net", l + " Age: " + response.header("Age", ""));
            Log.i("net", l + responseString);
        }else{
            Log.e("net", "error: " + response );
        }
        return responseString;
    }

    private Request.Builder commonRequest() {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader(API_VERSION, VERSION)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON);
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            builder.addHeader(Authorization, authorizationHeader);
        }
        if (lang != null && !lang.isEmpty()) {
            builder.addHeader(HEADER_LANG, lang);
        }
        if (method == POST && body != null) {
            builder.post(body);
        } else if (method == GET) {
            builder.get();
        }
        return builder;
    }

    void addAuthorizationHeader(String authorization) {
        this.authorizationHeader = authorization;
    }

    void setLanguage( String language) {
        this.lang = language;
    }

    public void setBody(String body) {
        this.bodyString = body;
        this.body = RequestBody.create(JSON, body);
    }

    public void setResponseCharsetType(String type) {
        this.charsetType = type;
    }

}
