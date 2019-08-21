package com.woniu.woniuticket.order.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.woniu.woniuticket.order.config.AlipayConfig;
import org.springframework.stereotype.Component;

@Component
public class AlipayUtil {

    public String alipay(String out_trade_no,String subject,String total_amount,String body) throws AlipayApiException {
        //获得初始化的AlipayClient
        String serverUrl = AlipayConfig.gatewayUrl;
        String appId = AlipayConfig.app_id;
        String privateKey = AlipayConfig.merchant_private_key;
        String format = "json";
        String charset = AlipayConfig.charset;
        String alipayPublicKey = AlipayConfig.alipay_public_key;
        String signType = AlipayConfig.sign_type;
        String returnUrl = AlipayConfig.return_url;
        String notifyUrl = AlipayConfig.notify_url;
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId, privateKey, format, charset, alipayPublicKey, signType);
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\"10m\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        //返回付款信息
        return result;
    }
}
