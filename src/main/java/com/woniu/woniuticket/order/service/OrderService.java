package com.woniu.woniuticket.order.service;

import com.woniu.woniuticket.order.pojo.Condition;
import com.woniu.woniuticket.order.pojo.Order;

import java.util.List;

public interface OrderService {
    // 条件查询所有订单
    List<Order> selectAllOrders(Integer currentPage, Integer pageSize, Condition condition);
    // 更新订单表
    void updatePayInfor(Order orders);
}
