package com.atguigu.gmall.order;

/**
 * 订单状态：给用户看的
 */
public enum OrderStatus {

    UNPAID("未支付"),
    PAID("已支付" ),
    WAITING_DELEVER("待发货"),
    DELEVERED("已发货"),
    CLOSED("已关闭"), //30分不支付订单就是关闭；
    FINISHED("已完结") , //
    SPLIT("订单已拆分"); //支付完成以后库存系统自动拆分订单

    private String comment ;


    OrderStatus(String comment ){
        this.comment=comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
