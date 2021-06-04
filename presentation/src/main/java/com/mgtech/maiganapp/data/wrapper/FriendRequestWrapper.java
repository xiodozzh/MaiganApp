package com.mgtech.maiganapp.data.wrapper;

import com.mgtech.domain.entity.net.response.RelationRequestResponseEntity;
import com.mgtech.maiganapp.data.model.FriendRequestModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaixiang
 */
public class FriendRequestWrapper {
    public static List<FriendRequestModel> getListFromNetEntityToFriendRequestAddModel(List<RelationRequestResponseEntity> list){
        List<FriendRequestModel> modelList = new ArrayList<>();
        for (RelationRequestResponseEntity entity : list) {
            FriendRequestModel model = new FriendRequestModel();
            model.targetUserId = entity.getSenderAccountGuid();
            model.name = entity.getSenderName();
            model.avatarUri = entity.getSenderAvatarUrl();
            model.message = entity.getMessage();
            model.result = entity.getResult();
            model.id = entity.getRequestGuid();
            modelList.add(model);
        }
        return modelList;
    }
}
