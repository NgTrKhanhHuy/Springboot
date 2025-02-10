package com.example.webspring.service;

import com.example.webspring.entity.User;
import com.example.webspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;  // Giả sử bạn đã có UserRepository

    public User getUserByEmail(String email) {
        // Tìm kiếm người dùng trong cơ sở dữ liệu dựa trên email
        return userRepository.findByEmail(email);
    }
}

