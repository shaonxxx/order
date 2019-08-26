package com.woniu.woniuticket.order.dto;

import java.io.Serializable;

/**
 * 响应回前端的订单数据传输对象
 */
public class ResultDTO implements Serializable {

    /**
     * 状态码
     * 返回状态 成功200   错误500
     */
    private int code;

    /**
     * 描述信息
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
