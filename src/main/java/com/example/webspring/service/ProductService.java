package com.example.webspring.service;

import com.example.webspring.entity.Product;
import com.example.webspring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Phương thức phân trang sản phẩm
    public Page<Product> findPaginatedProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

}
