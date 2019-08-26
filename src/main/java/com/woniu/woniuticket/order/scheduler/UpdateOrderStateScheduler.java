package com.woniu.woniuticket.order.scheduler;

import com.woniu.woniuticket.order.constant.SystemConstant;
import com.woniu.woniuticket.order.dao.OrderMapper;
import com.woniu.woniuticket.order.exception.OrderException;
import com.woniu.woniuticket.order.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class UpdateOrderStateScheduler {

    @Autowired
    private OrderMapper orderMapper;

    // 将已观看电影订单状态更新(每天0点执行一次)
    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void modifyOrderState(){
        // 查询数据库订单状态为2的订单
        List<Order> orders = orderMapper.selectOrderByOrderState(SystemConstant.ORDER_NO_WATCH_STATE);
        // 获取当前时间
        Date currentTime = new Date();
        for (Order order : orders) {
            if (order.getEndTime().getTime() < currentTime.getTime()){
                // 将订单状态改为3(已观影状态)
                int row = orderMapper.updateWatchedOrderStateByOrderId(SystemConstant.ORDER_WATCHED_STATE,order.getOrderId());
                System.out.println("+++++++++++++++++++++++++++++++++++++");
                if (row <= 0) {
                    throw new OrderException("订单状态更新失败!");
                }
            }
        }
    }
}
