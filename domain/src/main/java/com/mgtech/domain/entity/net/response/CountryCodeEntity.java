package com.mgtech.domain.entity.net.response;

/**
 * @author zhaixiang
 */
public class CountryCodeEntity {

    /**
     * name : 阿富汗
     * guid :
     * containsCity : 0
     */

    private String name;
    private String guid;
    private int containsCity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getContainsCity() {
        return containsCity;
    }

    public void setContainsCity(int containsCity) {
        this.containsCity = containsCity;
    }

    @Override
    public String toString() {
        return "CountryCodeEntity{" +
                "name='" + name + '\'' +
                ", guid='" + guid + '\'' +
                ", containsCity=" + containsCity +
                '}';
    }
}
