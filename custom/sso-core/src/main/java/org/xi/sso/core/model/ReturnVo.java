package org.xi.sso.core.model;

import java.io.Serializable;

public class ReturnVo<T> implements Serializable {
    public static final long serialVersionUID = 42L;

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;

    public static final ReturnVo<String> SUCCESS = new ReturnVo(null);
    public static final ReturnVo<String> FAIL = new ReturnVo(FAIL_CODE, null);

    private int code;
    private String msg;
    private T data;

    public ReturnVo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnVo(T data) {
        this.code = SUCCESS_CODE;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
