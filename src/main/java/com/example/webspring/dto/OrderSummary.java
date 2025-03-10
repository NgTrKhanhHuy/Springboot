package com.example.webspring.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
public class OrderSummary {
    private Long orderId;
    private BigDecimal totalAmount;
    private String orderStatus;
    private String paymentStatus;

    public OrderSummary() {
    }

    public OrderSummary(Long orderId, BigDecimal totalAmount, String orderStatus, String paymentStatus) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
