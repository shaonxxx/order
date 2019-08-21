package com.woniu.woniuticket.order.service.impl;

import com.woniu.woniuticket.order.dao.OrderMapper;
import com.woniu.woniuticket.order.pojo.Condition;
import com.woniu.woniuticket.order.pojo.Order;
import com.woniu.woniuticket.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    // 条件查询所有订单
    @Override
    public List<Order> selectAllOrders(Integer currentPage, Integer pageSize,Condition condition) {
        return orderMapper.selectAllOrders(currentPage,pageSize, condition);
    }

    // 更新订单表
    @Override
    public void updatePayInfor(Order orders) {
        orderMapper.updateByPrimaryKeySelective(orders);
    }
}
