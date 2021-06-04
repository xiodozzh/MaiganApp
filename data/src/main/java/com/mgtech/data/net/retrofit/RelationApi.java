package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.request.CheckPhoneRequestEntity;
import com.mgtech.domain.entity.net.request.DealRelationRequestEntity;
import com.mgtech.domain.entity.net.request.InviteFriendRequestEntity;
import com.mgtech.domain.entity.net.request.RemindFriendMeasureRequestEntity;
import com.mgtech.domain.entity.net.request.SearchPermissionRequestEntity;
import com.mgtech.domain.entity.net.request.SendRelationRequestRequestEntity;
import com.mgtech.domain.entity.net.request.SetRelationNoteNameRequestEntity;
import com.mgtech.domain.entity.net.request.UploadContactRequestEntity;
import com.mgtech.domain.entity.net.response.CheckPhoneLoginResponseEntity;
import com.mgtech.domain.entity.net.response.ContactBean;
import com.mgtech.domain.entity.net.response.FriendDataResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.RelationRequestResponseEntity;
import com.mgtech.domain.entity.net.response.RelationshipResponseEntity;
import com.mgtech.domain.entity.net.response.SearchContactResponseEntity;
import com.mgtech.domain.entity.net.response.SearchPermissionsResponseEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

/**
 * @author zhaixiang
 */
public interface RelationApi {
    /**
     * 获取关系列表
     *
     * @param type     0 我监护的人，1 监护我的人，2 我监护的人和监护我的人
     * @param page     页序号
     * @param pageSize 每页数目
     * @return 关系列表
     */
    @GET(HttpApi.API_GET_RELATIONSHIP)
    Observable<NetResponseEntity<List<RelationshipResponseEntity>>> getRelationList(@Query("type") int type,
                                                                                    @Query("page") int page,
                                                                                    @Query("pageSize") int pageSize);

    /**
     * 查找手机号是否是注册用户
     *
     * @param entity 手机号列表
     * @return 结果列表
     */
    @POST(HttpApi.API_CHECK_USER_BY_PHONE)
    Observable<NetResponseEntity<List<CheckPhoneLoginResponseEntity>>> checkPhones(@Body CheckPhoneRequestEntity entity);

    /**
     * 发送关注请求
     *
     * @param entity 请求
     * @return 结果返回
     */
    @POST(HttpApi.API_SEND_RELATION_REQUEST)
    Observable<NetResponseEntity> sendRelationRequest(@Body SendRelationRequestRequestEntity entity);


    @GET(HttpApi.API_GET_REQUEST)
    Observable<NetResponseEntity<List<RelationRequestResponseEntity>>> getRequest(@Query("page") int pageNumber,
                                                                                  @Query(("pageSize")) int pageSize);

    @POST(HttpApi.API_SET_RELATION)
    Observable<NetResponseEntity> dealRelationRequest(@Body DealRelationRequestEntity entity);

    @GET(HttpApi.API_REMOVE_RELATION)
    Observable<NetResponseEntity> removeRelation(@Query("accountGuid") String userId, @Query("relationGuid") String relationId);

    @POST(HttpApi.API_INVITE_FRIEND)
    Observable<NetResponseEntity> inviteFriend(@Body InviteFriendRequestEntity entity);


    @POST(HttpApi.API_SET_RELATION_NOTE_NAME)
    Observable<NetResponseEntity> setAuthority(@Body SetRelationNoteNameRequestEntity entity);


    @GET(HttpApi.API_GET_FRIEND_DATA)
    Observable<NetResponseEntity<FriendDataResponseEntity>> getFriendData(@Query("friendGuid") String friendId);


    @POST(HttpApi.API_UPLOAD_CONTACT)
    Observable<NetResponseEntity<Map<String, List<ContactBean>>>> uploadContact(@Body UploadContactRequestEntity entity);


    @GET(HttpApi.API_GET_CONTACT)
    Observable<NetResponseEntity<Map<String, List<ContactBean>>>> getContact(@Query("accountGuid") String id);

    @GET(HttpApi.API_SEARCH_CONTACT)
    Observable<NetResponseEntity<SearchContactResponseEntity>> searchContact(@Query("accountGuid") String id, @Query("searchText") String text);

    @GET(HttpApi.API_GET_FRIEND_INFO)
    Single<NetResponseEntity<RelationshipResponseEntity>> getFriendInfo(@Query("accountGuid") String id, @Query("targetAccountGuid") String targetId,
                                                                        @Query("friendType") int type);

    @POST(HttpApi.API_REMIND_FRIEND_MEASURE)
    Single<NetResponseEntity> remindFriendMeasure(@Body RemindFriendMeasureRequestEntity entity);

    @POST(HttpApi.API_SET_SEARCH_PERMISSION)
    Single<NetResponseEntity> setSearchPermissions(@Body SearchPermissionRequestEntity entity);

    @GET(HttpApi.API_GET_SEARCH_PERMISSION)
    Single<NetResponseEntity<SearchPermissionsResponseEntity>> getSearchPermissions(@Query("accountGuid") String id);
}
