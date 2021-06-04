package com.mgtech.domain.entity;

import com.google.gson.annotations.SerializedName;

public class QQLoginEntity {

    /**
     * ret : 0
     * openid : 386838FED3B9F6E4624B21FD22905D9A
     * access_token : 4D06EBA2C65CD66E52BFE59B6614A96C
     * pay_token : 7D9CD02C230EC8D0DACEAEA3A42EDBFE
     * expires_in : 7776000
     * pf : desktop_m_qq-10000144-android-2002-
     * pfkey : 5813aac933086a4360e6ec759189862f
     * msg :
     * login_cost : 36
     * query_authority_cost : 227
     * authority_cost : 0
     * expires_time : 1552535172766
     */

    @SerializedName("ret")
    private int ret;
    @SerializedName("openid")
    private String openid;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("pay_token")
    private String payToken;
    @SerializedName("expires_in")
    private int expiresIn;
    @SerializedName("pf")
    private String pf;
    @SerializedName("pfkey")
    private String pfKey;
    @SerializedName("msg")
    private String msg;
    @SerializedName("login_cost")
    private int loginCost;
    @SerializedName("query_authority_cost")
    private int queryAuthorityCost;
    @SerializedName("authority_cost")
    private int authorityCost;
    @SerializedName("expires_time")
    private long expiresTime;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getPayToken() {
        return payToken;
    }

    public void setPayToken(String payToken) {
        this.payToken = payToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getPf() {
        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public String getPfKey() {
        return pfKey;
    }

    public void setPfKey(String pfKey) {
        this.pfKey = pfKey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getLoginCost() {
        return loginCost;
    }

    public void setLoginCost(int loginCost) {
        this.loginCost = loginCost;
    }

    public int getQueryAuthorityCost() {
        return queryAuthorityCost;
    }

    public void setQueryAuthorityCost(int queryAuthorityCost) {
        this.queryAuthorityCost = queryAuthorityCost;
    }

    public int getAuthorityCost() {
        return authorityCost;
    }

    public void setAuthorityCost(int authorityCost) {
        this.authorityCost = authorityCost;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    @Override
    public String toString() {
        return "QQLoginEntity{" +
                "ret=" + ret +
                ", openid='" + openid + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", payToken='" + payToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", pf='" + pf + '\'' +
                ", pfKey='" + pfKey + '\'' +
                ", msg='" + msg + '\'' +
                ", loginCost=" + loginCost +
                ", queryAuthorityCost=" + queryAuthorityCost +
                ", authorityCost=" + authorityCost +
                ", expiresTime=" + expiresTime +
                '}';
    }
}
