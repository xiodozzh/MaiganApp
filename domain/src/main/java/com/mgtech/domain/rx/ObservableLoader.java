package com.mgtech.domain.rx;

import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ObservableLoader {
    public  <T> Observable<T> observeMain(Observable<T> observable){
        return observable
                .doOnError(new DoOnTokenErrorAction())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public  <T> Observable<T> observeIO(Observable<T> observable){
        return observable
                .doOnError(new DoOnTokenErrorAction())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public  <T> Single<T> observeMain(Single<T> observable){
        return observable
                .doOnError(new DoOnTokenErrorAction())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public  <T> Single<T> observeIO(Single<T> observable){
        return observable
                .doOnError(new DoOnTokenErrorAction())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
}
