package com.mgtech.maiganapp.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import android.text.TextUtils;

import com.mgtech.domain.entity.ContactEntity;
import com.mgtech.domain.entity.net.request.UploadContactRequestEntity;
import com.mgtech.domain.entity.net.response.ContactBean;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.SearchContactResponseEntity;
import com.mgtech.domain.interactor.ContactUseCase;
import com.mgtech.domain.interactor.RelationUseCase;
import com.mgtech.domain.utils.SaveUtils;
import com.mgtech.maiganapp.MyApplication;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.FriendAddEditModel;
import com.mgtech.maiganapp.data.model.FriendAddEditTextModel;
import com.mgtech.maiganapp.data.model.FriendAddItemModel;
import com.mgtech.maiganapp.data.model.FriendAddListHeaderModel;
import com.mgtech.maiganapp.data.model.IFriendAddModel;
import com.mgtech.maiganapp.data.wrapper.FriendAddModelWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * @author zhaixiang
 */
public class FriendAddListViewModel extends BaseViewModel {
    private List<FriendAddItemModel> list = new ArrayList<>();
    public List<IFriendAddModel> filterList = new ArrayList<>();
    public MutableLiveData<List<IFriendAddModel>> modelList = new MutableLiveData<>();
    public MutableLiveData<Boolean> searchSuccess = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public ObservableBoolean checkingFriendInfo = new ObservableBoolean(false);

    public ObservableBoolean haveAuthToReadContact = new ObservableBoolean(false);
    private ContactUseCase contactUseCase;
    private RelationUseCase relationUseCase;
    public FriendAddItemModel checkedFriendModel;
    private FriendAddEditModel editModel = new FriendAddEditModel();
    public FriendAddEditTextModel editTextModel = new FriendAddEditTextModel();
    private FriendAddListHeaderModel headerModel = new FriendAddListHeaderModel();

    public FriendAddListViewModel(@NonNull Application application) {
        super(application);
        contactUseCase = ((MyApplication) application).getContactUseCase();
        relationUseCase = ((MyApplication) application).getRelationUseCase();
    }

    /**
     * 查询通讯录好友
     */
    public void getFriendList() {
//        list.clear();
        loading.setValue(true);
        contactUseCase.searchContact(new Subscriber<List<ContactEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                loading.setValue(false);
            }

            @Override
            public void onNext(List<ContactEntity> contactEntities) {
                uploadContact(contactEntities);
            }
        });
    }

    /**
     * 清除数据
     */
    public void clearList() {
        list.clear();
        filterList.clear();
        editTextModel.editText = "";
        filterList.add(editModel);
        filterList.add(headerModel);
        modelList.setValue(filterList);
    }


    private void uploadContact(final List<ContactEntity> contactEntities) {
        UploadContactRequestEntity uploadContactRequestEntity = new UploadContactRequestEntity();
        uploadContactRequestEntity.setAccountGuid(SaveUtils.getUserId(getApplication()));
        List<UploadContactRequestEntity.ContactBean> beans = new ArrayList<>();
        for (ContactEntity e : contactEntities) {
            UploadContactRequestEntity.ContactBean bean = new UploadContactRequestEntity.ContactBean();
            bean.setNameInContact(e.getName());
            bean.setPhone(e.getPhone());
            bean.setContactIdStringInContact(e.getId());
            beans.add(bean);
        }
        uploadContactRequestEntity.setContacts(beans);
        relationUseCase.uploadContact(uploadContactRequestEntity, new Subscriber<NetResponseEntity<Map<String, List<ContactBean>>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                loading.setValue(false);
            }

            @Override
            public void onNext(NetResponseEntity<Map<String, List<ContactBean>>> netResponseEntity) {
                loading.setValue(false);
                if (netResponseEntity.getCode() == 0) {
                    Map<String, List<ContactBean>> map = netResponseEntity.getData();
                    if (map != null) {
                        list.clear();
                        list.addAll(FriendAddModelWrapper.getListFromNet(map));
                        filterPhone(editTextModel.editText);
                    }
                } else {
                    showToast(netResponseEntity.getMessage());
                }
            }
        });
    }

    /**
     * 根据查询过滤结果
     *
     * @param s 查询
     */
    public void filterPhone(String s) {
        filterList.clear();
        filterList.add(editModel);
        editTextModel.editText = s;
        if (TextUtils.isEmpty(s)){
            filterList.add(headerModel);
            filterList.addAll(list);
        }else {
            filterList.add(editTextModel);
            filterList.add(headerModel);
            for (FriendAddItemModel model : list) {
                if (model.phone.contains(s)) {
                    filterList.add(model);
                }
            }
        }
        modelList.setValue(filterList);
    }


    public void checkUser(String phone) {
        checkingFriendInfo.set(true);
        relationUseCase.searchContact(SaveUtils.getUserId(getApplication()), phone, new Subscriber<NetResponseEntity<SearchContactResponseEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                checkingFriendInfo.set(false);
                showToast(getApplication().getString(R.string.network_error));
            }

            @Override
            public void onNext(NetResponseEntity<SearchContactResponseEntity> netResponseEntity) {
                int code = netResponseEntity.getCode();
                if (code == 0) {
                    SearchContactResponseEntity entity = netResponseEntity.getData();
                    if (entity != null) {
                        checkedFriendModel = FriendAddModelWrapper.getModelFromNet(entity);
                        searchSuccess.setValue(true);
                    }
                } else if (code == 1) {
                    showToast(getApplication().getString(R.string.activity_friend_add_list_already_monitored));
                } else {
                    showToast(getApplication().getString(R.string.activity_friend_add_list_not_register));
                }
                checkingFriendInfo.set(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        contactUseCase.unSubscribe();
        relationUseCase.unSubscribe();
    }

}
