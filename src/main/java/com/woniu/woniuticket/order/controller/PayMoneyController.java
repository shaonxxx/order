package com.woniu.woniuticket.order.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.woniu.woniuticket.order.config.AlipayConfig;
import com.woniu.woniuticket.order.constant.SystemConstant;
import com.woniu.woniuticket.order.dto.AlipayRefundRequestDTO;
import com.woniu.woniuticket.order.dto.AlipayRefundResponseDTO;
import com.woniu.woniuticket.order.dto.ResultDTO;
import com.woniu.woniuticket.order.pojo.Coupon;
import com.woniu.woniuticket.order.pojo.Order;
import com.woniu.woniuticket.order.service.OrderService;
import com.woniu.woniuticket.order.utils.AlipayRefundUtil;
import com.woniu.woniuticket.order.utils.AlipayUtil;
import com.woniu.woniuticket.order.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 付款退款接口
 */
@RestController
// 允许跨域访问
@CrossOrigin
@RequestMapping("/pay")
@Api(tags = "付款退款接口")
public class PayMoneyController {

    @Autowired
    private AlipayUtil alipayUtil;

    @Autowired
    private AlipayRefundUtil alipayRefundUtil;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 付款
     * @param orderId
     * @return
     * @throws AlipayApiException
     */
    @ApiOperation(value = "付款",notes = "订单状态为1(待支付)才能付款")
    @RequestMapping("/alipayMoney")
    public String payMoney(Integer orderId) throws AlipayApiException {
        return orderService.payMoney(orderId);
    }

    // 付款成功异步回调
    @RequestMapping(value = "/save")
    public void getResponseInfo(HttpServletRequest request, String out_trade_no, String trade_no, String trade_status) throws AlipayApiException {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
        if (signVerified) {//验证成功
            if (trade_status.equals("TRADE_SUCCESS")) {
                // 如果交易完成，更新订单状态
                orderService.modifyOrder(out_trade_no);
                // (根据订单编号查询订单id)
                Integer orderId = orderService.selectOrderIdByOrderNum(out_trade_no);
                Order order = new Order();
                // 订单编号
                order.setOrderNum(out_trade_no);
                //插入流水号
                order.setPipeNum(trade_no);
                // 修改付款状态(2:表示已付款)
                order.setOrderState(SystemConstant.ORDER_NO_WATCH_STATE);
                // 取票凭证
                String proof = DateUtil.getShortDate().replace("-","") + orderId;
                order.setProof(proof);
                // 通过订单编号,更新订单状态
                orderService.updateOrderInfo(order);
                // 向用户发送订票成功信息
                // 删除Redis中此订单信息
                redisTemplate.delete("orderId:"+orderId);
            }
        }
    }

    /**
     * 退款
     * @param
     * @return
     * @throws AlipayApiException
     * @throws IOException
     */
    @ApiOperation(value = "订单", notes = "退款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orders", value = "订单号")
    })
    @PostMapping(value = "refund")
