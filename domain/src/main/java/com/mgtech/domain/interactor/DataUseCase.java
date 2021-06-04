package com.mgtech.domain.interactor;

import android.content.Context;
import androidx.appcompat.widget.ViewUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgtech.domain.entity.net.request.DeletePwRequestEntity;
import com.mgtech.domain.entity.net.request.GetHealthManagementDataRequestEntity;
import com.mgtech.domain.entity.net.request.GetHomeDataRequestEntity;
import com.mgtech.domain.entity.net.request.GetPwByDateRangeRequestEntity;
import com.mgtech.domain.entity.net.request.GetStatisticPwByDateRangeRequestEntity;
import com.mgtech.domain.entity.net.request.GetStatisticPwByDateRequestEntity;
import com.mgtech.domain.entity.net.request.SaveRawDataRequestEntity;
import com.mgtech.domain.entity.net.request.SetPwMarkRequestEntity;
import com.mgtech.domain.entity.net.response.GetHealthManagementResponseEntity;
import com.mgtech.domain.entity.net.response.GetHomeDataResponseEntity;
import com.mgtech.domain.entity.net.response.IndicatorDescriptionResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.PwDataResponseEntity;
import com.mgtech.domain.entity.net.response.PwStatisticDataResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.RawDataFileUtil;
import com.mgtech.domain.utils.Utils;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 * 获取数据
 */

public class DataUseCase {
    private NetRepository.Data repository;
    private Subscription lastSubscription;
    private Subscription statisticDataRangeSubscription;
    private Subscription statisticDataSubscription;
    private Subscription getDataRangeSubscription;
    private Subscription calculateDataSubscription;
    private Subscription calculateAutoDataSubscription;
    private Subscription deleteDataSubscription;
    private Subscription getHomePageDataSubscription;
    private Subscription getHealthManagementSubscription;
    private Subscription getIndicatorDescriptionSubscription;
    private Subscription setRemarkSubscription;
    private Subscription getDataSubscription;

    public DataUseCase(NetRepository.Data repository) {
        this.repository = repository;
    }

