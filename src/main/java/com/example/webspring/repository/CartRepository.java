package com.example.webspring.repository;
import com.example.webspring.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);

}

