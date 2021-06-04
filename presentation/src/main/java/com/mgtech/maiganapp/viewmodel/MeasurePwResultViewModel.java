package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.mgtech.domain.entity.IndicatorDescription;
import com.mgtech.domain.entity.net.response.IndicatorDescriptionResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.PwDataResponseEntity;
import com.mgtech.domain.interactor.DataUseCase;
import com.mgtech.domain.interactor.ExceptionUseCase;
import com.mgtech.domain.interactor.FileUseCase;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.IIndicatorModel;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.data.wrapper.IndicatorDataWrapper;
import com.mgtech.maiganapp.utils.IndicatorUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 * 数据展示 view-model
 */

public class MeasurePwResultViewModel extends BaseViewModel {
    private DataUseCase dataUseCase;
    private ExceptionUseCase exceptionUseCase;
    private FileUseCase fileUseCase;
    private boolean isMan;
    public final MutableLiveData<List<IIndicatorModel>> itemListLiveData = new MutableLiveData<>();
    public final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public final MutableLiveData<Boolean> networkError = new MutableLiveData<>();
    public IndicatorDataModel model;
    private String userId;
    private String indicatorId;
    private List<Float> rawData;
    public boolean enableSetRemark;


    public MeasurePwResultViewModel(Application context) {
        super(context);
        this.dataUseCase = ((MyApplication) context).getDataUseCase();
        this.exceptionUseCase = ((MyApplication) context).getExceptionUseCase();
        this.fileUseCase = ((MyApplication) context).getFileUseCase();
        if (IndicatorDescription.isEmpty()) {
            getIndicatorDescription();
        }
    }

    public void setIndicatorId(String indicatorId, String userId, boolean man) {
        this.userId = userId;
        this.isMan = man;
        this.indicatorId = indicatorId;
        getData();
    }

