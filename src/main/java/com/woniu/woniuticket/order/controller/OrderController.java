package com.woniu.woniuticket.order.controller;

import com.github.pagehelper.PageInfo;
import com.woniu.woniuticket.order.pojo.Condition;
import com.woniu.woniuticket.order.pojo.Order;
import com.woniu.woniuticket.order.service.OrderService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// 允许跨域访问
@CrossOrigin
@RequestMapping("/order")
//@Api(tags = "订单管理接口")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @ApiOperation(value = "订单",notes = "按条件分页展示所有订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "当前页数"),
            @ApiImplicitParam(name = "pageSize",value = "每页总条数"),
            @ApiImplicitParam(name = "condition",value = "查询条件")
    })
    @GetMapping("/queryAllOrders")
    public Object queryAllOrders(@RequestParam(value = "currentPage",defaultValue = "1",required = false) Integer currentPage,
                                 @RequestParam(value = "pageSize",defaultValue = "10",required = false) Integer pageSize,
                                 @RequestParam(value = "condition",required = false) Condition condition){
        List<Order> orders = orderService.selectAllOrders(currentPage,pageSize,condition);
        PageInfo<Order> pageInfo = new PageInfo<>(orders);
        return pageInfo;
    }

    // 支付测试
//    @Autowired
//    private AlipayUtil alipayUtil;
//
//    @RequestMapping("/alipayMoney")
//    public String payMoney(Order order) throws AlipayApiException {
//        Order orders = new Order();
//        orders.setOrderId(1);
//        orders.setOrderNum("123321");
//        orders.setTotalPrice(50L);
//        String result = alipayUtil.alipay(orders.getOrderNum(), "电影票", orders.getTotalPrice().toString(), "没有描述");
//        return result;
//    }
//
//    @RequestMapping(value = "/save")
//    public void getResponseInfo(HttpServletRequest request, String out_trade_no, String trade_no, String trade_status) throws AlipayApiException {
//        //获取支付宝POST过来反馈信息
//        Map<String, String> params = new HashMap<String, String>();
//        Map<String, String[]> requestParams = request.getParameterMap();
//        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
//            String name = (String) iter.next();
//            String[] values = (String[]) requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i]
//                        : valueStr + values[i] + ",";
//            }
//            params.put(name, valueStr);
//        }
//        //调用SDK验证签名
//        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
//        if (signVerified) {//验证成功
//            if (trade_status.equals("TRADE_SUCCESS")) {
//                /**
//                 *  如果交易完成，更新订单流水号，插入付款时间
//                 */
//                Order orders = new Order();
//                orders.setOrderNum(out_trade_no);
//                //插入流水号
//                orders.setPipeNum(trade_no);
//                // 修改付款状态(2:表示已付款)
//                orders.setOrderState(2);
//                orderService.updatePayInfor(orders);
//                //注意：
//                //付款完成后，支付宝系统发送该交易状态通知
//            }
//        }
//    }
}