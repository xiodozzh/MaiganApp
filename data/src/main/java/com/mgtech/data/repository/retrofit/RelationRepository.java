package com.mgtech.data.repository.retrofit;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.mgtech.data.net.retrofit.DataStoreFactory;
import com.mgtech.data.net.retrofit.RelationApi;
import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.domain.entity.net.request.CheckPhoneRequestEntity;
import com.mgtech.domain.entity.net.request.DealRelationRequestEntity;
import com.mgtech.domain.entity.net.request.DefaultRequestEntity;
import com.mgtech.domain.entity.net.request.GetFriendDataRequestEntity;
import com.mgtech.domain.entity.net.request.GetFriendInfoRequestEntity;
import com.mgtech.domain.entity.net.request.GetRelationRequestEntity;
import com.mgtech.domain.entity.net.request.GetRelationshipEntity;
import com.mgtech.domain.entity.net.request.InviteFriendRequestEntity;
import com.mgtech.domain.entity.net.request.RemindFriendMeasureRequestEntity;
import com.mgtech.domain.entity.net.request.RemoveRelationRequestEntity;
import com.mgtech.domain.entity.net.request.SearchContactRequestEntity;
import com.mgtech.domain.entity.net.request.SearchPermissionRequestEntity;
import com.mgtech.domain.entity.net.request.SendRelationRequestRequestEntity;
import com.mgtech.domain.entity.net.request.SetRelationNoteNameRequestEntity;
import com.mgtech.domain.entity.net.request.UploadContactRequestEntity;
import com.mgtech.domain.entity.net.request.UserIdRequestEntity;
import com.mgtech.domain.entity.net.response.CheckPhoneLoginResponseEntity;
import com.mgtech.domain.entity.net.response.ContactBean;
import com.mgtech.domain.entity.net.response.FriendDataResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.RelationRequestResponseEntity;
import com.mgtech.domain.entity.net.response.RelationshipResponseEntity;
import com.mgtech.domain.entity.net.response.SearchContactResponseEntity;
import com.mgtech.domain.entity.net.response.SearchPermissionsResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;
import com.mgtech.domain.utils.NetConstant;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Single;

/**
 * @author zhaixiang
 */
public class RelationRepository implements NetRepository.Relation {
    private RelationApi api;
    private Context context;

    public RelationRepository(Context context) {
        this.context = context;
        this.api = new RetrofitFactory()
                .setBaseUrl(HttpApi.API_BASE_URL)
                .setUseToken(true)
                .build(context).create(RelationApi.class);
    }

    @Override
    public Observable<NetResponseEntity<List<CheckPhoneLoginResponseEntity>>> checkPhone(CheckPhoneRequestEntity entity) {
        return api.checkPhones(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> sendRelationRequest(SendRelationRequestRequestEntity entity) {
        return api.sendRelationRequest(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> dealRelationship(DealRelationRequestEntity entity) {
        return api.dealRelationRequest(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> removeRelationship(RemoveRelationRequestEntity entity) {
        return api.removeRelation(entity.getUserId(),entity.getRelationGuid()).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<List<RelationRequestResponseEntity>>> getRelationRequest(GetRelationRequestEntity entity) {
        return api.getRequest(entity.getPage(),entity.getPageSize()).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<List<RelationshipResponseEntity>>> getRelationship(GetRelationshipEntity entity, int cacheType) {
        Type type = new TypeToken<NetResponseEntity<List<RelationshipResponseEntity>>>() {
        }.getType();
        return DataStoreFactory.strategy(context,
                api.getRelationList(entity.getType(),entity.getCurrentPage(),entity.getPageSize()).doOnError(new DoOnTokenErrorAction()),
                entity, cacheType, type);
    }

    @Override
    public Observable<NetResponseEntity> inviteFriend(InviteFriendRequestEntity entity) {
        return api.inviteFriend(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity> setAuthority(SetRelationNoteNameRequestEntity entity) {
        return api.setAuthority(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<FriendDataResponseEntity>> getFriendData(GetFriendDataRequestEntity entity, int cacheType) {
        Type type = new TypeToken<NetResponseEntity<FriendDataResponseEntity>>() {
        }.getType();
        return DataStoreFactory.strategy(context, api.getFriendData(entity.getFriendId()).doOnError(new DoOnTokenErrorAction()),
                entity, cacheType, type);
    }

    @Override
    public Observable<NetResponseEntity<Map<String,List<ContactBean>>>> uploadContact(UploadContactRequestEntity entity) {
        return api.uploadContact(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Observable<NetResponseEntity<SearchContactResponseEntity>> searchContact(SearchContactRequestEntity entity, int cacheType) {
        Type type = new TypeToken<NetResponseEntity<SearchContactResponseEntity>>() {
        }.getType();
        return DataStoreFactory.strategy(context,
                api.searchContact(entity.getUserId(),entity.getText()).doOnError(new DoOnTokenErrorAction()),
                entity, cacheType, type);
    }

    @Override
    public Observable<NetResponseEntity<Map<String,List<ContactBean>>>> getContact(UserIdRequestEntity entity, int cacheType) {
        Type type = new TypeToken<NetResponseEntity<Map<String,List<ContactBean>>>>() {
        }.getType();
        return DataStoreFactory.strategy(context,
                api.getContact(entity.getId()).doOnError(new DoOnTokenErrorAction()),
                new DefaultRequestEntity(HttpApi.API_GET_CONTACT+entity.getId()), cacheType, type);
    }

    @Override
    public Single<NetResponseEntity<RelationshipResponseEntity>> getFriendInfo(GetFriendInfoRequestEntity entity, int cacheType) {
        return api.getFriendInfo(entity.getAccountId(),entity.getTargetId(),entity.getFriendType())
                .doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Single<NetResponseEntity> remindFriendMeasure(RemindFriendMeasureRequestEntity entity) {
        return api.remindFriendMeasure(entity).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Single<NetResponseEntity> setSearchPermissions(SearchPermissionRequestEntity entity) {
        return api.setSearchPermissions(entity);
    }

    @Override
    public Single<NetResponseEntity<SearchPermissionsResponseEntity>> getSearchPermissions(String userId) {
        return api.getSearchPermissions(userId);
    }
}
