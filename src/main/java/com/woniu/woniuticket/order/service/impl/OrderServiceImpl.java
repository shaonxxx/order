package com.woniu.woniuticket.order.service.impl;

import com.alipay.api.AlipayApiException;
import com.rabbitmq.client.Channel;
import com.woniu.woniuticket.order.constant.SystemConstant;
import com.woniu.woniuticket.order.dao.OrderMapper;
import com.woniu.woniuticket.order.dto.ResultDTO;
import com.woniu.woniuticket.order.exception.OrderException;
import com.woniu.woniuticket.order.pojo.Coupon;
import com.woniu.woniuticket.order.pojo.Order;
import com.woniu.woniuticket.order.service.OrderService;
import com.woniu.woniuticket.order.utils.AlipayUtil;
import com.woniu.woniuticket.order.utils.QRCodeUtil.QRCodeUtil;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AlipayUtil alipayUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 条件查询所有已完成订单
    @Override
    public List<Order> selectAllOrders(Integer currentPage, Integer pageSize,String startDay,String endDay,String payType,String orderState,String orderNum) {
        return orderMapper.selectAllOrders(currentPage,pageSize,startDay,endDay,payType,orderState,orderNum);
    }

    /*
    // 创建订单
    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    public ResultDTO createOrder(Order order) {
        ResultDTO result = new ResultDTO();
        int row = orderMapper.insertSelective(order);
        // 当前插入订单的id
        int orderId = order.getOrderId();
        // 查询出当前插入的订单
        Order currentOrder = orderMapper.selectByPrimaryKey(orderId);
        System.out.println("------->"+orderId);
        if (row > 0){
            result.setCode(200);
            result.setMessage("订单创建成功!");
            result.setData(currentOrder);
            // 将订单放入Redis中
            redisTemplate.opsForValue().set("orderId:"+orderId,currentOrder);
            // 设置订单有效时间10分钟
            redisTemplate.expire("orderId:"+orderId,10, TimeUnit.MINUTES);
            return result;
        }else {
            result.setCode(500);
            result.setMessage("订单创建失败!");
            result.setData(null);
            return result;
        }
    }*/
    // 将生成的订单投递到MQ队列中(生产者)
    @Override
    public ResultDTO sendOrderToQueue(Order order) {
        ResultDTO result = new ResultDTO();
        try {
            rabbitTemplate.convertAndSend("order-exchange",null,order);
            result.setCode(200);
            result.setMessage("订单创建成功!");
            result.setData(order);
            return result;
        } catch (AmqpException e) {
            e.printStackTrace();
            result.setCode(500);
            result.setMessage("订单创建失败!");
            result.setData(null);
            return result;
        }
    }

    // 消费者(创建订单)
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(name = "order-queue",autoDelete = "false",durable = "false"),
                            exchange = @Exchange(name = "order-exchange",type = "fanout")
                    )
            }
    )
    @RabbitHandler
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    public void saveOrder(@Payload Order order, Channel channel, @Headers Map header) {
        // 将订单添加到数据库
        Long tag = (Long) header.get(AmqpHeaders.DELIVERY_TAG);
        try {
            channel.basicAck(tag,false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int row = orderMapper.insertSelective(order);
        // 当前插入订单的id
        int orderId = order.getOrderId();
        // 查询出当前插入的订单
        Order currentOrder = orderMapper.selectByPrimaryKey(orderId);
        System.out.println("------->"+orderId);
        if (row > 0){
            // 将订单放入Redis中
            redisTemplate.opsForValue().set("orderId:"+orderId,currentOrder);
            // 设置订单有效时间10分钟
            redisTemplate.expire("orderId:"+orderId,10, TimeUnit.MINUTES);
        }else {
            throw new OrderException("订单创建失败!");
        }
    }

    // 付款
    @Override
    public String payMoney(Integer orderId) throws AlipayApiException {
        orderId = 56;
        String result = null;
        // 从Redis中根据订单id取出订单信息
        Order redisOrder = (Order) redisTemplate.opsForValue().get("orderId:"+orderId);
        // 根据订单创建时间,判断订单是否过期
        Date createOrderTime = redisOrder.getCreateTime();
        // 付款时间
        Date payMoneyTime = new Date();
        // 订单过期
        if (payMoneyTime.getTime() - createOrderTime.getTime() > (10*60*1000)){
            // 根据订单Id将该订单改为无效订单(状态为6)
            orderMapper.updateOrderStateByOrderId(SystemConstant.ORDER_DISABLE_STATE,orderId);
            result = "订单已过期,请重新下单";
            // 从Redis中删除该条订单信息
            redisTemplate.delete("orderId:"+orderId);
            return result;
        }else { // 订单未过期,付款
            // 如果用户选择了代金券,则计算实际金额
            if(redisOrder.getCouponId() != null) {
                // 根据代金券id查询出代金券对象
                Coupon coupon = orderMapper.selectCouponByCouponId(redisOrder.getCouponId());
                if (new Date().getTime() > coupon.getActiveTime().getTime()){
                    // 将代金券状态修改为2(已过期)
                    int row = orderMapper.updateCouponStateByCouponId(SystemConstant.COUPON_OUT_DATE_STATE,coupon.getCouponId());
                    if (row <= 0){
                        throw new OrderException("代金券状态修改失败!");
                    }
                    // 修改当前订单状态为无效6
                    orderMapper.modifyOrderState(orderId,SystemConstant.ORDER_DISABLE_STATE);
                    result = "请重新下单,在有效时间内使用代金券";
                    return result;
                }
                // 代金券状态不为0,不可以使用
                if (coupon.getState() != SystemConstant.COUPON_CAN_USE_STATE){
                    result = "代金券不可用,请重新下单";
                    // 将当前订单修改为无效状态
                    int row = orderMapper.modifyOrderState(orderId,SystemConstant.ORDER_DISABLE_STATE);
                    if (row <= 0){
                        throw new OrderException("订单状态修改为无效失败!");
                    }
                    return result;
                }
                // 代金券状态为0,可以使用
                // 根据订单中代金券Id,查询出代金券金额
                Double couponPrice = orderMapper.selectCouponPriceById(redisOrder.getCouponId());
                // 计算实际付款金额
                Double realTotlPrice = redisOrder.getTotalPrice().doubleValue() - couponPrice;
                // 更新订单信息
                int row = orderMapper.updateOrderByOrderId(orderId,realTotlPrice);
                if (row > 0) {
                    // 付款
                    result = alipayUtil.alipay(redisOrder.getOrderNum(), "电影票", realTotlPrice.toString(), "没有描述");
                    return result;
                }else {
                    result = "付款失败!";
                    return result;
                }
            }else {
                // 用户没有选择使用代金券
                // 付款
                result = alipayUtil.alipay(redisOrder.getOrderNum(), "电影票",redisOrder.getTotalPrice().toString(), "没有描述");
                return result;
            }
        }
    }

    // 付款成功,修改订单信息
    @Override
    public Integer modifyOrder(String out_trade_no) {
        // 通过订单编号查询订单对象
        Order order = orderMapper.selectOrderStateByOrderNum(out_trade_no);
        // 生成二维码(存放订单编号信息)
        // 存放在二维码中的内容
        String text = order.getOrderNum();
        // 嵌入二维码的图片路径
        String imgPath = null;
        // 生成的二维码的路径及名称
        String qrCode = "F:/QRCode/"+order.getOrderNum()+".jpg";
        try {
            //生成二维码
            QRCodeUtil.encode(text, imgPath, qrCode, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 根据订单Id更新二维码信息
        int row = orderMapper.updateOrderQRCodeByOrderId(order.getOrderId(),qrCode);
        if (row <= 0){
            throw new OrderException("订单二维码修改失败!");
        }
        // 根据代金券id查询出代金券对象
        Coupon coupon = orderMapper.selectCouponByCouponId(order.getCouponId());
        // 更改代金券状态(已使用)
        int couponStateRow = orderMapper.updateCouponStateByCouponId(SystemConstant.COUPON_IS_USE_STATE, coupon.getCouponId());
        System.out.println(couponStateRow+"======");
        if (couponStateRow <= 0) {
            throw new OrderException("代金券状态修改失败!");
        }
        return null;
    }


    // 通过订单编号,更新订单状态
    @Override
    public Integer updateOrderInfo(Order order) {
        int row = orderMapper.updateOrderInfo(order);
        if (row <= 0){
             throw new OrderException("订单状态更新失败!");
        }
        return  null;
    }
    // 根据订单编号查询订单id
    @Override
    public Integer selectOrderIdByOrderNum(String orderNum) {
        return orderMapper.selectOrderIdByOrderNum(orderNum);
    }

    // 根据订单编号查询订单状态
    @Override
    public Order selectOrderStateByOrderNum(String orderNum) {
        return orderMapper.selectOrderStateByOrderNum(orderNum);
    }

    // 通过订单编号修改订单状态(4:已退款)
    @Override
    public Integer updateOrderStateByOrderId(Integer orderRefundMoneyState, String orderNum) {
        int row = orderMapper.updateOrderStateByOrderNum(orderRefundMoneyState,orderNum);
        if (row <= 0){
            throw new OrderException("退款订单状态修改失败!");
        }
        return null;
    }

    // 根据代金券id查询代金券对象
    @Override
    public Coupon selectCouponByCouponId(Integer couponId) {
        return orderMapper.selectCouponByCouponId(couponId);
    }

    // 修改退款代金券状态
    @Override
    public Integer updateCouponStateByCouponId(Integer couponCanUseState, Integer couponId) {
        int row = orderMapper.updateCouponStateByCouponId(couponCanUseState,couponId);
        if (row <= 0){
            throw new OrderException("退款订单代金券状态修改失败!");
        }
        return null;
    }

}
