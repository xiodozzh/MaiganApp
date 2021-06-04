//package com.mgtech.maiganapp.viewmodel;
//
//import android.app.Application;
//import android.text.TextUtils;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.MutableLiveData;
//
//import com.mgtech.domain.entity.net.response.CheckServiceAuthorityResponseEntity;
//import com.mgtech.domain.entity.net.response.GetServiceAuthCodeResponseEntity;
//import com.mgtech.domain.entity.net.response.GetServiceResponseEntity;
//import com.mgtech.domain.entity.net.response.NetResponseEntity;
//import com.mgtech.domain.interactor.ServeUseCase;
//import com.mgtech.domain.utils.SaveUtils;
//import com.mgtech.maiganapp.MyApplication;
//import com.mgtech.maiganapp.R;
//import com.mgtech.maiganapp.data.model.ServiceCompanyModel;
//import com.mgtech.maiganapp.data.model.ServiceModel;
//import com.mgtech.maiganapp.data.wrapper.ServiceModelWrapper;
//
//import java.util.List;
//
//import rx.Subscriber;
//
//public class ServiceViewModel extends BaseViewModel {
//    public MutableLiveData<List<ServiceModel>> data = new MutableLiveData<>();
//    public MutableLiveData<Boolean> error = new MutableLiveData<>();
//    public MutableLiveData<String> waitingString = new MutableLiveData<>();
//    public MutableLiveData<String> pageUrl = new MutableLiveData<>();
//    public MutableLiveData<ServiceCompanyModel> needAuthCompany = new MutableLiveData<>();
//    //    public MutableLiveData<List<ServiceCompanyModel>> showCompanyList = new MutableLiveData<>();
//    private ServeUseCase serveUseCase;
////    private String enterServiceId;
//
//    public ServiceViewModel(@NonNull Application application) {
//        super(application);
//        serveUseCase = ((MyApplication) application).getServeUseCase();
//        error.setValue(true);
//    }
//
//    public void getData() {
//        serveUseCase.getService(SaveUtils.getUserId(), new Subscriber<NetResponseEntity<List<GetServiceResponseEntity>>>() {
//            @Override
//            public void onCompleted() {
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//                showToast(getApplication().getString(R.string.network_error));
//                error.setValue(true);
//            }
//
//            @Override
//            public void onNext(NetResponseEntity<List<GetServiceResponseEntity>> listNetResponseEntity) {
//                if (listNetResponseEntity.getCode() == 0) {
//                    data.setValue(ServiceModelWrapper.getListFromNet(listNetResponseEntity.getData()));
//                    error.setValue(false);
//                } else {
//                    showToast(listNetResponseEntity.getMessage());
//                    error.setValue(true);
//                }
//            }
//        });
//    }
//
//    public void getAuthorCode(ServiceCompanyModel company) {
//        waitingString.setValue("正在授权");
//        serveUseCase.GetServiceAuthorCode(SaveUtils.getUserId(), company.service.id, company.id,
//                new Subscriber<NetResponseEntity<GetServiceAuthCodeResponseEntity>>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        waitingString.setValue("");
//                        showToast(getApplication().getString(R.string.network_error));
//                    }
//
//                    @Override
//                    public void onNext(NetResponseEntity<GetServiceAuthCodeResponseEntity> netResponseEntity) {
//                        waitingString.setValue("");
//                        if (netResponseEntity.getCode() == 0) {
//                            GetServiceAuthCodeResponseEntity getServiceAuthCodeResponseEntity = netResponseEntity.getData();
//                            if (getServiceAuthCodeResponseEntity != null) {
//                                SaveUtils.saveServiceAuthCode(getServiceAuthCodeResponseEntity.getAuthCode());
//                                jump(company, getServiceAuthCodeResponseEntity.getAuthCode());
//                            } else {
//                                showToast(getApplication().getString(R.string.network_error));
//                            }
//                        } else {
//                            showToast(netResponseEntity.getMessage());
//                        }
//                    }
//                });
//    }
//
//
//    public void checkAuth(final ServiceCompanyModel company) {
//        waitingString.setValue("请稍等");
//        serveUseCase.checkCompanyAuthor(SaveUtils.getUserId(), company.service.id, company.id,
//                new Subscriber<NetResponseEntity<CheckServiceAuthorityResponseEntity>>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        waitingString.setValue("");
//                        showToast(getApplication().getString(R.string.network_error));
//                    }
//
//                    @Override
//                    public void onNext(NetResponseEntity<CheckServiceAuthorityResponseEntity> netResponseEntity) {
//                        waitingString.setValue("");
//                        if (netResponseEntity.getCode() == 0) {
//                            CheckServiceAuthorityResponseEntity entity = netResponseEntity.getData();
//                            if (entity != null) {
//                                if (entity.getAuthStatus() == 2) {
//                                    jump(company, "");
//                                } else if (entity.getAuthStatus() == 1) {
//                                    jump(company, entity.getAuthCode());
//                                } else {
//                                    needAuthCompany.setValue(company);
//                                }
//                            } else {
//                                needAuthCompany.setValue(company);
//                            }
//                        }
//                    }
//                });
//    }
//
//    private void jump(ServiceCompanyModel company, String code) {
//        pageUrl.setValue(company.url + "?phone=" + SaveUtils.getPhone(getApplication()) + "&zone=" + SaveUtils.getZone(getApplication()) + "&code=" + code + "&serviceCode=" + company.service.serviceCode);
//    }
//}
