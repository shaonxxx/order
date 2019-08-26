package com.woniu.woniuticket.order.dto;

/**
 * 退款带签名的数据传输对象
 */
public class AlipayRefundResponseSignDTO {

    private AlipayRefundResponseDTO alipay_trade_refund_response;

    private String sign;

    public AlipayRefundResponseDTO getAlipay_trade_refund_response() {
        return alipay_trade_refund_response;
    }

    public void setAlipay_trade_refund_response(AlipayRefundResponseDTO alipay_trade_refund_response) {
        this.alipay_trade_refund_response = alipay_trade_refund_response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
