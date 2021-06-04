package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;

public class QQLoginResponseEntity {

    /**
     * ret : 0
     * openid : 386838FED3B9F6E4624B21FD22905D9A
     * access_token : 4D06EBA2C65CD66E52BFE59B6614A96C
     * payToken : 6345F901DB101D389E8956E3F29AC7D5
     * expiresIn : 7776000
     * pf : desktop_m_qq-10000144-android-2002-
     * pfkey : 72a3e8e0d3203f56326757275dab29fe
     * msg :
     * login_cost : 43
     * query_authority_cost : 228
     * authority_cost : 0
     * expires_time : 1556516669508
     */

    private int ret;
    private String openid;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("pay_token")
    private String payToken;
    @SerializedName("expires_in")
    private int expiresIn;
    private String pf;
    private String pfkey;
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

    public String getPfkey() {
        return pfkey;
    }

    public void setPfkey(String pfkey) {
        this.pfkey = pfkey;
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

    public void setqueryAuthorityCost(int queryAuthorityCost) {
        this.queryAuthorityCost = queryAuthorityCost;
    }

    public int getauthorityCost() {
        return authorityCost;
    }

    public void setauthorityCost(int authorityCost) {
        this.authorityCost = authorityCost;
    }

    public long getexpiresTime() {
        return expiresTime;
    }

    public void setexpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    @Override
    public String toString() {
        return "QQLoginResponseEntity{" +
                "ret=" + ret +
                ", openid='" + openid + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", payToken='" + payToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", pf='" + pf + '\'' +
                ", pfkey='" + pfkey + '\'' +
                ", msg='" + msg + '\'' +
                ", loginCost=" + loginCost +
                ", queryAuthorityCost=" + queryAuthorityCost +
                ", authorityCost=" + authorityCost +
                ", expiresTime=" + expiresTime +
                '}';
    }
}
