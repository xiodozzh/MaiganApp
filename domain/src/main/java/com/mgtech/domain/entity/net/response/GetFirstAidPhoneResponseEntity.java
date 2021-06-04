package com.mgtech.domain.entity.net.response;

import java.util.List;

public class GetFirstAidPhoneResponseEntity {

    /**
     * isBought : 1
     * firstAidPhones : ["57071497","13189734","13454222"]
     */

    private int isBought;
    private String[] firstAidPhones;

    public int getIsBought() {
        return isBought;
    }

    public void setIsBought(int isBought) {
        this.isBought = isBought;
    }

    public String[] getFirstAidPhones() {
        return firstAidPhones;
    }

    public void setFirstAidPhones(String[] firstAidPhones) {
        this.firstAidPhones = firstAidPhones;
    }

    @Override
    public String toString() {
        return "GetFirstAidPhoneResponseEntity{" +
                "isBought=" + isBought +
                ", firstAidPhones=" + firstAidPhones +
                '}';
    }
}