//    @RequestMapping("/refund")
    public ResultDTO refund(AlipayRefundRequestDTO requestDTO) throws AlipayApiException, IOException {
        ResultDTO resultDTO = new ResultDTO();
        //退款时，前端需要将订单号或者支付宝交易号传到后台
//        requestDTO.setOut_trade_no("1111111111");
//        requestDTO.setTrade_no("");
//        requestDTO.setRefund_amount("50.0");
//        requestDTO.setRefund_reason("");
//        requestDTO.setOut_request_no("");
        // 根据订单号查询订单
        Order order = orderService.selectOrderStateByOrderNum(requestDTO.getOut_trade_no());
        System.out.println("====="+order);
        System.out.println(order.getOrderState());
        if(order.getOrderState() != 2){
            resultDTO.setCode(500);
            resultDTO.setMessage("此订单不能退款!");
            resultDTO.setData(null);
            return resultDTO;
        }else {
            // 获取当前退款时间
            Date currentTime = new Date();
            // 开场前10分钟不能退款
            if (order.getStartTime().getTime() - currentTime.getTime() < 0){
                resultDTO.setCode(500);
                resultDTO.setMessage("电影已放映,不能退款!");
                resultDTO.setData(null);
                return resultDTO;
            }else if (order.getStartTime().getTime() - currentTime.getTime() > 0 && order.getStartTime().getTime() - currentTime.getTime() <= (10*60*1000)){
                resultDTO.setData(500);
                resultDTO.setMessage("开场前十分钟不能退款!");
                resultDTO.setData(null);
                return resultDTO;
            }else if(order.getStartTime().getTime() - currentTime.getTime() > (10*60*1000) && order.getStartTime().getTime() - currentTime.getTime() <= (30*60*1000)){
                // 开场时间在10-30分钟
                // 退款金额不能高于实际付款金额
                if (Double.parseDouble(requestDTO.getRefund_amount()) > order.getTotalPrice().doubleValue()){
                    resultDTO.setCode(500);
                    resultDTO.setMessage("退款金额不能高于实际付款金额");
                    resultDTO.setData(null);
                    return resultDTO;
                }else {
                    // 收取30%手续费
                    Double realRefundDouble = (order.getTotalPrice().doubleValue())-(order.getTotalPrice().doubleValue()*(0.3));
                    String realRefund = realRefundDouble.toString();
                    AlipayRefundResponseDTO refundResponseDTO=alipayRefundUtil.AlipayRefund(requestDTO.getOut_trade_no(),requestDTO.getTrade_no(),realRefund,requestDTO.getRefund_reason(),requestDTO.getOut_request_no());
                    if(refundResponseDTO.getMsg().equals("Success")){
                        System.out.println("退款成功!");
                        resultDTO.setCode(200);
                        resultDTO.setMessage("开场前30分钟收取30%手续费");
                        resultDTO.setData(refundResponseDTO);
                        System.out.println(resultDTO);
                        // 更改订单状态通过订单编号(4:已退款)
                        orderService.updateOrderStateByOrderId(SystemConstant.ORDER_REFUND_MONEY_STATE,order.getOrderNum());
                        // 如果用户使用了代金券,则更新代金券状态
                        if (order.getCouponId() != null){
                            // 根据代金券id查询代金券对象
                            Coupon coupon = orderService.selectCouponByCouponId(order.getCouponId());
                            // 修改代金券状态(0:可以使用)
                            orderService.updateCouponStateByCouponId(SystemConstant.COUPON_CAN_USE_STATE,coupon.getCouponId());
                        }
                        return resultDTO;
                    }
                }
            }else {
                // 开场前30分钟退全款
                // 退款金额不能高于实际付款金额
                if (Double.parseDouble(requestDTO.getRefund_amount()) > order.getTotalPrice().doubleValue()){
                    resultDTO.setCode(500);
                    resultDTO.setMessage("退款金额不能高于实际付款金额");
                    resultDTO.setData(null);
                    return resultDTO;
                }else {
                    AlipayRefundResponseDTO refundResponseDTO=alipayRefundUtil.AlipayRefund(requestDTO.getOut_trade_no(),requestDTO.getTrade_no(),requestDTO.getRefund_amount(),requestDTO.getRefund_reason(),requestDTO.getOut_request_no());
                    if(refundResponseDTO.getMsg().equals("Success")){
                        System.out.println("退款成功!");
                        resultDTO.setCode(200);
                        resultDTO.setMessage("退款成功!");
                        resultDTO.setData(refundResponseDTO);
                        System.out.println(resultDTO);
                        // 更改订单状态通过订单编号
                        orderService.updateOrderStateByOrderId(SystemConstant.ORDER_REFUND_MONEY_STATE, order.getOrderNum());
                        // 如果用户使用了代金券,则更新代金券状态
                        if (order.getCouponId() != null){
                            // 根据代金券id查询代金券对象
                            Coupon coupon = orderService.selectCouponByCouponId(order.getCouponId());
                            // 修改代金券状态
                            orderService.updateCouponStateByCouponId(SystemConstant.COUPON_CAN_USE_STATE, coupon.getCouponId());
                        }
                        return resultDTO;
                    }
                }
            }
        }
        return null;
    }

}
