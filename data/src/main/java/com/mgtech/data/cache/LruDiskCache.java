package com.mgtech.data.cache;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mgtech.data.net.http.CacheUtil;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.utils.MyConstant;

import java.io.IOException;
import java.lang.reflect.Type;

import rx.Observable;
import rx.Subscriber;


/**
 * @author zhaixiang
 */
public class LruDiskCache<T> implements IDiskCache<T>{
    private static final String TAG = "LruDiskCache";
    private Context context;
    private CacheUtil cacheUtil;

    public LruDiskCache(Context context) {
        this.context = context;
        this.cacheUtil = CacheUtil.getInstance();
    }


    @Override
    public Observable<T> getFromDisk(final RequestEntity entity,final Type type) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    String cache = cacheUtil.get(context,MyConstant.API_VERSION + entity.getUrl() + entity.getBody());
                    Log.i(TAG, "call: "+ MyConstant.API_VERSION + entity.getUrl() + entity.getBody());
                    Log.i(TAG, "call: "+ cache);
                    if (TextUtils.isEmpty(cache)){
                        subscriber.onError(new Exception("没有缓存"));
                        return;
                    }
                    T t = new GsonBuilder().enableComplexMapKeySerialization().create().fromJson(cache,type);
                    if (t instanceof NetResponseEntity){
                        ((NetResponseEntity)t).setCache(true);
                    }
                    Log.i(TAG, "call: "+ t.toString());
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public  void saveToDisk(RequestEntity entity, T value){
        try {
            String v = new Gson().toJson(value);
            Log.i(TAG, "saveToDisk: "+ MyConstant.API_VERSION + entity.getUrl() + entity.getBody());
            Log.i(TAG, "saveToDisk: "+ v);
            cacheUtil.save(context,MyConstant.API_VERSION + entity.getUrl() + entity.getBody(),v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
