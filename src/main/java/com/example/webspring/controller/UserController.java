package com.example.webspring.controller;

import com.example.webspring.config.CustomUserDetails;
import com.example.webspring.entity.User;
import com.example.webspring.repository.UserRepository;
import com.example.webspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserService userService;  // Đã có hàm getCurrentUserDetails()

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user")
    public String viewUserProfile(Model model, Authentication authentication) {
        // Lấy thông tin người dùng hiện tại từ SecurityContext thông qua UserService
        CustomUserDetails userDetails = userService.getCurrentUserDetails();
        Long userId = userDetails.getId();
        // Lấy đối tượng User đầy đủ từ DB
        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }
        // Ẩn mật khẩu trước khi hiển thị (đặt thành "******")
        currentUser.setPassword("******");
        model.addAttribute("user", currentUser);
        return "user"; // View: user.html
    }
    // --- Chức năng đổi mật khẩu ---

    // Bước 1: Hiển thị form nhập mật khẩu hiện tại
    @GetMapping("/user/change-password")
    public String showChangePasswordForm(Model model) {
        return "change-password"; // View: change-password.html
    }

    // Bước 1: Xác nhận mật khẩu hiện tại
    @PostMapping("/user/change-password")
    public String verifyCurrentPassword(@RequestParam("currentPassword") String currentPassword,
                                        RedirectAttributes redirectAttributes) {
        CustomUserDetails userDetails = userService.getCurrentUserDetails();
        // Kiểm tra mật khẩu hiện tại: Lưu ý: userDetails.getPassword() chứa mật khẩu đã mã hóa
        if (!passwordEncoder.matches(currentPassword, userDetails.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng!");
            return "redirect:/user/change-password";
        }
        // Nếu mật khẩu đúng, chuyển sang trang cập nhật mật khẩu mới
        return "redirect:/user/update-password";
    }

    // Bước 2: Hiển thị form nhập mật khẩu mới
    @GetMapping("/user/update-password")
    public String showUpdatePasswordForm() {
        return "update-password"; // View: update-password.html
    }

    // Bước 2: Xử lý cập nhật mật khẩu mới
    @PostMapping("/user/update-password")
    public String updatePassword(@RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không khớp!");
            return "redirect:/user/update-password";
        }

        CustomUserDetails userDetails = userService.getCurrentUserDetails();
        Long userId = userDetails.getId();
        User currentUser = userRepository.findById(userId).orElse(null);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng!");
            return "redirect:/login";
        }

        // Mã hóa mật khẩu mới và cập nhật cho người dùng
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);

        redirectAttributes.addFlashAttribute("success", "Mật khẩu đã được thay đổi thành công!");
        return "redirect:/user";
    }
}