package com.mgtech.domain.entity.socket;

import com.mgtech.domain.entity.net.response.PwDataResponseEntity;

public class SocketAutoPwEntity {
    private String module;
    private String messageName;
    private PwDataResponseEntity data;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public PwDataResponseEntity getData() {
        return data;
    }

    public void setData(PwDataResponseEntity data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SocketEntity{" +
                "module='" + module + '\'' +
                ", messageName='" + messageName + '\'' +
                ", data=" + data +
                '}';
    }
}
