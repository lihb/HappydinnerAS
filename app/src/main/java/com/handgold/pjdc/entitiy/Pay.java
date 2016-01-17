package com.handgold.pjdc.entitiy;

/**
 * 支付类
 * Created by lihb on 15/5/13.
 */
public class Pay  extends BaseEntity {

    // 支付的订单号
    private String orderId;

    // 支付价格
    private float totalPrice;

    // 支付类型，支付宝、微信等
    private int payType;

    // 团购的优惠码
    private String discountCode;

    // 支付时间
    private String payTime;

    public Pay(String orderId) {
        this.orderId = orderId;
    }

    public Pay(int payType, String discountCode, float totalPrice, String orderId) {
        this.payType = payType;
        this.discountCode = discountCode;
        this.totalPrice = totalPrice;
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }
}
