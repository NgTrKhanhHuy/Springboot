// ForgotPasswordController.java
package com.example.webspring.controller;

import com.example.webspring.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ForgotPasswordController {

    @Autowired
    private PasswordResetService passwordResetService;

    // GET: Hiển thị form "Quên mật khẩu"
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password"; // View: forgot-password.html
    }

    // POST: Xử lý gửi yêu cầu quên mật khẩu
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email,
                                        RedirectAttributes redirectAttributes) {
        passwordResetService.createPasswordResetToken(email);
        redirectAttributes.addFlashAttribute("message", "Nếu email tồn tại, bạn sẽ nhận được email đặt lại mật khẩu.");
        return "redirect:/forgot-password";
    }

    // GET: Hiển thị form đặt lại mật khẩu
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        boolean valid = passwordResetService.validatePasswordResetToken(token);
        if (!valid) {
            redirectAttributes.addFlashAttribute("error", "Token không hợp lệ hoặc đã hết hạn.");
            return "redirect:/forgot-password";
        }
        model.addAttribute("token", token);
        return "reset-password"; // View: reset-password.html
    }

    // POST: Xử lý cập nhật mật khẩu mới
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("newPassword") String newPassword,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không khớp.");
            redirectAttributes.addFlashAttribute("token", token);
            return "redirect:/reset-password?token=" + token;
        }
        passwordResetService.updatePassword(token, newPassword);
        redirectAttributes.addFlashAttribute("message", "Mật khẩu đã được thay đổi thành công. Bạn có thể đăng nhập lại.");
        return "redirect:/login";
    }
}
