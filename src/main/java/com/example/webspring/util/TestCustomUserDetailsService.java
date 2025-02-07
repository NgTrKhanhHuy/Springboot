package com.example.webspring.util;

import com.example.webspring.entity.Role;
import com.example.webspring.entity.User;
import com.example.webspring.repository.UserRepository;
import com.example.webspring.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestCustomUserDetailsService {
    public static void main(String[] args) {
        // Khởi tạo PasswordEncoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Tạo user mẫu với mật khẩu đã mã hóa
        String rawPassword = "password123";
        String encodedPassword = encoder.encode(rawPassword);

        User dummyUser = new User();
        dummyUser.setId(1L);
        dummyUser.setEmail("test@example.com");
        dummyUser.setUsername("testuser");
        dummyUser.setPassword(encodedPassword);

        // Giả sử user có role ROLE_CUSTOMER
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_CUSTOMER");
        dummyUser.setRoles(Collections.singletonList(role));

        // Tạo một dummy implementation của UserRepository chỉ hỗ trợ findByEmail
        UserRepository dummyUserRepo = new UserRepository() {
            @Override
            public User findByEmail(String email) {
                if(email.equals(dummyUser.getEmail())) {
                    return dummyUser;
                }
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends User> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
                return null;
            }

            @Override
            public void deleteAllInBatch(Iterable<User> entities) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Long> longs) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public User getOne(Long aLong) {
                return null;
            }

            @Override
            public User getById(Long aLong) {
                return null;
            }

            @Override
            public User getReferenceById(Long aLong) {
                return null;
            }

            @Override
            public <S extends User> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public <S extends User> List<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public List<User> findAll() {
                return null;
            }

            @Override
            public List<User> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public <S extends User> S save(S entity) {
                return null;
            }

            @Override
            public Optional<User> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(User entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends User> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public List<User> findAll(Sort sort) {
                return null;
            }

            @Override
            public Page<User> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends User> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends User> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends User> boolean exists(Example<S> example) {
                return false;
            }

            @Override
            public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
                return null;
            }
        };
        // Tạo instance của CustomUserDetailsService với dummy repository
        CustomUserDetailsService customService = new CustomUserDetailsService(dummyUserRepo);
        try {
            // Gọi loadUserByUsername với email đúng
            UserDetails userDetails = customService.loadUserByUsername("test@example.com");
            System.out.println("UserDetails retrieved: " + userDetails);

            // Kiểm tra so sánh mật khẩu
            boolean match = encoder.matches(rawPassword, userDetails.getPassword());
            System.out.println("Password raw: " + rawPassword);
            System.out.println("Password : " + userDetails.getPassword());
            System.out.println("Password match: " + match);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
