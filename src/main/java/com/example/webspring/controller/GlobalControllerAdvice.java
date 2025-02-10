package com.example.webspring.controller;

import com.example.webspring.config.CustomUserDetails;
import com.example.webspring.entity.User;
import com.example.webspring.repository.UserRepository;
import com.example.webspring.service.CartService;
import com.example.webspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    // Thêm attribute "cartItemCount" cho mọi view
    @ModelAttribute("cartItemCount")
    public int addCartItemCount(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();
            User currentUser = userRepository.findById(userId).orElse(null);
            if (currentUser != null) {
                return cartService.getCartItemCount(currentUser);
            }
        }
        return 0;
    }

}
