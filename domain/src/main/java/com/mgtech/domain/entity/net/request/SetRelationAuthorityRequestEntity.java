//package com.mgtech.domain.entity.net.request;
//
//import com.google.gson.Gson;
//import com.mgtech.domain.entity.net.RequestEntity;
//import com.mgtech.domain.utils.HttpApi;
//
///**
// * @author zhaixiang
// */
//public class SetRelationAuthorityRequestEntity implements RequestEntity {
//
//    /**
//     * accountGuid :
//     * targetAccountGuid :
//     * authorityPush : 1
//     * authorityRead : 1
//     * noteName :
//     */
//
//    private String targetAccountGuid;
//    private int authorityPush;
//    private int authorityRead;
//    private String noteName;
//
//    public SetRelationAuthorityRequestEntity(String targetAccountGuid, int authorityPush, int authorityRead, String noteName) {
//        this.targetAccountGuid = targetAccountGuid;
//        this.authorityPush = authorityPush;
//        this.authorityRead = authorityRead;
//        this.noteName = noteName;
//    }
//
//    @Override
//    public String getUrl() {
//        return HttpApi.API_SET_RELATION_AUTHORIZATION;
//    }
//
//    @Override
//    public String getBody() {
//        return new Gson().toJson(this);
//    }
//
//    public String getTargetAccountGuid() {
//        return targetAccountGuid;
//    }
//
//    public void setTargetAccountGuid(String targetAccountGuid) {
//        this.targetAccountGuid = targetAccountGuid;
//    }
//
//    public int getAuthorityPush() {
//        return authorityPush;
//    }
//
//    public void setAuthorityPush(int authorityPush) {
//        this.authorityPush = authorityPush;
//    }
//
//    public int getAuthorityRead() {
//        return authorityRead;
//    }
//
//    public void setAuthorityRead(int authorityRead) {
//        this.authorityRead = authorityRead;
//    }
//
//    public String getNoteName() {
//        return noteName;
//    }
//
//    public void setNoteName(String noteName) {
//        this.noteName = noteName;
//    }
//
//    @Override
//    public String toString() {
//        return "SetRelationAuthorityRequestEntity{" +
//                "targetAccountGuid='" + targetAccountGuid + '\'' +
//                ", authorityPush=" + authorityPush +
//                ", authorityRead=" + authorityRead +
//                ", noteName='" + noteName + '\'' +
//                '}';
//    }
//}
