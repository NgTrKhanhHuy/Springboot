package com.example.webspring.controller;

import com.example.webspring.config.CustomUserDetails;
import com.example.webspring.entity.CartItem;
import com.example.webspring.entity.Order;
import com.example.webspring.entity.OrderItem;
import com.example.webspring.entity.Payment;
import com.example.webspring.entity.Product;
import com.example.webspring.entity.User;
import com.example.webspring.repository.OrderItemRepository;
import com.example.webspring.repository.OrderRepository;
import com.example.webspring.repository.PaymentRepository;
import com.example.webspring.repository.UserRepository;
import com.example.webspring.service.CartItemService;
import com.example.webspring.service.CartService;
import com.example.webspring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CheckOutController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        User currentUser = userRepository.findById(userId).orElse(null);
        if(currentUser == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }
        List<CartItem> cartItems = cartService.getCartItems(currentUser);
        model.addAttribute("cartItems", cartItems);

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            BigDecimal itemTotal = item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
        }
        model.addAttribute("totalPrice", totalPrice);

        return "checkout"; // View: checkout.html
    }

    @PostMapping("/checkout")
    public String processCheckout(@RequestParam("paymentMethod") String paymentMethod,
                                  Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        User currentUser = userRepository.findById(userId).orElse(null);
        if(currentUser == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }
        List<CartItem> cartItems = cartService.getCartItems(currentUser);
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            BigDecimal itemTotal = item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
        }

        // Tạo Order
        Order order = new Order();
        order.setUser(currentUser);
        order.setStatus("Đang giao"); // Đơn hàng đang được giao
        order.setTotalAmount(totalPrice);
        order = orderRepository.save(order);

        // Tạo OrderItem cho mỗi CartItem
        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            // Giá của OrderItem là giá sản phẩm * số lượng
            BigDecimal itemPrice = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            orderItem.setPrice(itemPrice);
            orderItemRepository.save(orderItem);
        }

        // Tạo Payment dựa trên lựa chọn của khách hàng
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod); // Giá trị từ radio button ("COD", "Tiền mặt", "Chuyển khoản", ...)
        payment.setPaymentStatus("Chưa thanh toán");
        paymentRepository.save(payment);

        // Xóa giỏ hàng sau khi thanh toán thành công
        cartItemService.clearCart(currentUser);

        // Chuyển hướng đến trang /orders (sẽ triển khai chức năng quản lý đơn hàng sau)
        return "redirect:/orders";
    }
}
