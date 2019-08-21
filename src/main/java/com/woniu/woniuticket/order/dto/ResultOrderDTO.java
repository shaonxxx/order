package com.woniu.woniuticket.order.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 响应回前端的订单数据传输对象
 */
public class ResultOrderDTO implements Serializable {

    private Integer orderId;

    private Integer userId;

    private Integer chipId;

    private String pipeNum;

    private Date createTime;

    private String seat;

    private String proof;

    private Long totalPrice;

    private String orderNum;

    private Integer orderState;

    private String orderQrcode;

    public ResultOrderDTO() {
    }

    public ResultOrderDTO(Integer orderId, Integer userId, Integer chipId, String pipeNum, Date createTime, String seat, String proof, Long totalPrice, String orderNum, Integer orderState, String orderQrcode) {
        this.orderId = orderId;
        this.userId = userId;
        this.chipId = chipId;
        this.pipeNum = pipeNum;
        this.createTime = createTime;
        this.seat = seat;
        this.proof = proof;
        this.totalPrice = totalPrice;
        this.orderNum = orderNum;
        this.orderState = orderState;
        this.orderQrcode = orderQrcode;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getChipId() {
        return chipId;
    }

    public void setChipId(Integer chipId) {
        this.chipId = chipId;
    }

    public String getPipeNum() {
        return pipeNum;
    }

    public void setPipeNum(String pipeNum) {
        this.pipeNum = pipeNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public String getOrderQrcode() {
        return orderQrcode;
    }

    public void setOrderQrcode(String orderQrcode) {
        this.orderQrcode = orderQrcode;
    }
}
