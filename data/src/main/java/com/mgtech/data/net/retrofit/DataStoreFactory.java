package com.mgtech.data.net.retrofit;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.mgtech.data.cache.IDiskCache;
import com.mgtech.data.cache.LruDiskCache;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.utils.NetConstant;

import java.lang.reflect.Type;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author zhaixiang
 */
public class DataStoreFactory {
    /**
     * 不缓存，不使用缓存数据
     */
    public static final int NO_CACHE = NetConstant.NO_CACHE;
    /**
     * 获取缓存同时获取网络
     */
    public static final int CACHE_THEN_NET = NetConstant.CACHE_THEN_NET;
    /**
     * 如果有缓存则不用网络
     */
    public static final int CACHE_THAN_NET = NetConstant.CACHE_THAN_NET;
    /**
     * 如果网络不通则使用缓存
     */
    public static final int CACHE_IF_NET_ERROR = NetConstant.CACHE_IF_NET_ERROR;


    public static <T> Observable<T> strategy(final Context c, final Observable<T> net, final RequestEntity entity, int cacheType, Type type) {
        if (cacheType == NO_CACHE) {
            return noCache(c, net);
        } else if (cacheType == CACHE_THEN_NET) {
            return useCacheBeforeNet(c, net, entity, type);
        } else if (cacheType == CACHE_THAN_NET) {
            return useCacheIfExist(c, net, entity, type);
        } else if (cacheType == CACHE_IF_NET_ERROR) {
            return useCacheIfNetError(c, net, entity, type);
        } else {
            return useCacheIfNetError(c, net, entity, type);
        }
    }

    /**
     * 不需要缓存
     *
     * @param context 上下文
     * @param net     网络数据流
     * @param <T>     泛型
     * @return 数据流
     */
    private static <T> Observable<T> noCache(final Context context, final Observable<T> net) {
        boolean isNetAvailable = isThereInternetConnection(context);
        if (isNetAvailable) {
            return net;
        } else {
            return Observable.error(new NetworkErrorException());
        }
    }

    /**
     * 在网络返回前如果有缓存则先返回缓存数据，之后再返回网络数据。
     *
     * @param context 上下文
     * @param net     网络数据流
     * @param entity  请求
     * @param <T>     泛型
     * @return 数据流
     */
    private static <T> Observable<T> useCacheBeforeNet(final Context context, final Observable<T> net, final RequestEntity entity, Type type) {
        boolean isNetAvailable = isThereInternetConnection(context);
        final Observable<T> cache = getCacheObservable(context, entity, type);
        if (isNetAvailable) {
            return Observable.mergeDelayError(cache
                    .onErrorReturn(new Func1<Throwable, T>() {
                        @Override
                        public T call(Throwable throwable) {
                            return null;
                        }
                    }).filter(new Func1<T, Boolean>() {
                        @Override
                        public Boolean call(T t) {
                            return t != null;
                        }
                    }),net
                    .map(new Func1<T, T>() {
                        @Override
                        public T call(T t) {
                            save(context, entity, t);
                            return t;
                        }
                    }));
        } else {
            return cache;
        }
    }


    /**
     * 如果有缓存则只使用缓存，如果没缓存则从网络获取
     *
     * @param context 上下文
     * @param net     网络数据流
     * @param entity  请求
     * @param <T>     泛型
     * @return 数据流
     */
    private static <T> Observable<T> useCacheIfExist(final Context context, final Observable<T> net, final RequestEntity entity, Type type) {
        final Observable<T> cache = getCacheObservable(context, entity, type);
        return cache
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends T>>() {
                    @Override
                    public Observable<? extends T> call(Throwable throwable) {
                        return net.map(new Func1<T, T>() {
                            @Override
                            public T call(T t) {
                                save(context, entity, t);
                                return t;
                            }
                        });
                    }
                });
    }

    /**
     * 如果没网络则使用缓存
     *
     * @param context 上下文
     * @param net     网络
     * @param entity  请求
     * @param type    返回类型
     * @param <T>     返回类型
     * @return 数据流
     */
    private static <T> Observable<T> useCacheIfNetError(final Context context, final Observable<T> net, final RequestEntity entity, Type type) {
        boolean isNetAvailable = isThereInternetConnection(context);
        if (isNetAvailable) {
            return net.map(new Func1<T, T>() {
                @Override
                public T call(T t) {
                    save(context, entity, t);
                    return t;
                }
            });
        } else {
            return getCacheObservable(context, entity, type);
        }
    }

    private static <T> void save(Context c, RequestEntity entity, T t) {
        Log.i("TTTTTT", "save call: ");
        if (t != null) {
            IDiskCache<T> cache = new LruDiskCache<>(c);
            cache.saveToDisk(entity, t);
        }
    }

    private static <T> Observable<T> getCacheObservable(Context context, RequestEntity entity, Type type) {
        IDiskCache<T> cache = new LruDiskCache<>(context);
        return cache.getFromDisk(entity, type);
    }

    private static boolean isThereInternetConnection(Context context) {
        boolean isConnected;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());
        return isConnected;
    }


}
