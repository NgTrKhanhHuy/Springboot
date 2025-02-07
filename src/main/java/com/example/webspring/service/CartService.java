package com.example.webspring.service;
import com.example.webspring.entity.*;
import com.example.webspring.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

//    public Cart getCartByUser(User user) {
//        return cartRepository.findById(user.getId()).orElse(null);
//    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }
}
