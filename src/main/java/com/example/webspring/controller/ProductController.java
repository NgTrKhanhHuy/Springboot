package com.example.webspring.controller;

import com.example.webspring.entity.Product;
import com.example.webspring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    // Trang chủ hiển thị danh sách sản phẩm với phân trang và tìm kiếm
    @GetMapping({ "/products"})
    public String productsPage(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String keyword) {
        Page<Product> productPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            productPage = productService.searchProducts(keyword, PageRequest.of(page, size));
            model.addAttribute("keyword", keyword);
        } else {
            productPage = productService.findPaginatedProducts(PageRequest.of(page, size));
        }
        model.addAttribute("productPage", productPage);
        return "products"; // View: home.html
    }

    // Trang chi tiết sản phẩm
    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model){
        Product product = productService.findById(id);
        if(product == null){
            model.addAttribute("error", "Sản phẩm không tồn tại!");
            return "redirect:/home";
        }
        model.addAttribute("product", product);
        return "product-detail"; // View: product-detail.html
    }
}
