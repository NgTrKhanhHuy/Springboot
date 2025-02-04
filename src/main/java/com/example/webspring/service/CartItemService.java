package com.example.webspring.service;
import com.example.webspring.entity.*;
import com.example.webspring.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartItem> getCartItemsByCart(Cart cart) {
        return cartItemRepository.findAll();
    }

    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }
}
