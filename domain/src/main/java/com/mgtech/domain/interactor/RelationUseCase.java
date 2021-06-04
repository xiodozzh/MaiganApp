package com.mgtech.domain.interactor;

import com.mgtech.domain.entity.net.request.CheckPhoneRequestEntity;
import com.mgtech.domain.entity.net.request.DealRelationRequestEntity;
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
import com.mgtech.domain.utils.NetConstant;

import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * @author zhaixiang
 * 关系
 */

public class RelationUseCase {
    private NetRepository.Relation repository;
    private Subscription checkPhoneSubscription;
    private Subscription sendRequestSubscription;
    private Subscription setRelationSubscription;
    private Subscription removeRelationSubscription;
    private Subscription getRelationRequestSubscription;
    private Subscription getRelationSubscription;
    private Subscription inviteSubscription;
    private Subscription setNoteNameSubscription;
    private Subscription getFriendDataSubscription;
    private Subscription uploadContactSubscription;
    private Subscription getContactSubscription;
    private Subscription searchContactSubscription;
    private Subscription getFriendInfoSubscription;
    private Subscription remindFriendMeasureSubscription;

    public RelationUseCase(NetRepository.Relation repository) {
        this.repository = repository;
    }


    /**
     * 检查手机号是否注册
     *
     * @param entity  手机号列表
     * @param subscriber 订阅
     */
    public void checkPhone(CheckPhoneRequestEntity entity, Subscriber<NetResponseEntity<List<CheckPhoneLoginResponseEntity>>> subscriber) {
        unSubscribeCheckPhone();
        checkPhoneSubscription = this.repository.checkPhone(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void uploadContact(UploadContactRequestEntity entity, Subscriber<NetResponseEntity<Map<String,List<ContactBean>>>>subscriber){
        unSubscribeUploadContact();
        uploadContactSubscription = this.repository.uploadContact(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getContact(String id, Subscriber<NetResponseEntity<Map<String,List<ContactBean>>>>subscriber){
        unSubscribeGetContact();
        getContactSubscription = this.repository.getContact(new UserIdRequestEntity(id),NetConstant.NO_CACHE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void searchContact(String id,String text, Subscriber<NetResponseEntity<SearchContactResponseEntity>>subscriber){
        unSubscribeSearchContact();
        searchContactSubscription = this.repository.searchContact(new SearchContactRequestEntity(id,text),NetConstant.NO_CACHE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 发送关注请求
     *
     * @param entity     关注请求
     * @param subscriber 订阅Fre
     */
    public void sendRelationRequest(SendRelationRequestRequestEntity entity,
                                    Subscriber<NetResponseEntity> subscriber) {
        unSubscribeSendRequest();
        sendRequestSubscription = this.repository.sendRelationRequest(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 处理关注请求
     *
     * @param entity     处理的关注请求
     * @param subscriber 订阅
     */
    public void setRelation(DealRelationRequestEntity entity, Subscriber<NetResponseEntity> subscriber) {
        unSubscribeSetRelation();
        this.setRelationSubscription = this.repository.dealRelationship(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 取消关注
     *
     * @param entity     取消的关注
     * @param subscriber 订阅
     */
    public void removeRelation(RemoveRelationRequestEntity entity, Subscriber<NetResponseEntity> subscriber) {
        this.removeRelationSubscription = this.repository.removeRelationship(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取请求关注列表
     *
     * @param subscriber 订阅
     */
    public void getRelationRequest(int page, int pageSize, Subscriber<NetResponseEntity<List<RelationRequestResponseEntity>>> subscriber) {
        GetRelationRequestEntity entity = new GetRelationRequestEntity(page,pageSize);
        this.getRelationRequestSubscription = this.repository.getRelationRequest(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getRelationship(GetRelationshipEntity entity,int cacheType, Subscriber<NetResponseEntity<List<RelationshipResponseEntity>>> subscriber) {
        this.getRelationSubscription = this.repository.getRelationship(entity,cacheType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

//    public void setAuthority(SetRelationAuthorityRequestEntity entity,Subscriber<NetResponseEntity>subscriber){
//        unSubscribeSetAuthority();
//        this.setAuthoritySubscription = this.repository.setAuthority(entity)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }

    public void setAuthority(SetRelationNoteNameRequestEntity entity,Subscriber<NetResponseEntity>subscriber){
        unSubscribeSetNoteName();
        this.setNoteNameSubscription = this.repository.setAuthority(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void invite(InviteFriendRequestEntity entity, Subscriber<NetResponseEntity>subscriber){
        unSubscribeInvite();
        this.inviteSubscription = this.repository.inviteFriend(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getFriendData(String friendId, int cacheType, Subscriber<NetResponseEntity<FriendDataResponseEntity>> subscriber){
        unSubscribeGetFriendData();
        this.getFriendDataSubscription = this.repository.getFriendData(new GetFriendDataRequestEntity(friendId),cacheType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getFriendInfo(String id,String targetId,int friendType, int cacheType, Subscriber<NetResponseEntity<RelationshipResponseEntity>> subscriber){
        unSubscribeGetFriendInfo();
        GetFriendInfoRequestEntity entity = new GetFriendInfoRequestEntity(id,targetId,friendType);
        this.getFriendInfoSubscription = this.repository.getFriendInfo(entity,cacheType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void reminderFriendMeasure(String userId, String friendId, Subscriber<NetResponseEntity>subscriber){
        unSubscribeRemindFriend();
        this.remindFriendMeasureSubscription = this.repository.remindFriendMeasure(
                new RemindFriendMeasureRequestEntity(userId,friendId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void setSearchPermission(String userId, boolean forbidFindByPhone,Subscriber<NetResponseEntity>subscriber){
        SearchPermissionRequestEntity entity = new SearchPermissionRequestEntity(userId,forbidFindByPhone?1:0);
        repository.setSearchPermissions(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    public void getSearchPermission(String userId,Subscriber<NetResponseEntity<SearchPermissionsResponseEntity>>subscriber){
        repository.getSearchPermissions(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void unSubscribe() {
        unSubscribeCheckPhone();
        unSubscribeSendRequest();
        unSubscribeSetRelation();
        unSubscribeRemoveRelation();
        unSubscribeGetRelationRequest();
        unSubscribeGetRelation();
        unSubscribeGetFriendData();
        unSubscribeUploadContact();
        unSubscribeGetContact();
        unSubscribeSearchContact();
        unSubscribeGetFriendInfo();
        unSubscribeRemindFriend();
    }

    private void unSubscribeCheckPhone(){
        if (checkPhoneSubscription != null && !checkPhoneSubscription.isUnsubscribed()) {
            checkPhoneSubscription.unsubscribe();
        }
    }

    private void unSubscribeSendRequest(){
        if (sendRequestSubscription != null && !sendRequestSubscription.isUnsubscribed()) {
            sendRequestSubscription.unsubscribe();
        }
    }

    private void unSubscribeSetRelation(){
        if (setRelationSubscription != null && !setRelationSubscription.isUnsubscribed()) {
            setRelationSubscription.unsubscribe();
        }
    }

    private void unSubscribeRemoveRelation(){
        if (removeRelationSubscription != null && !removeRelationSubscription.isUnsubscribed()) {
            removeRelationSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetRelationRequest(){
        if (getRelationRequestSubscription != null && !getRelationRequestSubscription.isUnsubscribed()) {
            getRelationRequestSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetRelation(){
        if (getRelationSubscription != null && !getRelationSubscription.isUnsubscribed()) {
            getRelationSubscription.unsubscribe();
        }
    }



    private void unSubscribeSetNoteName(){
        if (setNoteNameSubscription != null && !setNoteNameSubscription.isUnsubscribed()) {
            setNoteNameSubscription.unsubscribe();
        }
    }

    private void unSubscribeInvite(){
        if (inviteSubscription != null && !inviteSubscription.isUnsubscribed()) {
            inviteSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetFriendData(){
        if (getFriendDataSubscription != null && !getFriendDataSubscription.isUnsubscribed()) {
            getFriendDataSubscription.unsubscribe();
        }
    }

    private void unSubscribeUploadContact(){
        if (uploadContactSubscription != null && !uploadContactSubscription.isUnsubscribed()) {
            uploadContactSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetContact(){
        if (getContactSubscription != null && !getContactSubscription.isUnsubscribed()) {
            getContactSubscription.unsubscribe();
        }
    }

    private void unSubscribeSearchContact(){
        if (searchContactSubscription != null && !searchContactSubscription.isUnsubscribed()) {
            searchContactSubscription.unsubscribe();
        }
    }

    private void unSubscribeGetFriendInfo(){
        if (getFriendInfoSubscription != null && !getFriendInfoSubscription.isUnsubscribed()) {
            getFriendInfoSubscription.unsubscribe();
        }
    }

    private void unSubscribeRemindFriend(){
        if (remindFriendMeasureSubscription != null && !remindFriendMeasureSubscription.isUnsubscribed()) {
            remindFriendMeasureSubscription.unsubscribe();
        }
    }

}
