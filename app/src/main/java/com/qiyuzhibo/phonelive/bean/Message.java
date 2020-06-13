package com.qiyuzhibo.phonelive.bean;

public class Message {
    Object object;
    String  id;

    public Message(Object object, String id) {
        this.object = object;
        this.id = id;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
