package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.databinding.ObservableBoolean;

import com.mgtech.domain.entity.net.request.MedicineAddEntity;
import com.mgtech.domain.entity.net.request.MedicineDeleteRequestEntity;
import com.mgtech.domain.entity.net.response.CustomMedicineEntity;
import com.mgtech.domain.entity.net.response.MedicineResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.MedicineUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.IMedicineModel;
import com.mgtech.maiganapp.data.wrapper.MedicineModelWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 * 选择药物
 */

public class SelectMedicineViewModel extends BaseViewModel {
    private List<IMedicineModel> libList;
    private List<IMedicineModel> customList;
    public List<IMedicineModel> list;
    private MedicineUseCase useCase;
    private List<String> indexLetterList;
    public final ObservableBoolean loadSuccess = new ObservableBoolean(false);
    public final ObservableBoolean adding = new ObservableBoolean(false);
    public final ObservableBoolean error = new ObservableBoolean(false);
    public final ObservableBoolean loading = new ObservableBoolean(false);
    private Subscription getDataSubscription;


    public SelectMedicineViewModel(Application application) {
        super(application);
        this.list = new ArrayList<>();
        this.libList = new ArrayList<>();
        this.customList = new ArrayList<>();
        this.indexLetterList = new ArrayList<>();
        this.useCase = ((MyApplication) application).getMedicineUseCase();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        unSubscribe();
        useCase.unSubscribe();
    }

    private String libWrapper(NetResponseEntity<Map<String, List<MedicineResponseEntity>>> listNetResponseEntity) {
        String errorText;
        if (listNetResponseEntity != null) {
            if (listNetResponseEntity.getCode() != 0) {
                errorText = listNetResponseEntity.getMessage();
            } else {
                indexLetterList.clear();
                libList.clear();
                Map<String, List<MedicineResponseEntity>> map = listNetResponseEntity.getData();
                if (map != null) {
                    for (Map.Entry<String, List<MedicineResponseEntity>> entry : map.entrySet()) {
                        indexLetterList.add(entry.getKey());
                    }
                    Collections.sort(indexLetterList);
                    for (String letter : indexLetterList) {
                        libList.add(new IMedicineModel.Title(letter));
                        for (MedicineResponseEntity entity : map.get(letter)) {
                            libList.add(MedicineModelWrapper.wrapFromLib(entity));
                        }
                    }
                }
                errorText = "";
            }
        } else {
            errorText = getApplication().getString(R.string.network_error);
        }
        return errorText;
    }

    private String customWrapper(NetResponseEntity<List<CustomMedicineEntity>> netResponseEntity) {
        String errorText;
        if (netResponseEntity == null) {
            errorText = getApplication().getString(R.string.network_error);
        } else {
            if (netResponseEntity.getCode() == 0) {
                errorText = "";
                List<CustomMedicineEntity> list = netResponseEntity.getData();
                if (list != null) {
                    customList.clear();
                    for (CustomMedicineEntity entity : list) {
                        customList.add(MedicineModelWrapper.wrapFromCustom(entity));
                    }
                }
            } else {
                errorText = netResponseEntity.getMessage();
            }
        }
        return errorText;
    }

    public void getAllMedicine() {
        String id = SaveUtils.getUserId(getApplication());
        unSubscribe();
        loading.set(true);
        getDataSubscription = Observable.zip(useCase.getCustomMedicineListObs(id), useCase.getLibMedicineObs(id),
                new Func2<NetResponseEntity<List<CustomMedicineEntity>>, NetResponseEntity<Map<String, List<MedicineResponseEntity>>>, String>() {
                    @Override
                    public String call(NetResponseEntity<List<CustomMedicineEntity>> listNetResponseEntity,
                                       NetResponseEntity<Map<String, List<MedicineResponseEntity>>> mapNetResponseEntity) {
                        String errorText1 = libWrapper(mapNetResponseEntity);
                        String errorText2 = customWrapper(listNetResponseEntity);
                        if (TextUtils.isEmpty(errorText1)) {
                            return errorText2;
                        } else {
                            return errorText1;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loading.set(false);
                        e.printStackTrace();
                        error.set(true);
                    }

                    @Override
                    public void onNext(String text) {
                        error.set(false);
                        loading.set(false);
                        if (TextUtils.isEmpty(text)) {
                            render();
                        } else {
                            showToast(text);
                        }
                    }
                });
    }

    private void unSubscribe() {
        if (getDataSubscription != null && !getDataSubscription.isUnsubscribed()) {
            getDataSubscription.unsubscribe();
        }
    }

    private void render() {
        list.clear();
        list.add(new IMedicineModel.Search());
        list.addAll(customList);
        list.addAll(libList);
        loadSuccess.set(!loadSuccess.get());
    }

    public void addMedicine(String name) {
        adding.set(true);
        this.useCase.addCustomMedicine(new MedicineAddEntity(SaveUtils.getUserId(), name),
                new Subscriber<NetResponseEntity<CustomMedicineEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showToast(getApplication().getString(R.string.network_error));
                        adding.set(false);
                    }

                    @Override
                    public void onNext(NetResponseEntity<CustomMedicineEntity> netResponseEntity) {
                        adding.set(false);
                        if (netResponseEntity != null) {
                            if (netResponseEntity.getCode() == 0) {
                                CustomMedicineEntity entity = netResponseEntity.getData();
                                if (entity != null) {
                                    customList.add(0, MedicineModelWrapper.wrapFromCustom(entity));
                                }
                                render();
                            } else {
                                showToast(netResponseEntity.getMessage());
                            }
                        }
                    }
                });
    }

    public void deleteMedicine(int index) {
        IMedicineModel model = list.get(index);
        if (model instanceof IMedicineModel.Content) {
            final IMedicineModel.Content content = (IMedicineModel.Content) model;
            MedicineDeleteRequestEntity entity = new MedicineDeleteRequestEntity(SaveUtils.getUserId(), content.id);
            this.useCase.deleteCustomMedicine(entity, new Subscriber<NetResponseEntity>() {
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
                        customList.remove(content);
                        render();
                        showToast(getApplication().getString(R.string.medicine_delete_success));
                    } else {
                        showToast(netResponseEntity.getMessage());
                    }
                }
            });
        }
    }

}
