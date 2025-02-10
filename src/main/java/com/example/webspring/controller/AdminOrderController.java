package com.example.webspring.controller;

import com.example.webspring.dto.AdminOrderSummary;
import com.example.webspring.entity.Order;
import com.example.webspring.entity.Payment;
import com.example.webspring.repository.OrderRepository;
import com.example.webspring.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminOrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/admin/orders")
    public String listOrders(Model model, RedirectAttributes redirectAttributes) {
        // Lấy tất cả các đơn hàng
        List<Order> orders = orderRepository.findAll();
        if (orders == null || orders.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không có đơn hàng nào!");
            return "redirect:/admin/orders";  // Redirect lại trang đơn hàng để hiển thị flash message
        }

        List<AdminOrderSummary> summaries = new ArrayList<>();
        for (Order order : orders) {
            Payment payment = paymentRepository.findByOrder(order);
            String paymentStatus = (payment != null) ? payment.getPaymentStatus() : "N/A";
            summaries.add(new AdminOrderSummary(
                    order.getId(),
                    order.getUser().getEmail(),
                    order.getTotalAmount(),
                    order.getStatus(),
                    paymentStatus
            ));
        }
        model.addAttribute("orderSummaries", summaries);
        return "admin-orders";  // View: admin-orders.html
    }
    /**
     * POST /admin/orders/confirm/{orderId}
     * Xác nhận đơn hàng: Chỉ cho phép nếu đơn hàng đang ở trạng thái "chờ xác nhận"
     */
    @PostMapping("/admin/orders/confirm/{orderId}")
    public String confirmOrder(@PathVariable("orderId") Long orderId,
                               RedirectAttributes redirectAttributes) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng không tồn tại!");
            return "redirect:/admin/orders";
        }
        // Chỉ xác nhận đơn hàng nếu trạng thái là "chờ xác nhận"
        if (!"chờ xác nhận".equalsIgnoreCase(order.getStatus())) {
            redirectAttributes.addFlashAttribute("error", "Chỉ có đơn hàng chờ xác nhận mới được xác nhận!");
            return "redirect:/admin/orders";
        }
        order.setStatus("đang giao");
        orderRepository.save(order);
        redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được xác nhận và đang giao!");
        return "redirect:/admin/orders";
    }

    /**
     * POST /admin/orders/cancel/{orderId}
     * Hủy đơn hàng: Nếu đơn hàng có trạng thái "chờ xác nhận" mới cho phép hủy.
     * Nếu đơn hàng đang giao, không cho phép hủy.
     */
    @PostMapping("/admin/orders/cancel/{orderId}")
    public String cancelOrder(@PathVariable("orderId") Long orderId,
                              RedirectAttributes redirectAttributes) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng không tồn tại!");
            return "redirect:/admin/orders";
        }
        // Nếu đơn hàng đang giao, không cho phép hủy
        if ("đang giao".equalsIgnoreCase(order.getStatus())) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng đang giao, không thể hủy!");
            return "redirect:/admin/orders";
        }
        // Cập nhật trạng thái đơn hàng thành "đã hủy"
        order.setStatus("đã hủy");
        orderRepository.save(order);
        // Nếu có Payment liên quan, cập nhật trạng thái thanh toán
        Payment payment = paymentRepository.findByOrder(order);
        if (payment != null) {
            payment.setPaymentStatus("Đã hủy");
            paymentRepository.save(payment);
        }
        redirectAttributes.addFlashAttribute("success", "Đơn hàng đã bị hủy!");
        return "redirect:/admin/orders";
    }
}
