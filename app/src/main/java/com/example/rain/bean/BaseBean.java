package com.example.rain.bean;


public class BaseBean<T> {

    public long id;

    private String code;
    private String msg;
    private T data;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getMsg() { return msg; }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
