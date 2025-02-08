package com.example.webspring.service;

import com.example.webspring.config.CustomUserDetails;
import com.example.webspring.entity.User;
import com.example.webspring.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.stream.Collectors;
import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            System.out.println("CustomUserDetailsService: User not found with email: " + email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        System.out.println("CustomUserDetailsService: Found user: " + user);

//        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());

        return new CustomUserDetails(
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                getAuthorities(user));

    }
    private List<GrantedAuthority> getAuthorities(User user) {
        // Bạn có thể lấy quyền (roles) từ user và chuyển nó thành quyền (authority)
        return AuthorityUtils.createAuthorityList(user.getRoles().stream()
                .map(role -> role.getName())
                .toArray(String[]::new));
    }
}
