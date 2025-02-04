package com.example.webspring.service;
import com.example.webspring.entity.*;
import com.example.webspring.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> getOrderItemsByOrder(Order order) {
        return orderItemRepository.findAll();
    }

    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
}