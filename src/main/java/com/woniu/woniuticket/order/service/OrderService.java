package com.woniu.woniuticket.order.service;

import com.alipay.api.AlipayApiException;
import com.woniu.woniuticket.order.dto.ResultDTO;
import com.woniu.woniuticket.order.pojo.Coupon;
import com.woniu.woniuticket.order.pojo.Order;

import java.util.List;

public interface OrderService {
    // 条件查询所有已完成订单
    List<Order> selectAllOrders(Integer currentPage, Integer pageSize, String startDay, String endDay, String payType, String orderState, String orderNum);
    // 创建订单
    // ResultDTO createOrder(Order order);
    // 付款
    String payMoney(Integer orderId) throws AlipayApiException;
    // 通过订单编号,更新订单状态
    Integer updateOrderInfo(Order order);
    // 根据订单编号查询订单id
    Integer selectOrderIdByOrderNum(String out_trade_no);
    // 根据订单编号查询订单状态
    Order selectOrderStateByOrderNum(String out_trade_no);
    // 通过订单编号修改订单状态(4:已退款)
    Integer updateOrderStateByOrderId(Integer orderRefundMoneyState, String orderNum);
    // 根据代金券id查询代金券对象
    Coupon selectCouponByCouponId(Integer couponId);
    // 修改退款代金券状态
    Integer updateCouponStateByCouponId(Integer couponCanUseState, Integer couponId);
    // 付款成功,修改订单信息
    Integer modifyOrder(String out_trade_no);
    // 将生成的订单投递到MQ队列中
    ResultDTO sendOrderToQueue(Order order);
}
