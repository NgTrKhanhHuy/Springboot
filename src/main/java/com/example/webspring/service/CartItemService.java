package com.example.webspring.service;

import com.example.webspring.entity.Cart;
import com.example.webspring.entity.CartItem;
import com.example.webspring.entity.Product;
import com.example.webspring.entity.User;
import com.example.webspring.repository.CartItemRepository;
import com.example.webspring.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    /**
     * Lấy danh sách các mục (CartItem) của giỏ hàng của người dùng.
     * Nếu giỏ hàng chưa tồn tại, tạo mới và trả về danh sách rỗng.
     */
    public List<CartItem> getCartItemsByUser(User user) {
        Cart cart = getOrCreateCart(user);
        return cart.getCartItems();
    }

    /**
     * Cập nhật số lượng của một CartItem cho sản phẩm cụ thể của người dùng.
     * Nếu sản phẩm chưa có trong giỏ hàng, trả về null (hoặc có thể ném ngoại lệ).
     */
    public CartItem updateCartItemQuantity(User user, Product product, int newQuantity) {
        Cart cart = getOrCreateCart(user);
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (cartItem != null) {
            cartItem.setQuantity(newQuantity);
            return cartItemRepository.save(cartItem);
        }
        return null;
    }

    /**
     * Xóa một CartItem khỏi giỏ hàng của người dùng dựa trên sản phẩm.
     */
    public void removeCartItem(User user, Product product) {
        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (cartItem != null) {
            // Loại bỏ CartItem khỏi danh sách trong Cart
            cart.getCartItems().remove(cartItem);
            // Xóa CartItem khỏi DB
            cartItemRepository.delete(cartItem);
            // Cập nhật lại Cart nếu cần thiết (để orphanRemoval hoạt động)
            cartRepository.save(cart);        }
    }

    /**
     * Xóa toàn bộ các mục trong giỏ hàng của người dùng.
     */
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        if (cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
            cartItemRepository.deleteAll(cart.getCartItems());
        }
    }

    /**
     * Phương thức trợ giúp: lấy giỏ hàng của người dùng.
     * Nếu chưa có giỏ hàng, tạo mới và lưu vào CSDL.
     */
    private Cart getOrCreateCart(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }
        return cart;
    }
}
