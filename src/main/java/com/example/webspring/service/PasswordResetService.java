// PasswordResetService.java
package com.example.webspring.service;

import com.example.webspring.entity.PasswordResetToken;
import com.example.webspring.entity.User;
import com.example.webspring.repository.PasswordResetTokenRepository;
import com.example.webspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private static final long EXPIRATION_MINUTES = 60; // Token có hiệu lực 60 phút

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES));
            tokenRepository.save(resetToken);
            // Gửi email reset mật khẩu bất đồng bộ
            emailService.sendResetPasswordEmail(email, token);
        }
        // Để bảo mật, nếu user không tồn tại, không thông báo gì cả
    }

    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null) {
            return false;
        }
        return resetToken.getExpiryDate().isAfter(LocalDateTime.now());
    }

    public void updatePassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken != null && resetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            User user = resetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            tokenRepository.delete(resetToken);
        }
    }
}
