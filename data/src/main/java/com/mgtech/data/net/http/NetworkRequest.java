//package com.mgtech.data.net.http;
//
//import android.accounts.NetworkErrorException;
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import androidx.annotation.IntDef;
//import android.util.Log;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.mgtech.domain.entity.Mapper;
//import com.mgtech.domain.entity.net.RequestEntity;
//import com.mgtech.domain.entity.net.response.NetResponseEntity;
//import com.mgtech.domain.entity.net.response.TokenResponseEntity;
//import com.mgtech.domain.utils.HttpApi;
//import com.mgtech.domain.utils.MyConstant;
//import com.mgtech.domain.utils.NetConstant;
//import com.mgtech.domain.utils.SaveUtils;
//import com.mgtech.domain.utils.Utils;
//
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.net.MalformedURLException;
//import java.util.Calendar;
//import java.util.Locale;
//
//import rx.Observable;
//import rx.Subscriber;
//import rx.functions.Func1;
//import rx.schedulers.Schedulers;
//
///**
// * Created by zhaixiang on 2017/7/26.
// * 网络请求
// */
//
//public class NetworkRequest {
//    private static final String TAG = "NetworkRequest";
//    public static final int GET = 0;
//    public static final int POST = 1;
//    private static final long SAFE_TIME = 3600 * 1000;
//    private static final String FIRMWARE_CHARSET = "ISO-8859-1";
//    public static final int NO_CACHE = NetConstant.NO_CACHE;
//    /**
//     * 获取缓存同时获取网络
//     */
//    public static final int CACHE_THEN_NET = NetConstant.CACHE_THEN_NET;
//    /**
//     * 如果有缓存则不用网络
//     */
//    public static final int CACHE_THAN_NET = NetConstant.CACHE_THAN_NET;
//    /**
//     * 如果网络不通则使用缓存
//     */
//    public static final int CACHE_IF_NET_ERROR = NetConstant.CACHE_IF_NET_ERROR;
//
////    public static <RANK_K extends RequestEntity, V> Observable<V> requestFromNet(final Context context, final boolean useToken,
////                                                                            final int method, final RANK_K request, final int cacheType,
////                                                                            final Mapper<V> mapper) {
////        final Context c = context.getApplicationContext();
////        return Observable.defer(new Func0<Observable<String>>() {
////            @Override
////            public Observable<String> call() {
////                if (useToken) {
////                    return getToken(c);
////                } else {
////                    String token = "Basic " + Utils.Base64_Encode(NetConstant.clientId + ":" + NetConstant.clientSecret);
////                    return Observable.just(token);
////                }
////            }
////        }).flatMap(new Func1<String, Observable<V>>() {
////            @Override
////            public Observable<V> call(final String s) {
////                if (s == null || s.isEmpty()) {
////                    return Observable.error(new NetworkErrorException("token error"));
////                }
////                boolean isNetAvailable = isThereInternetConnection(c);
////                switch (cacheType) {
////                    case NO_CACHE:
////                        if (isNetAvailable) {
////                            return requestFromNet(c, s, method, request, false, mapper);
////                        } else {
////                            return Observable.error(new NetworkErrorException());
////                        }
////                    case CACHE_THEN_NET:
////                        if (isNetAvailable) {
////                            return requestFromNet(c, s, method, request, false, mapper)
////                                    .subscribeOn(Schedulers.io())
////                                    .publish(new Func1<Observable<V>, Observable<V>>() {
////                                        @Override
////                                        public Observable<V> call(Observable<V> network) {
////                                            return Observable.merge(network, requestFromCache(c, request, mapper)
////                                                    .filter(new Func1<V, Boolean>() {
////                                                        @Override
////                                                        public Boolean call(V v) {
////                                                            return v != null;
////                                                        }
////                                                    })
////                                                    .subscribeOn(Schedulers.io())
////                                                    .takeUntil(network));
////                                        }
////                                    });
////                        } else {
////                            return requestFromCache(c, request, mapper);
////                        }
////                    case CACHE_THAN_NET:
////                        if (isNetAvailable) {
////                            return requestFromCache(c, request, mapper)
////                                    .subscribeOn(Schedulers.io())
////                                    .publish(new Func1<Observable<V>, Observable<V>>() {
////                                        @Override
////                                        public Observable<V> call(Observable<V> cacheWork) {
////                                            return Observable.merge(cacheWork, requestFromNet(c, s, method, request, false, mapper)
////                                                    .subscribeOn(Schedulers.io())
////                                                    .takeUntil(cacheWork));
////                                        }
////                                    });
////                        } else {
////                            return requestFromCache(c, request, mapper);
////                        }
////                    case CACHE_IF_NET_ERROR:
////                        if (isNetAvailable) {
////                            return requestFromNet(c, s, method, request, false, mapper);
////                        } else {
////                            return requestFromCache(c, request, mapper);
////                        }
////                    default:
////                        return requestFromNet(c, s, method, request, true, mapper);
////                }
////
////            }
////        });
////    }
//
//    private static <K extends RequestEntity, V> Observable<V> requestFromNet(final Context context, final String token,
//                                                                             final int method, final K request, final boolean noCache,
//                                                                             final Mapper<V> mapper) {
//        final String body = request.getBody();
//        final String url = request.getUrl();
//        return Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                NetworkConnection apiConnection;
//                try {
//                    apiConnection = NetworkConnection.create(url, method);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                    subscriber.onError(e);
//                    return;
//                }
//                apiConnection.addAuthorizationHeader(token);
//                String lang = Locale.getDefault().getLanguage();
//                Log.e("lang", "lang: " + lang);
//                if (NetworkConnection.LANG_ZH.equals(lang)) {
//                    apiConnection.setLanguage(NetworkConnection.LANG_ZH);
//                } else {
//                    apiConnection.setLanguage(NetworkConnection.LANG_EN);
//                }
//                if (body != null) {
//                    apiConnection.setBody(body);
//                }
//                try {
//                    String result = apiConnection.requestSyncCall();
//                    subscriber.onNext(result);
//                } catch (NetworkErrorException | IOException e) {
//                    e.printStackTrace();
//                    subscriber.onError(e);
//                }
//            }
//        }).map(new Func1<String, V>() {
//            @Override
//            public V call(String s) {
//                CacheUtil cacheUtil = CacheUtil.getInstance();
//                String key = MyConstant.API_VERSION + url + (body != null ? body : "");
//                try {
//                    if (s != null && !noCache) {
//                        cacheUtil.save(context, key, s);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return mapper.mapToEntity(s);
//            }
//        });
//    }
//
//    private static <K extends RequestEntity, V> Observable<V> requestFromCache(final Context context, final K request,
//                                                                               final Mapper<V> mapper) {
//        final String body = request.getBody();
//        final String url = request.getUrl();
//        return Observable.create(new Observable.OnSubscribe<V>() {
//            @Override
//            public void call(Subscriber<? super V> subscriber) {
//                CacheUtil cacheUtil = CacheUtil.getInstance();
//                String key = MyConstant.API_VERSION + url + (body != null ? body : "");
//                String cache;
//                try {
//                    cache = cacheUtil.get(context, key);
//                    Log.e(TAG, "get from cache: " + cache);
//                    if (!cache.isEmpty()) {
//                        subscriber.onNext(mapper.mapToEntity(cache));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//
//    public static Observable<String> getFirmwareFile(final Context context, final String url) {
//        return getToken(context)
//                .flatMap(new Func1<String, Observable<String>>() {
//                    @Override
//                    public Observable<String> call(String s) {
//                        return getFirmwareFile(context, url, s);
//                    }
//                });
//    }
//
//    private static Observable<String> getFirmwareFile(final Context context, final String url, final String token) {
//        return Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                NetworkConnection apiConnection;
//                try {
//                    apiConnection = NetworkConnection.create(url, GET);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                    subscriber.onError(e);
//                    return;
//                }
//                apiConnection.setResponseCharsetType(FIRMWARE_CHARSET);
//                apiConnection.addAuthorizationHeader(token);
//                try {
//                    String result = apiConnection.requestSyncCall();
//                    subscriber.onNext(result);
//                } catch (NetworkErrorException | IOException e) {
//                    e.printStackTrace();
//                    subscriber.onError(e);
//                }
//            }
//        });
//    }
//
//    private static Observable<String> getToken(final Context context) {
//        final String phone = SaveUtils.getPhone(context);
//        final String password = SaveUtils.getPassword(context);
//        final String zone = SaveUtils.getZone(context);
//        final String mac = Utils.getPhoneMac(context);
//        if (isTokenAvailable(context)) {
//            return Observable.just("bearer " + SaveUtils.getToken(context));
//        } else {
//            return Observable.create(new Observable.OnSubscribe<String>() {
//                @Override
//                public void call(Subscriber<? super String> subscriber) {
//                    NetworkConnection apiConnection;
//                    try {
//                        apiConnection = NetworkConnection.create(HttpApi.API_LOGIN_URL, POST);
//                        apiConnection.setBody("UserName=" + phone + "&Password=" + password +
//                                "&login_type=2&Zone" +zone + "&grant_type=password&device_id=" + mac);
//                        apiConnection.addAuthorizationHeader("Basic " + Utils.Base64_Encode(NetConstant.clientId + ":" + NetConstant.clientSecret));
//                        String result = apiConnection.requestSyncCall();
//                        subscriber.onNext(result);
//                    } catch (NetworkErrorException | IOException e) {
//                        e.printStackTrace();
//                        subscriber.onError(e);
//                    }
//                }
//            }).map(new Func1<String, String>() {
//                @Override
//                public String call(String s) {
//                    String token;
//                    try {
//                        Type loginResultType = new TypeToken<NetResponseEntity<TokenResponseEntity>>() {
//                        }.getType();
//                        NetResponseEntity<TokenResponseEntity> entity = new Gson().fromJson(s, loginResultType);
//                        token = entity.getData().getAccessToken();
////                        SaveUtils.saveToken(context, token);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        token = "";
//                    }
//                    return token;
//                }
//            });
//        }
//    }
//
//    private static boolean isTokenAvailable(Context context) {
//        long time = SaveUtils.getTokenTime(context);
//        return time - Calendar.getInstance().getTimeInMillis() > SAFE_TIME && !SaveUtils.getToken(context).isEmpty();
//    }
//
//    private static boolean isThereInternetConnection(Context context) {
//        boolean isConnected;
//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivityManager == null){
//            return false;
//        }
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());
//        return isConnected;
//    }
//
////    @IntDef({NO_CACHE, CACHE_THAN_NET, CACHE_THEN_NET})
////    public @interface CacheType {
////    }
//
//
//    public static <K extends RequestEntity, V> Observable<V> requestFromNet(final Context context, final boolean useToken,
//                                                                            final int method, final K request, final int cacheType,
//                                                                            final Mapper<V> mapper) {
//        final Context c = context.getApplicationContext();
//        boolean isNetAvailable = isThereInternetConnection(c);
//        switch (cacheType) {
//            case NO_CACHE:
//                if (isNetAvailable) {
//                    return requestFromNets(c, useToken, method, request, false, mapper);
//                } else {
//                    return Observable.error(new NetworkErrorException());
//                }
//            case CACHE_THEN_NET:
//                if (isNetAvailable) {
//                    return requestFromNets(c, useToken, method, request, false, mapper)
//                            .subscribeOn(Schedulers.io())
//                            .publish(new Func1<Observable<V>, Observable<V>>() {
//                                @Override
//                                public Observable<V> call(Observable<V> network) {
//                                    return Observable.merge(network, requestFromCache(c, request, mapper)
//                                            .filter(new Func1<V, Boolean>() {
//                                                @Override
//                                                public Boolean call(V v) {
//                                                    return v != null;
//                                                }
//                                            })
//                                            .subscribeOn(Schedulers.io())
//                                            .takeUntil(network));
//                                }
//                            });
//                } else {
//                    return requestFromCache(c, request, mapper);
//                }
//            case CACHE_THAN_NET:
//                if (isNetAvailable) {
//                    return requestFromCache(c, request, mapper)
//                            .subscribeOn(Schedulers.io())
//                            .publish(new Func1<Observable<V>, Observable<V>>() {
//                                @Override
//                                public Observable<V> call(Observable<V> cacheWork) {
//                                    return Observable.merge(cacheWork, requestFromNets(c, useToken, method, request, false, mapper)
//                                            .subscribeOn(Schedulers.io())
//                                            .takeUntil(cacheWork));
//                                }
//                            });
//                } else {
//                    return requestFromCache(c, request, mapper);
//                }
//            case CACHE_IF_NET_ERROR:
//                if (isNetAvailable) {
//                    return requestFromNets(c, useToken, method, request, false, mapper);
//                } else {
//                    return requestFromCache(c, request, mapper);
//                }
//            default:
//                return requestFromNets(c, useToken, method, request, true, mapper);
//        }
//
//    }
//
//    private static <K extends RequestEntity, V> Observable<V> requestFromNets(final Context context, final boolean useToken,
//                                                                             final int method, final K request, final boolean noCache,
//                                                                             final Mapper<V> mapper) {
//        final String body = request.getBody();
//        final String url = request.getUrl();
//        return Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                NetworkConnection2 apiConnection;
//                try {
//                    apiConnection = NetworkConnection2.create(context,url, method,useToken);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                    subscriber.onError(e);
//                    return;
//                }
//                String lang = Locale.getDefault().getLanguage();
//                Log.e("lang", "lang: " + lang);
//                if (NetworkConnection2.LANG_ZH.equals(lang)) {
//                    apiConnection.setLanguage(NetworkConnection2.LANG_ZH);
//                } else {
//                    apiConnection.setLanguage(NetworkConnection2.LANG_EN);
//                }
//                if (body != null) {
//                    apiConnection.setBody(body);
//                }
//                if (useToken){
//                    apiConnection.setToken("bearer " + SaveUtils.getToken(context));
//                }else{
//                    apiConnection.setToken("Basic " + Utils.Base64_Encode(NetConstant.clientId + ":" + NetConstant.clientSecret));
//                }
//                try {
//                    String result = apiConnection.requestSyncCall();
//                    subscriber.onNext(result);
//                } catch (NetworkErrorException | IOException e) {
//                    e.printStackTrace();
//                    subscriber.onError(e);
//                }
//            }
//        }).map(new Func1<String, V>() {
//            @Override
//            public V call(String s) {
//                CacheUtil cacheUtil = CacheUtil.getInstance();
//                String key = MyConstant.API_VERSION + url + (body != null ? body : "");
//                try {
//                    if (s != null && !noCache) {
//                        cacheUtil.save(context, key, s);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return mapper.mapToEntity(s);
//            }
//        });
//    }
//
//}
