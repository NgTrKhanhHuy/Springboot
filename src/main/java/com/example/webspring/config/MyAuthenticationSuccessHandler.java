package com.example.webspring.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        // In thông tin người dùng ra console
        System.out.println("=== Đăng nhập thành công ===");
        System.out.println("Username (email): " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());

        // Nếu muốn in chi tiết hơn:
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User userDetails = (User) principal;
            System.out.println("User details: " + userDetails);
        }

        // Chuyển hướng theo vai trò: nếu có role ADMIN -> /admin-home, ngược lại -> /home
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            response.sendRedirect("/admin-home");
        } else {
            response.sendRedirect("/home");
        }
    }
}
