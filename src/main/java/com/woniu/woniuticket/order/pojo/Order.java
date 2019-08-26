package com.woniu.woniuticket.order.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private Integer orderId;

    private Integer userId;

    private Integer chipId;

    private String pipeNum;

    private Date createTime;

    private String seat;

    private String proof;

    private BigDecimal totalPrice;

    private String orderNum;

    private Integer orderState;

    private String payType;

    private Integer filmNum;

    private Integer couponId;

    private String orderQrcode;

    private Date startTime;

    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Integer getFilmNum() {
        return filmNum;
    }

    public void setFilmNum(Integer filmNum) {
        this.filmNum = filmNum;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getOrderQrcode() {
        return orderQrcode;
    }

    public void setOrderQrcode(String orderQrcode) {
        this.orderQrcode = orderQrcode;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", chipId=" + chipId +
                ", pipeNum='" + pipeNum + '\'' +
                ", createTime=" + createTime +
                ", seat='" + seat + '\'' +
                ", proof='" + proof + '\'' +
                ", totalPrice=" + totalPrice +
                ", orderNum='" + orderNum + '\'' +
                ", orderState=" + orderState +
                ", payType='" + payType + '\'' +
                ", filmNum=" + filmNum +
                ", couponId=" + couponId +
                ", orderQrcode='" + orderQrcode + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}