package com.mgtech.domain.entity.socket;

public class SocketEntity {
    private String module;
    private String messageName;
    private Object data;

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


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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
