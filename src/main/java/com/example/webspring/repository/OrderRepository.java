package com.example.webspring.repository;
import com.example.webspring.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderRepository extends JpaRepository<Order, Long> {}

