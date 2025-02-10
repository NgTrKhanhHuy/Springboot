package com.example.webspring.controller;

import com.example.webspring.entity.CartItem;
import com.example.webspring.entity.Product;
import com.example.webspring.entity.User;
import com.example.webspring.config.CustomUserDetails;
import com.example.webspring.repository.UserRepository;
import com.example.webspring.service.CartItemService;
import com.example.webspring.service.CartService;
import com.example.webspring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    // Hiển thị trang giỏ hàng
    @GetMapping("/cart")
    public String showCart(Model model, Authentication authentication) {
        // Ép kiểu principal sang CustomUserDetails để lấy đối tượng User đầy đủ
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        System.out.println("User ID: " + userId);
//        System.out.println("User ID: " + currentUser.getId()); // Kiểm tra user đã có id hay chưa
// Lấy đối tượng User đầy đủ từ DB dựa trên id
        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }
        // Lấy số lượng sản phẩm trong giỏ hàng của người dùng
        int itemCount = cartService.getCartItemCount(currentUser);
        model.addAttribute("itemCount", itemCount);


        // Nếu cần load chi tiết giỏ hàng, bạn cũng có thể thêm vào model (ví dụ: danh sách CartItem)
        List<CartItem> cartItems = cartService.getCartItems(currentUser);

        model.addAttribute("cartItems", cartItems);

        // Tính tổng số tiền của giỏ hàng (sử dụng BigDecimal để tính toán chính xác)
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            BigDecimal itemTotal = item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
        }
        System.out.println("Total Price: " + totalPrice);

        model.addAttribute("totalPrice", totalPrice);

        return "cart"; // View: cart.html
    }

    // Thêm sản phẩm vào giỏ hàng (số lượng mặc định = 1)
    @PostMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable("productId") Long productId,
                            Authentication authentication,
                            Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId= userDetails.getId();
        // Lấy User đầy đủ từ DB
        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }
        // Lấy sản phẩm cần thêm từ DB
        Product product = productService.findById(productId);
        if (product == null) {
            model.addAttribute("error", "Sản phẩm không tồn tại!");
            return "redirect:/products";
        }
        cartService.addToCart(currentUser, product, 1);
        return "redirect:/cart";
    }

    // Cập nhật số lượng cho sản phẩm trong giỏ hàng
    @PostMapping("/cart/update/{productId}")
    public String updateCartItem(@PathVariable("productId") Long productId,
                                 @RequestParam("quantity") int quantity,
                                 Authentication authentication,
                                 Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }
        Product product = productService.findById(productId);
        if (product == null) {
            model.addAttribute("error", "Sản phẩm không tồn tại!");
            return "redirect:/cart";
        }
        // Gọi phương thức cập nhật số lượng trong CartItemService
        cartItemService.updateCartItemQuantity(currentUser, product, quantity);
        return "redirect:/cart";
    }
    @PostMapping("/cart/delete/{productId}")
    public String deleteCartItem(@PathVariable("productId") Long productId,
                                 Authentication authentication,
                                 Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }
        Product product = productService.findById(productId);
        if (product == null) {
            model.addAttribute("error", "Sản phẩm không tồn tại!");
            return "redirect:/cart";
        }
        cartItemService.removeCartItem(currentUser, product);
        return "redirect:/cart";
    }
}
