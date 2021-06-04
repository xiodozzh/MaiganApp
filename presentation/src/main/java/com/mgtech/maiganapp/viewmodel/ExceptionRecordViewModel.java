package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;

import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.response.ExceptionResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.interactor.ExceptionUseCase;
import com.mgtech.domain.interactor.PersonalInfoUseCase;
import com.mgtech.domain.utils.MyConstant;
import com.mgtech.domain.utils.NetConstant;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.ExceptionRecordModel;
import com.mgtech.maiganapp.data.model.IndicatorDataModel;
import com.mgtech.maiganapp.data.wrapper.ExceptionRecordModelWrapper;

import java.util.ArrayList;
import java.util.List;

import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zhaixiang
 */
public class ExceptionRecordViewModel extends BaseViewModel {
    private static final int PAGE_SIZE = 20;
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<Boolean> empty = new MutableLiveData<>();
    public MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>();
    public ObservableField<String> name = new ObservableField<>("");
    private ExceptionUseCase useCase;
    private PersonalInfoUseCase personalInfoUseCase;
    private String userId;
    private boolean man;
    public LiveData<PagedList<ExceptionRecordModel>> models;

    public ExceptionRecordViewModel(@NonNull Application application) {
        super(application);
        useCase = ((MyApplication) application).getExceptionUseCase();
        personalInfoUseCase = ((MyApplication) application).getPersonInfoUseCase();
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(PAGE_SIZE)
                .build();
        MyDataSourceFactory factory = new MyDataSourceFactory();
        models = new LivePagedListBuilder<>(factory, config).build();
    }

    private void getUserInfo(String userId) {
        personalInfoUseCase.getInfo(NetConstant.NO_CACHE, userId, new Subscriber<NetResponseEntity<PersonalInfoEntity>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(NetResponseEntity<PersonalInfoEntity> netResponseEntity) {
                if (netResponseEntity.getCode() == 0) {
                    PersonalInfoEntity entity = netResponseEntity.getData();
                    name.set(entity.getDisplayName());
                    man = entity.getSex() == MyConstant.MAN;
                }
            }
        });
    }

    public void deleteException(final ExceptionRecordModel model) {

        useCase.deleteException(userId, model.noticeId, new Subscriber<NetResponseEntity>() {
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
                    deleteSuccess.setValue(true);
                } else {
                    showToast(netResponseEntity.getMessage());
                }
            }
        });
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        useCase.unSubscribe();
        personalInfoUseCase.unSubscribe();
    }

    public void setUserId(String id) {
        getUserInfo(id);
        this.userId = id;
    }

    public void invalidateDataSource(){
        if (models.getValue()==null){
            return;
        }
        models.getValue().getDataSource().invalidate();
    }

    public String getUserId() {
        return userId;
    }

    public boolean isMan() {
        return man;
    }

    public void setMan(boolean man) {
        this.man = man;
    }


    private class MyDataSourceFactory extends DataSource.Factory<Integer, ExceptionRecordModel> {
//        private MutableLiveData<MyDataSource> liveData = new MutableLiveData<>();

        @Override
        public DataSource<Integer, ExceptionRecordModel> create() {
//            MyDataSource myDataSource = new MyDataSource();
//            liveData.postValue(myDataSource);
            return new MyDataSource();
        }
    }

    private class MyDataSource extends PositionalDataSource<ExceptionRecordModel> {

        @Override
        public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<ExceptionRecordModel> callback) {
            useCase.getExceptionObservable(userId, 0, PAGE_SIZE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleSubscriber<NetResponseEntity<List<ExceptionResponseEntity>>>() {
                        @Override
                        public void onSuccess(NetResponseEntity<List<ExceptionResponseEntity>> listNetResponseEntity) {
                            if (listNetResponseEntity.getCode() == 0) {
                                List<ExceptionResponseEntity> entities = listNetResponseEntity.getData();
                                if (entities != null) {
                                    empty.setValue(entities.isEmpty());
                                    callback.onResult(ExceptionRecordModelWrapper.getModelListFromNet(entities), 0, entities.size());
                                }
                            } else {
                                showToast(listNetResponseEntity.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            showToast(getApplication().getString(R.string.network_error));
                        }
                    });
        }

        @Override
        public void loadRange(@NonNull LoadRangeParams params
                , @NonNull LoadRangeCallback<ExceptionRecordModel> callback) {
            useCase.getExceptionObservable(userId, params.startPosition/PAGE_SIZE, PAGE_SIZE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleSubscriber<NetResponseEntity<List<ExceptionResponseEntity>>>() {
                        @Override
                        public void onSuccess(NetResponseEntity<List<ExceptionResponseEntity>> listNetResponseEntity) {
                            if (listNetResponseEntity.getCode() == 0) {
                                List<ExceptionResponseEntity> entities = listNetResponseEntity.getData();
                                if (entities != null) {
                                    callback.onResult(ExceptionRecordModelWrapper.getModelListFromNet(entities));
                                }
                            } else {
                                showToast(listNetResponseEntity.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable error) {
                            showToast(getApplication().getString(R.string.network_error));
                        }
                    });
        }

    }
}
