package com.mgtech.domain.entity.net.response;

public class ProvinceCodeEntity {

    /**
     * name : 阿富汗
     * guid :
     */

    private String name;
    private String guid;

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

    @Override
    public String toString() {
        return "ProvinceCodeEntity{" +
                "name='" + name + '\'' +
                ", guid='" + guid + '\'' +
                '}';
    }
}
