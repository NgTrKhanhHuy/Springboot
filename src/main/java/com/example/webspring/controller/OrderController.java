package com.example.webspring.controller;

import com.example.webspring.config.CustomUserDetails;
import com.example.webspring.dto.OrderSummary;
import com.example.webspring.entity.Order;
import com.example.webspring.entity.OrderItem;
import com.example.webspring.entity.Payment;
import com.example.webspring.entity.User;
import com.example.webspring.repository.OrderItemRepository;
import com.example.webspring.repository.OrderRepository;
import com.example.webspring.repository.PaymentRepository;
import com.example.webspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * GET /orders: Hiển thị danh sách đơn hàng của người dùng với thông tin khái quát
     */
    @GetMapping("/orders")
    public String listOrders(Model model, Authentication authentication) {
        // Lấy thông tin user từ Authentication
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }

        // Lấy danh sách Order của người dùng
        List<Order> orders = orderRepository.findByUser(currentUser);
        List<OrderSummary> orderSummaries = new ArrayList<>();

        // Với mỗi Order, lấy Payment tương ứng để hiển thị trạng thái thanh toán
        for (Order order : orders) {
            Payment payment = paymentRepository.findByOrder(order);
            String paymentStatus = (payment != null) ? payment.getPaymentStatus() : "N/A";
            orderSummaries.add(new OrderSummary(
                    order.getId(),
                    order.getTotalAmount(),
                    order.getStatus(),
                    paymentStatus
            ));
        }

        model.addAttribute("orderSummaries", orderSummaries);
        return "orders"; // View: orders.html
    }
    /**
     * GET /orders/{orderId} - Xem chi tiết đơn hàng
     */
    @GetMapping("/orders/{orderId}")
    public String viewOrderDetail(@PathVariable("orderId") Long orderId,
                                  Model model,
                                  Authentication authentication) {
        // Lấy thông tin user từ Authentication
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }

        // Lấy đơn hàng theo orderId
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || !order.getUser().getId().equals(currentUser.getId())) {
            model.addAttribute("error", "Đơn hàng không tồn tại hoặc không thuộc về bạn");
            return "redirect:/orders";
        }

        // Lấy danh sách OrderItem cho đơn hàng này
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

        // Lấy thông tin Payment cho đơn hàng này
        Payment payment = paymentRepository.findByOrder(order);

        // Đưa thông tin vào model
        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("payment", payment);

        return "order-detail"; // View: order-detail.html
    }
    /**
     * POST /orders/cancel/{orderId} - Hủy đơn hàng
     * Chỉ cho phép hủy đơn nếu trạng thái hiện tại không phải "đang giao"
     */
    @PostMapping("/orders/cancel/{orderId}")
    public String cancelOrder(@PathVariable("orderId") Long orderId,
                              Model model, RedirectAttributes redirectAttributes,
                              Authentication authentication) {
        // Lấy thông tin người dùng từ Authentication
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }

        // Tìm đơn hàng theo orderId
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || !order.getUser().getId().equals(currentUser.getId())) {
            model.addAttribute("error", "Đơn hàng không tồn tại hoặc không thuộc về bạn");
            return "redirect:/orders";
        }

        // Kiểm tra trạng thái đơn hàng: nếu là "đang giao", không cho phép hủy
        if ("đang giao".equalsIgnoreCase(order.getStatus())) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng đang giao, không thể hủy!");
            return "redirect:/orders";
        }

        // Nếu trạng thái là "chờ xác nhận" (hoặc trạng thái khác cho phép hủy), cập nhật trạng thái thành "đã hủy"
        order.setStatus("đã hủy");
        orderRepository.save(order);

        // Nếu có Payment liên quan, cập nhật trạng thái thanh toán thành "Đã hủy"
        Payment payment = paymentRepository.findByOrder(order);
        if (payment != null) {
            payment.setPaymentStatus("Đã hủy");
            paymentRepository.save(payment);
        }

        return "redirect:/orders";
    }


}
