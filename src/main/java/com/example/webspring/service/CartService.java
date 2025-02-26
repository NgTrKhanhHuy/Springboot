package com.example.webspring.service;

import com.example.webspring.entity.Cart;
import com.example.webspring.entity.CartItem;
import com.example.webspring.entity.Product;
import com.example.webspring.entity.User;
import com.example.webspring.repository.CartItemRepository;
import com.example.webspring.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // Lấy giỏ hàng của người dùng, nếu chưa có thì tạo mới
    public Cart getCartByUser(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }
        return cart;
    }

    // Thêm sản phẩm vào giỏ hàng với số lượng mặc định
    @Async
    public void addToCart(User user, Product product, int quantity) {
        Cart cart = getCartByUser(user);
        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }
        cartItemRepository.save(cartItem);
    }

    // Tính tổng số lượng sản phẩm trong giỏ
    public int getCartItemCount(User user) {
        Cart cart = getCartByUser(user);
        List<CartItem> items = cart.getCartItems();
        int count = 0;
        if (items != null) {
            for (CartItem item : items) {
                count += item.getQuantity();
            }
        }
        return count;
    }
    public List<CartItem> getCartItems(User user) {
        Cart cart = getCartByUser(user);
        if (cart != null && cart.getCartItems() != null) {
            return cart.getCartItems();
        }
        return new ArrayList<>();
    }

}
