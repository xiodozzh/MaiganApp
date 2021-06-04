package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.CheckPhoneLoginResponseEntity;
import com.mgtech.domain.entity.net.response.ContactBean;
import com.mgtech.domain.entity.net.response.SearchContactResponseEntity;
import com.mgtech.maiganapp.data.model.FriendAddItemModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhaixiang
 */
public class FriendAddModelWrapper {

    public static FriendAddItemModel getModelFromNet(SearchContactResponseEntity entity){
        FriendAddItemModel model = new FriendAddItemModel();

        model.id = entity.getAccountGuid();
        model.contactName = entity.getNameInAddressBook();
        model.avatarUri = entity.getPortrait();
        model.relation = FriendAddItemModel.RELATION_NOT_FOLLOW;
        model.phone = entity.getPhone();
        model.name = entity.getNameInMystrace();
        return model;
    }

    public static List<FriendAddItemModel> getListFromNetEntityToFriendAddModel(List<CheckPhoneLoginResponseEntity> list){
        List<FriendAddItemModel> modelList = new ArrayList<>();
        for (CheckPhoneLoginResponseEntity entity : list) {
            FriendAddItemModel model = getModelFromNetEntityToFriendAddModel(entity);
            modelList.add(model);
        }
        return modelList;
    }

    public static List<FriendAddItemModel> getListFromNet(Map<String, List<ContactBean>> map){
        List<FriendAddItemModel> modelList = new ArrayList<>();
        for (Map.Entry<String,List<ContactBean>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<ContactBean> list = entry.getValue();
            for (ContactBean bean :list) {
                FriendAddItemModel model = new FriendAddItemModel();
                model.searchText = key;
                model.phone = bean.getPhone();
                model.contactName = bean.getNameInContact();
                model.name = bean.getNameInMystrace();
                model.avatarUri = bean.getPortrait();
                model.id = bean.getAccountGuid();
                if (bean.getIsMystraceUser() == 1){
                    if (bean.getIsFriend()==1){
                        model.relation = FriendAddItemModel.RELATION_FOLLOWED;
                    }else{
                        model.relation = FriendAddItemModel.RELATION_NOT_FOLLOW;
                    }
                }else{
                    model.relation = FriendAddItemModel.RELATION_NOT_REGISTER;
                }
                modelList.add(model);
            }
        }
        return modelList;
    }

    public static FriendAddItemModel getModelFromNetEntityToFriendAddModel(CheckPhoneLoginResponseEntity entity){
        FriendAddItemModel model = new FriendAddItemModel();
        model.id = entity.getAccountGuid();
        model.contactName = entity.getContractName();
        model.relation = entity.getRelation();
        model.phone = entity.getPhone();
        model.zone = entity.getZone();
        model.name = entity.getDisplayName();
        return model;
    }
}
