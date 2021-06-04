package com.mgtech.domain.entity.net.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaixiang on 2017/1/6.
 * 网络结果
 */
public class SingleResultEntity<T>  {
    @SerializedName("Header")
    private String header = "";
    @SerializedName("Flag")
    private String result = "";
    @SerializedName("Body")
    private T data;
    @SerializedName("Additional")
    private List<ResultMessage> messages;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMessage(){
        if (messages != null && !messages.isEmpty()){
            return messages.get(0).getMessage();
        }
        return null;
    }

    public List<ResultMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ResultMessage> messages) {
        this.messages = messages;
    }



    @Override
    public String toString() {
        return "ResultEntity{" +
                "header='" + header + '\'' +
                ", result='" + result + '\'' +
                ", data=" + data +
                ", messages=" + messages +
                '}';
    }

    public class ResultMessage{
        @SerializedName("Message")
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "ResultMessage{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }
}