    public void getData() {
        loading.setValue(true);
        dataUseCase.getDataById(indicatorId, userId, new Subscriber<NetResponseEntity<PwDataResponseEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                networkError.setValue(true);
                loading.setValue(false);
                showToast(getApplication().getString(R.string.network_error));
            }

            @Override
            public void onNext(NetResponseEntity<PwDataResponseEntity> netResponseEntity) {
                loading.setValue(false);
                if (netResponseEntity.getCode() == 0) {
                    model = IndicatorDataWrapper.getModelFromPwDataResponseEntity(netResponseEntity.getData());
                    getRawData(model.rawDataFileId);
                    networkError.setValue(false);
                } else {
                    showToast(netResponseEntity.getMessage());
                    networkError.setValue(true);
                }
            }
        });
    }

    private void getRawData(String fileUrl) {
        if (TextUtils.isEmpty(fileUrl)) {
            render();
            return;
        }
        fileUseCase.getPwRawData(fileUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Float>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        render();
                    }

                    @Override
                    public void onNext(List<Float> data) {
                        rawData = data;
                        render();
                    }
                });
    }

    public String getRemark() {
        if (model.remark == null) {
            return "";
        }
        return model.remark;
    }

    public void render() {
        List<IIndicatorModel> itemList = new ArrayList<>();
//        this.itemList.clear();
        itemList.add(new IIndicatorModel.Time(DateFormat.format(MyConstant.DISPLAY_DATE_5, model.time).toString(), model.isAuto));
        itemList.add(new IIndicatorModel.HrBp(model.ps, model.pd, model.psUnit, model.hr, model.hrUnit, model.psLevel, model.pdLevel, model.hrLevel,
                IndicatorDescription.get(NetConstant.HR, isMan ? MyConstant.MAN : MyConstant.WOMEN)));

        itemList.add(new IIndicatorModel.Image(rawData));
        itemList.add(new IIndicatorModel.TAG(getApplication().getString(R.string.indicator_detail_test_parameter)));
        itemList.add(new IIndicatorModel.Title(IIndicatorModel.TYPE_TITLE_START, NetConstant.PM,
                IndicatorUtils.getTitle(getApplication(), NetConstant.PM), model.pm, model.pmLevel, model.pmUnit, "",
                IndicatorDescription.get(NetConstant.PM, isMan ? MyConstant.MAN : MyConstant.WOMEN)));

        itemList.add(new IIndicatorModel.Title(IIndicatorModel.TYPE_TITLE_MIDDLE, NetConstant.CO,
                IndicatorUtils.getTitle(getApplication(), NetConstant.CO), model.co, model.coLevel, model.coUnit, "",
                IndicatorDescription.get(NetConstant.CO, isMan ? MyConstant.MAN : MyConstant.WOMEN)));


        itemList.add(new IIndicatorModel.Title(IIndicatorModel.TYPE_TITLE_MIDDLE, NetConstant.V0,
                IndicatorUtils.getTitle(getApplication(), NetConstant.V0), model.v0, model.v0Level, model.v0Unit, "",
                IndicatorDescription.get(NetConstant.V0, isMan ? MyConstant.MAN : MyConstant.WOMEN)));

        itemList.add(new IIndicatorModel.Title(IIndicatorModel.TYPE_TITLE_END, NetConstant.TPR,
                IndicatorUtils.getTitle(getApplication(), NetConstant.TPR), model.tpr, model.tprLevel, model.tprUnit, getApplication().getString(R.string.indicator_detail_test_tag),
                IndicatorDescription.get(NetConstant.TPR, isMan ? MyConstant.MAN : MyConstant.WOMEN)));
        enableSetRemark = SaveUtils.getUserId().equalsIgnoreCase(userId);
        if (enableSetRemark || !TextUtils.isEmpty(model.remark)) {
            itemList.add(new IIndicatorModel.Mark(getApplication().getString(enableSetRemark ? R.string.measure_pw_result_set_mark : R.string.measure_pw_result_mark),
                    model.remark, SaveUtils.getUserId().equalsIgnoreCase(userId)));
        }
        itemList.add(new IIndicatorModel.Footer(getApplication().getString(R.string.indicator_detail_footer_text)));
//        getSuccess.set(!getSuccess.get());
        itemListLiveData.setValue(itemList);
    }

    private void getIndicatorDescription() {
        dataUseCase.getIndicatorDescription(NetConstant.NO_CACHE, false
                , new Subscriber<NetResponseEntity<List<IndicatorDescriptionResponseEntity>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(NetResponseEntity<List<IndicatorDescriptionResponseEntity>> listNetResponseEntity) {
                        if (listNetResponseEntity != null && listNetResponseEntity.getCode() == 0) {
                            List<IndicatorDescriptionResponseEntity> list = listNetResponseEntity.getData();
                            if (list != null) {
                                IndicatorDescription.saveToLocal(list);
                                render();
                            }
                        }
                    }
                });
    }

    public void setExceptionRead(String noticeId, String targetId) {
        exceptionUseCase.markRead(SaveUtils.getUserId(getApplication()), targetId,
                noticeId, new Subscriber<NetResponseEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(NetResponseEntity netResponseEntity) {
                        Log.i(TAG, "onNext: " + netResponseEntity);
                    }
                });
    }

    public void setRemark(final String remarkString) {
        dataUseCase.setPwRemark(SaveUtils.getUserId(), model.id, remarkString, new Subscriber<NetResponseEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                showToast(getApplication().getString(R.string.network_error));
            }

            @Override
            public void onNext(NetResponseEntity netResponseEntity) {
                if (netResponseEntity.getCode() == 0) {
                    model.remark = remarkString;
                    Log.e(TAG, "onNext: " + model.remark);
                    render();
                } else {
                    showToast(netResponseEntity.getMessage());
                }
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        exceptionUseCase.unSubscribe();
        dataUseCase.unSubscribe();
    }

}