    /**
     * 获取最近一次数据
     *
     * @param subscriber 订阅者
     */
    public void getLastData(String id, int cacheType,
                            Subscriber<NetResponseEntity<PwDataResponseEntity>> subscriber) {
        unSubscribeLast();
        lastSubscription = this.repository.getLastData(id, cacheType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 查询一段时间的分析数据
     *
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param subscriber 订阅者
     */
    public void getStatisticDataByDateRange(String userId, long startTime, long endTime, boolean useCache,
                                            Subscriber<NetResponseEntity<PwStatisticDataResponseEntity>> subscriber) {
        unSubscribeStatisticRangeData();
        GetStatisticPwByDateRangeRequestEntity entity = new GetStatisticPwByDateRangeRequestEntity(userId, startTime,
                endTime,
                MyConstant.DISPLAY_DATA_TYPE,
                MyConstant.GET_PW_DATA_TYPE);
        statisticDataRangeSubscription = this.repository.getStatisticPwByDateRange(entity,
                useCache ? NetConstant.CACHE_THEN_NET : NetConstant.NO_CACHE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 查询某天的分析数据
     *
     * @param date       日期
     * @param useCache   是否使用缓存
     * @param subscriber 订阅者
     */
    public void getStatisticDataByDate(String userId, long date, boolean useCache,
                                       Subscriber<NetResponseEntity<PwStatisticDataResponseEntity>> subscriber) {
        unSubscribeStatisticData();
        GetStatisticPwByDateRequestEntity entity = new GetStatisticPwByDateRequestEntity(userId, date,
                MyConstant.DISPLAY_DATA_TYPE, MyConstant.GET_PW_DATA_TYPE);
        statisticDataSubscription = repository.getStatisticPwByDate(entity, useCache ? NetConstant.CACHE_THEN_NET : NetConstant.NO_CACHE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 查询一段时间数据
     *
     * @param subscriber 订阅者
     */
    public void getDataByDateRange(String userId, long startDate, long endDate, int cacheType, Subscriber<NetResponseEntity<List<PwDataResponseEntity>>> subscriber) {
        unSubscribeDataRangeData();
        GetPwByDateRangeRequestEntity entity = new GetPwByDateRangeRequestEntity(userId, startDate, endDate,
                MyConstant.DISPLAY_DATA_TYPE, MyConstant.GET_PW_DATA_TYPE);
        getDataRangeSubscription = this.repository.getPwByDateRange(entity, cacheType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void getHomePageData(String id, Subscriber<NetResponseEntity<GetHomeDataResponseEntity>> subscriber) {
        unSubscribeHome();
        GetHomeDataRequestEntity entity = new GetHomeDataRequestEntity(id, MyConstant.GET_PW_DATA_TYPE);
        getHomePageDataSubscription = this.repository.getHomePageData(NetConstant.NO_CACHE, entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void deleteData(DeletePwRequestEntity entity, Subscriber<NetResponseEntity> subscriber) {
        unSubscribeDeleteData();
        this.deleteDataSubscription = this.repository.deletePw(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 发送一个数据
     *
     * @param subscriber 订阅者
     */
    public void sendOneSavedData(final Context context, final Subscriber<Boolean> subscriber) {
        String[] fileNames = RawDataFileUtil.getFileNames(context);
        if (fileNames == null || fileNames.length == 0 || !Utils.isThereInternetConnection(context)) {
            return;
        }
        final String fileName = fileNames[(int) (Math.random() * fileNames.length)];
        Observable.create(new Observable.OnSubscribe<SaveRawDataRequestEntity>() {
            @Override
            public void call(Subscriber<? super SaveRawDataRequestEntity> subscriber) {
                String content = RawDataFileUtil.getContent(context, fileName);
                if (content.isEmpty()) {
                    RawDataFileUtil.deleteFile(context, fileName);
                    subscriber.onCompleted();
                }
                Type type = new TypeToken<SaveRawDataRequestEntity>() {
                }.getType();
                SaveRawDataRequestEntity entity = new Gson().fromJson(content, type);
                entity.setIsAsync(1);
                subscriber.onNext(entity);
            }
        })
                .observeOn(Schedulers.io())
                .filter(new Func1<SaveRawDataRequestEntity, Boolean>() {
                    @Override
                    public Boolean call(SaveRawDataRequestEntity saveRawDataRequestEntity) {
                        return saveRawDataRequestEntity != null;
                    }
                })
                .flatMap(new Func1<SaveRawDataRequestEntity, Observable<NetResponseEntity<PwDataResponseEntity>>>() {
                    @Override
                    public Observable<NetResponseEntity<PwDataResponseEntity>> call(SaveRawDataRequestEntity saveRawDataRequestEntity) {
                        return repository.calculatePwData(saveRawDataRequestEntity);
                    }
                })
                .onErrorReturn(new Func1<Throwable, NetResponseEntity<PwDataResponseEntity>>() {
                    @Override
                    public NetResponseEntity<PwDataResponseEntity> call(Throwable throwable) {
                        Logger.e(throwable,"缓存上传失败");
                        RawDataFileUtil.deleteFile(context, fileName);
                        return null;
                    }
                })
                .map(new Func1<NetResponseEntity<PwDataResponseEntity>, Boolean>() {
                    @Override
                    public Boolean call(NetResponseEntity<PwDataResponseEntity> netResponseEntity) {
                        if (netResponseEntity != null) {
                            if (RawDataFileUtil.deleteFile(context, fileName)) {
                                Logger.i("删除成功");
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

//    private Observable<Boolean> reSendAutoSampleData(final Context context, final String id, final String fileName, SaveRawDataRequestEntity entity) {
//        return repository.calculateAutoData(entity)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(Schedulers.newThread())
//                .map(new Func1<NetResponseEntity<AutoMeasureResponseEntity>, Boolean>() {
//                    @Override
//                    public Boolean call(NetResponseEntity<AutoMeasureResponseEntity> autoMeasureResponseEntity) {
//                        boolean flag = false;
//                        if (autoMeasureResponseEntity != null && autoMeasureResponseEntity.getCode() == 0) {
//                            flag = true;
//                            if (RawDataFileUtil.deleteFile(context, fileName)) {
//                                Log.e("sendSavedData", "删除成功");
//                            }
//                        }
//                        return flag;
//                    }
//                })
//                .doOnError(new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        Log.e("DataUseCase", "SocketTimeoutException is " + (throwable instanceof SocketTimeoutException || throwable.getMessage().equals(
//                                SocketTimeoutException.class.getSimpleName())));
//                        if (throwable instanceof SocketTimeoutException || throwable.getMessage().equals(
//                                SocketTimeoutException.class.getSimpleName())) {
//                            RawDataFileUtil.deleteFile(context, fileName);
//                        }
//                    }
//                });
//    }

    private void saveData(final Context context, String request) {
        try {
            RawDataFileUtil.save(context, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传采样数据
     *
     * @param data       数据
     * @param time       时间
     * @param sex        性别 男1，女0
     * @param height     身高
     * @param weight     体重
     * @param age        年龄（周岁）
     * @param subscriber 订阅者
     */
    public void calculateData(List<Object> data, long time,
                              int sex, int height, int weight, int age, String mac, String protocolVersion, String firmwareVersion,
                              Subscriber<NetResponseEntity<PwDataResponseEntity>> subscriber) {
        SaveRawDataRequestEntity entity = new SaveRawDataRequestEntity(false, time, data, sex,
                age, height, weight, mac, protocolVersion, firmwareVersion);
        unSubscribeCalculateData();
        calculateDataSubscription = this.repository.calculatePwData(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void calculateAutoData(final Context context, List<Object> data, long time,
                                  int sex, int height, int weight, int age, String mac,
                                  String protocolVersion, String firmwareVersion,
                                  Subscriber<NetResponseEntity<PwDataResponseEntity>> subscriber) {
        SaveRawDataRequestEntity entity = new SaveRawDataRequestEntity(true, time, data, sex,
                age, height, weight, mac, protocolVersion, firmwareVersion);
        final String requestString = entity.getBody();
        calculateAutoDataSubscription = repository.calculateAutoData(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("error", "计算发生错误了，保存信息 ");
                        throwable.printStackTrace();
                        saveData(context, requestString);
                    }
                }).doOnNext(new Action1<NetResponseEntity<PwDataResponseEntity>>() {
                    @Override
                    public void call(NetResponseEntity<PwDataResponseEntity> netResponseEntity) {
                        if (netResponseEntity == null || netResponseEntity.getCode() != 0) {
                            Log.e("error", "auto发生错误，保存信息 ");
                            saveData(context, requestString);
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public void getHealthManagement(String id, long startDate, long endDate
            , Subscriber<NetResponseEntity<GetHealthManagementResponseEntity>> subscriber) {
        unSubscribeHealthManagement();
        GetHealthManagementDataRequestEntity entity = new GetHealthManagementDataRequestEntity(id, startDate, endDate, MyConstant.DISPLAY_DATA_TYPE);
        getHealthManagementSubscription = repository.getHealthManagementData(NetConstant.CACHE_IF_NET_ERROR, entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取参数说明
     */
    public void getIndicatorDescription(int cacheType,boolean inService, Subscriber<NetResponseEntity<List<IndicatorDescriptionResponseEntity>>> subscriber) {
        unSubscribeDescription();
        if (inService){
            this.getIndicatorDescriptionSubscription = this.repository.getIndicatorDescription(cacheType)
                    .subscribeOn(Schedulers.immediate())
                    .observeOn(Schedulers.immediate())
                    .subscribe(subscriber);
        }else {
            this.getIndicatorDescriptionSubscription = this.repository.getIndicatorDescription(cacheType)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    public void setPwRemark(String userId, String measureId, String remark,Subscriber<NetResponseEntity> subscriber){
        unSubscribePwRemark();
        setRemarkSubscription = this.repository.setPwRemark(new SetPwMarkRequestEntity(userId,measureId,remark))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getDataById(String measureId,String userId,Subscriber<NetResponseEntity<PwDataResponseEntity>>subscriber){
        unSubscribeGetDataById();
        getDataSubscription = this.repository.getDataById(measureId,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 取消订阅
     */
    public void unSubscribe() {
        unSubscribeLast();
        unSubscribeCalculateAutoData();
        unSubscribeCalculateData();
        unSubscribeDeleteData();
        unSubscribeStatisticData();
        unSubscribeStatisticRangeData();
        unSubscribeDataRangeData();
        unSubscribeHome();
        unSubscribeHealthManagement();
        unSubscribeDescription();
        unSubscribePwRemark();
        unSubscribeGetDataById();
    }

    private void unSubscribePwRemark(){
        if (setRemarkSubscription != null && !setRemarkSubscription.isUnsubscribed()){
            setRemarkSubscription.unsubscribe();
        }
    }

    private void unSubscribeLast() {
        if (lastSubscription != null && !lastSubscription.isUnsubscribed()) {
            lastSubscription.unsubscribe();
        }
    }

    private void unSubscribeCalculateData() {
        if (calculateDataSubscription != null && !calculateDataSubscription.isUnsubscribed()) {
            calculateDataSubscription.unsubscribe();
        }
    }

    private void unSubscribeCalculateAutoData() {
        if (calculateAutoDataSubscription != null && !calculateAutoDataSubscription.isUnsubscribed()) {
            calculateAutoDataSubscription.unsubscribe();
        }
    }

    private void unSubscribeDeleteData() {
        if (deleteDataSubscription != null && !deleteDataSubscription.isUnsubscribed()) {
            deleteDataSubscription.unsubscribe();
        }
    }

    private void unSubscribeStatisticData() {
        if (statisticDataSubscription != null && !statisticDataSubscription.isUnsubscribed()) {
            statisticDataSubscription.unsubscribe();
        }
    }

    private void unSubscribeStatisticRangeData() {
        if (statisticDataRangeSubscription != null && !statisticDataRangeSubscription.isUnsubscribed()) {
            statisticDataRangeSubscription.unsubscribe();
        }
    }

    private void unSubscribeDataRangeData() {
        if (getDataRangeSubscription != null && !getDataRangeSubscription.isUnsubscribed()) {
            getDataRangeSubscription.unsubscribe();
        }
    }

    private void unSubscribeHome() {
        if (getHomePageDataSubscription != null && !getHomePageDataSubscription.isUnsubscribed()) {
            getHomePageDataSubscription.unsubscribe();
        }
    }

    private void unSubscribeHealthManagement() {
        if (getHealthManagementSubscription != null && !getHealthManagementSubscription.isUnsubscribed()) {
            getHealthManagementSubscription.unsubscribe();
        }
    }

    private void unSubscribeDescription() {
        if (getIndicatorDescriptionSubscription != null && !getIndicatorDescriptionSubscription.isUnsubscribed()) {
            getIndicatorDescriptionSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetDataById() {
        if (getDataSubscription != null && !getDataSubscription.isUnsubscribed()) {
            getDataSubscription.unsubscribe();
        }
    }
}
