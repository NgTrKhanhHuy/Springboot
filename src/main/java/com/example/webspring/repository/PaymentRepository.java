package com.example.webspring.repository;

import com.example.webspring.entity.Order;
import com.example.webspring.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrder(Order order);
}
