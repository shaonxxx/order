package com.woniu.woniuticket.order.dao;

import com.woniu.woniuticket.order.pojo.Condition;
import com.woniu.woniuticket.order.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
    // 条件查询所有订单
    List<Order> selectAllOrders(@Param("currentPage") Integer currentPage,@Param("pageSize") Integer pageSize,@Param("condition") Condition condition);
}