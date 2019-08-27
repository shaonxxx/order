package com.woniu.woniuticket.order.controller;

import com.github.pagehelper.PageInfo;
import com.woniu.woniuticket.order.constant.SystemConstant;
import com.woniu.woniuticket.order.dto.ResultDTO;
import com.woniu.woniuticket.order.pojo.Order;
import com.woniu.woniuticket.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
// 允许跨域访问
@CrossOrigin
@Api(tags = "订单管理接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "查询所有订单(可以带条件)",notes = "按条件分页展示所有已完成的订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数"),
            @ApiImplicitParam(name = "pageSize",value = "每页总条数"),
            @ApiImplicitParam(name = "startDay",value = "条件开始日"),
            @ApiImplicitParam(name = "endDay",value = "条件截止日"),
            @ApiImplicitParam(name = "payType",value = "条件支付方式"),
            @ApiImplicitParam(name = "orderState",value = "条件订单状态,1：待支付、2：已付款未观影、3：已观影、4：已退款、5：待评价、6：无效。"),
            @ApiImplicitParam(name = "orderNum",value = "条件订单编号")
    })
    @GetMapping("/queryAllOrders")
    public Object queryAllOrders(@RequestParam(value = "currentPage",defaultValue = "1",required = false) Integer currentPage,
                                 @RequestParam(value = "pageSize",defaultValue = "10",required = false) Integer pageSize,
                                 @RequestParam(value = "startDay",required = false) String startDay,
                                 @RequestParam(value = "endDay",required = false) String endDay,
                                 @RequestParam(value = "payType",required = false) String payType,
                                 @RequestParam(value = "orderState",required = false) String orderState,
                                 @RequestParam(value = "orderNum",required = false) String orderNum){
        List<Order> orders = orderService.selectAllOrders(currentPage,pageSize,startDay,endDay,payType,orderState,orderNum);
        PageInfo<Order> pageInfo = new PageInfo<>(orders);
        return pageInfo;
    }

    @ApiOperation(value = "生成订单",notes = "创建订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order",value = "前端传过来的数据封装到Order对象"),
    })
    @GetMapping("/createOrder")
    public ResultDTO createOrder(Order order){
        ResultDTO result = null;
        order.setChipId(1002);
        order.setUserId(1002);
        order.setChipId(102);
        order.setSeat("5,6");
        order.setTotalPrice(new BigDecimal(35));
        order.setCreateTime(new Date());
        order.setOrderNum(UUID.randomUUID().toString().replace("-",""));
        order.setOrderState(SystemConstant.ORDER_NO_PAY_STATE);
        order.setCouponId(1001);
        order.setPayType("支付宝");
        //result = orderService.createOrder(order);
        // 生成订单(将订单数据投递到MQ队列中)
        result = orderService.sendOrderToQueue(order);
        return result;
    }
}