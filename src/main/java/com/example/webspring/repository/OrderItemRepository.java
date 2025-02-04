package com.example.webspring.repository;
import com.example.webspring.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}

