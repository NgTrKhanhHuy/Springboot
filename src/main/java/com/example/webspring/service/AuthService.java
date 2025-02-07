package com.example.webspring.service;

// AuthService.java

import com.example.webspring.dto.RegisterDTO;
import com.example.webspring.entity.Role;
import com.example.webspring.entity.User;
import com.example.webspring.repository.RoleRepository;
import com.example.webspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterDTO registerDTO) throws Exception {
        // Kiểm tra password và confirmPassword
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new Exception("Passwords do not match");
        }
        // Kiểm tra xem email đã tồn tại chưa
        if (userRepository.findByEmail(registerDTO.getEmail()) != null) {
            throw new Exception("Email already exists");
        }

        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        // Gán vai trò mặc định là ROLE_CUSTOMER
        Role role = roleRepository.findByName("ROLE_CUSTOMER");
        if (role == null) {
            // Nếu chưa có, tạo mới (chỉ nên làm trong quá trình khởi tạo dữ liệu mẫu)
            role = new Role();
            role.setName("ROLE_CUSTOMER");
            role = roleRepository.save(role);
        }
        user.setRoles(Collections.singletonList(role));

        return userRepository.save(user);
    }
}
