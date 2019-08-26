package com.woniu.woniuticket.order.constant;

public interface SystemConstant {


    // 订单状态
    // 1:未付款状态
    public static final Integer ORDER_NO_PAY_STATE = 1;

    // 2:已付款未观影状态
    public static final Integer ORDER_NO_WATCH_STATE = 2;

    // 3:已观影状态
    public static final Integer ORDER_WATCHED_STATE = 3;

    // 4:已退款状态
    public static final Integer ORDER_REFUND_MONEY_STATE = 4;

    // 5:未评价状态
    public static final Integer ORDER_NO_EVALUATE_STATE = 5;

    // 6:无效状态
    public static final Integer ORDER_DISABLE_STATE = 6;

    // 代金券状态
    // 0:可以使用
    public static final Integer COUPON_CAN_USE_STATE = 0;

    // 1:已使用
    public static final Integer COUPON_IS_USE_STATE = 1;

    // 2:已过期
    public static final Integer COUPON_OUT_DATE_STATE = 2;

    // 3:已删除
    public static final Integer COUPON_IS_DELETE_STATE = 3;
}
