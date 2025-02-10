package com.example.webspring.repository;

import com.example.webspring.entity.Order;
import com.example.webspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
