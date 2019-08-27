package com.woniu.woniuticket.order.dao;

import com.woniu.woniuticket.order.pojo.Coupon;
import com.woniu.woniuticket.order.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface OrderMapper {
    int deleteByPrimaryKey(Integer orderId);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer orderId);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKeyWithBLOBs(Order record);

    int updateByPrimaryKey(Order record);
    // 条件查询所有已完成订单
    List<Order> selectAllOrders(@Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize, @Param("startDay") String startDay, @Param("endDay")String endDay, @Param("payType")String payType, @Param("orderState")String orderState, @Param("orderNum")String orderNum);
    // 根据订单查询订单创建时间
    Date selectCreateOrderTime(Integer orderId);
    // 根据订单Id将该订单改为无效订单(状态为6)
    void updateOrderStateByOrderId(@Param("orderDisableState") Integer orderDisableState,@Param("orderId") Integer orderId);
    // 根据订单中代金券Id,查询出代金券金额
    Double selectCouponPriceById(Integer couponId);
    // 更新订单信息(实际金额和二维码)
    int updateOrderByOrderId(@Param("orderId") Integer orderId,@Param("realTotalPrice") Double realTotlPrice);
    // 更新订单信息(二维码)
    int updateOrderQRCodeByOrderId(@Param("orderId") Integer orderId,@Param("qrCode") String qrCode);
    // 通过订单编号,更新订单状态
    int updateOrderInfo(Order order);
    // 根据订单编号查询出订单id
    Integer selectOrderIdByOrderNum(String orderNum);
    // 根据订单编号查询该订单
    Order selectOrderStateByOrderNum(String orderNum);
    // 根据代金券Id查询出代金券对象
    Coupon selectCouponByCouponId(Integer couponId);
    // 修改代金券状态
    int updateCouponStateByCouponId(@Param("couponState") Integer couponState,@Param("couponId")Integer couponId);
    // 通过订单编号修改退款订单状态(4:已退款)
    int updateOrderStateByOrderNum(@Param("orderRefundMoneyState") Integer orderRefundMoneyState, @Param("orderNum") String orderNum);
    // 修改退款订单代金券状态(未实现)
    int updateRefundCouponStateByCouponId(Integer couponId);
    // 查询数据库订单状态为2(已付款未观影)
    List<Order> selectOrderByOrderState(Integer orderNoWatchState);
    // 将订单状态改为3(已观影状态)
    int updateWatchedOrderStateByOrderId(@Param("orderWatchedState") Integer orderWatchedState,@Param("orderId") Integer orderId);
    // 代金券不可用修改订单状态为6
    int modifyOrderState(@Param("orderId") Integer orderId, @Param("orderState") Integer orderState);
}