package com.example.webspring.repository;
import com.example.webspring.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Tìm kiếm sản phẩm theo tên (không phân biệt chữ hoa chữ thường)
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}

