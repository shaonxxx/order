package com.woniu.woniuticket.order.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woniu.woniuticket.order.config.AlipayConfig;
import com.woniu.woniuticket.order.dto.AlipayRefundResponseDTO;
import com.woniu.woniuticket.order.dto.AlipayRefundResponseSignDTO;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AlipayRefundUtil {

    public AlipayRefundResponseDTO AlipayRefund(String out_trade_no,String trade_no,String refund_amount,String refund_reason,String out_request_no) throws AlipayApiException, IOException {
        // 获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        // 设置请求参数
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"trade_no\":\""+ trade_no +"\","
                + "\"refund_amount\":\""+ refund_amount +"\","
                + "\"refund_reason\":\""+ refund_reason +"\","
                + "\"out_request_no\":\""+ out_request_no +"\"}");
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(alipayRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        if(response.isSuccess()){
            // 将json数据转化对象
            ObjectMapper om = new ObjectMapper();
            AlipayRefundResponseSignDTO refundResponseSignDTO = om.readValue(response.getBody(),AlipayRefundResponseSignDTO.class);
            return refundResponseSignDTO.getAlipay_trade_refund_response();
        }
        return new AlipayRefundResponseDTO();
    }
}
